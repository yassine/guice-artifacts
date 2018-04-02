package com.github.yassine.artifacts.guice.scheduling;

import com.google.inject.AbstractModule;

public class TaskSchedulerModule extends AbstractModule{
  @Override
  protected void configure() {
    bind(TaskScheduler.class).to(TaskSchedulerSupport.class);
  }
}
