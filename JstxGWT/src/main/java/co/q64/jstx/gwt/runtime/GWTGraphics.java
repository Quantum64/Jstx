package co.q64.jstx.gwt.runtime;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.canvas.dom.client.Context2d;

import co.q64.jstx.gwt.lang.value.GWTImage;
import co.q64.jstx.gwt.lang.value.GWTImageFactory;
import co.q64.jstx.lang.value.Value;
import co.q64.jstx.runtime.Graphics;
import co.q64.jstx.util.Color;
import co.q64.jstx.util.ColorFactory;

@Singleton
public class GWTGraphics implements Graphics {
	protected @Inject GWTImageFactory factory;
	protected @Inject ColorFactory colors;

	protected @Inject GWTGraphics() {}

	@Override
	public Value createImage(int x, int y, Color color) {
		GWTImage result = factory.create(x, y);
		setColor(result, color);
		drawRectangle(result, 0, 0, x, y, true);
		setColor(result, colors.create(0, 0, 0));
		return result;
	}

	@Override
	public void setColor(Value image, Color color) {
		image(image).setColor(color);
	}

	@Override
	public void setStroke(Value image, int stroke) {
		image(image).setStroke(stroke);
	}

	@Override
	public void drawEllipse(Value image, int x, int y, int i, int j, boolean fill) {
		Context2d context = image(image).context();
		context.save();
		context.beginPath();
		context.translate(x - i, y - j);
		context.scale(i, j);
		context.arc(1, 1, 1, 0, 2 * Math.PI, false);
		context.restore();
		context.stroke();
	}

	@Override
	public void drawRectangle(Value image, int x, int y, int w, int h, boolean fill) {
		if (fill) {
			image(image).context().fillRect(x, y, w, h);
		} else {
			image(image).context().rect(x, y, w, h);
		}
	}

	@Override
	public void drawText(Value image, String text, int x, int y, boolean fill) {
		if (fill) {
			image(image).context().fillText(text, x, y);
		} else {
			image(image).context().strokeText(text, x, y);
		}
	}

	@Override
	public int getWidth(Value image) {
		return image(image).getCanvas().getCoordinateSpaceHeight();
	}

	@Override
	public int getHeight(Value image) {
		return image(image).getCanvas().getCoordinateSpaceWidth();
	}

	private GWTImage image(Value value) {
		if (!(value instanceof GWTImage)) {
			throw new IllegalArgumentException("The provided value could not be interpreted as an image.");
		}
		return (GWTImage) value;
	}
}
