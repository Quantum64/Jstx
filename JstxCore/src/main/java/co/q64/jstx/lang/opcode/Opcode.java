package co.q64.jstx.lang.opcode;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.google.auto.factory.AutoFactory;

import co.q64.jstx.lang.Stack;
import lombok.Getter;

@Getter
@AutoFactory
public class Opcode {
	private  String name;
	private  Consumer<Stack> executor;
	private  Set<OpcodeMarker> markers;
	private Optional<String> description;

	protected Opcode(String name, Consumer<Stack> executor, Set<OpcodeMarker> markers) {
		this.name = name;
		this.executor = executor;
		this.description = Optional.empty();
		this.markers = markers;
	}

	protected Opcode(String name, Consumer<Stack> executor, String description, Set<OpcodeMarker> markers) {
		this(name, executor, markers);
		this.description = Optional.of(description);
	}
}
