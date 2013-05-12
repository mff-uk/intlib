package cz.cuni.xrg.intlib.backend.context.impl;

import cz.cuni.xrg.intlib.backend.context.ContextException;
import cz.cuni.xrg.intlib.backend.context.TransformContext;
import cz.cuni.xrg.intlib.backend.data.DataUnitFactoryImpl;
import cz.cuni.xrg.intlib.backend.dpu.event.DPUMessage;
import cz.cuni.xrg.intlib.commons.ProcessingContext;
import cz.cuni.xrg.intlib.commons.DpuType;
import cz.cuni.xrg.intlib.commons.app.dpu.DPUInstance;
import cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecution;
import cz.cuni.xrg.intlib.commons.data.DataUnit;
import cz.cuni.xrg.intlib.commons.data.DataUnitFactory;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEventPublisher;

/**
 *
 * @author Petyr
 */
public class TransformContextImpl implements TransformContext {

	/**
	 * Unique context id.
	 */
	private String id;	
	
	/**
	 * Context input data units.
	 */
    private List<DataUnit> intputs = new LinkedList<DataUnit>();
    
    /**
     * Context output data units.
     */
    private List<DataUnit> outputs = new LinkedList<DataUnit>();
    
    /**
     * Custom data holder.
     */
    private Map<String, Object> customData;

    /**
     * True id the related DPU should be run in debug mode.
     */
    private boolean isDebugging = false;
    
    /**
     * PipelineExecution. The one who caused
     * run of this DPU.
     */
	private PipelineExecution execution;

	/**
	 * Instance of DPU for which is this context.
	 */
	private DPUInstance dpuInstance;
    
	/**
	 * Application event publisher used to publish messages from DPU.
	 */
	private ApplicationEventPublisher eventPublisher;	
	
	/**
	 * Used factory.
	 */
	private DataUnitFactoryImpl dataUnitFactory;
	
	/**
	 * Path to the directory that can be used by this context.
	 */
	private File contextDirectory;
	
	public TransformContextImpl(String id, PipelineExecution execution, DPUInstance dpuInstance, 
			ApplicationEventPublisher eventPublisher, File contextDirectory) {
		this.id = id;
		this.intputs = new LinkedList<DataUnit>();
		this.outputs = new LinkedList<DataUnit>();
		this.customData = new HashMap<String, Object>();
		this.isDebugging = execution.isDebugging();
		this.execution = execution;
		this.dpuInstance = dpuInstance;
		this.eventPublisher = eventPublisher;
		this.dataUnitFactory = new DataUnitFactoryImpl(this.id, new File(contextDirectory, "DataUnits") );
	}

	@Override
	public List<DataUnit> getInputs() {
		return intputs;
	}

	@Override
	public List<DataUnit> getOutputs() {
		return outputs;
	}

	@Override
	public void addOutputDataUnit(DataUnit dataUnit) {
		outputs.add(dataUnit);
	}

	@Override
	public String storeData(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object loadData(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessage(DpuType type, String shortMessage) {
		eventPublisher.publishEvent(new DPUMessage(shortMessage, "", type, this, this) );
	}

	@Override
	public void sendMessage(DpuType type, String shortMessage, String fullMessage) {
		eventPublisher.publishEvent(new DPUMessage(shortMessage, fullMessage, type, this, this) );		
	}

	@Override
	public void storeDataForResult(String id, Object object) {
		// TODO Auto-generated method stub		
	}

	@Override
	public boolean isDebugging() {
		return isDebugging;
	}

	@Override
	public Map<String, Object> getCustomData() {
		return customData;
	}

	@Override
	public PipelineExecution getPipelineExecution() {
		return execution;
	}

	@Override
	public DPUInstance getDPUInstance() {
		return dpuInstance;
	}

	@Override
	public DataUnitFactory getDataUnitFactory() {
		return dataUnitFactory;
	} 	
	
	@Override
	public void addSource(ProcessingContext context) throws ContextException {
		// TODO Auto-generated method stub		
	}
      
}