/**
 * 
 */
package com.test.poc.sauceLabs.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.test.poc.sauceLabs.core.model.CapabilityConfiguration;

/**
 * Collection of various utility methods used across tests
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 * 
 */
public class TestUtil {

	/**
	 * class logger.
	 */
	private static final Logger	logger	= Logger.getLogger(TestUtil.class);

	/**
	 * Prevent instantiation of utility class
	 */
	private TestUtil() {
	}

	/**
	 * Dynamically prepares a {@link DesiredCapabilities} based on the provided {@link CapabilityConfiguration}
	 * 
	 * @param capabilityConfiguration
	 * @return the dynamically created {@link DesiredCapabilities}
	 */
	public static DesiredCapabilities prepareDesiredCapabilities(final CapabilityConfiguration capabilityConfiguration) {
		final DesiredCapabilities desiredCapabilities = new DesiredCapabilities(capabilityConfiguration.getBrowserName(), capabilityConfiguration.getBrowserVersion(), Platform.valueOf(capabilityConfiguration.getPlatform()));

		return desiredCapabilities;
	}

	/**
	 * Dynamically prepares a {@link WebDriver} based on the provided {@link CapabilityConfiguration} and {@link SauceOnDemandAuthentication}
	 * 
	 * @param capabilityConfiguration
	 *            profile related configuration data
	 * @param authentication
	 *            the Sauce OnDemand authentication
	 * @return the dynamically created {@link WebDriver}
	 * @throws MalformedURLException
	 *             if the dynamically created URL when initializing the {@link RemoteWebDriver} specifies an unknown protocol.
	 */
	public static WebDriver prepareWebDriver(final CapabilityConfiguration capabilityConfiguration, final SauceOnDemandAuthentication authentication) throws MalformedURLException {
		WebDriver webDriver = null;

		final DesiredCapabilities desiredCapabilities = TestUtil.prepareDesiredCapabilities(capabilityConfiguration);

		webDriver = new RemoteWebDriver(new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"), desiredCapabilities);
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
		boolean isElementPresent = false;

		try {
			webDriver.findElement(by);
			isElementPresent = true;
		} catch (final NoSuchElementException noSuchElementException) {
			logger.debug("Element wasn't found [" + noSuchElementException.getMessage() + "]");
		}

		return isElementPresent;
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
	 * Utility method to determine the total number of elements ({@link WebElements}), identified through a given tagName, within the current page.
	 * 
	 * @param webDriver
	 *            {@link WebDriver} to be used
	 * @param tagName
	 *            The element's tagName
	 * @return the number of elements ({@link WebElements}) found
	 */
	public static int getNumberOfElements(final WebDriver webDriver, final String tagName) {
		return webDriver.findElements(By.tagName(tagName)).size();
	}

	/**
	 * Utility method that causes your {@link WebDriver} to wait until the specified number of iFrames are present
	 * 
	 * @param webDriver
	 *            {@link WebDriver} to be used
	 * @param expectedCount
	 *            the number of occurrences
	 */
	public static void waitForIframes(final WebDriver webDriver, final int expectedCount) {
		Boolean webDriverWaiting = false;

		while (!webDriverWaiting) {
			logger.debug("waiting...");
			webDriverWaiting = new WebDriverWait(webDriver, 10).until(new ExpectedCondition<Boolean>() {

				/**
				 * @param driver
				 *            {@link WebDriver} to be used
				 * @return true if all required iframes are present, false otherwise
				 */
				@Override
				public Boolean apply(final WebDriver driver) {
					final List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
					final Integer size = iframes.size();
					logger.debug("number of current iframes [" + size + "]");
					if (size == expectedCount) {
						for (final WebElement iframe : iframes) {
							logger.debug("iframe ID: [" + iframe.getAttribute("id") + "]");
						}
					}
					return (size == expectedCount);
				}
			});
		}
	}

}
