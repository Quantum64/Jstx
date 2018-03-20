package co.q64.jstx.opcode;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;

@Singleton
public class LongRegisterOpcodes implements OpcodeRegistry {
	protected @Inject LongRegisterOpcodes() {}

	@Override
	public void register(Opcodes oc) {
		for (int i = 0; i < 256; i++) {
			final int lock = i;
			oc.reg("ldr 0x" + Integer.toHexString(i), stack -> stack.push(stack.getProgram().getRegisters().getLongRegister()[lock]));
			oc.reg("sdr 0x" + Integer.toHexString(i), stack -> stack.getProgram().getRegisters().getLongRegister()[lock] = stack.pop());
		}
	}
}
