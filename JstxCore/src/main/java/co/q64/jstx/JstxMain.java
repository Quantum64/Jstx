package co.q64.jstx;

import javax.inject.Singleton;

import com.google.common.annotations.GwtIncompatible;

@Singleton
@GwtIncompatible("")
public class JstxMain {

	public static void main(String[] args) {
		JstxComponent component = DaggerJstxComponent.create();
	}
}
