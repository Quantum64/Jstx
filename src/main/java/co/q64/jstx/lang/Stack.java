package co.q64.jstx.lang;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.LiteralFactoryFactory;
import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;
import lombok.Getter;

@AutoFactory
public class Stack {
	private Null nul;
	private @Getter Program program;
	private List<Value> stack = new ArrayList<>();
	private LiteralFactory literal;

	protected @Inject Stack(@Provided Null nul, @Provided LiteralFactoryFactory literal, Program program) {
		this.nul = nul;
		this.program = program;
		this.literal = literal.getFactory();
	}

	public void dup(int depth) {
		for (int i = 0; i < depth; i++) {
			if (stack.size() > 0) {
				stack.add(stack.get(stack.size() - 1));
			}
		}
	}

	public void dup() {
		dup(1);
	}

	public Value pop(int depth) {
		Value result = nul;
		for (int i = 0; i < 1; i++) {
			if (stack.size() > 0) {
				result = stack.remove(stack.size() - 1);
			}
		}
		return result;
	}

	public Value pop() {
		return pop(1);
	}

	public void clr() {
		stack.clear();
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

	public void push(Object value) {
		stack.add(literal.create(value.toString()));
	}
}
