/**
 * 
 */
package com.test.poc.sauceLabs.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		return webDriver;
	}

	/**
	 * Waits until the desired element (identified based on the provided {@link By}) is present and displayed.
	 * 
	 * @param webDriver
	 *            the {@link WebDriver} to be used
	 * @param by
	 *            the way to identify the desired element
	 * @return the element
	 */
	public static WebElement waitForElement(final WebDriver webDriver, final By by) {
		WebElement webElement = null;

		boolean displayed = false;

		while (webElement == null || !displayed) {
			webElement = isElementPresent(webDriver, by);
			displayed = webElement.isDisplayed();
		}

		return webElement;

	}

	/**
	 * Checks if an element (identified based on the provided {@link By}) is present
	 * 
	 * @param webDriver
	 *            the {@link WebDriver} to be used
	 * @param by
	 *            the way to identify the desired element
	 * @return the element if it is present
	 */
	public static WebElement isElementPresent(final WebDriver webDriver, final By by) {
		try {
			final WebElement element = webDriver.findElement(by);
			return element;
		} catch (final NoSuchElementException e) {
			return null;
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
		logger.info("Wating for iframes to load");
		while (!webDriverWaiting) {
			logger.info("waiting...");
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
					logger.info("number of current iframes [" + size + "]");
					if (size == expectedCount) {
						for (final WebElement iframe : iframes) {
							logger.info("iframe ID: [" + iframe.getAttribute("id") + "]");
						}
					}
					return (size == expectedCount);
				}
			});
		}
		logger.info("Iframes loaded completely");
	}

}
