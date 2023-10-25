package com.goldbach.commons.plugin.service;

import com.goldbach.commons.plugin.model.RootClassFolder;

public interface ScopePathProvider {

    RootClassFolder getMainClassesPath();

    RootClassFolder getTestClassesPath();
}
