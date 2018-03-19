package co.q64.jstx.opcode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;
import lombok.val;

@Singleton
public class ListOpcodes implements OpcodeRegistry {
	protected @Inject ListOpcodes() {}

	protected @Inject Null nul;

	@Override
	public void register(Opcodes op) {
		op.reg("list.explode",  stack -> stack.pop().iterate().forEach(x -> stack.push(x)));
		op.reg("list.reverse",  stack -> {
			val vals = stack.pop().iterate();
			Collections.reverse(vals);
			stack.push(vals.stream().map(Object::toString).collect(Collectors.joining(",")));
		});
		op.reg("list.range",  stack -> stack.push(IntStream.rangeClosed(stack.peek(2).asInt(), stack.pull(2).asInt()).boxed().map(Object::toString).collect(Collectors.joining(","))));
		op.reg("list.reverseRange",  stack -> {
			int to = stack.pop().asInt();
			int from = stack.pop().asInt();
			stack.push(IntStream.rangeClosed(from, to).map(i -> to - i + from - 1).boxed().map(Object::toString).collect(Collectors.joining(",")));
		});
		op.reg("list.set",  stack -> {
			int index = stack.pop().asInt();
			Value target = stack.pop();
			List<Value> list = stack.pop().iterate();
			if (list.size() > index) {
				list.set(index, target);
			} else {
				int size = list.size();
				for (int i = size; i < index; i++) {
					list.add(nul);
				}
				list.add(target);
			}
			stack.push(list);
		});
		op.reg("list.get",  stack -> {
			int index = stack.pop().asInt();
			List<Value> list = stack.pop().iterate();
			if (index >= list.size()) {
				stack.push(nul);
				return;
			}
			stack.push(list.get(index));
		});
	}
}
