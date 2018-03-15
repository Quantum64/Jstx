package co.q64.jstx.lang.value;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.inject.types.CompareType;
import co.q64.jstx.inject.types.OperateType;

@Singleton
public class Null implements Value {
	protected @Inject Null() {}

	@Override
	public boolean compare(Value value, CompareType type) {
		return false;
	}

	@Override
	public List<Value> iterate() {
		return Collections.emptyList();
	}

	@Override
	public Value operate(Value value, OperateType type) {
		return this;
	}

	@Override
	public int asInt() {
		return 0;
	}

	@Override
	public String toString() {
		return "null";
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
		return false;
	}

	@Override
	public boolean isBoolean() {
		return false;
	}

	@Override
	public boolean isFloat() {
		return false;
	}

	@Override
	public boolean isInteger() {
		return false;
	}
}
