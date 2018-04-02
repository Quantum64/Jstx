package co.q64.jstx.inject;

import co.q64.jstx.runtime.Graphics;
import co.q64.jstx.runtime.Output;
import co.q64.jstx.runtime.system.AWTGraphics;
import co.q64.jstx.runtime.system.SystemOutput;
import dagger.Binds;
import dagger.Module;

@Module
public interface SystemModule {
	// @formatter:off
	@Binds Output bindOutput(SystemOutput output);
	@Binds Graphics bindGraphics(AWTGraphics graphics);
	// @formatter:on
}
