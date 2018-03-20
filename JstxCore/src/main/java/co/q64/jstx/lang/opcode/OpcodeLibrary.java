package co.q64.jstx.lang.opcode;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.ListOpcodes;
import co.q64.jstx.opcode.LongRegisterOpcodes;
import co.q64.jstx.opcode.MathOpcodes;
import co.q64.jstx.opcode.StackOpcodes;
import co.q64.jstx.opcode.StandardOpcodes;
import co.q64.jstx.opcode.StringOpcodes;

@Singleton
public class OpcodeLibrary {
	protected @Inject OpcodeLibrary() {}

	protected @Inject StandardOpcodes standard;
	protected @Inject StackOpcodes stack;
	protected @Inject StringOpcodes string;
	protected @Inject ListOpcodes list;
	protected @Inject MathOpcodes math;
	protected @Inject LongRegisterOpcodes registry;

	protected List<OpcodeRegistry> get() {
		return Arrays.asList(standard, stack, string, math, list, registry);
	}
}
