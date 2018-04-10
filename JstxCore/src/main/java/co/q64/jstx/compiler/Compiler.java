package co.q64.jstx.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compression.Base;
import co.q64.jstx.compression.Lzma;
import co.q64.jstx.compression.Shoco;
import co.q64.jstx.compression.Smaz;
import co.q64.jstx.lang.Stack;
import co.q64.jstx.lang.opcode.Chars;
import co.q64.jstx.lang.opcode.OpcodeCache;
import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.Opcodes;

@Singleton
public class Compiler {
	protected @Inject Compiler() {}

	protected @Inject Opcodes opcodes;
	protected @Inject OpcodeCache cache;
	protected @Inject Smaz smaz;
	protected @Inject Base base;
	protected @Inject Shoco shoco;
	protected @Inject Lzma lzma;
	protected @Inject CompilerOutputFactory output;

	private String codepage = Arrays.stream(Chars.values()).map(Chars::getCharacter).collect(Collectors.joining());

	public CompilerOutput compile(List<String> input) {
		cache.resetPrioritization();
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
			StringBuilder current = new StringBuilder();
			for (Chars c : opt.get()) {
				if (c == null) {
					break;
				}
				current.append(c.getCharacter());
			}
			result.append(current.toString());
			int targetId = opcodes.lookupSymbol(current.toString()).orElse(0);
			Optional<Consumer<Stack>> executor = Optional.empty();
			for (int id : opcodes.getFlags(OpcodeMarker.PRIORITIZATION)) {
				if (targetId == id) {
					executor = Optional.of(opcodes.getExecutor(id));
				}
			}
			if (executor.isPresent()) {
				executor.get().accept(null);
			}
			return Optional.empty();
		}
		if (ins.startsWith("load ")) {
			String load = ins.substring(5);
			if (load.length() == 0) {
				return Optional.of(output.create("Attempted to load a 0 length literal (probably an empty load instruction). Line: " + (index + 1)));
			}
			if (load.length() == 1) {
				if (codepage.contains(load)) {
					result.append(opcodes.getChars(OpcodeMarker.LITERAL1).getCharacter());
					result.append(Chars.fromInt(~Chars.fromCode(load).getId() & 0xff).getCharacter());
					return Optional.empty();
				}
			}
			if (load.length() == 2) {
				boolean canInclude = true;
				for (char c : load.toCharArray()) {
					if (!codepage.contains(String.valueOf(c))) {
						canInclude = false;
						break;
					}
				}
				if (canInclude) {
					result.append(opcodes.getChars(OpcodeMarker.LITERAL2).getCharacter());
					for (char c : load.toCharArray()) {
						result.append(Chars.fromInt(~Chars.fromCode(String.valueOf(c)).getId() & 0xff).getCharacter());
					}
					return Optional.empty();
				}
			}
			List<String> attempts = new ArrayList<>();
			if (base.canCompress(load)) {
				byte[] compressed = base.compress(load);
				if (compressed.length < load.length() && compressed.length <= 256 && compressed.length > 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(opcodes.getChars(OpcodeMarker.COMPRESSION1).getCharacter());
					sb.append(Chars.fromInt(compressed.length - 1).getCharacter());
					for (byte b : compressed) {
						sb.append(Chars.fromByte(b).getCharacter());
					}
					attempts.add(sb.toString());
				}
			}
			if (shoco.canCompress(load)) {
				byte[] compressed = shoco.compress(load);
				if (compressed.length <= load.length() && compressed.length <= 256 && compressed.length > 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(opcodes.getChars(OpcodeMarker.SPECIAL).getCharacter());
					sb.append(Chars.fromInt(compressed.length - 1).getCharacter());
					for (byte b : compressed) {
						sb.append(Chars.fromByte(b).getCharacter());
					}
					attempts.add(sb.toString());
				}
			}
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
					if (smaz.canCompress(lower)) {
						byte[] compressed = smaz.compress(lower);
						if (compressed.length <= load.length() && compressed.length <= 256 && compressed.length > 0) {
							StringBuilder sb = new StringBuilder();
							sb.append(opcodes.getChars(OpcodeMarker.COMPRESSION3).getCharacter());
							sb.append(Chars.fromInt(compressed.length - 1).getCharacter());
							for (byte b : compressed) {
								sb.append(Chars.fromByte(b).getCharacter());
							}
							attempts.add(sb.toString());
						}
					}
				}
			}
			if (smaz.canCompress(load)) {
				byte[] compressed = smaz.compress(load);
				if (compressed.length <= result.length() && compressed.length <= 256 && compressed.length > 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(opcodes.getChars(OpcodeMarker.COMPRESSION2).getCharacter());
					sb.append(Chars.fromInt(compressed.length - 1).getCharacter());
					for (byte b : compressed) {
						sb.append(Chars.fromByte(b).getCharacter());
					}
					attempts.add(sb.toString());
				}
			}
			if (lzma.canCompress(load)) {
				byte[] compressed = lzma.compress(load);
				if (compressed.length <= load.length() && compressed.length > 0) {
					StringBuilder sb = new StringBuilder();
					sb.append(opcodes.getChars(OpcodeMarker.LZMA).getCharacter());
					sb.append(Chars.fromInt(compressed.length - 1 >= 255 ? 255 : compressed.length - 1).getCharacter());
					if (compressed.length - 1 >= 255) {
						sb.append(Chars.fromInt(compressed.length - 256).getCharacter());
					}
					for (byte b : compressed) {
						sb.append(Chars.fromByte(b).getCharacter());
					}
					attempts.add(sb.toString());
				}
			}
			if (attempts.size() > 0) {
				result.append(Collections.min(attempts, new Comparator<String>() {
					@Override
					public int compare(String s1, String s2) {
						return s1.length() - s2.length();
					}
				}));
				return Optional.empty();
			}
			boolean mustShoco = false;
			for (char c : load.toCharArray()) {
				if (!codepage.contains(String.valueOf(c))) {
					mustShoco = true;
					break;
				}
			}
			if (load.contains(Chars.fromInt(~opcodes.getChars(OpcodeMarker.SPECIAL).getId() & 0xff).getCharacter()) || mustShoco) {
				if (shoco.canCompress(load)) {
					byte[] compressed = shoco.compress(load);
					if (compressed.length <= 256 && compressed.length > 0) {
						result.append(opcodes.getChars(OpcodeMarker.SPECIAL).getCharacter());
						result.append(Chars.fromInt(compressed.length - 1).getCharacter());
						for (byte b : compressed) {
							result.append(Chars.fromByte(b).getCharacter());
						}
						return Optional.empty();
					}
				}
				return Optional.of(output.create("Failed to process literal. Line: " + (index + 1)));
			}
			result.append(opcodes.getChars(OpcodeMarker.LITERAL).getCharacter());
			for (char c : load.toCharArray()) {
				result.append(Chars.fromInt(~Chars.fromCode(String.valueOf(c)).getId() & 0xff).getCharacter());
			}
			result.append(opcodes.getChars(OpcodeMarker.SPECIAL).getCharacter());
			return Optional.empty();
		}
		return Optional.of(output.create("Invalid instruction '" + ins + "' in source. Line: " + (index + 1)));
	}
}
