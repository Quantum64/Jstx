package co.q64.jstx.lang;

import java.util.function.Consumer;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.Value;
import lombok.Getter;

@AutoFactory
public class Instruction {
	private Opcodes opcodes;
	private @Getter Integer opcode;
	private Consumer<Stack> executor;
	private Value value;

	protected Instruction(@Provided Opcodes opcodes, int opcode) {
		this.opcodes = opcodes;
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

	public String getInstruction() {
		if (value != null) {
			return "load " + value.toString();
		}
		if (executor != null) {
			return opcodes.getName(opcode).orElse("undefined");
		}
		return "nop";
	}

	@Override
	public String toString() {
		return getInstruction();
	}
}
