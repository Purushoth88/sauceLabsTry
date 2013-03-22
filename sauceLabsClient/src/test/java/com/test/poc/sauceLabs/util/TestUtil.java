/**
 * 
 */
package com.test.poc.sauceLabs.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.test.poc.sauceLabs.core.model.CapabilityConfiguraton;

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
	public static DesiredCapabilities prepareDesiredCapabilities(final CapabilityConfiguraton capabilityConfiguraton) {
		final DesiredCapabilities desiredCapabilities = new DesiredCapabilities(capabilityConfiguraton.getBrowserName(),
				capabilityConfiguraton.getBrowserVersion(), Platform.valueOf(capabilityConfiguraton.getPlatform()));

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
	public static WebDriver prepareWebDriver(final CapabilityConfiguraton capabilityConfiguraton, final SauceOnDemandAuthentication authentication)
			throws MalformedURLException {
		WebDriver webDriver = null;

		final DesiredCapabilities desiredCapabilities = TestUtil.prepareDesiredCapabilities(capabilityConfiguraton);

		webDriver = new RemoteWebDriver(new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey()
				+ "@ondemand.saucelabs.com:80/wd/hub"), desiredCapabilities);
		return webDriver;
	}

	/**
	 * Utility method to check text present on page.
	 * 
	 * @param webDriver
	 *            : driver to run the test check
	 * @param by
	 *            : By static class.
	 * @return
	 */
	public static boolean isElementPresent(final By by, final WebDriver webDriver) {
		try {
			webDriver.findElement(by);
			return true;
		} catch (final NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Utility method to check text present on page.
	 * 
	 * @param text
	 * @param webDriver
	 * @return
	 */
	public static boolean verifyTextPresent(final String text, final WebDriver webDriver) {
		return webDriver.findElement(By.tagName("body")).getText().contains(text);
	}

	/**
	 * Utility method to read Alert test, and closes it afterwards.
	 * 
	 * @param acceptNextAlert
	 * @param webDriver
	 * @return
	 */
	public static String closeAlertAndGetItsText(boolean acceptNextAlert, final WebDriver webDriver) {
		try {
			final Alert alert = webDriver.switchTo().alert();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alert.getText();
		} finally {
			acceptNextAlert = true;
		}
	}

	/**
	 * Utility method to read the number of elements of a specific kind.
	 * 
	 * @param webDriver
	 * @param element
	 * @return
	 */
	public static int getNumberOfElements(final WebDriver webDriver, final String element) {
		return webDriver.findElements(By.tagName(element)).size();
	}

}
