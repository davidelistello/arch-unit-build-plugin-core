package com.goldbach.aut.test;

import com.goldbach.commons.plugin.model.RootClassFolder;
import com.goldbach.commons.plugin.service.ScopePathProvider;

public class TestSpecificScopeProvider implements ScopePathProvider {

    @Override
    public RootClassFolder getMainClassesPath() {
        return new RootClassFolder("./target/aut-target/classes/");
    }

    @Override
    public RootClassFolder getTestClassesPath() {
        return new RootClassFolder("./target/aut-target/test-classes/");
    }
}
