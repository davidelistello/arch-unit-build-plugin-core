package com.gb.cleanarch.inspector.core.lib.rules;

import com.gb.cleanarch.inspector.core.lib.service.ScopePathProvider;
import com.gb.cleanarch.inspector.core.lib.utils.ArchUtils;
import com.tngtech.archunit.lang.ArchRule;

import java.util.Collection;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

/**
 * It is important to respect encapsulation.
 *
 * @see <a href=
 * "https://en.wikipedia.org/wiki/Encapsulation_(computer_programming)">Encapsulation</a>
 */

public class NoPublicFieldArchRuleCheck implements ArchRuleCheck {
    public static final String NO_PUBLIC_FIELD_VIOLATION_MESSAGE = "you should respect encapsulation";

    @Override
    public void execute(String packagePath, ScopePathProvider scopePathProvider, Collection<String> excludedPaths) {

        ArchRule rulePublic = fields().that().areNotStatic().or().areNotFinal().should().notBePublic()
                .allowEmptyShould(true)
                .because(NO_PUBLIC_FIELD_VIOLATION_MESSAGE);

        rulePublic.check(ArchUtils.importAllClassesInPackage(scopePathProvider.getMainClassesPath(), packagePath, excludedPaths));

    }

}
