package ch.lambdaj.collection;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.function.aggregate.Money.money;
import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;

import java.util.*;

import ch.lambdaj.function.matcher.*;
import junit.framework.*;
import org.hamcrest.*;
import org.junit.Test;

import ch.lambdaj.function.aggregate.Money;
import ch.lambdaj.mock.Person;
import static ch.lambdaj.collection.LambdaCollections.with;

public class LambdaListTest {

	@Test
	public void test1() {
		List<Integer> result = with(asList(1, 2, 3, 4, 5)).retain(greaterThan(3));
		assertNotNull(result);
		assertEquals(2, result.size());
	}

    @Test
    public void testSet() {
        Set set = new HashSet();
        set.addAll(with(1, 2, 3, 4, 5));

        LambdaSet<Integer> lambdaSet = with(set);
        LambdaSet<Integer> lambdaSet2 = lambdaSet.clone();
        assertEquals(lambdaSet.hashCode(), lambdaSet2.hashCode());
        assertEquals(lambdaSet, lambdaSet2);

        lambdaSet.retain(greaterThan(2));
        assertEquals(3, lambdaSet.size());
        lambdaSet.remove(Odd);
        assertEquals(1, lambdaSet.size());
    }

    @Test
    public void testIterable() {
        LambdaIterable<Integer> iterable = with(new TestIterable<Integer>(asList(1, 2, 3, 4, 5)));
        LambdaIterable<Integer> iterable2 = iterable.clone().retain(greaterThan(3));
        assertFalse(iterable.equals(iterable2));
        assertEquals(1, (int)iterable.iterator().next());
        assertEquals(4, (int)iterable2.iterator().next());
    }

    private static class TestIterable<T> implements Iterable<T> {
        private Iterable<T> iterable;
        TestIterable(Iterable<T> iterable) { this.iterable = iterable; }
        public Iterator<T> iterator() { return iterable.iterator(); }
    }

	Matcher<Integer> Odd = new TypeSafeMatcher<Integer>() {
		public boolean matchesSafely(Integer item) {
			return item % 2 == 1;
		}

		public void describeTo(Description description) {
			description.appendText("odd()");
		}
	};

	@Test
	public void test2() {
		List<Integer> result = with(asList(1, 2, 3, 4, 5)).retain(Odd);
		assertNotNull(result);
		assertEquals(3, result.size());
	}

	Person me = new Person("Mario", "Fusco", 35);
	Person luca = new Person("Luca", "Marrocco", 29);
	Person biagio = new Person("Biagio", "Beatrice", 39);
	Person celestino = new Person("Celestino", "Bellone", 29);
	List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

	@Test
	public void test3() {
		List<Person> oldFriends = with(meAndMyFriends).retain(having(on(Person.class).getAge(), greaterThan(30)));
		assertNotNull(oldFriends);
		assertEquals(2, oldFriends.size());
	}

    @Test
    public void testPredicate() {
        List<Person> oldFriends = with(meAndMyFriends).retain(new Predicate<Person>() {
            @Override
            public boolean apply(Person person) { return person.getAge() > 30; }
        });
        assertNotNull(oldFriends);
        assertEquals(2, oldFriends.size());
    }

	@Test
	public void test4() {
		Premium endorsementPremium1 = premiumOf("1,311,314,330.89 GBP");
		Premium endorsementPremium2 = premiumOf("96,563,001.19 GBP");
		Premium endorsementPremium3 = premiumOf("4,443,725.16 GBP");

		List<Premium> premiums = asList(endorsementPremium1, endorsementPremium2, endorsementPremium3);

		Money total = with(premiums).aggregate(on(Premium.class).getPremium(), new Money.MoneyAggregator());

		assertEquals(money("1,412,321,057.24 GBP").getValue(), total.getValue(), 0.000001);
	}

	@Test
	public void testConcatNull1() {
		String result = with(asList(null, "a")).join();
		assertEquals(result, "a");
	}

	@Test
	public void testConcatNull2() {
		String result = with(Arrays.<String> asList(null, null)).join();
		assertEquals(result, "");
	}

	@Test
	public void testConcatNull3() {
		String result = with(asList("a", null, "b")).join("-");
		assertEquals(result, "a-b");
	}

	@Test
	public void testMap() {
		Map<String, Person> personsByName = with(meAndMyFriends).map(on(Person.class).getFirstName());

		assertNotNull(personsByName);
		assertEquals(4, personsByName.size());
	}

	@Test
	public void testMap1() {
		Map<Integer, Person> personsByAge = with(meAndMyFriends).map(on(Person.class).getAge());

		assertNotNull(personsByAge);
		assertEquals(3, personsByAge.size());
	}
	
	@Test
	public void testMap2() {
		Map<Integer, Person> personsByAge = with(meAndMyFriends).map(on(Person.class).getAge());

		assertNotNull(personsByAge);
		assertEquals(3, personsByAge.size());
	}
	
	private Premium premiumOf(final String value) {
		return new Premium() {
			public Money getPremium() {
				return money(value);
			}
		};
	}

	public interface Premium {
		Money getPremium();
	}

    @Test
    public void testList() {
        LambdaList<Integer> list = with(1, 2, 3, 4, 5).subList(1, 4);
        assertEquals(3, list.size());

        list.add(0, 1);
        assertEquals(4, list.size());
        assertEquals(1, (int)list.get(0));
        assertEquals(0, (int)list.indexOf(1));

        list.add(1);
        assertEquals(5, list.size());
        assertEquals(4, (int)list.lastIndexOf(1));

        list.clear();
        assertEquals(0, list.size());

        list.addAll(with(1, 2, 3, 4, 5));
        assertEquals(5, list.size());
        list.remove(0);
        assertEquals(4, list.size());
        assertEquals(2, (int)list.get(0));
        list.set(0, 1);
        assertEquals(4, list.size());
        assertEquals(1, (int)list.get(0));

        ListIterator<Integer> iterator = list.listIterator();
        assertEquals(1, (int)iterator.next());

        iterator = list.listIterator(2);
        assertEquals(4, (int)iterator.next());
    }

    @Test
    public void testNonCloneableMap() {
        List<String> list = new NonCloneableList<String>();
        list.add("Italy");
        list.add("Germany");

        List<String> clonedList = with(list).clone();
        Assert.assertEquals(list, clonedList);
    }

    public static class NonCloneableList<T> extends ArrayList<T> {
        @Override
        public Object clone() {
            throw new RuntimeException();
        }
    }

    @Test
    public void testIterateAfterModifyCollection() {
        List<Integer> list = new ArrayList<Integer>() {{ add(2); add(2); }};
        LambdaList<Integer> lambdaList = with(list);
        lambdaList.add(0, 1);
        assertEquals(1, (int)lambdaList.iterator().next());
    }

    @Test
    public void testForEachAfterModifyCollection() {
        List<Person> list = new ArrayList<Person>() {{ add(new Person("Domenico")); add(new Person("Irma")); }};
        LambdaList<Person> lambdaList = with(list);
        lambdaList.add(0, new Person("Mario"));
        lambdaList.forEach().setLastName("Fusco");
        assertEquals("Fusco", lambdaList.get(0).getLastName());
    }
}
