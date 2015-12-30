package io.polyglotted.pgmodel.search;

import io.polyglotted.pgmodel.search.query.Expression;
import io.polyglotted.pgmodel.search.query.Expressions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.polyglotted.pgmodel.util.ModelUtil.deserialize;
import static io.polyglotted.pgmodel.util.ModelUtil.serialize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ExpressionsTest extends Expressions {
    @DataProvider
    public static Object[][] expressionInputs() {
        return new Object[][]{
           {all()},
           {ids("ab", "cd")},
           {equalsTo("hello", "world")},
           {greaterThanEquals("hello", "world")},
           {greaterThan("hello", "world")},
           {lessThanEquals("hello", "world")},
           {lessThan("hello", "world")},
           {prefix("hello", "world")},
           {notEquals("hello", "world")},
           {in("hello", "foo", "bar")},
           {in("hello", 2.5, 3.2)},
           {between("hello", "foo", "bar")},
           {textAnywhere("hello")},
           {textAnywhere("a", "hello")},
           {regex("hello", "wor*")},
           {exists("hello")},
           {missing("hello")},
           {type("hello")},
           {json("{\"query\":{\"match_phrase\":{\"_all\":{\"query\":\"commodity\",\"slop\":20}}}}")},
           {and(equalsTo("hello", "world"))},
           {liveIndex()},
           {archiveIndex()},
           {allIndex()},
           {approvalStatus()},
           {or(equalsTo("hello", "world"))},
           {not(equalsTo("hello", "world"))},
           {nested("foo.bar", equalsTo("hello", "world"))},
           {hasParent("foo.bar", equalsTo("hello", "world"))},
           {hasChild("foo.bar", equalsTo("hello", "world"))},
        };
    }

    @Test(dataProvider = "expressionInputs")
    public void expressionValid(Expression expression) throws Exception {
        String serialize = serialize(expression);
        Expression actual = deserialize(serialize, Expression.class);
        assertThat(actual, is(expression));
    }
}