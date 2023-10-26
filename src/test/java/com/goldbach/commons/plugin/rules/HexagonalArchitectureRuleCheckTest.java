package com.goldbach.commons.plugin.rules;

import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import com.goldbach.aut.test.TestSpecificScopeProvider;
import com.goldbach.commons.plugin.Log;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;


public class HexagonalArchitectureRuleCheckTest {


    private String pathForDomainClassUsingSpring = "com/goldbach/aut/main/myproject/domain/DomainClassUsingSpring.class";

    private String pathForDomainClassAnnotatedWithJson = "com/goldbach/aut/main/myproject/domain/DomainClassAnnotatedWithJson.class";

    private String pathForDomainClassAnnotatedWithLombok = "com/goldbach/aut/main/myproject/domain/DomainClassAnnotatedWithLombok.class";


    private String pathForDomainClassEndingWithDto = "com/goldbach/aut/main/myproject/domain/SomeClassDto.class";

    private String pathForDomainClassEndingWithDTO = "com/goldbach/aut/main/myproject/domain/SomeOtherClassDTO.class";

    private String pathForDomainClassEndingWithVo = "com/goldbach/aut/main/myproject/domain/SomeClassVo.class";

    private String pathForInfraClassUsingSpring = "com/goldbach/aut/main/myproject/infrastructure/InfraClassUsingSpring.class";

    private String pathForInfraClassUsingConfig = "com/goldbach/aut/main/myproject/infrastructure/InfraClassUsingConfig.class";


    private Log silentLogger=new SilentLog();

    @Before
    public void setup(){
        //in the normal lifecycle, ArchUtils is instantiated, which enables a static field there to be initialized
        ArchUtils archUtils=new ArchUtils(new SilentLog());
    }


    @Test
    public void domainClassUsingSpringFrameworkShouldThrowViolations(){

        Throwable validationExceptionThrown = catchThrowable(() -> {

            new HexagonalArchitectureRuleCheck(silentLogger).execute(pathForDomainClassUsingSpring, new TestSpecificScopeProvider(),emptySet());

        });

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining("was violated (1 times)")
                .hasMessageContaining("ClassUsingSpring")
                .hasMessageContaining(HexagonalArchitectureRuleCheck.WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE)
                .hasMessageContaining("domain classes should use only a limited set of core libraries");

    }

    @Test
    public void domainClassAnnotatedWithJacksonShouldThrowViolations(){

        Throwable validationExceptionThrown = catchThrowable(() -> {

            new HexagonalArchitectureRuleCheck(silentLogger).execute(pathForDomainClassAnnotatedWithJson, new TestSpecificScopeProvider(),emptySet());

        });

        assertThat(validationExceptionThrown).as("a violation should have been raised").isNotNull();

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining("was violated (1 times)")
                .hasMessageContaining("ClassAnnotatedWithJson")
                .hasMessageContaining(HexagonalArchitectureRuleCheck.WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE)
                .hasMessageContaining("domain classes should use only a limited set of core libraries");

    }

    @Test
    public void domainClassAnnotatedWithLombokShould_Not_ThrowViolations(){

        assertThatCode(() -> new HexagonalArchitectureRuleCheck(silentLogger).execute(pathForDomainClassAnnotatedWithLombok, new TestSpecificScopeProvider(),emptySet())).doesNotThrowAnyException();

    }

    @Test
    public void infraClassUsingSpringFrameworkShould_Not_ThrowViolations() {

        assertThatCode(() -> new NoPublicFieldArchRuleCheck().execute(pathForInfraClassUsingSpring, new TestSpecificScopeProvider(),emptySet()))
                .doesNotThrowAnyException();

    }

    @Test
    public void infraClassUsingConfigShouldThrowViolations(){

        Throwable validationExceptionThrown = catchThrowable(() -> {

            new HexagonalArchitectureRuleCheck(silentLogger).execute(pathForInfraClassUsingConfig, new TestSpecificScopeProvider(),emptySet());

        });

        assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining("was violated (1 times)")
                .hasMessageContaining("ClassUsingConfig")
                .hasMessageContaining(HexagonalArchitectureRuleCheck.WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE)
                .hasMessageContaining("infrastructure classes should not know about config code");

    }

    @Test
    public void domainClassEndingWithDtoIgnoringCaseShouldThrowViolation(){

        Throwable validationExceptionThrownForDto = catchThrowable(() -> {

            new HexagonalArchitectureRuleCheck(silentLogger).execute(pathForDomainClassEndingWithDto, new TestSpecificScopeProvider(),emptySet());

        });

        assertThat(validationExceptionThrownForDto).as("expecting a violation to be raised for SomeClassDto in domain").isNotNull();
        assertViolationFor(validationExceptionThrownForDto,"SomeClassDto");

        Throwable validationExceptionThrownForDTO = catchThrowable(() -> {

            new HexagonalArchitectureRuleCheck(silentLogger).execute(pathForDomainClassEndingWithDTO, new TestSpecificScopeProvider(),emptySet());

        });
        assertThat(validationExceptionThrownForDTO).as("expecting a violation to be raised for SomeOtherClassDTO in domain").isNotNull();
        assertViolationFor(validationExceptionThrownForDTO,"SomeOtherClassDTO");
    }

    @Test
    public void domainClassEndingWithVoShouldThrowViolation(){

        Throwable validationExceptionThrownForDto = catchThrowable(() -> {

            new HexagonalArchitectureRuleCheck(silentLogger).execute(pathForDomainClassEndingWithVo, new TestSpecificScopeProvider(),emptySet());

        });

        assertThat(validationExceptionThrownForDto).as("expecting a violation to be raised for SomeClassVo in domain").isNotNull();
        assertViolationFor(validationExceptionThrownForDto,"SomeClassVo");

    }

    private void assertViolationFor(Throwable violation, String className){

        assertThat(violation).isInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Architecture Violation")
                .hasMessageContaining("was violated (1 times)")
                .hasMessageContaining(className)
                .hasMessageContaining(HexagonalArchitectureRuleCheck.WHEN_FOLLOWING_HEXAGONAL_ARCHITECTURE)
                .hasMessageContaining("DTO / VO classes shouldn't be located in domain, as they are not business oriented");
    }

}
