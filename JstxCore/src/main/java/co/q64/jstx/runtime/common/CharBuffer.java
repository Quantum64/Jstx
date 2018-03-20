package co.q64.jstx.runtime.common;

public class CharBuffer {
	private char[] internal;
	private int pointer;
	private int mark;

	public CharBuffer(char[] chars) {
		this.internal = chars;
	}

	public CharBuffer(String chars) {
		this.internal = chars.toCharArray();
	}

	public char[] array() {
		return internal;
	}

	public char get(int pointer) {
		return internal[pointer];
	}

	public void mark() {
		mark = pointer;
	}

	public void reset() {
		pointer = mark;
	}

	public char get() {
		pointer++;
		return internal[pointer - 1];
	}

	public int remaining() {
		return internal.length - pointer;
	}

	public void position(int p) {
		pointer = p;
	}

	public int position() {
		return pointer;
	}

	public int length() {
		return internal.length;
	}

	public CharSequence subSequence(int a, int b) {
		StringBuilder result = new StringBuilder();
		for (int i = pointer + a; i < pointer + b; i++) {
			result.append(internal[i]);
		}
		return result.toString();
	}
}
