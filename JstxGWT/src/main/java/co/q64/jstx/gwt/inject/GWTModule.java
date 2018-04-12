package co.q64.jstx.gwt.inject;

import co.q64.jstx.gwt.runtime.GWTGraphics;
import co.q64.jstx.gwt.runtime.GWTJavascript;
import co.q64.jstx.runtime.Graphics;
import co.q64.jstx.runtime.Javascript;
import dagger.Binds;
import dagger.Module;

@Module
public interface GWTModule {
	// @formatter:off
	@Binds Graphics bindGraphics(GWTGraphics graphics);
	@Binds Javascript bindJavascript(GWTJavascript javascript);
	// @formatter:on
}
