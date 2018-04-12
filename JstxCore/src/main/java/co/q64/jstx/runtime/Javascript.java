package co.q64.jstx.runtime;

import java.util.List;

import co.q64.jstx.lang.value.Literal;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Value;

public interface Javascript {
	public Value evalFunction(String function, List<Value> args);

	public default Literal literal(Value value) {
		return new LiteralFactory().create("\"" + value.toString() + "\""); // TODO fix this nonsense lol
	}
}
