package co.q64.jstx.inject;

import co.q64.jstx.version.VersionModule;
import dagger.Module;

@Module(includes = { VersionModule.class })
public interface StandardModule {
	// @formatter:off
	// @formatter:on
}
