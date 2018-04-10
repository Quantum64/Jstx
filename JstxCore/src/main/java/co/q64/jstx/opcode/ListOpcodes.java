package co.q64.jstx.opcode;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;

@Singleton
public class ListOpcodes extends OpcodeRegistry {
	protected @Inject Null nul;
	protected @Inject LiteralFactory literal;
	protected @Inject ValueSorter sorter;

	protected @Inject ListOpcodes() {
		super(OpcodeMarker.LIST);
	}

	@Override
	public void register() {
		r("list.flatten", stack -> stack.push(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.joining())), "Push all elements from the first stack value as a string.");
		r("list.flattenSoft", stack -> stack.push(stack.pop().iterate().stream().map(Object::toString).collect(Collectors.joining(" "))), "Push all elements from the first stack value as a string seperated by a space.");
		r("list.singleton", stack -> stack.push(Arrays.asList(stack.pop())), "Push the first stack value as a list.");
		r("list.pair", stack -> stack.push(Arrays.asList(stack.peek(2), stack.pull(2))), "Push the second and first stack values as a list.");
		r("list.triad", stack -> stack.push(Arrays.asList(stack.peek(3), stack.peek(2), stack.pull(3))), "Push the third, second, and first stack values as a list.");
		r("list.join", stack -> stack.push(stack.peek(2).iterate().stream().map(Object::toString).collect(Collectors.joining(stack.pull(2).toString()))), "Push all elements from the second stack value as a string seperated by the first stack value.");
		r("list.unique", stack -> stack.push(stack.pop().iterate().stream().distinct().collect(Collectors.toList())), "Push the first stack value with duplicate elements removed.");
		r("list.explode", stack -> stack.pop().iterate().forEach(x -> stack.push(x)), "Push every value from the first stack value, interpreted as a list.");
		r("list.size", stack -> stack.push(stack.pop().iterate().size()), "Push the size of the first stack value.");
		r("list.length", stack -> stack.push(stack.pop().iterate().stream().filter(o -> o != nul).count()), "Push the size of the first stack value, excluding null elements.");
		r("list.of", stack -> stack.push(IntStream.range(0, stack.pop().asInt()).mapToObj(i -> stack.pop()).collect(Collectors.toList())), "Push stack values into a list of the size of the first stack value starting with the second stack value.");
		r("list.empty", stack -> stack.push(Collections.emptyList()), "Push an empty list.");
		r("list.reverse", stack -> {
			List<Value> vals = stack.pop().iterate();
			Collections.reverse(vals);
			stack.push(vals.stream().map(Object::toString).collect(Collectors.joining(",")));
		}, "Reverses the list in the first stack value.");
		r("list.range", stack -> stack.push(IntStream.rangeClosed(stack.peek(2).asInt(), stack.pull(2).asInt()).boxed().map(literal::create).collect(Collectors.toList())), "Push a list of integers in the range of the second stack value to the first stack value.");
		r("list.reverseRange", stack -> {
			int to = stack.pop().asInt(), from = stack.pop().asInt();
			stack.push(IntStream.rangeClosed(from, to).map(i -> to - i + from - 1).boxed().map(literal::create).collect(Collectors.toList()));
		}, "Push a list of integers in decending order in the range of the second stack value to the first stack value.");
		r("list.set", stack -> {
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
		r("list.get", stack -> {
			int index = stack.pop().asInt();
			List<Value> list = stack.pop().iterate();
			if (index >= list.size()) {
				stack.push(nul);
				return;
			}
			stack.push(list.get(index));
		}, "Push the value in the list of the second stack value at the index of the first stack value.");
		r("list.add", stack -> {
			List<Value> list = stack.peek(2).iterate();
			list.add(stack.pull(2));
			stack.push(list);
		}, "Add the first stack value to the list in the second stack value. Does not remove the list from the stack.");
		r("list.remove", stack -> {
			List<Value> list = stack.peek(2).iterate();
			list.remove(stack.pull(2));
			stack.push(list);
		}, "Remove the first stack value from the list in the second stack value. Does not remove the list from the stack.");
		r("list.sort", stack -> {
			List<Value> list = stack.pop().iterate();
			Collections.sort(list, sorter);
			stack.push(list);
		}, "Push the first stack value sorted in ascending order.");
		r("list.reverseSort", stack -> {
			List<Value> list = stack.pop().iterate();
			Collections.sort(list, Collections.reverseOrder(sorter));
			stack.push(list);
		}, "Push the first stack value sorted in decending order.");
		r("list.shuffle", stack -> {
			List<Value> list = stack.pop().iterate();
			Collections.shuffle(list);
			stack.push(list);
		}, "Push the list on the first stack value with elements in random order.");
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
