package com.github.yassine.artifacts.guice.templating;

import com.google.inject.Binder;
import com.google.inject.BindingAnnotation;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Yassine E. <yechabbi at gmail.com>
 */
public class TemplatingModule implements Module{

  @Override
  public void configure(Binder binder) {
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
    ClassTemplateLoader templateLoader = new ClassTemplateLoader(getClass(), "/");
    cfg.setTemplateLoader(templateLoader);
    cfg.setDefaultEncoding("UTF-8");
    binder.bind(Configuration.class).annotatedWith(Internal.class).toInstance(cfg);
    TemplateGuiceTypeListener listener = new TemplateGuiceTypeListener();
    binder.requestInjection(listener);
    binder.bindListener(Matchers.any(), listener);
  }

  @BindingAnnotation
  @Retention(RetentionPolicy.RUNTIME)
  @interface Internal{

  }

}
