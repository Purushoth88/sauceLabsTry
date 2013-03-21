package com.test.sauceLabs.features;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Simple test used to test integration with Selenium and SauceLabs
 * 
 * @author Nicolae.Petridean
 * @author Ciprian I. Ileana
 */
@RunWith(value = Parameterized.class)
@Ignore
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
