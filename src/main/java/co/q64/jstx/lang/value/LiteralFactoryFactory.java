package co.q64.jstx.lang.value;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;

@Singleton
public class LiteralFactoryFactory {
	protected @Inject LiteralFactoryFactory() {}
	protected @Inject @Getter LiteralFactory factory;
}
