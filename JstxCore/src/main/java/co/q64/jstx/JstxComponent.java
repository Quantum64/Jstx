package co.q64.jstx;

import javax.inject.Singleton;

import co.q64.jstx.compiler.Compiler;
import co.q64.jstx.inject.StandardModule;
import co.q64.jstx.lexer.Lexer;
import dagger.Component;

@Singleton
@Component(modules = { StandardModule.class })
public interface JstxComponent {
	public Compiler getCompiler();

	public Lexer getLexer();

	public Jstx getJstx();
}
