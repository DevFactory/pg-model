package io.polyglotted.pgmodel.ac;

import org.testng.annotations.Test;

import static io.polyglotted.pgmodel.EqualityChecker.verifyEqualsHashCode;
import static io.polyglotted.pgmodel.ac.AccessRole.ADMINISTRATOR;
import static io.polyglotted.pgmodel.ac.AccessRole.CONSUMER;
import static io.polyglotted.pgmodel.ac.AccessRole.CURATOR;
import static io.polyglotted.pgmodel.ac.Condition.conditionBuilder;
import static io.polyglotted.pgmodel.ac.Effect.ALLOW;
import static io.polyglotted.pgmodel.ac.Effect.DENY;
import static io.polyglotted.pgmodel.ac.Function.BETWEEN;
import static io.polyglotted.pgmodel.ac.Function.GREATER_THAN;
import static io.polyglotted.pgmodel.ac.Function.LESS_THAN;
import static io.polyglotted.pgmodel.ac.Policy.policyBuilder;
import static io.polyglotted.pgmodel.ac.Rule.ruleBuilder;
import static io.polyglotted.pgmodel.ac.Subject.subjectBuilder;
import static io.polyglotted.pgmodel.ac.SubjectAttribute.ACCESS_TOKEN;
import static io.polyglotted.pgmodel.ac.SubjectAttribute.CREDENTIAL;
import static org.testng.Assert.assertEquals;

public class AcObjectsTest {

    @Test
    public void conditionEqHash() {
        Condition orig = condition();
        Condition copy = condition();
        Condition other1 = conditionBuilder().attribute("b").function(BETWEEN).value("b").negate(true).build();
        verifyEqualsHashCode(orig, copy, other1);
    }

    @Test
    public void policyEqHash() {
        Policy orig = policy();
        Policy copy = policy();
        Policy other1 = policyBuilder().urn("testco:policy:2017").resource("*")
           .role(CONSUMER).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(LESS_THAN).value("d"))).build();
        Policy other2 = policyBuilder().urn("testco:policy:2015").resource("abc/def")
           .role(CONSUMER).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(LESS_THAN).value("d"))).build();
        Policy other3 = policyBuilder().urn("testco:policy:2015").resource("*")
           .role(ADMINISTRATOR).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(LESS_THAN).value("d"))).build();
        Policy other4 = policyBuilder().urn("testco:policy:2015").resource("*")
           .role(CONSUMER).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(GREATER_THAN).value("d")).build()).build();
        Policy other5 = policyBuilder().urn("testco:policy:2015").resource("*")
           .role(CONSUMER).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
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
           .role(CURATOR).rule(ruleBuilder().effect(DENY).condition(conditionBuilder()
              .attribute("b").function(LESS_THAN).value("d"))).build();
    }

    public static Rule rule() {
        return ruleBuilder().effect(ALLOW).condition(condition()).build();
    }

    public static Condition condition() {
        return conditionBuilder().attribute("a").function(BETWEEN).value("b").negate(true).build();
    }

    public static Subject subject() {
        return subjectBuilder().principal("tester").attribute("name", "unit tester").build();
    }
}