package com.github.yassine.artifacts.guice.utils

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.util.Types
import spock.lang.Specification

import java.util.stream.Collectors


class GuiceUtilsSpec extends Specification {

  def "BindImplementationsAsSet"() {
    given:
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        GuiceUtils.bindImplementationsAsSet(binder(), Fixtures.Contract.class, Fixtures.Contract.class.getPackage());
      }
    })
    Set<Fixtures.Contract> implementations = injector.getInstance(Key.get(Types.setOf(Fixtures.Contract.class))) as Set<Fixtures.Contract>

    expect:
    implementations.size() == 3
    implementations.stream().anyMatch{implementation -> (implementation.getClass() == Fixtures.ContractImplementation1.class) }
    implementations.stream().anyMatch{implementation -> (implementation.getClass() == Fixtures.ContractImplementation2.class) }
    implementations.stream().anyMatch{implementation -> (implementation.getClass() == Fixtures.ContractImplementation4.class) }
  }

  def "it should ignore implementations annotated by a user defined scanning bypass flag"() {
    given:
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        GuiceUtils.bindImplementationsAsSet(binder(), Fixtures.Contract.class, Fixtures.Contract.class.getPackage(), Fixtures.IgnoreMe.class)
      }
    })
    Set<Fixtures.Contract> implementations = injector.getInstance(Key.get(Types.setOf(Fixtures.Contract.class))) as Set<Fixtures.Contract>

    expect:
    implementations.size() == 2
    implementations.stream().anyMatch{implementation -> (implementation.getClass() == Fixtures.ContractImplementation1.class) }
    implementations.stream().anyMatch{implementation -> (implementation.getClass() == Fixtures.ContractImplementation2.class) }
  }

  def "LoadSPIExtensions"() {
    given:
    Set<Fixtures.SPIContract> spiClasses = GuiceUtils.loadSPIExtensions(Fixtures.SPIContract.class)

    expect:
    spiClasses.size() == 2
    spiClasses.stream().map{spiInstance -> spiInstance.getClass()}.collect(Collectors.toSet()) == [Fixtures.SPIContractImplementation1, Fixtures.SPIContractImplementation2] as Set
  }

  def "LoadSPIClasses"() {
    given:
    Set<Class<? extends Fixtures.SPIContract>> spiClasses = GuiceUtils.loadSPIClasses(Fixtures.SPIContract.class)

    expect:
    spiClasses.size() == 2
    spiClasses == [Fixtures.SPIContractImplementation1, Fixtures.SPIContractImplementation2] as Set
  }


}
