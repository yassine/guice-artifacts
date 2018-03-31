package com.github.yassine.artifacts.guice.templating;

import freemarker.template.Template;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 *
 * @author Yassine E. <yechabbi at gmail.com>
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TemplateCompilerSupport implements TemplateCompiler {
    
  private final Template template;

  @Override
  public void compile(Map<String, ?> params, Writer out) throws Exception {
    template.process(params, out);
  }

  @Override
  public String compile(Map<String, ?> params) throws Exception {
    StringWriter sw = new StringWriter();
    compile(params, sw);
    return sw.toString();
  }
    
}
