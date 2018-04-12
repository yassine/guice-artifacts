package com.github.yassine.artifacts.guice.scheduling;

import com.google.common.collect.*;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.copyOf;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class TaskSchedulerSupport implements TaskScheduler{

  @SuppressWarnings("unchecked")
  public <TYPE> List<Set<Class<? extends TYPE>>> scheduleClasses(final Set<Class<? extends TYPE>> classes){
    Multimap<Class, Class> index = HashMultimap.create();
    classes.stream()
      .filter(clazz -> clazz.isAnnotationPresent(DependsOn.class))
      .forEach(clazz -> index.putAll(clazz, Arrays.asList(clazz.getAnnotation(DependsOn.class).value())));
    classes.stream()
      .filter(clazz -> clazz.isAnnotationPresent(ReverseDependsOn.class))
      .forEach(clazz -> Arrays.asList(clazz.getAnnotation(ReverseDependsOn.class).value())
                          .forEach(reverse -> index.put(reverse, clazz)));
    return schedule(classes, (clazz) -> index.containsKey(clazz)
      ? ImmutableSet.copyOf(index.get(clazz).stream().map(dependentClass -> (Class<? extends TYPE>) dependentClass).collect(Collectors.toSet()))
      : ImmutableSet.of());
  }

  public <TYPE> List<Set<TYPE >> scheduleInstances(final Set<TYPE> instances){
    @SuppressWarnings("unchecked")
    Set<Class<? extends TYPE>> classes = instances.stream().map(t -> (Class<? extends TYPE>)t.getClass())
                                                  .collect(Collectors.toSet());
    return copyOf(scheduleClasses(classes).stream()
                    .map(set -> ImmutableSet.copyOf(set.stream()
                        .flatMap(v -> instances.stream().filter(instance -> v.isAssignableFrom(instance.getClass())))
                        .collect(Collectors.toSet()))
                    ).collect(Collectors.toList()));
  }

  public <T> List<Set<T>> schedule(final Set<T> tasks, final Function<T, Set<T>> directDependencyProvider){
    HashMultimap<T, T> mb = HashMultimap.create();
    DefaultDirectedGraph<T, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    tasks.forEach(graph::addVertex);
    tasks.forEach(task -> directDependencyProvider.apply(task)
                            .forEach(cls -> {
                              graph.addEdge(cls, task);
                              mb.put(task, cls);
                            }));
    checkNoCycle(graph);

    List<T> orderedTasks = Streams.stream(new TopologicalOrderIterator<>(graph))
                              .collect(Collectors.toList());
    Set<T> currentGroup   = new HashSet<>();
    List<Set<T>> schedule = new ArrayList<>();
    for (T current : orderedTasks) {
      Set<T> dependencies = mb.get(current);
      if (!Sets.intersection(dependencies, currentGroup).isEmpty()) {
        schedule.add(currentGroup);
        currentGroup = new HashSet<>();
      }
      currentGroup.add(current);
    }
    schedule.add(currentGroup);

    return schedule;
  }

  private <T> void checkNoCycle(DefaultDirectedGraph<T, DefaultEdge> graph){
    CycleDetector<T, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
    if(cycleDetector.detectCycles()){
      StringBuilder mainSB = new StringBuilder();
      cycleDetector.findCycles()
        .forEach(node -> {
          StringBuilder sb = new StringBuilder();
          cycleDetector.findCyclesContainingVertex(node).forEach(cycle -> {
            sb.append(" -> ");
            sb.append(cycle.toString());
          });
          mainSB.append(sb);
          mainSB.append("\n");
        });
      throw new CyclicDependencyException("Detected Dependency Cycle : \n"+mainSB.toString());
    }
  }


}
