package co.q64.jstx.opcode.x3a;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
		op.accept(of.create("list.explode", code(Chars.x01), stack -> stack.pop().iterate().forEach(x -> stack.push(x))));
		// @formatter:off
		op.accept(of.create("list.reverse", code(Chars.x02), stack -> { val vals = stack.pop().iterate(); Collections.reverse(vals); stack.push(vals.stream().map(Object::toString).collect(Collectors.joining(","))); }));
	}

	private List<Chars> code(Chars code) {
		return Arrays.asList(Chars.x3a, code);
	}
}
