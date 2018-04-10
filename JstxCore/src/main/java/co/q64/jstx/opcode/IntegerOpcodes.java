package co.q64.jstx.opcode;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.OpcodeRegistry;

@Singleton
public class IntegerOpcodes extends OpcodeRegistry {
	protected @Inject IntegerOpcodes() {
		super(OpcodeMarker.INTEGER);
	}

	@Override
	public void register() {
		r("integer.bitCount", stack -> stack.push(Long.bitCount(stack.pop().asLong())), "Push the bit count of the first stack value.");
		r("integer.compare", stack -> stack.push(Long.compare(stack.peek(2).asLong(), stack.pull(2).asLong())), "Push the comparision integer of the second and first stack values.");

		r("integer.toBinary", stack -> stack.push(Long.toString(stack.pop().asLong(), 2)), "Push the first stack value in binary.");
		r("integer.toOctal", stack -> stack.push(Long.toString(stack.pop().asLong(), 8)), "Push the first stack value in octal.");
		r("integer.toHex", stack -> stack.push(Long.toString(stack.pop().asLong(), 16)), "Push the first stack value in hexadecimal.");
		r("integer.toBase", stack -> stack.push(Long.toString(stack.peek(2).asLong(), stack.pull(2).asInt())), "Push the second stack value in base first stack value.");
		r("integer.fromBinary", stack -> stack.push(Long.valueOf(stack.pop().toString(), 2)), "Push the decimal representation of the binary first stack value.");
		r("integer.fromOctal", stack -> stack.push(Long.valueOf(stack.pop().toString(), 8)), "Push the decimal representation of the octal first stack value.");
		r("integer.fromHex", stack -> stack.push(Long.valueOf(stack.pop().toString(), 16)), "Push the decimal representation of the hexadecimal first stack value.");
		r("integer.fromBase", stack -> stack.push(Long.valueOf(stack.peek(2).toString(), stack.pull(2).asInt())), "Push the second stack value in base first stack value converted to decimal.");
	}
}
