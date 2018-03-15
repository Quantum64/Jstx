package co.q64.jstx.lang;

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
	private Value a, b, c, d, i, o;

	protected Registers(@Provided Null nul) {
		this.a = nul;
		this.b = nul;
		this.c = nul;
		this.d = nul;
		this.i = nul;
		this.o = nul;
	}
}
