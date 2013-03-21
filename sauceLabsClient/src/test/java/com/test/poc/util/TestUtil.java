/**
 * 
 */
package com.test.poc.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.test.poc.core.model.CapabilityConfiguraton;

/**
 * Collection of various utility methods used across tests
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 * 
 */
public class TestUtil {

	/**
	 * Prevent instantiation of utils class
	 */
	private TestUtil() {
	}

	/**
	 * Dynamically prepares a {@link DesiredCapabilities} based on the provided {@link CapabilityConfiguraton}
	 * 
	 * @param capabilityConfiguraton
	 * @return the dynamically created {@link DesiredCapabilities}
	 */
	public static DesiredCapabilities prepareDesiredCapabilities(CapabilityConfiguraton capabilityConfiguraton) {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities(capabilityConfiguraton.getBrowserName(), capabilityConfiguraton.getBrowserVersion(), Platform.valueOf(capabilityConfiguraton.getPlatform()));

		return desiredCapabilities;

	}

	/**
	 * Dynamically prepares a {@link WebDriver} based on the provided {@link CapabilityConfiguraton} and {@link SauceOnDemandAuthentication}
	 * 
	 * @param capabilityConfiguraton
	 *            profile related configuration data
	 * @param authentication
	 *            the Sauce OnDemand authentication
	 * @return the dynamically created {@link WebDriver}
	 * @throws MalformedURLException
	 *             if the dynamically created URL when initializing the {@link RemoteWebDriver} specifies an unknown protocol.
	 */
	public static WebDriver prepareWebDriver(CapabilityConfiguraton capabilityConfiguraton, SauceOnDemandAuthentication authentication) throws MalformedURLException {
		WebDriver webDriver = null;

		DesiredCapabilities desiredCapabilities = TestUtil.prepareDesiredCapabilities(capabilityConfiguraton);

		webDriver = new RemoteWebDriver(new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"), desiredCapabilities);
		return webDriver;
	}

}
