package com.goldbach.commons.plugin.rules;

import com.goldbach.aut.test.TestSpecificScopeProvider;
import com.goldbach.commons.plugin.SilentLog;
import com.goldbach.commons.plugin.utils.ArchUtils;
import org.junit.Before;
import org.junit.Test;

import static com.goldbach.commons.plugin.rules.ConstantsAndStaticNonFinalFieldsNamesArchRuleCheck.CONSTANTS_VIOLATION_MESSAGE;
import static com.goldbach.commons.plugin.rules.ConstantsAndStaticNonFinalFieldsNamesArchRuleCheck.ENUM_CONSTANTS_VIOLATION_MESSAGE;
import static com.goldbach.commons.plugin.rules.ConstantsAndStaticNonFinalFieldsNamesArchRuleCheck.STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConstantsAndStaticNonFinalFieldsNamesArchRuleTestCheck {

	private String pathClassWithConstantNamesNotWrittenCorrectly = "com/goldbach/aut/main/ClassWithConstantNamesNotWrittenCorrectly.class";

	private String pathClassWithConstantNamesWrittenCorrectly = "com/goldbach/aut/main/ClassWithConstantNamesWrittenCorrectly.class";

	private String pathClassWithStaticNonFinalFieldsNotWrittenCorrectly = "com/goldbach/aut/main/ClassWithStaticNonFinalFieldsNotWrittenCorrectly.class";

	private String pathClassWithStaticNonFinalFieldsWrittenCorrectly = "com/goldbach/aut/main/ClassWithStaticNonFinalFieldsWrittenCorrectly.class";

	private String pathEnumWithValuesNotWrittenCorrectly = "com/goldbach/aut/main/EnumWithValuesNotWrittenCorrectly.class";

	private String pathEnumWithValuesWrittenCorrectly = "com/goldbach/aut/main/EnumWithValuesWrittenCorrectly.class";

	@Before
	public void setup() {
		// in the normal lifecycle, ArchUtils is instantiated, which enables a static
		// field there to be initialized
		ArchUtils archUtils = new ArchUtils(new SilentLog());
	}

	@Test
	public void shouldThrowViolationsConstants() {

		assertExceptionIsThrown(pathClassWithConstantNamesNotWrittenCorrectly, CONSTANTS_VIOLATION_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolationConstants() {

		assertNoExceptionIsThrown(pathClassWithConstantNamesWrittenCorrectly);

	}

	@Test
	public void shouldThrowViolationsStaticNonFinalFields() {

		assertExceptionIsThrown(pathClassWithStaticNonFinalFieldsNotWrittenCorrectly,
				STATIC_NON_FINAL_FIELDS_VIOLATION_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolationStaticNonFinalFields() {

		assertNoExceptionIsThrown(pathClassWithStaticNonFinalFieldsWrittenCorrectly);

	}

	@Test
	public void shouldThrowViolationsEnums() {

		assertExceptionIsThrown(pathEnumWithValuesNotWrittenCorrectly, ENUM_CONSTANTS_VIOLATION_MESSAGE);

	}

	@Test
	public void shouldNotThrowAnyViolationEnums() {

		assertNoExceptionIsThrown(pathEnumWithValuesWrittenCorrectly);

	}

	private void assertExceptionIsThrown(String packagePath, String violationMessage) {

		assertThatThrownBy(() -> {
			new ConstantsAndStaticNonFinalFieldsNamesArchRuleCheck().execute(packagePath, new TestSpecificScopeProvider(), emptySet());
		}).hasMessageContaining(violationMessage);

	}

	private void assertNoExceptionIsThrown(String path) {

		assertThatCode(() -> new ConstantsAndStaticNonFinalFieldsNamesArchRuleCheck().execute(path, new TestSpecificScopeProvider(), emptySet()))
				.doesNotThrowAnyException();

	}

}
