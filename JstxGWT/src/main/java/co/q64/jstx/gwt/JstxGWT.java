package co.q64.jstx.gwt;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;

import co.q64.jstx.DaggerJstxComponent;
import co.q64.jstx.Jstx;
import co.q64.jstx.JstxComponent;
import co.q64.jstx.Test;
import co.q64.jstx.runtime.Output;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JstxGWT implements EntryPoint {
	public void onModuleLoad() {
		Logger logger = Logger.getLogger("Jstx");
	    logger.log(Level.SEVERE, "Hello world!");
		JstxComponent component = DaggerJstxComponent.builder().build();
		Jstx jstx = component.getJstx();
		String program = jstx.compileProgram(Test.PROGRAM);
		 logger.log(Level.INFO, program);
		jstx.runProgram(program, new String[0], new Output() {

			@Override
			public void println(String message) {
				 logger.log(Level.INFO, message);
			}

			@Override
			public void print(String message) {
				 logger.log(Level.INFO, message);
			}
		});
	}
}
