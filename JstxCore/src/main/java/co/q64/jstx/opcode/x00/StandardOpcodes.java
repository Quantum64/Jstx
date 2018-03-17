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
		op.reg("endif", Chars.xff, stack -> {});
		op.reg("load 0", Chars.x00, stack -> stack.push(literal.create("0")));
		op.reg("load 1", Chars.x01, stack -> stack.push(literal.create("1")));
		op.reg("load 2", Chars.x02, stack -> stack.push(literal.create("2")));
		op.reg("load 3", Chars.x03, stack -> stack.push(literal.create("3")));
		op.reg("load 4", Chars.x04, stack -> stack.push(literal.create("4")));
		op.reg("load 5", Chars.x05, stack -> stack.push(literal.create("5")));
		op.reg("load 6", Chars.x06, stack -> stack.push(literal.create("6")));
		op.reg("load 7", Chars.x07, stack -> stack.push(literal.create("7")));
		op.reg("load 8", Chars.x08, stack -> stack.push(literal.create("8")));
		op.reg("load 9", Chars.x09, stack -> stack.push(literal.create("9")));
		op.reg("load null", Chars.x0a, stack -> stack.push(nul));
		op.reg("UNUSED literal begin", Chars.x0b, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal uncompressed", Chars.x0c, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal compression mode 1", Chars.x0d, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal compression mode 2", Chars.x0e, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal compression mode 3", Chars.x0f, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal begin 2 character", Chars.x10, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal begin 1 character", Chars.x11, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		// 12-18 conditional operators
		op.reg("pop", Chars.x19, stack -> stack.pop());
		op.reg("pop 2", Chars.x1a, stack -> stack.pop(2));
		op.reg("clr", Chars.x1b, stack -> stack.clr());
		op.reg("dup", Chars.x1c, stack -> stack.dup());
		op.reg("swp", Chars.x1d, stack -> stack.swap());
		op.reg("load true", Chars.x1e, stack -> stack.push("true"));
		op.reg("load false", Chars.x1f, stack -> stack.push("false"));
		op.reg("ldr a", Chars.x20, stack -> stack.push(stack.getProgram().getRegisters().getA()));
		op.reg("ldr b", Chars.x21, stack -> stack.push(stack.getProgram().getRegisters().getB()));
		op.reg("ldr c", Chars.x22, stack -> stack.push(stack.getProgram().getRegisters().getC()));
		op.reg("ldr d", Chars.x23, stack -> stack.push(stack.getProgram().getRegisters().getD()));
		op.reg("ldr i", Chars.x24, stack -> stack.push(stack.getProgram().getRegisters().getI()));
		op.reg("ldr o", Chars.x25, stack -> stack.push(stack.getProgram().getRegisters().getO()));
		op.reg("sdr a", Chars.x26, stack -> stack.getProgram().getRegisters().setA(stack.pop()));
		op.reg("sdr b", Chars.x27, stack -> stack.getProgram().getRegisters().setB(stack.pop()));
		op.reg("sdr c", Chars.x28, stack -> stack.getProgram().getRegisters().setC(stack.pop()));
		op.reg("sdr d", Chars.x29, stack -> stack.getProgram().getRegisters().setD(stack.pop()));
		// 2a - 37 basic opcodes

	}
}
