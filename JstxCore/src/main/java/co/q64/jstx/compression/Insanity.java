package co.q64.jstx.compression;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Insanity {
	private List<Integer> points = Arrays.asList(0, 19893, 40872, 55197);
	private List<Integer> shift = Arrays.asList(13319, 11, 3166, 75877);

	protected @Inject Insanity() {}

	public String getCharacter(int index) {
		StringBuilder sb = new StringBuilder();
		sb.appendCodePoint(getOffset(index));
		return sb.toString();
	}

	public int getIndex(int offset) {
		int result = offset, s = 0;
		for (int i = 0; i < points.size(); i++) {
			if (offset >= points.get(i) + shift.get(i) + s) {
				result -= shift.get(i);
				s += shift.get(i);
			}
		}
		return result;
	}

	private int getOffset(int codepoint) {
		int result = codepoint;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i) <= codepoint) {
				result += shift.get(i);
			}
		}
		return result;
	}

	public IntStream codePoints(String str) {
		class CodePointIterator implements PrimitiveIterator.OfInt {
			int cur = 0;

			@Override
			public void forEachRemaining(IntConsumer block) {
				final int length = str.length();
				int i = cur;
				try {
					while (i < length) {
						char c1 = str.charAt(i++);
						if (!Character.isHighSurrogate(c1) || i >= length) {
							block.accept(c1);
						} else {
							char c2 = str.charAt(i);
							if (Character.isLowSurrogate(c2)) {
								i++;
								block.accept(Character.toCodePoint(c1, c2));
							} else {
								block.accept(c1);
							}
						}
					}
				} finally {
					cur = i;
				}
			}

			public boolean hasNext() {
				return cur < str.length();
			}

			public int nextInt() {
				final int length = str.length();

				if (cur >= length) {
					throw new NoSuchElementException();
				}
				char c1 = str.charAt(cur++);
				if (Character.isHighSurrogate(c1) && cur < length) {
					char c2 = str.charAt(cur);
					if (Character.isLowSurrogate(c2)) {
						cur++;
						return Character.toCodePoint(c1, c2);
					}
				}
				return c1;
			}
		}

		return StreamSupport.intStream(() -> Spliterators.spliteratorUnknownSize(new CodePointIterator(), Spliterator.ORDERED), Spliterator.ORDERED, false);
	}
}
