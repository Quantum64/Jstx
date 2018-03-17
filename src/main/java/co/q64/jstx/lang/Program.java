package co.q64.jstx.lang;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.factory.IteratorFactoryFactory;
import co.q64.jstx.factory.LiteralFactoryFactory;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.opcode.Chars;
import lombok.Getter;

@AutoFactory
public class Program {
	private StackFactory stackFactory;
	private RegistersFactory registersFactory;
	private LiteralFactory literal;
	private IteratorFactory iteratorFactory;

	private @Getter List<Instruction> instructions;
	private @Getter Stack stack;
	private @Getter Registers registers;

	private @Getter int instruction;
	private boolean printOnTerminate;
	private boolean terminated;
	private Deque<Iterator> iterators = new ArrayDeque<>();
	private @Getter boolean lastConditional = false; // TODO replace with stack?
	private String[] args;

	protected Program(@Provided StackFactory stackFactory, @Provided RegistersFactory registersFactory, @Provided LiteralFactoryFactory literal, @Provided IteratorFactoryFactory iteratorFactory, List<Instruction> instructions, String[] args) {
		this.stackFactory = stackFactory;
		this.registersFactory = registersFactory;
		this.iteratorFactory = iteratorFactory.getFactory();
		this.literal = literal.getFactory();
		this.instructions = instructions;
		this.args = args;
		instructions.add(0, new Instruction());
	}

	public void execute() {
		this.stack = stackFactory.create(this);
		this.registers = registersFactory.create();
		this.printOnTerminate = true;
		this.terminated = false;
		this.instruction = 0;
		this.iterators.clear();
		for (String s : args) {
			stack.push(s);
		}
		while (true) {
			if (terminated) {
				break;
			}
			if (instructions.size() <= instruction) {
				break;
			}
			Instruction current = instructions.get(instruction);
			instruction++;
			current.execute(stack);
		}
		if (printOnTerminate) {
			System.out.println(stack.pop());
		}
	}

	public void iterate(boolean onStack) {
		Iterator itr = iteratorFactory.create(this, instruction, onStack);
		if (itr.next()) {
			// Special case - if the iterator has nothing to iterate
			// over we will just jump to the end of the iterator
			jumpToEnd();
			return;
		}
		iterators.push(itr);
	}

	public void end() {
		if (iterators.size() > 0) {
			if (iterators.peek().next()) {
				iterators.poll();
			}
		}
	}

	public void terminate() {
		this.terminated = true;
	}

	public void terminateNoPrint() {
		this.terminated = true;
		this.printOnTerminate = false;
	}

	/*
	public void jump(int line) {
		for (int i = 0; i < instructions.size(); i++) {
			Instruction insn = instructions.get(i);
			if (insn.getLine() == line) {
				instruction = i;
				return;
			}
		}
		crash("JMP attempted to jump outside the program! (Instruction " + instructions.get((instruction - 1)).getLine() + " JMP to " + line + ")");
	}
	*/

	public void jumpToNode(int node) {
		if (node >= instructions.size()) {
			crash("Jump to node attempted to jump outside the program! (Instruction " + (instruction - 1) + " JMP to " + node + ")");
		}
		instruction = node;
	}

	public void jumpToEndif() {
		int debt = 0;
		for (int i = instruction; i < instructions.size(); i++) {
			Instruction ins = instructions.get(i);
			if (ins.getOpcode() == null) {
				continue;
			}
			if (Chars.conditional.contains(ins.getOpcode().getChars().get(0))) {
				debt++;
			}
			if (ins.getOpcode().getChars().size() == 1 && (ins.getOpcode().getChars().get(0) == Chars.ifElse || ins.getOpcode().getChars().get(0) == Chars.conditionalEnd)) {
				if (debt <= 0) {
					instruction = i;
					return;
				}
				debt--;
			}
		}
		// If no endif instruction is found then we will simply jump over the next line.
		// This special case should only be used for base-layer conditionals. Nested conditionals will
		// see the endif statement meant for the base-layer conditional and jump there instead
		instruction++;
	}

	private void jumpToEnd() {
		for (int i = instruction; i < instructions.size(); i++) {
			Instruction ins = instructions.get(i);
			if (ins.getOpcode() == null) {
				continue;
			}
			if (ins.getOpcode().getChars().size() == 1 && ins.getOpcode().getChars().get(0) == Chars.x2c /* end opcode */) {
				instruction = i + 1; // Don't actually run the end instruction
				return;
			}
		}
	}

	public void warn(String message) {
		System.out.println("Warning: " + message);
	}

	public void crash(String message) {
		System.err.println("Fatal: " + message);
		terminateNoPrint();
	}

	public void updateLastConditional(boolean result) {
		lastConditional = result;
	}
}
