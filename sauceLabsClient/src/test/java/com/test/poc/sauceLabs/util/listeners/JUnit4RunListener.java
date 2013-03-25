package com.test.poc.sauceLabs.util.listeners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.maven.plugin.surefire.StartupReportConfiguration;
import org.apache.maven.plugin.surefire.booterclient.output.DeserializedStacktraceWriter;
import org.apache.maven.plugin.surefire.report.DefaultReporterFactory;
import org.apache.maven.surefire.report.RunListener;
import org.apache.maven.surefire.report.SimpleReportEntry;
import org.apache.maven.surefire.report.StackTraceWriter;
import org.apache.maven.surefire.testset.TestSetFailedException;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Parameterized;

/**
 * Simple JUnit test listener. Useful together with Surefire plugin to listen to test events.
 * 
 * @author Nicolae.Petridean
 * @author Ciprian I. Ileana
 */
@RunWith(value = Parameterized.class)
public class JUnit4RunListener extends org.junit.runner.notification.RunListener {

	/**
	 * class logger.
	 */
	private static final Logger			logger		= Logger.getLogger(JUnit4RunListener.class);

	/**
	 * 
	 */
	private static final Pattern		PARENS		= Pattern.compile("^" + ".+" // any character
															+ "\\(("
															// then an open-paren (start matching a group)
															+ "[^\\\\(\\\\)]+" // non-parens
															+ ")\\)" + "$");
	// then a close-paren (end group match)
	/**
	 * run listener reporter.
	 */
	protected final RunListener			reporter;

	/**
	 * This flag is set after a failure has occurred so that a <code>testSucceeded</code> event is not fired. This is necessary because JUnit4 always fires a <code>testRunFinished</code> event-- even if there was a failure.
	 */
	private final ThreadLocal<Boolean>	failureFlag	= new InheritableThreadLocal<Boolean>();

	/**
	 * Constructor.
	 * 
	 * @param reporter
	 *            the reporter to log testing events to
	 */
	public JUnit4RunListener() {
		final DefaultReporterFactory factory = new DefaultReporterFactory(StartupReportConfiguration.defaultValue());
		this.reporter = factory.createReporter();
	}

	// Testrun methods are not invoked when using the runner

	/**
	 * Called when a specific test has been skipped (for whatever reason).
	 * 
	 * @see org.junit.runner.notification.RunListener#testIgnored(org.junit.runner.Description)
	 */
	@Override
	public void testIgnored(final Description description) throws Exception {

	}

	/**
	 * Called when a specific test has started.
	 * 
	 * @see org.junit.runner.notification.RunListener#testStarted(org.junit.runner.Description)
	 */
	@Override
	public void testStarted(final Description description) throws Exception {
		reporter.testStarting(createReportEntry(description));
		failureFlag.remove();
	}

	/**
	 * Called when a specific test has failed.
	 * 
	 * @see org.junit.runner.notification.RunListener#testFailure(org.junit.runner.notification.Failure)
	 */
	@Override
	public void testFailure(final Failure failure) throws Exception {
	}

	/**
	 * used to write the stackTrace to report files.
	 * 
	 * @param failure
	 * @return
	 */
	protected StackTraceWriter createStackTraceWriter(final Failure failure) {
		return new DeserializedStacktraceWriter(failure.getMessage(), failure.getTestHeader(), failure.getTrace());
	}

	/**
	 * assumption failure listener method.
	 */
	@Override
	public void testAssumptionFailure(final Failure failure) {
		this.reporter.testAssumptionFailure(createReportEntry(failure.getDescription()));
		failureFlag.set(Boolean.TRUE);
	}

	/**
	 * Called after a specific test has finished.
	 * 
	 * @see org.junit.runner.notification.RunListener#testFinished(org.junit.runner.Description)
	 */
	@Override
	public void testFinished(final Description description) throws Exception {
		@SuppressWarnings("unused")
		String displayName = "";
		try {
			displayName += description.getTestClass().getField("sessionId").toGenericString();
		} catch (final SecurityException securityException) {
			logger.error("Security exception occured " + securityException.getMessage());
			logger.error(securityException.getStackTrace());
		} catch (final NoSuchFieldException noSuchFieldException) {
			logger.error("noSuchFieldException exception occured " + noSuchFieldException.getMessage());
			logger.error(noSuchFieldException.getStackTrace());
		}
		final Boolean failure = failureFlag.get();
		if (failure == null) {
			reporter.testSucceeded(createReportEntry(description));
		}
	}

	/**
	 * Create Report entry.
	 * 
	 * @param description
	 * @return
	 */
	protected SimpleReportEntry createReportEntry(final Description description) {
		final SimpleReportEntry entry = SimpleReportEntry.ignored(getClassName(description), description.getDisplayName(), "MUHAHAHA");
		return entry;
	}

	/**
	 * 
	 * @param description
	 * @return
	 */
	public String getClassName(final Description description) {
		return extractClassName(description);
	}

	/**
	 * 
	 * @param description
	 * @return
	 */
	public static String extractClassName(final Description description) {
		final String displayName = description.getDisplayName();
		final Matcher matcher = PARENS.matcher(displayName);
		if (!matcher.find()) {
			return displayName;
		}
		return matcher.group(1);
	}

	/**
	 * 
	 * @param run
	 * @throws TestSetFailedException
	 */
	public static void rethrowAnyTestMechanismFailures(final Result run) throws TestSetFailedException {
		if (run.getFailureCount() > 0) {
			for (final Failure failure : run.getFailures()) {
				if (isFailureInsideJUnitItself(failure)) {
					final Throwable exception = failure.getException();
					logger.warn("Test set failed " + exception.getMessage());
					throw new TestSetFailedException(exception);
				}
			}
		}
	}

	/**
	 * 
	 * @param failure
	 * @return
	 */
	private static boolean isFailureInsideJUnitItself(final Failure failure) {
		return failure.getDescription().getDisplayName().equals("Test mechanism");
	}
}
