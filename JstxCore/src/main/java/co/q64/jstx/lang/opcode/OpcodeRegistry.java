package co.q64.jstx.lang.opcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;

import co.q64.jstx.lang.Stack;
import lombok.Getter;

public abstract class OpcodeRegistry {
	protected @Inject OpcodeFactory opcodeFactory;

	private OpcodeMarker priority;
	private @Getter Opcodes opcodes;

	protected OpcodeRegistry(OpcodeMarker marker) {
		this.priority = marker;
	}

	public abstract void register();

	public void registerExternal(Opcodes opcodes) {
		this.opcodes = opcodes;
		register();
	}

	public void r(String name, Consumer<Stack> exec) {
		opcodes.getOpcodes().add(opcodeFactory.create(name, exec, new HashSet<>(Arrays.asList(priority))));
	}

	public void r(String name, OpcodeMarker marker, Consumer<Stack> exec) {
		opcodes.getOpcodes().add(opcodeFactory.create(name, exec, new HashSet<>(Arrays.asList(marker, priority))));
	}

	public void r(String name, Set<OpcodeMarker> markers, Consumer<Stack> exec) {
		markers.add(priority);
		opcodes.getOpcodes().add(opcodeFactory.create(name, exec, markers));
	}

	public void r(String name, Consumer<Stack> exec, String description) {
		opcodes.getOpcodes().add(opcodeFactory.create(name, exec, description, new HashSet<>(Arrays.asList(priority))));
	}

	public void r(String name, OpcodeMarker marker, Consumer<Stack> exec, String description) {
		opcodes.getOpcodes().add(opcodeFactory.create(name, exec, description, new HashSet<>(Arrays.asList(marker, priority))));
	}

	public void r(String name, Set<OpcodeMarker> markers, Consumer<Stack> exec, String description) {
		markers.add(priority);
		opcodes.getOpcodes().add(opcodeFactory.create(name, exec, description, markers));
	}
}
