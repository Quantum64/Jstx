package co.q64.jstx.runtime;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MockOutput implements Output {
	protected @Inject MockOutput() {}

	@Override
	public void print(String message) {}

	@Override
	public void println(String message) {}
}
