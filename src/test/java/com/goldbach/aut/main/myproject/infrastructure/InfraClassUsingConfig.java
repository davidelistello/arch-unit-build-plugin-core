package com.goldbach.aut.main.myproject.infrastructure;

import com.goldbach.aut.main.myproject.config.ConfigClass;

public class InfraClassUsingConfig {

    public InfraClassUsingConfig() {

        ConfigClass configClassThatShouldNotBeHere=new ConfigClass();

    }
}
