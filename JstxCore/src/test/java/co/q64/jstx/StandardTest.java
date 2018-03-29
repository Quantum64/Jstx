package co.q64.jstx;

import org.junit.Test;

import co.q64.jstx.util.SimpleJstxTest;

public class StandardTest {
	@Test
	public void testLoadIncompressibleString() {
		new SimpleJstxTest("load H1e2l3l4o5 6W7o8r9l0d!").execute("H1e2l3l4o5 6W7o8r9l0d!");
	}

	@Test
	public void testLoadLowercaseString() {
		new SimpleJstxTest("load hello world").execute("hello world");
	}

	@Test
	public void testLoadTitlecaseString() {
		new SimpleJstxTest("load Hello World").execute("Hello World");
	}

	@Test
	public void testLoadSingleChar() {
		new SimpleJstxTest("load Q").execute("Q");
	}

	@Test
	public void testLoadDoubleChar() {
		new SimpleJstxTest("load 64").execute("64");
	}

	@Test
	public void testLoadLargeNumber() {
		new SimpleJstxTest("load 1234567890987654321").execute("1234567890987654321");
	}
}
