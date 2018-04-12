package co.q64.jstx.gwt.runtime;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.runtime.Javascript;

@Singleton
public class GWTJavascript implements Javascript {
	protected @Inject LiteralFactory literal;

	protected @Inject GWTJavascript() {}

	@Override
	public Value evalFunction(String function, List<Value> args) {
		StringBuilder payload = new StringBuilder("f=");
		payload.append(function.replace("\\n", "\n"));
		payload.append("; f");
		for (Value v : args) {
			payload.append("(");
			payload.append(v);
			payload.append(")");
		}
		if (args.size() == 0) {
			payload.append("()");
		}
		return literal.create(eval(payload.toString()));
	}

	private native Object eval(String func)
	/*-{
	 return eval(func);
	}-*/;
}
