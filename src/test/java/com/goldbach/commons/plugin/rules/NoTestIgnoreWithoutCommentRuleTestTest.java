package com.goldbach.commons.plugin.rules;

import com.goldbach.commons.plugin.aut.test.TestClassWithIgnoreAtClassLevel;
import com.goldbach.commons.plugin.aut.test.TestClassWithIgnoreAtClassLevelWithComment;
import com.goldbach.commons.plugin.aut.test.TestClassWithIgnoreAtMethodLevel;
import com.goldbach.commons.plugin.aut.test.TestClassWithIgnoreAtMethodLevelWithComment;
import com.goldbach.commons.plugin.aut.test.TestClassWithJunit5DisabledAtClassLevel;
import com.goldbach.commons.plugin.aut.test.TestClassWithJunit5DisabledAtClassLevelWithComment;
import com.goldbach.commons.plugin.aut.test.TestClassWithJunit5DisabledAtMethodLevel;
import com.goldbach.commons.plugin.aut.test.TestClassWithJunit5DisabledAtMethodLevelWithComment;
import com.goldbach.commons.plugin.aut.test.TestClassWithOutJunitAsserts;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NoTestIgnoreWithoutCommentRuleTestTest {

    private JavaClasses testClassWithIgnoreButNoComment = new ClassFileImporter().importClasses(TestClassWithIgnoreAtMethodLevel.class,
                                                                                                TestClassWithIgnoreAtClassLevel.class,
                                                                                                TestClassWithJunit5DisabledAtMethodLevel.class,
                                                                                                TestClassWithJunit5DisabledAtClassLevel.class
                                                                                                );

    private JavaClasses testClassWithIgnoreAndComment = new ClassFileImporter().importClasses(TestClassWithIgnoreAtMethodLevelWithComment.class,
                                                                                                TestClassWithIgnoreAtClassLevelWithComment.class,
                                                                                                TestClassWithJunit5DisabledAtMethodLevelWithComment.class,
                                                                                                TestClassWithJunit5DisabledAtClassLevelWithComment.class);

    private JavaClasses testClassWithoutIgnoreAtAll= new ClassFileImporter().importClasses(TestClassWithOutJunitAsserts.class);

    @Test
    public void shouldNotThrowAnyViolation(){
        assertThatCode(
                () -> classes().should(NoTestIgnoreWithoutCommentRuleTest.notBeIgnoredWithoutAComment()).check(testClassWithoutIgnoreAtAll))
                .doesNotThrowAnyException();

        assertThatCode(
                () -> classes().should(NoTestIgnoreWithoutCommentRuleTest.notBeIgnoredWithoutAComment()).check(testClassWithIgnoreAndComment))
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldThrowViolations(){

        Throwable validationExceptionThrown = catchThrowable(() -> {

            classes().should(NoTestIgnoreWithoutCommentRuleTest.notBeIgnoredWithoutAComment()).check(testClassWithIgnoreButNoComment);

        });

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining("was violated (4 times)")
                .hasMessageContaining(TestClassWithIgnoreAtClassLevel.class.getName()+", at class level")
                .hasMessageContaining(TestClassWithIgnoreAtMethodLevel.class.getName()+" - someIgnoredTestWithoutAComment, at method level")
                .hasMessageContaining(TestClassWithJunit5DisabledAtClassLevel.class.getName()+", at class level")
                .hasMessageContaining(TestClassWithJunit5DisabledAtMethodLevel.class.getName()+" - someDisabledTestWithoutAComment, at method level")

                .hasMessageContaining(NoTestIgnoreWithoutCommentRuleTest.NO_JUNIT_IGNORE_WITHOUT_COMMENT_VIOLATION_MESSAGE);

    }
}
