package co.q64.jstx.opcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Opcodes implements Consumer<Opcode> {
	protected @Inject Opcodes() {}
	protected @Inject Set<OpcodeRegistry> regs;
	
	// 0 - 37 standard opcodes
	// 38 - 9B two character
	// 9C - FF three character

	private List<Opcode> opcodes = new ArrayList<>();

	@Inject
	protected void init() {
		for(OpcodeRegistry reg : regs) {
			reg.init(this);
		}
	}

	@Override
	public void accept(Opcode t) {
		opcodes.add(t);
	}

	public List<Opcode> all() {
		return opcodes;
	}
}
