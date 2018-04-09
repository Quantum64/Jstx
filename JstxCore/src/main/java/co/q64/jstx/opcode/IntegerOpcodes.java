package co.q64.jstx.opcode;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;

@Singleton
public class IntegerOpcodes implements OpcodeRegistry {
	protected @Inject IntegerOpcodes() {}

	@Override
	public void register(Opcodes oc) {
		oc.reg("integer.bitCount", stack -> stack.push(Long.bitCount(stack.pop().asLong())), "Push the bit count of the first stack value.");
		oc.reg("integer.compare", stack -> stack.push(Long.compare(stack.peek(2).asLong(), stack.pull(2).asLong())), "Push the comparision integer of the second and first stack values.");

		oc.reg("integer.toBinary", stack -> stack.push(Long.toString(stack.pop().asLong(), 2)), "Push the first stack value in binary.");
		oc.reg("integer.toOctal", stack -> stack.push(Long.toString(stack.pop().asLong(), 8)), "Push the first stack value in octal.");
		oc.reg("integer.toHex", stack -> stack.push(Long.toString(stack.pop().asLong(), 16)), "Push the first stack value in hexadecimal.");
		oc.reg("integer.toBase", stack -> stack.push(Long.toString(stack.peek(2).asLong(), stack.pull(2).asInt())), "Push the second stack value in base first stack value.");
		oc.reg("integer.fromBinary", stack -> stack.push(Long.valueOf(stack.pop().toString(), 2)), "Push the decimal representation of the binary first stack value.");
		oc.reg("integer.fromOctal", stack -> stack.push(Long.valueOf(stack.pop().toString(), 8)), "Push the decimal representation of the octal first stack value.");
		oc.reg("integer.fromHex", stack -> stack.push(Long.valueOf(stack.pop().toString(), 16)), "Push the decimal representation of the hexadecimal first stack value.");
		oc.reg("integer.fromBase", stack -> stack.push(Long.valueOf(stack.peek(2).toString(), stack.pull(2).asInt())), "Push the second stack value in base first stack value converted to decimal.");
	}
}
