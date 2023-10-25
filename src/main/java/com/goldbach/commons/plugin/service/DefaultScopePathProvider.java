package com.goldbach.commons.plugin.service;

import com.goldbach.commons.plugin.model.RootClassFolder;

/**
 * Default scope path provider, returning typical values for Maven
 */
public class DefaultScopePathProvider implements ScopePathProvider {
    @Override
    public RootClassFolder getMainClassesPath() {
        return new RootClassFolder("target/classes");
    }

    @Override
    public RootClassFolder getTestClassesPath() {
        return new RootClassFolder("target/test-classes");
    }
}
