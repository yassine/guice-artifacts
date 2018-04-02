package com.github.yassine.artifacts.guice.scheduling;

class CyclicDependencyException extends RuntimeException {
  CyclicDependencyException(String s) {
    super(s);
  }
}
