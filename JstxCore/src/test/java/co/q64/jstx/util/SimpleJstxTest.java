package co.q64.jstx.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;

import co.q64.jstx.DaggerJstxMain_JstxMainComponent;
import co.q64.jstx.Jstx;
import co.q64.jstx.compiler.CompilerOutput;

public class SimpleJstxTest {
	private Jstx jstx = DaggerJstxMain_JstxMainComponent.create().getJstx();
	private String[] program;

	public SimpleJstxTest(String... program) {
		this.program = program;
	}

	public void execute(String args[], String expected) {
		CompilerOutput comp = jstx.compileProgram(Arrays.asList(program));
		Assert.assertEquals(comp.isSuccess(), true);
		OutputBuffer output = new OutputBuffer();
		jstx.runProgram(comp.getProgram(), "", output);
		Assert.assertEquals(expected, output.firstLine());
	}

	public void execute(String expected) {
		execute(new String[0], expected);
	}

	public void execute(String args[], List<String> expected) {
		CompilerOutput comp = jstx.compileProgram(Arrays.asList(program));
		Assert.assertEquals(comp.isSuccess(), true);
		OutputBuffer output = new OutputBuffer();
		jstx.runProgram(comp.getProgram(), "", output);
		String[] lines = output.toString().split(Pattern.quote("\n"));
		for (int i = 0; i < expected.size(); i++) {
			if (i >= lines.length) {
				Assert.fail();
			}
			Assert.assertEquals(expected.get(i), lines[i]);
		}
	}

	public void execute(List<String> expected) {
		execute(new String[0], expected);
	}

	public void execute(double expected) {
		execute(new String[0], expected, 0.01);
	}

	public void execute(double expected, double delta) {
		execute(new String[0], expected, delta);
	}

	public void execute(String[] args, double expected, double delta) {
		CompilerOutput comp = jstx.compileProgram(Arrays.asList(program));
		Assert.assertEquals(comp.isSuccess(), true);
		OutputBuffer output = new OutputBuffer();
		jstx.runProgram(comp.getProgram(), "", output);
		try {
			Assert.assertEquals(expected, Double.parseDouble(output.firstLine()), delta);
		} catch (NumberFormatException e) {
			Assert.fail("Could not convert output to a number!");
		}
	}
}
