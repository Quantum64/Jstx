package co.q64.jstx.util;

import java.util.Iterator;

import com.google.auto.factory.AutoFactory;

@AutoFactory
public class ArgumentIterator implements Iterator<String> {
	private String[] args;
	private int index = 0;

	protected ArgumentIterator(String[] args) {
		this.args = args;
	}

	@Override
	public boolean hasNext() {
		return args.length > index;
	}

	@Override
	public String next() {
		index++;
		return args[index - 1];
	}
}