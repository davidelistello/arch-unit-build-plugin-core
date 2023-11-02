package com.gb.cleanarch.inspector.core.lib.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionUtilsTest {

    @Test
    public void shouldLoadRuleClassEvenIfConstructorIsPrivate() throws ClassNotFoundException {

        assertThat(ReflectionUtils.newInstance(Class.forName("com.tngtech.archunit.library.GeneralCodingRules"))).isNotNull();
    }

}