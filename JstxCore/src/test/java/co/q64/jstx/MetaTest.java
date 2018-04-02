package co.q64.jstx;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.util.SimpleJstxTest;

public class MetaTest {
	@Test
	public void testSource() {
		String[] program = { "load Hello World", "meta.source", "print", "print", "terminate" };
		CompilerOutput co = DaggerJstxMain_JstxMainComponent.create().getJstx().compileProgram(Arrays.asList(program));
		Assert.assertEquals(true, co.isSuccess());
		new SimpleJstxTest(program).execute(co.getProgram() + "Hello World");
	}
}
