/**
 * 
 */
package com.test.poc.sauceLabs.core.model;

/**
 * Holds all profile related configuration data.
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 * 
 */
public class CapabilityConfiguraton {

	/**
	 * The browser name
	 */
	private String	browserName;

	/**
	 * The browser version
	 */
	private String	browserVersion;

	/**
	 * The platform
	 */
	private String	platform;

	/**
	 * @return the browserName
	 */
	public String getBrowserName() {
		return browserName;
	}

	/**
	 * @param browserName
	 *            the browserName to set
	 */
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	/**
	 * @return the browserVersion
	 */
	public String getBrowserVersion() {
		return browserVersion;
	}

	/**
	 * @param browserVersion
	 *            the browserVersion to set
	 */
	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform
	 *            the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CapabilityConfiguraton [browserName=" + browserName + ", browserVersion=" + browserVersion + ", platform=" + platform + "]";
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
		result = prime * result + ((browserName == null) ? 0 : browserName.hashCode());
		result = prime * result + ((browserVersion == null) ? 0 : browserVersion.hashCode());
		result = prime * result + ((platform == null) ? 0 : platform.hashCode());
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
		CapabilityConfiguraton other = (CapabilityConfiguraton) obj;
		if (browserName == null) {
			if (other.browserName != null) {
				return false;
			}
		} else if (!browserName.equals(other.browserName)) {
			return false;
		}
		if (browserVersion == null) {
			if (other.browserVersion != null) {
				return false;
			}
		} else if (!browserVersion.equals(other.browserVersion)) {
			return false;
		}
		if (platform == null) {
			if (other.platform != null) {
				return false;
			}
		} else if (!platform.equals(other.platform)) {
			return false;
		}
		return true;
	}

}
