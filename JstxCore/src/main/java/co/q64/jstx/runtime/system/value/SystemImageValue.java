package co.q64.jstx.runtime.system.value;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.common.annotations.GwtIncompatible;

import co.q64.jstx.annotation.GWT;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@GwtIncompatible(GWT.MESSAGE)
public class SystemImageValue {
	private Graphics2D graphics;
	private BufferedImage image;

	public SystemImageValue(int w, int h) {
		this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}

	public Graphics2D graphics() {
		return graphics;
	}

	@Override
	public String toString() {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "png", os);
			return "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
		} catch (final IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
	}
}
