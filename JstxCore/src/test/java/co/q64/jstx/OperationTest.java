package co.q64.jstx;

import org.junit.Test;

import co.q64.jstx.util.SimpleJstxTest;

public class OperationTest {
	@Test
	public void testAddition() {
		new SimpleJstxTest("load 4", "load 5", "+").execute("9");
	}

	@Test
	public void testSubtraction() {
		new SimpleJstxTest("load 10", "load 8", "-").execute("2");
	}

	@Test
	public void testMultiplication() {
		new SimpleJstxTest("load 5", "load 6", "*").execute("30");
	}

	@Test
	public void testDivision() {
		new SimpleJstxTest("load 30", "load 10", "/").execute("3");
	}
}
