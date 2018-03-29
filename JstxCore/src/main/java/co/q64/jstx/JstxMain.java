package co.q64.jstx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.annotations.GwtIncompatible;

import co.q64.jstx.annotation.GWT;
import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.inject.StandardModule;
import co.q64.jstx.inject.SystemModule;
import co.q64.jstx.lang.Instruction;
import co.q64.jstx.lang.Program;
import co.q64.jstx.lang.ProgramFactory;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.lexer.Lexer;
import co.q64.jstx.runtime.Output;
import dagger.Component;

@Singleton
@GwtIncompatible(GWT.MESSAGE)
public class JstxMain {
	protected @Inject Jstx jstx;
	protected @Inject Output output;
	protected @Inject Lexer lexer;
	protected @Inject ProgramFactory programFactory;

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
		jstx.compileProgram(Arrays.asList("load Hello World!"));
		boolean debug = args.length > 0 && args[0].equalsIgnoreCase("debug");
		boolean dev = args.length > 0 && args[0].equalsIgnoreCase("dev");
		if (args.length == 0 || ((debug || dev) && args.length <= 1)) {
			output.println("Provide a script filename as the " + (debug || dev ? "next" : "first") + " command line argument!");
			return;
		}

		String[] programArgs = new String[args.length - (dev || debug ? 2 : 1)];
		if (programArgs.length > 0) {
			System.arraycopy(args, dev || debug ? 2 : 1, programArgs, 0, programArgs.length);
		}
		try {
			if (dev) {
				CompilerOutput compiled = jstx.compileProgram(Files.readAllLines(new File(args[0]).toPath()));
				for (String s : compiled.getDisplayOutput()) {
					output.println(s);
				}
				if (compiled.isSuccess()) {
					output.println("");
					jstx.runProgram(compiled.getProgram(), programArgs, output);
				}
			} else if (debug) {
				CompilerOutput compiled = jstx.compileProgram(Files.readAllLines(new File(args[1]).toPath()));
				for (String s : compiled.getDisplayOutput()) {
					output.println(s);
				}
				if (!compiled.isSuccess()) {
					return;
				}
				try (Scanner in = new Scanner(System.in)) {
					output.println("");
					output.println("Press any key to begin execution...");
					in.nextLine();
					List<Instruction> insns = lexer.parse(compiled.getProgram(), output);
					List<String> outputBuffer = new ArrayList<String>();
					Program program = programFactory.create(insns, programArgs, new Output() {

						@Override
						public void println(String message) {
							outputBuffer.add(message);
						}

						@Override
						public void print(String message) {
							if (outputBuffer.size() == 0) {
								println(message);
								return;
							}
							outputBuffer.set(outputBuffer.size() - 1, outputBuffer.get(outputBuffer.size() - 1) + message);
						}
					});
					program.execute(false);
					while (true) {
						boolean terminated = program.isTerminated() || program.getInstruction() >= insns.size();
						if (terminated) {
							if (program.isPrintOnTerminate()) {
								outputBuffer.add(program.getStack().pop().toString());
							}
						}
						for (int i = 0; i < 50; i++) {
							output.println("");
						}
						output.println("Program");
						for (int i = 0; i < insns.size(); i++) {
							output.print(i == program.getInstruction() ? " => " : " == ");
							output.println(insns.get(i).getInstruction());
						}
						output.println("");
						output.println("Stack");
						for (Value value : program.getStack().getStack()) {
							output.println(" " + value.toString());
						}
						output.println("");
						output.println("Output");
						for (String s : outputBuffer) {
							output.println(s);
						}
						output.println("");
						output.println("Press any key to continue execution...");
						in.nextLine();
						if (terminated) {
							break;
						}
						program.step();
					}
				}
			} else {
				jstx.runProgram(Files.readAllLines(new File(args[1]).toPath()).stream().collect(Collectors.joining()), programArgs, output);
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
