package com.github.yassine.artifacts.guice.templating;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * @author Yassine E. <yechabbi at gmail.com>
 */
@Target({ ElementType.CONSTRUCTOR, ElementType.FIELD  })
@Retention(RUNTIME)
public @interface TemplateResource {
  String value();
  boolean relative() default true;
}
