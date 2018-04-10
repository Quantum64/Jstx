package co.q64.jstx;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.annotation.Constants.Version;
import co.q64.jstx.compiler.Compiler;
import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.lang.Program;
import co.q64.jstx.lang.ProgramFactory;
import co.q64.jstx.runtime.Output;
import lombok.Getter;

@Singleton
public class Jstx {
	protected @Inject Jstx() {}

	protected @Getter @Version @Inject String version;
	protected @Inject Compiler compiler;
	protected @Inject ProgramFactory programFactory;

	public CompilerOutput compileProgram(List<String> lines) {
		return compiler.compile(lines);
	}

	public void runProgram(String compiled, String args, Output output) {
		Program program = programFactory.create(compiled, args, output);
		program.execute();
	}
}
