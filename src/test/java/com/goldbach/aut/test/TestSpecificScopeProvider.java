package com.goldbach.aut.test;

import com.gb.cleanarch.inspector.core.lib.model.RootClassFolder;
import com.gb.cleanarch.inspector.core.lib.service.ScopePathProvider;

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
