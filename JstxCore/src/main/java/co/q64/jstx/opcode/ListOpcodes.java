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
		op.reg("list.flatten", stack -> stack.push(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.joining())), "Push all elements from the first stack value as a string.");
		op.reg("list.flattenSoft", stack -> stack.push(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.joining(" "))), "Push all elements from the first stack value as a string seperated by a space.");
		op.reg("list.join", stack -> stack.push(stack.peek(2).iterate().stream().map(Object::toString).collect(Collectors.joining(stack.pull(2).toString()))), "Push all elements from the second stack value as a string seperated by the first stack value.");
		op.reg("list.unique", stack -> stack.push(stack.pop().iterate().stream().distinct().collect(Collectors.toList())), "Push the first stack value with duplicate elements removed.");
		op.reg("list.explode", stack -> stack.pop().iterate().forEach(x -> stack.push(x)), "Push every value from the first stack value, interpreted as a list.");
		op.reg("list.size", stack -> stack.push(stack.pop().iterate().size()), "Push the size of the first stack value.");
		op.reg("list.length", stack -> stack.push(stack.pop().iterate().stream().filter(o -> o != nul).count()), "Push the size of the first stack value, excluding null elements.");
		op.reg("list.reverse", stack -> {
			val vals = stack.pop().iterate();
			Collections.reverse(vals);
			stack.push(vals.stream().map(Object::toString).collect(Collectors.joining(",")));
		}, "Reverses the list in the first stack value.");
		op.reg("list.range", stack -> stack.push(IntStream.rangeClosed(stack.peek(2).asInt(), stack.pull(2).asInt()).boxed().map(Object::toString).collect(Collectors.toList())), "Push a list of integers in the range of the second stack value to the first stack value.");
		op.reg("list.reverseRange", stack -> {
			int to = stack.pop().asInt(), from = stack.pop().asInt();
			stack.push(IntStream.rangeClosed(from, to).map(i -> to - i + from - 1).boxed().map(Object::toString).collect(Collectors.toList()));
		}, "Push a list of integers in decending order in the range of the second stack value to the first stack value.");
		op.reg("list.set", stack -> {
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
		}, "Set the value in the list in the third stack value to the second stack value at the index of the first stack value.");
		op.reg("list.get", stack -> {
			int index = stack.pop().asInt();
			List<Value> list = stack.pop().iterate();
			if (index >= list.size()) {
				stack.push(nul);
				return;
			}
			stack.push(list.get(index));
		}, "Push the value in the list of the second stack value at the index of the first stack value.");
		op.reg("list.add", stack -> {
			List<Value> list = stack.peek(2).iterate();
			list.add(stack.pull(2));
			stack.push(list);
		}, "Add the first stack value to the list in the second stack value. Does not remove the list from the stack.");
		op.reg("list.remove", stack -> {
			List<Value> list = stack.peek(2).iterate();
			list.remove(stack.pull(2));
			stack.push(list);
		}, "Remove the first stack value from the list in the second stack value. Does not remove the list from the stack.");
	}
}
