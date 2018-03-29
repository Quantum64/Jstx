package co.q64.jstx;

import org.junit.Test;

import co.q64.jstx.util.SimpleJstxTest;

public class DefTest {
	@Test
	public void testDef() {
		new SimpleJstxTest("load 2", "load 3", "jump add", "load 1", "def add", "+").execute("5");
	}

	@Test
	public void testMultiFunctionWithReturn() {
		new SimpleJstxTest(
		// @formatter:off
				"# Test factorial function several times",
				"load 2",
				"jump factorial",
				"print",
				"load 5",
				"jump factorial",
				"print",
				"load 9",
				"jump addOne",
				"jump factorial",
				"println",
				"terminate",
				"",
				"# The factorial function",
				"def factorial",
				"load 1",
				"swp",
				"iterate",
				"ldr o",
				"jump addOne",
				"*",
				"end",
				"return",
				"",
				"# Adds one to the current value on the stack",
				"def addOne",
				"load 1",
				"+",
				"return"
		// @formatter:on
		).execute("21203628800");
	}
}
