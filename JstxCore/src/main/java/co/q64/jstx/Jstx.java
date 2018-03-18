package co.q64.jstx;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compiler.Compiler;
import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.lang.Program;
import co.q64.jstx.lang.ProgramFactory;
import co.q64.jstx.lexer.Lexer;
import co.q64.jstx.runtime.Output;

@Singleton
public class Jstx {
	protected @Inject Jstx() {}

	protected @Inject Compiler compiler;
	protected @Inject Lexer lexer;
	protected @Inject ProgramFactory programFactory;

	public static void main(String[] args) {
		JstxComponent component = DaggerJstxComponent.create();

	}

	public CompilerOutput compileProgram(List<String> lines) {
		return compiler.compile(lines);
	}

	public void runProgram(String compiled, String[] args, Output output) {
		Program program = programFactory.create(lexer.parse(compiled, output), args, output);
		program.execute();
	}
}
