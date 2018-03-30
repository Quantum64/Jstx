package co.q64.jstx.inject;

import co.q64.jstx.JstxInfo;
import co.q64.jstx.annotation.Constants.Author;
import co.q64.jstx.annotation.Constants.Name;
import co.q64.jstx.version.VersionModule;
import dagger.Module;
import dagger.Provides;

@Module(includes = { VersionModule.class })
public interface StandardModule {
	// @formatter:off
	static @Provides @Name String provideName() { return JstxInfo.name; }
	static @Provides @Author String provideAuthor() { return JstxInfo.author; }
	// @formatter:on
}
