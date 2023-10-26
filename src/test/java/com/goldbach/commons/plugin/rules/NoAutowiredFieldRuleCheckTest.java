package com.goldbach.commons.plugin.rules;

import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import com.goldbach.aut.main.ClassWithAutowiredField;
import com.goldbach.aut.test.TestSpecificScopeProvider;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NoAutowiredFieldRuleCheckTest {

	private String pathTestClassWithAutowiredField = "com/goldbach/aut/main/ClassWithAutowiredField.class";

	// injected fields should not trigger autowired violation - they have their own rule
	private String pathTestClassWithInjectedField = "com/goldbach/aut/main/ClassWithInjectedField.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoAutowiredFieldArchRuleCheck().execute(pathTestClassWithAutowiredField, new TestSpecificScopeProvider(),
					emptySet());
		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(ClassWithAutowiredField.class.getName())
				.hasMessageContaining(NoAutowiredFieldArchRuleCheck.NO_AUTOWIRED_FIELD_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoAutowiredFieldArchRuleCheck().execute(pathTestClassWithInjectedField,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();
	}

}
