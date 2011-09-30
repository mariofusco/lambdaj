package ch.lambdaj.proxy.nodefconstructor.problem;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectFirst;
import static ch.lambdaj.function.matcher.HasArgumentWithValue.havingValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class RelationSmellQuery {

    private List<RelationSmell> smells = new ArrayList<RelationSmell>();

    public RelationSmell getSmellForID(final ElementId from, final ElementId to) {
        return selectFirst(smells,
                havingValue(on(RelationSmell.class).isForRelation(from, to)));
        /*
         * return Collections2.findFirstOrDefault(smells, new
         * Predicate<RelationSmell>() {
         * public boolean apply(final RelationSmell smell) {
         * return smell.isForRelation(from, to);
         * }});
         */
    }

    @Test
    public void when_selecting_first_element_for_relation_smell_query_should_not_throw() {
        RelationSmell smell_between_1_and_1 =
            new RelationSmell(new ElementId("1"), new ElementId("2"));

        RelationSmell smell_between_3_and_4 =
            new RelationSmell(new ElementId("3"), new ElementId("4"));

        smells = Arrays.asList(
                smell_between_1_and_1,
                smell_between_3_and_4,
                smell_between_1_and_1);

        ElementId from = new ElementId("3");
        ElementId to = new ElementId("4");

        RelationSmell smell = getSmellForID(from, to);

        assertThat(smell, is(smell_between_3_and_4));
    }

    public boolean hasSmellForID(final ElementId from, final ElementId to) {
        return getSmellForID(from, to) != null;
    }

    public RelationSmellQuery() {
    }
}
