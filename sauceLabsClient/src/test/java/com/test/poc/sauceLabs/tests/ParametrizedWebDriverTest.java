/**
 * 
 */
package com.test.poc.sauceLabs.tests;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.test.poc.sauceLabs.core.model.CapabilityConfiguration;
import com.test.poc.sauceLabs.core.parent.ParametrizedParentTest;
import com.test.poc.sauceLabs.util.watchers.SauceRestRule;

/**
 * Sample {@link RemoteWebDriver} test that demonstrates how to run your Selenium tests with <a href="http://saucelabs.com/ondemand">Sauce OnDemand</a>. *
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 */
@RunWith(Parameterized.class)
public class ParametrizedWebDriverTest extends ParametrizedParentTest {

	/**
	 * class logger.
	 */
	private static final Logger				logger						= Logger.getLogger(ParametrizedWebDriverTest.class);

	/**
	 * The {@link WebDriver} to be used in the test
	 */
	private WebDriver						webDriver					= null;

	/**
	 * Profile related configuration data
	 */
	private final CapabilityConfiguration	capabilityConfiguration;

	/**
	 * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds/fails.
	 */
	@Rule
	public SauceRestRule					resultReportingTestWatcher	= new SauceRestRule(this, authentication);

	/**
	 * Will be feed with data provided by
	 * 
	 * @param capabilityConfiguration
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public ParametrizedWebDriverTest(final CapabilityConfiguration capabilityConfiguration) throws JSONException, IOException {
		super();
		this.capabilityConfiguration = capabilityConfiguration;
	}

	/**
	 * Method that runs before the actual test.
	 * 
	 * @throws MalformedURLException
	 */
	@Before
	public void setup() throws MalformedURLException {
		logger.info("ParametrizedWebDriverTest - setup");
		webDriver = provideWebDriver(capabilityConfiguration);
		sessionId = ((RemoteWebDriver) webDriver).getSessionId().toString();
	}

	/**
	 * Method that runs after the actual test.
	 */
	@After
	public void tearDown() {
		logger.info("ParametrizedWebDriverTest - tearDown");
		webDriver.quit();
	}

	/**
	 * The actual test
	 */
	@Test
	public void sampleTest() {
		logger.info("ParametrizedWebDriverTest - sampleTest");
	}

}
