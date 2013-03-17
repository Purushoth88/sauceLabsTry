/**
 * 
 */
package com.test.poc.core.parent;

import static com.test.poc.util.TestUtil.prepareWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.test.poc.core.model.CapabilityConfiguraton;
import com.test.poc.core.model.ProfileConfiguration;
import com.test.poc.util.JSONConfigurationUtils;

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
	protected SauceOnDemandAuthentication	authentication		= new SauceOnDemandAuthentication("ciprianileana", "54c66330-430e-4a32-be8a-ab7e2b418965");

	/**
	 * 
	 */
	protected String						sessionId;

	protected static String					configurationFile	= "\\profiles\\saucelabs.poc.profile.DEVELOPMENT2.json";

	protected WebDriver provideWebDriver(CapabilityConfiguraton capabilityConfiguraton) throws MalformedURLException {
		return prepareWebDriver(capabilityConfiguraton, authentication);
	}

	@Parameters
	public static Collection data() throws JSONException, IOException {
		ProfileConfiguration profileConfiguration = JSONConfigurationUtils.loadProfileConfiguration(configurationFile);
		List<CapabilityConfiguraton> capabilityConfiguratons = profileConfiguration.getCapabilities();

		int index = 0;
		Object[][] data = new Object[capabilityConfiguratons.size()][];
		for (CapabilityConfiguraton capabilityConfiguraton : capabilityConfiguratons) {
			Object[] array = new Object[] { capabilityConfiguraton };
			data[index] = array;
			index++;
		}

		List<Object[]> lo = Arrays.asList(data);
		return lo;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

}
