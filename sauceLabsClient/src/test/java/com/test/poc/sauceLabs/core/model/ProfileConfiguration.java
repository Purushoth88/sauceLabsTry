/**
 * 
 */
package com.test.poc.sauceLabs.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all profile related configuration data.
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 * 
 */
public class ProfileConfiguration {

	private List<CapabilityConfiguraton>	capabilities	= new ArrayList<CapabilityConfiguraton>();

	/**
	 * @return the capabilities
	 */
	public List<CapabilityConfiguraton> getCapabilities() {
		return capabilities;
	}

}
