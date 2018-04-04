package co.q64.jstx.lang.value.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.factory.LiteralFactoryFactory;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.types.Comparison;
import co.q64.jstx.types.Operation;
import lombok.Getter;

@AutoFactory
public class BigIntegerValue implements Value {
	private @Getter BigInteger value;
	private LiteralFactoryFactory literal;

	protected BigIntegerValue(@Provided LiteralFactoryFactory literal, BigInteger value) {
		this.literal = literal;
		this.value = value;
	}

	protected BigIntegerValue(@Provided LiteralFactoryFactory literal) {
		this.literal = literal;
		this.value = BigInteger.valueOf(0);
	}

	protected BigIntegerValue(@Provided LiteralFactoryFactory literal, String value) {
		this.literal = literal;
		this.value = new BigInteger(value);
	}

	@Override
	public boolean compare(Value to, Comparison type) {
		int result = 0;
		if (to instanceof BigIntegerValue) {
			result = value.compareTo(((BigIntegerValue) to).getValue());
		} else if (to.isInteger()) {
			result = value.compareTo(BigInteger.valueOf(to.asLong()));
		}
		if (to instanceof BigIntegerValue || to.isInteger()) {
			switch (type) {
			case EQUAL:
				return result == 0;
			case GREATER:
				return result > 0;
			case LESS:
				return result < 0;
			}
		}
		return false;
	}

	@Override
	public Value operate(Value on, Operation type) {
		BigInteger target = null;
		if (on instanceof BigIntegerValue) {
			target = ((BigIntegerValue) on).getValue();
		} else if (on.isInteger()) {
			target = BigInteger.valueOf(on.asLong());
		}
		if (target == null) {
			return this;
		}
		switch (type) {
		case DIVIDE:
			return new BigIntegerValue(literal, value.divide(target));
		case SUBTRACT:
			return new BigIntegerValue(literal, value.subtract(target));
		case MULTIPLY:
			return new BigIntegerValue(literal, value.multiply(target));
		case ADD:
			return new BigIntegerValue(literal, value.add(target));
		}
		return this;
	}

	// Should this operation even be allowed?...
	@Override
	public List<Value> iterate() {
		List<Value> result = new ArrayList<>();
		for (long l = 0; l < getValue().longValue(); l++) {
			result.add(literal.getFactory().create(l));
		}
		return result;
	}

	public Value and(Value target) {
		return new BigIntegerValue(literal, value.and(convert(target)));
	}

	public Value andNot(Value target) {
		return new BigIntegerValue(literal, value.andNot(convert(target)));
	}

	public List<Value> divideAndRemainder(Value target) {
		return Arrays.stream(value.divideAndRemainder(convert(target))).map(v -> new BigIntegerValue(literal, v)).collect(Collectors.toList());
	}

	public Value gcd(Value target) {
		return new BigIntegerValue(literal, value.gcd(convert(target)));
	}

	public Value max(Value target) {
		return new BigIntegerValue(literal, value.max(convert(target)));
	}

	public Value min(Value target) {
		return new BigIntegerValue(literal, value.min(convert(target)));
	}

	public Value mod(Value target) {
		return new BigIntegerValue(literal, value.mod(convert(target)));
	}

	public Value modInverse(Value target) {
		return new BigIntegerValue(literal, value.modInverse(convert(target)));
	}

	public Value modPow(Value target, Value pow) {
		return new BigIntegerValue(literal, value.modPow(convert(target), convert(pow)));
	}

	public Value or(Value target) {
		return new BigIntegerValue(literal, value.or(convert(target)));
	}

	public Value remainder(Value target) {
		return new BigIntegerValue(literal, value.min(convert(target)));
	}

	public Value xor(Value target) {
		return new BigIntegerValue(literal, value.xor(convert(target)));
	}

	private BigInteger convert(Value value) {
		if (value instanceof BigIntegerValue) {
			return ((BigIntegerValue) value).getValue();
		}
		if (value.isInteger()) {
			return BigInteger.valueOf(value.asLong());
		}
		throw new IllegalArgumentException("Could not convert " + value + " to a BigInteger");
	}

	@Override
	public int asInt() {
		return value.intValue();
	}

	@Override
	public long asLong() {
		return value.longValue();
	}

	@Override
	public char asChar() {
		return (char) value.intValue();
	}

	@Override
	public double asDouble() {
		return value.longValue();
	}

	@Override
	public boolean asBoolean() {
		return value.intValue() != 0;
	}

	@Override
	public boolean isBoolean() {
		return false;
	}

	@Override
	public boolean isFloat() {
		return true;
	}

	@Override
	public boolean isInteger() {
		return true;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
