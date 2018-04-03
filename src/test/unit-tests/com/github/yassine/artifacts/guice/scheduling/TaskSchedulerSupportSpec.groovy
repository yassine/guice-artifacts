package com.github.yassine.artifacts.guice.scheduling

import com.google.inject.Inject
import spock.guice.UseModules
import spock.lang.Specification

import static com.google.common.collect.ImmutableSet.copyOf
import static java.util.Arrays.asList

@UseModules(TaskSchedulerModule.class)
class TaskSchedulerSupportSpec extends Specification {
  @Inject
  TaskScheduler taskScheduler

  /*
     A -----> B -----> E
                      /
     C -----> D _____/
  */
  def "scheduleClasses : it should a list of 'waves' that includes tasks that can run in parallel"() {
    given:
    List<Set<Class<? extends Task>>> waves = taskScheduler.scheduleClasses(copyOf(asList(A.class, B.class, C.class, D.class, E.class)))
    expect:
    waves.get(0) == [A.class, C.class] as Set
    waves.get(1) == [B.class, D.class] as Set
    waves.get(2) == [E.class] as Set
  }

  /*
     CycleA -----> CycleB -----> CycleC
       ^                           |
       |___________________________|
  */
  def "scheduleClasses : it should throw an exception if a cycle is detected"() {
    when:
    taskScheduler.scheduleClasses(copyOf(asList(CycleA.class, CycleB.class, CycleC.class)))
    then:
    Exception e = thrown()
    e instanceof CyclicDependencyException
  }

  def "scheduleInstances: it should a list of 'waves' that includes tasks that can run in parallel"() {
    given:
    A a = new A()
    B b = new B()
    C c = new C()
    D d = new D()
    E e = new E()
    List<Set<Task>> waves = taskScheduler.scheduleInstances(copyOf(asList(a, b, c, d, e)))
    expect:
    waves.get(0) == [a, c] as Set
    waves.get(1) == [b, d] as Set
    waves.get(2) == [e] as Set
  }

  def "scheduleInstances: it should throw an exception if a cycle is detected"() {
    when:
    CycleA a = new CycleA()
    CycleB b = new CycleB()
    CycleC c = new CycleC()
    taskScheduler.scheduleInstances(copyOf(asList(a, b, c)))
    then:
    Exception e = thrown()
    e instanceof CyclicDependencyException
  }

  static interface Task {}
  static class A implements Task {}
  @DependsOn(A.class)
  static class B implements Task {}
  static class C implements Task {}
  @DependsOn(C.class)
  static class D implements Task {}
  @DependsOn([B.class, D.class])
  static class E implements Task {}
  @DependsOn(CycleC.class)
  static class CycleA implements Task {}
  @DependsOn(CycleA.class)
  static class CycleB implements Task {}
  @DependsOn(CycleB.class)
  static class CycleC implements Task {}

}
