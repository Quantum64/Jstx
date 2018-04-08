package co.q64.jstx.runtime.system.compression;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.compression.Insanity;

@Singleton
public class SystemInsanity implements Insanity {
	protected @Inject SystemInsanity() {}

	@Override
	public int[] getCodepage() {
		return new int[0];
	}
}
