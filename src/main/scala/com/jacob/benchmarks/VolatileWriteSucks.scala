package com.jacob.benchmarks

import java.util.concurrent.atomic.AtomicLong

import org.openjdk.jmh.annotations.{Benchmark, Scope, State}
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Thread)
class VolatileWriteSucks {

  private val value = new AtomicLong()

  @Benchmark
  def incrementAndGet(bh: Blackhole): Unit = {
    bh.consume(value.incrementAndGet())
  }

  @Benchmark
  def addAndGet(bh: Blackhole): Unit = {
    bh.consume(value.addAndGet(42))
  }

  @Benchmark
  def get(bh: Blackhole): Unit = {
    bh.consume(value.get())
  }

}
