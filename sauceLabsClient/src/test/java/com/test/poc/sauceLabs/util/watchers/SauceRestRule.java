package com.test.poc.sauceLabs.util.watchers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.common.Utils;
import com.test.poc.sauceLabs.util.rest.SauceRestUtil;

/**
 * Test Watcher used to enrich test reports with test data from SauceLabs.
 * 
 * @author Nicolae.Petridean
 * @author Ciprian I. Ileana
 */
public class SauceRestRule extends TestWatcher {

	/**
	 * class logger.
	 */
	private static final Logger						logger	= Logger.getLogger(SauceRestRule.class);

	/**
	 * The underlying {@link com.saucelabs.common.SauceOnDemandSessionIdProvider} instance which contains the Selenium session id. This is typically the unit test being executed.
	 */
	private final SauceOnDemandSessionIdProvider	sessionIdProvider;

	/**
	 * The instance of the Sauce OnDemand Java REST API client.
	 */
	private final SauceRestUtil						sauceRestUtil;

	/**
	 * @param sessionIdProvider
	 */
	public SauceRestRule(final SauceOnDemandSessionIdProvider sessionIdProvider) {
		this(sessionIdProvider, new SauceOnDemandAuthentication());
	}

	/**
	 * @param sessionIdProvider
	 * @param authentication
	 */
	public SauceRestRule(final SauceOnDemandSessionIdProvider sessionIdProvider, final SauceOnDemandAuthentication authentication) {
		this(sessionIdProvider, authentication.getUsername(), authentication.getAccessKey());
	}

	/**
	 * @param sessionIdProvider
	 * @param username
	 * @param accessKey
	 */
	public SauceRestRule(final SauceOnDemandSessionIdProvider sessionIdProvider, final String username, final String accessKey) {
		this.sessionIdProvider = sessionIdProvider;
		sauceRestUtil = new SauceRestUtil(username, accessKey);
	}

	/**
	 * Invoked if the unit test passes without error or failure. Invokes the Sauce REST API to mark the Sauce Job as 'passed'. Prints test information from sauceLabs, to the test results log file.
	 * 
	 * @param description
	 *            not used
	 */
	@Override
	protected void succeeded(final Description description) {
		try {
			if (sessionIdProvider.getSessionId() != null) {
				// log the session id to the system out
				printSessionId(description);
				final Map<String, Object> updates = new HashMap<String, Object>();
				updates.put("passed", true);
				Utils.addBuildNumberToUpdate(updates);
				sauceRestUtil.updateJobInfo(sessionIdProvider.getSessionId(), updates);
			}
		} catch (final IOException ioe) {
			logger.error("sauce rest to update job for success failed: " + ioe.getMessage());
			logger.error(ioe.getStackTrace());
			throw new RuntimeException(ioe);
		}
	}

	/**
	 * Invoked if the unit test either throws an error or fails. Invokes the Sauce REST API to mark the Sauce Job as 'failed'. Prints test information from sauceLabs, to the test results log.
	 * 
	 * @param e
	 *            not used
	 * @param description
	 *            not used
	 */
	@Override
	protected void failed(final Throwable e, final Description description) {
		try {
			if (sessionIdProvider != null && sessionIdProvider.getSessionId() != null) {
				printSessionId(description);
				final Map<String, Object> updates = new HashMap<String, Object>();
				updates.put("passed", false);
				Utils.addBuildNumberToUpdate(updates);
				sauceRestUtil.updateJobInfo(sessionIdProvider.getSessionId(), updates);
			}
		} catch (final IOException ioe) {
			logger.error("sauce rest to update job for failed failed: " + ioe.getMessage());
			logger.error(ioe.getStackTrace());
			throw new RuntimeException(ioe);
		}
	}

	/**
	 * Print test information from sauceLabs.
	 * 
	 * @param description
	 */
	private void printSessionId(final Description description) {
		final String sessionId = sessionIdProvider.getSessionId();
		final String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s.%3$s", sessionId, description.getClassName(),
				description.getMethodName());
		// System.Out is redirected to print in the test results file. Check surefire plugin configuration in pom.xml file.
		System.out.println(message);
		// show test data info
		System.out.println(sauceRestUtil.composeJobInfoUrl(sessionId).toString());
		// show test log file
		System.out.println(sauceRestUtil.composeJobLogUrl(sessionId).toString());
		// show test video url.
		System.out.println(sauceRestUtil.composeVideoUrl(sessionId).toString());
	}
}
