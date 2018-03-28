package co.q64.jstx;

import org.junit.Test;

import co.q64.jstx.util.SimpleJstxTest;

public class DefTest {
	@Test
	public void testDef() {
		new SimpleJstxTest("load 2", "load 3", "jump add", "load 1", "def add", "+").execute("5");
	}
}
