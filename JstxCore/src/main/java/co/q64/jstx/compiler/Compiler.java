package co.q64.jstx.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compression.Base;
import co.q64.jstx.compression.Smaz;
import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.Opcode;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class Compiler {
	protected @Inject Compiler() {}

	protected @Inject Opcodes opcodes;
	protected @Inject Smaz smaz;
	protected @Inject Base base;
	protected @Inject CompilerOutputFactory output;

	public CompilerOutput compile(List<String> instructions) {
		int index = -1, characters = 0;
		List<String> compiledInsns = new ArrayList<>();
		StringBuilder result = new StringBuilder();
		instruction: for (String ins : instructions) {
			index++;
			if (ins.isEmpty()) {
				continue;
			}
			// Add debug information to output from last round
			if (index > 0) {
				compiledInsns.add(result.toString().substring(characters));
			}
			characters = +result.length();

			for (Opcode op : opcodes.all()) {
				if (op.getName().equals(ins)) {
					for (Chars c : op.getChars()) {
						result.append(c.getCharacter());
					}
					continue instruction;
				}
			}
			if (ins.startsWith("load ")) {
				String load = ins.substring(5);
				if (load.length() == 0) {
					return output.create("Attempted to load a 0 length literal (probably an empty load instruction). Line: " + (index + 1));
				}
				if (load.length() == 1) {
					result.append(Chars.literalSingle.getCharacter());
					result.append(load);
					continue;
				}
				if (load.length() == 2) {
					result.append(Chars.literalPair.getCharacter());
					result.append(load);
					continue;
				}
				if (base.canCompress(load)) {
					byte[] compressed = base.compress(load);
					if (compressed.length < load.length() && compressed.length <= 256 && compressed.length > 0) {
						result.append(Chars.literalCompressionMode1.getCharacter());
						result.append(Chars.fromInt(compressed.length - 1).getCharacter());
						for (byte b : compressed) {
							result.append(Chars.fromByte(b).getCharacter());
						}
						continue;
					}
				}
				// Check for casing that can cause horrible smaz performance. This is the special "compression mode 3".
				if (Character.isUpperCase(load.charAt(0))) {
					char[] chars = load.toCharArray();
					boolean valid = true;
					for (int i = 1; i < chars.length; i++) {
						if (Character.isUpperCase(chars[i])) {
							if (String.valueOf(chars[i - 1]).equals(" ")) {
								continue;
							}
							valid = false;
							break;
						} else {
							if (String.valueOf(chars[i - 1]).equals(" ")) {
								valid = false;
								break;
							}
						}
					}
					if (valid) {
						String lower = load.toLowerCase();
						if (smaz.canCompress(load)) {
							byte[] compressed = smaz.compress(lower);
							if (compressed.length < lower.length() && compressed.length <= 256 && compressed.length > 0) {
								result.append(Chars.literalCompressionMode3.getCharacter());
								result.append(Chars.fromInt(compressed.length - 1).getCharacter());
								for (byte b : compressed) {
									result.append(Chars.fromByte(b).getCharacter());
								}
								continue;
							}
						}
					}
				}
				if (smaz.canCompress(load)) {
					byte[] compressed = smaz.compress(load);
					if (compressed.length < load.length() && compressed.length <= 256 && compressed.length > 0) {
						result.append(Chars.literalCompressionMode2.getCharacter());
						result.append(Chars.fromInt(compressed.length - 1).getCharacter());
						for (byte b : compressed) {
							result.append(Chars.fromByte(b).getCharacter());
						}
						continue;
					}
				}
				if (load.length() > 2) {
					if (index > 0) {
						result.append(Chars.literalBegin.getCharacter());
					}
					result.append(load);
					result.append(Chars.literalUncompressed.getCharacter());
					continue;
				}
			}
			return output.create("Invalid instruction '" + ins + "' in source. Line: " + (index + 1));
		}
		compiledInsns.add(result.toString().substring(characters));
		return output.create(result.toString(), compiledInsns, instructions);
	}
}
