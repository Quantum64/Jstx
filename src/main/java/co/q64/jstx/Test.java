package co.q64.jstx;

import java.util.Arrays;
import java.util.List;

public class Test {
	public static final List<String> PROGRAM = Arrays.asList(
	// @formatter:off
	"load 1",
	"load 9",
	"list.range",
	"iterate",
	"ldr o",
	"load 3",
	"%",
	"load 0",
	"if =",
	"load Fizz",
	"load 24",
	"jump",
	"endif",
	"ldr o",
	"load 5",
	"%",
	"load 0",
	"if =",
	"load Buzz",
	"load 24",
	"jump",
	"endif",
	"ldr o",
	"println",
	"end"
	// @formatter:on
	);

	public static final String[] ARGS = { "" };
}
