package co.q64.jstx.lang.opcode;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.BigNumberOpcodes;
import co.q64.jstx.opcode.DictionaryOpcodes;
import co.q64.jstx.opcode.GraphicsOpcodes;
import co.q64.jstx.opcode.ListOpcodes;
import co.q64.jstx.opcode.LongRegisterOpcodes;
import co.q64.jstx.opcode.MathOpcodes;
import co.q64.jstx.opcode.MetaOpcodes;
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
	protected @Inject MetaOpcodes meta;
	protected @Inject GraphicsOpcodes graphics;
	protected @Inject BigNumberOpcodes bigNumber;
	protected @Inject LongRegisterOpcodes registry;
	protected @Inject DictionaryOpcodes dictionary;

	protected List<OpcodeRegistry> get() {
		return Arrays.asList(standard, stack, string, math, list, meta, graphics, bigNumber, registry, dictionary);
	}
}
