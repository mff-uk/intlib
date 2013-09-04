package cz.cuni.xrg.commons.module.dpu;

import org.junit.Test;

import cz.cuni.xrg.intlib.commons.configuration.ConfigException;
import cz.cuni.xrg.intlib.commons.configuration.DPUConfigObject;
import cz.cuni.xrg.intlib.commons.module.dpu.ConfigurableBase;

import static org.junit.Assert.*;

/**
 * Test suite for {@link ConfigurableBase} class.
 * 
 * @author Petyr
 *
 */
public class ConfigurableBaseInstanceTest extends ConfigurableBase<ConfigDummy> {

	public ConfigurableBaseInstanceTest() {
		super(ConfigDummy.class);
	}
	
	/**
	 * Test that initial configuration has been set propoerly.
	 */
	@Test 
	public void initialConfigNotNull() {
		assertNotNull(config);
	}
	
	/**
	 * Configuration is not changed on configure(null).
	 */
	@Test
	public void nullSet() throws ConfigException {
		DPUConfigObject oldConfig = config;
		assertNotNull(oldConfig);
		
		this.configure(null);
		
		assertEquals(oldConfig, config);
	}
}
