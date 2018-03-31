package com.github.yassine.artifacts.guice.templating;

import java.io.Writer;
import java.util.Map;

/**
 *
 * @author Yassine E. <yechabbi at gmail.com>
 */
public interface TemplateCompiler {
    
  void compile(Map<String, ?> params, Writer out) throws Exception;

  String compile(Map<String, ?> params) throws Exception;
    
}
