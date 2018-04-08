package co.q64.jstx.compression.lzma.impl;

public interface ICodeProgress {
	public void SetProgress(long inSize, long outSize);
}
