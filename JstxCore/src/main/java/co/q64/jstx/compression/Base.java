package co.q64.jstx.compression;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Base {
	private static final String codebook = "0123456789";

	protected @Inject Base() {}

	public boolean canCompress(String string) {
		for (char c : string.toCharArray()) {
			if (codebook.indexOf(String.valueOf(c)) == -1) {
				return false;
			}
		}
		return true;
	}

	public byte[] compress(String string) {
		return new BigInteger(string).toByteArray();
	}

	public String decompress(byte[] bytes) {
		return new BigInteger(bytes).toString();
	}
}
