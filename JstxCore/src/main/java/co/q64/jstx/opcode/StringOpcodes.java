package co.q64.jstx.opcode;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;

@Singleton
public class StringOpcodes implements OpcodeRegistry {
	protected @Inject StringOpcodes() {}


	@Override
	public void register(Opcodes op) {

		op.reg("string.charAt",  stack -> stack.push(stack.peek(2).toString().charAt(stack.pull(2).asInt())));
		op.reg("string.compareTo",  stack -> stack.push(stack.peek(2).toString().compareTo(stack.pull(2).toString())));
		op.reg("string.contains",  stack -> stack.push(stack.peek(2).toString().contains(stack.pull(2).toString())));
		op.reg("string.concat",  stack -> stack.push(stack.peek(2).toString().concat(stack.pull(2).toString())));
		op.reg("string.endsWith",  stack -> stack.push(stack.peek(2).toString().endsWith(stack.pull(2).toString())));
		op.reg("string.equals",  stack -> stack.push(stack.peek(2).toString().equals(stack.pull(2).toString())));
		op.reg("string.equalsIgnoreCase",  stack -> stack.push(stack.peek(2).toString().equalsIgnoreCase(stack.pull(2).toString())));
		//op.reg("string.format",  stack -> stack.push(String.format(stack.peek(2).toString(), stack.pull(2).iterate().toArray()))); // GWT does not like this
		op.reg("string.getBytes",  stack -> stack.push(IntStream.range(0, stack.peek().toString().getBytes().length).map(i -> stack.pop().toString().getBytes()[i]).boxed().map(Object::toString).collect(Collectors.joining(","))));
		op.reg("string.hashCode",  stack -> stack.push(stack.pop().toString().hashCode()));
		op.reg("string.indexOf",  stack -> stack.push(stack.peek(2).toString().indexOf(stack.pull(2).asChar())));
		op.reg("string.intern",  stack -> stack.push(stack.pop().toString().intern()));
		op.reg("string.isEmpty",  stack -> stack.push(stack.pop().toString().isEmpty()));
		op.reg("string.lastIndexOf",  stack -> stack.push(stack.peek(2).toString().lastIndexOf(stack.pull(2).asChar())));
		op.reg("string.length",  stack -> stack.push(stack.pop().toString().length()));
		op.reg("string.matches",  stack -> stack.push(stack.peek(2).toString().matches(stack.pull(2).toString())));
		op.reg("string.offsetByCodePoints",  stack -> stack.push(stack.peek(3).toString().offsetByCodePoints(stack.peek(2).asInt(), stack.pull(3).asInt())));
		op.reg("string.replace",  stack -> stack.push(stack.peek(3).toString().replace(stack.peek(2).toString(), stack.pull(3).toString())));

		op.reg("string.substr",  stack -> stack.push(stack.peek(2).toString().substring(stack.pull(2).asInt())));
		op.reg("string.toLowerCase",  stack -> stack.push(stack.pop().toString().toLowerCase()));
		op.reg("string.toUpperCase",  stack -> stack.push(stack.pop().toString().toUpperCase()));

		op.reg("string.reverse",  stack -> stack.push(new StringBuilder(stack.pop().toString()).reverse()));
		op.reg("string.reverseconcat",  stack -> stack.push(stack.pop().toString().concat(stack.pop().toString())));
		op.reg("string.charValue",  stack -> stack.push(((int) stack.pop().asChar())));
	}
}
