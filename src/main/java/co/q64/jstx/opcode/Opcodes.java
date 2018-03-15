package co.q64.jstx.opcode;

import java.util.function.Consumer;

import javax.inject.Singleton;

@Singleton
public class Opcodes implements Consumer<Opcode> {
	// 0 - 55 standard opcodes
	// 56 - 154 two character
	// 155 - 255 three character
	
	@Override
	public void accept(Opcode t) {
		// TODO Auto-generated method stub
		
	}

}
