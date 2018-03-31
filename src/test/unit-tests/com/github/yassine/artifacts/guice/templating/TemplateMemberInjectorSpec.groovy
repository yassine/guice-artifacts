package com.github.yassine.artifacts.guice.templating

import com.google.common.collect.ImmutableMap
import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import freemarker.template.Template
import lombok.Getter
import spock.lang.Specification

import java.lang.reflect.Field

class TemplateMemberInjectorSpec extends Specification {

  def "it should inject TemplateCompiler fields"() {
    given:
      Field field = ServiceA.class.getDeclaredField("templateCompiler")
      Configuration cfg = new Configuration(Configuration.VERSION_2_3_25)
      ClassTemplateLoader templateLoader = new ClassTemplateLoader(getClass(), "/")
      cfg.setTemplateLoader(templateLoader)
      cfg.setDefaultEncoding("UTF-8")
      TemplateMemberInjector templateMemberInjector = new TemplateMemberInjector(field, "com/github/yassine/artifacts/guice/templating/template.sample", cfg)
      ServiceA serviceA = new ServiceA();
      templateMemberInjector.injectMembers(serviceA)
    expect:
      serviceA.templateCompiler != null
      serviceA.templateCompiler.compile(ImmutableMap.of("test","world")) == "hello world"
  }

  def "it should inject Template fields"() {
    given:
      Field field = ServiceB.class.getDeclaredField("template")
      Configuration cfg = new Configuration(Configuration.VERSION_2_3_25)
      ClassTemplateLoader templateLoader = new ClassTemplateLoader(getClass(), "/")
      cfg.setTemplateLoader(templateLoader)
      cfg.setDefaultEncoding("UTF-8")
      TemplateMemberInjector templateMemberInjector = new TemplateMemberInjector(field, "com/github/yassine/artifacts/guice/templating/template.sample", cfg)
      ServiceB serviceB = new ServiceB();
      templateMemberInjector.injectMembers(serviceB)
      StringWriter sw = new StringWriter()
      serviceB.template.process(ImmutableMap.of("test","world"), sw)
    expect:
      sw.toString() == "hello world"
  }

  static class ServiceA {
    @Getter
    private TemplateCompiler templateCompiler
  }

  static class ServiceB {
    @Getter
    private Template template
  }

}
