package co.q64.jstx.inject;

import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.x00.StandardOpcodes;
import dagger.Binds;
import dagger.multibindings.IntoSet;

public interface OpcodeModule {
	// @formatter:off
	@Binds @IntoSet OpcodeRegistry bindStandard(StandardOpcodes so);
	// @formatter:on
}
