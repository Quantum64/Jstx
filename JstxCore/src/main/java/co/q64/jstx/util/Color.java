package co.q64.jstx.util;

import com.google.auto.factory.AutoFactory;

import lombok.Getter;

@Getter
@AutoFactory
public class Color {
	private int r, g, b;
	private int color;

	protected Color(int color) {
		this.color = color;
		this.r = (color >> 16) & 0x0ff;
		this.g = (color >> 8) & 0x0ff;
		this.b = (color) & 0x0ff;
	}

	protected Color(int r, int g, int b) {
		this.color = ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
		this.r = r;
		this.g = g;
		this.b = b;
	}

	protected Color(String hex) {
		hex = hex.replace("#", "");
		if (hex.length() != 6) {
			throw new IllegalArgumentException("Invalid hex color code " + hex);
		}
		this.r = Integer.valueOf(hex.substring(0, 2), 16);
		this.g = Integer.valueOf(hex.substring(2, 4), 16);
		this.b = Integer.valueOf(hex.substring(4, 6), 16);
		this.color = ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
	}
}
