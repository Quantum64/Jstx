package co.q64.jstx.opcode;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeMarker;
import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.runtime.Graphics;
import co.q64.jstx.util.ColorFactory;

@Singleton
public class GraphicsOpcodes extends OpcodeRegistry {
	protected @Inject Graphics graphics;
	protected @Inject ColorFactory colors;

	protected @Inject GraphicsOpcodes() {
		super(OpcodeMarker.GRAPHICS);
	}

	@Override
	public void register() {
		r("graphics.image", stack -> stack.push(graphics.createImage(100, 100, colors.create(255, 255, 255))), "Push a 100x100 image filled with white pixels.");
		r("graphics.imageWithColor", stack -> stack.push(graphics.createImage(100, 100, colors.create(stack.pop().toString()))), "Push a 100x100 image filled with pixels colored the first stack value.");
		r("graphics.imageWithSize", stack -> stack.push(graphics.createImage(stack.peek(2).asInt(), stack.pull(2).asInt(), colors.create(255, 255, 255))), "Push an image filled with white pixels with a width of the second stack value and a height of the first stack value.");
		r("graphics.imageWithSquareSize", stack -> stack.push(graphics.createImage(stack.peek().asInt(), stack.pop().asInt(), colors.create(255, 255, 255))), "Push an image filled with white pixels with a width and height of the first stack value.");
		r("graphics.imageWithSizeAndColor", stack -> stack.push(graphics.createImage(stack.peek(3).asInt(), stack.peek(2).asInt(), colors.create(stack.pull(3).toString()))), "Push an image color filled with the first stack value with a width of the third stack value and a height of the second stack value.");
		r("graphics.imageWithSquareSizeAndColor", stack -> stack.push(graphics.createImage(stack.peek(2).asInt(), stack.peek(2).asInt(), colors.create(stack.pull(2).toString()))), "Push an image color filled with the first stack value with a width and height of the second stack value.");
		r("gramhics.imageFromUrl", stack -> stack.push(graphics.createImage(stack.pop().toString())), "Push an image from the Base64 image URL on the first stack value.");
		r("graphics.setColor", stack -> graphics.setColor(stack.peek(2), colors.create(stack.pop().toString())), "Set drawing color of the image on the second stack value to the color hex code on the first stack value.");
		r("graphics.setStroke", stack -> graphics.setStroke(stack.peek(2), stack.pop().asInt()), "Set drawing stroke of the image on the second stack value to the width on the first stack value.");
		r("graphics.drawText", stack -> graphics.drawText(stack.peek(2), stack.pop().toString(), 0, 10, false), "Draw the first stack value as a string to the second stack value at 0, 10.");
		r("graphics.drawTextAt", stack -> graphics.drawText(stack.peek(4), stack.peek(3).toString(), stack.peek(2).asInt(), stack.pull(3).asInt(), false), "Draw the third stack value as a string to the fourth stack value at the first stack value, the second stack value.");
		r("graphics.fillText", stack -> graphics.drawText(stack.peek(2), stack.pop().toString(), 0, 10, true), "Fill the first stack value as a string to the second stack value at 0, 10.");
		r("graphics.fillTextAt", stack -> graphics.drawText(stack.peek(4), stack.peek(3).toString(), stack.peek(2).asInt(), stack.pull(3).asInt(), true), "Fill the third stack value as a string to the fourth stack value at the first stack value, the second stack value.");
		r("graphics.drawCircle", stack -> graphics.drawEllipse(stack.peek(), 0, 0, 50, 50, false), "Draw a circle on the image on the first stack value with a radius of 50 pixels at 0, 0.");
		r("graphics.drawCircleWithRadius", stack -> graphics.drawEllipse(stack.peek(2), 0, 0, stack.peek().asInt(), stack.pop().asInt(), false), "Draw a circle on the image on the second stack value with a radius of the first stack value pixels at 0, 0.");
		r("graphics.drawCircleAt", stack -> graphics.drawEllipse(stack.peek(3), stack.peek(2).asInt(), stack.pull(2).asInt(), 50, 50, false), "Draw a circle on the image on the third stack value with a radius of 50 pixels at the second stack value, the first stack value.");
		r("graphics.drawCircleWithRadiusAt", stack -> graphics.drawEllipse(stack.peek(4), stack.peek(2).asInt(), stack.peek().asInt(), stack.peek(3).asInt(), stack.pull(3).asInt(), false), "Draw a circle on the image on the fourth stack value with a radius of the third stack value pixels at the second stack value, the first stack value.");
		r("graphics.fillCircle", stack -> graphics.drawEllipse(stack.peek(), 0, 0, 50, 50, true), "Fill a circle on the image on the first stack value with a radius of 50 pixels at 0, 0.");
		r("graphics.fillCircleWithRadius", stack -> graphics.drawEllipse(stack.peek(2), 0, 0, stack.peek().asInt(), stack.pop().asInt(), true), "Fill a circle on the image on the second stack value with a radius of the first stack value pixels at 0, 0.");
		r("graphics.fillCircleAt", stack -> graphics.drawEllipse(stack.peek(3), stack.peek(2).asInt(), stack.pull(2).asInt(), 50, 50, true), "Fill a circle on the image on the third stack value with a radius of 50 pixels at the second stack value, the first stack value.");
		r("graphics.fillCircleWithRadiusAt", stack -> graphics.drawEllipse(stack.peek(4), stack.peek(2).asInt(), stack.peek().asInt(), stack.peek(3).asInt(), stack.pull(3).asInt(), true), "Fill a circle on the image on the fourth stack value with a radius of the third stack value pixels at the second stack value, the first stack value.");

		r("graphics.drawEllipse", stack -> graphics.drawEllipse(stack.peek(3), 0, 0, stack.peek(2).asInt(), stack.pull(2).asInt(), false), "Draw an ellipse on the image on the third stack value with a width of the second stack value and a height of the first stack value pixels at 0, 0.");
		r("graphics.drawEllipseAt", stack -> graphics.drawEllipse(stack.peek(5), stack.peek(2).asInt(), stack.peek().asInt(), stack.peek(4).asInt(), stack.peek(3).asInt(), stack.pop(4).asInt() == -1), "Draw an ellipse on the image on the fifth stack value with a width of the fourth stack value and a height of the third stack value at the second stack value, the first stack value.");
		r("graphics.fillEllipse", stack -> graphics.drawEllipse(stack.peek(3), 0, 0, stack.peek(2).asInt(), stack.pull(2).asInt(), true), "Fill an ellipse on the image on the first stack value with a radius of 50 pixels at 0, 0.");
		r("graphics.fillEllipseAt", stack -> graphics.drawEllipse(stack.peek(5), stack.peek(2).asInt(), stack.peek().asInt(), stack.peek(4).asInt(), stack.peek(3).asInt(), stack.pop(4).asInt() != -1), "Fill an ellipse on the image on the fifth stack value with a width of the fourth stack value and a height of the third stack value at the second stack value, the first stack value.");
	}
}
