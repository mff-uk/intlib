package cz.cuni.xrg.intlib.frontend.auxiliaries;

import com.vaadin.ui.CustomComponent;

import cz.cuni.xrg.intlib.commons.DPUExecutive;
import cz.cuni.xrg.intlib.commons.configuration.Configuration;
import cz.cuni.xrg.intlib.commons.web.GraphicalExtractor;
import cz.cuni.xrg.intlib.commons.web.GraphicalLoader;
import cz.cuni.xrg.intlib.commons.web.GraphicalTransformer;

/**
 * Provide function that enable obtain Configuration dialog 
 * from module (DPU) in easy way.
 * 
 * @author Petyr
 *
 */
public class ModuleDialogGetter {

	/**
	 * Prevent from creating instance.
	 */
	private ModuleDialogGetter() {
		
	}
	
	/**
	 * Return configuration dialog for given DPU. 
	 * @param dpuExewcutive
	 * @param configuration 
	 * @return configuration dialog or null
	 */
	public static CustomComponent getDialog(DPUExecutive dpuExewcutive, Configuration config) {
		CustomComponent confComponent;
		// get DPU type, recast, get configuration component and return it
		switch(dpuExewcutive.getType()) {
		case EXTRACTOR:
			GraphicalExtractor graphExtract = (GraphicalExtractor)dpuExewcutive;
			confComponent = graphExtract.getConfigurationComponent(config);
			break;
		case LOADER:
			GraphicalLoader graphLoader = (GraphicalLoader)dpuExewcutive;
			confComponent = graphLoader.getConfigurationComponent(config);
			break;
		case TRANSFORMER:
			GraphicalTransformer graphTrans = (GraphicalTransformer)dpuExewcutive;
			confComponent = graphTrans.getConfigurationComponent(config);
			break;
		default:
			confComponent = null;
			break;
		}
		return confComponent;
	}
	
}