package co.q64.jstx.opcode;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.LiteralFactory;

@Singleton
public class MathOpcodes implements OpcodeRegistry {
	protected @Inject LiteralFactory literal;
	private Random random = new Random();

	protected @Inject MathOpcodes() {}

	@Override
	public void register(Opcodes oc) {
		oc.reg("math.shiftRight", stack -> stack.push(stack.peek(2).asLong() >> stack.pull(2).asLong()), "Push the second stack value bitshifted right by the first stack value.");
		oc.reg("math.shiftZeroRight", stack -> stack.push(stack.peek(2).asLong() >>> stack.pull(2).asLong()), "Push the second stack value bitshifted right and zero filled right by the first stack value.");
		oc.reg("math.shiftLeft", stack -> stack.push(stack.peek(2).asLong() << stack.pull(2).asLong()), "Push the second stack value bitshifted left by the first stack value.");
		oc.reg("math.shiftRightOne", stack -> stack.push(stack.pop().asLong() >> 1), "Push the second stack value bitshifted right by one.");
		oc.reg("math.shiftZeroRightOne", stack -> stack.push(stack.pop().asLong() >>> 1), "Push the second stack value bitshifted right and zero filled right by one.");
		oc.reg("math.shiftLeftOne", stack -> stack.push(stack.pop().asLong() << 1), "Push the second stack value bitshifted left by one.");
		oc.reg("math.and", stack -> stack.push(stack.peek(2).asLong() & stack.pull(2).asLong()), "Push the bitwise and of the second and first stack values.");
		oc.reg("math.or", stack -> stack.push(stack.peek(2).asLong() | stack.pull(2).asLong()), "Push the bitwise or of the second and first stack values.");
		oc.reg("math.xor", stack -> stack.push(stack.peek(2).asLong() ^ stack.pull(2).asLong()), "Push the bitwise exclusive or of the second and first stack values.");
		oc.reg("math.not", stack -> stack.push(~stack.pop().asLong()), "Push the bitwise not of the first stack value.");

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

		oc.reg("math.randInt", stack -> stack.push(random.nextInt()), "Push a random integer.");
		oc.reg("math.randInt100", stack -> stack.push(random.nextInt(100)), "Push a random integer between 0 and 99.");
		oc.reg("math.randInt2", stack -> stack.push(random.nextInt(2)), "Push a random integer than is either 0 or 1.");
		oc.reg("math.randByte", stack -> stack.push(random.nextInt(256)), "Push a random unsigned byte.");
		oc.reg("math.randBoolean", stack -> stack.push(random.nextBoolean()), "Push a random boolean.");
		oc.reg("math.randLong", stack -> stack.push(random.nextLong()), "Push a random long integer (-2^63 to 2^63-1).");
		oc.reg("math.randGaussian", stack -> stack.push(random.nextBoolean()), "Push a normally distributed floating point value around mean 0 and standard deviation 1.");
		oc.reg("math.randTo", stack -> stack.push(random.nextInt(stack.pop().asInt())), "Push a random integer between 0 and one less than the first stack value.");
		oc.reg("math.randRange", stack -> stack.push(random.nextInt(stack.pop().asInt() + 1) + stack.pop().asInt()), "Push a random integer between the second stack value and the first stack value.");
		oc.reg("math.randBytes", stack -> stack.push(IntStream.range(0, 100).map(i -> random.nextInt(256)).boxed().collect(Collectors.toList())), "Push a list of 100 random unsigned bytes.");
		oc.reg("math.randBytesTo", stack -> stack.push(IntStream.range(0, stack.pop().asInt()).map(i -> random.nextInt(256)).boxed().collect(Collectors.toList())), "Push a list of random unsigned bytes of the size of the first stack value.");

		oc.reg("math.triangular", stack -> stack.push((stack.peek().asInt() * (stack.pop().asInt() + 1)) / 2), "Push the triangular sum of the first stack value.");
		oc.reg("math.digitalRoot", stack -> stack.push(stack.pop().asInt() - (9 * Math.round(Math.floor((stack.peek().asInt() - 1) / 9f)))), "Push the digital root of the first stack value.");
		oc.reg("math.digitSum", stack -> stack.push(stack.pop().toString().chars().mapToObj(c -> ((char) c)).map(Object::toString).mapToInt(Integer::parseInt).sum()), "Push the sum of the digits of the first stack value.");
		oc.reg("math.castNines", stack -> stack.push(stack.pop().toString().chars().mapToObj(c -> ((char) c)).map(Object::toString).mapToInt(Integer::parseInt).filter(i -> i != 9).sum()), "Push the sum of the digits of the first stack value, casting out nines.");
		oc.reg("math.fibonacci", stack -> stack.push(fibonacci(stack.pop().asInt())), "Push the fibonacci number indexed at the first stack value.");
		oc.reg("math.fibonacciList", stack -> stack.push(IntStream.rangeClosed(1, stack.pop().asInt()).map(this::fibonacci).boxed().map(literal::create).collect(Collectors.toList())), "Push a list of fibonacci numbers of the length of the first stack value.");
		oc.reg("math.isPrime", stack -> stack.push(prime(stack.pop().asInt())), "Push true if the first stack value is prime, else false.");
		oc.reg("math.primeList", stack -> stack.push(IntStream.rangeClosed(2, stack.pop().asInt()).filter(this::prime).boxed().map(literal::create).collect(Collectors.toList())), "Push a list of primes that are less than or equal to the first stack value.");
		oc.reg("math.isEvil", stack -> stack.push(stack.peek().isInteger() && stack.peek().asInt() > 1 && (Integer.toBinaryString(stack.pop().asInt()).split("1", -1).length - 1) % 2 == 0), "Push true if the top stack value is evil, else false.");
		oc.reg("math.isInteger", stack -> stack.push(stack.pop().isInteger()), "Push true if the first stack value is an integer, else false.");
		oc.reg("math.isFloat", stack -> stack.push(stack.pop().isFloat()), "Push true if the first stack value is a floating point number, else false.");
		oc.reg("math.asInteger", stack -> stack.push(stack.pop().asInt()), "Push the first stack value converted to an integer.");
		oc.reg("math.asFloat", stack -> stack.push(stack.pop().asDouble()), "Push the first stack value converted to a floating point number.");
		oc.reg("math.incrementDigits", stack -> stack.push(stack.pop().toString().chars().map(i -> Integer.parseInt(Character.valueOf((char) i).toString()) + 1).mapToObj(String::valueOf).collect(Collectors.joining())), "Push the first stack value with digits incremented by 1.");
		oc.reg("math.decrementDigits", stack -> stack.push(stack.pop().toString().chars().map(i -> Integer.parseInt(Character.valueOf((char) i).toString()) - 1).mapToObj(String::valueOf).collect(Collectors.joining())), "Push the first stack value with digits decremented by 1.");
	}

	private boolean prime(int n) {
		if (n > 2 && (n & 1) == 0) {
			return false;
		}
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	private int fibonacci(int n) {
		int c = 0;
		for (int a = 0, b = 1, i = 0, term = n; i < term; i++) {
			c = a + b;
			a = b;
			b = c;
		}
		return c;
	}
}
