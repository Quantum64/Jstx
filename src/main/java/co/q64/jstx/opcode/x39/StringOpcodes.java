package co.q64.jstx.opcode.x39;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeFactory;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class StringOpcodes implements OpcodeRegistry {
	protected @Inject StringOpcodes() {}

	protected @Inject OpcodeFactory of;

	@Override
	public void init(Opcodes op) {

		op.accept(of.create("string.charAt", code(Chars.x01), stack -> stack.push(stack.peek(2).toString().charAt(stack.pull(2).asInt()))));
		op.accept(of.create("string.compareTo", code(Chars.x02), stack -> stack.push(stack.peek(2).toString().compareTo(stack.pull(2).toString()))));
		op.accept(of.create("string.contains", code(Chars.x03), stack -> stack.push(stack.peek(2).toString().contains(stack.pull(2).toString()))));
		op.accept(of.create("string.concat", code(Chars.x04), stack -> stack.push(stack.peek(2).toString().concat(stack.pull(2).toString()))));

		op.accept(of.create("string.substr", code(Chars.x39), stack -> stack.push(stack.peek(2).toString().substring(stack.pull(2).asInt()))));
		op.accept(of.create("string.toLowerCase", code(Chars.x50), stack -> stack.push(stack.pop().toString().toLowerCase())));
		op.accept(of.create("string.toUpperCase", code(Chars.x51), stack -> stack.push(stack.pop().toString().toUpperCase())));

		op.accept(of.create("string.reverse", code(Chars.xf0), stack -> stack.push(new StringBuilder(stack.pop().toString()).reverse())));
		op.accept(of.create("string.reverseconcat", code(Chars.xf1), stack -> stack.push(stack.pop().toString().concat(stack.pop().toString()))));
	}

	private List<Chars> code(Chars code) {
		return Arrays.asList(Chars.x39, code);
	}
}
