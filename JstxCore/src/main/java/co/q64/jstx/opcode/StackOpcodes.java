package co.q64.jstx.opcode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Value;

@Singleton
public class StackOpcodes implements OpcodeRegistry {
	protected @Inject StackOpcodes() {}

	protected @Inject LiteralFactory literal;

	@Override
	public void register(Opcodes op) {
		op.reg("dup 2",  stack -> stack.dup(2));
		op.reg("dup 3",  stack -> stack.dup(3));
		op.reg("dup 4",  stack -> stack.dup(4));
		op.reg("dup 5",  stack -> stack.dup(5));
		op.reg("dup 6",  stack -> stack.dup(6));
		op.reg("dup 7",  stack -> stack.dup(7));
		op.reg("dup x",  stack -> stack.dup(stack.pop().asInt()));
		op.reg("lda 0",  stack -> stack.dup(stack.pop().asInt()));
		op.reg("lda 1",  stack -> stack.dup(stack.pop().asInt()));
		op.reg("lda 2",  stack -> stack.dup(stack.pop().asInt()));
		op.reg("lda 3",  stack -> stack.dup(stack.pop().asInt()));
		op.reg("lda 4",  stack -> stack.dup(stack.pop().asInt()));
		op.reg("lda 5",  stack -> stack.dup(stack.pop().asInt()));
		op.reg("lda 6",  stack -> stack.dup(stack.pop().asInt()));
		op.reg("lda x",  stack -> stack.dup(stack.pop().asInt()));
		op.reg("flatten",  stack -> {
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
		op.reg("flatten hard",  stack -> {
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
		op.reg("flatten soft",  stack -> {
			if (stack.size() <= 0) {
				return;
			}
			StringBuilder value = new StringBuilder();
			for (int i = 0; i < stack.size(); i++) {
				value.append(stack.peek(stack.size() - i));
				if (i < stack.size() - 1) {
					value.append(" ");
				}
			}
			stack.clr();
			stack.push(value.toString());
		});
		// @formatter:off
	}
}
