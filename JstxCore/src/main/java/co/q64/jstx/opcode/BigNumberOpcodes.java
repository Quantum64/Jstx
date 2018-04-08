package co.q64.jstx.opcode;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.lang.value.math.BigIntegerValue;
import co.q64.jstx.lang.value.math.BigIntegerValueFactory;
import co.q64.jstx.types.Operation;

public class BigNumberOpcodes implements OpcodeRegistry {
	protected @Inject LiteralFactory literal;
	protected @Inject BigIntegerValueFactory bigIntFactory;

	protected @Inject BigNumberOpcodes() {}

	@Override
	public void register(Opcodes oc) {
		oc.reg("bigint.new", stack -> stack.push(bigIntFactory.create()), "Push a BigInteger with a value of 0.");
		oc.reg("bigint.of", stack -> stack.push(bigIntFactory.create(stack.pop().toString())), "Push a BigInteger with a value of the first stack value.");
		oc.reg("bigint.add", stack -> stack.push(bigIntFactory.create(stack.peek(2).operate(stack.pull(2), Operation.ADD).toString())), "Push a BigInteger that is the sum of the first and second stack values.");
		oc.reg("bigint.subtract", stack -> stack.push(bigIntFactory.create(stack.peek(2).operate(stack.pull(2), Operation.SUBTRACT).toString())), "Push a BigInteger that is the difference of the first and second stack values.");
		oc.reg("bigint.multiply", stack -> stack.push(bigIntFactory.create(stack.peek(2).operate(stack.pull(2), Operation.MULTIPLY).toString())), "Push a BigInteger that is the product of the first and second stack values.");
		oc.reg("bigint.divide", stack -> stack.push(bigIntFactory.create(stack.peek(2).operate(stack.pull(2), Operation.DIVIDE).toString())), "Push a BigInteger that is the quotient of the first and second stack values.");
		oc.reg("bigint.and", stack -> stack.push(bigIntValue(stack.peek(2)).and(stack.pull(2))), "Push the bitwise and of the second and first stack values.");
		oc.reg("bigint.andNot", stack -> stack.push(bigIntValue(stack.peek(2)).andNot(stack.pull(2))), "Push the bitwise and of the second and the bitwise negated first stack values.");
		oc.reg("bigint.bitCount", stack -> stack.push(bigIntFactory.create(String.valueOf(bigInt(stack.pop()).bitCount()))), "Push the bit count of the first stack value.");
		oc.reg("bigint.bitLength", stack -> stack.push(bigIntFactory.create(String.valueOf(bigInt(stack.pop()).bitLength()))), "Push the bit length of the first stack value.");
		oc.reg("bigint.clearBit", stack -> stack.push(bigIntFactory.create(bigInt(stack.peek(2)).clearBit(stack.pull(2).asInt()))), "Push second stack value with the bit indexed by the first stack value cleared.");
		oc.reg("bigint.divideAndRemainder", stack -> stack.push(bigIntValue(stack.peek(2)).and(stack.pull(2))), "Push a list of the quotient of the second and first stack values and the remainder produced by that operation.");
		oc.reg("bigint.flipBit", stack -> stack.push(bigIntFactory.create(bigInt(stack.peek(2)).clearBit(stack.pull(2).asInt()))), "Push second stack value with the bit indexed by the first stack value flipped.");
		oc.reg("bigint.gcd", stack -> stack.push(bigIntValue(stack.peek(2)).gcd(stack.pull(2))), "Push the greatest common denominator of the second and first stack values.");
		oc.reg("bigint.getLowestSetBit", stack -> stack.push(bigIntFactory.create(String.valueOf(bigInt(stack.pop()).getLowestSetBit()))), "Push the lowest set bit of the first stack value.");
		oc.reg("bigint.max", stack -> stack.push(bigIntValue(stack.peek(2)).max(stack.pull(2))), "Push the greater of the second and first stack values.");
		oc.reg("bigint.min", stack -> stack.push(bigIntValue(stack.peek(2)).min(stack.pull(2))), "Push the lesser of the second and first stack values.");
		oc.reg("bigint.mod", stack -> stack.push(bigIntValue(stack.peek(2)).mod(stack.pull(2))), "Push the modulus of the second and first stack values.");
		oc.reg("bigint.modInverse", stack -> stack.push(bigIntValue(stack.peek(2)).modInverse(stack.pull(2))), "Push the modulus of the reciprocal of the second stack value and first stack value.");
		oc.reg("bigint.modPow", stack -> stack.push(bigIntValue(stack.peek(3)).modPow(stack.peek(2), stack.pull(3))), "Push the modulus of the third stack value to the power of the second stack value and the first stack value.");
		oc.reg("bigint.negate", stack -> stack.push(bigIntFactory.create(bigInt(stack.pop()).negate())), "Push the first stack value with the sign flipped.");
		oc.reg("bigint.not", stack -> stack.push(bigIntFactory.create(bigInt(stack.pop()).not())), "Push the bitwise not of the first stack value.");
		oc.reg("bigint.or", stack -> stack.push(bigIntValue(stack.peek(2)).or(stack.pull(2))), "Push the bitwise or of the second and first stack values.");
		oc.reg("bigint.pow", stack -> stack.push(bigIntFactory.create(bigInt(stack.peek(2)).pow(stack.pull(2).asInt()))), "Push the first stack value to the power of the second stack value.");
		oc.reg("bigint.remainder", stack -> stack.push(bigIntValue(stack.peek(2)).remainder(stack.pull(2))), "Push the remainder of the quotient of the second and first stack values.");
		oc.reg("bigint.setBit", stack -> stack.push(bigIntFactory.create(bigInt(stack.peek(2)).setBit(stack.pull(2).asInt()))), "Push second stack value with the bit indexed by the first stack value set.");
		oc.reg("bigint.shiftLeft", stack -> stack.push(bigIntFactory.create(bigInt(stack.peek(2)).shiftLeft(stack.pull(2).asInt()))), "Push second stack value with the bits shifted left by the first stack value.");
		oc.reg("bigint.shiftRight", stack -> stack.push(bigIntFactory.create(bigInt(stack.peek(2)).shiftRight(stack.pull(2).asInt()))), "Push second stack value with its bits shifted right by the first stack value.");
		oc.reg("bigint.signum", stack -> stack.push(bigIntFactory.create(String.valueOf(bigInt(stack.pop()).signum()))), "Push 1 if the first stack value is greater than 0, -1 if the first stack value is less than 0, and 0 if the first stack value is equal to 0. The signum function.");
		oc.reg("bigint.testBit", stack -> stack.push(bigInt(stack.peek(2)).testBit(stack.pull(2).asInt())), "Push true if the bit of the second stack value indexed by the first stack value is set, else false.");
		oc.reg("bigint.xor", stack -> stack.push(bigIntValue(stack.peek(2)).xor(stack.pull(2))), "Push the bitwise exclusive or of the second and first stack values.");
		oc.reg("bigint.shiftLeftOne", stack -> stack.push(bigIntFactory.create(bigInt(stack.pop()).shiftLeft(1))), "Push first stack value with the bits shifted left by one.");
		oc.reg("bigint.shiftRightOne", stack -> stack.push(bigIntFactory.create(bigInt(stack.pop()).shiftRight(1))), "Push first stack value with its bits shifted right by one.");

		oc.reg("bigint.toByteArray", stack -> {
			byte[] bytes = bigInt(stack.peek()).toByteArray();
			stack.push(IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).map(literal::create).collect(Collectors.toList()));
		}, "Push a list of bytes that make up the first stack value.");

		oc.reg("bigint.toUnsignedByteArray", stack -> {
			byte[] bytes = bigInt(stack.peek()).toByteArray();
			stack.push(IntStream.range(0, bytes.length).mapToObj(i -> bytes[i] & 0xff).map(literal::create).collect(Collectors.toList()));
		}, "Push a list of unsigned bytes that make up the first stack value.");

		oc.reg("bigint.fromBytes", stack -> {
			List<Byte> lst = stack.pop().iterate().stream().map(Value::asInt).map(i -> i.byteValue()).collect(Collectors.toList());
			byte[] bytes = new byte[lst.size()];
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = lst.get(i);
			}
			stack.push(bigIntFactory.create(new BigInteger(bytes)));
		}, "Push a BigInteger from a big-endian byte list on the first stack value.");
	}

	private BigInteger bigInt(Value value) {
		return bigIntValue(value).getValue();
	}

	private BigIntegerValue bigIntValue(Value value) {
		if (value instanceof BigIntegerValue) {
			return ((BigIntegerValue) value);
		}
		if (value.isInteger()) {
			return bigIntFactory.create(value.toString());
		}
		throw new IllegalArgumentException("Opcode could not convert " + value + " to a BigInteger");
	}
}
