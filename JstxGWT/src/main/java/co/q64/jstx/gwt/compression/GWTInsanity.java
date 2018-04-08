package co.q64.jstx.gwt.compression;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;

import co.q64.jstx.compression.Insanity;
import co.q64.jstx.gwt.ui.resource.Resources;

@Singleton
public class GWTInsanity implements Insanity {
	private Resources res = GWT.create(Resources.class);
	private int[] codepage = codePoints(res.insanity().getText()).toArray();

	protected @Inject GWTInsanity() {}

	@Override
	public int[] getCodepage() {
		return codepage;
	}
}
