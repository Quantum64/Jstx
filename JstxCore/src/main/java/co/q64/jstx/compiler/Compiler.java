package co.q64.jstx.compiler;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.opcode.Chars;
import co.q64.jstx.opcode.Opcode;
import co.q64.jstx.opcode.Opcodes;

@Singleton
public class Compiler {
	protected @Inject Compiler() {}

	protected @Inject Opcodes opcodes;

	public String compile(List<String> instructions) {
		//TODO String compression
		StringBuilder result = new StringBuilder();
		instruction: for (String ins : instructions) {
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
				if (load.length() == 1) {
					result.append(Chars.literalSingle.getCharacter());
					result.append(load);
				}
				if (load.length() == 2) {
					result.append(Chars.literalPair.getCharacter());
					result.append(load);
				}
				if (load.length() > 2) {
					result.append(Chars.literalBegin.getCharacter());
					result.append(load);
					result.append(Chars.literalUncompressed.getCharacter());
				}
				continue;
			}
			throw new IllegalStateException("Invalid instruction in source: " + ins);
		}
		return result.toString();
	}
}
