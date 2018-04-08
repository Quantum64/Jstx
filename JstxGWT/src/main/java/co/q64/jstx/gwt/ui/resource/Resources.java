package co.q64.jstx.gwt.ui.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {
	@Source("AceEditor.js")
	public TextResource aceEditor();

	@Source("Insanity.dat")
	public TextResource insanity();

	@Source("JstxStyle.css")
	@CssResource.NotStrict
	public CssResource style();
}
