package co.q64.jstx.opcode.x38;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class MathOpcodes implements OpcodeRegistry {
	protected @Inject MathOpcodes() {}

	@Override
	public void init(Opcodes oc) {
		oc.reg("math.abs", code(Chars.x00), stack -> stack.push(Math.abs(stack.pop().asLong())));
		oc.reg("math.absf", code(Chars.x01), stack -> stack.push(Math.abs(stack.pop().asDouble())));
		oc.reg("math.acos", code(Chars.x02), stack -> stack.push(Math.acos(stack.pop().asDouble())));
		oc.reg("math.asin", code(Chars.x03), stack -> stack.push(Math.asin(stack.pop().asDouble())));
		oc.reg("math.atan", code(Chars.x04), stack -> stack.push(Math.atan(stack.pop().asDouble())));
		oc.reg("math.atan2", code(Chars.x05), stack -> stack.push(Math.atan2(stack.peek(2).asDouble(), stack.pull(2).asDouble())));

		oc.reg("math.triangular", code(Chars.xfa), stack -> stack.push((stack.peek().asInt() * (stack.pop().asInt() + 1)) / 2));
	}

	private List<Chars> code(Chars code) {
		return Arrays.asList(Chars.x38, code);
	}
}
