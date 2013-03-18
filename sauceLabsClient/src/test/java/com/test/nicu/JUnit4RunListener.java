package com.test.nicu;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to you
 * under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.surefire.common.junit4.JUnit4StackTraceWriter;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.RunListener;
import org.apache.maven.surefire.report.SimpleReportEntry;
import org.apache.maven.surefire.report.StackTraceWriter;
import org.apache.maven.surefire.testset.TestSetFailedException;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class JUnit4RunListener extends org.junit.runner.notification.RunListener {
	private static final Pattern		PARENS		= Pattern.compile("^" + ".+" // any character
															+ "\\(("
															// then an open-paren (start matching a group)
															+ "[^\\\\(\\\\)]+" // non-parens
															+ ")\\)" + "$");
	// then a close-paren (end group match)

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
	public JUnit4RunListener(final RunListener reporter) {
		this.reporter = reporter;
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
		final ReportEntry report = SimpleReportEntry.withException(getClassName(failure.getDescription()), failure.getTestHeader(),
				createStackTraceWriter(failure));

		if (failure.getException() instanceof AssertionError) {
			this.reporter.testFailed(report);
		} else {
			this.reporter.testError(report);
		}
		failureFlag.set(Boolean.TRUE);
	}

	protected StackTraceWriter createStackTraceWriter(final Failure failure) {
		return new JUnit4StackTraceWriter(failure);
	}

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
		final Boolean failure = failureFlag.get();
		if (failure == null) {
			reporter.testSucceeded(createReportEntry(description));
		}
	}

	protected SimpleReportEntry createReportEntry(final Description description) {
		System.out.println("MUHAHAHAHAHAHAHAHAHAHHHHHHHHHHHHHHHHH");
		return new SimpleReportEntry(getClassName(description), description.getDisplayName());
	}

	public String getClassName(final Description description) {
		return extractClassName(description);
	}

	public static String extractClassName(final Description description) {
		final String displayName = description.getDisplayName();
		final Matcher m = PARENS.matcher(displayName);
		if (!m.find()) {
			return displayName;
		}
		return m.group(1);
	}

	public static void rethrowAnyTestMechanismFailures(final Result run) throws TestSetFailedException {
		if (run.getFailureCount() > 0) {
			for (final Failure failure : run.getFailures()) {
				if (isFailureInsideJUnitItself(failure)) {
					final Throwable exception = failure.getException();
					throw new TestSetFailedException(exception);
				}
			}
		}
	}

	private static boolean isFailureInsideJUnitItself(final Failure failure) {
		return failure.getDescription().getDisplayName().equals("Test mechanism");
	}
}