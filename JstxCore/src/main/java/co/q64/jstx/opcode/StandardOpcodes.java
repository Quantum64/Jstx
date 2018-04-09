package co.q64.jstx.opcode;

import static co.q64.jstx.lang.opcode.OpcodeMarker.COMPRESSION1;
import static co.q64.jstx.lang.opcode.OpcodeMarker.COMPRESSION2;
import static co.q64.jstx.lang.opcode.OpcodeMarker.COMPRESSION3;
import static co.q64.jstx.lang.opcode.OpcodeMarker.CONDITIONAL;
import static co.q64.jstx.lang.opcode.OpcodeMarker.ELSE;
import static co.q64.jstx.lang.opcode.OpcodeMarker.END;
import static co.q64.jstx.lang.opcode.OpcodeMarker.ENDIF;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LITERAL;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LITERAL1;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LITERAL2;
import static co.q64.jstx.lang.opcode.OpcodeMarker.SPECIAL;
import static co.q64.jstx.lang.opcode.OpcodeMarker.EXIT;
import static co.q64.jstx.lang.opcode.OpcodeMarker.LZMA;

import java.util.function.BiFunction;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.Program;
import co.q64.jstx.lang.Stack;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.lang.value.Null;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.types.Comparison;
import co.q64.jstx.types.Operation;

@Singleton
public class StandardOpcodes implements OpcodeRegistry {
	protected @Inject StandardOpcodes() {}

	protected @Inject LiteralFactory literal;
	protected @Inject Null nul;

	@Override
	public void register(Opcodes op) {
		op.reg("load 0", stack -> stack.push(literal.create("0")));
		op.reg("load 1", stack -> stack.push(literal.create("1")));
		op.reg("load 2", stack -> stack.push(literal.create("2")));
		op.reg("load 3", stack -> stack.push(literal.create("3")));
		op.reg("load 4", stack -> stack.push(literal.create("4")));
		op.reg("load 5", stack -> stack.push(literal.create("5")));
		op.reg("load 6", stack -> stack.push(literal.create("6")));
		op.reg("load 7", stack -> stack.push(literal.create("7")));
		op.reg("load 8", stack -> stack.push(literal.create("8")));
		op.reg("load 9", stack -> stack.push(literal.create("9")));
		op.reg("endif", ENDIF, stack -> {}, "End a conditional block.");
		op.reg("UNUSED literal begin", LITERAL, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal special", SPECIAL, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal compression mode 1", COMPRESSION1, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal compression mode 2", COMPRESSION2, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal compression mode 3", COMPRESSION3, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal begin 2 character", LITERAL1, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal begin 1 character", LITERAL2, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("UNUSED literal LZMA", LZMA, stack -> stack.getProgram().crash("The interpreter attempted to execute an unused literal opcode!"));
		op.reg("if =", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.EQUAL)), "Enter a conditional block if the top two stack values are equal.");
		op.reg("if !=", CONDITIONAL, stack -> conditional(stack, (v, o) -> !v.compare(o, Comparison.EQUAL)), "Enter a conditional block if the top two stack values not equal.");
		op.reg("if >", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.GREATER)), "Enter a conditional block if the second stack value is greater than the top stack value.");
		op.reg("if >=", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.GREATER) || v.compare(o, Comparison.EQUAL)), "Enter a conditional block if the second stack value is greater than or equal to the top stack value.");
		op.reg("if <", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.LESS)), "Enter a conditional block if the second stack value is less than the top stack value.");
		op.reg("if <=", CONDITIONAL, stack -> conditional(stack, (v, o) -> v.compare(o, Comparison.LESS) || v.compare(o, Comparison.EQUAL)), "Enter a conditional block if the second stack value is less than or equal to the top stack value.");
		op.reg("if true", CONDITIONAL, stack -> conditional(stack.push(true), (v, o) -> v.compare(o, Comparison.EQUAL)), "Enter a conditional block if first stack value exactly equals true.");
		op.reg("if false", CONDITIONAL, stack -> conditional(stack.push(false), (v, o) -> v.compare(o, Comparison.EQUAL)), "Enter a conditional block if first stack value exactly equals false.");
		op.reg("load true", stack -> stack.push(true));
		op.reg("load false", stack -> stack.push(false));
		op.reg("load null", stack -> stack.push(nul));
		op.reg("else", ELSE, stack -> processEsle(stack), "Enter a conditional block if and only if the last conditional block was not executed.");
		op.reg("pop", stack -> stack.pop(), "Remove the first stack values from the stack.");
		op.reg("pop 2", stack -> stack.pop(2), "Remove the first two stack values from the stack.");
		op.reg("clr", stack -> stack.clr(), "Remove all values on the stack.");
		op.reg("dup", stack -> stack.dup(), "Push a copy of the first stack value.");
		op.reg("swp", stack -> stack.swap(), "Swap the top two stack values.");
		op.reg("ldv", stack -> stack.push(stack.getProgram().getRegisters().getGlobal().get(stack.pop())), "Push the value from the global variable table with the name of the first stack value.");
		op.reg("sdv", stack -> stack.getProgram().getRegisters().getGlobal().put(stack.pop(), stack.pop()), "Saves the second stack value to the the global variable table with the name of the first stack value.");
		op.reg("ldr a", stack -> stack.push(stack.getProgram().getRegisters().getA()), "Push the value contained in the a register.");
		op.reg("ldr b", stack -> stack.push(stack.getProgram().getRegisters().getB()), "Push the value contained in the b register.");
		op.reg("ldr c", stack -> stack.push(stack.getProgram().getRegisters().getC()), "Push the value contained in the c register.");
		op.reg("ldr d", stack -> stack.push(stack.getProgram().getRegisters().getD()), "Push the value contained in the d register.");
		op.reg("ldr i", stack -> stack.push(stack.getProgram().getRegisters().getI()), "Push the value contained in the iteration index register.");
		op.reg("ldr o", stack -> stack.push(stack.getProgram().getRegisters().getO()), "Push the value contained in the iteration element register.");
		op.reg("sdr a", stack -> stack.getProgram().getRegisters().setA(stack.pop()), "Store the first stack value in the a register.");
		op.reg("sdr b", stack -> stack.getProgram().getRegisters().setB(stack.pop()), "Store the first stack value in the b register.");
		op.reg("sdr c", stack -> stack.getProgram().getRegisters().setC(stack.pop()), "Store the first stack value in the c register.");
		op.reg("sdr d", stack -> stack.getProgram().getRegisters().setD(stack.pop()), "Store the first stack value in the d register.");
		op.reg("iterate", stack -> stack.getProgram().iterate(false), "Enter an iteration block over the first stack value.");
		op.reg("iterate stack", stack -> stack.getProgram().iterate(true), "Enter an iteration block over the first stack value and push the iteration element register at the begining of each loop.");
		op.reg("end", END, stack -> stack.getProgram().end(), "End an iteration block.");
		op.reg("print", stack -> stack.getProgram().print(stack.pop().toString()), "Print the first stack value.");
		op.reg("print space", stack -> stack.getProgram().print(" "), "Print a space character.");
		op.reg("println", stack -> stack.getProgram().println(stack.pop().toString()), "Print the first stack value, then a newline.");
		op.reg("exit", EXIT, stack -> stack.getProgram().terminate(), "End program execution, then prints the top stack value followed by a newline.");
		op.reg("terminate", stack -> stack.getProgram().terminateNoPrint(), "End program execution.");
		op.reg("restart", stack -> stack.getProgram().jumpToNode(1), "Jump to the first instruction in the program without pushing the call stack.");
		op.reg("jump", stack -> stack.getProgram().jump(stack.pop().asInt()), "Push the next instruction pointer to the call stack then jump to the first stack value interpreted as an instruction pointer.");
		op.reg("return", stack -> stack.getProgram().jumpReturn(), "Jump to the top instruction pointer on the call stack.");
		op.reg("str", stack -> stack.push(stack.pop().toString()), "Push the first stack value as a string.");
		op.reg("+", stack -> stack.push(stack.peek(2).operate(stack.pull(2), Operation.ADD)), "Push the sum of the second and first stack values.");
		op.reg("-", stack -> stack.push(stack.peek(2).operate(stack.pull(2), Operation.SUBTRACT)), "Push the difference of the second and first stack values.");
		op.reg("*", stack -> stack.push(stack.peek(2).operate(stack.pull(2), Operation.MULTIPLY)), "Push the product of the second and first stack values.");
		op.reg("/", stack -> stack.push(stack.peek(2).operate(stack.pull(2), Operation.DIVIDE)), "Push the quotient of the second and first stack values.");
		op.reg("%", stack -> stack.push(stack.peek(2).asInt() % stack.pull(2).asInt()), "Push the modulus of the second and first stack values.");
		// 0x37

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
}
