package co.q64.jstx;

import javax.inject.Singleton;

import co.q64.jstx.compiler.Compiler;
import co.q64.jstx.inject.OpcodeModule;
import co.q64.jstx.lexer.Lexer;
import dagger.Component;

@Singleton
@Component(modules = { OpcodeModule.class })
public interface JstxComponent {
	public Compiler getCompiler();

	public Lexer getLexer();
	
	public Jstx getJstx();
}
