package co.q64.jstx.opcode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Value;

@Singleton
public class StackOpcodes implements OpcodeRegistry {
	protected @Inject StackOpcodes() {}

	protected @Inject LiteralFactory literal;

	@Override
	public void register(Opcodes op) {
		op.reg("dup 2", stack -> stack.dup(2), "Push two copies of the first stack value.");
		op.reg("dup 3", stack -> stack.dup(3), "Push three copies of the first stack value.");
		op.reg("dup 4", stack -> stack.dup(4), "Push four copies of the first stack value.");
		op.reg("dup 5", stack -> stack.dup(5), "Push five copies of the first stack value.");
		op.reg("dup 6", stack -> stack.dup(6), "Push six copies of the first stack value.");
		op.reg("dup 7", stack -> stack.dup(7), "Push seven copies of the first stack value.");
		op.reg("dup x", stack -> stack.dup(Math.abs(stack.pop().asInt())), "Push the second stack value the absolute value of the first stack value times.");
		op.reg("lda 0", stack -> stack.push(stack.getProgram().getArg(0)), "Push the first command line argument.");
		op.reg("lda 1", stack -> stack.push(stack.getProgram().getArg(1)), "Push the second command line argument.");
		op.reg("lda 2", stack -> stack.push(stack.getProgram().getArg(2)), "Push the third command line argument.");
		op.reg("lda 3", stack -> stack.push(stack.getProgram().getArg(3)), "Push the fourth command line argument.");
		op.reg("lda 4", stack -> stack.push(stack.getProgram().getArg(4)), "Push the fifth command line argument.");
		op.reg("lda 5", stack -> stack.push(stack.getProgram().getArg(5)), "Push the sixth command line argument.");
		op.reg("lda 6", stack -> stack.push(stack.getProgram().getArg(6)), "Push the seventh command line argument.");
		op.reg("lda x", stack -> stack.push(stack.getProgram().getArg(stack.pop().asInt())), "Push the command line argument indexed by the first stack value.");
		op.reg("flatten", stack -> {
			if (stack.size() <= 0) {
				return;
			}
			List<Value> values = new ArrayList<>();
			for (int i = 0; i < stack.size(); i++) {
				values.add(stack.peek(stack.size() - i));
			}
			stack.clr();
			stack.push(values);
		}, "Collapse all stack values into a list, then push that list.");
		op.reg("flatten hard", stack -> {
			if (stack.size() <= 0) {
				return;
			}
			StringBuilder value = new StringBuilder();
			for (int i = 0; i < stack.size(); i++) {
				value.append(stack.peek(stack.size() - i));
			}
			stack.clr();
			stack.push(value.toString());
		}, "Collapse all stack values into a string, then push that string.");
		op.reg("flatten soft", stack -> {
			if (stack.size() <= 0) {
				return;
			}
			StringBuilder value = new StringBuilder();
			for (int i = 0; i < stack.size(); i++) {
				value.append(stack.peek(stack.size() - i));
				if (i < stack.size() - 1) {
					value.append(" ");
				}
			}
			stack.clr();
			stack.push(value.toString());
		}, "Collapse all stack values into a string seperated by spaces, then push that string.");

		op.reg("stack.size", stack -> stack.push(stack.size()), "Push the number of values on the stack.");
		op.reg("stack.print", stack -> stack.getStack().stream().forEach(v -> stack.getProgram().println(v.toString())), "Print each value on the stack, followed by a newline. Does not remove values from the stack.");
		// @formatter:off
	}
}
