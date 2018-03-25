package co.q64.jstx.util;

import co.q64.jstx.runtime.Output;

public class OutputBuffer implements Output {
	private StringBuilder buffer = new StringBuilder();

	@Override
	public void print(String message) {
		buffer.append(message);
	}

	@Override
	public void println(String message) {
		buffer.append(message + "\n");
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

	public String firstLine() {
		if (buffer.indexOf("\n") > -1) {
			return buffer.substring(0, buffer.indexOf("\n"));
		}
		return buffer.toString();
	}
}
