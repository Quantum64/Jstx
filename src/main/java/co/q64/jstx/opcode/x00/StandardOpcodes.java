package co.q64.jstx.opcode.x00;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeFactory;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class StandardOpcodes implements OpcodeRegistry {
	protected @Inject StandardOpcodes() {}

	protected @Inject Opcodes op;
	protected @Inject OpcodeFactory of;
	protected @Inject LiteralFactory literal;

	@Override
	public void init() {
		op.accept(of.create("load 0", Chars.x00, stack -> stack.push(literal.create("0"))));
		op.accept(of.create("load 1", Chars.x01, stack -> stack.push(literal.create("1"))));
		op.accept(of.create("load 2", Chars.x02, stack -> stack.push(literal.create("2"))));
		op.accept(of.create("load 3", Chars.x03, stack -> stack.push(literal.create("3"))));
		op.accept(of.create("load 4", Chars.x04, stack -> stack.push(literal.create("4"))));
		op.accept(of.create("load 5", Chars.x05, stack -> stack.push(literal.create("5"))));
		op.accept(of.create("load 6", Chars.x06, stack -> stack.push(literal.create("6"))));
		op.accept(of.create("load 7", Chars.x07, stack -> stack.push(literal.create("7"))));
		op.accept(of.create("load 8", Chars.x08, stack -> stack.push(literal.create("8"))));
		op.accept(of.create("load 9", Chars.x09, stack -> stack.push(literal.create("9"))));
	}
}
