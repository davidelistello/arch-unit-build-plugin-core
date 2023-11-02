package com.gb.cleanarch.inspector.core.lib.rules;

import com.gb.cleanarch.inspector.core.lib.service.ScopePathProvider;
import com.gb.cleanarch.inspector.core.lib.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchRule;

import javax.annotation.Nonnull;
import java.util.Collection;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

/**
 * Returning null collections (List, Set) forces the caller to always perform a null check, which hinders readability. It's much better to never return a null Collection, and instead return an empty one.
 * This rule enforces that all methods returning a Collection must be annotated with @Nonnull
 *
 * @see <a href= "https://stackoverflow.com/questions/4963300/which-notnull-java-annotation-should-i-use/">there is no agreed standard for notNull annotation</a>
 */
public class DontReturnNullCollectionArchRuleCheck implements ArchRuleCheck {

    protected static final String NO_NULL_COLLECTION_MESSAGE = "we don't want callers to perform null check every time. Return an empty collection, not null. Please annotate the method with " + Nonnull.class.getCanonicalName();
    DescribedPredicate<JavaMethod> areNotLambdas =
            new DescribedPredicate<JavaMethod>("are not lambda") {
                @Override
                public boolean test(JavaMethod input) {

                    return !input.getName().contains("lambda$new");

                }
            };
    DescribedPredicate<JavaMethod> returnCollections =
            new DescribedPredicate<JavaMethod>("return collections") {
                @Override
                public boolean test(JavaMethod input) {

                    JavaClass returnedJavaClass = input.getReturnType().toErasure();

                    return returnedJavaClass.getAllRawInterfaces().stream()
                            .anyMatch(implementedInterface -> implementedInterface.isAssignableTo(Collection.class));

                }
            };

    @Override
    public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

        JavaClasses classesToCheck = ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(), packagePath, excludedPaths);

        ArchRule rule = methods().that(returnCollections).and(areNotLambdas)
                .should().beAnnotatedWith(Nonnull.class)
                .because(NO_NULL_COLLECTION_MESSAGE)
                .allowEmptyShould(true);

        rule.check(classesToCheck);
    }


}