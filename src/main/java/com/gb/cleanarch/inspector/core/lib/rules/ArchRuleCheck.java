package com.gb.cleanarch.inspector.core.lib.rules;

import java.util.Collection;

import com.gb.cleanarch.inspector.core.lib.service.ScopePathProvider;

@FunctionalInterface
public interface ArchRuleCheck {

  /**
   *
   * @param packagePath the package from which classes should be loaded, for example "com.goldbach"
   * @param scopePathProvider from which root directory we should load classes, either for "main" or "test" classes
   * @param excludedPaths a list of paths to exclude from the analysis
   */
  void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths);

}
