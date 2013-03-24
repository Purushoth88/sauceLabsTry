package com.test.poc.sauceLabs.util.rest;

import org.json.simple.JSONValue;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * Simple Java API that invokes the Sauce REST API.
 */
public class SRTest {

	private static final Logger	logger					= Logger.getLogger(SRTest.class.getName());

	protected String			username;
	protected String			accessKey;

	public static final String	RESTURL					= "https://saucelabs.com/rest/v1/%1$s";
	private static final String	USER_RESULT_FORMAT		= RESTURL + "/%2$s";
	private static final String	JOB_RESULT_FORMAT		= RESTURL + "/jobs/%2$s";
	private static final String	DOWNLOAD_VIDEO_FORMAT	= JOB_RESULT_FORMAT + "/results/video.flv";
	private static final String	DOWNLOAD_LOG_FORMAT		= JOB_RESULT_FORMAT + "/selenium_server.log";
	private static final String	DATE_FORMAT				= "yyyyMMdd_HHmmSS";

	public SRTest(final String username, final String accessKey) {
		this.username = username;
		this.accessKey = accessKey;
	}

	/**
	 * Marks a Sauce Job as 'passed'.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @throws IOException
	 *             thrown if an error occurs invoking the REST request
	 */
	/*
	 * public void jobPassed(final String jobId) { final Map<String, Object> updates = new HashMap<String, Object>(); updates.put("passed", true); updateJobInfo(jobId, updates); }
	 */
	/**
	 * Marks a Sauce Job as 'failed'.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @throws IOException
	 *             thrown if an error occurs invoking the REST request
	 */
	/*
	 * public void jobFailed(final String jobId) { final Map<String, Object> updates = new HashMap<String, Object>(); updates.put("passed", false); updateJobInfo(jobId, updates); }
	 *//**
	 * Downloads the video for a Sauce Job to the filesystem. The file will be stored in a directory specified by the <code>location</code> field.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @param location
	 * @throws IOException
	 *             thrown if an error occurs invoking the REST request
	 */
	/*
	 * public void downloadVideo(final String jobId, final String location) { URL restEndpoint = null; try { restEndpoint = new URL(String.format(DOWNLOAD_VIDEO_FORMAT, username, jobId)); } catch (final MalformedURLException e) {
	 * logger.log(Level.WARNING, "Error constructing Sauce URL", e); } downloadFile(jobId, location, restEndpoint); }
	 */

	/**
	 * Downloads the log file for a Sauce Job to the filesystem. The file will be stored in a directory specified by the <code>location</code> field.
	 * 
	 * @param jobId
	 *            the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
	 * @param location
	 * @throws IOException
	 *             thrown if an error occurs invoking the REST request
	 */
	/*
	 * public void downloadLog(final String jobId, final String location) { URL restEndpoint = null; try { restEndpoint = new URL(String.format(DOWNLOAD_LOG_FORMAT, username, jobId)); } catch (final MalformedURLException e) {
	 * logger.log(Level.WARNING, "Error constructing Sauce URL", e); } downloadFile(jobId, location, restEndpoint); }
	 * 
	 * public String retrieveResults(final String path) { URL restEndpoint = null; try { restEndpoint = new URL(String.format(USER_RESULT_FORMAT, username, path)); } catch (final MalformedURLException e) { logger.log(Level.WARNING,
	 * "Error constructing Sauce URL", e); } return retrieveResults(restEndpoint); }
	 * 
	 * public String getJobInfo(final String jobId) { URL restEndpoint = null; try { restEndpoint = new URL(String.format(JOB_RESULT_FORMAT, username, jobId)); } catch (final MalformedURLException e) { logger.log(Level.WARNING,
	 * "Error constructing Sauce URL", e); } return retrieveResults(restEndpoint); }
	 */

	/*
	 * public String retrieveResults(final URL restEndpoint) { BufferedReader reader = null; final StringBuilder builder = new StringBuilder(); try { final HttpURLConnection connection = (HttpURLConnection) restEndpoint.openConnection();
	 * 
	 * connection.setDoOutput(true); final String auth = encodeAuthentication(); connection.setRequestProperty("Authorization", auth); reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	 * 
	 * String inputLine; while ((inputLine = reader.readLine()) != null) { builder.append(inputLine); } } catch (final IOException e) { logger.log(Level.WARNING, "Error retrieving Sauce Results", e); } try { if (reader != null) {
	 * reader.close(); } } catch (final IOException e) { logger.log(Level.WARNING, "Error closing Sauce input stream", e); } return builder.toString(); }
	 * 
	 * private void downloadFile(final String jobId, final String location, final URL restEndpoint) { try { final HttpURLConnection connection = (HttpURLConnection) restEndpoint.openConnection();
	 * 
	 * connection.setDoOutput(true); connection.setRequestMethod("PUT"); final String auth = encodeAuthentication(); connection.setRequestProperty("Authorization", auth);
	 * 
	 * final InputStream stream = connection.getInputStream(); final BufferedInputStream in = new BufferedInputStream(stream); final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT); String saveName = jobId + format.format(new
	 * Date()); if (restEndpoint.getPath().endsWith(".flv")) { saveName = saveName + ".flv"; } else { saveName = saveName + ".log"; } final FileOutputStream file = new FileOutputStream(new File(location, saveName)); final
	 * BufferedOutputStream out = new BufferedOutputStream(file); int i; while ((i = in.read()) != -1) { out.write(i); } out.flush(); } catch (final IOException e) { logger.log(Level.WARNING, "Error downloading Sauce Results"); } }
	 */

	/*
	 * public void updateJobInfo(final String jobId, final Map<String, Object> updates) { HttpURLConnection postBack = null; try { final URL restEndpoint = new URL(String.format(JOB_RESULT_FORMAT, username, jobId)); postBack =
	 * (HttpURLConnection) restEndpoint.openConnection(); postBack.setDoOutput(true); postBack.setRequestMethod("PUT"); final String auth = encodeAuthentication(); postBack.setRequestProperty("Authorization", auth); final String jsonText =
	 * JSONValue.toJSONString(updates); postBack.getOutputStream().write(jsonText.getBytes()); } catch (final IOException e) { logger.log(Level.WARNING, "Error updating Sauce Results", e); }
	 * 
	 * try { if (postBack != null) { postBack.getInputStream().close(); } } catch (final IOException e) { logger.log(Level.WARNING, "Error closing result stream", e); }
	 * 
	 * }
	 */

	private String encodeAuthentication() throws NoSuchAlgorithmException, InvalidKeyException {
		String auth = username + ":" + accessKey;
		// Handle long strings encoded using BASE64Encoder - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6947917
		final BASE64Encoder encoder = new BASE64Encoder() {
			@Override
			protected int bytesPerLine() {
				return 9999;
			}
		};
		auth = new String(encoder.encode(auth.getBytes()));

		final KeyGenerator kg = KeyGenerator.getInstance("HmacMD5");
		final SecretKey sk = kg.generateKey();

		// Get instance of Mac object implementing HMAC-MD5, and
		// initialize it with the above secret key
		final Mac mac = Mac.getInstance("HmacMD5");
		mac.init(sk);
		final byte[] result = mac.doFinal(auth.getBytes());
		return new String(result);
	}

	public static void main(final String args[]) {
		final SRTest test = new SRTest("martchouk", "87335815-89fd-4022-94e0-9c268f5991f9");
		String authToken = "";
		try {
			authToken = test.encodeAuthentication();
		} catch (final InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String jobId = "https://saucelabs.com/jobs/d133e0fef6c94c99bff3435a83a0f757?auth=" + authToken;
		// test.downloadLog("d133e0fef6c94c99bff3435a83a0f757", "C:\\aaa.txt");
		System.out.println(jobId);
	}
}