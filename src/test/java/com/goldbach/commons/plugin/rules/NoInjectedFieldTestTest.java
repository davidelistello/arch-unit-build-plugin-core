package com.goldbach.commons.plugin.rules;

import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import com.goldbach.commons.plugin.aut.main.ClassWithInjectedField;
import com.goldbach.commons.plugin.aut.test.TestSpecificScopeProvider;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NoInjectedFieldTestTest {

	// autowired fields should not trigger injected violations here - there's a separate rule for it 
	private String pathTestClassWithAutowiredFields = "com/goldbach/commons/plugin/aut/main/ClassWithAutowiredField.class";

	private String pathTestClassWithInjectedField = "com/goldbach/commons/plugin/aut/main/ClassWithInjectedField.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoInjectedFieldTest().execute(pathTestClassWithInjectedField, new TestSpecificScopeProvider(),
					emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(ClassWithInjectedField.class.getName())
				.hasMessageContaining(NoInjectedFieldTest.NO_INJECTED_FIELD_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoInjectedFieldTest().execute(pathTestClassWithAutowiredFields,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();
	}

}
