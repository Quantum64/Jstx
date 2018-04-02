package co.q64.jstx.gwt.lang.value;

import com.google.auto.factory.AutoFactory;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

import co.q64.jstx.lang.value.Null;
import co.q64.jstx.util.Color;
import lombok.Getter;

@AutoFactory
public class GWTImage extends Null {
	private @Getter Canvas canvas;

	protected GWTImage(int w, int h) {
		this.canvas = Canvas.createIfSupported();
		canvas.setPixelSize(w, h);
		canvas.setSize(w + "px", h + "px");
	}

	@Override
	public String toString() {
		return canvas.toDataUrl();
	}

	public void setColor(Color color) {
		context().setFillStyle(CssColor.make(color.getR(), color.getG(), color.getB()));
		context().setStrokeStyle(CssColor.make(color.getR(), color.getG(), color.getB()));
	}

	public void setStroke(int stroke) {
		context().setLineWidth(stroke);
	}

	public Context2d context() {
		return canvas.getContext2d();
	}
}
