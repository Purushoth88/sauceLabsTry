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
public class SauceRestUtility extends SauceREST {

	/**
	 * class logger.
	 */
	private static final Logger	logger					= Logger.getLogger(SauceRestUtility.class);

	public static final String	RESTURL					= "https://saucelabs.com/rest/v1/%1$s";
	private static final String	USER_RESULT_FORMAT		= RESTURL + "/%2$s";
	private static final String	JOB_RESULT_FORMAT		= RESTURL + "/jobs/%2$s";
	private static final String	DOWNLOAD_VIDEO_FORMAT	= JOB_RESULT_FORMAT + "/results/video.flv";
	// TODO Corect link : Fix it https://martchouk:87335815-89fd-4022-94e0-9c268f5991f9@saucelabs.com/jobs/d133e0fef6c94c99bff3435a83a0f757/video_d133e0fef6c94c99bff3435a83a0f757.flv
	private static final String	DOWNLOAD_LOG_FORMAT		= JOB_RESULT_FORMAT + "/results/video.flv";
	private static final String	DATE_FORMAT				= "yyyyMMdd_HHmmSS";

	/**
	 * @param username
	 * @param accessKey
	 */
	public SauceRestUtility(final String username, final String accessKey) {
		super(username, accessKey);
	}

	/**
	 * Downloads the video for a Sauce Job to the filesystem. The file will be stored in a directory specified by the <code>location</code> field.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @param location
	 */
	public void downloadVideo(final String jobId, final String location) {
		final URL restEndpoint = composeRestVideoUrl(jobId);
		downloadFile(jobId, location, restEndpoint);
	}

	/**
	 * job video url.
	 * 
	 * @param jobId
	 * @return
	 */
	public URL composeRestVideoUrl(final String jobId) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(DOWNLOAD_VIDEO_FORMAT, username, jobId));
		} catch (final MalformedURLException malformedURLException) {
			logger.warn("Error constructing Sauce URL", malformedURLException);
		}
		return restEndpoint;
	}

	/**
	 * Downloads the log file for a Sauce Job to the filesystem. The file will be stored in a directory specified by the <code>location</code> field.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @param location
	 * @param location
	 */
	public void downloadLog(final String jobId, final String location) {
		final URL restEndpoint = composeJobLogUrl(jobId);
		downloadFile(jobId, location, restEndpoint);
	}

	/**
	 * test log url.
	 * 
	 * @param jobId
	 * @return
	 */
	public URL composeJobLogUrl(final String jobId) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(DOWNLOAD_LOG_FORMAT, username, jobId));
		} catch (final MalformedURLException malformedURLException) {
			logger.warn("Error constructing Sauce URL", malformedURLException);
		}
		return restEndpoint;
	}

	/**
	 * user test results url.
	 * 
	 * @param path
	 * @return
	 */
	public String retrieveResults(final String path) {
		final URL restEndpoint = composeUserTestResultsUrl(path);
		return retrieveResults(restEndpoint);
	}

	/**
	 * @param path
	 * @return
	 */
	public URL composeUserTestResultsUrl(final String path) {
		URL restEndpoint = null;
		try {
			restEndpoint = new URL(String.format(USER_RESULT_FORMAT, username, path));
		} catch (final MalformedURLException malformedURLException) {
			logger.warn("Error constructing Sauce URL", malformedURLException);
		}
		return restEndpoint;
	}

	/**
	 * job info url.
	 * 
	 * @param jobId
	 * @return
	 */
	public String getJobInfo(final String jobId) {
		URL restEndpoint = null;
		restEndpoint = composeJobInfoUrl(jobId, restEndpoint);
		return retrieveResults(restEndpoint);
	}

	/**
	 * @param jobId
	 * @param restEndpoint
	 * @return
	 */
	public URL composeJobInfoUrl(final String jobId, URL restEndpoint) {
		try {
			restEndpoint = new URL(String.format(JOB_RESULT_FORMAT, username, jobId));
		} catch (final MalformedURLException malformedURLException) {
			logger.warn("Error constructing Sauce URL", malformedURLException);
		}
		return restEndpoint;
	}

	/**
	 * @param restEndpoint
	 * @return
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
			logger.warn("Error retrieving Sauce Results", ioException);
		}
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (final IOException ioException) {
			logger.warn("Error closing Sauce input stream", ioException);
		}
		return builder.toString();
	}

	/**
	 * @param jobId
	 * @param location
	 * @param restEndpoint
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
			if (restEndpoint.getPath().endsWith(".flv")) {
				saveName = saveName + ".flv";
			} else {
				saveName = saveName + ".log";
			}
			final FileOutputStream file = new FileOutputStream(new File(location, saveName));
			final BufferedOutputStream out = new BufferedOutputStream(file);
			int i;
			while ((i = in.read()) != -1) {
				out.write(i);
			}
			out.flush();
		} catch (final IOException ioException) {
			logger.warn("Error downloading Sauce Results", ioException);
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