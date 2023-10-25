package com.goldbach.commons.plugin.rules;

import com.goldbach.aut.main.ClassWithInjectedField;
import com.goldbach.aut.test.TestSpecificScopeProvider;
import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import org.junit.Before;
import org.junit.Test;

import static com.goldbach.commons.plugin.rules.NoInjectedFieldTest.NO_INJECTED_FIELD_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NoInjectedFieldTestTest {

	// autowired fields should not trigger injected violations here - there's a separate rule for it 
	private String pathTestClassWithAutowiredFields = "com/goldbach/aut/main/ClassWithAutowiredField.class";

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

			new NoInjectedFieldTest().execute(pathTestClassWithInjectedField, new TestSpecificScopeProvider(),
					emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(ClassWithInjectedField.class.getName())
				.hasMessageContaining(NO_INJECTED_FIELD_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoInjectedFieldTest().execute(pathTestClassWithAutowiredFields,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();
	}

}
