package co.q64.jstx.lang.opcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.Stack;

// 0 - 156 = 1 byte
// 157 - 24990 = 2 byte

@Singleton
public class Opcodes {
	protected @Inject Opcodes() {}

	protected @Inject OpcodeLibrary library;

	private int index = 0;
	private Map<Integer, Consumer<Stack>> opcodes = new HashMap<>();
	private Map<String, Integer> names = new LinkedHashMap<>();
	private Map<String, String> descriptions = new HashMap<>();
	private Map<OpcodeMarker, List<Integer>> markers = new HashMap<OpcodeMarker, List<Integer>>();

	@Inject
	protected void init() {
		library.get().forEach(registry -> registry.register(this));
	}

	public void reg(String name, Consumer<Stack> exec) {
		reg(name, exec, null);
	}

	public void reg(String name, OpcodeMarker marker, Consumer<Stack> exec) {
		reg(name, marker, exec, null);
	}

	public void reg(String name, Consumer<Stack> exec, String description) {
		if (names.containsKey(name)) {
			return;
		}
		names.put(name, index);
		opcodes.put(index, exec);
		index++;
		if (description != null) {
			descriptions.put(name, description);
		}
	}

	public void reg(String name, OpcodeMarker marker, Consumer<Stack> exec, String description) {
		reg(name, exec);
		List<Integer> flags = markers.get(marker);
		if (flags == null) {
			flags = new ArrayList<>();
			markers.put(marker, flags);
		}
		flags.add(index - 1);
		if (description != null) {
			descriptions.put(name, description);
		}
	}

	public Consumer<Stack> getExecutor(int id) {
		return opcodes.get(id);
	}

	public Optional<String> getName(int id) {
		for (Entry<String, Integer> e : names.entrySet()) {
			if (e.getValue().equals(id)) {
				return Optional.of(e.getKey());
			}
		}
		return Optional.empty();
	}

	public int getFlag(OpcodeMarker marker) {
		return markers.get(marker).get(0);
	}

	public List<Integer> getFlags(OpcodeMarker marker) {
		return markers.get(marker);
	}

	public Chars getChars(OpcodeMarker marker) {
		return lookupChars(getFlag(marker))[0];
	}

	public String getDescription(String name) {
		return Optional.ofNullable(descriptions.get(name)).orElse("No description available.");
	}

	public List<String> getNames() {
		return new ArrayList<>(names.keySet());
	}

	public String getDebugInfo() {
		return "Using " + index + "/" + lookupId(new Chars[] { Chars.xff, Chars.xff }).get() + " opcodes (" + (lookupId(new Chars[] { Chars.xff, Chars.xff }).get() - index) + " remaining)";
	}

	public Optional<List<Chars>> lookupName(String name) {
		Integer id = names.get(name);
		if (id == null) {
			return Optional.empty();
		}
		return Optional.of(Arrays.asList(lookupChars(id)));
	}

	public Optional<Integer> lookupSymbol(String symbol) {
		Chars[] chars = new Chars[2];
		if (symbol.length() > 0) {
			char[] ar = symbol.toCharArray();
			chars[0] = Chars.fromCode(String.valueOf(ar[0]));
			if (symbol.length() > 1) {
				chars[1] = Chars.fromCode(String.valueOf(ar[1]));
			}
		}
		return lookupId(chars);
	}

	private Chars[] lookupChars(int id) {
		Chars[] result = new Chars[2];
		if (id < 156) {
			result[0] = Chars.fromInt(id);
			return result;
		}
		result[0] = Chars.fromInt(((id - 156) / 256) + 156);
		result[1] = Chars.fromInt((id - 156) % 256);
		return result;
	}

	private Optional<Integer> lookupId(Chars[] chars) {
		if (chars[0].getId() < 156) {
			return Optional.of(chars[0].getId());
		}
		if (chars[1] == null) {
			return Optional.empty();
		}
		int result = ((chars[0].getId() - 156) * 256 + 156);
		result += chars[1].getId();
		return Optional.of(result);
	}
}
