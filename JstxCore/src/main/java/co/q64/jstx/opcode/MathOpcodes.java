package co.q64.jstx.opcode;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;

@Singleton
public class MathOpcodes implements OpcodeRegistry {
	protected @Inject MathOpcodes() {}

	@Override
	public void register(Opcodes oc) {
		oc.reg("math.abs",  stack -> stack.push(Math.abs(stack.pop().asLong())));
		oc.reg("math.absf",  stack -> stack.push(Math.abs(stack.pop().asDouble())));
		oc.reg("math.acos",  stack -> stack.push(Math.acos(stack.pop().asDouble())));
		oc.reg("math.asin",  stack -> stack.push(Math.asin(stack.pop().asDouble())));
		oc.reg("math.atan",  stack -> stack.push(Math.atan(stack.pop().asDouble())));
		oc.reg("math.atan2",  stack -> stack.push(Math.atan2(stack.peek(2).asDouble(), stack.pull(2).asDouble())));

		oc.reg("math.triangular",  stack -> stack.push((stack.peek().asInt() * (stack.pop().asInt() + 1)) / 2));
	}
}
