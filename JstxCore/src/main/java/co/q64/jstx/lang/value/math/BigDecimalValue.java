package co.q64.jstx.lang.value.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.factory.LiteralFactoryFactory;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.types.Comparison;
import co.q64.jstx.types.Operation;
import lombok.Getter;

@AutoFactory
public class BigDecimalValue implements Value {
	private @Getter BigDecimal value;
	private LiteralFactoryFactory literal;

	protected BigDecimalValue(@Provided LiteralFactoryFactory literal, BigDecimal value) {
		this.literal = literal;
		this.value = value;
	}

	protected BigDecimalValue(@Provided LiteralFactoryFactory literal) {
		this.literal = literal;
		this.value = BigDecimal.valueOf(0);
	}

	protected BigDecimalValue(@Provided LiteralFactoryFactory literal, String value) {
		this.literal = literal;
		this.value = new BigDecimal(value);
	}

	@Override
	public boolean compare(Value to, Comparison type) {
		int result = 0;
		if (to instanceof BigDecimalValue) {
			result = value.compareTo(((BigDecimalValue) to).getValue());
		} else if (to.isInteger()) {
			result = value.compareTo(BigDecimal.valueOf(to.asLong()));
		}
		if (to instanceof BigDecimalValue || to.isInteger()) {
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
		BigDecimal target = null;
		if (on instanceof BigDecimalValue) {
			target = ((BigDecimalValue) on).getValue();
		} else if (on.isFloat()) {
			target = BigDecimal.valueOf(on.asDouble());
		}
		if (target == null) {
			return this;
		}
		switch (type) {
		case DIVIDE:
			return new BigDecimalValue(literal, value.divide(target));
		case SUBTRACT:
			return new BigDecimalValue(literal, value.subtract(target));
		case MULTIPLY:
			return new BigDecimalValue(literal, value.multiply(target));
		case ADD:
			return new BigDecimalValue(literal, value.add(target));
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
