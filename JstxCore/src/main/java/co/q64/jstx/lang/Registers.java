package co.q64.jstx.lang;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;
import lombok.Getter;
import lombok.Setter;

@AutoFactory
@Getter
@Setter
public class Registers {
	private Map<Value, Value> global = new HashMap<Value, Value>();
	private Value[] longRegister = new Value[256];
	private Value a, b, c, d, i, o;

	protected Registers(@Provided Null nul) {
		this.a = nul;
		this.b = nul;
		this.c = nul;
		this.d = nul;
		this.i = nul;
		this.o = nul;
		Arrays.fill(longRegister, nul);
	}
}
