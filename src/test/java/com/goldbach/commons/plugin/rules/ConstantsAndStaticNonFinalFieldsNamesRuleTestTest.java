package com.goldbach.commons.plugin.rules;

import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import com.goldbach.commons.plugin.aut.test.TestSpecificScopeProvider;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConstantsAndStaticNonFinalFieldsNamesRuleTestTest {

	private String pathClassWithConstantNamesNotWrittenCorrectly = "com/goldbach/commons/plugin/aut/main/ClassWithConstantNamesNotWrittenCorrectly.class";

	private String pathClassWithConstantNamesWrittenCorrectly = "com/goldbach/commons/plugin/aut/main/ClassWithConstantNamesWrittenCorrectly.class";

	private String pathClassWithStaticNonFinalFieldsNotWrittenCorrectly = "com/goldbach/commons/plugin/aut/main/ClassWithStaticNonFinalFieldsNotWrittenCorrectly.class";

	private String pathClassWithStaticNonFinalFieldsWrittenCorrectly = "com/goldbach/commons/plugin/aut/main/ClassWithStaticNonFinalFieldsWrittenCorrectly.class";

	private String pathEnumWithValuesNotWrittenCorrectly = "com/goldbach/commons/plugin/aut/main/EnumWithValuesNotWrittenCorrectly.class";

	private String pathEnumWithValuesWrittenCorrectly = "com/goldbach/commons/plugin/aut/main/EnumWithValuesWrittenCorrectly.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolationsConstants() {

		assertExceptionIsThrown(pathClassWithConstantNamesNotWrittenCorrectly, ConstantsAndStaticNonFinalFieldsNamesRuleTest.CONSTANTS_VIOLATION_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolationConstants() {

		assertNoExceptionIsThrown(pathClassWithConstantNamesWrittenCorrectly);

	}

	@Test
	public void shouldThrowViolationsStaticNonFinalFields() {

		assertExceptionIsThrown(pathClassWithStaticNonFinalFieldsNotWrittenCorrectly,
				ConstantsAndStaticNonFinalFieldsNamesRuleTest.STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolationStaticNonFinalFields() {

		assertNoExceptionIsThrown(pathClassWithStaticNonFinalFieldsWrittenCorrectly);

	}

	@Test
	public void shouldThrowViolationsEnums() {

		assertExceptionIsThrown(pathEnumWithValuesNotWrittenCorrectly, ConstantsAndStaticNonFinalFieldsNamesRuleTest.ENUM_CONSTANTS_VIOLATION_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolationEnums() {

		assertNoExceptionIsThrown(pathEnumWithValuesWrittenCorrectly);

	}

	private void assertExceptionIsThrown(String packagePath, String violationMessage) {

		assertThatThrownBy(() -> {
			new ConstantsAndStaticNonFinalFieldsNamesRuleTest().execute(packagePath, new TestSpecificScopeProvider(), emptySet());
		}).hasMessageContaining(violationMessage);

	}

	private void assertNoExceptionIsThrown(String path) {

		assertThatCode(() -> new ConstantsAndStaticNonFinalFieldsNamesRuleTest().execute(path, new TestSpecificScopeProvider(), emptySet()))
				.doesNotThrowAnyException();

	}

}
