package co.q64.jstx.opcode.x39;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeFactory;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class StringOpcodes implements OpcodeRegistry {
	protected @Inject StringOpcodes() {}

	protected @Inject OpcodeFactory of;

	@Override
	public void init(Opcodes op) {

		op.reg("string.charAt", code(Chars.x01), stack -> stack.push(stack.peek(2).toString().charAt(stack.pull(2).asInt())));
		op.reg("string.compareTo", code(Chars.x02), stack -> stack.push(stack.peek(2).toString().compareTo(stack.pull(2).toString())));
		op.reg("string.contains", code(Chars.x03), stack -> stack.push(stack.peek(2).toString().contains(stack.pull(2).toString())));
		op.reg("string.concat", code(Chars.x04), stack -> stack.push(stack.peek(2).toString().concat(stack.pull(2).toString())));
		op.reg("string.endsWith", code(Chars.x05), stack -> stack.push(stack.peek(2).toString().endsWith(stack.pull(2).toString())));
		op.reg("string.equals", code(Chars.x06), stack -> stack.push(stack.peek(2).toString().equals(stack.pull(2).toString())));
		op.reg("string.equalsIgnoreCase", code(Chars.x07), stack -> stack.push(stack.peek(2).toString().equalsIgnoreCase(stack.pull(2).toString())));
		op.reg("string.format", code(Chars.x08), stack -> stack.push(String.format(stack.peek(2).toString(), stack.pull(2).iterate().toArray())));
		op.reg("string.getBytes", code(Chars.x09), stack -> stack.push(IntStream.range(0, stack.peek().toString().getBytes().length).map(i -> stack.pop().toString().getBytes()[i]).boxed().map(Object::toString).collect(Collectors.joining(","))));
		op.reg("string.hashCode", code(Chars.x0a), stack -> stack.push(stack.pop().toString().hashCode()));
		op.reg("string.indexOf", code(Chars.x0b), stack -> stack.push(stack.peek(2).toString().indexOf(stack.pull(2).asChar())));
		op.reg("string.intern", code(Chars.x0c), stack -> stack.push(stack.pop().toString().intern()));
		op.reg("string.isEmpty", code(Chars.x0d), stack -> stack.push(stack.pop().toString().isEmpty()));
		op.reg("string.lastIndexOf", code(Chars.x0e), stack -> stack.push(stack.peek(2).toString().lastIndexOf(stack.pull(2).asChar())));
		op.reg("string.length", code(Chars.x0f), stack -> stack.push(stack.pop().toString().length()));
		op.reg("string.matches", code(Chars.x10), stack -> stack.push(stack.peek(2).toString().matches(stack.pull(2).toString())));
		op.reg("string.offsetByCodePoints", code(Chars.x11), stack -> stack.push(stack.peek(3).toString().offsetByCodePoints(stack.peek(2).asInt(), stack.pull(3).asInt())));
		op.reg("string.replace", code(Chars.x12), stack -> stack.push(stack.peek(3).toString().replace(stack.peek(2).toString(), stack.pull(3).toString())));

		op.reg("string.substr", code(Chars.x39), stack -> stack.push(stack.peek(2).toString().substring(stack.pull(2).asInt())));
		op.reg("string.toLowerCase", code(Chars.x50), stack -> stack.push(stack.pop().toString().toLowerCase()));
		op.reg("string.toUpperCase", code(Chars.x51), stack -> stack.push(stack.pop().toString().toUpperCase()));

		op.reg("string.reverse", code(Chars.xf0), stack -> stack.push(new StringBuilder(stack.pop().toString()).reverse()));
		op.reg("string.reverseconcat", code(Chars.xf1), stack -> stack.push(stack.pop().toString().concat(stack.pop().toString())));
	}

	private List<Chars> code(Chars code) {
		return Arrays.asList(Chars.x39, code);
	}
}
