package co.q64.jstx.opcode.x38;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class MathOpcodes implements OpcodeRegistry {
	protected @Inject MathOpcodes() {}

	@Override
	public void init(Opcodes oc) {

	}

	private List<Chars> code(Chars code) {
		return Arrays.asList(Chars.x3b, code);
	}
}
