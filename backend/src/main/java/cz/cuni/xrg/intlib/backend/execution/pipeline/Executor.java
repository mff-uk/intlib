package cz.cuni.xrg.intlib.backend.execution.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import cz.cuni.xrg.intlib.backend.context.Context;
import cz.cuni.xrg.intlib.backend.pipeline.event.PipelineAbortedEvent;
import cz.cuni.xrg.intlib.backend.pipeline.event.PipelineFinished;
import cz.cuni.xrg.intlib.commons.app.conf.AppConfig;
import cz.cuni.xrg.intlib.commons.app.conf.ConfigProperty;
import cz.cuni.xrg.intlib.commons.app.execution.log.LogFacade;
import cz.cuni.xrg.intlib.commons.app.execution.log.LogMessage;
import cz.cuni.xrg.intlib.commons.app.pipeline.Pipeline;
import cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecution;
import cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecutionStatus;
import cz.cuni.xrg.intlib.commons.app.pipeline.PipelineFacade;
import cz.cuni.xrg.intlib.commons.app.pipeline.graph.DependencyGraph;
import cz.cuni.xrg.intlib.commons.app.pipeline.graph.Node;

/**
 * Execute given pipeline. The {@link Executor} must be bind to the certain
 * {@link PipelineExecution} by calling {@link #bind(PipelineExecution)} before
 * any future use.
 * 
 * @author Petyr
 * 
 */
public class Executor implements Runnable {

	/**
	 * Logger class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Executor.class);

	/**
	 * Publisher instance for publishing pipeline execution events.
	 */
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	/**
	 * Bean factory used to create beans for single pipeline execution.
	 */
	@Autowired
	private BeanFactory beanFactory;

	/**
	 * Pipeline facade.
	 */
	@Autowired
	private PipelineFacade pipelineFacade;

	/**
	 * Log facade.
	 */
	@Autowired
	private LogFacade logFacade;

	/**
	 * Application's configuration.
	 */
	@Autowired
	private AppConfig appConfig;

	/**
	 * PipelineExecution record, determine pipeline to run.
	 */
	private PipelineExecution execution;
	
	/**
	 * Store context related to Nodes (DPUs).
	 */
	private Map<Node, Context> contexts = new HashMap<>();

	/**
	 * End time of last successful pipeline execution.
	 */
	private Date lastSuccessfulExTime;

	/**
	 * Bind {@link Executor} to the given {@link PipelineExecution}. Also update
	 * the {@link PipelineExecution}'s state.
	 * 
	 * @param execution
	 */
	public void bind(PipelineExecution execution) {
		this.execution = execution;
		contexts = new HashMap<>();
		// update state
		this.execution.setExecutionStatus(PipelineExecutionStatus.RUNNING);
		pipelineFacade.save(this.execution);
		// load last execution time
		Date lastSucess = pipelineFacade.getLastExecTime(
				execution.getPipeline(),
				PipelineExecutionStatus.FINISHED_SUCCESS);
		Date lastSucessWarn = pipelineFacade.getLastExecTime(
				execution.getPipeline(),
				PipelineExecutionStatus.FINISHED_WARNING);
		// null check ..
		if (lastSucess == null) {
			this.lastSuccessfulExTime = lastSucessWarn;
		} else if (lastSucessWarn == null) {
			this.lastSuccessfulExTime = lastSucess;
		} else {
			// get last successful execution time
			this.lastSuccessfulExTime = lastSucess.after(lastSucessWarn)
					? lastSucess
					: lastSucessWarn;
		}		
	}

	/**
	 * Should be called in case that the execution failed. Does not save the
	 * {@link PipelineExecution} into database.
	 */
	private void executionFailed() {
		execution.setExecutionStatus(PipelineExecutionStatus.FAILED);
	}

	/**
	 * Should be called in case that the execution has finished without error.
	 * Does not save the {@link PipelineExecution} into database.
	 */
	private void executionSuccessful() {
		// update state -> check logs
		Set<Level> levels = new HashSet<>(2);
		levels.add(Level.WARN);
		levels.add(Level.ERROR);
		levels.add(Level.FATAL);
		if (logFacade.existLogs(execution, levels)) {
			execution
					.setExecutionStatus(PipelineExecutionStatus.FINISHED_WARNING);
		} else {
			execution
					.setExecutionStatus(PipelineExecutionStatus.FINISHED_SUCCESS);
		}
	}

	/**
	 * Try to delete directory in execution directory. If error occur then is
	 * logged but otherwise ignored.
	 * 
	 * @param directory Relative path from execution directory.
	 */
	private void deleteDirectory(String directoryPath) {
		final String generalWorking = appConfig
				.getString(ConfigProperty.GENERAL_WORKINGDIR);
		File directory = new File(generalWorking, directoryPath);
		try {
			FileUtils.deleteDirectory(directory);
		} catch (IOException e) {
			LOG.error("Can't delete directory after execution", e);
		}
	}

	/**
	 * Delete debug data that has been created during pipeline execution.
	 */
	private void deleteDebugDate() {
		// delete working directory
		// the sub directories should be already deleted by DPU's
		deleteDirectory(execution.getContext().getWorkingPath());
	}

	/**
	 * Do cleanup work after pipeline execution. Also delete the execution
	 * directory if the pipeline does not run in debug mode.
	 */
	private void cleanup() {
		LOG.debug("Clean up");
		// release/delete all contexts
		for (Context item : contexts.values()) {
			if (execution.isDebugging()) {
				// just release leave
				item.release();
			} else {
				// delete data ..
				item.delete();
			}
		}
		if (execution.isDebugging()) {
			return;
		} else {
			deleteDebugDate();
		}
		// result directory is never deleted
	}

	/**
	 * Prepare and return instance of {@link DependencyGraph}.
	 * @return
	 */
	private DependencyGraph prepareDependencyGraph() {
		DependencyGraph dependencyGraph = null;
		final Pipeline pipeline = execution.getPipeline();
		// if in debug mode then pass the final DPU
		if (execution.isDebugging() && execution.getDebugNode() != null) {
			dependencyGraph = new DependencyGraph(pipeline.getGraph(),
					execution.getDebugNode());
		} else {
			dependencyGraph = new DependencyGraph(pipeline.getGraph());
		}
		return dependencyGraph;
	}
	
	/**
	 * Run the execution.
	 */
	private void execute() {
		boolean executionFailed = false;
		// add marker to logs from this thread -> both must be specified !!
		MDC.put(LogMessage.MDPU_EXECUTION_KEY_NAME,
				Long.toString(execution.getId()));
		LOG.debug("Started");
		// set start time
		execution.setStart(new Date());
		// contextInfo is in pipeline so by saving pipeline we also save context
		pipelineFacade.save(execution);
		// get dependency graph
		DependencyGraph dependencyGraph = prepareDependencyGraph();
		// execute each node
		for (Node node : dependencyGraph) {
			
			// check for user request to stop execution -> we need new instance
			if (pipelineFacade.getExecution(execution.getId()).getStop()) {
				// stop the execution
				eventPublisher.publishEvent(
						new PipelineAbortedEvent(execution, this));
				executionFailed = true;
				break;
			}
			
			// put dpuInstance id to MDC, so we can identify logs related to the
			// dpuInstance
			MDC.put(LogMessage.MDC_DPU_INSTANCE_KEY_NAME,
					Long.toString(node.getDpuInstance().getId()));			
			
			cz.cuni.xrg.intlib.backend.execution.dpu.Executor dpuExecutor = 
					beanFactory.getBean(cz.cuni.xrg.intlib.backend.execution.dpu.Executor.class);
			dpuExecutor.bind(node, dependencyGraph, contexts, execution, lastSuccessfulExTime);
			
			// TODO Petyr: run in parallel thread and check for abort
			dpuExecutor.run();

			// ..
			if (dpuExecutor.executionFailed()) {
				// continue
				executionFailed = true;
				break;
			}
			MDC.remove(LogMessage.MDC_DPU_INSTANCE_KEY_NAME);
		}
		// ending ..		
		// set time then the pipeline's execution finished
		execution.setEnd(new Date());
		if (executionFailed) {
			LOG.debug("Execution failed");
			executionFailed();
		} else {
			LOG.debug("Execution finished");
			executionSuccessful();
			// publish information for the rest of the application
			eventPublisher.publishEvent(new PipelineFinished(execution, this));			
		}
		// save the execution for the last time
		pipelineFacade.save(execution);
		// do clean/up
		cleanup();
		// clear all threads markers
		MDC.clear();		
	}

	@Override
	public void run() {
		execute();
	}

}
