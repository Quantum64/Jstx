package co.q64.jstx.inject.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.Instruction;
import co.q64.jstx.lang.InstructionFactory;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.Opcode;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class CompiledLexer {
	protected @Inject CompiledLexer() {}

	protected @Inject InstructionFactory instructionFactory;
	protected @Inject LiteralFactory literalFactory;
	protected @Inject Opcodes opcodes;

	public List<Instruction> parse(String program) {
		// Check for implied literal
		for (char c : program.toCharArray()) {
			if (String.valueOf(c).equals(Chars.literalBegin.getCharacter())) {
				break;
			}
			if (Arrays.asList(Chars.literalUncompressed.getCharacter(), Chars.literalCompressionMode1.getCharacter(), Chars.literalCompressionMode2.getCharacter()).contains(String.valueOf(c))) {
				program = Chars.literalBegin.getCharacter() + program;
			}
		}
		boolean readingLiteral = false;
		boolean readingPair = false;
		int charsToRead = 0;
		StringBuilder currentLiteral = null;
		String opcodeQueue = "";
		List<Instruction> instructions = new ArrayList<Instruction>();
		char[] chars = program.toCharArray();
		for (int index = 0; index < chars.length; index++) {
			String c = String.valueOf(chars[index]);
			if (Chars.literalBegin.getCharacter().equals(c)) {
				readingLiteral = true;
				currentLiteral = new StringBuilder();
				continue;
			}
			if (Chars.literalUncompressed.getCharacter().equals(c)) {
				readingLiteral = false;
				instructions.add(instructionFactory.create(literalFactory.create(currentLiteral.toString())));
				continue;
			}
			if (Chars.literalCompressionMode1.getCharacter().equals(c)) {
				readingLiteral = false;
				continue;
			}
			if (Chars.literalCompressionMode2.getCharacter().equals(c)) {
				readingLiteral = false;
				continue;
			}
			if (readingLiteral) {
				currentLiteral.append(c);
				continue;
			}
			if (readingPair) {
				currentLiteral.append(c);
				charsToRead--;
				if (charsToRead == 0) {
					readingPair = false;
					instructions.add(instructionFactory.create(literalFactory.create(currentLiteral.toString())));
				}
				continue;
			}
			if (Chars.literalPair.getCharacter().equals(c)) {
				currentLiteral = new StringBuilder();
				readingPair = true;
				charsToRead = 2;
				continue;
			}
			if (Chars.literalSingle.getCharacter().equals(c)) {
				currentLiteral = new StringBuilder();
				readingPair = true;
				charsToRead = 1;
				continue;
			}
			String op = opcodeQueue + c;
			Opcode oc = null;
			for (Opcode opcode : opcodes.all()) {
				if (op.length() == opcode.getChars().size()) {
					boolean match = true;
					int i = 0;
					for (Chars ch : opcode.getChars()) {
						if (!ch.getCharacter().equals(String.valueOf(op.charAt(i)))) {
							match = false;
							break;
						}
						i++;
					}
					if (match == true) {
						oc = opcode;
					}
				}
			}
			if (oc == null) {
				opcodeQueue += c;
				continue;
			}
			opcodeQueue = "";
			Instruction instruction = instructionFactory.create(oc);
			instructions.add(instruction);
		}
		return instructions;
	}
}
