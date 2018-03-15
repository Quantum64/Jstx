package co.q64.jstx;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compiler.Compiler;
import co.q64.jstx.inject.OpcodeModule;
import co.q64.jstx.inject.lexer.CompiledLexer;
import co.q64.jstx.lang.Instruction;
import co.q64.jstx.lang.ProgramFactory;
import dagger.Component;

@Singleton
public class TestLexer {
	protected @Inject TestLexer() {}

	protected @Inject Compiler compiler;
	protected @Inject CompiledLexer cl;
	protected @Inject ProgramFactory pf;

	public static void main(String[] args) {
		DaggerTestLexer_TestComponent.create().getLexer().start();
	}

	private void start() {
		String pr = compiler.compile(Arrays.asList(
				// @formatter:off
				"load -1",
				"sdr a",
				"load This is a test", // Load the test string
				"iterate stack", // Iterate over each character on the stack
				"dup", // Duplicate the current character on the stack
				"load a", // Loads the letter a on to the stack
				"if =", // Is the current letter an a?
				"pop", // Remove the letter from the stack
				"load -1",
				"ldr a",
				"*",
				"sdr a",
				"endif",
				"else",
				"ldr a",
				"load 1",
				"if =",
				"string.toUpperCase",
				"println",
				"endif",
				"end",
				"terminate"
				// @formatter:on
				));
		System.out.println(pr);
		List<Instruction> insns = cl.parse(pr);
		pf.create(insns).execute();
	}

	@Singleton
	@Component(modules = OpcodeModule.class)
	protected static interface TestComponent {
		public TestLexer getLexer();
	}
}
