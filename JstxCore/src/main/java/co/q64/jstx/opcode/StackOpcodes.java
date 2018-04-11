package co.q64.jstx.opcode;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.value.LiteralFactory;

@Singleton
public class StackOpcodes extends OpcodeRegistry {
	protected @Inject StackOpcodes() {
		super(OpcodeMarker.STACK);
	}

	protected @Inject LiteralFactory literal;

	@Override
	public void register() {
		r("dup 4", stack -> stack.dup(4), "Push four copies of the first stack value.");
		r("dup 5", stack -> stack.dup(5), "Push five copies of the first stack value.");
		r("dup 6", stack -> stack.dup(6), "Push six copies of the first stack value.");
		r("dup 7", stack -> stack.dup(7), "Push seven copies of the first stack value.");

		r("stack.size", stack -> stack.push(stack.size()), "Push the number of values on the stack.");
		r("stack.print", stack -> stack.getStack().stream().forEach(v -> stack.getProgram().println(v.toString())), "Print each value on the stack, followed by a newline. Does not remove values from the stack.");
		// @formatter:off
	}
}
