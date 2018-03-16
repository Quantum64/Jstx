package co.q64.jstx.lang.value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.auto.factory.AutoFactory;

import co.q64.jstx.inject.types.CompareType;
import co.q64.jstx.inject.types.OperateType;

@AutoFactory
public class Literal implements Value {
	private String literal;

	protected Literal(String literal) {
		this.literal = literal;
	}

	protected Literal(Object literal) {
		this(literal.toString());
	}

	@Override
	public boolean compare(Value value, CompareType type) {
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
		List<String> elements = new ArrayList<>();
		StringBuilder currentElement = new StringBuilder();
		char[] chars = literal.toCharArray();
		for (int i = 0; i < chars.length; i++) {
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
	public Value operate(Value value, OperateType type) {
		if ((isInteger() || isFloat()) && (value.isInteger() || value.isFloat())) {
			if (isInteger() && value.isInteger()) {
				switch (type) {
				case DIVIDE:
					return new Literal(asLong() / value.asLong());
				case MINUS:
					return new Literal(asLong() - value.asLong());
				case MULTIPLY:
					return new Literal(asLong() * value.asLong());
				case PLUS:
					return new Literal(asLong() + value.asLong());
				}
			} else {
				switch (type) {
				case DIVIDE:
					return new Literal(asDouble() / value.asDouble());
				case MINUS:
					return new Literal(asDouble() - value.asDouble());
				case MULTIPLY:
					return new Literal(asDouble() * value.asDouble());
				case PLUS:
					return new Literal(asDouble() + value.asDouble());
				}
			}
		}
		if (isBoolean() && value.isBoolean()) {
			switch (type) {
			case DIVIDE:
				return new Literal(asBoolean() == value.asBoolean());
			case MINUS:
				return new Literal(asBoolean() != value.asBoolean());
			case MULTIPLY:
				return new Literal(asBoolean() == value.asBoolean());
			case PLUS:
				return new Literal(asBoolean() == value.asBoolean());
			}
		}
		switch (type) {
		case DIVIDE:
			return new Literal(toString() + value.toString());
		case MINUS:
			return new Literal(value.toString() + toString());
		case MULTIPLY:
			return new Literal(toString() + value.toString());
		case PLUS:
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
		return literal.equalsIgnoreCase("true") || literal.equalsIgnoreCase("false");
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
		return 0;
	}

	@Override
	public long asLong() {
		try {
			return Long.parseLong(literal);
		} catch (Exception e) {}
		return 0;
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
		try {
			return literal.equalsIgnoreCase("true");
		} catch (Exception e) {}
		return false;
	}

	@Override
	public char asChar() {
		if (literal.length() > 0) {
			return literal.charAt(0);
		}
		return 0;
	}
}
