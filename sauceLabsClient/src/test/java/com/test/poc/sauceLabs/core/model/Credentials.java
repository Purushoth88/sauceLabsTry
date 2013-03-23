/**
 * 
 */
package com.test.poc.sauceLabs.core.model;

/**
 * Holds all credentials related configuration data.
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 * 
 */
public class Credentials {

	/**
	 * The user name
	 */
	private String	userName;

	/**
	 * The access key
	 */
	private String	accessKey;

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * @param accessKey
	 *            the accessKey to set
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
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
		result = prime * result + ((accessKey == null) ? 0 : accessKey.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		Credentials other = (Credentials) obj;
		if (accessKey == null) {
			if (other.accessKey != null) {
				return false;
			}
		} else if (!accessKey.equals(other.accessKey)) {
			return false;
		}
		if (userName == null) {
			if (other.userName != null) {
				return false;
			}
		} else if (!userName.equals(other.userName)) {
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
		return "Credentials [userName=" + userName + ", accessKey=" + accessKey + "]";
	}

}
