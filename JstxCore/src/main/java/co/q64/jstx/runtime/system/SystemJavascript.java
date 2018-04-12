package co.q64.jstx.runtime.system;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.common.annotations.GwtIncompatible;

import co.q64.jstx.annotation.GWT;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.runtime.Javascript;

@Singleton
@GwtIncompatible(GWT.MESSAGE)
public class SystemJavascript implements Javascript {
	protected @Inject LiteralFactory literal;
	private ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

	protected @Inject SystemJavascript() {}

	@Override
	public Value evalFunction(String function, List<Value> args) {
		StringBuilder payload = new StringBuilder("f=");
		payload.append(function);
		payload.append("; f");
		for (Value v : args) {
			payload.append("(");
			payload.append(v);
			payload.append("");
		}
		try {
			return literal.create(engine.eval(payload.toString()));
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		}
	}
}
