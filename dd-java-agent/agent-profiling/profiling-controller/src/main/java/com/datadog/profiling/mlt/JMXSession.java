package com.datadog.profiling.mlt;

import datadog.trace.profiling.Session;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JMXSession implements Session {
  private final String id;
  private final long threadId;
  private final Consumer<JMXSession> cleanup;
  private final AtomicInteger refCount = new AtomicInteger();

  public JMXSession(String id, long threadId, Consumer<JMXSession> cleanup) {
    log.info("new JMXSession for id={}, threadId={}", id, threadId);
    this.id = id;
    this.threadId = threadId;
    this.cleanup = cleanup;
  }

  @Override
  public void close() {
    cleanup.accept(this);
  }

  String getId() {
    return id;
  }

  long getThreadId() {
    return threadId;
  }

  void activate() {
    refCount.getAndIncrement();
  }

  int deactivate() {
    return refCount.decrementAndGet();
  }
}
