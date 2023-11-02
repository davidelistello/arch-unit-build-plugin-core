package com.gb.cleanarch.inspector.core.lib.rules;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.gb.cleanarch.inspector.core.lib.SilentLog;
import com.gb.cleanarch.inspector.core.lib.utils.ArchUtils;
import com.goldbach.aut.test.TestSpecificScopeProvider;
import org.junit.Before;
import org.junit.Test;

public class DontReturnNullCollectionTestArchRuleCheck {

    String pathObjectWithAmethodReturningAnullList = "com/goldbach/aut/main/ObjectWithMethodsReturningNullCollections.class";

    String pathProperlyAnnotatedObjectWithAmethodReturningAlist = "com/goldbach/aut/main/ObjectWithProperlyAnnotatedMethodsReturningCollections.class";

    String pathObjectWithLambdasReturningListsInside = "com/goldbach/aut/main/ObjectWithLambdasReturningListsInside.class";

    String pathObjectWithLombokBuilder = "com/goldbach/aut/main/ObjectWithLombokBuilder.class$ObjectWithLombokBuilderBuilder";

    @Before
    public void setup(){
        //in the normal lifecycle, ArchUtils is instantiated, which enables a static field there to be initialized
        new ArchUtils(new SilentLog());
    }

    @Test
    public void shouldThrowViolations() {

        Throwable validationExceptionThrown = catchThrowable(() ->
            new DontReturnNullCollectionArchRuleCheck().execute(pathObjectWithAmethodReturningAnullList, new TestSpecificScopeProvider(), emptySet())
        );

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageContaining("was violated (2 times)");

    }

    @Test
    public void shouldNotThrowViolations() {

        assertThatCode(() -> new DontReturnNullCollectionArchRuleCheck().execute(pathProperlyAnnotatedObjectWithAmethodReturningAlist, new TestSpecificScopeProvider(),emptySet()))
                .doesNotThrowAnyException();

    }


    @Test
    public void shouldNotThrowViolationsOnLambdas() {

        assertThatCode(() -> new DontReturnNullCollectionArchRuleCheck()
            .execute(pathObjectWithLambdasReturningListsInside, new TestSpecificScopeProvider(),emptySet()))
            .doesNotThrowAnyException();

    }

    @Test
    public void shouldNotThrowViolationsOnClassesUsingLombokBuilder() {

        assertThatCode(() -> new DontReturnNullCollectionArchRuleCheck()
            .execute("com/goldbach/aut/main/lombok_builder", new TestSpecificScopeProvider(),emptySet()))
            .doesNotThrowAnyException();

    }
}
