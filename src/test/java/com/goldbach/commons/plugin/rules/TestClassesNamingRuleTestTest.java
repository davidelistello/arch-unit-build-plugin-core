package com.goldbach.commons.plugin.rules;

import com.goldbach.aut.test.TestSpecificScopeProvider;
import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

public class TestClassesNamingRuleTestTest {

	private String pathTestClassWithIncorrectName1 = "com/goldbach/aut/test/ClassTestWithIncorrectName1.class";

	private String pathTestClassWithIncorrectName2 = "com/goldbach/aut/test/ClassTestWithIncorrectName2.class";

	private String pathClassWithCorrectName1Test = "com/goldbach/aut/test/ClassTestWithCorrectName1Test.class";

	private String pathClassWithCorrectName2Test = "com/goldbach/aut/test/ClassTestWithCorrectName2Test.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation1Test() {

		new TestClassesNamingRuleTest().execute(pathTestClassWithIncorrectName1, new TestSpecificScopeProvider(),
				emptySet());

	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation2Test() {

		new TestClassesNamingRuleTest().execute(pathTestClassWithIncorrectName2, new TestSpecificScopeProvider(),
				emptySet());

	}

	@Test
	public void shouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName1Test,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

		assertThatCode(() -> new TestClassesNamingRuleTest().execute(pathClassWithCorrectName2Test,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

	}

}
