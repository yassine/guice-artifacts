package com.github.yassine.artifacts.guice.scheduling;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DependsOn {
  Class[] value();
}
