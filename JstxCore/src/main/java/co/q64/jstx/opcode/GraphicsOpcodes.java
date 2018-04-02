package co.q64.jstx.opcode;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.opcode.OpcodeRegistry;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.runtime.Graphics;
import co.q64.jstx.util.ColorFactory;

@Singleton
public class GraphicsOpcodes implements OpcodeRegistry {
	protected @Inject Graphics graphics;
	protected @Inject ColorFactory colors;

	protected @Inject GraphicsOpcodes() {}

	@Override
	public void register(Opcodes oc) {
		oc.reg("graphics.image", stack -> stack.push(graphics.createImage(100, 100, colors.create(255, 255, 255))), "Push a 100x100 image filled with white pixels.");
		oc.reg("graphics.imageWithColor", stack -> stack.push(graphics.createImage(100, 100, colors.create(stack.pop().toString()))), "Push a 100x100 image filled with pixels colored the first stack value.");
		oc.reg("graphics.imageWithSize", stack -> stack.push(graphics.createImage(stack.peek(2).asInt(), stack.pull(2).asInt(), colors.create(255, 255, 255))), "Push an image filled with white pixels with a width of the second stack value and a height of the first stack value.");
		oc.reg("graphics.imageWithSquareSize", stack -> stack.push(graphics.createImage(stack.peek().asInt(), stack.pop().asInt(), colors.create(255, 255, 255))), "Push an image filled with white pixels with a width and height of the first stack value.");
		oc.reg("graphics.imageWithSizeAndColor", stack -> stack.push(graphics.createImage(stack.peek(3).asInt(), stack.peek(2).asInt(), colors.create(stack.pull(3).toString()))), "Push an image color filled with the first stack value with a width of the third stack value and a height of the second stack value.");
		oc.reg("graphics.imageWithSquareSizeAndColor", stack -> stack.push(graphics.createImage(stack.peek(2).asInt(), stack.peek(2).asInt(), colors.create(stack.pull(2).toString()))), "Push an image color filled with the first stack value with a width and height of the second stack value.");
		oc.reg("graphics.drawText", stack -> graphics.drawText(stack.peek(2), stack.pop().toString(), 0, 10, false), "Draw the first stack value as a string to the second stack value at 0, 10.");
		oc.reg("graphics.drawTextAt", stack -> graphics.drawText(stack.peek(4), stack.peek(3).toString(), stack.peek(2).asInt(), stack.pull(3).asInt(), false), "Draw the third stack value as a string to the fourth stack value at the first stack value, the second stack value.");
		oc.reg("graphics.fillText", stack -> graphics.drawText(stack.peek(2), stack.pop().toString(), 0, 10, true), "Fill the first stack value as a string to the second stack value at 0, 10.");
		oc.reg("graphics.fillTextAt", stack -> graphics.drawText(stack.peek(4), stack.peek(3).toString(), stack.peek(2).asInt(), stack.pull(3).asInt(), true), "Fill the third stack value as a string to the fourth stack value at the first stack value, the second stack value.");
	}
}
