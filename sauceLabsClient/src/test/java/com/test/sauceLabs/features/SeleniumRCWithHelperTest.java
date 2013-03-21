package com.test.sauceLabs.features;

import static junit.framework.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import com.saucelabs.saucerest.SauceREST;
import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Simple test used to test integration with Selenium and SauceLabs
 * 
 * @author Nicolae.Petridean
 * @author Ciprian I. Ileana
 */
@RunWith(value = Parameterized.class)
@Ignore
public class SeleniumRCWithHelperTest implements SauceOnDemandSessionIdProvider {

	/**
	 * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key. To use the authentication supplied by environment variables or from an external file, use the no-arg
	 * {@link SauceOnDemandAuthentication} constructor.
	 */
	public SauceOnDemandAuthentication	authentication				= new SauceOnDemandAuthentication("martchouk", "87335815-89fd-4022-94e0-9c268f5991f9");

	/**
	 * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
	 */
	public @Rule
	SauceOnDemandTestWatcher			resultReportingTestWatcher	= new SauceOnDemandTestWatcher(this, authentication);

	/**
	 * JUnit Rule which will record the test name of the current test. This is referenced when creating the {@link DesiredCapabilities}, so that the Sauce Job is created with the test name.
	 */
	public @Rule
	TestName							testName					= new TestName();

	private DefaultSelenium				selenium;

	private String						sessionId;

	@Before
	public void setUp() throws Exception {
		final DefaultSelenium selenium = new DefaultSelenium("ondemand.saucelabs.com", 80, "{\"username\": \"" + authentication.getUsername() + "\","
				+ "\"access-key\": \"" + authentication.getAccessKey() + "\"," + "\"os\": \"Windows 2003\"," + "\"browser\": \"firefox\","
				+ "\"browser-version\": \"7\"," + "\"name\": \"Testing Selenium 1 with Java on Sauce\"}", "http://saucelabs.com/");
		selenium.start();
		this.selenium = selenium;
		this.sessionId = getSessionIdFromSelenium();
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	public String getSessionIdFromSelenium() {
		try {
			final Field commandProcessorField = DefaultSelenium.class.getDeclaredField("commandProcessor");
			commandProcessorField.setAccessible(true);
			final CommandProcessor commandProcessor = (CommandProcessor) commandProcessorField.get(selenium);
			final Field f = commandProcessor.getClass().getDeclaredField("sessionId");
			f.setAccessible(true);
			final Object id = f.get(commandProcessor);
			if (id != null) {
				return id.toString();
			}
		} catch (final NoSuchFieldException e) {

		} catch (final IllegalAccessException e) {

		}
		return null;

	}

	@Test
	public void seleniumRCWithHelper() throws Exception {
		this.selenium.open("http://www.amazon.com");
		assertEquals("Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more", this.selenium.getTitle());

		final SauceREST restApi = new SauceREST("martchouk", "87335815-89fd-4022-94e0-9c268f5991f9");
		restApi.jobFailed(sessionId);
	}

	@After
	public void tearDown() throws Exception {
		this.selenium.stop();
	}

}
