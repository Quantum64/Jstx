package co.q64.jstx.opcode.x3b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeFactory;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class StackOpcodes implements OpcodeRegistry {
	protected @Inject StackOpcodes() {}

	protected @Inject OpcodeFactory of;
	protected @Inject LiteralFactory literal;

	@Override
	public void init(Opcodes op) {
		op.reg("dup 2", code(Chars.x00), stack -> stack.dup(2));
		op.reg("dup 3", code(Chars.x01), stack -> stack.dup(3));
		op.reg("dup 4", code(Chars.x02), stack -> stack.dup(4));
		op.reg("dup 5", code(Chars.x03), stack -> stack.dup(5));
		op.reg("dup 6", code(Chars.x04), stack -> stack.dup(6));
		op.reg("dup 7", code(Chars.x05), stack -> stack.dup(7));
		op.reg("dup x", code(Chars.x06), stack -> stack.dup(stack.pop().asInt()));
		op.reg("ldr 0", code(Chars.x07), stack -> stack.dup(stack.pop().asInt()));
		op.reg("ldr 1", code(Chars.x08), stack -> stack.dup(stack.pop().asInt()));
		op.reg("ldr 2", code(Chars.x0a), stack -> stack.dup(stack.pop().asInt()));
		op.reg("ldr 3", code(Chars.x0b), stack -> stack.dup(stack.pop().asInt()));
		op.reg("ldr 4", code(Chars.x0c), stack -> stack.dup(stack.pop().asInt()));
		op.reg("ldr 5", code(Chars.x0d), stack -> stack.dup(stack.pop().asInt()));
		op.reg("ldr 6", code(Chars.x0e), stack -> stack.dup(stack.pop().asInt()));
		op.reg("ldr x", code(Chars.x0f), stack -> stack.dup(stack.pop().asInt()));
		op.reg("flatten", code(Chars.x10), stack -> {
			if (stack.size() <= 0) {
				return;
			}
			List<Value> values = new ArrayList<>();
			for (int i = 0; i < stack.size(); i++) {
				values.add(stack.peek(stack.size() - i));
			}
			stack.clr();
			stack.push(values);
		});
		op.reg("flatten hard", code(Chars.x11), stack -> {
			if (stack.size() <= 0) {
				return;
			}
			StringBuilder value = new StringBuilder();
			for (int i = 0; i < stack.size(); i++) {
				value.append(stack.peek(stack.size() - i));
			}
			stack.clr();
			stack.push(value.toString());
		});
		// @formatter:off
	}

	private List<Chars> code(Chars code) {
		return Arrays.asList(Chars.x3b, code);
	}
}
