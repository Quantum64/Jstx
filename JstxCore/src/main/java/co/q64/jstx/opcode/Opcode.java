package co.q64.jstx.opcode;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.google.auto.factory.AutoFactory;

import co.q64.jstx.lang.Stack;
import lombok.Getter;

@AutoFactory
@Getter
public class Opcode {
	String name;
	List<Chars> chars;
	Consumer<Stack> executor;

	protected Opcode(String name, List<Chars> chars, Consumer<Stack> executor) {
		this.name = name;
		this.chars = chars;
		this.executor = executor;
	}

	protected Opcode(String name, Chars chars, Consumer<Stack> executor) {
		this(name, Collections.singletonList(chars), executor);
	}
}
