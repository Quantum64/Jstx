package co.q64.jstx.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compression.Base;
import co.q64.jstx.compression.Smaz;
import co.q64.jstx.lang.Instruction;
import co.q64.jstx.lang.InstructionFactory;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.Opcode;
import co.q64.jstx.opcode.Opcodes;
import co.q64.jstx.runtime.ByteBuffer;
import co.q64.jstx.runtime.Output;

@Singleton
public class Lexer {
	protected @Inject Lexer() {}

	protected @Inject InstructionFactory instructionFactory;
	protected @Inject LiteralFactory literalFactory;
	protected @Inject Opcodes opcodes;
	protected @Inject Smaz smaz;
	protected @Inject Base base;

	public List<Instruction> parse(String program, Output output) {
		// Check for implied literal
		for (char c : program.toCharArray()) {
			if (String.valueOf(c).equals(Chars.literalBegin.getCharacter())) {
				break;
			}
			if (Arrays.asList(Chars.literalUncompressed.getCharacter()).contains(String.valueOf(c))) {
				program = Chars.literalBegin.getCharacter() + program;
			}
		}
		boolean readingLiteral = false, smazSpecial = false;
		int smazToRead = 0, baseToRead = 0, shortToRead = 0;
		StringBuilder currentLiteral = null;
		ByteBuffer currentBuffer = null;
		String opcodeQueue = "";
		List<Instruction> instructions = new ArrayList<Instruction>();
		char[] chars = program.toCharArray();
		for (int index = 0; index < chars.length; index++) {
			String c = String.valueOf(chars[index]);
			if (opcodeQueue.isEmpty()) {
				if (shortToRead > 0) {
					currentLiteral.append(c);
					shortToRead--;
					if (shortToRead == 0) {
						instructions.add(instructionFactory.create(literalFactory.create(currentLiteral.toString())));
					}
					continue;
				}
				if (smazToRead > 0) {
					currentBuffer.put(Chars.fromCode(c).getByte());
					smazToRead--;
					if (smazToRead == 0) {
						String decomp = smaz.decompress(currentBuffer.array());
						if (smazSpecial) {
							StringBuilder caseCorrector = new StringBuilder();
							char[] chs = decomp.toCharArray();
							for (int i = 0; i < chs.length; i++) {
								if (i == 0 || String.valueOf(chs[i - 1]).equals(" ")) {
									caseCorrector.append(Character.toUpperCase(chs[i]));
									continue;
								}
								caseCorrector.append(chs[i]);
							}
							decomp = caseCorrector.toString();
						}
						instructions.add(instructionFactory.create(literalFactory.create(decomp)));
					}
					continue;
				}
				if (baseToRead > 0) {
					currentBuffer.put(Chars.fromCode(c).getByte());
					baseToRead--;
					if (baseToRead == 0) {
						instructions.add(instructionFactory.create(literalFactory.create(base.decompress(currentBuffer.array()))));
					}
					continue;
				}
				if (readingLiteral && Chars.literalUncompressed.getCharacter().equals(c)) {
					readingLiteral = false;
					instructions.add(instructionFactory.create(literalFactory.create(currentLiteral.toString())));
					continue;
				}
				if (readingLiteral) {
					currentLiteral.append(c);
					continue;
				}
				if (Chars.literalCompressionMode1.getCharacter().equals(c)) {
					index++;
					baseToRead = Chars.fromCode(String.valueOf(chars[index])).getId() + 1;
					currentBuffer = new ByteBuffer(baseToRead);
					continue;
				}
				if (Chars.literalCompressionMode2.getCharacter().equals(c) || Chars.literalCompressionMode3.getCharacter().equals(c)) {
					index++;
					smazToRead = Chars.fromCode(String.valueOf(chars[index])).getId() + 1;
					currentBuffer = new ByteBuffer(smazToRead);
					smazSpecial = Chars.literalCompressionMode3.getCharacter().equals(c);
					continue;
				}
				if (Chars.literalBegin.getCharacter().equals(c)) {
					readingLiteral = true;
					currentLiteral = new StringBuilder();
					continue;
				}

				if (Chars.literalPair.getCharacter().equals(c)) {
					currentLiteral = new StringBuilder();
					shortToRead = 2;
					continue;
				}
				if (Chars.literalSingle.getCharacter().equals(c)) {
					currentLiteral = new StringBuilder();
					shortToRead = 1;
					continue;
				}
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
				if (opcodeQueue.length() > 3) {
					output.println("Lexer Warning: Unusual opcode '" + opcodeQueue + "'. There is likely a syntax error in your program.");
				}
				continue;
			}
			opcodeQueue = "";
			Instruction instruction = instructionFactory.create(oc);
			instructions.add(instruction);
		}
		return instructions;
	}
}
