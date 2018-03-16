package co.q64.jstx.opcode.x00;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Null;
import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeFactory;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class StandardOpcodes implements OpcodeRegistry {
	protected @Inject StandardOpcodes() {}

	protected @Inject OpcodeFactory of;
	protected @Inject LiteralFactory literal;
	protected @Inject Null nul;

	@Override
	public void init(Opcodes op) {
		op.accept(of.create("endif", Chars.xff, stack -> {}));
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
		op.accept(of.create("load null", Chars.x0a, stack -> stack.push(nul)));
		op.accept(of.create("UNUSED literal begin", Chars.x0b, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!")));
		op.accept(of.create("UNUSED literal uncompressed", Chars.x0c, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!")));
		op.accept(of.create("UNUSED literal compression mode 1", Chars.x0d, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!")));
		op.accept(of.create("UNUSED literal compression mode 2", Chars.x0e, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!")));
		op.accept(of.create("UNUSED literal compression mode 3", Chars.x0f, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!")));
		op.accept(of.create("UNUSED literal begin 2 character", Chars.x10, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!")));
		op.accept(of.create("UNUSED literal begin 1 character", Chars.x11, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!")));
		// 12-18 conditional operators
		op.accept(of.create("pop", Chars.x19, stack -> stack.pop()));
		op.accept(of.create("pop 2", Chars.x1a, stack -> stack.pop(2)));
		op.accept(of.create("clr", Chars.x1b, stack -> stack.clr()));
		op.accept(of.create("dup", Chars.x1c, stack -> stack.dup()));
		op.accept(of.create("swp", Chars.x1d, stack -> stack.swap()));
		op.accept(of.create("load true", Chars.x1e, stack -> stack.push("true")));
		op.accept(of.create("load false", Chars.x1f, stack -> stack.push("false")));
		op.accept(of.create("ldr a", Chars.x20, stack -> stack.push(stack.getProgram().getRegisters().getA())));
		op.accept(of.create("ldr b", Chars.x21, stack -> stack.push(stack.getProgram().getRegisters().getB())));
		op.accept(of.create("ldr c", Chars.x22, stack -> stack.push(stack.getProgram().getRegisters().getC())));
		op.accept(of.create("ldr d", Chars.x23, stack -> stack.push(stack.getProgram().getRegisters().getD())));
		op.accept(of.create("ldr i", Chars.x24, stack -> stack.push(stack.getProgram().getRegisters().getI())));
		op.accept(of.create("ldr o", Chars.x25, stack -> stack.push(stack.getProgram().getRegisters().getO())));
		op.accept(of.create("sdr a", Chars.x26, stack -> stack.getProgram().getRegisters().setA(stack.pop())));
		op.accept(of.create("sdr b", Chars.x27, stack -> stack.getProgram().getRegisters().setB(stack.pop())));
		op.accept(of.create("sdr c", Chars.x28, stack -> stack.getProgram().getRegisters().setC(stack.pop())));
		op.accept(of.create("sdr d", Chars.x29, stack -> stack.getProgram().getRegisters().setD(stack.pop())));
		// 2a - 37 basic opcodes

	}
}
