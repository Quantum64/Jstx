package co.q64.jstx.opcode.x3b;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeFactory;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class StackOpcodes implements OpcodeRegistry {
	protected @Inject StackOpcodes() {}

	protected @Inject OpcodeFactory of;

	@Override
	public void init(Opcodes op) {
		op.reg("dup 2", code(Chars.x00), stack -> stack.dup(2));
		op.reg("dup 3", code(Chars.x01), stack -> stack.dup(3));
		op.reg("dup 4", code(Chars.x02), stack -> stack.dup(4));
		op.reg("dup 5", code(Chars.x03), stack -> stack.dup(5));
		op.reg("dup 6", code(Chars.x04), stack -> stack.dup(6));
		op.reg("dup 7", code(Chars.x05), stack -> stack.dup(7));
		op.reg("dup x", code(Chars.x06), stack -> stack.dup(stack.pop().asInt()));
		// @formatter:off
	}

	private List<Chars> code(Chars code) {
		return Arrays.asList(Chars.x3b, code);
	}
}
