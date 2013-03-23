package com.test.poc.sauceLabs.features;

import static com.test.poc.sauceLabs.util.flows.FlowsUtil.cabCheckoutWithExistingAccount;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.junit.Parallelized;
import com.test.poc.sauceLabs.core.model.CapabilityConfiguration;
import com.test.poc.sauceLabs.core.parent.ParametrizedParentTest;
import com.test.poc.sauceLabs.util.watchers.TestRuleWatcher;

/**
 * Parallelization tests.
 * 
 * @author Nicolae.Petridean
 * @author Ciprian I. Ileana
 */
@RunWith(Parallelized.class)
public class TestParalelizationFeatures extends ParametrizedParentTest {

	/**
	 * Selenium generic web driver.
	 */
	private WebDriver						webDriver;

	/**
	 * String buffer to hold validation errors.
	 */
	private final StringBuffer				verificationErrors			= new StringBuffer();

	/**
	 * Profile related configuration data
	 */
	private final CapabilityConfiguration	capabilityConfiguration;

	/**
	 * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds/fails.
	 */
	@Rule
	public TestRuleWatcher					resultReportingTestWatcher	= new TestRuleWatcher(this, authentication);

	/**
	 * Will be feed with data provided by
	 * 
	 * @param capabilityConfiguration
	 * @throws IOException
	 * @throws JSONException
	 */
	public TestParalelizationFeatures(final CapabilityConfiguration capabilityConfiguration) throws JSONException, IOException {
		super();
		this.capabilityConfiguration = capabilityConfiguration;
	}

	@Before
	public void setUp() throws Exception {
		webDriver = provideWebDriver(capabilityConfiguration);
		System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		sessionId = ((RemoteWebDriver) webDriver).getSessionId().toString();
	}

	/**
	 * Junit Parallel test .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParallel1() throws Exception {
		paralelTest();
	}

	/**
	 * Junit Parallel test .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParallel2() throws Exception {
		paralelTest();
	}

	/**
	 * Junit Parallel test .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParallel3() throws Exception {
		paralelTest();
	}

	/**
	 * parallel test body.
	 */
	private void paralelTest() {
		cabCheckoutWithExistingAccount(webDriver);
	}

	/**
	 * Method that runs after the actual test.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		webDriver.quit();
		final String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

}
