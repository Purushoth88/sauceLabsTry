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
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.test.poc.sauceLabs.core.model.CapabilityConfiguration;
import com.test.poc.sauceLabs.core.model.ProfileConfiguration;
import com.test.poc.sauceLabs.util.TestUtil;
import com.test.poc.sauceLabs.util.config.JSONConfigurationUtils;

/**
 * Abstract parent class for all SauceLabs related tests. It loads and provides to the test all the capability configurations resulted from the provided configuration file.
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 */
public abstract class ParametrizedParentTest implements SauceOnDemandSessionIdProvider {

	/**
	 * class logger.
	 */
	private static final Logger						logger					= Logger.getLogger(ParametrizedParentTest.class);

	/**
	 * The default configuration file.
	 */
	protected static final String					CONFIGURATION_FILE		= "\\profiles\\saucelabs.poc.profile.json";

	/**
	 * Profile related configuration data.
	 */
	protected static ProfileConfiguration			profileConfiguration	= null;

	/**
	 * Hold a {@link SauceOnDemandAuthentication} instance using the supplied credentials (user name / access key) from the profile configuration.
	 */
	protected static SauceOnDemandAuthentication	authentication			= null;

	/**
	 * Session id for the SeleniumRC/WebDriver instance - this equates to the Sauce OnDemand Job id.
	 */
	public String									sessionId;

	/**
	 * The constructor will load the profile configuration only if it is not already loaded. The reason stands in the fact that the profile configuration is normally loaded before the call to this constructor through the method annotated
	 * with {@link Parameters}.
	 * 
	 * @throws IOException
	 *             if a problem occurs while reading data from the provided configuration file (propagated from {@link JSONConfigurationUtils#loadJSON})
	 * @throws JSONException
	 *             if the data loaded from the provided configuration file is not a valid JSON (propagated from {@link JSONConfigurationUtils#loadJSON})
	 */
	public ParametrizedParentTest() throws JSONException, IOException {
		if (profileConfiguration == null) {
			loadConfiguration();
		}
	}

	/**
	 * Loads the profile configuration data (in case it is not already loaded) and initializes the {@link SauceOnDemandAuthentication} that will be further used through the tests.
	 * 
	 * @throws JSONException
	 *             if the data loaded from the provided configuration file is not a valid JSON (propagated from {@link JSONConfigurationUtils#loadJSON})
	 * @throws IOException
	 *             if a problem occurs while reading data from the provided configuration file (propagated from {@link JSONConfigurationUtils#loadJSON})
	 */
	private static void loadConfiguration() throws JSONException, IOException {
		if (profileConfiguration == null) {
			logger.debug("Preparing to load the configuration");

			profileConfiguration = JSONConfigurationUtils.loadProfileConfiguration(CONFIGURATION_FILE);
			authentication = new SauceOnDemandAuthentication(profileConfiguration.getCredentials().getUserName(), profileConfiguration.getCredentials().getAccessKey());
		} else {
			logger.debug("Configuration already present. No need to load it again.");
		}
	}

	/**
	 * Based on the provided {@link CapabilityConfiguration} will provide the proper {@link WebDriver}
	 * 
	 * @param capabilityConfiguration
	 *            capability related configuration data.
	 * @return instance of {@link WebDriver} obtained based on the provided capability configuration
	 * @throws MalformedURLException
	 *             if the dynamically created URL when initializing the {@link RemoteWebDriver} specifies an unknown protocol (propagated from {@link TestUtil#prepareWebDriver})
	 */
	protected WebDriver provideWebDriver(final CapabilityConfiguration capabilityConfiguration) throws MalformedURLException {
		logger.debug("Providing the WebDriver");
		return prepareWebDriver(capabilityConfiguration, authentication);
	}

	/**
	 * Loads the profile configuration and provides parameters to be injected into the test class constructor by {@link Parameterized}.
	 * 
	 * @return a {@link Collection} with the parameterized test data to be feed into the test class constructor by {@link Parameterized}.
	 * @throws JSONException
	 *             if the data loaded from the provided configuration file is not a valid JSON (propagated from {@link JSONConfigurationUtils#loadJSON})
	 * @throws IOException
	 *             if a problem occurs while reading data from the provided configuration file (propagated from {@link JSONConfigurationUtils#loadJSON})
	 */
	@SuppressWarnings("rawtypes")
	@Parameters
	public static Collection data() throws JSONException, IOException {
		loadConfiguration();

		final List<CapabilityConfiguration> capabilityConfigurations = profileConfiguration.getCapabilities();

		logger.debug("Preparing test data collection to be feed to the test constructor");
		int index = 0;
		final Object[][] data = new Object[capabilityConfigurations.size()][];
		for (final CapabilityConfiguration capabilityConfiguration : capabilityConfigurations) {
			final Object[] array = new Object[] { capabilityConfiguration };
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
