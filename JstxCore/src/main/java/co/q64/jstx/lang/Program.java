package co.q64.jstx.lang;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.jstx.factory.IteratorFactoryFactory;
import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lexer.Lexer;
import co.q64.jstx.runtime.Output;
import lombok.Getter;

@AutoFactory
public class Program {
	private StackFactory stackFactory;
	private RegistersFactory registersFactory;
	private IteratorFactory iteratorFactory;
	private Opcodes opcodes;

	private @Getter Output output;
	private @Getter List<Instruction> instructions;
	private @Getter Stack stack;
	private @Getter Registers registers;
	private @Getter String source;
	private @Getter String args;

	private @Getter int instruction;
	private @Getter boolean printOnTerminate, terminated;
	private Deque<Iterator> iterators = new ArrayDeque<>();
	private Deque<Integer> jumps = new ArrayDeque<>();
	private @Getter boolean lastConditional = false; // TODO replace with stack?
	private long start;

	protected Program(@Provided StackFactory stackFactory, @Provided RegistersFactory registersFactory, @Provided IteratorFactoryFactory iteratorFactory, @Provided Opcodes opcodes, @Provided Lexer lexer, String source, String args, Output output) {
		this.stackFactory = stackFactory;
		this.registersFactory = registersFactory;
		this.iteratorFactory = iteratorFactory.getFactory();
		this.instructions = lexer.parse(source, output);
		this.source = source;
		this.opcodes = opcodes;
		this.args = args;
		this.output = output;
		instructions.add(0, new Instruction());
	}

	public void execute() {
		execute(true);
	}

	public void execute(boolean full) {
		this.opcodes.reset();
		this.stack = stackFactory.create(this);
		this.registers = registersFactory.create();
		this.printOnTerminate = true;
		this.terminated = false;
		this.instruction = 0;
		this.start = System.currentTimeMillis();
		this.iterators.clear();
		this.jumps.clear();
		if (!args.isEmpty()) {
			stack.push(args);
		}
		while (full) {
			if (terminated) {
				break;
			}
			if (instructions.size() <= instruction) {
				break;
			}
			if (System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(2) > start) {
				crash("Unusually long execution time! (2000ms)");
				continue;
			}
			step();
		}
		if (printOnTerminate) {
			println(stack.pop().toString());
		}
	}

	public void step() {
		if (instructions.size() < instruction) {
			return;
		}
		Instruction current = instructions.get(instruction);
		instruction++;
		try {
			current.execute(stack);
		} catch (Exception e) {
			crash(e.getClass().getSimpleName() + ": " + e.getMessage() + " [Instruction: " + current.getInstruction() + ", Line: " + (instruction - 1) + "]");
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
				if (iterators.size() > 0) {
					iterators.peek().register();
				}
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

	public void jump(int node) {
		jumps.push(instruction);
		jumpToNode(node);
	}

	public void jumpToNode(int node) {
		if (node >= instructions.size()) {
			crash("Jump to node attempted to jump outside the program! (Instruction " + (instruction - 1) + " JMP to " + node + ")");
			return;
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
			if (opcodes.getFlags(OpcodeMarker.CONDITIONAL).contains(ins.getOpcode()) || (ins.getOpcode().equals(opcodes.getFlag(OpcodeMarker.ELSE)))) {
				debt++;
			}
			if (ins.getOpcode().equals(opcodes.getFlag(OpcodeMarker.ELSE)) || ins.getOpcode().equals(opcodes.getFlag(OpcodeMarker.ENDIF))) {
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
			if (ins.getOpcode().equals(opcodes.getFlag(OpcodeMarker.END))) {
				instruction = i + 1; // Don't actually run the end instruction
				return;
			}
		}
	}

	public void jumpReturn() {
		if (jumps.size() <= 0) {
			crash("Call stack underflow detected! Likely attempted to return without calling a function: did program execution fall through a function declaration? (Instruction " + (instruction - 1) + " RETURN)");
			return;
		}
		jumpToNode(jumps.poll());
	}

	public void warn(String message) {
		output.println("");
		output.println("Warning: " + message);
	}

	public void crash(String message) {
		output.println("");
		output.println("Fatal: " + message);
		output.println("The program cannot continue and will now terminate.");
		terminateNoPrint();
	}

	public void updateLastConditional(boolean result) {
		lastConditional = result;
	}

	public void print(String str) {
		output.print(str.replace("\\n", "\n"));
	}

	public void println(String str) {
		output.println(str.replace("\\n", "\n"));
	}
}
