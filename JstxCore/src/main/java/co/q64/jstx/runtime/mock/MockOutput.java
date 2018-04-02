package co.q64.jstx.runtime.mock;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.runtime.Output;

@Singleton
public class MockOutput implements Output {
	protected @Inject MockOutput() {}

	@Override
	public void print(String message) {}

	@Override
	public void println(String message) {}
}
