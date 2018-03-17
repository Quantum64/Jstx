package co.q64.jstx.opcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.Stack;

@Singleton
public class Opcodes {
	protected @Inject Opcodes() {}

	protected @Inject OpcodeFactory of;
	protected @Inject Set<OpcodeRegistry> regs;

	// 0 - 37 standard opcodes
	// 38 - 9B two character
	// 9C - FF three character

	private List<Opcode> opcodes = new ArrayList<>();

	@Inject
	protected void init() {
		for (OpcodeRegistry reg : regs) {
			reg.init(this);
		}
	}

	public void reg(String name, List<Chars> chars, Consumer<Stack> exec) {
		opcodes.add(of.create(name, chars, exec));
	}

	public void reg(String name, Chars chars, Consumer<Stack> exec) {
		opcodes.add(of.create(name, chars, exec));
	}

	public List<Opcode> all() {
		return opcodes;
	}
}
