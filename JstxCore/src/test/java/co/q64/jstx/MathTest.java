package co.q64.jstx;

import org.junit.Test;

import co.q64.jstx.util.SimpleJstxTest;

public class MathTest {
	@Test
	public void testE() {
		new SimpleJstxTest("math.e").execute(Math.E);
	}
	
	@Test
	public void testPi() {
		new SimpleJstxTest("math.pi").execute(Math.PI);
	}
}
