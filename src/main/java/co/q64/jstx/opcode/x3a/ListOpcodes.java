package co.q64.jstx.opcode.x3a;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.OpcodeFactory;
import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.Opcodes;
import lombok.val;

@Singleton
public class ListOpcodes implements OpcodeRegistry {
	protected @Inject ListOpcodes() {}

	protected @Inject OpcodeFactory of;

	@Override
	public void init(Opcodes op) {
		op.reg("list.explode", code(Chars.x00), stack -> stack.pop().iterate().forEach(x -> stack.push(x)));
		op.reg("list.reverse", code(Chars.x01), stack -> {
			val vals = stack.pop().iterate();
			Collections.reverse(vals);
			stack.push(vals.stream().map(Object::toString).collect(Collectors.joining(",")));
		});
		op.reg("list.range", code(Chars.x02), stack -> stack.push(IntStream.rangeClosed(stack.peek(2).asInt(), stack.pull(2).asInt()).boxed().map(Object::toString).collect(Collectors.joining(","))));
	}

	private List<Chars> code(Chars code) {
		return Arrays.asList(Chars.x3a, code);
	}
}
