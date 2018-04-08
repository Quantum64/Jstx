
package co.q64.jstx.compression.lzma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.auto.factory.AutoFactory;

/**
 * LZMA decompressor for {@code byte[]} arrays.
 */
@AutoFactory
public class LZMAByteArrayDecompressor extends LZMADecompressor {

	private final ByteArrayOutputStream output;

	/**
	 * Constructor.
	 *
	 * @param data compressed data
	 * @throws IOException if the compressed data is truncated or corrupted
	 */
	protected LZMAByteArrayDecompressor(byte[] data) {
		this.output = new ByteArrayOutputStream();
		try {
			init(new ByteArrayInputStream(data), this.output);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the uncompressed data.
	 */
	public byte[] getUncompressedData() {
		return this.output.toByteArray();
	}
}
