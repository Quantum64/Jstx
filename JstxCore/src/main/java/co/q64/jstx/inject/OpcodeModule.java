package co.q64.jstx.inject;

import co.q64.jstx.opcode.OpcodeRegistry;
import co.q64.jstx.opcode.x00.BasicOpcodes;
import co.q64.jstx.opcode.x00.ConditionalOpcodes;
import co.q64.jstx.opcode.x00.StandardOpcodes;
import co.q64.jstx.opcode.x38.MathOpcodes;
import co.q64.jstx.opcode.x39.StringOpcodes;
import co.q64.jstx.opcode.x3a.ListOpcodes;
import co.q64.jstx.opcode.x3b.StackOpcodes;
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
	@Binds @IntoSet OpcodeRegistry bindMath(MathOpcodes so);
	@Binds @IntoSet OpcodeRegistry bindList(ListOpcodes so);
	@Binds @IntoSet OpcodeRegistry bindStack(StackOpcodes so);
	// @formatter:on
}
