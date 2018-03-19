package co.q64.jstx.lang;

import java.util.function.Consumer;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.Value;
import lombok.Getter;

@AutoFactory
public class Instruction {
	private @Getter Integer opcode;
	private Consumer<Stack> executor;
	private Value value;

	protected Instruction(@Provided Opcodes opcodes, int opcode) {
		this.opcode = opcode;
		this.executor = opcodes.getExecutor(opcode);
	}

	protected Instruction(Value value) {
		this.value = value;
	}

	protected Instruction() {}

	public void execute(Stack stack) {
		if (value != null) {
			stack.push(value);
			return;
		}
		if (executor != null) {
			executor.accept(stack);
		}
		// no-op
	}
}
