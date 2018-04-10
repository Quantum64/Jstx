package co.q64.jstx;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import co.q64.jstx.compiler.CompilerOutput;
import co.q64.jstx.inject.StandardModule;
import co.q64.jstx.inject.SystemModule;
import co.q64.jstx.lang.opcode.Opcodes;
import co.q64.jstx.runtime.Output;
import dagger.Component;
import lombok.AllArgsConstructor;

public class BenchmarkTest {
	@Test
	public void benchmark() throws Exception {
		Options opt = new OptionsBuilder()
		// @formatter:off
				.include(this.getClass().getName() + ".*")
				.mode(Mode.AverageTime)
				.timeUnit(TimeUnit.MICROSECONDS)
				.warmupTime(TimeValue.milliseconds(100))
				.warmupIterations(1)
				.measurementTime(TimeValue.milliseconds(100))
				.measurementIterations(2)
				.threads(4)
				.forks(1)
				.shouldFailOnError(true)
				.shouldDoGC(true)
				.build();
		// @formatter:on
		new Runner(opt).run();
	}

	@Benchmark
	public void benchmarkOpcodeRegistration(Blackhole blackhole) {
		Opcodes opcodes = DaggerBenchmarkTest_OpcodesComponent.create().getOpcodes();
		blackhole.consume(opcodes);
	}

	@Benchmark
	public void benchmarkHelloWorld(Blackhole blackhole) {
		Jstx jstx = DaggerJstxMain_JstxMainComponent.create().getJstx();
		CompilerOutput compiled = jstx.compileProgram(Arrays.asList("load Hello,", "load World!", "flatten soft"));
		Assert.assertEquals(true, compiled.isSuccess());
		jstx.runProgram(compiled.getProgram(), "", new BlackholeOutput(blackhole));
	}

	@AllArgsConstructor
	private static class BlackholeOutput implements Output {
		private Blackhole blackhole;

		@Override
		public void print(String message) {
			blackhole.consume(message);
		}

		@Override
		public void println(String message) {
			print(message);
		}
	}

	@Singleton
	@Component(modules = { SystemModule.class, StandardModule.class })
	protected static interface OpcodesComponent {
		public Opcodes getOpcodes();
	}
}
