package co.q64.jstx.lang;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.factory.LiteralFactoryFactory;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;
import lombok.Getter;

@AutoFactory
public class Stack {
	private Null nul;
	private @Getter Program program;
	private @Getter List<Value> stack = new ArrayList<>();
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
		for (int i = 0; i < depth; i++) {
			if (stack.size() > 0) {
				result = stack.remove(stack.size() - 1);
			}
		}
		return result;
	}

	public Value pull(int depth) {
		Value result = nul;
		for (int i = 0; i < depth; i++) {
			if (stack.size() > 0) {
				Value buffer = stack.remove(stack.size() - 1);
				if (result == nul) {
					result = buffer;
				}
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
		if (stack.size() >= depth) {
			return stack.get(stack.size() - depth);
		}
		return nul;
	}

	public Value peek() {
		return peek(1);
	}

	public Stack swap() {
		if (stack.size() > 1) {
			Value buffer = stack.get(stack.size() - 1);
			stack.set(stack.size() - 1, stack.get(stack.size() - 2));
			stack.set(stack.size() - 2, buffer);
		}
		return this;
	}

	public Stack push(Value value) {
		stack.add(value);
		return this;
	}

	public Stack push(Object value) {
		stack.add(literal.create(value.toString()));
		return this;
	}

	public Stack push(List<Object> values) {
		stack.add(literal.create(values));
		return this;
	}

	public int size() {
		return stack.size();
	}
}
