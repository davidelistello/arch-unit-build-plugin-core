package com.goldbach.commons.plugin.rules;

import com.goldbach.aut.test.TestClassWithIgnoreAtClassLevel;
import com.goldbach.aut.test.TestClassWithIgnoreAtMethodLevel;
import com.goldbach.aut.test.TestClassWithJunit5DisabledAtClassLevel;
import com.goldbach.aut.test.TestClassWithJunit5DisabledAtMethodLevel;
import com.goldbach.aut.test.TestClassWithOutJunitAsserts;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.Test;

import static com.goldbach.commons.plugin.rules.NoTestIgnoreRuleTest.NO_JUNIT_IGNORE_VIOLATION_MESSAGE;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NoTestIgnoreRuleTestTest {

    private JavaClasses testClassWithIgnore = new ClassFileImporter().importClasses(TestClassWithIgnoreAtMethodLevel.class,
                                                                                    TestClassWithIgnoreAtClassLevel.class,
                                                                                    TestClassWithJunit5DisabledAtClassLevel.class,
                                                                                    TestClassWithJunit5DisabledAtMethodLevel.class);

    private JavaClasses testClassWithoutIgnoreAtAll= new ClassFileImporter().importClasses(TestClassWithOutJunitAsserts.class);

    @Test
    public void classesWithNoIgnore_shouldNotThrowAnyViolation(){
        assertThatCode(
                () -> classes().should(NoTestIgnoreRuleTest.notBeenIgnore()).check(testClassWithoutIgnoreAtAll))
                .doesNotThrowAnyException();
    }

    @Test
    public void classesWithNoJunit5DisableNorJunit4Ignore_shouldNotThrowAnyViolation(){
        assertThatCode(
                () -> classes().should(NoTestIgnoreRuleTest.notBeenIgnore()).check(testClassWithoutIgnoreAtAll))
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldThrowViolations(){

        Throwable validationExceptionThrown = catchThrowable(() -> {

            classes().should(NoTestIgnoreRuleTest.notBeenIgnore()).check(testClassWithIgnore);

        });

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining("was violated (4 times)")
                .hasMessageContaining(TestClassWithIgnoreAtClassLevel.class.getName()+", at class level")
                .hasMessageContaining(TestClassWithIgnoreAtMethodLevel.class.getName()+" - someIgnoredTestWithoutAComment, at method level")

                .hasMessageContaining(TestClassWithJunit5DisabledAtClassLevel.class.getName()+", at class level")
                .hasMessageContaining(TestClassWithJunit5DisabledAtMethodLevel.class.getName()+" - someDisabledTestWithoutAComment, at method level")
                .hasMessageContaining(NO_JUNIT_IGNORE_VIOLATION_MESSAGE);

    }
}
