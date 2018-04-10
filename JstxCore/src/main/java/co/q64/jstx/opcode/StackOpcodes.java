package co.q64.jstx.opcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Value;

@Singleton
public class StackOpcodes extends OpcodeRegistry {
	protected @Inject StackOpcodes() {
		super(OpcodeMarker.STACK);
	}

	protected @Inject LiteralFactory literal;

	@Override
	public void register() {
		r("dup 2", stack -> stack.dup(2), "Push two copies of the first stack value.");
		r("dup 3", stack -> stack.dup(3), "Push three copies of the first stack value.");
		r("dup 4", stack -> stack.dup(4), "Push four copies of the first stack value.");
		r("dup 5", stack -> stack.dup(5), "Push five copies of the first stack value.");
		r("dup 6", stack -> stack.dup(6), "Push six copies of the first stack value.");
		r("dup 7", stack -> stack.dup(7), "Push seven copies of the first stack value.");
		r("dup x", stack -> stack.dup(Math.abs(stack.pop().asInt())), "Push the second stack value the absolute value of the first stack value times.");
		r("flatten", stack -> {
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
		r("flatten hard", stack -> {
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
		r("flatten soft", stack -> {
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

		r("stack.size", stack -> stack.push(stack.size()), "Push the number of values on the stack.");
		r("stack.print", stack -> stack.getStack().stream().forEach(v -> stack.getProgram().println(v.toString())), "Print each value on the stack, followed by a newline. Does not remove values from the stack.");
		// @formatter:off
	}
}
