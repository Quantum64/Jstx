package co.q64.jstx.lang.value;

import com.google.auto.factory.AutoFactory;

@AutoFactory
public class Literal implements Value {
	private String literal;

	protected Literal(String literal) {
		this.literal = literal;
	}
}
