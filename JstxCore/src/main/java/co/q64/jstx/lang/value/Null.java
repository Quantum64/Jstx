package co.q64.jstx.lang.value;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.types.Comparison;
import co.q64.jstx.types.Operation;

@Singleton
public class Null implements Value {
	protected @Inject Null() {}

	@Override
	public boolean compare(Value value, Comparison type) {
		return false;
	}

	@Override
	public List<Value> iterate() {
		return new ArrayList<>();
	}

	@Override
	public Value operate(Value value, Operation type) {
		return value;
	}

	@Override
	public int asInt() {
		return 0;
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public long asLong() {
		return 0;
	}

	@Override
	public double asDouble() {
		return 0;
	}

	@Override
	public boolean asBoolean() {
		return true;
	}

	@Override
	public boolean isBoolean() {
		return true;
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
	public char asChar() {
		return 0;
	}
}
