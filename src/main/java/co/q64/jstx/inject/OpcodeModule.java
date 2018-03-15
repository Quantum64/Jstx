package co.q64.jstx.inject;

import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.x00.BasicOpcodes;
import co.q64.jstx.opcode.x00.ConditionalOpcodes;
import co.q64.jstx.opcode.x00.StandardOpcodes;
import co.q64.jstx.opcode.x39.StringOpcodes;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public interface OpcodeModule {
	// @formatter:off
	@Binds @IntoSet OpcodeRegistry bindStandard(StandardOpcodes so);
	@Binds @IntoSet OpcodeRegistry bindConditional(ConditionalOpcodes co);
	@Binds @IntoSet OpcodeRegistry bindBasic(BasicOpcodes bo);
	@Binds @IntoSet OpcodeRegistry bindString(StringOpcodes so);
	// @formatter:on
}
