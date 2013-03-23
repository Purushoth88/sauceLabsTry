/**
 * 
 */
package com.test.poc.sauceLabs.util.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.poc.sauceLabs.core.model.CapabilityConfiguration;
import com.test.poc.sauceLabs.core.model.Credentials;
import com.test.poc.sauceLabs.core.model.ProfileConfiguration;

/**
 * Collection of utility methods for loading JSON formated profile configuration file.
 * 
 * @author Ciprian I. Ileana
 * @author Nicolae Petridean
 */
public class JSONConfigurationUtils {

	/**
	 * class logger.
	 */
	private static final Logger	logger	= Logger.getLogger(JSONConfigurationUtils.class);

	/**
	 * Loads a JSON file from the provided resource
	 * 
	 * @param resource
	 *            the file from which to load the JSON
	 * @return A JSONObject representing the passed resource argument.
	 * @throws JSONException
	 *             if the data loaded from the provided resource is not a valid JSON
	 * @throws IOException
	 *             if a problem occurs while reading data from the provided resource
	 */
	private static JSONObject loadJSON(String resource) throws JSONException, IOException {
		logger.debug("Preparing to load JSON from resource [" + resource + "]");
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);

		if (inputStream == null) {
			try {
				inputStream = new FileInputStream(resource);
			} catch (FileNotFoundException fileNotFoundException) {
				logger.error("Unable to find file [" + resource + "].");
				throw new RuntimeException(resource + " is not a valid resource.", fileNotFoundException);
			}
		}

		StringBuilder stringBuilder = new StringBuilder();
		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);
		String line;

		try {
			while ((line = buffreader.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException ioException) {
			logger.error("Error while reading from [" + resource + "].");
			throw new IOException("Cannot read file [" + resource + "] , " + ioException);
		} finally {
			try {
				buffreader.close();
				inputreader.close();
				inputStream.close();
			} catch (IOException ioException) {
				logger.error("Error while reading from [" + resource + "].");
				ioException.printStackTrace();
			}
		}
		logger.debug("Finished loading JSON from resource [" + resource + "]");

		String content = stringBuilder.toString();
		JSONObject jsonContent;
		jsonContent = new JSONObject(content);

		return jsonContent;
	}

	/**
	 * Loads configuration profile related data from the provided configurationFile and returns and instance of {@link ProfileConfiguration} based on it.
	 * 
	 * @param configurationFile
	 *            the file from which to load the configuration profile related data
	 * @return an instance of {@link ProfileConfiguration} based on the data loaded
	 * @throws JSONException
	 *             if the data loaded from the provided configuration file is not a valid JSON (propagated from {@link #loadJSON})
	 * @throws IOException
	 *             if a problem occurs while reading data from the provided configuration file (propagated from {@link #loadJSON})
	 * @throws IllegalArgumentException
	 *             if any of the required configuration data is not present in the configuration file
	 */
	public static ProfileConfiguration loadProfileConfiguration(String configurationFile) throws JSONException, IOException {
		logger.debug("Preparing to load the profile configuration from [" + configurationFile + "]");
		ProfileConfiguration profileConfiguration = new ProfileConfiguration();

		if ((configurationFile == null) || (configurationFile.isEmpty())) {
			logger.error("Unable to load from [" + configurationFile + "] (configurationFile cannot be neither NULL nor empty).");
			throw new IllegalArgumentException("configurationFile cannot be neither NULL nor empty!");
		}

		JSONObject jsonObject = loadJSON(configurationFile);

		if (jsonObject.has("credentials")) {
			logger.info("credentials - CHECKED");
			JSONObject jsonCredentials = jsonObject.getJSONObject("credentials");

			String userName = jsonCredentials.getString("userName");
			String accessKey = jsonCredentials.getString("accessKey");

			Credentials credentials = new Credentials();
			credentials.setUserName(userName);
			credentials.setAccessKey(accessKey);
			profileConfiguration.setCredentials(credentials);

		} else {
			logger.error("credentials - NOT PRESENT");
			throw new IllegalArgumentException("credentials - NOT PRESENT");
		}

		if (jsonObject.has("capabilities")) {
			logger.info("capabilities - CHECKED");
			JSONArray capabilities = jsonObject.getJSONArray("capabilities");
			if (capabilities.length() >= 1) {
				for (int i = 0; i < capabilities.length(); i++) {
					if (!capabilities.isNull(i)) {
						JSONObject capability = capabilities.getJSONObject(i);

						CapabilityConfiguration capabilityConfiguration = new CapabilityConfiguration();

						String browserName = capability.getString("browserName");
						String browserVersion = capability.getString("version");
						String platform = capability.getString("platform");

						capabilityConfiguration.setBrowserName(browserName);
						capabilityConfiguration.setBrowserVersion(browserVersion);
						capabilityConfiguration.setPlatform(platform);

						profileConfiguration.getCapabilities().add(capabilityConfiguration);

						logger.info(capabilityConfiguration);
					} else {
						logger.error("capabilities - ignoring empty capability!");
					}
				}

				if (profileConfiguration.getCapabilities().isEmpty()) {
					logger.error("capabilities - No capability configuration found!");
					throw new IllegalArgumentException("capabilities - No capability configuration found!");
				}
			} else {
				logger.error("capabilities - EMPTY!");
				throw new IllegalArgumentException("capabilities - EMPTY!");
			}
		} else {
			logger.error("capabilities - NOT PRESENT");
			throw new IllegalArgumentException("capabilities - NOT PRESENT");
		}

		return profileConfiguration;
	}
}