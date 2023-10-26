package com.goldbach.commons.plugin.service;

import com.goldbach.commons.plugin.Log;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

import static com.goldbach.commons.plugin.utils.ReflectionUtils.*;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

class InvokableRules {
    private final Class<?> rulesLocation;
    private final Set<Field> archRuleFields;
    private final Set<Method> archRuleMethods;

    private final Log log;

    private InvokableRules(String rulesClassName, List<String> ruleChecks, Log log) {

        this.log = log;

        rulesLocation = loadClassWithContextClassLoader(rulesClassName);

        Set<Field> allFieldsWhichAreArchRules = getAllFieldsWhichAreArchRules(rulesLocation.getDeclaredFields());
        Set<Method> allMethodsWhichAreArchRules = getAllMethodsWhichAreArchRules(rulesLocation.getDeclaredMethods());
        validateRuleChecks(Sets.union(allMethodsWhichAreArchRules, allFieldsWhichAreArchRules), ruleChecks);

        Predicate<String> isChosenCheck = ruleChecks.isEmpty() ? check -> true : ruleChecks::contains;

        archRuleFields = filterNames(allFieldsWhichAreArchRules, isChosenCheck);
        archRuleMethods = filterNames(allMethodsWhichAreArchRules, isChosenCheck);

        if (log.isInfoEnabled()) {
            logBuiltInvokableRules(rulesClassName);
        }
    }

    static InvokableRules of(String rulesClassName, List<String> checks, Log log) {
        return new InvokableRules(rulesClassName, checks, log);
    }

    private void logBuiltInvokableRules(String rulesClassName) {

        log.info("just built " + rulesClassName + " : ");

        log.info(archRuleFields.size() + " field rules loaded ");
        archRuleFields.stream().forEach(a -> log.info(a.toString()));

        log.info(archRuleMethods.size() + " method rules loaded");
        archRuleMethods.stream().forEach(a -> log.info(a.toString()));

    }

    private void validateRuleChecks(Set<? extends Member> allFieldsAndMethods, Collection<String> ruleChecks) {
        Set<String> allFieldAndMethodNames = allFieldsAndMethods.stream().map(Member::getName).collect(toSet());
        Set<String> illegalChecks = Sets.difference(ImmutableSet.copyOf(ruleChecks), allFieldAndMethodNames);

        if (!illegalChecks.isEmpty()) {
            throw new IllegalChecksConfigurationException(rulesLocation, illegalChecks);
        }
    }

    private <M extends Member> Set<M> filterNames(Set<M> members, Predicate<String> namePredicate) {
        return members.stream()
                .filter(member -> namePredicate.test(member.getName()))
                .collect(toSet());
    }

    private Set<Method> getAllMethodsWhichAreArchRules(Method[] methods) {
        return stream(methods)
                .filter(m -> m.getParameterCount() == 1 && JavaClasses.class.isAssignableFrom(m.getParameterTypes()[0]))
                .collect(toSet());
    }

    private Set<Field> getAllFieldsWhichAreArchRules(Field[] fields) {
        return stream(fields)
                .filter(f -> ArchRule.class.isAssignableFrom(f.getType()))
                .collect(toSet());
    }

    InvocationResult invokeOn(JavaClasses importedClasses) {

        Object instance = newInstance(rulesLocation);

        if (log.isInfoEnabled()) {
            log.info("applying rules on " + importedClasses.size() + " classe(s). To see the details, enable debug logs");

            if (log.isDebugEnabled()) {
                importedClasses.stream().forEach(c -> log.debug(c.getName()));
            }
        }

        InvocationResult result = new InvocationResult();
        for (Method method : archRuleMethods) {
            checkForFailure(() -> invoke(method, instance, importedClasses))
                    .ifPresent(result::add);
        }
        for (Field field : archRuleFields) {
            ArchRule rule = getValue(field, instance);
            checkForFailure(() -> rule.check(importedClasses))
                    .ifPresent(result::add);
        }
        return result;
    }

    private Optional<String> checkForFailure(Runnable runnable) {
        try {
            runnable.run();
            return Optional.empty();
        } catch (RuntimeException | AssertionError e) {
            return Optional.of(e.getMessage());
        }
    }

    static class InvocationResult {
        private final List<String> violations = new ArrayList<>();

        private void add(String violationMessage) {
            violations.add(violationMessage);
        }

        String getMessage() {
            return violations.stream().collect(joining(lineSeparator()));
        }
    }
}
