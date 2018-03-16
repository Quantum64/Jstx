package co.q64.jstx.opcode.x38;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class MathOpcodes implements OpcodeRegistry {
	protected @Inject MathOpcodes() {}

	@Override
	public void init(Opcodes oc) {

	}
}
