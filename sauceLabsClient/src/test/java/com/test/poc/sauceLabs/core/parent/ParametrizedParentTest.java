/**
 * 
 */
package com.test.poc.sauceLabs.core.parent;

import static com.test.poc.sauceLabs.util.TestUtil.prepareWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.test.poc.sauceLabs.core.model.CapabilityConfiguraton;
import com.test.poc.sauceLabs.core.model.ProfileConfiguration;
import com.test.poc.sauceLabs.util.JSONConfigurationUtils;

/**
 * Abstract parent class for all SauceLabs related tests. It loads and provides to the test all the capability configuration resulted from the provided configuration file.
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 * 
 */
public abstract class ParametrizedParentTest implements SauceOnDemandSessionIdProvider {

	/**
	 * class logger.
	 */
	private static final Logger				logger				= Logger.getLogger(ParametrizedParentTest.class);

	/**
	 * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key. To use the authentication supplied by environment variables or from an external file, use the no-arg
	 * {@link SauceOnDemandAuthentication} constructor.
	 */
	protected SauceOnDemandAuthentication	authentication		= new SauceOnDemandAuthentication("martchouk", "87335815-89fd-4022-94e0-9c268f5991f9");

	/**
	 * Session id for the SeleniumRC/WebDriver instance - this equates to the Sauce OnDemand Job id.
	 */
	public String							sessionId;

	/**
	 * The default configuration file.
	 */
	protected static String					configurationFile	= "\\profiles\\saucelabs.poc.profile.DEVELOPMENT2.json";

	/**
	 * Based on the provided {@link CapabilityConfiguraton} will provide the proper {@link WebDriver}
	 * 
	 * @param capabilityConfiguraton
	 * @return
	 * @throws MalformedURLException
	 */
	protected WebDriver provideWebDriver(final CapabilityConfiguraton capabilityConfiguraton) throws MalformedURLException {
		logger.debug("Providing the WebDriver");
		return prepareWebDriver(capabilityConfiguraton, authentication);
	}

	/**
	 * Loads the profile configuration and provides parameters to be injected into the test class constructor by {@link Parameterized}.
	 * 
	 * @return a {@link Collection} with the parameterized test data to be feed into the test class constructor by {@link Parameterized}.
	 * @throws JSONException
	 *             if the JSON configuration file doesn't comply to the JSON format
	 * @throws IOException
	 *             if problems occur while locating or reading the configuration file
	 */
	@SuppressWarnings("rawtypes")
	@Parameters
	public static Collection data() throws JSONException, IOException {
		logger.debug("Preparing to load the profile JSON configuration file");
		final ProfileConfiguration profileConfiguration = JSONConfigurationUtils.loadProfileConfiguration(configurationFile);
		final List<CapabilityConfiguraton> capabilityConfiguratons = profileConfiguration.getCapabilities();

		logger.debug("Preparing test data collection to be feed to the test constructor");
		int index = 0;
		final Object[][] data = new Object[capabilityConfiguratons.size()][];
		for (final CapabilityConfiguraton capabilityConfiguraton : capabilityConfiguratons) {
			final Object[] array = new Object[] { capabilityConfiguraton };
			data[index] = array;
			index++;
		}

		List<Object[]> dataCollection = Arrays.asList(data);
		return dataCollection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.saucelabs.common.SauceOnDemandSessionIdProvider#getSessionId()
	 */
	@Override
	public String getSessionId() {
		return sessionId;
	}

}
