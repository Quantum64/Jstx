package co.q64.jstx.lang;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;

@Singleton
public class Stack {
	protected @Inject Stack() {}

	protected @Inject Null nul;

	private List<Value> stack = new ArrayList<>();

	public void dup() {
		if (stack.size() > 0) {
			stack.add(stack.get(stack.size() - 1));
		}
	}

	public Value pop() {
		if (stack.size() > 0) {
			return stack.remove(stack.size() - 1);
		}
		return nul;
	}

	public Value peek(int depth) {
		if (stack.size() > depth) {
			return stack.remove(stack.size() - depth - 1);
		}
		return nul;
	}

	public Value peek() {
		return peek(0);
	}

	public void swap() {
		if (stack.size() > 1) {
			Value buffer = stack.get(stack.size() - 1);
			stack.set(stack.size() - 1, stack.get(stack.size() - 2));
			stack.set(stack.size() - 2, buffer);
		}
	}

	public void push(Value value) {
		stack.add(value);
	}
}
