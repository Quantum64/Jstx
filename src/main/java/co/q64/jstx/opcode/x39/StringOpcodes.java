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
		op.accept(of.create("string.toLowerCase", code(Chars.x00), stack -> stack.push(stack.pop().toString().toLowerCase())));
		op.accept(of.create("string.toUpperCase", code(Chars.x01), stack -> stack.push(stack.pop().toString().toUpperCase())));
	}
	
	private List<Chars> code(Chars code) {
		return Arrays.asList(Chars.x39, code);
	}
}
