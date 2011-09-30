package ch.lambdaj.proxy.nodefconstructor;

import java.util.Arrays;

import org.junit.*;
import static ch.lambdaj.Lambda.*;
import static org.hamcrest.CoreMatchers.*;

public class ProxyNoDefaultConstructorTest {

    @Test
    public void testProxyNoDefaultConstructor() {
        RelationMetricResult firstSuspect = new RelationMetricResult(new ElementId("1"), new ElementId("2"), 1.0);
        RelationMetricResult secondSuspect = new RelationMetricResult(new ElementId("2"), new ElementId("3"), 2.0);
        Iterable<RelationMetricResult> suspects = Arrays.asList(firstSuspect, secondSuspect);

        RelationMetricResult selected = selectFirst(suspects,
                having(on(RelationMetricResult.class).from().intValue(),
                equalTo(2)));
        assert(secondSuspect.equals(selected));
    }
}


