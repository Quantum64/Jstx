package co.q64.jstx.opcode.x00;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.inject.types.OperateType;
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
		op.reg("iterate", Chars.x2a, stack -> {
			stack.getProgram().iterate(false);
		});
		op.reg("iterate stack", Chars.x2b, stack -> {
			stack.getProgram().iterate(true);
		});
		op.reg("end", Chars.x2c, stack -> {
			stack.getProgram().end();
		});
		op.reg("print", Chars.x2d, stack -> {
			stack.getProgram().getOutput().print(stack.pop().toString());
		});
		op.reg("println", Chars.x2e, stack -> {
			stack.getProgram().getOutput().println(stack.pop().toString());
		});
		op.reg("exit", Chars.x2f, stack -> {
			stack.getProgram().terminate();
		});
		op.reg("terminate", Chars.x30, stack -> {
			stack.getProgram().terminateNoPrint();
		});
		op.reg("restart", Chars.x31, stack -> {
			stack.getProgram().jumpToNode(1);
		});
		op.reg("jump", Chars.x32, stack -> {
			stack.getProgram().jumpToNode(stack.pop().asInt());
		});
		op.reg("+", Chars.x33, stack -> {
			stack.push(stack.peek(2).operate(stack.pull(2), OperateType.PLUS));
		});
		op.reg("-", Chars.x34, stack -> {
			stack.push(stack.peek(2).operate(stack.pull(2), OperateType.MINUS));
		});
		op.reg("*", Chars.x35, stack -> {
			stack.push(stack.peek(2).operate(stack.pull(2), OperateType.MULTIPLY));
		});
		op.reg("/", Chars.x36, stack -> {
			stack.push(stack.peek(2).operate(stack.pull(2), OperateType.DIVIDE));
		});
		op.reg("%", Chars.x37, stack -> {
			stack.push(stack.peek(2).asInt() % stack.pull(2).asInt());
		});
	}
}
