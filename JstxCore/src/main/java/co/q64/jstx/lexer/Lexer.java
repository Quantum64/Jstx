package co.q64.jstx.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compression.Base;
import co.q64.jstx.compression.Insanity;
import co.q64.jstx.compression.Lzma;
import co.q64.jstx.compression.Shoco;
import co.q64.jstx.compression.Smaz;
import co.q64.jstx.lang.Instruction;
import co.q64.jstx.lang.InstructionFactory;
import co.q64.jstx.lang.opcode.Chars;
import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.lang.value.LiteralFactory;
import co.q64.jstx.runtime.Output;
import co.q64.jstx.runtime.common.ByteBuffer;

@Singleton
public class Lexer {
	protected @Inject Lexer() {}

	protected @Inject InstructionFactory instructionFactory;
	protected @Inject LiteralFactory literalFactory;
	protected @Inject Opcodes opcodes;
	protected @Inject Smaz smaz;
	protected @Inject Shoco shoco;
	protected @Inject Insanity insanity;
	protected @Inject Base base;
	protected @Inject Lzma lzma;

	public List<Instruction> parse(String program, Output output) {
		String codepage = Arrays.stream(Chars.values()).map(Chars::getCharacter).collect(Collectors.joining());
		for (char c : program.toCharArray()) {
			if (!codepage.contains(String.valueOf(c))) {
				StringBuilder decoded = new StringBuilder();
				for (int ch : insanity.codePoints(program).toArray()) {
					int in = insanity.getIndex(ch);
					byte[] data = new byte[2];
					data[0] = (byte) (in & 0xFF);
					data[1] = (byte) ((in >> 8) & 0xFF);
					decoded.append(Chars.fromByte(data[1]).getCharacter());
					decoded.append(Chars.fromByte(data[0]).getCharacter());
				}
				program = decoded.toString();
				break;
			}
		}
		boolean readingLiteral = false, smazSpecial = false;
		int smazToRead = 0, baseToRead = 0, shortToRead = 0, specialToRead = 0, lzmaToRead = 0;
		StringBuilder currentLiteral = null;
		ByteBuffer currentBuffer = null;
		String opcodeQueue = "";
		List<Instruction> instructions = new ArrayList<Instruction>();
		char[] chars = program.toCharArray();
		for (int index = 0; index < chars.length; index++) {
			String c = String.valueOf(chars[index]);
			if (opcodeQueue.isEmpty()) {
				if (shortToRead > 0) {
					currentLiteral.append(Chars.fromInt(~Chars.fromCode(c).getId() & 0xff).getCharacter());
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
				if (specialToRead > 0) {
					currentBuffer.put(Chars.fromCode(c).getByte());
					specialToRead--;
					if (specialToRead == 0) {
						String decomp = shoco.decompress(currentBuffer.array());
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
				if (lzmaToRead > 0) {
					currentBuffer.put(Chars.fromCode(c).getByte());
					lzmaToRead--;
					if (lzmaToRead == 0) {
						instructions.add(instructionFactory.create(literalFactory.create(lzma.decompress(currentBuffer.array()))));
					}
					continue;
				}
				if (readingLiteral && opcodes.getChars(OpcodeMarker.SPECIAL).getCharacter().equals(c)) {
					readingLiteral = false;
					StringBuilder literal = new StringBuilder();
					for (char ch : currentLiteral.toString().toCharArray()) {
						literal.append(Chars.fromInt(~Chars.fromCode(String.valueOf(ch)).getId() & 0xff).getCharacter());
					}
					instructions.add(instructionFactory.create(literalFactory.create(literal.toString())));
					continue;
				}
				if (readingLiteral) {
					currentLiteral.append(c);
					continue;
				}
				if (opcodes.getChars(OpcodeMarker.COMPRESSION1).getCharacter().equals(c)) {
					index++;
					baseToRead = Chars.fromCode(String.valueOf(chars[index])).getId() + 1;
					currentBuffer = new ByteBuffer(baseToRead);
					continue;
				}
				if (opcodes.getChars(OpcodeMarker.COMPRESSION2).getCharacter().equals(c) || opcodes.getChars(OpcodeMarker.COMPRESSION3).getCharacter().equals(c)) {
					index++;
					smazToRead = Chars.fromCode(String.valueOf(chars[index])).getId() + 1;
					currentBuffer = new ByteBuffer(smazToRead);
					smazSpecial = opcodes.getChars(OpcodeMarker.COMPRESSION3).getCharacter().equals(c);
					continue;
				}
				if (opcodes.getChars(OpcodeMarker.LITERAL).getCharacter().equals(c)) {
					readingLiteral = true;
					currentLiteral = new StringBuilder();
					continue;
				}

				if (opcodes.getChars(OpcodeMarker.LITERAL2).getCharacter().equals(c)) {
					currentLiteral = new StringBuilder();
					shortToRead = 2;
					continue;
				}
				if (opcodes.getChars(OpcodeMarker.LITERAL1).getCharacter().equals(c)) {
					currentLiteral = new StringBuilder();
					shortToRead = 1;
					continue;
				}
				if (opcodes.getChars(OpcodeMarker.SPECIAL).getCharacter().equals(c)) {
					index++;
					specialToRead = Chars.fromCode(String.valueOf(chars[index])).getId() + 1;
					currentBuffer = new ByteBuffer(specialToRead);
					continue;
				}
				if (opcodes.getChars(OpcodeMarker.LZMA).getCharacter().equals(c)) {
					index++;
					lzmaToRead = Chars.fromCode(String.valueOf(chars[index])).getId() + 1;
					if (lzmaToRead == 256) {
						index++;
						lzmaToRead += Chars.fromCode(String.valueOf(chars[index])).getId();
					}
					currentBuffer = new ByteBuffer(lzmaToRead);
					continue;
				}
			}
			Optional<Integer> oc = opcodes.lookupSymbol(opcodeQueue + c);
			if (!oc.isPresent()) {
				opcodeQueue += c;
				if (opcodeQueue.length() > 3) {
					output.println("Lexer Warning: Unusual opcode '" + opcodeQueue + "'. There is likely a syntax error in your program.");
				}
				continue;
			}
			opcodeQueue = "";
			Instruction instruction = instructionFactory.create(oc.get());
			instructions.add(instruction);
		}
		return instructions;
	}
}
