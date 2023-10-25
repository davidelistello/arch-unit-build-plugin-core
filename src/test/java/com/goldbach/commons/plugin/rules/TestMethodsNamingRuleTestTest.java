package com.goldbach.commons.plugin.rules;

import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import com.goldbach.commons.plugin.aut.test.TestSpecificScopeProvider;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

public class TestMethodsNamingRuleTestTest {

	private String pathMethodWithIncorrectNameTest = "com/goldbach/commons/plugin/aut/test/MethodWithIncorrectNameTest.class";

	private String pathMethodWithCorrectNameStartingWithShouldTest = "com/goldbach/commons/plugin/aut/test/MethodWithCorrectNameStartingWithShouldTest.class";

	private String pathMethodWithCorrectNameStartingWithTestTest = "com/goldbach/commons/plugin/aut/test/MethodWithCorrectNameStartingWithTestTest.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation() {

		new TestMethodsNamingRuleTest().execute(pathMethodWithIncorrectNameTest, new TestSpecificScopeProvider(),
				emptySet());

	}

	@Test
	public void testShouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new TestMethodsNamingRuleTest().execute(pathMethodWithCorrectNameStartingWithShouldTest,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestMethodsNamingRuleTest().execute(pathMethodWithCorrectNameStartingWithTestTest,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

	}

}
