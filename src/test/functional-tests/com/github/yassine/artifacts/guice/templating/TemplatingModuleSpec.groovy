package com.github.yassine.artifacts.guice.templating

import com.google.common.collect.ImmutableMap
import com.google.inject.AbstractModule
import com.google.inject.Inject
import freemarker.template.Template
import lombok.Getter
import spock.guice.UseModules
import spock.lang.Specification

@UseModules(TestModule.class)
class TemplatingModuleSpec extends Specification {

  @Inject
  private ServiceA serviceA
  @Inject
  private ServiceB serviceB

  def "Configure"() {
    given:
      Map<String,String> values = ImmutableMap.of("test","world")
      StringWriter sw = new StringWriter()
      serviceB.template.process(values, sw)
    expect:
      serviceA.templateCompiler != null
      serviceA.templateCompiler.compile(ImmutableMap.of("test","world")) == "hello world"
      sw.toString() == "hello world"
  }

  static class TestModule extends AbstractModule{
    @Override
    protected void configure() {
      install(new TemplatingModule())
    }
  }

  static class ServiceA{
    @TemplateResource("template.sample") @Getter
    private TemplateCompiler templateCompiler
  }

  static class ServiceB{
    @TemplateResource("template.sample") @Getter
    private Template template
  }

}
