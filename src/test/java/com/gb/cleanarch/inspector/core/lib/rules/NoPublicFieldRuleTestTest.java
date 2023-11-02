package com.gb.cleanarch.inspector.core.lib.rules;

import com.gb.cleanarch.inspector.core.lib.SilentLog;
import com.gb.cleanarch.inspector.core.lib.utils.ArchUtils;
import com.goldbach.aut.test.TestSpecificScopeProvider;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;

public class NoPublicFieldRuleTestTest {

	String pathObjectWithNoPublicField = "com/goldbach/aut/main/ObjectWithNoNonStaticPublicField.class";

	String pathObjectWithPublicField = "com/goldbach/aut/main/ObjectWithPublicField.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test(expected = AssertionError.class)
	public void shouldThrowViolations() {

		new NoPublicFieldArchRuleCheck().execute(pathObjectWithPublicField, new TestSpecificScopeProvider(), emptySet());

	}

	@Test
	public void shouldNotThrowAnyViolationEvenWithPublicStaticFinaField() {

		assertThatCode(() -> new NoPublicFieldArchRuleCheck().execute(pathObjectWithNoPublicField,
				new TestSpecificScopeProvider(), emptySet())).doesNotThrowAnyException();

	}

}