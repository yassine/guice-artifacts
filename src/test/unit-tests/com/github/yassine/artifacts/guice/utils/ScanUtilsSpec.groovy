package com.github.yassine.artifacts.guice.utils

import spock.lang.Specification

import java.util.stream.Collectors

class ScanUtilsSpec extends Specification {

  def "ScanForImplementationsOf"() {
    given:
      Set<Class> classes = ScanUtils.scanForImplementationsOf(Fixtures.SPIContract, Fixtures.SPIContract.class.getPackage().getName())
    expect:
      classes.size() == 2
      classes == [Fixtures.SPIContractImplementation1.class, Fixtures.SPIContractImplementation2.class] as Set
  }

  def "ScanAndInstantiateImplementations"() {
    given:
    Set<Fixtures.SPIContract> instances = ScanUtils.scanAndInstantiateImplementations(Fixtures.SPIContract, Fixtures.SPIContract.class.getPackage().getName())
    expect:
    instances.size() == 2
    instances.stream().map{instance -> instance.getClass()}.collect(Collectors.toSet()) == [Fixtures.SPIContractImplementation1.class, Fixtures.SPIContractImplementation2.class] as Set
  }

}
