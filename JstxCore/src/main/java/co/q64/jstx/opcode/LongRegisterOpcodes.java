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
			String register = "0x" + (i < 16 ? "0" : "") + Integer.toHexString(i);
			oc.reg("ldr " + register, stack -> stack.push(stack.getProgram().getRegisters().getLongRegister()[lock]));
			oc.reg("sdr " + register, stack -> stack.getProgram().getRegisters().getLongRegister()[lock] = stack.pop());
		}
	}
}
