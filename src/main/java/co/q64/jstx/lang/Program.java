package co.q64.jstx.lang;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.LiteralFactoryFactory;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.opcode.Chars;
import lombok.Getter;

@AutoFactory
public class Program {
	private StackFactory stackFactory;
	private RegistersFactory registersFactory;
	private LiteralFactory literal;

	private @Getter List<Instruction> instructions;
	private @Getter Stack stack;
	private @Getter Registers registers;

	private @Getter int instruction;
	private boolean printOnTerminate;
	private boolean terminated;
	private boolean iteratorStack;
	private int iteratorLine;
	private int iteratorIndex;
	private Queue<Value> iterator = new LinkedList<>();
	private @Getter boolean lastConditional = false;

	protected Program(@Provided StackFactory stackFactory, @Provided RegistersFactory registersFactory, @Provided LiteralFactoryFactory literal, List<Instruction> instructions) {
		this.stackFactory = stackFactory;
		this.registersFactory = registersFactory;
		this.literal = literal.getFactory();
		this.instructions = instructions;
		instructions.add(0, new Instruction());
	}

	public void execute() {
		this.stack = stackFactory.create(this);
		this.registers = registersFactory.create();
		this.printOnTerminate = true;
		this.terminated = false;
		this.instruction = 0;
		this.iteratorIndex = 0;
		this.iteratorLine = 0;
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
		iteratorStack = onStack;
		iterator.clear();
		iterator.addAll(stack.pop().iterate());
		if (iterator.size() > 0) {
			iteratorIndex = 0;
			iteratorLine = instruction;
			registers.setI(literal.create(String.valueOf(iteratorIndex)));
			registers.setO(iterator.poll());
			if (iteratorStack) {
				stack.push(registers.getO());
			}
		}
	}

	public void end() {
		if (iterator.size() > 0) {
			instruction = iteratorLine;
			iteratorIndex++;
			registers.setI(literal.create(String.valueOf(iteratorIndex)));
			registers.setO(iterator.poll());
			if (iteratorStack) {
				stack.push(registers.getO());
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
			if (ins.getOpcode().getChars().get(0) == Chars.ifElse || ins.getOpcode().getChars().get(0) == Chars.conditionalEnd) {
				if (debt <= 0) {
					instruction = i;
					return;
				}
				debt--;
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
