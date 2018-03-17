package co.q64.jstx.runtime;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SystemOutput implements Output {
	protected @Inject SystemOutput() {}

	@Override
	public void print(String message) {
		System.out.print(message);
	}

	@Override
	public void println(String message) {
		System.out.println(message);
	}
}
