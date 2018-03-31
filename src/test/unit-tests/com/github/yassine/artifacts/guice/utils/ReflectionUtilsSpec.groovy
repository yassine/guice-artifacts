package com.github.yassine.artifacts.guice.utils

import spock.lang.Specification

class ReflectionUtilsSpec extends Specification {

  def "Instantiate"() {
    given:
      SampleClass sc = ReflectionUtils.instantiate(SampleClass.class)
    expect:
      sc instanceof SampleClass
  }

  def "HasNoArgConstructor"() {
    expect:
    ReflectionUtils.hasNoArgConstructor(SampleClass.class)
    !ReflectionUtils.hasNoArgConstructor(SampleClass2.class)
  }

  def "HasPublicNoArgConstructor"() {
    expect:
    ReflectionUtils.hasPublicNoArgConstructor(SampleClass.class)
    !ReflectionUtils.hasPublicNoArgConstructor(SampleClass2.class)
  }

  def "SneakyClassForName"() {
    expect:
    ReflectionUtils.sneakyClassForName(SampleClass.class.getName()) == SampleClass.class
  }

  static class SampleClass {

  }

  static class SampleClass2{
    private final String arg;

    SampleClass2(String arg) {
      this.arg = arg;
    }
  }

  static class SampleClass3{
    private SampleClass3() {
    }
  }

}
