package cz.cuni.xrg.intlib.commons.dpu;

import cz.cuni.xrg.intlib.commons.data.DataUnitException;

/**
 * Interface for DPU.
 * 
 * @see {@link DPUContext}
 * @see {@link DPUException}
 * @author Petyr
 * 
 */
public interface DPU {

	/**
	 * Execute the DPU. If any exception is thrown then the DPU execution is 
	 * considered to failed.
	 * 
	 * @param context DPU's context.
	 * @throws DPUException
	 * @throws DataUnitException
	 * @throws InterruptedException
	 */
	public void execute(DPUContext context)
			throws DPUException,
				DataUnitException,
				InterruptedException;

	/**
	 * Is called if and only if the @{link #execute} executive thread is
	 * interrupted. This method should clean the DPU content.
	 */
	public void cleanUp();
}
