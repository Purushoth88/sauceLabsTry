package com.test.poc.sauceLabs.util.watchers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.common.Utils;
import com.saucelabs.saucerest.SauceREST;
import com.test.poc.sauceLabs.util.rest.SauceRestUtility;

/**
 * Test Watcher used to enrich test reports with test data from SauceLabs.
 * 
 * @author Nicolae.Petridean
 * @author Ciprian I. Ileana
 */
public class TestRuleWatcher extends TestWatcher {

	/**
	 * The underlying {@link com.saucelabs.common.SauceOnDemandSessionIdProvider} instance which contains the Selenium session id. This is typically the unit test being executed.
	 */
	private final SauceOnDemandSessionIdProvider	sessionIdProvider;

	/**
	 * The instance of the Sauce OnDemand Java REST API client.
	 */
	private final SauceREST							sauceREST;

	/**
	 * @param sessionIdProvider
	 */
	public TestRuleWatcher(final SauceOnDemandSessionIdProvider sessionIdProvider) {
		this(sessionIdProvider, new SauceOnDemandAuthentication());
	}

	/**
	 * @param sessionIdProvider
	 * @param authentication
	 */
	public TestRuleWatcher(final SauceOnDemandSessionIdProvider sessionIdProvider, final SauceOnDemandAuthentication authentication) {
		this(sessionIdProvider, authentication.getUsername(), authentication.getAccessKey());
	}

	/**
	 * @param sessionIdProvider
	 * @param username
	 * @param accessKey
	 */
	public TestRuleWatcher(final SauceOnDemandSessionIdProvider sessionIdProvider, final String username, final String accessKey) {
		this.sessionIdProvider = sessionIdProvider;
		sauceREST = new SauceREST(username, accessKey);
	}

	/**
	 * Invoked if the unit test passes without error or failure. Invokes the Sauce REST API to mark the Sauce Job as 'passed'. Prints test information from sauceLabs.
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
				sauceREST.updateJobInfo(sessionIdProvider.getSessionId(), updates);
			}
		} catch (final IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	/**
	 * Invoked if the unit test either throws an error or fails. Invokes the Sauce REST API to mark the Sauce Job as 'failed'. Prints test information from sauceLabs.
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
				sauceREST.updateJobInfo(sessionIdProvider.getSessionId(), updates);
			}
		} catch (final IOException ioe) {
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
		System.out.println(message);
		// TODO: extract this to be read from the project configuration
		final SauceRestUtility restUtility = new SauceRestUtility("martchouk", "87335815-89fd-4022-94e0-9c268f5991f9");
		// show test data info
		System.out.println("https://martchouk:87335815-89fd-4022-94e0-9c268f5991f9@saucelabs.com/rest/v1/martchouk/jobs/" + sessionId);
		// show test data info, log file
		System.out.println(restUtility.composeJobLogUrl(sessionId).toString());
		// show test data info, log file
		System.out.println(restUtility.composeRestVideoUrl(sessionId).toString());
	}
}
