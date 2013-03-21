package com.test.nicu;

import java.util.Arrays;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Stack;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import sun.util.logging.resources.logging;

@RunWith(value = Parameterized.class)
@Ignore
public class StackTest {
	Stack<Integer>		stack;

	private final int	number;

	public StackTest(final int number) {
		System.out.println(number);
		this.number = number;
	}

	@Parameters
	public static Collection data() {
		final Object[][] data = new Object[][] { { 1 }, { 2 }, { 3 }, { 4 } };
		return Arrays.asList(data);
	}

	@Before
	public void noSetup() {
		System.out.println("setup");
		stack = new Stack<Integer>();
	}

	@After
	public void noTearDown() {
		stack = null;
	}

	@Test
	public void popTest() {
		System.out.println("popTest");
	}

	@Test(expected = EmptyStackException.class)
	public void peekTest() {
		stack = new Stack<Integer>();
		stack.peek();
	}

	@Test
	public void emptyTest() {
		stack = new Stack<Integer>();
		org.junit.Assert.assertTrue(stack.isEmpty());
	}

	@Test
	public void searchTest() {
	}
}
