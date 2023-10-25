package com.goldbach.commons.plugin.rules;

import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import com.goldbach.commons.plugin.aut.test.TestSpecificScopeProvider;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

public class FinalNonStaticFieldsHaveToBeStaticFinalFieldsRuleTestTest {

	private String pathClassWithFinalNonStaticFields = "com/goldbach/commons/plugin/aut/main/ClassWithFinalNonStaticFields.class";

	private String pathClassWithStaticFinalFields = "com/goldbach/commons/plugin/aut/main/ClassWithStaticFinalFields.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolation() {

		new FinalNonStaticFieldsHaveToBeStaticFinalFieldsRuleTest().execute(pathClassWithFinalNonStaticFields,
				new TestSpecificScopeProvider(), emptySet());

	}

	@Test
	public void shouldNotThrowAnyViolation() {

		assertThatCode(() -> new FinalNonStaticFieldsHaveToBeStaticFinalFieldsRuleTest()
				.execute(pathClassWithStaticFinalFields, new TestSpecificScopeProvider(), emptySet()))
						.doesNotThrowAnyException();

	}

}
