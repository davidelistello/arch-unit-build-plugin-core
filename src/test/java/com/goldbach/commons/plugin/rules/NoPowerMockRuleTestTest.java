package com.goldbach.commons.plugin.rules;

import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import com.goldbach.commons.plugin.aut.test.TestClassWithPowerMock;
import com.goldbach.commons.plugin.aut.test.TestSpecificScopeProvider;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NoPowerMockRuleTestTest {

	// testClassWithoutPowerMock
	private String pathTestClassWithOutJunitAsserts = "com/goldbach/commons/plugin/aut/test/TestClassWithOutJunitAsserts.class";

	private String pathTestClassWithPowerMock = "com/goldbach/commons/plugin/aut/test/TestClassWithPowerMock.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldNotThrowAnyViolation() {
		assertThatCode(() -> new NoPowerMockRuleTest().execute(pathTestClassWithOutJunitAsserts,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

	}

	@Test
	public void shouldThrowViolations() {

		Throwable validationExceptionThrown = catchThrowable(() -> {

			new NoPowerMockRuleTest().execute(pathTestClassWithPowerMock, new TestSpecificScopeProvider(), emptySet());

		});

		assertThat(validationExceptionThrown).isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Architecture Violation").hasMessageContaining("was violated (1 times)")
				.hasMessageContaining(TestClassWithPowerMock.class.getName())
				.hasMessageContaining(NoPowerMockRuleTest.POWER_MOCK_VIOLATION_MESSAGE);

	}

}
