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
		oc.reg("math.e", stack -> stack.push(Math.E));
		oc.reg("math.pi", stack -> stack.push(Math.PI));

		oc.reg("math.abs", stack -> stack.push(Math.abs(stack.pop().asLong())));
		oc.reg("math.absf", stack -> stack.push(Math.abs(stack.pop().asDouble())));
		oc.reg("math.acos", stack -> stack.push(Math.acos(stack.pop().asDouble())));
		oc.reg("math.asin", stack -> stack.push(Math.asin(stack.pop().asDouble())));
		oc.reg("math.atan", stack -> stack.push(Math.atan(stack.pop().asDouble())));
		oc.reg("math.atan2", stack -> stack.push(Math.atan2(stack.peek(2).asDouble(), stack.pull(2).asDouble())));
		oc.reg("math.cbrt", stack -> stack.push(Math.cbrt(stack.pop().asDouble())));
		oc.reg("math.ceil", stack -> stack.push(Math.ceil(stack.pop().asDouble())));
		oc.reg("math.copySign", stack -> stack.push(Math.copySign(stack.peek(2).asDouble(), stack.pull(2).asDouble())));
		oc.reg("math.cos", stack -> stack.push(Math.cos(stack.pop().asDouble())));
		oc.reg("math.cosh", stack -> stack.push(Math.cosh(stack.pop().asDouble())));
		oc.reg("math.exp", stack -> stack.push(Math.exp(stack.pop().asDouble())));
		oc.reg("math.expm1", stack -> stack.push(Math.expm1(stack.pop().asDouble())));
		oc.reg("math.floor", stack -> stack.push(Math.floor(stack.pop().asDouble())));
		oc.reg("math.floorDiv", stack -> stack.push(Math.floorDiv(stack.peek(2).asInt(), stack.pull(2).asInt())));
		oc.reg("math.floorMod", stack -> stack.push(Math.floorMod(stack.peek(2).asInt(), stack.pull(2).asInt())));
		oc.reg("math.hypot", stack -> stack.push(Math.hypot(stack.peek(2).asDouble(), stack.pull(2).asDouble())));
		oc.reg("math.log", stack -> stack.push(Math.log(stack.pop().asDouble())));
		oc.reg("math.log10", stack -> stack.push(Math.log10(stack.pop().asDouble())));
		oc.reg("math.log1p", stack -> stack.push(Math.log1p(stack.pop().asDouble())));
		oc.reg("math.max", stack -> stack.push(Math.max(stack.pop().asInt(), stack.pop().asInt())));
		oc.reg("math.maxf", stack -> stack.push(Math.max(stack.pop().asDouble(), stack.pop().asDouble())));
		oc.reg("math.min", stack -> stack.push(Math.min(stack.pop().asInt(), stack.pop().asInt())));
		oc.reg("math.minf", stack -> stack.push(Math.min(stack.pop().asDouble(), stack.pop().asDouble())));
		oc.reg("math.pow", stack -> stack.push(Math.pow(stack.peek(2).asDouble(), stack.pull(2).asDouble())));
		oc.reg("math.random", stack -> stack.push(Math.random()));
		oc.reg("math.rint", stack -> stack.push(Math.rint(stack.pop().asDouble())));
		oc.reg("math.round", stack -> stack.push(Math.round(stack.pop().asDouble())));
		oc.reg("math.scalb", stack -> stack.push(Math.scalb(stack.peek(2).asDouble(), stack.pull(2).asInt())));
		oc.reg("math.signum", stack -> stack.push(Math.signum(stack.pop().asDouble())));
		oc.reg("math.sin", stack -> stack.push(Math.sin(stack.pop().asDouble())));
		oc.reg("math.sinh", stack -> stack.push(Math.sinh(stack.pop().asDouble())));
		oc.reg("math.sqrt", stack -> stack.push(Math.sqrt(stack.pop().asDouble())));
		oc.reg("math.tan", stack -> stack.push(Math.tan(stack.pop().asDouble())));
		oc.reg("math.tanh", stack -> stack.push(Math.tanh(stack.pop().asDouble())));
		oc.reg("math.toDegrees", stack -> stack.push(Math.toDegrees(stack.pop().asDouble())));
		oc.reg("math.toRadians", stack -> stack.push(Math.toRadians(stack.pop().asDouble())));

		oc.reg("math.triangular", stack -> stack.push((stack.peek().asInt() * (stack.pop().asInt() + 1)) / 2));
	}
}
