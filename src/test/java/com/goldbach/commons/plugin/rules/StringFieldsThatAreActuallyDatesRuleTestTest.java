package com.goldbach.commons.plugin.rules;

import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import com.goldbach.aut.test.TestSpecificScopeProvider;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

public class StringFieldsThatAreActuallyDatesRuleTestTest {

	private String pathClassWithStringsEndingWithTheWordDate = "com/goldbach/aut/main/ClassWithStringsEndingWithTheWordDate.class";

	private String pathClassWithoutStringsEndingWithTheWordDate = "com/goldbach/aut/main/ClassWithoutStringsEndingWithTheWordDate.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolationTest() {

		new StringFieldsThatAreActuallyDatesRuleTest().execute(pathClassWithStringsEndingWithTheWordDate,
				new TestSpecificScopeProvider(), emptySet());

	}

	@Test
	public void shouldNotThrowAnyViolationTest() {

		assertThatCode(() -> new StringFieldsThatAreActuallyDatesRuleTest()
				.execute(pathClassWithoutStringsEndingWithTheWordDate, new TestSpecificScopeProvider(), emptySet()))
						.doesNotThrowAnyException();

	}

}
