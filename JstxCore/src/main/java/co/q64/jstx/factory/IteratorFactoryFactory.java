package co.q64.jstx.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.IteratorFactory;

@Singleton
public class IteratorFactoryFactory {
	protected @Inject IteratorFactoryFactory() {}

	protected @Inject IteratorFactory factory;

	public IteratorFactory getFactory() {
		return factory;
	}
}
