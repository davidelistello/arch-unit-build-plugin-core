package com.gb.cleanarch.inspector.core.lib.service;

import com.gb.cleanarch.inspector.core.lib.model.RootClassFolder;

public interface ScopePathProvider {

    RootClassFolder getMainClassesPath();

    RootClassFolder getTestClassesPath();
}
