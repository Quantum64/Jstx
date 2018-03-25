package co.q64.jstx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.annotations.GwtIncompatible;

import co.q64.jstx.annotation.GWT;
import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.inject.StandardModule;
import co.q64.jstx.inject.SystemModule;
import co.q64.jstx.runtime.Output;
import dagger.Component;

@Singleton
@GwtIncompatible(GWT.MESSAGE)
public class JstxMain {
	protected @Inject Jstx jstx;
	protected @Inject Output output;

	private String[] args;

	private JstxMain(String[] args) {
		this.args = args;
	}

	public static void main(String[] args) {
		JstxMainComponent component = DaggerJstxMain_JstxMainComponent.create();
		component.inject(new JstxMain(args));
	}

	@Inject
	protected void start() {
		if (args.length == 0) {
			output.println("Provide a script filename as the first command line argument!");
			return;
		}
		String[] programArgs = new String[args.length - 1];
		System.arraycopy(args, 1, programArgs, 0, programArgs.length);
		try {
			CompilerOutput compiled = jstx.compileProgram(Files.readAllLines(new File(args[0]).toPath()));
			for (String s : compiled.getDisplayOutput()) {
				output.println(s);
			}
			if (compiled.isSuccess()) {
				output.println(new String());
				jstx.runProgram(compiled.getProgram(), programArgs, output);
			}
		} catch (FileNotFoundException e) {
			output.println("The file '" + args[0] + "' could not be found!");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			output.println("Failed to read script file!");
			return;
		}
	}

	@Singleton
	@GwtIncompatible(GWT.MESSAGE)
	@Component(modules = { SystemModule.class, StandardModule.class })
	protected static interface JstxMainComponent {
		public void inject(JstxMain main);
	}
}
