package co.q64.jstx.gwt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import co.q64.jstx.Jstx;
import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.gwt.inject.DaggerJstxComponent;
import co.q64.jstx.gwt.inject.JstxComponent;
import co.q64.jstx.gwt.ui.ace.AceEditor;
import co.q64.jstx.gwt.ui.ace.AceEditorTheme;
import co.q64.jstx.gwt.ui.resource.Resources;
import co.q64.jstx.gwt.ui.util.Base64;
import co.q64.jstx.gwt.ui.util.FlipTable;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.runtime.Output;

public class JstxGWT implements EntryPoint {
	private static final int buffer = 40;

	public void onModuleLoad() {
		Logger logger = Logger.getLogger("Jstx");
		JstxComponent component = DaggerJstxComponent.create();
		Jstx jstx = component.getJstx();
		String modeParam = Window.Location.getParameter("mode");
		Mode mode = modeParam == null ? Mode.RUN : modeParam.equals("dev") ? Mode.DEV : modeParam.equals("ref") ? Mode.REF : Mode.RUN;

		Resources res = GWT.create(Resources.class);
		res.style().ensureInjected();
		ScriptInjector.fromString(res.aceEditor().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();

		Window.setTitle("Jstx GWT");
		HTML forkMe = new HTML("<a href=\"https://github.com/Quantum64/Jstx\"><img style=\"position: absolute; top: 0; right: 0; border: 0;\" src=\"https://s3.amazonaws.com/github/ribbons/forkme_right_white_ffffff.png\" alt=\"Fork me on GitHub\"></a>");
		switch (mode) {
		case REF:
			break;
		default:
			RootPanel.get().add(forkMe);
			break;
		}

		Panel root = RootPanel.get();
		VerticalPanel main = new VerticalPanel();
		root.add(main);
		switch (mode) {
		case DEV: {
			HTML languageLabel = new HTML("<h1>Jstx Online Compiler (GWT)</h1>");
			HTML versionLabel = new HTML("<h3 class=\"title\">Version " + jstx.getVersion() + "</h3>");
			main.setWidth("100%");
			main.setHeight("100%");
			main.add(languageLabel);
			main.add(versionLabel);

			HorizontalPanel buttonsParent = new HorizontalPanel();
			HorizontalPanel buttons = new HorizontalPanel();
			Button runCodeButton = new Button();
			runCodeButton.setText("Run Code");
			Button switchToRunButton = new Button();
			switchToRunButton.setText("Interpret Mode");
			buttonsParent.setWidth("100%");
			buttonsParent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			buttons.setSpacing(10);
			buttons.add(runCodeButton);
			buttons.add(switchToRunButton);
			buttonsParent.add(buttons);
			main.add(buttonsParent);

			VerticalPanel code = new VerticalPanel();
			code.setWidth("100%");
			code.setHeight("10%");
			main.add(code);
			HTML codeLabel = new HTML("<span class=\"label\">Code</span>");
			code.add(codeLabel);

			AceEditor codeEditor = new AceEditor();
			codeEditor.setWidth((String.valueOf(Window.getClientWidth() - buffer)) + "px");
			codeEditor.setHeight(String.valueOf(Window.getClientHeight() / 3.7) + "px");
			code.add(codeEditor);
			codeEditor.startEditor();
			codeEditor.setShowPrintMargin(false);
			codeEditor.setTheme(AceEditorTheme.TWILIGHT);
			String codeParam = Window.Location.getParameter("code");
			if (codeParam != null) {
				try {
					codeEditor.setText(new String(Base64.fromBase64(codeParam), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.log(Level.SEVERE, "Failed to load code from URL!", e);
				}
			}

			VerticalPanel compiler = new VerticalPanel();
			main.add(compiler);
			HTML compilerLabel = new HTML("<span class=\"label\">Compiler Output</span>");
			compiler.add(compilerLabel);

			AceEditor compilerEditor = new AceEditor();
			compilerEditor.setWidth((String.valueOf(Window.getClientWidth() - buffer)) + "px");
			compilerEditor.setHeight(String.valueOf(Window.getClientHeight() / 3.7) + "px");
			compiler.add(compilerEditor);
			compilerEditor.startEditor();
			compilerEditor.setReadOnly(true);
			compilerEditor.setShowPrintMargin(false);
			compilerEditor.setTheme(AceEditorTheme.TWILIGHT);
			Runnable updateCompiler = () -> {
				CompilerOutput co = jstx.compileProgram(Arrays.asList(codeEditor.getText().split("\n")));
				compilerEditor.setText(co.getDisplayOutput().stream().collect(Collectors.joining("\n")));
				if (co.isSuccess()) {
					codeLabel.setHTML("<span class=\"label\">Code (" + co.getProgram().length() + " bytes)</span>");
				} else {
					codeLabel.setHTML("<span class=\"label\">Code</span>");
				}
			};
			updateCompiler.run();

			VerticalPanel output = new VerticalPanel();
			main.add(output);
			HTML outputLabel = new HTML("<span class=\"label\">Output</span>");
			output.add(outputLabel);

			AceEditor outputEditor = new AceEditor();
			outputEditor.setWidth((String.valueOf(Window.getClientWidth() - buffer)) + "px");
			outputEditor.setHeight(String.valueOf(Window.getClientHeight() / 5.7) + "px");
			output.add(outputEditor);
			outputEditor.startEditor();
			outputEditor.setReadOnly(true);
			outputEditor.setShowPrintMargin(false);
			outputEditor.setTheme(AceEditorTheme.TWILIGHT);

			runCodeButton.addClickHandler(event -> {
				outputEditor.setText("");
				CompilerOutput co = jstx.compileProgram(Arrays.asList(codeEditor.getText().split("\n")));
				if (co.isSuccess()) {
					jstx.runProgram(co.getProgram(), new String[0], new Output() {

						@Override
						public void println(String message) {
							outputEditor.setText(outputEditor.getText() + message + "\n");
						}

						@Override
						public void print(String message) {
							outputEditor.setText(outputEditor.getText() + message);
						}
					});
				} else {
					outputEditor.setText("Fatal: Could not run program due to compiler error!");
				}
			});

			codeEditor.addOnChangeHandler(event -> {
				try {
					updateUrl(Window.Location.createUrlBuilder().setParameter("code", Base64.toBase64(codeEditor.getText().getBytes("UTF-8"))).buildString());
				} catch (UnsupportedEncodingException e) {
					logger.log(Level.SEVERE, "Failed to push code to URL!", e);
				}
				updateCompiler.run();
			});

			switchToRunButton.addClickHandler(event -> {
				Window.Location.replace(Window.Location.createUrlBuilder().removeParameter("code").removeParameter("args").removeParameter("mode").buildString());
			});
			break;
		}
		case RUN: {
			HTML languageLabel = new HTML("<h1>Jstx Online Interpreter (GWT)</h1>");
			HTML versionLabel = new HTML("<h3 class=\"title\">Version " + jstx.getVersion() + "</h3>");
			main.setWidth("100%");
			main.setHeight("100%");
			main.add(languageLabel);
			main.add(versionLabel);

			HorizontalPanel buttonsParent = new HorizontalPanel();
			HorizontalPanel buttons = new HorizontalPanel();
			Button runCodeButton = new Button();
			runCodeButton.setText("Run Code");
			Button devModeButton = new Button();
			devModeButton.setText("Dev Mode");
			buttonsParent.setWidth("100%");
			buttonsParent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			buttons.setSpacing(10);
			buttons.add(runCodeButton);
			buttons.add(devModeButton);
			buttonsParent.add(buttons);
			main.add(buttonsParent);

			VerticalPanel code = new VerticalPanel();
			code.setWidth("100%");
			code.setHeight("10%");
			main.add(code);
			HTML codeLabel = new HTML("<span class=\"label\">Code</span>");
			code.add(codeLabel);

			AceEditor codeEditor = new AceEditor();
			codeEditor.setWidth((String.valueOf(Window.getClientWidth() - buffer)) + "px");
			codeEditor.setHeight(String.valueOf(Window.getClientHeight() / 10) + "px");
			code.add(codeEditor);
			codeEditor.startEditor();
			codeEditor.setShowPrintMargin(false);
			codeEditor.setTheme(AceEditorTheme.TWILIGHT);
			String codeParam = Window.Location.getParameter("code");
			if (codeParam != null) {
				try {
					codeEditor.setText(new String(Base64.fromBase64(codeParam), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.log(Level.SEVERE, "Failed to load code from URL!", e);
				}
			}
			Runnable updateCodeLabel = () -> codeLabel.setHTML("<span class=\"label\">Code (" + codeEditor.getText().replace("\n", "").length() + " bytes)</span>");
			updateCodeLabel.run();

			VerticalPanel arguments = new VerticalPanel();
			main.add(arguments);
			HTML argumentsLabel = new HTML("<span class=\"label\">Arguments</span>");
			arguments.add(argumentsLabel);

			AceEditor argumentsEditor = new AceEditor();
			argumentsEditor.setWidth((String.valueOf(Window.getClientWidth() - buffer)) + "px");
			argumentsEditor.setHeight(String.valueOf(Window.getClientHeight() / 10) + "px");
			arguments.add(argumentsEditor);
			argumentsEditor.startEditor();
			argumentsEditor.setShowPrintMargin(false);
			argumentsEditor.setTheme(AceEditorTheme.TWILIGHT);
			String argsParam = Window.Location.getParameter("args");
			if (argsParam != null) {
				try {
					argumentsEditor.setText(new String(Base64.fromBase64(argsParam), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.log(Level.SEVERE, "Failed to load code from URL!", e);
				}
			}

			VerticalPanel output = new VerticalPanel();
			main.add(output);
			HTML outputLabel = new HTML("<span class=\"label\">Output</span>");
			output.add(outputLabel);

			AceEditor outputEditor = new AceEditor();
			outputEditor.setWidth((String.valueOf(Window.getClientWidth() - buffer)) + "px");
			outputEditor.setHeight(String.valueOf(Window.getClientHeight() / 2.1) + "px");
			output.add(outputEditor);
			outputEditor.startEditor();
			outputEditor.setReadOnly(true);
			outputEditor.setShowPrintMargin(false);
			outputEditor.setTheme(AceEditorTheme.TWILIGHT);

			runCodeButton.addClickHandler(event -> {
				outputEditor.setText("");
				List<String> args = new ArrayList<>();
				StringBuilder currentArg = new StringBuilder();
				boolean inQuote = false;
				for (char c : argumentsEditor.getText().replace("\n", "").toCharArray()) {
					if (String.valueOf(c).equals("\"")) {
						inQuote = !inQuote;
						continue;
					}
					if (String.valueOf(c).equals(" ") && !inQuote) {
						args.add(currentArg.toString());
						currentArg.setLength(0);
						continue;
					}
					currentArg.append(c);
				}
				if (currentArg.length() > 0) {
					args.add(currentArg.toString());
				}
				jstx.runProgram(codeEditor.getText().replace("\n", ""), args.toArray(new String[0]), new Output() {

					@Override
					public void println(String message) {
						outputEditor.setText(outputEditor.getText() + message + "\n");
					}

					@Override
					public void print(String message) {
						outputEditor.setText(outputEditor.getText() + message);
					}
				});
			});

			codeEditor.addOnChangeHandler(event -> {
				updateCodeLabel.run();
				String text = codeEditor.getText().replace("\n", "");
				try {
					updateUrl(Window.Location.createUrlBuilder().setParameter("code", Base64.toBase64(text.getBytes("UTF-8"))).buildString());
				} catch (UnsupportedEncodingException e) {
					logger.log(Level.SEVERE, "Failed to push code to URL!", e);
				}
			});

			argumentsEditor.addOnChangeHandler(event -> {
				String text = argumentsEditor.getText().replace("\n", "");
				try {
					updateUrl(Window.Location.createUrlBuilder().setParameter("args", Base64.toBase64(text.getBytes("UTF-8"))).buildString());
				} catch (UnsupportedEncodingException e) {
					logger.log(Level.SEVERE, "Failed to push arguments to URL!", e);
				}
			});

			devModeButton.addClickHandler(event -> {
				Window.Location.replace(Window.Location.createUrlBuilder().removeParameter("code").removeParameter("args").setParameter("mode", "dev").buildString());
			});
			break;
		}
		case REF: {
			StyleInjector.inject("body {margin:0;}");
			AceEditor refBox = new AceEditor();
			refBox.setWidth((String.valueOf(Window.getClientWidth())) + "px");
			refBox.setHeight(String.valueOf(Window.getClientHeight()) + "px");
			main.add(refBox);
			refBox.startEditor();
			refBox.setReadOnly(true);
			refBox.setShowPrintMargin(false);
			refBox.setTheme(AceEditorTheme.TWILIGHT);
			StringBuilder ref = new StringBuilder();
			// Header
			ref.append("                                    ,----,               \n         ,---._                   ,/   .`|               \n       .-- -.' \\   .--.--.      ,`   .'  :,--,     ,--,  \n       |    |   : /  /    '.  ;    ;     /|'. \\   / .`|  \n       :    ;   ||  :  /`. /.'___,/    ,' ; \\ `\\ /' / ;  \n       :        |;  |  |--` |    :     |  `. \\  /  / .'  \n       |    :   :|  :  ;_   ;    |.';  ;   \\  \\/  / ./   \n       :          \\  \\    `.`----'  |  |    \\  \\.'  /    \n       |    ;   |  `----.   \\   '   :  ;     \\  ;  ;     \n   ___ l           __ \\  \\  |   |   |  '    / \\  \\  \\    \n /    /\\    J   : /  /`--'  /   '   :  |   ;  /\\  \\  \\   \n/  ../  `..-    ,'--'.     /    ;   |.'  ./__;  \\  ;  \\  \n\\    \\         ;   `--'---'     '---'    |   : / \\  \\  ; \n \\    \\      ,'                          ;   |/   \\  ' | \n  \"---....--'                            `---'     `--`  ");
			ref.append("\n  _____                                               _                _____ _    _ _     _      \n |  __ \\                                             (_)              / ____| |  | (_)   | |     \n | |__) | __ ___   __ _ _ __ __ _ _ __ ___  _ __ ___  _ _ __   __ _  | |  __| |  | |_  __| | ___ \n |  ___/ '__/ _ \\ / _` | '__/ _` | '_ ` _ \\| '_ ` _ \\| | '_ \\ / _` | | | |_ | |  | | |/ _` |/ _ \\\n | |   | | | (_) | (_| | | | (_| | | | | | | | | | | | | | | | (_| | | |__| | |__| | | (_| |  __/\n |_|   |_|  \\___/ \\__, |_|  \\__,_|_| |_| |_|_| |_| |_|_|_| |_|\\__, |  \\_____|\\____/|_|\\__,_|\\___|\n                   __/ |                                       __/ |                             \n                  |___/                                       |___/                              \n");
			// The Basics
			ref.append("  _____ _             ____            _           \n |_   _| |__   ___   | __ )  __ _ ___(_) ___ ___  \n   | | | '_ \\ / _ \\  |  _ \\ / _` / __| |/ __/ __| \n   | | | | | |  __/  | |_) | (_| \\__ \\ | (__\\__ \\ \n   |_| |_| |_|\\___|  |____/ \\__,_|___/_|\\___|___/ \n");
			ref.append(line("= Overview ="));
			ref.append(wrapLine("Welcome to Jstx (“Java Stax”), a golfing language inspired by Stax and written in pure Java by Quantum64. “Wait, what? Java? But this is a web based language...” For your convenience, I have used Google Web Toolkit to compile the Java based compiler and interpreter to JavaScript, as well create a nice user interface for compiling, running, and sharing your programs. The only feature currently missing from the web version is the real time debugger, which is only available in the command line version."));
			ref.append(line());
			ref.append(line("= The Compiler ="));
			ref.append(wrapLine("One difference between Jstx and most other widely used golfing languages is that Jstx is actually a compiled language. This means that you don’t need to worry about managing golfed code. The compiler does all the golfing for you. You will be writing all your programs in uncompiled Jstx only, as the compiled Jstx code can vary from version to version of the compiler and interpreter. In addition to compiling your code, the compiler also generates an annotated version of the compiled code for you to stick on your Code Golf Stack Exchange answer and sweep in that sweet sweet reputation for no additional work. Make sure you’re in “Dev Mode” when writing Jstx code so that your code is compiled correctly. “Interpret Mode” will only read compiled Jstx code. Note that while some code can be tested in development mode, code that requires command line arguments must be run from the interpreter."));
			ref.append(line());
			ref.append(line("= Programming ="));
			ref.append(wrapLine("Jstx is a stack based language, which means that the program's main memory is stored as a stack of values. Values in Jstx are untyped, so each opcode will attempt to convert its arguments to the appropriate type before execution. All the opcodes in Jstx will take arguments from the stack and add (hereon referred to as “push”) their results to the stack if applicable. Unless specifically stated in the opcode’s description, each opcode will remove (hereon referred to as “pop”) its arguments from the stack. Jstx also has 6 registers addressable with one byte and 256 registers addressable with two bytes. These registers allow values to be stored off of the stack. Jstx programs consist of one opcode per line, with additional blank lines or comments acceptable. A Jstx program will push all command line arguments onto the stack before execution begins. As a Jstx program exits normally, it will print the top value on the stack followed by a newline unless the program specifically instructs the interpreter not to."));
			ref.append(line());
			ref.append(line("= Comments ="));
			ref.append(wrapLine("Blank lines will be ignored by the compiler. All text after a # or ‘ symbol will be ignored by the compiler until the next line, unless these characters appear on the same line as a ‘load’ opcode."));
			ref.append(line());
			ref.append(line("= Functions ="));
			ref.append(wrapLine("Jstx supports functions complete with a call stack. Note that functions are relatively expensive in terms of byte count, so it’s better to try and write your program without them. Functions are declared with a ‘def function-name’ opcode, where function-name can be any string literal used to call your function. Function blocks are closed with a ‘return’ opcode, which will return program execution to where the function was called. Functions have no explicit arguments or return values, instead use the stack to pass information to functions and return values to the caller. A function is called with the ‘jump function-name’ opcode, where the function name is specified in a ‘def’ opcode."));
			ref.append(line("Here is an example of function usage."));
			ref.append(line());
			ref.append(line("load 1"));
			ref.append(line("load 2"));
			ref.append(line("jump sum   # Call the sum function"));
			ref.append(line("println    # Print the result of the sum function"));
			ref.append(line("terminate  # Terminate the program so the function doesn’t execute again"));
			ref.append(line());
			ref.append(line("def sum    # Define the sum function"));
			ref.append(line("+          # Sum the top two elements on the stack and push result"));
			ref.append(line("return     # Return execution to the caller"));
			ref.append(line());
			ref.append(wrapLine("Watch out for function declaration fallthrough! Function declarations are removed from compiled Jstx code in order to save bytes. This means that program execution call fall through a function declaration and cause the program to crash."));
			ref.append(line("Here is the same example without the ‘terminate’ opcode."));
			ref.append(line());
			ref.append(line("load 1"));
			ref.append(line("load 2"));
			ref.append(line("jump sum   # Call the sum function"));
			ref.append(line("println    # Print the result of the sum function"));
			ref.append(line("           # Program execution continues and falls through the next line, causing the sum function to be called for the second time in an illegal way"));
			ref.append(line("def sum"));
			ref.append(line("+          # The sum opcode is executed for the second time"));
			ref.append(line("return     # Upon the second execution of the return statement, the call stack underflows and the program crashes"));
			ref.append(line());
			ref.append(line("= Examples ="));
			ref.append(line("Here are a few examples to get you started with Jstx."));
			ref.append(line());
			ref.append(line("# Print integers 1 through 10 each followed by a newline"));
			ref.append(line("load 1"));
			ref.append(line("load 10"));
			ref.append(line("list.range"));
			ref.append(line("iterate stack"));
			ref.append(line("println"));
			ref.append(line("end"));
			ref.append(line("terminate"));
			ref.append(line());
			ref.append(line("I’ll add more examples eventually..."));
			ref.append(line());
			// Special opcodes
			ref.append("  ____                  _       _     ___                      _            \n / ___| _ __   ___  ___(_) __ _| |   / _ \\ _ __   ___ ___   __| | ___  ___  \n \\___ \\| '_ \\ / _ \\/ __| |/ _` | |  | | | | '_ \\ / __/ _ \\ / _` |/ _ \\/ __| \n  ___) | |_) |  __/ (__| | (_| | |  | |_| | |_) | (_| (_) | (_| |  __/\\__ \\ \n |____/| .__/ \\___|\\___|_|\\__,_|_|   \\___/| .__/ \\___\\___/ \\__,_|\\___||___/ \n       |_|                                |_|                              \n");
			String special = FlipTable.of(new String[] { "Opcode", "Description" }, new String[][] { { "load <literal>", "Push a literal value onto the stack." }, { "ldr <0x00 - 0xff>", "Push the value in this register." }, { "sdr <0x00 - 0xff>", "Store the top stack value in this register." } });
			ref.append(special);
			ref.append(line());
			ref.append(line());
			ref.append(line("= load ="));
			ref.append(wrapLine("The load opcode takes all data on the line past the load statement as a literal value to be loaded onto the stack during program execution. The compiler will look at the data in your load statement and determine the optimal way to compress it for lowest byte count. Jstx has many ways of compressing different types of literals and this is done completely automatically. Note that comments are not supported on load lines, as the comment will also be taken as part of the literal value to load onto the stack. Since values in Jstx are untyped, they are all loaded in the same way. For example booleans are loaded as ‘load true’, or ‘load false’, integers are loaded as ‘load 100’, string as ‘loas abc’, etc..."));
			ref.append(line());
			ref.append(line("= srd & ldr ="));
			ref.append(wrapLine("The set of 256 two byte addressable registers can be loaded from and saved to in the same way as the four one byte registers. For example ‘ldr 0x23’ loads register 0x23 and ‘sdr 0x23’ stores to register 0x23."));
			ref.append(line());
			// Opcode table
			ref.append("   ___                      _            \n  / _ \\ _ __   ___ ___   __| | ___  ___  \n | | | | '_ \\ / __/ _ \\ / _` |/ _ \\/ __| \n | |_| | |_) | (_| (_) | (_| |  __/\\__ \\ \n  \\___/| .__/ \\___\\___/ \\__,_|\\___||___/ \n       |_|                              \n");
			ref.append(line("I just started on this language a few weeks ago so there are hundreds if not thousands of opcodes yet to come."));
			Opcodes opcodes = component.getOpcodes();
			Map<String, String> descriptions = new HashMap<>();
			for (String s : opcodes.getNames()) {
				if (s.startsWith("load") || s.startsWith("UNUSED") || s.startsWith("sdr 0x") || s.startsWith("ldr 0x")) {
					continue;
				}
				descriptions.put(s, opcodes.getDescription(s));
			}
			List<String[]> tableData = new ArrayList<>();
			for (Entry<String, String> e : descriptions.entrySet()) {
				tableData.add(new String[] { e.getKey(), e.getValue() });
			}
			String table = FlipTable.of(new String[] { "Opcode", "Description" }, tableData.toArray(new String[0][0]));
			ref.append(table);
			refBox.setText(ref.toString());
			break;
		}
		}
	}

	private static enum Mode {
		RUN, DEV, REF
	}

	private static String wrapLine(String line) {
		int lineLength = 100;
		String linebreak = "\n";
		if (line.length() == 0)
			return linebreak;
		if (line.length() <= lineLength)
			return line + linebreak;
		String[] words = line.split(" ");
		StringBuilder allLines = new StringBuilder();
		StringBuilder trimmedLine = new StringBuilder();
		for (String word : words) {
			if (trimmedLine.length() + 1 + word.length() <= lineLength) {
				trimmedLine.append(word).append(" ");
			} else {
				allLines.append(trimmedLine).append(linebreak);
				trimmedLine = new StringBuilder();
				trimmedLine.append(word).append(" ");
			}
		}
		if (trimmedLine.length() > 0) {
			allLines.append(trimmedLine);
		}
		allLines.append(linebreak);
		return allLines.toString() + "\n";
	}

	private static String line(String line) {
		return line + "\n";
	}

	private static String line() {
		return line("");
	}

	private static native void updateUrl(String newUrl)
	/*-{
	$wnd.history.pushState(newUrl, "", newUrl);
	}-*/;
}
