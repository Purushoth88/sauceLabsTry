/**
 * 
 */
package com.test.poc;

import java.net.MalformedURLException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.junit.Parallelized;
import com.test.poc.core.model.CapabilityConfiguraton;
import com.test.poc.core.parent.ParametrizedParentTest;

/**
 * Simple {@link RemoteWebDriver} test that demonstrates how to run your Selenium tests with <a href="http://saucelabs.com/ondemand">Sauce OnDemand</a>. *
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 */
@RunWith(Parallelized.class)
public class ParametrizedWebDriverTest extends ParametrizedParentTest {

	/**
	 * class logger.
	 */
	private static final Logger				logger						= Logger.getLogger(ParametrizedWebDriverTest.class);

	/**
	 * 
	 */
	private WebDriver						webDriver					= null;

	/**
	 * 
	 */
	private final CapabilityConfiguraton	capabilityConfiguraton;
	/**
	 * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
	 */
	@Rule
	public Watcher							resultReportingTestWatcher	= new Watcher(this, authentication);

	public ParametrizedWebDriverTest(final CapabilityConfiguraton capabilityConfiguraton) {
		this.capabilityConfiguraton = capabilityConfiguraton;
	}

	/**
	 * @throws MalformedURLException
	 */
	@Before
	public void setup() throws MalformedURLException {
		logger.info("ParametrizedWebDriverTest - setup");
		webDriver = provideWebDriver(capabilityConfiguraton);
		sessionId = ((RemoteWebDriver) webDriver).getSessionId().toString();
	}

	/**
	 * 
	 */
	@After
	public void tearDown() {
		logger.info("ParametrizedWebDriverTest - tearDown");
		webDriver.quit();
	}

	/**
	 * 
	 */
	@Test
	public void sampleTest() {
		logger.info("ParametrizedWebDriverTest - sampleTest");
		final boolean b = true;
		Assert.assertTrue(!b);
	}
}
