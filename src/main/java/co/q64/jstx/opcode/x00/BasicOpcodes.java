package co.q64.jstx.opcode.x00;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.inject.types.OperateType;
import co.q64.jstx.lang.Instruction;
import co.q64.jstx.lang.Program;
import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeFactory;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class BasicOpcodes implements OpcodeRegistry {
	protected @Inject BasicOpcodes() {}

	protected @Inject OpcodeFactory of;

	@Override
	public void init(Opcodes op) {
		op.accept(of.create("iterate", Chars.x2a, stack -> {
			stack.getProgram().iterate(false);
		}));
		op.accept(of.create("iterate stack", Chars.x2a, stack -> {
			stack.getProgram().iterate(true);
		}));
		op.accept(of.create("end", Chars.x2c, stack -> {
			stack.getProgram().end();
		}));
		op.accept(of.create("print", Chars.x2d, stack -> {
			System.out.print(stack.pop());
		}));
		op.accept(of.create("println", Chars.x2e, stack -> {
			System.out.println(stack.pop());
		}));
		op.accept(of.create("exit", Chars.x2f, stack -> {
			stack.getProgram().terminate();
		}));
		op.accept(of.create("terminate", Chars.x30, stack -> {
			stack.getProgram().terminateNoPrint();
		}));
		op.accept(of.create("restart", Chars.x31, stack -> {
			stack.getProgram().jumpToNode(1);
		}));
		op.accept(of.create("jump", Chars.x32, stack -> {
			stack.getProgram().jumpToNode(stack.pop().asInt());
		}));
		op.accept(of.create("+", Chars.x33, stack -> {
			stack.push(stack.pop().operate(stack.pop(), OperateType.PLUS));
		}));
		op.accept(of.create("-", Chars.x34, stack -> {
			stack.push(stack.pop().operate(stack.pop(), OperateType.MINUS));
		}));
		op.accept(of.create("*", Chars.x35, stack -> {
			stack.push(stack.pop().operate(stack.pop(), OperateType.MULTIPLY));
		}));
		op.accept(of.create("/", Chars.x36, stack -> {
			stack.push(stack.pop().operate(stack.pop(), OperateType.DIVIDE));
		}));
		op.accept(of.create("%", Chars.x37, stack -> {
			stack.push(stack.pop().operate(stack.pop(), OperateType.DIVIDE));
		}));
	}
}
