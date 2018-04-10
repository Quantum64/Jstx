package co.q64.jstx.opcode;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.OpcodeRegistry;

@Singleton
public class LongRegisterOpcodes extends OpcodeRegistry {
	protected @Inject LongRegisterOpcodes() {
		super(OpcodeMarker.LONG_REGISTER);
	}

	@Override
	public void register() {
		for (int i = 0; i < 256; i++) {
			final int lock = i;
			String register = "0x" + (i < 16 ? "0" : "") + Integer.toHexString(i);
			r("ldr " + register, stack -> stack.push(stack.getProgram().getRegisters().getLongRegister()[lock]));
			r("sdr " + register, stack -> stack.getProgram().getRegisters().getLongRegister()[lock] = stack.pop());
		}
	}
}
