package ch.lambdaj.function.argument;

import ch.lambdaj.demo.Car;
import ch.lambdaj.demo.Person;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.project;
import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

/**
 * @author Mario Fusco
 */
public class InvocationSequenceTest {

    @Test
    public void verifyEquals() throws NoSuchMethodException {
        final InvocationSequence invocationSequenceCar = new InvocationSequence(Car.class);
        final InvocationSequence invocationSequencePerson = new InvocationSequence(Person.class);
        assertFalse(invocationSequenceCar.equals(invocationSequencePerson));
        assertFalse(invocationSequenceCar.hashCode() == invocationSequencePerson.hashCode());
    }

    @Test
    public void testProject() {
        Collection<Car> cars = asList(new Car("Ford", "F150", 2011, 200));
        assertThat(project(cars, SimpleBrand.class, on(Car.class)), hasItem(new SimpleBrand("Ford")));
        assertThat(project(cars, SimpleBrand.class, on(Car.class).getBrand()), hasItem(new SimpleBrand("Ford")));
    }

    public static class SimpleBrand {
        private String brand;

        public SimpleBrand(Car car) {
            this.brand = car.getBrand();
        }

        public SimpleBrand(String brand) {
            this.brand = brand;
        }

        public String getBrand() {
            return brand;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final SimpleBrand that = (SimpleBrand) o;

            if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return brand != null ? brand.hashCode() : 0;
        }
    }
}
