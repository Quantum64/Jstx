package co.q64.jstx;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compiler.Compiler;
import co.q64.jstx.inject.OpcodeModule;
import co.q64.jstx.lang.Instruction;
import co.q64.jstx.lang.ProgramFactory;
import co.q64.jstx.lexer.CompiledLexer;
import co.q64.jstx.runtime.SystemOutput;
import dagger.Component;

@Singleton
public class TestLexer {
	protected @Inject TestLexer() {}

	protected @Inject Compiler compiler;
	protected @Inject CompiledLexer cl;
	protected @Inject ProgramFactory pf;
	protected @Inject SystemOutput so;

	public static void main(String[] args) {
		DaggerTestLexer_TestComponent.create().getLexer().start();
	}

	private void start() {
		String pr = compiler.compile(Test.PROGRAM);
		System.out.println(pr);
		List<Instruction> insns = cl.parse(pr);
		insns.forEach(i -> System.out.println(i.getOpcode() == null ? "load value" : i.getOpcode().getName()));
		pf.create(insns, Test.ARGS, so).execute();
	}

	@Singleton
	@Component(modules = OpcodeModule.class)
	protected static interface TestComponent {
		public TestLexer getLexer();
	}
}
