package co.q64.jstx.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compression.Base;
import co.q64.jstx.compression.Smaz;
import co.q64.jstx.lang.opcode.Chars;
import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.Opcodes;

@Singleton
public class Compiler {
	protected @Inject Compiler() {}

	protected @Inject Opcodes opcodes;
	protected @Inject Smaz smaz;
	protected @Inject Base base;
	protected @Inject CompilerOutputFactory output;

	public CompilerOutput compile(List<String> input) {
		List<String> instructions = new ArrayList<>();
		for (String current : input) {
			if (current.startsWith("load")) {
				instructions.add(current);
				continue;
			}
			StringBuilder updated = new StringBuilder();
			for (char c : current.toCharArray()) {
				if (String.valueOf(c).equals("'") || String.valueOf(c).equals("#")) {
					break;
				}
				updated.append(c);
			}
			current = updated.toString().trim();
			if (!current.isEmpty()) {
				instructions.add(current);
			}
		}
		int index = -1, characters = 0;
		List<String> compiledInsns = new ArrayList<>();
		Map<String, Integer> markers = new HashMap<>();
		StringBuilder result = new StringBuilder();
		for (ListIterator<String> itr = instructions.listIterator(); itr.hasNext();) {
			String ins = itr.next();
			index++;
			if (ins.isEmpty()) {
				continue;
			}
			// Add debug information to output from last round
			if (index > 0) {
				if (result.toString().substring(characters).length() > 0) {
					compiledInsns.add(result.toString().substring(characters));
				}
			}
			characters = +result.length();
			if (ins.startsWith("def ") && ins.length() > 4) {
				markers.put(ins.substring(4), compiledInsns.size() + 1);
				itr.remove();
				continue;
			}
			if (ins.startsWith("jump ") && ins.length() > 5) {
				compiledInsns.add(null);
				result.append(ins);
				itr.set("def " + ins.substring(5));
				itr.add("jump");
				continue;
			}
			Optional<CompilerOutput> output = processInstruction(ins, result, index);
			if (output.isPresent()) {
				return output.get();
			}
		}
		compiledInsns.add(result.toString().substring(characters));
		compiledInsns.removeIf(o -> o == null);
		for (ListIterator<String> itr = compiledInsns.listIterator(); itr.hasNext();) {
			String ins = itr.next();
			if (ins.startsWith("jump ") && ins.length() > 5) {
				Integer target = markers.get(ins.substring(5));
				if (target == null) {
					return output.create("Jump '" + ins.substring(5) + "' was never defined!");
				}
				StringBuilder jumpInstruction = new StringBuilder();
				processInstruction("load " + target, jumpInstruction, index);
				itr.set(jumpInstruction.toString());
				jumpInstruction.setLength(0);
				processInstruction("jump", jumpInstruction, index);
				itr.add(jumpInstruction.toString());
			}
		}
		return output.create(compiledInsns, instructions);
	}

	private Optional<CompilerOutput> processInstruction(String ins, StringBuilder result, int index) {
		Optional<List<Chars>> opt = opcodes.lookupName(ins);
		if (opt.isPresent()) {
			for (Chars c : opt.get()) {
				if (c == null) {
					return Optional.empty();
				}
				result.append(c.getCharacter());
			}
			return Optional.empty();
		}
		if (ins.startsWith("load ")) {
			String load = ins.substring(5);
			if (load.length() == 0) {
				return Optional.of(output.create("Attempted to load a 0 length literal (probably an empty load instruction). Line: " + (index + 1)));
			}
			if (load.length() == 1) {
				result.append(opcodes.getChars(OpcodeMarker.LITERAL1).getCharacter());
				result.append(load);
				return Optional.empty();
			}
			if (load.length() == 2) {
				result.append(opcodes.getChars(OpcodeMarker.LITERAL2).getCharacter());
				result.append(load);
				return Optional.empty();
			}
			if (base.canCompress(load)) {
				byte[] compressed = base.compress(load);
				if (compressed.length < load.length() && compressed.length <= 256 && compressed.length > 0) {
					result.append(opcodes.getChars(OpcodeMarker.COMPRESSION1).getCharacter());
					result.append(Chars.fromInt(compressed.length - 1).getCharacter());
					for (byte b : compressed) {
						result.append(Chars.fromByte(b).getCharacter());
					}
					return Optional.empty();
				}
			}
			// Check for casing that can cause horrible smaz performance. This is the special "compression mode 3".
			if (Character.isUpperCase(load.charAt(0))) {
				char[] chars = load.toCharArray();
				boolean valid = true;
				for (int i = 1; i < chars.length; i++) {
					if (Character.isUpperCase(chars[i])) {
						if (String.valueOf(chars[i - 1]).equals(" ")) {
							return Optional.empty();
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
							result.append(opcodes.getChars(OpcodeMarker.COMPRESSION3).getCharacter());
							result.append(Chars.fromInt(compressed.length - 1).getCharacter());
							for (byte b : compressed) {
								result.append(Chars.fromByte(b).getCharacter());
							}
							return Optional.empty();
						}
					}
				}
			}
			if (smaz.canCompress(load)) {
				byte[] compressed = smaz.compress(load);
				if (compressed.length < load.length() && compressed.length <= 256 && compressed.length > 0) {
					result.append(opcodes.getChars(OpcodeMarker.COMPRESSION2).getCharacter());
					result.append(Chars.fromInt(compressed.length - 1).getCharacter());
					for (byte b : compressed) {
						result.append(Chars.fromByte(b).getCharacter());
					}
					return Optional.empty();
				}
			}
			if (load.length() > 2) {
				if (index > 0) {
					result.append(opcodes.getChars(OpcodeMarker.LITERAL).getCharacter());
				}
				result.append(load);
				result.append(opcodes.getChars(OpcodeMarker.UNCOMPRESSED).getCharacter());
				return Optional.empty();
			}
		}
		return Optional.of(output.create("Invalid instruction '" + ins + "' in source. Line: " + (index + 1)));
	}
}
