package co.q64.jstx.lang.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.auto.factory.AutoFactory;

import co.q64.jstx.types.Comparison;
import co.q64.jstx.types.Operation;
import lombok.EqualsAndHashCode;

@AutoFactory
@EqualsAndHashCode
public class Literal implements Value {
	private String literal;

	private Literal(String literal) {
		this.literal = literal;
	}

	protected Literal(Object literal) {
		this(literal.toString());
	}

	protected Literal(List<Object> list) {
		this("[" + list.stream().map(Object::toString).collect(Collectors.joining(",")) + "]");
	}

	@Override
	public boolean compare(Value value, Comparison type) {
		if (isFloat() && value.isFloat()) {
			if (isInteger() && value.isInteger()) {
				switch (type) {
				case EQUAL:
					return asLong() == value.asLong();
				case GREATER:
					return asLong() > value.asLong();
				case LESS:
					return asLong() < value.asLong();
				}
			}
			switch (type) {
			case EQUAL:
				return asDouble() == value.asDouble();
			case GREATER:
				return asDouble() > value.asDouble();
			case LESS:
				return asDouble() < value.asDouble();
			}
		}
		if (isBoolean() && value.isBoolean()) {
			switch (type) {
			case EQUAL:
				return asBoolean() == value.asBoolean();
			case GREATER:
				return asBoolean() == value.asBoolean();
			case LESS:
				return asBoolean() == value.asBoolean();
			}
		}
		if (value.isBoolean() && isInteger()) {
			switch (type) {
			case EQUAL:
				return value.asBoolean() == true ? asInt() != 0 : asInt() == 0;
			default:
				return false;
			}
		}
		if (isBoolean() && value.isInteger()) {
			switch (type) {
			case EQUAL:
				return asBoolean() == true ? value.asInt() != 0 : value.asInt() == 0;
			default:
				return false;
			}
		}
		switch (type) {
		case EQUAL:
			return toString().equals(value.toString());
		case GREATER:
			return toString().length() > value.toString().length();
		case LESS:
			return toString().length() < value.toString().length();
		}
		return false;
	}

	@Override
	public String toString() {
		return literal;
	}

	@Override
	public List<Value> iterate() {
		List<Value> result = new ArrayList<>();
		if (literal.startsWith("[") && literal.endsWith("]")) {
			List<String> elements = new ArrayList<>();
			StringBuilder currentElement = new StringBuilder();
			char[] chars = literal.toCharArray();
			for (int i = 1; i < chars.length - 1; i++) {
				if (String.valueOf(chars[i]).equals(",")) {
					elements.add(currentElement.toString());
					currentElement = new StringBuilder();
					continue;
				}
				currentElement.append(chars[i]);
			}
			if (elements.size() > 0) {
				if (currentElement.length() > 0) {
					elements.add(currentElement.toString());
				}
				return elements.stream().map(Literal::new).collect(Collectors.toList());
			}
			return Arrays.asList(new Literal(currentElement.toString()));
		}
		if (isInteger()) {
			for (long l = 0; l < asLong(); l++) {
				result.add(new Literal(l));
			}
		} else {
			for (char c : literal.toCharArray()) {
				result.add(new Literal(c));
			}
		}
		return result;
	}

	@Override
	public Value operate(Value value, Operation type) {
		if ((isInteger() || isFloat()) && (value.isInteger() || value.isFloat())) {
			if (isInteger() && value.isInteger()) {
				switch (type) {
				case DIVIDE:
					return new Literal(asLong() / value.asLong());
				case SUBTRACT:
					return new Literal(asLong() - value.asLong());
				case MULTIPLY:
					return new Literal(asLong() * value.asLong());
				case ADD:
					return new Literal(asLong() + value.asLong());
				}
			} else {
				switch (type) {
				case DIVIDE:
					return new Literal(asDouble() / value.asDouble());
				case SUBTRACT:
					return new Literal(asDouble() - value.asDouble());
				case MULTIPLY:
					return new Literal(asDouble() * value.asDouble());
				case ADD:
					return new Literal(asDouble() + value.asDouble());
				}
			}
		}
		if (isBoolean() && value.isBoolean()) {
			switch (type) {
			case DIVIDE:
				return new Literal(asBoolean() == value.asBoolean());
			case SUBTRACT:
				return new Literal(asBoolean() != value.asBoolean());
			case MULTIPLY:
				return new Literal(asBoolean() == value.asBoolean());
			case ADD:
				return new Literal(asBoolean() == value.asBoolean());
			}
		}
		switch (type) {
		case DIVIDE:
			return new Literal(toString() + value.toString());
		case SUBTRACT:
			return new Literal(value.toString() + toString());
		case MULTIPLY:
			return new Literal(toString() + value.toString());
		case ADD:
			return new Literal(toString() + value.toString());
		}
		return new Literal(toString() + value.toString());
	}

	public boolean isInteger() {
		try {
			Long.parseLong(literal);
			return true;
		} catch (Exception e) {}
		return false;
	}

	public boolean isBoolean() {
		return literal.equalsIgnoreCase("true") || literal.equalsIgnoreCase("false") || literal.equals("1") || literal.equals("0");
	}

	public boolean isFloat() {
		try {
			Double.parseDouble(literal);
			return true;
		} catch (Exception e) {}
		return false;
	}

	@Override
	public int asInt() {
		try {
			return Integer.parseInt(literal);
		} catch (Exception e) {}
		if (isBoolean()) {
			return asBoolean() ? 1 : 0;
		}
		if (literal.length() == 1) {
			return literal.charAt(0);
		}
		return literal.length();
	}

	@Override
	public long asLong() {
		try {
			return Long.parseLong(literal);
		} catch (Exception e) {}
		if (isBoolean()) {
			return asBoolean() ? 1 : 0;
		}
		if (literal.length() <= 1) {
			return literal.charAt(0);
		}
		return literal.length();
	}

	@Override
	public double asDouble() {
		try {
			return Double.parseDouble(literal);
		} catch (Exception e) {}
		return 0;
	}

	@Override
	public boolean asBoolean() {
		if (literal.equals("1")) {
			return true;
		}
		return literal.equalsIgnoreCase("true");
	}

	@Override
	public char asChar() {
		if (literal.length() > 0) {
			return literal.charAt(0);
		}
		return 0;
	}
}
