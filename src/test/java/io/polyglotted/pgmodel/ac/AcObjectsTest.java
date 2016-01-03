package io.polyglotted.pgmodel.ac;

import com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;

import java.time.Clock;
import java.time.ZoneOffset;

import static io.polyglotted.pgmodel.EqualityChecker.verifyEqualsHashCode;
import static io.polyglotted.pgmodel.ac.AccessRole.ADMINISTRATOR;
import static io.polyglotted.pgmodel.ac.AccessRole.CONSUMER;
import static io.polyglotted.pgmodel.ac.AccessRole.CURATOR;
import static io.polyglotted.pgmodel.ac.Condition.conditionBuilder;
import static io.polyglotted.pgmodel.ac.Effect.ALLOW;
import static io.polyglotted.pgmodel.ac.Effect.DENY;
import static io.polyglotted.pgmodel.ac.Function.*;
import static io.polyglotted.pgmodel.ac.Function.LESS_THAN;
import static io.polyglotted.pgmodel.ac.Policy.policyBuilder;
import static io.polyglotted.pgmodel.ac.Rule.ruleBuilder;
import static io.polyglotted.pgmodel.ac.Subject.subjectBuilder;
import static io.polyglotted.pgmodel.ac.SubjectAttribute.ACCESS_TOKEN;
import static io.polyglotted.pgmodel.ac.SubjectAttribute.CREDENTIAL;
import static java.time.Instant.ofEpochMilli;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AcObjectsTest {
    private static final Clock TEST_CLOCK = Clock.fixed(ofEpochMilli(1442955118895L), ZoneOffset.UTC);

    @Test
    public void conditionEqHash() {
        Condition orig = condition();
        Condition copy = condition();
        Condition other1 = conditionBuilder().attribute("b").function(BETWEEN).value("b").negate(true).build();
        verifyEqualsHashCode(orig, copy, other1);
    }

    @Test
    public void environmentEqHash() {
        Environment orig = environment();
        Environment copy = environment();
        Environment other1 = Environment.from(ImmutableMap.of("a", 2),
           Clock.fixed(ofEpochMilli(1442955118895L), ZoneOffset.UTC));
        verifyEqualsHashCode(orig, copy, other1);
    }

    @Test
    public void accessContextEqHash() {
        AccessContext orig = new AccessContext(subjectBuilder().principal("mgr").build(),
           Environment.from(ImmutableMap.of("API_REF", 1, "RESOURCE", "b"), TEST_CLOCK));
        AccessContext copy = new AccessContext(subjectBuilder().principal("mgr").build(),
           Environment.from(ImmutableMap.of("API_REF", 1, "RESOURCE", "b"), TEST_CLOCK));
        AccessContext other1 = new AccessContext(subjectBuilder().principal("sub").build(),
           Environment.from(ImmutableMap.of("API_REF", 1, "RESOURCE", "b"), TEST_CLOCK));
        AccessContext other2 = new AccessContext(subjectBuilder().principal("mgr").build(),
           Environment.from(ImmutableMap.of("API_REF", 2, "RESOURCE", "d"), TEST_CLOCK));
        verifyEqualsHashCode(orig, copy, other1, other2);

        for (AccessContext context : asList(orig, copy, other1, other2)) {
            assertNotNull(context.apiRef());
            assertNotNull(context.resource());
        }
    }

    @Test
    public void policyEqHash() {
        Policy orig = policy();
        Policy copy = policy();
        Policy other1 = policyBuilder().urn("testco:policy:2017").resource("*")
           .roles(CONSUMER, CURATOR).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(LESS_THAN).value("d"))).build();
        Policy other2 = policyBuilder().urn("testco:policy:2015").resource("abc/def")
           .roles(CONSUMER, CURATOR).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(LESS_THAN).value("d"))).build();
        Policy other3 = policyBuilder().urn("testco:policy:2015").resource("*")
           .roles(ADMINISTRATOR).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(LESS_THAN).value("d"))).build();
        Policy other4 = policyBuilder().urn("testco:policy:2015").resource("*")
           .roles(CONSUMER, CURATOR).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(GREATER_THAN).value("d")).build()).build();
        Policy other5 = policyBuilder().urn("testco:policy:2015").resource("*")
           .roles(CONSUMER, CURATOR).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(LESS_THAN).value("d"))).defaultEffect(ALLOW).build();
        verifyEqualsHashCode(orig, copy, other1, other2, other3, other4, other5);
    }

    @Test
    public void ruleEqHash() {
        Rule orig = rule();
        Rule copy = rule();
        Rule other1 = ruleBuilder().effect(DENY).condition(condition()).build();
        Rule other2 = ruleBuilder().effect(Effect.valueOf("ALLOW")).condition(conditionBuilder()
           .attribute("a").function(Function.valueOf("REGEX")).value("b").negate(true).build()).build();
        verifyEqualsHashCode(orig, copy, other1, other2);
    }

    @Test
    public void subjectEqHash() {
        Subject orig = subject();
        Subject copy = subject();
        Subject other1 = subjectBuilder().principal("tester2").attribute("DISPLAY_NAME", "unit tester")
           .attribute(CREDENTIAL, "zzcss").attribute(ACCESS_TOKEN, "bbntre").build();
        verifyEqualsHashCode(orig, copy, other1);
        assertEquals(other1.credential(), "zzcss");
        assertEquals(other1.token(), "bbntre");
    }

    public static Policy policy() {
        return policyBuilder().urn("testco:policy:2015").resource("*")
           .roles(CONSUMER, CURATOR).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(LESS_THAN).value("d"))).build();
    }

    public static Rule rule() {
        return ruleBuilder().effect(ALLOW).condition(condition()).build();
    }

    public static Condition condition() {
        return conditionBuilder().attribute("a").function(BETWEEN).value("b").negate(true).build();
    }

    public static Environment environment() {
        return Environment.from(ImmutableMap.of("a", "aa", "b", true, "c", 25),
           Clock.fixed(ofEpochMilli(1442955118895L), ZoneOffset.UTC));
    }

    public static Subject subject() {
        return subjectBuilder().principal("tester").attribute("name", "unit tester").build();
    }
}