package co.q64.jstx.opcode;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.inject.Inject;

import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.runtime.Javascript;

public class JavascriptOpcodes extends OpcodeRegistry {
	protected @Inject Javascript javascript;

	protected @Inject JavascriptOpcodes() {
		super(OpcodeMarker.JAVASCRIPT);
	}

	@Override
	public void register() {
		r("javascript.eval", stack -> stack.push(javascript.evalFunction(stack.pop().toString(), Collections.emptyList())), "Evaluate the top stack value as a JavaScript function (ES6).");
		r("javascript.evalSingle", stack -> stack.push(javascript.evalFunction(stack.peek(2).toString(), Arrays.asList(stack.pull(2)))), "Evaluate the second stack value as a JavaScript function with the first stack vale as the argument.");
		r("javascript.evalSingleString", stack -> stack.push(javascript.evalFunction(stack.peek(2).toString(), Arrays.asList(javascript.literal(stack.pull(2))))), "Evaluate the second stack value as a JavaScript function with the first stack vale as a non-literal string argument.");
		r("javascript.evalPair", stack -> stack.push(javascript.evalFunction(stack.peek(3).toString(), Arrays.asList(stack.peek(2), stack.pull(3)))), "Evaluate the third stack value as a JavaScript function with the first two stack vale as the arguments.");
		r("javascript.evalPair", stack -> stack.push(javascript.evalFunction(stack.peek(4).toString(), Arrays.asList(stack.peek(3), stack.peek(2), stack.pull(4)))), "Evaluate the fourth stack value as a JavaScript function with the first three stack vale as the arguments.");
		r("javascript.evalList", stack -> stack.push(javascript.evalFunction(stack.peek(2).toString(), stack.pull(2).iterate())), "Evaluate the second stack value as a JavaScript function with the list on the first stack value as arguments.");
		r("javascript.evalListString", stack -> stack.push(javascript.evalFunction(stack.peek(2).toString(), stack.pull(2).iterate().stream().map(javascript::literal).collect(Collectors.toList()))), "Evaluate the second stack value as a JavaScript function with the list on the first stack value as arguments.");
		r("javascript.reverseEvalSingle", stack -> stack.swap().push(javascript.evalFunction(stack.peek(2).toString(), Arrays.asList(stack.pull(2)))), "Evaluate the first stack value as a JavaScript function with the second stack vale as the argument.");
		r("javascript.reverseEvalSingleString", stack -> stack.swap().push(javascript.evalFunction(stack.peek(2).toString(), Arrays.asList(javascript.literal(stack.pull(2))))), "Evaluate the first stack value as a JavaScript function with the second stack vale as the argument.");
		r("javascript.reverseEvalListString", stack -> stack.swap().push(javascript.evalFunction(stack.peek(2).toString(), stack.pull(2).iterate().stream().map(javascript::literal).collect(Collectors.toList()))), "Evaluate the first stack value as a JavaScript function with the list on the second stack value as arguments.");
	}
}
