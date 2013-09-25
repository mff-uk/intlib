package cz.cuni.xrg.intlib.backend.execution.pipeline;

import java.util.Map;

import org.springframework.core.Ordered;

import cz.cuni.xrg.intlib.backend.context.Context;
import cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecution;
import cz.cuni.xrg.intlib.commons.app.pipeline.graph.DependencyGraph;
import cz.cuni.xrg.intlib.commons.app.pipeline.graph.Node;

/**
 * Provide action that should be perform after pipeline execution. 
 * The {@link PostExecutor}s are used as a singletons, so they
 * must be able to run concurrently on multiple instances. 
 *  
 * The PreExecutors are executed in order that is defined by {@link Ordered}
 * 
 * @author Petyr
 * 
 */
public interface PostExecutor extends Ordered {

	/**
	 * Should perform post-execution actions. If return false then the execution
	 * is cancelled.
	 * 
	 * @param execution
	 * @param contexts
	 * @param graph Dependency graph used for execution.
	 * @return
	 */
	public boolean postAction(PipelineExecution execution,
			Map<Node, Context> contexts,
			DependencyGraph graph);

}
