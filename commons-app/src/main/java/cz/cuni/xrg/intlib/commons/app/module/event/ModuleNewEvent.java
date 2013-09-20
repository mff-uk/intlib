package cz.cuni.xrg.intlib.commons.app.module.event;

/**
 * Event indicate that there is new directory in DPU's directory. So there
 * is possibility that new DPU has been loaded into system.
 * 
 * @author Petyr
 * 
 */
public class ModuleNewEvent extends ModuleEvent {

	/**
	 * DPU's relative directory name.
	 */
	private String directoryName;

	public ModuleNewEvent(Object source,
			String directoryName) {
		super(source);
		this.directoryName = directoryName;
	}

	public String getDirectoryName() {
		return directoryName;
	}
	
}