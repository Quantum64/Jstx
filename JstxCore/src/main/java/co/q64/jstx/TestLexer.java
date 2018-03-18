package co.q64.jstx;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compiler.Compiler;
import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.inject.OpcodeModule;
import co.q64.jstx.lang.Instruction;
import co.q64.jstx.lang.ProgramFactory;
import co.q64.jstx.lexer.Lexer;
import co.q64.jstx.runtime.SystemOutput;
import dagger.Component;

@Singleton
public class TestLexer {
	protected @Inject TestLexer() {}

	protected @Inject Compiler compiler;
	protected @Inject Lexer cl;
	protected @Inject ProgramFactory pf;
	protected @Inject SystemOutput so;

	public static void main(String[] args) {
		DaggerTestLexer_TestComponent.create().getLexer().start();
	}

	private void start() {
		CompilerOutput co = compiler.compile(Test.PROGRAM);
		co.getDisplayOutput().forEach(s -> System.out.println(s));
		if (!co.isSuccess()) {
			return;
		}
		String pr = co.getProgram();
		List<Instruction> insns = cl.parse(pr, so);
		pf.create(insns, Test.ARGS, so).execute();
	}

	@Singleton
	@Component(modules = OpcodeModule.class)
	protected static interface TestComponent {
		public TestLexer getLexer();
	}
}
