/**
 * 
 */
package com.test.poc;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.junit.SauceOnDemandTestWatcher;
import com.test.poc.core.model.CapabilityConfiguraton;
import com.test.poc.core.parent.ParametrizedParentTest;

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
	private static final Logger		logger						= Logger.getLogger(ParametrizedWebDriverTest.class);

	/**
	 * The {@link WebDriver} to be used in the test
	 */
	private WebDriver				webDriver					= null;

	/**
	 * Profile related configuration data
	 */
	private CapabilityConfiguraton	capabilityConfiguraton;

	/**
	 * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds/fails.
	 */
	@Rule
	public SauceOnDemandTestWatcher	resultReportingTestWatcher	= new SauceOnDemandTestWatcher(this, authentication);

	/**
	 * Will be feed with data provided by
	 * 
	 * @param capabilityConfiguraton
	 */
	public ParametrizedWebDriverTest(CapabilityConfiguraton capabilityConfiguraton) {
		this.capabilityConfiguraton = capabilityConfiguraton;
	}

	/**
	 * Method that runs before the actual test.
	 * 
	 * @throws MalformedURLException
	 */
	@Before
	public void setup() throws MalformedURLException {
		logger.info("ParametrizedWebDriverTest - setup");
		webDriver = provideWebDriver(capabilityConfiguraton);
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
