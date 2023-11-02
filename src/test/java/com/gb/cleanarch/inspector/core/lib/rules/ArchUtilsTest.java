package com.gb.cleanarch.inspector.core.lib.rules;

import java.util.Arrays;

import com.gb.cleanarch.inspector.core.lib.model.RootClassFolder;
import com.gb.cleanarch.inspector.core.lib.utils.ArchUtils;
import com.gb.cleanarch.inspector.core.lib.SilentLog;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * These are not great test, as they will fail when new classes are added in the
 * code base, but acceptable for now
 */
public class ArchUtilsTest {

	// used to define the absolute ClassPath for scanning our internal Base-Class used in the Polymorphic-Plugins-Discovery-Design
	private static final String PACKAGE_NAMESPACE_MODEL_FULL_PATH = "com/gb/cleanarch/inspector/core/lib/model";

	// instantiating to init the static logger in ArchUtils..
	ArchUtils archUtils = new ArchUtils(new SilentLog());

	@Test
	public void shouldLoadClassesFromGivenPackage() {
		JavaClasses classes = ArchUtils.importAllClassesInPackage(new RootClassFolder("./target/classes/"),
				PACKAGE_NAMESPACE_MODEL_FULL_PATH);

		long noOfClassesInPackage = classes.stream().count();

		assertThat(noOfClassesInPackage).isEqualTo(4);
	}

	@Test
	public void shouldLoadAllClassesWhenGivenPackageDoesntExist() {
		JavaClasses classes = ArchUtils.importAllClassesInPackage(new RootClassFolder("./target/classes"), "someNotExistingFolder");

		long noOfClasses = classes.stream().filter(it -> !it.isNestedClass()).count();

		assertThat(noOfClasses).isEqualTo(23);
	}

	@Test
	public void shouldIgnoreClassesFromConfiguredPaths() {

		JavaClasses classes = ArchUtils.importAllClassesInPackage(new RootClassFolder("./target"), "");

		assertThat(classes).isNotEmpty();

		JavaClass classToExclude = classes.stream()
				.filter(c -> c.getSource().get().getUri().toString().contains("ClassToExclude")).findFirst().get();
		assertThat(classToExclude).as("when no exclusion pattern configured, ClassToExclude should be found")
				.isNotNull();

		JavaClasses classesWithTestClassesExclusions = ArchUtils.importAllClassesInPackage(new RootClassFolder("./target"), "",
				Arrays.asList("test-classes"));

		assertThat(containsClassWithPattern(classesWithTestClassesExclusions, "ClassToExclude"))
				.as("when 'test-classes' pattern configured, ClassToExclude should still be found").isTrue();

		assertThat(classes.size()).as("There should be less classes loaded when we apply the test-classes exclusion")
				.isGreaterThan(classesWithTestClassesExclusions.size());

		JavaClasses classesWithTestClassesAndSpecificExclusions = ArchUtils.importAllClassesInPackage(new RootClassFolder("./target"), "",
				Arrays.asList("test-classes", "ClassToExclude"));

		assertThat(containsClassWithPattern(classesWithTestClassesAndSpecificExclusions, "ClassToExclude"))
				.as("when 'ClassToExclude' pattern configured, ClassToExclude should not  be found").isFalse();

		assertThat(classesWithTestClassesAndSpecificExclusions.size() + 1)
				.as("with a specific exclusion; we should have one less class than without")
				.isEqualTo(classesWithTestClassesExclusions.size());

	}

	private boolean containsClassWithPattern(JavaClasses javaClassesToTest, String pattern) {

		return javaClassesToTest.stream().filter(c -> c.getSource().get().getUri().toString().contains(pattern))
				.findFirst().isPresent();
	}
}
