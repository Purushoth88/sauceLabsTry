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
 * Collection of various utils methods used across tests
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
	 * @param capabilityConfiguraton
	 * @return
	 */
	public static DesiredCapabilities prepareDesiredCapabilities(CapabilityConfiguraton capabilityConfiguraton) {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities(capabilityConfiguraton.getBrowserName(), capabilityConfiguraton.getBrowserVersion(), Platform.valueOf(capabilityConfiguraton.getPlatform()));

		return desiredCapabilities;

	}

	/**
	 * @param capabilityConfiguraton
	 * @param authentication
	 * @return
	 * @throws MalformedURLException
	 */
	public static WebDriver prepareWebDriver(CapabilityConfiguraton capabilityConfiguraton, SauceOnDemandAuthentication authentication) throws MalformedURLException {
		WebDriver webDriver = null;

		DesiredCapabilities desiredCapabilities = TestUtil.prepareDesiredCapabilities(capabilityConfiguraton);

		webDriver = new RemoteWebDriver(new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"), desiredCapabilities);
		return webDriver;
	}

}
