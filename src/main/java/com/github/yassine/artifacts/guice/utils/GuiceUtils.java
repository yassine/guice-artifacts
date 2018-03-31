package com.github.yassine.artifacts.guice.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.FileMatchProcessor;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ImplementingClassMatchProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.apache.commons.io.IOUtils.readLines;

public class GuiceUtils {

  @SafeVarargs
  public static <T> void bindImplementationsAsSet(Binder binder, Class<T> contract, Package pkg, Class<? extends Annotation>... ignore) {
    Set<Class<? extends T>> items = new HashSet<>();
    FastClasspathScanner scanner = new FastClasspathScanner(pkg.getName());
    scanner.matchClassesImplementing(contract, (ImplementingClassMatchProcessor<T>) items::add);
    scanner.scan();
    Set<Class<? extends T>> implementations = ImmutableSet.copyOf(items.stream()
        .map(clazz -> (Class<? extends T>) clazz)
        .filter(clazz -> Streams.concat(Arrays.stream(ignore), Stream.of(ScanIgnore.class)).noneMatch(clazz::isAnnotationPresent))
        .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
        .collect(Collectors.toSet()));
    Multibinder<T> multiBinder = Multibinder.newSetBinder(binder, contract);
    implementations.forEach(implementation -> multiBinder.addBinding().to(implementation));
  }

  public static <T> void bindImplementationsAsSet(Binder binder, Class<T> contract, Package pkg) {
    bindImplementationsAsSet(binder, contract, pkg, new Class[0]);
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> loadSPIExtensions(Class<? extends T> extensionContract) {
    Iterator<T> it = (Iterator<T>) ServiceLoader.load(extensionContract).iterator();
    Iterable<T> iterable = () -> it;
    return StreamSupport.stream(iterable.spliterator(), false)
      .collect(Collectors.toList());
  }

  public static <CONTRACT> Set<Class<? extends CONTRACT>> loadSPIClasses(Class<? extends CONTRACT> contract) {
    HashSet<String> pluginModuleImplementations = new HashSet<>();
    FastClasspathScanner scanner = new FastClasspathScanner("META-INF/services");
    scanner.matchFilenamePattern(Pattern.quote("META-INF/services/" + contract.getName()),
        (FileMatchProcessor) (relativePath, inputStream, lengthBytes) ->
          pluginModuleImplementations.addAll(readLines(inputStream, "utf-8"))
    );
    scanner.scan();
    return pluginModuleImplementations.stream()
      .map(ReflectionUtils::<CONTRACT>sneakyClassForName)
      .collect(Collectors.toSet());
  }

}
