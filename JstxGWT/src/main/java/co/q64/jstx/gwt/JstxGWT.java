package co.q64.jstx.gwt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import co.q64.jstx.DaggerJstxComponent;
import co.q64.jstx.Jstx;
import co.q64.jstx.JstxComponent;
import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.gwt.ace.AceEditor;
import co.q64.jstx.gwt.ace.AceEditorTheme;
import co.q64.jstx.gwt.util.Base64;
import co.q64.jstx.runtime.Output;

public class JstxGWT implements EntryPoint {
	private static final int buffer = 40;

	public void onModuleLoad() {
		Logger logger = Logger.getLogger("Jstx");
		JstxComponent component = DaggerJstxComponent.builder().build();
		Jstx jstx = component.getJstx();
		String modeParam = Window.Location.getParameter("mode");
		boolean dev = modeParam != null && modeParam.equals("dev");

		Panel root = RootPanel.get();
		VerticalPanel main = new VerticalPanel();
		root.add(main);
		if (dev) {
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
			codeEditor.setHeight(String.valueOf(Window.getClientHeight() / 3.5) + "px");
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
			compilerEditor.setHeight(String.valueOf(Window.getClientHeight() / 3.5) + "px");
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
			outputEditor.setHeight(String.valueOf(Window.getClientHeight() / 3.7) + "px");
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
		} else {
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
			outputEditor.setHeight(String.valueOf(Window.getClientHeight() / 1.7) + "px");
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
		}
	}

	private static native void updateUrl(String newUrl)
	/*-{
	$wnd.history.pushState(newUrl, "", newUrl);
	}-*/;
}
