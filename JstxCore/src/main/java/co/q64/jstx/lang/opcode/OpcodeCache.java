package co.q64.jstx.lang.opcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.Stack;
import lombok.Getter;

@Getter
@Singleton
public class OpcodeCache {
	private int mapped = 0;
	private List<Opcode> literalOpcodes = new ArrayList<>();
	private Map<Integer, Consumer<Stack>> opcodes = new HashMap<>();
	private Map<String, Integer> names = new LinkedHashMap<>();
	private Map<String, String> descriptions = new HashMap<>();
	private Map<OpcodeMarker, List<Integer>> markers = new HashMap<OpcodeMarker, List<Integer>>();
	private List<OpcodeMarker> prioritization = new ArrayList<>();
	private boolean secondaryPrioritization = false;

	protected @Inject OpcodeCache() {}

	protected void init(List<Opcode> readOnlyOpcodes) {
		literalOpcodes = readOnlyOpcodes;
	}

	protected void remapOpcodes(List<OpcodeMarker> priority) {
		List<Opcode> codes = new ArrayList<>(literalOpcodes);
		mapped = 0;
		opcodes.clear();
		names.clear();
		descriptions.clear();
		markers.clear();
		for (Iterator<Opcode> itr = codes.iterator(); itr.hasNext();) {
			Opcode op = itr.next();
			if (op.getMarkers().contains(OpcodeMarker.STANDARD)) {
				register(op);
				itr.remove();
			}
		}
		for (OpcodeMarker om : priority) {
			for (Iterator<Opcode> itr = codes.iterator(); itr.hasNext();) {
				Opcode op = itr.next();
				if (op.getMarkers().contains(om)) {
					register(op);
					itr.remove();
				}
			}
		}
		for (Opcode oc : codes) {
			register(oc);
		}
	}

	private void register(Opcode opcode) {
		if (names.containsKey(opcode.getName())) {
			return;
		}
		names.put(opcode.getName(), mapped);
		opcodes.put(mapped, opcode.getExecutor());

		if (opcode.getDescription().isPresent()) {
			descriptions.put(opcode.getName(), opcode.getDescription().get());
		}
		for (OpcodeMarker marker : opcode.getMarkers()) {
			List<Integer> flags = markers.get(marker);
			if (flags == null) {
				flags = new ArrayList<>();
				markers.put(marker, flags);
			}
			flags.add(mapped);
		}
		mapped++;
	}

	public void prioritize(OpcodeMarker marker) {
		if (!secondaryPrioritization) {
			prioritization.clear();
		}
		secondaryPrioritization = false;
		prioritization.add(marker);
		remapOpcodes(prioritization);
	}

	public void resetPrioritization() {
		if (prioritization.size() > 0) {
			prioritization.clear();
			remapOpcodes(prioritization);
		}
	}

	public void secondaryPrioritization() {
		this.secondaryPrioritization = true;
	}
}
