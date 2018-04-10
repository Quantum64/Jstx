package co.q64.jstx.factory;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.value.LiteralFactory;
import lombok.Getter;

@Singleton
public class LiteralFactoryFactory {
	protected @Inject LiteralFactoryFactory() {}

	protected @Inject @Getter LiteralFactory factory;
}
