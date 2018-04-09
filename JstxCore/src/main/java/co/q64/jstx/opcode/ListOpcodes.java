package co.q64.jstx.opcode;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;

@Singleton
public class ListOpcodes implements OpcodeRegistry {
	protected @Inject ListOpcodes() {}

	protected @Inject Null nul;
	protected @Inject LiteralFactory literal;
	protected @Inject ValueSorter sorter;

	@Override
	public void register(Opcodes op) {
		op.reg("list.flatten", stack -> stack.push(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.joining())), "Push all elements from the first stack value as a string.");
		op.reg("list.flattenSoft", stack -> stack.push(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.joining(" "))), "Push all elements from the first stack value as a string seperated by a space.");
		op.reg("list.singleton", stack -> stack.push(Arrays.asList(stack.pop())), "Push the first stack value as a list.");
		op.reg("list.pair", stack -> stack.push(Arrays.asList(stack.peek(2), stack.pull(2))), "Push the second and first stack values as a list.");
		op.reg("list.triad", stack -> stack.push(Arrays.asList(stack.peek(3), stack.peek(2), stack.pull(3))), "Push the third, second, and first stack values as a list.");
		op.reg("list.join", stack -> stack.push(stack.peek(2).iterate().stream().map(Object::toString).collect(Collectors.joining(stack.pull(2).toString()))), "Push all elements from the second stack value as a string seperated by the first stack value.");
		op.reg("list.unique", stack -> stack.push(stack.pop().iterate().stream().distinct().collect(Collectors.toList())), "Push the first stack value with duplicate elements removed.");
		op.reg("list.explode", stack -> stack.pop().iterate().forEach(x -> stack.push(x)), "Push every value from the first stack value, interpreted as a list.");
		op.reg("list.size", stack -> stack.push(stack.pop().iterate().size()), "Push the size of the first stack value.");
		op.reg("list.length", stack -> stack.push(stack.pop().iterate().stream().filter(o -> o != nul).count()), "Push the size of the first stack value, excluding null elements.");
		op.reg("list.of", stack -> stack.push(IntStream.range(0, stack.pop().asInt()).mapToObj(i -> stack.pop()).collect(Collectors.toList())), "Push stack values into a list of the size of the first stack value starting with the second stack value.");
		op.reg("list.reverse", stack -> {
			List<Value> vals = stack.pop().iterate();
			Collections.reverse(vals);
			stack.push(vals.stream().map(Object::toString).collect(Collectors.joining(",")));
		}, "Reverses the list in the first stack value.");
		op.reg("list.range", stack -> stack.push(IntStream.rangeClosed(stack.peek(2).asInt(), stack.pull(2).asInt()).boxed().map(literal::create).collect(Collectors.toList())), "Push a list of integers in the range of the second stack value to the first stack value.");
		op.reg("list.reverseRange", stack -> {
			int to = stack.pop().asInt(), from = stack.pop().asInt();
			stack.push(IntStream.rangeClosed(from, to).map(i -> to - i + from - 1).boxed().map(literal::create).collect(Collectors.toList()));
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
		op.reg("list.sort", stack -> {
			List<Value> list = stack.pop().iterate();
			Collections.sort(list, sorter);
			stack.push(list);
		}, "Push the first stack value sorted in ascending order.");
		op.reg("list.reverseSort", stack -> {
			List<Value> list = stack.pop().iterate();
			Collections.sort(list, Collections.reverseOrder(sorter));
			stack.push(list);
		}, "Push the first stack value sorted in decending order.");
	}

	@Singleton
	protected static class ValueSorter implements Comparator<Value> {
		protected @Inject ValueSorter() {}

		@Override
		public int compare(Value o1, Value o2) {
			if (o1.isInteger() && o2.isInteger()) {
				return Long.compare(o1.asLong(), o2.asLong());
			}
			if (o1.isFloat() && o2.isFloat()) {
				return Double.compare(o1.asDouble(), o2.asDouble());
			}
			if (o1.isBoolean() && o2.isBoolean()) {
				return Boolean.compare(o1.asBoolean(), o2.asBoolean());
			}
			return o1.toString().compareTo(o2.toString());
		}
	}
}
