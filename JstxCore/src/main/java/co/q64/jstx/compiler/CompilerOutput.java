package co.q64.jstx.compiler;

import java.util.ArrayList;
import java.util.List;

import com.google.auto.factory.AutoFactory;

import lombok.Getter;

@AutoFactory
public class CompilerOutput {
	private @Getter boolean success;
	private @Getter String error, program;
	private List<String> compiledLines, instructionLines;

	protected CompilerOutput(String error) {
		this.error = error;
		this.success = false;
	}

	protected CompilerOutput(String program, List<String> compiledLines, List<String> instructionLines) {
		this.program = program;
		this.compiledLines = compiledLines;
		this.instructionLines = instructionLines;
		this.success = true;
	}

	public List<String> getDisplayOutput() {
		List<String> result = new ArrayList<>();
		if (success) {
			int offsetLength = 0;
			for (String s : instructionLines) {
				if (s.length() > offsetLength) {
					offsetLength = s.length();
				}
			}
			result.add(program);
			result.add(new String());
			result.add("Size: " + program.length() + " bytes");
			result.add("Instructions: " + instructionLines.size());
			result.add(new String());
			for (int i = 0; i < compiledLines.size(); i++) {
				if (instructionLines.size() <= i) {
					continue;
				}
				String instruction = instructionLines.get(i);
				String offset = new String();
				for (int u = 0; u < offsetLength - instruction.length(); u++) {
					offset += " ";
				}
				String compiled = compiledLines.get(i);
				if (compiled.equals(" ")) {
					compiled = "<whitespace character>";
				}
				result.add((i + 1) + ": " + instruction + offset + " => " + compiled);
			}
		} else {
			result.add(error);
		}
		return result;
	}
}
