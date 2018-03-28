package co.q64.jstx.opcode;

import static co.q64.jstx.lang.opcode.OpcodeMarker.COMPRESSION1;
import static co.q64.jstx.lang.opcode.OpcodeMarker.COMPRESSION2;
import static co.q64.jstx.lang.opcode.OpcodeMarker.COMPRESSION3;
import static co.q64.jstx.lang.opcode.OpcodeMarker.CONDITIONAL;
import static co.q64.jstx.lang.opcode.OpcodeMarker.ELSE;
import static co.q64.jstx.lang.opcode.OpcodeMarker.END;
import static co.q64.jstx.lang.opcode.OpcodeMarker.ENDIF;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LITERAL;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LITERAL1;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LITERAL2;
import static co.q64.jstx.lang.opcode.OpcodeMarker.UNCOMPRESSED;

import java.util.function.BiFunction;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.Program;
import co.q64.jstx.lang.Stack;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.types.CompareType;
import co.q64.jstx.types.OperateType;

@Singleton
public class StandardOpcodes implements OpcodeRegistry {
	protected @Inject StandardOpcodes() {}

	protected @Inject LiteralFactory literal;
	protected @Inject Null nul;

	@Override
	public void register(Opcodes op) {
		op.reg("load 0", stack -> stack.push(literal.create("0")));
		op.reg("load 1", stack -> stack.push(literal.create("1")));
		op.reg("load 2", stack -> stack.push(literal.create("2")));
		op.reg("load 3", stack -> stack.push(literal.create("3")));
		op.reg("load 4", stack -> stack.push(literal.create("4")));
		op.reg("load 5", stack -> stack.push(literal.create("5")));
		op.reg("load 6", stack -> stack.push(literal.create("6")));
		op.reg("load 7", stack -> stack.push(literal.create("7")));
		op.reg("load 8", stack -> stack.push(literal.create("8")));
		op.reg("load 9", stack -> stack.push(literal.create("9")));
		op.reg("endif", ENDIF, stack -> {});
		op.reg("UNUSED literal begin", LITERAL, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal uncompressed", UNCOMPRESSED, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal compression mode 1", COMPRESSION1, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal compression mode 2", COMPRESSION2, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal compression mode 3", COMPRESSION3, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal begin 2 character", LITERAL1, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal begin 1 character", LITERAL2, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("if =", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.EQUAL)));
		op.reg("if !=", CONDITIONAL, stack -> conditional(stack, (v, o) -> !v.compare(o, CompareType.EQUAL)));
		op.reg("if >", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.GREATER)));
		op.reg("if >=", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.GREATER) || v.compare(o, CompareType.EQUAL)));
		op.reg("if <", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.LESS)));
		op.reg("if <=", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.LESS) || v.compare(o, CompareType.EQUAL)));
		op.reg("else", ELSE, stack -> processEsle(stack));
		op.reg("pop", stack -> stack.pop());
		op.reg("pop 2", stack -> stack.pop(2));
		op.reg("clr", stack -> stack.clr());
		op.reg("dup", stack -> stack.dup());
		op.reg("swp", stack -> stack.swap());
		op.reg("load true", stack -> stack.push("true"));
		op.reg("load false", stack -> stack.push("false"));
		op.reg("ldv", stack -> stack.push(stack.getProgram().getRegisters().getGlobal().get(stack.pop())));
		op.reg("sdv", stack -> stack.getProgram().getRegisters().getGlobal().put(stack.pop(), stack.pop()));
		op.reg("ldr a", stack -> stack.push(stack.getProgram().getRegisters().getA()));
		op.reg("ldr b", stack -> stack.push(stack.getProgram().getRegisters().getB()));
		op.reg("ldr c", stack -> stack.push(stack.getProgram().getRegisters().getC()));
		op.reg("ldr d", stack -> stack.push(stack.getProgram().getRegisters().getD()));
		op.reg("ldr i", stack -> stack.push(stack.getProgram().getRegisters().getI()));
		op.reg("ldr o", stack -> stack.push(stack.getProgram().getRegisters().getO()));
		op.reg("sdr a", stack -> stack.getProgram().getRegisters().setA(stack.pop()));
		op.reg("sdr b", stack -> stack.getProgram().getRegisters().setB(stack.pop()));
		op.reg("sdr c", stack -> stack.getProgram().getRegisters().setC(stack.pop()));
		op.reg("sdr d", stack -> stack.getProgram().getRegisters().setD(stack.pop()));
		op.reg("iterate", stack -> stack.getProgram().iterate(false));
		op.reg("iterate stack", stack -> stack.getProgram().iterate(true));
		op.reg("end", END, stack -> stack.getProgram().end());
		op.reg("print", stack -> stack.getProgram().getOutput().print(stack.pop().toString()));
		op.reg("println", stack -> stack.getProgram().getOutput().println(stack.pop().toString()));
		op.reg("exit", stack -> stack.getProgram().terminate());
		op.reg("terminate", stack -> stack.getProgram().terminateNoPrint());
		op.reg("restart", stack -> stack.getProgram().jumpToNode(1));
		op.reg("jump", stack -> stack.getProgram().jump(stack.pop().asInt()));
		op.reg("return", stack -> stack.getProgram().jumpReturn());
		op.reg("+", stack -> stack.push(stack.peek(2).operate(stack.pull(2), OperateType.PLUS)));
		op.reg("-", stack -> stack.push(stack.peek(2).operate(stack.pull(2), OperateType.MINUS)));
		op.reg("*", stack -> stack.push(stack.peek(2).operate(stack.pull(2), OperateType.MULTIPLY)));
		op.reg("/", stack -> stack.push(stack.peek(2).operate(stack.pull(2), OperateType.DIVIDE)));
		op.reg("%", stack -> stack.push(stack.peek(2).asInt() % stack.pull(2).asInt()));
		op.reg("nop", stack -> {});
		// 0x37

	}

	private void processEsle(Stack stack) {
		Program p = stack.getProgram();
		if (p.isLastConditional()) {
			p.jumpToEndif();
		}
	}

	private void conditional(Stack stack, BiFunction<Value, Value, Boolean> func) {
		Value a = stack.pop();
		Value b = stack.pop();
		Program p = stack.getProgram();
		if (func.apply(a, b)) {
			p.updateLastConditional(true);
			return;
		}
		p.updateLastConditional(false);
		p.jumpToEndif();
	}
}
