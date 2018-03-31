package com.github.yassine.artifacts.guice.utils;

import com.google.common.collect.ImmutableSet;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by yassine on 11/29/16.
 */
public class ScanUtils {

  @SuppressWarnings("unchecked")
  public static <CONTRACT, IMPLEMENTATION extends CONTRACT> Set<Class<IMPLEMENTATION>> scanForImplementationsOf(Class<CONTRACT> clazz, String... packages) {
    ImmutableSet.Builder<Class<IMPLEMENTATION>> items = ImmutableSet.builder();
    FastClasspathScanner scanner = new FastClasspathScanner(packages);
    scanner.matchClassesImplementing(clazz, (implementation) -> items.add((Class<IMPLEMENTATION>)implementation));
    scanner.scan();
    return items.build();
  }

  @SuppressWarnings("unchecked")
  public static <CONTRACT, IMPLEMENTATION extends CONTRACT> Set<IMPLEMENTATION> scanAndInstantiateImplementations(Class<CONTRACT> contractClass, String... packages) {
    return  ImmutableSet.copyOf(scanForImplementationsOf(contractClass, packages)).stream()
        .filter(ReflectionUtils::hasNoArgConstructor)
        .map((implementation) -> (IMPLEMENTATION) ReflectionUtils.instantiate(implementation))
        .collect(Collectors.toSet());
  }

}
