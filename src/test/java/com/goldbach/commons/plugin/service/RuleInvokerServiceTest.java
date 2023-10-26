package com.goldbach.commons.plugin.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.SilentLogWithMemory;
import com.goldbach.commons.plugin.model.ApplyOn;
import com.goldbach.commons.plugin.model.RootClassFolder;
import com.goldbach.commons.plugin.rules.HexagonalArchitectureArchRuleCheck;
import com.goldbach.commons.plugin.rules.classesForTests.DummyCustomRule;
import com.goldbach.aut.test.TestSpecificScopeProvider;
import com.goldbach.commons.plugin.model.ConfigurableRule;
import com.goldbach.commons.plugin.model.Rules;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.Ignore;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class RuleInvokerServiceTest {

    RuleInvokerService ruleInvokerService = new RuleInvokerService(new SilentLog(), new TestSpecificScopeProvider());

    ConfigurableRule configurableRule = new ConfigurableRule();

    @Test
    public void shouldInvokePreConfiguredRuleThatCanLog()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        Rules rules = new Rules(Arrays.asList(HexagonalArchitectureArchRuleCheck.class.getName()), emptyList());

        String errorMessage = ruleInvokerService.invokeRules(rules);

        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("Rule 'classes that reside in a package");
    }

    @Test
    public void shouldNotExecuteSkippedConfigurableRules()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.goldbach.commons.plugin.rules", "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);
        configurableRule.setChecks(Arrays.asList("annotatedWithTest", "resideInMyPackage"));
        configurableRule.setSkip(true);

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);
        assertThat(errorMessage).isEmpty();
    }

    @Test
    @Ignore
    public void shouldExecuteConfigurableRuleWithNoPackageProvided_OnlyOnClassesOfScope()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn(null, "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);
        configurableRule.setChecks(Arrays.asList("annotatedWithTest"));

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);
        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).doesNotContain("Class <main.com.goldbach.aut.ObjectWithAdateField>");
        assertThat(errorMessage).contains("Class <test.com.goldbach.aut.TestClassWithOutJunitAsserts>");
    }

    @Test
    public void shouldExecute2ConfigurableRulesOnTest()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.goldbach.commons.plugin.rules", "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);
        configurableRule.setChecks(Arrays.asList("annotatedWithTest", "resideInMyPackage"));

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);
        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("classes should be annotated with @Test");
        assertThat(errorMessage).contains("classes should reside in a package 'myPackage'");
    }

    @Test
    public void shouldExecuteOnlyTheConfiguredRule()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.goldbach.commons.plugin.rules", "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);
        configurableRule.setChecks(singletonList("annotatedWithTest"));

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);
        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("classes should be annotated with @Test");
        assertThat(errorMessage).doesNotContain("classes should reside in a package 'myPackage'");
    }

    @Test
    public void shouldExecuteAllRulesFromConfigurableClassByDefault()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.goldbach.commons.plugin.rules", "main");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);

        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("classes should be annotated with @Test");
        assertThat(errorMessage).contains("classes should reside in a package 'myPackage'");
    }

    @Test
    public void shouldExecuteAllRulesOnSpecificPackageInTest()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.goldbach.aut.test.specificCase", "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);

        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains("Rule 'classes should be annotated with @Test' was violated (1 times)");
        assertThat(errorMessage).contains("Rule 'classes should reside in a package 'myPackage'' was violated (1 times)");
    }

    @Test
    @Ignore
    public void shouldExecuteAllRulesFromArchUnit_GeneralCodingRule()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        ApplyOn applyOn = new ApplyOn("com.goldbach.aut.test.specificCase", "test");

        configurableRule.setRule(GeneralCodingRules.class.getName());
        configurableRule.setApplyOn(applyOn);

        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        String errorMessage = ruleInvokerService.invokeRules(rules);

        assertThat(errorMessage).isNotEmpty();
        assertThat(errorMessage).contains("Architecture Violation");
        assertThat(errorMessage).contains(
                "Rule 'no classes should use JodaTime, because modern Java projects use the [java.time] API instead' was violated (1 times)");
        assertThat(errorMessage).contains(
                "Field <specificCase.test.com.goldbach.aut.DummyClassToValidate.anyJodaTimeObject> has type <org.joda.time.JodaTimePermission");
    }

    @Test
    public void testScopeProviderWithDots() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        ApplyOn applyOn = new ApplyOn("com.goldbach.aut.test.specificCase", "test");

        configurableRule.setRule(DummyCustomRule.class.getName());
        configurableRule.setApplyOn(applyOn);
        Rules rules = new Rules(emptyList(), Arrays.asList(configurableRule));

        SilentLogWithMemory logger = new SilentLogWithMemory();

        ruleInvokerService = new RuleInvokerService(logger, new TestSpecificScopeProviderWithDotsInPath());

        ruleInvokerService.invokeRules(rules);

        assertThat(logger.getInfoLogs()).contains("invoking ConfigurableRule "+configurableRule.toString()+" on test/minor-1.2/com/goldbach/aut/test/specificCase");
    }

    private class TestSpecificScopeProviderWithDotsInPath  implements ScopePathProvider{
        @Override
        public RootClassFolder getMainClassesPath() {
            return new RootClassFolder("main/minor-1.2");
        }

        @Override
        public RootClassFolder getTestClassesPath() {
            return new RootClassFolder("test/minor-1.2");
        }
    }
}
