package co.q64.jstx.lang;

import com.google.auto.factory.AutoFactory;

import co.q64.jstx.lang.value.Value;
import co.q64.jstx.opcode.Opcode;
import lombok.Getter;

@AutoFactory
public class Instruction {
	private @Getter Opcode opcode;
	private Value value;

	protected Instruction(Opcode opcode) {
		this.opcode = opcode;
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
		if (opcode != null) {
			opcode.getExecutor()
			.accept(stack);
		}
		// no-op
	}
}
