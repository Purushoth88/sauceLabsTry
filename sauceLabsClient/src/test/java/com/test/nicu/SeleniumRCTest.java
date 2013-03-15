package com.test.nicu;

import static junit.framework.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Simple {@link DefaultSelenium} test that demonstrates how to run your Selenium tests with <a href="http://saucelabs.com/ondemand">Sauce OnDemand</a>. *
 * 
 * @author Ross Rowe
 */
public class SeleniumRCTest {

	private DefaultSelenium	selenium;

	@Before
	public void setUp() throws Exception {

		final DefaultSelenium selenium = new DefaultSelenium("ondemand.saucelabs.com", 80, "{\"username\": \"martchouk\"," + "\"access-key\": \"87335815-89fd-4022-94e0-9c268f5991f9\"," + "\"os\": \"Windows 2003\","
				+ "\"browser\": \"firefox\"," + "\"browser-version\": \"7\"," + "\"name\": \"Testing Selenium 1 with Java on Sauce\"}", "http://saucelabs.com/");
		selenium.start();
		this.selenium = selenium;

	}

	@Test
	public void selenumRC() throws Exception {
		this.selenium.open("http://www.amazon.com");
		assertEquals("Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more", this.selenium.getTitle());
	}

	@After
	public void tearDown() throws Exception {
		this.selenium.stop();
	}

}
