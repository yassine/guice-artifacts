package com.github.yassine.artifacts.guice.templating;

import com.google.inject.MembersInjector;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 *
 * @author Yassine E. <yechabbi at gmail.com>
 */
@RequiredArgsConstructor
@Slf4j
class TemplateMemberInjector<T> implements MembersInjector<T>{
    
  private final Field field;
  private final String path;
  private final Configuration configuration;

  @Override @SneakyThrows
  public void injectMembers(T instance) {
    field.setAccessible(true);
    Template template = configuration.getTemplate(path);
    if(field.getType().equals(Template.class)){
      field.set(instance, template);
    }else{
      field.set(instance, new TemplateCompilerSupport(template));
    }
    field.setAccessible(false);
  }
    
}
