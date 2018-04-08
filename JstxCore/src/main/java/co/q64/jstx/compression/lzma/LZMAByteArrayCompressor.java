
package co.q64.jstx.compression.lzma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.auto.factory.AutoFactory;

/**
 * LZMA compressor for {@code byte[]} arrays.
 */
@AutoFactory
public class LZMAByteArrayCompressor extends LZMACompressor {

	private final ByteArrayOutputStream output;

	/**
	 * Construct an encoder using the {@link #DEFAULT_COMPRESSION_MODE default compression mode}.
	 *
	 * <p>
	 * This is a convenience constructor, equivalent to:
	 * <blockquote>
	 * <code>LZMACompressor(data, DEFAULT_COMPRESSION_MODE)</code>
	 * </blockquote>
	 */
	protected LZMAByteArrayCompressor(byte[] data) {
		this(data, DEFAULT_COMPRESSION_MODE);
	}

	/**
	 * Primary constructor.
	 *
	 * @param data uncompressed data
	 * @param mode compression mode
	 * @throws IllegalArgumentException if {@code mode} is null
	 */
	protected LZMAByteArrayCompressor(byte[] data, CompressionMode mode) {
		this.output = new ByteArrayOutputStream();
		try {
			init(new ByteArrayInputStream(data), this.output, data.length, mode);
		} catch (IOException e) {
			throw new RuntimeException("impossible exception");
		}
	}

	/**
	 * Get the compressed data.
	 */
	public byte[] getCompressedData() {
		return this.output.toByteArray();
	}
}
