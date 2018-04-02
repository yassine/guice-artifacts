package com.github.yassine.artifacts.guice.scheduling;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * A task scheduler aims at solving the following problem:
 * Given a set of tasks, where each task can have a set of preceding tasks, what are the tasks that can be executed in
 * parallel? on which order?
 */
public interface TaskScheduler {
  /**
   * @param classes a set of classes annotated which declared its dependencies using <code>@DependsOn</code> annotation
   * @param <TYPE> a given type
   * @return a list of 'waves' of tasks that can be executed in parallel
   */
  <TYPE> List<Set<Class<? extends TYPE>>> scheduleClasses(final Set<Class<? extends TYPE>> classes);
  /**
   * @param instances a set of instances of which the classes are annotated which <code>@DependsOn</code> annotation
   * @param <TYPE> a given type
   * @return a list of 'waves' of tasks that can be executed in parallel
   */
  <TYPE> List<Set<TYPE>> scheduleInstances(final Set<TYPE> instances);
  /**
   * @param tasks a set of tasks
   * @param directDependencyProvider a task dependency provider
   * @param <TYPE> the type of task
   * @return a list of 'waves' of tasks that can be executed in parallel
   */
  <TYPE> List<Set<TYPE>> schedule(final Set<TYPE> tasks, final Function<TYPE, Set<TYPE>> directDependencyProvider);
}
