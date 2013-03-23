/**
 * 
 */
package com.test.poc.sauceLabs.core.model;

import java.util.ArrayList;
import java.util.List;

import com.saucelabs.common.SauceOnDemandAuthentication;

/**
 * Holds all profile related configuration data.
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 * 
 */
public class ProfileConfiguration {

	/**
	 * List of capabilities present in the profile.
	 */
	private List<CapabilityConfiguration>	capabilities	= new ArrayList<CapabilityConfiguration>();

	/**
	 * Credentials that will be used to create {@link SauceOnDemandAuthentication}
	 */
	private Credentials						credentials;

	/**
	 * @return the capabilities
	 */
	public List<CapabilityConfiguration> getCapabilities() {
		return capabilities;
	}

	/**
	 * @return the credentials
	 */
	public Credentials getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials
	 *            the credentials to set
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capabilities == null) ? 0 : capabilities.hashCode());
		result = prime * result + ((credentials == null) ? 0 : credentials.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProfileConfiguration other = (ProfileConfiguration) obj;
		if (capabilities == null) {
			if (other.capabilities != null) {
				return false;
			}
		} else if (!capabilities.equals(other.capabilities)) {
			return false;
		}
		if (credentials == null) {
			if (other.credentials != null) {
				return false;
			}
		} else if (!credentials.equals(other.credentials)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProfileConfiguration [capabilities=" + capabilities + ", credentials=" + credentials + "]";
	}

}
