package com.github.yassine.artifacts.guice.templating;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.lang.reflect.Field;

/**
 *
 * @author Yassine E. <yechabbi at gmail.com>
 */
class TemplateGuiceTypeListener implements TypeListener{

  @Inject @TemplatingModule.Internal
  private Configuration configuration;

  @Override
  public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
    Class<?> clazz = typeLiteral.getRawType();
    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        if ( (field.getType() == TemplateCompiler.class
                || field.getType() == Template.class ) &&
          field.isAnnotationPresent(TemplateResource.class)) {
          TemplateResource resource = field.getAnnotation(TemplateResource.class);
          String path = resource.value();
          if(resource.relative()){
            path = field.getDeclaringClass().getPackage()
                        .getName()
                        .replaceAll("\\.", "\\/")+ "/" + path;
            path = path.replaceAll("\\/+","\\/");
          }
          //noinspection unchecked
          typeEncounter.register(new TemplateMemberInjector(field, path, configuration));
        }
      }
      clazz = clazz.getSuperclass();
    }
  }
    
}
