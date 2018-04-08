package co.q64.jstx.runtime;

import co.q64.jstx.lang.value.Value;
import co.q64.jstx.util.Color;

public interface Graphics {
	public Value createImage(int x, int y, Color color);

	public Value createImage(String encoded);

	public void setColor(Value image, Color color);

	public void setStroke(Value image, int stroke);

	public void drawEllipse(Value image, int x, int y, int i, int j, boolean fill);

	public void drawRectangle(Value image, int x, int y, int w, int h, boolean fill);

	public void drawText(Value image, String text, int x, int y, boolean fill);

	public int getWidth(Value image);

	public int getHeight(Value image);
}
