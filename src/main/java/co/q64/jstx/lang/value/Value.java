package co.q64.jstx.lang.value;

import java.util.List;

import co.q64.jstx.inject.types.CompareType;
import co.q64.jstx.inject.types.OperateType;

public interface Value {
	public boolean compare(Value value, CompareType type);

	public Value operate(Value value, OperateType type);

	public List<Value> iterate();

	public int asInt();

	public long asLong();

	public double asDouble();

	public boolean asBoolean();

	public boolean isBoolean();

	public boolean isFloat();

	public boolean isInteger();
}
