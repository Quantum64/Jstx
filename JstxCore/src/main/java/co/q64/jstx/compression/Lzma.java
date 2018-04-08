package co.q64.jstx.compression;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compression.lzma.CompressionMode;
import co.q64.jstx.compression.lzma.LZMAByteArrayCompressorFactory;
import co.q64.jstx.compression.lzma.LZMAByteArrayDecompressorFactory;
import co.q64.jstx.compression.lzma.UTF8;

@Singleton
public class Lzma {
	protected @Inject LZMAByteArrayCompressorFactory compressor;
	protected @Inject LZMAByteArrayDecompressorFactory decompressor;

	protected @Inject Lzma() {}

	public boolean canCompress(String str) {
		return true;
	}

	public byte[] compress(String str) {
		return compressBytes(UTF8.encode(str));
	}

	public String decompress(byte[] data) {
		return UTF8.decode(decompressBytes(data));
	}

	private byte[] compressBytes(byte[] data) {
		return compressor.create(data, CompressionMode.MODE_4).getCompressedData();
	}

	private byte[] decompressBytes(byte[] data) {
		return decompressor.create(data).getUncompressedData();
	}
}
