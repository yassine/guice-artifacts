package com.github.yassine.artifacts.guice.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Created by yassine on 11/29/16.
 */
public class ReflectionUtils {

  @SneakyThrows
  public static <T> T instantiate(Class<? extends T> clazz){
    return clazz.newInstance();
  }

  public static boolean hasNoArgConstructor(Class clazz){
    return Arrays.stream(clazz.getDeclaredConstructors())
            .anyMatch((constructor)->constructor.getParameterCount() == 0);
  }

  public static boolean hasPublicNoArgConstructor(Class clazz){
    return Arrays.stream(clazz.getDeclaredConstructors())
      .anyMatch((constructor)->constructor.getParameterCount() == 0 && Modifier.isPublic(constructor.getModifiers()));
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  public static <TYPE> Class<? extends TYPE> sneakyClassForName(String name) {
    return (Class<? extends TYPE>) Class.forName(name);
  }
}
