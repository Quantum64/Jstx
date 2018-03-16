package co.q64.jstx.opcode.x00;

import java.util.function.BiFunction;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.inject.types.CompareType;
import co.q64.jstx.lang.Program;
import co.q64.jstx.lang.Stack;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class ConditionalOpcodes implements OpcodeRegistry {
	protected @Inject ConditionalOpcodes() {}

	@Override
	public void init(Opcodes op) {
		op.reg("if =", Chars.x12, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.EQUAL)));
		op.reg("if !=", Chars.x13, stack -> conditional(stack, (v, o) -> !v.compare(o, CompareType.EQUAL)));
		op.reg("if >", Chars.x14, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.GREATER)));
		op.reg("if >=", Chars.x15, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.GREATER) || v.compare(o, CompareType.EQUAL)));
		op.reg("if <", Chars.x16, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.LESS)));
		op.reg("if <=", Chars.x17, stack -> conditional(stack, (v, o) -> v.compare(o, CompareType.LESS) || v.compare(o, CompareType.EQUAL)));

		op.reg("else", Chars.x18, stack -> {
			Program p = stack.getProgram();
			if (p.isLastConditional()) {
				p.jumpToEndif();
			}
		});
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
