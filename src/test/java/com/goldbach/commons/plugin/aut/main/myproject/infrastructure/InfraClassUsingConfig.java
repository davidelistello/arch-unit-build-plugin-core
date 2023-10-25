package com.goldbach.commons.plugin.aut.main.myproject.infrastructure;

import com.goldbach.commons.plugin.aut.main.myproject.config.ConfigClass;

public class InfraClassUsingConfig {

    public InfraClassUsingConfig() {

        ConfigClass configClassThatShouldNotBeHere=new ConfigClass();

    }
}
