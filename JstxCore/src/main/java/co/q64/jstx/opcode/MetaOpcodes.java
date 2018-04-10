package co.q64.jstx.opcode;

import java.util.stream.Collectors;

import javax.inject.Inject;

import co.q64.jstx.Jstx;
import co.q64.jstx.annotation.Constants.Name;
import co.q64.jstx.compiler.Compiler;
import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lexer.Lexer;
import co.q64.jstx.runtime.mock.MockOutput;
import dagger.Lazy;

public class MetaOpcodes extends OpcodeRegistry {
	protected @Inject Lazy<Compiler> compiler;
	protected @Inject Lazy<Lexer> lexer;
	protected @Inject Lazy<Jstx> jstx;
	protected @Inject MockOutput mock;
	protected @Inject @Name String name;

	protected @Inject MetaOpcodes() {
		super(OpcodeMarker.META);
	}

	@Override
	public void register() {
		r("meta.source", stack -> stack.push(stack.getProgram().getSource()), "Push the program's source code.");
		r("meta.compile", stack -> stack.push(compiler.get().compile(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.toList())).getProgram()), "Compile a " + name + " program using a list of instructions on the top of the stack.");
		r("meta.lex", stack -> stack.push(lexer.get().parse(stack.pop().toString(), mock)), "Push a list of " + name + " instructions from the compiled program on the top of the stack.");
		r("meta.crash", stack -> stack.getProgram().crash("Manually initiated crash"), "Crash the program.");
		r("meta.crashMessage", stack -> stack.getProgram().crash(stack.pop().toString()), "Crash the program using the first stack value as the error message.");
		r("meta.compilePrint", stack -> jstx.get().compileProgram(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.toList())).getDisplayOutput().forEach(stack.getProgram()::println), "Compile a list of " + name + " instructions on the top of the stack and print the compiler output.");
		r("meta.exec", stack -> jstx.get().runProgram(stack.pop().toString(), "", stack.getProgram().getOutput()), "Evaluate a " + name + " program on the top of the stack.");
		r("meta.eval", stack -> {
			CompilerOutput co = jstx.get().compileProgram(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.toList()));
			if (!co.isSuccess()) {
				co.getDisplayOutput().forEach(stack.getProgram()::println);
				return;
			}
			jstx.get().runProgram(co.getProgram(), "", stack.getProgram().getOutput());
		}, "Compile and evaluate a list of " + name + " instructions on the top of the stack.");
	}
}
