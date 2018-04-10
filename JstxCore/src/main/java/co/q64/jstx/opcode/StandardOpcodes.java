package co.q64.jstx.opcode;

import static co.q64.jstx.lang.opcode.OpcodeMarker.COMPRESSION1;
import static co.q64.jstx.lang.opcode.OpcodeMarker.COMPRESSION2;
import static co.q64.jstx.lang.opcode.OpcodeMarker.COMPRESSION3;
import static co.q64.jstx.lang.opcode.OpcodeMarker.CONDITIONAL;
import static co.q64.jstx.lang.opcode.OpcodeMarker.ELSE;
import static co.q64.jstx.lang.opcode.OpcodeMarker.END;
import static co.q64.jstx.lang.opcode.OpcodeMarker.ENDIF;
import static co.q64.jstx.lang.opcode.OpcodeMarker.EXIT;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LITERAL;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LITERAL1;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LITERAL2;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LZMA;
import static co.q64.jstx.lang.opcode.OpcodeMarker.PRIORITIZATION;
import static co.q64.jstx.lang.opcode.OpcodeMarker.SPECIAL;
import static co.q64.jstx.lang.opcode.OpcodeMarker.STANDARD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.Program;
import co.q64.jstx.lang.Stack;
import co.q64.jstx.lang.opcode.OpcodeCache;
import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.types.Comparison;
import co.q64.jstx.types.Operation;

@Singleton
public class StandardOpcodes extends OpcodeRegistry {
	protected @Inject StandardOpcodes() {
		super(STANDARD);
	}

	protected @Inject Null nul;
	protected @Inject OpcodeCache cache;

	@Override
	public void register() {
		r("load 0", stack -> stack.push(0));
		r("load 1", stack -> stack.push(1));
		r("load 2", stack -> stack.push(2));
		r("load 3", stack -> stack.push(3));
		r("load 4", stack -> stack.push(4));
		r("load 5", stack -> stack.push(5));
		r("load 6", stack -> stack.push(6));
		r("load 7", stack -> stack.push(7));
		r("load 8", stack -> stack.push(8));
		r("load 9", stack -> stack.push(9));
		r("endif", ENDIF, stack -> {}, "End a conditional block.");
		r("UNUSED literal begin", LITERAL, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		r("UNUSED literal special", SPECIAL, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		r("UNUSED literal compression mode 1", COMPRESSION1, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		r("UNUSED literal compression mode 2", COMPRESSION2, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		r("UNUSED literal compression mode 3", COMPRESSION3, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		r("UNUSED literal begin 2 character", LITERAL1, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		r("UNUSED literal begin 1 character", LITERAL2, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		r("UNUSED literal LZMA", LZMA, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		r("if =", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.EQUAL)), "Enter a conditional block if the top two stack values are equal.");
		r("if !=", CONDITIONAL, stack -> conditional(stack, (v, o) -> !v.compare(o, Comparison.EQUAL)), "Enter a conditional block if the top two stack values not equal.");
		r("if >", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.GREATER)), "Enter a conditional block if the second stack value is greater than the top stack value.");
		r("if >=", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.GREATER) || v.compare(o, Comparison.EQUAL)), "Enter a conditional block if the second stack value is greater than or equal to the top stack value.");
		r("if <", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.LESS)), "Enter a conditional block if the second stack value is less than the top stack value.");
		r("if <=", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.LESS) || v.compare(o, Comparison.EQUAL)), "Enter a conditional block if the second stack value is less than or equal to the top stack value.");
		r("if true", CONDITIONAL, stack -> conditional(stack.push(true), (v, o) -> v.compare(o, Comparison.EQUAL)), "Enter a conditional block if first stack value exactly equals true.");
		r("if false", CONDITIONAL, stack -> conditional(stack.push(false), (v, o) -> v.compare(o, Comparison.EQUAL)), "Enter a conditional block if first stack value exactly equals false.");
		r("load true", stack -> stack.push(true));
		r("load false", stack -> stack.push(false));
		r("load null", stack -> stack.push(nul));
		r("else", ELSE, stack -> processEsle(stack), "Enter a conditional block if and only if the last conditional block was not executed.");
		r("pop", stack -> stack.pop(), "Remove the first stack values from the stack.");
		r("pop 2", stack -> stack.pop(2), "Remove the first two stack values from the stack.");
		r("clr", stack -> stack.clr(), "Remove all values on the stack.");
		r("dup", stack -> stack.dup(), "Push a copy of the first stack value.");
		r("dup 2", stack -> stack.dup(2), "Push two copies of the first stack value.");
		r("dup 3", stack -> stack.dup(3), "Push three copies of the first stack value.");
		r("dup x", stack -> stack.dup(Math.abs(stack.pop().asInt())), "Push the second stack value the absolute value of the first stack value times.");
		r("swp", stack -> stack.swap(), "Swap the top two stack values.");
		r("ldv", stack -> stack.push(stack.getProgram().getRegisters().getGlobal().get(stack.pop())), "Push the value from the global variable table with the name of the first stack value.");
		r("sdv", stack -> stack.getProgram().getRegisters().getGlobal().put(stack.pop(), stack.pop()), "Saves the second stack value to the the global variable table with the name of the first stack value.");
		r("ldr a", stack -> stack.push(stack.getProgram().getRegisters().getA()), "Push the value contained in the a register.");
		r("ldr b", stack -> stack.push(stack.getProgram().getRegisters().getB()), "Push the value contained in the b register.");
		r("ldr c", stack -> stack.push(stack.getProgram().getRegisters().getC()), "Push the value contained in the c register.");
		r("ldr d", stack -> stack.push(stack.getProgram().getRegisters().getD()), "Push the value contained in the d register.");
		r("ldr i", stack -> stack.push(stack.getProgram().getRegisters().getI()), "Push the value contained in the iteration index register.");
		r("ldr o", stack -> stack.push(stack.getProgram().getRegisters().getO()), "Push the value contained in the iteration element register.");
		r("sdr a", stack -> stack.getProgram().getRegisters().setA(stack.pop()), "Store the first stack value in the a register.");
		r("sdr b", stack -> stack.getProgram().getRegisters().setB(stack.pop()), "Store the first stack value in the b register.");
		r("sdr c", stack -> stack.getProgram().getRegisters().setC(stack.pop()), "Store the first stack value in the c register.");
		r("sdr d", stack -> stack.getProgram().getRegisters().setD(stack.pop()), "Store the first stack value in the d register.");
		r("iterate", stack -> stack.getProgram().iterate(false), "Enter an iteration block over the first stack value.");
		r("iterate stack", stack -> stack.getProgram().iterate(true), "Enter an iteration block over the first stack value and push the iteration element register at the begining of each loop.");
		r("end", END, stack -> stack.getProgram().end(), "End an iteration block.");
		r("print", stack -> stack.getProgram().print(stack.pop().toString()), "Print the first stack value.");
		r("print space", stack -> stack.getProgram().print(" "), "Print a space character.");
		r("println", stack -> stack.getProgram().println(stack.pop().toString()), "Print the first stack value, then a newline.");
		r("exit", EXIT, stack -> stack.getProgram().terminate(), "End program execution, then prints the top stack value followed by a newline.");
		r("terminate", stack -> stack.getProgram().terminateNoPrint(), "End program execution.");
		r("restart", stack -> stack.getProgram().jumpToNode(1), "Jump to the first instruction in the program without pushing the call stack.");
		r("jump", stack -> stack.getProgram().jump(stack.pop().asInt()), "Push the next instruction pointer to the call stack then jump to the first stack value interpreted as an instruction pointer.");
		r("return", stack -> stack.getProgram().jumpReturn(), "Jump to the top instruction pointer on the call stack.");
		r("str", stack -> stack.push(stack.pop().toString()), "Push the first stack value as a string.");
		r("args", stack -> stack.push(stack.getProgram().getArgs()), "Push the command line arguments.");
		r("args list", stack -> stack.push(Arrays.stream(stack.getProgram().getArgs().split(" ")).collect(Collectors.toList())), "Push the command line arguments as a list seperated by spaces.");
		r("flatten", stack -> pushStackAsList(stack), "Collapse all stack values into a list, then push that list.");
		r("flatten hard", stack -> pushStackSplit(stack, ""), "Collapse all stack values into a string, then push that string.");
		r("flatten soft", stack -> pushStackSplit(stack, " "), "Collapse all stack values into a string seperated by spaces, then push that string.");
		r("+", stack -> stack.push(stack.peek(2).operate(stack.pull(2), Operation.ADD)), "Push the sum of the second and first stack values.");
		r("-", stack -> stack.push(stack.peek(2).operate(stack.pull(2), Operation.SUBTRACT)), "Push the difference of the second and first stack values.");
		r("*", stack -> stack.push(stack.peek(2).operate(stack.pull(2), Operation.MULTIPLY)), "Push the product of the second and first stack values.");
		r("/", stack -> stack.push(stack.peek(2).operate(stack.pull(2), Operation.DIVIDE)), "Push the quotient of the second and first stack values.");
		r("%", stack -> stack.push(stack.peek(2).asInt() % stack.pull(2).asInt()), "Push the modulus of the second and first stack values.");
		// 73
		r("remap.math", PRIORITIZATION, stack -> cache.prioritize(OpcodeMarker.MATH), "Prioritize math opcodes for one byte instructions.");
		r("remap.list", PRIORITIZATION, stack -> cache.prioritize(OpcodeMarker.LIST), "Prioritize list opcodes for one byte instructions.");
		r("remap.string", PRIORITIZATION, stack -> cache.prioritize(OpcodeMarker.STRING), "Prioritize string opcodes for one byte instructions.");
		r("remap.meta", PRIORITIZATION, stack -> cache.prioritize(OpcodeMarker.META), "Prioritize meta opcodes for one byte instructions.");
		r("remap.bignumber", PRIORITIZATION, stack -> cache.prioritize(OpcodeMarker.BIG_NUMBER), "Prioritize big number opcodes for one byte instructions.");
		r("remap.graphics", PRIORITIZATION, stack -> cache.prioritize(OpcodeMarker.GRAPHICS), "Prioritize graphics opcodes for one byte instructions.");
		r("remap.integer", PRIORITIZATION, stack -> cache.prioritize(OpcodeMarker.GRAPHICS), "Prioritize integer opcodes for one byte instructions.");
		// 79
	}

	private void processEsle(Stack stack) {
		Program p = stack.getProgram();
		if (p.isLastConditional()) {
			p.jumpToEndif();
		}
	}

	private void conditional(Stack stack, BiFunction<Value, Value, Boolean> func) {
		Value b = stack.pop();
		Value a = stack.pop();
		Program p = stack.getProgram();
		if (func.apply(a, b)) {
			p.updateLastConditional(true);
			return;
		}
		p.updateLastConditional(false);
		p.jumpToEndif();
	}

	private void pushStackAsList(Stack stack) {
		if (stack.size() <= 0) {
			return;
		}
		List<Value> values = new ArrayList<>();
		for (int i = 0; i < stack.size(); i++) {
			values.add(stack.peek(stack.size() - i));
		}
		stack.clr();
		stack.push(values);
	}

	private void pushStackSplit(Stack stack, String splitter) {
		if (stack.size() <= 0) {
			return;
		}
		StringBuilder value = new StringBuilder();
		for (int i = 0; i < stack.size(); i++) {
			value.append(stack.peek(stack.size() - i));
			if (i < stack.size() - 1) {
				value.append(splitter);
			}
		}
		stack.clr();
		stack.push(value.toString());
	}
}
