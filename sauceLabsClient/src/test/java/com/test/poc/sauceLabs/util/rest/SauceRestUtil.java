package com.test.poc.sauceLabs.util.rest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import com.saucelabs.saucerest.SauceREST;

/**
 * Simple Java API that invokes the Sauce REST API.
 * 
 * @author Nicolae Petridean
 * @author Ciprian I. Ileana
 * 
 */
@SuppressWarnings("restriction")
public class SauceRestUtil extends SauceREST {

	/**
	 * class logger.
	 */
	private static final Logger	logger					= Logger.getLogger(SauceRestUtil.class);

	/**
	 * .flv extension
	 */
	private static final String	FLV_EXTENTION			= ".flv";

	/**
	 * .log extension
	 */
	private static final String	LOG_EXTENTION			= ".log";

	/**
	 * Base URL , containing link to sauceLabs rest api and authentication tokens
	 */
	private static final String	AUTH_RESTURL			= "https://%1$s:%2$s@saucelabs.com";

	/**
	 * jobs results URL
	 */
	private static final String	ALL_JOBS_RESULT_URL		= AUTH_RESTURL + "/rest/v1/%3$s/jobs";

	/**
	 * job results URL
	 */
	private static final String	SPECIFIC_JOB_RESULT_URL	= ALL_JOBS_RESULT_URL + "/%4$s";

	/**
	 * asset base URL
	 */
	private static final String	JOB_ASSET_BASE_URL		= AUTH_RESTURL + "/rest/%3$s/jobs/%4$s/results";

	/**
	 * video URL
	 */
	private static final String	JOB_VIDEO_URL			= JOB_ASSET_BASE_URL + "/video.flv";

	/**
	 * log URL
	 */
	private static final String	JOB_LOG_URL				= JOB_ASSET_BASE_URL + "/selenium-server.log";

	/**
	 * date format to limit rest API results.
	 */
	private static final String	DATE_FORMAT				= "yyyyMMdd_HHmmSS";

	/**
	 * Parameterized constructor, using sauceLabs userName and accessKey.
	 * 
	 * @param username
	 *            the user name
	 * @param accessKey
	 *            the access key
	 */
	public SauceRestUtil(final String username, final String accessKey) {
		super(username, accessKey);
	}

	/**
	 * Downloads the video for a Sauce Job to the file system. The file will be stored in a directory specified by the <code>location</code> field.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @param location
	 *            the location
	 */
	public void downloadVideo(final String jobId, final String location) {
		final URL restEndpoint = composeVideoUrl(jobId);
		downloadFile(jobId, location, restEndpoint);
	}

	/**
	 * Compose Rest API job video URL
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @return the URL for job's video
	 */
	public URL composeVideoUrl(final String jobId) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(JOB_VIDEO_URL, username, accessKey, username, jobId));
		} catch (final MalformedURLException malformedURLException) {
			logger.error("Error constructing Sauce URL", malformedURLException);
		}
		return restEndpoint;
	}

	/**
	 * Downloads the log file for a Sauce Job to the file-system. The file will be stored in a directory specified by the <code>location</code> field.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @param location
	 *            the location
	 */
	public void downloadLog(final String jobId, final String location) {
		final URL restEndpoint = composeJobLogUrl(jobId);
		downloadFile(jobId, location, restEndpoint);
	}

	/**
	 * compose test log URL, via Rest API
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @return job's log URL
	 */
	public URL composeJobLogUrl(final String jobId) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(JOB_LOG_URL, username, accessKey, username, jobId));
		} catch (final MalformedURLException malformedURLException) {
			logger.error("Error constructing Sauce URL", malformedURLException);
		}
		return restEndpoint;
	}

	/**
	 * Retrieves all user test results via Rest API.
	 * 
	 * @return
	 */
	public String retrieveAllUserJobResults() {
		final URL restEndpoint = composeUserTestResultsUrl();
		return retrieveResults(restEndpoint);
	}

	/**
	 * Compose URL to user test results.
	 * 
	 * @return the test result's URL
	 */
	public URL composeUserTestResultsUrl() {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(ALL_JOBS_RESULT_URL, username, accessKey, username));
		} catch (final MalformedURLException malformedURLException) {
			logger.error("Error constructing Sauce URL", malformedURLException);
		}
		return restEndpoint;
	}

	/**
	 * read job info via REST API.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @return the job info
	 */
	public String getJobInfo(final String jobId) {
		URL restEndpoint = null;
		restEndpoint = composeJobInfoUrl(jobId);
		return retrieveResults(restEndpoint);
	}

	/**
	 * Compose the job info URL (Rest API URL).
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @return
	 */
	public URL composeJobInfoUrl(final String jobId) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(SPECIFIC_JOB_RESULT_URL, username, accessKey, username, jobId));
		} catch (final MalformedURLException malformedURLException) {
			logger.error("Error constructing Sauce URL", malformedURLException);
		}
		return restEndpoint;
	}

	/**
	 * Retrieve results from SauceLabs , via REST API.
	 * 
	 * @param restEndpoint
	 *            endpoint's URL
	 * @return the results
	 */
	public String retrieveResults(final URL restEndpoint) {
		BufferedReader reader = null;
		final StringBuilder builder = new StringBuilder();
		try {
			final HttpURLConnection connection = (HttpURLConnection) restEndpoint.openConnection();

			connection.setDoOutput(true);
			final String auth = encodeAuthentication();
			connection.setRequestProperty("Authorization", auth);
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				builder.append(inputLine);
			}
		} catch (final IOException ioException) {
			logger.error("Error retrieving Sauce Results", ioException);
		}
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (final IOException ioException) {
			logger.error("Error closing Sauce input stream", ioException);
		}
		return builder.toString();
	}

	/**
	 * Downloads a sauceLabs file (either server/screen-shot or video) from SauceLabs environment, via REST API.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @param location
	 *            the location
	 * @param restEndpoint
	 *            endpoint's URL
	 */
	private void downloadFile(final String jobId, final String location, final URL restEndpoint) {
		try {
			final HttpURLConnection connection = (HttpURLConnection) restEndpoint.openConnection();

			connection.setDoOutput(true);
			connection.setRequestMethod("PUT");
			final String auth = encodeAuthentication();
			connection.setRequestProperty("Authorization", auth);

			final InputStream stream = connection.getInputStream();
			final BufferedInputStream in = new BufferedInputStream(stream);
			final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
			String saveName = jobId + format.format(new Date());
			if (restEndpoint.getPath().endsWith(FLV_EXTENTION)) {
				saveName = saveName + FLV_EXTENTION;
			} else {
				saveName = saveName + LOG_EXTENTION;
			}
			final FileOutputStream file = new FileOutputStream(new File(location, saveName));
			final BufferedOutputStream out = new BufferedOutputStream(file);
			int i;
			while ((i = in.read()) != -1) {
				out.write(i);
			}
			out.flush();
		} catch (final IOException ioException) {
			logger.error("Error downloading Sauce Results", ioException);
		}
	}

	/**
	 * Additional SauceLabs authentication method, via a temporary authentication token.
	 * 
	 * @return the token.
	 */
	private String encodeAuthentication() {
		String auth = username + ":" + accessKey;
		// Handle long strings encoded using BASE64Encoder - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6947917
		final BASE64Encoder encoder = new BASE64Encoder() {
			@Override
			protected int bytesPerLine() {
				return 9999;
			}
		};
		auth = "Basic " + new String(encoder.encode(auth.getBytes()));
		return auth;
	}

}