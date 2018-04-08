package co.q64.jstx.runtime.common;

public class ByteBuffer {
	private byte[] internal;
	private int pointer;

	public ByteBuffer(int size) {
		this.internal = new byte[size];
		this.pointer = 0;
	}

	public void put(byte b) {
		internal[pointer] = b;
		pointer++;
	}

	public byte[] array() {
		return internal;
	}
	
	public int pointer() {
		return pointer;
	}
}
