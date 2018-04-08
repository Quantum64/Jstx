package co.q64.jstx.opcode;

import java.util.stream.Collectors;

import javax.inject.Inject;

import co.q64.jstx.Jstx;
import co.q64.jstx.annotation.Constants.Name;
import co.q64.jstx.compiler.Compiler;
import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lexer.Lexer;
import co.q64.jstx.runtime.mock.MockOutput;
import dagger.Lazy;

public class MetaOpcodes implements OpcodeRegistry {
	protected @Inject Lazy<Compiler> compiler;
	protected @Inject Lazy<Lexer> lexer;
	protected @Inject Lazy<Jstx> jstx;
	protected @Inject MockOutput mock;
	protected @Inject @Name String name;

	protected @Inject MetaOpcodes() {}

	@Override
	public void register(Opcodes oc) {
		oc.reg("meta.source", stack -> stack.push(stack.getProgram().getSource()), "Push the program's source code.");
		oc.reg("meta.compile", stack -> stack.push(compiler.get().compile(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.toList())).getProgram()), "Compile a " + name + " program using a list of instructions on the top of the stack.");
		oc.reg("meta.lex", stack -> stack.push(lexer.get().parse(stack.pop().toString(), mock)), "Push a list of " + name + " instructions from the compiled program on the top of the stack.");
		oc.reg("meta.crash", stack -> stack.getProgram().crash("Manually initiated crash"), "Crash the program.");
		oc.reg("meta.crashMessage", stack -> stack.getProgram().crash(stack.pop().toString()), "Crash the program using the first stack value as the error message.");
		oc.reg("meta.compilePrint", stack -> jstx.get().compileProgram(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.toList())).getDisplayOutput().forEach(stack.getProgram()::println), "Compile a list of " + name + " instructions on the top of the stack and print the compiler output.");
		oc.reg("meta.exec", stack -> jstx.get().runProgram(stack.pop().toString(), new String[0], stack.getProgram().getOutput()), "Evaluate a " + name + " program on the top of the stack.");
		oc.reg("meta.eval", stack -> {
			CompilerOutput co = jstx.get().compileProgram(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.toList()));
			if (!co.isSuccess()) {
				co.getDisplayOutput().forEach(stack.getProgram()::println);
				return;
			}
			jstx.get().runProgram(co.getProgram(), new String[0], stack.getProgram().getOutput());
		}, "Compile and evaluate a list of " + name + " instructions on the top of the stack.");
	}
}
