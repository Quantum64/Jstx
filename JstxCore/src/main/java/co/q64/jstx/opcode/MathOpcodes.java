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
		oc.reg("math.e", stack -> stack.push(Math.E), "Push the number e where the n-th derivative of f(x)=e^x equals the function itself, Euler's number.");
		oc.reg("math.pi", stack -> stack.push(Math.PI), "Push the ratio of the circumference of any circle to its diameter, the number Pi.");
		oc.reg("math.tau", stack -> stack.push(Math.PI * 2), "Push the ratio of the circumference of any circle to its radius, the number Tau.");

		oc.reg("math.abs", stack -> stack.push(Math.abs(stack.pop().asLong())), "Push the absolute integer value of the first stack value.");
		oc.reg("math.absf", stack -> stack.push(Math.abs(stack.pop().asDouble())), "Push the absolute floating point value of the first stack value.");
		oc.reg("math.acos", stack -> stack.push(Math.acos(stack.pop().asDouble())), "Push the inverse cosine of the first stack value.");
		oc.reg("math.asin", stack -> stack.push(Math.asin(stack.pop().asDouble())), "Push the inverse sine of the first stack value.");
		oc.reg("math.atan", stack -> stack.push(Math.atan(stack.pop().asDouble())), "Push the inverse tangent of the first stack value.");
		oc.reg("math.atan2", stack -> stack.push(Math.atan2(stack.peek(2).asDouble(), stack.pull(2).asDouble())), "Push the angle component of the second and first stack values (x, y) converted to polar coordinates.");
		oc.reg("math.cbrt", stack -> stack.push(Math.cbrt(stack.pop().asDouble())), "Push the cube root of the first stack value.");
		oc.reg("math.ceil", stack -> stack.push(Math.ceil(stack.pop().asDouble())), "Push the first stack value rounded up to the next integer.");
		oc.reg("math.copySign", stack -> stack.push(Math.copySign(stack.peek(2).asDouble(), stack.pull(2).asDouble())), "Push the second stack value with the sign of the first stack value, as floating point numbers.");
		oc.reg("math.cos", stack -> stack.push(Math.cos(stack.pop().asDouble())), "Push the cosine of the first stack value.");
		oc.reg("math.cosh", stack -> stack.push(Math.cosh(stack.pop().asDouble())), "Push the hyperbolic cosine of the first stack value.");
		oc.reg("math.exp", stack -> stack.push(Math.exp(stack.pop().asDouble())), "Push Euler's number to the power of the first stack value.");
		oc.reg("math.expm1", stack -> stack.push(Math.expm1(stack.pop().asDouble())), "Push Euler's number to the power of the first stack value, minus one.");
		oc.reg("math.floor", stack -> stack.push(Math.floor(stack.pop().asDouble())), "Push the first stack value rounded down to the previous integer.");
		oc.reg("math.floorDiv", stack -> stack.push(Math.floorDiv(stack.peek(2).asInt(), stack.pull(2).asInt())), "Push the quotient of the second and first stack values rounded up to the next integer.");
		oc.reg("math.floorMod", stack -> stack.push(Math.floorMod(stack.peek(2).asInt(), stack.pull(2).asInt())), "Push the modulus of the second and first stack values rounded down to the previous integer.");
		oc.reg("math.hypot", stack -> stack.push(Math.hypot(stack.peek(2).asDouble(), stack.pull(2).asDouble())), "Push the length of the hypotenuse of the right triangle formed by legs of the lengths of the first two stack values.");
		oc.reg("math.log", stack -> stack.push(Math.log(stack.pop().asDouble())), "Push the natural logarithm of the first stack value.");
		oc.reg("math.log10", stack -> stack.push(Math.log10(stack.pop().asDouble())), "Push the base 10 logarithm of the first stack value.");
		oc.reg("math.log1p", stack -> stack.push(Math.log1p(stack.pop().asDouble())), "Push the natural logarithm of one plus the first stack value.");
		oc.reg("math.max", stack -> stack.push(Math.max(stack.pop().asInt(), stack.pop().asInt())), "Push the greater of the first two stack values.");
		oc.reg("math.maxf", stack -> stack.push(Math.max(stack.pop().asDouble(), stack.pop().asDouble())), "Push the greater of the first two stack values as floating point numbers.");
		oc.reg("math.min", stack -> stack.push(Math.min(stack.pop().asInt(), stack.pop().asInt())), "Push the lesser of the first two stack values.");
		oc.reg("math.minf", stack -> stack.push(Math.min(stack.pop().asDouble(), stack.pop().asDouble())), "Push the lesser of the first two stack values as floating point numbers.");
		oc.reg("math.pow", stack -> stack.push(Math.pow(stack.peek(2).asDouble(), stack.pull(2).asDouble())), "Push the second stack value to the power of the first stack value.");
		oc.reg("math.random", stack -> stack.push(Math.random()), "Push a random floating point number between 0 and 1.");
		oc.reg("math.rint", stack -> stack.push(Math.rint(stack.pop().asDouble())), "Push a floating point number equal to the nearest integer value of the first stack value.");
		oc.reg("math.round", stack -> stack.push(Math.round(stack.pop().asDouble())), "Push the first stack value rounded to an integer.");
		oc.reg("math.scalb", stack -> stack.push(Math.scalb(stack.peek(2).asDouble(), stack.pull(2).asInt())), "Push the product of the second stack value and two to the power of the first stack value.");
		oc.reg("math.signum", stack -> stack.push(Math.signum(stack.pop().asDouble())), "Push 1 if the first stack value is greater than 0, -1 if the first stack value is less than 0, and 0 if the first stack value is equal to 0. The signum function.");
		oc.reg("math.sin", stack -> stack.push(Math.sin(stack.pop().asDouble())), "Push the sine of the first stack value.");
		oc.reg("math.sinh", stack -> stack.push(Math.sinh(stack.pop().asDouble())), "Push the hyperbolic sine of the first stack value.");
		oc.reg("math.sqrt", stack -> stack.push(Math.sqrt(stack.pop().asDouble())), "Push the square root of the first stack value.");
		oc.reg("math.tan", stack -> stack.push(Math.tan(stack.pop().asDouble())), "Push the tangent of the first stack value.");
		oc.reg("math.tanh", stack -> stack.push(Math.tanh(stack.pop().asDouble())), "Push the hyperbolic tangent of the first stack value.");
		oc.reg("math.toDegrees", stack -> stack.push(Math.toDegrees(stack.pop().asDouble())), "Push the degree value of the first stack value interpreted as radians.");
		oc.reg("math.toRadians", stack -> stack.push(Math.toRadians(stack.pop().asDouble())), "Push the radian value of the first stack value interpreted as degrees.");

		oc.reg("math.triangular", stack -> stack.push((stack.peek().asInt() * (stack.pop().asInt() + 1)) / 2), "Push the triangular sum of the first stack value.");
		oc.reg("math.digitalRoot", stack -> stack.push(stack.pop().asInt() - (9 * Math.round(Math.floor((stack.peek().asInt() - 1) / 9f)))), "Push the digital root of the first stack value.");
		oc.reg("math.digitSum", stack -> stack.push(stack.pop().toString().chars().mapToObj(c -> ((char) c)).map(Object::toString).mapToInt(Integer::parseInt).sum()), "Push the sum of the digits of the first stack value.");
		oc.reg("math.castNines", stack -> stack.push(stack.pop().toString().chars().mapToObj(c -> ((char) c)).map(Object::toString).mapToInt(Integer::parseInt).filter(i -> i != 9).sum()), "Push the sum of the digits of the first stack value, casting out nines.");

		oc.reg("math.fibonacci", stack -> {
			int a = 0, b = 1, term = stack.pop().asInt();
			while (a < term) {
				a = a + b;
				b = a - b;
			}
			stack.push(a);
		}, "Push the fibonacci number indexed at the first stack value.");
	}
}
