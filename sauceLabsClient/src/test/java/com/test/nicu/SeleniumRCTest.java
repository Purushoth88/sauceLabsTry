package com.test.nicu;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.saucerest.SauceREST;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Simple {@link DefaultSelenium} test that demonstrates how to run your Selenium tests with <a href="http://saucelabs.com/ondemand">Sauce OnDemand</a>. *
 * 
 * @author Ross Rowe
 */
public class SeleniumRCTest {

	@Test
	public void testTest() throws IOException {
		// run against Firefox v12 on Windows XP
		final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setPlatform(Platform.XP);
		capabilities.setCapability("version", "12");
		final WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
		driver.get("http://www.amazon.com");
		driver.quit();

		final SauceREST restApi = new SauceREST("martchouk", "87335815-89fd-4022-94e0-9c268f5991f9");
		restApi.jobFailed("test");
	}

	@Test
	public void testTest2() throws MalformedURLException {
		// run against Firefox v12 on Windows XP
		final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setPlatform(Platform.LINUX);
		capabilities.setCapability("version", "6.0.2");
		final WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
		driver.get("http://www.amazon.com");
		driver.quit();
	}

	@Test
	public void testTest3() throws MalformedURLException {
		// run against Firefox v12 on Windows XP
		final DesiredCapabilities capabilities = DesiredCapabilities.android();
		capabilities.setPlatform(Platform.LINUX);
		capabilities.setCapability("version", "4");
		final WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
		driver.get("http://www.amazon.com");
		driver.quit();
	}

	/*
	 * @After public void tearDown() throws Exception { this.selenium.stop(); }
	 */

}
