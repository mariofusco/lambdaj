// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static ch.lambdaj.function.matcher.HasNestedPropertyWithValue.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;

import java.lang.reflect.*;
import java.util.*;
import java.math.*;

import ch.lambdaj.function.compare.Sort;
import org.hamcrest.*;
import org.junit.*;
import static org.junit.Assert.*;

import ch.lambdaj.function.argument.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.mock.*;
import ch.lambdaj.mock.IPerson.*;
import ch.lambdaj.proxy.*;
import static ch.lambdaj.Lambda.*;

/**
 * @author Mario Fusco
 */
public class LambdaTest {

	private final Person me = new Person("Mario", "Fusco", 35);
	private final Person luca = new Person("Luca", "Marrocco", 29);
	private final Person biagio = new Person("Biagio", "Beatrice", 39);
	private final Person celestino = new Person("Celestino", "Bellone", 29);
    private final Person sister = new Person("Irma", "Fusco", 31);
    private final Person dad = new Person("Domenico", "Fusco", 70);

	@Test
	public void testArgument() {
		Argument<Person> bestFriendArgument = argument(on(Person.class).getBestFriend());
		assertEquals("bestFriend", bestFriendArgument.getInkvokedPropertyName());
		assertEquals(Person.class, bestFriendArgument.getRootArgumentClass());
		assertEquals(Person.class, bestFriendArgument.getReturnType());
        assertEquals("[public ch.lambdaj.mock.Person ch.lambdaj.mock.Person.getBestFriend()]", bestFriendArgument.toString());
        assertEquals(bestFriendArgument, argument(on(Person.class).getBestFriend()));

		Argument<Integer> bestFriendAgeArgument = argument(on(Person.class).getBestFriend().getAge());
		assertEquals("bestFriend.age", bestFriendAgeArgument.getInkvokedPropertyName());
		assertEquals(Person.class, bestFriendAgeArgument.getRootArgumentClass());
		assertEquals(Integer.TYPE, bestFriendAgeArgument.getReturnType());
        assertEquals("[public ch.lambdaj.mock.Person ch.lambdaj.mock.Person.getBestFriend(), public int ch.lambdaj.mock.Person.getAge()]", bestFriendAgeArgument.toString());

        Argument<Integer> bestFriendOfBestFriendAgeArgument = argument(on(Person.class).getBestFriend().getBestFriend().getAge());
        assertEquals("bestFriend.bestFriend.age", bestFriendOfBestFriendAgeArgument.getInkvokedPropertyName());
        assertEquals("[public ch.lambdaj.mock.Person ch.lambdaj.mock.Person.getBestFriend(), public ch.lambdaj.mock.Person ch.lambdaj.mock.Person.getBestFriend(), public int ch.lambdaj.mock.Person.getAge()]", bestFriendOfBestFriendAgeArgument.toString());

        me.setBestFriend(biagio);
        assertEquals(biagio, bestFriendArgument.evaluate(me));
        assertEquals(biagio.getAge(), (int)bestFriendAgeArgument.evaluate(me));
        me.setBestFriend(null);
	}
	
	@Test
	public void testForEach() {
		List<Person> personInFamily = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
		forEach(personInFamily).setLastName("Fusco");
		for (Person person : personInFamily) assertEquals("Fusco", person.getLastName());
	}

    @Test
    public void testForEachThrowingException() {
        List<Person> personInFamily = asList(new Person("Domenico"), new Person("Mario"), new PersonThrowingException());
        try {
            forEach(personInFamily).setLastName("Fusco");
            fail("Must throw a RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Cannot set last name", e.getMessage());
        }
    }

    @Test
    public void testForEachOnIterator() {
        List<Person> personInFamily = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
        forEach(personInFamily.iterator()).setLastName("Fusco");
        for (Person person : personInFamily) assertEquals("Fusco", person.getLastName());
    }

    @Test
    public void testForEachOnArray() {
        Person dad = new Person("Domenico");
        Person me = new Person("Mario");
        Person sis = new Person("Irma");

        forEach(dad, me, sis).setLastName("Fusco");

        assertEquals("Fusco", dad.getLastName());
        assertEquals("Fusco", me.getLastName());
        assertEquals("Fusco", sis.getLastName());
    }

	@Test
	public void testFailingForEach() {
		List<Person> personInFamily = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
		try {
			forEach(personInFamily).setFailingLastName("Fusco");
			fail("Invocation on wrong method must fail");
		} catch (Exception ie) { }
        try {
            forEach(personInFamily.iterator()).setFailingLastName("Fusco");
            fail("Invocation on wrong method must fail");
        } catch (Exception ie) { }
	}

	@Test
	public void testForEachForProxy() {
		IPerson dad = (IPerson)Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{ IPerson.class }, new PersonProxy("Domenico"));
		IPerson me = (IPerson)Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{ IPerson.class }, new PersonProxy("Mario"));
		IPerson sister = (IPerson)Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{ IPerson.class }, new PersonProxy("Irma"));

		List<IPerson> personInFamily = asList(dad, me, sister);
		forEach(personInFamily).setLastName("Fusco");
		for (IPerson person : personInFamily) assertEquals("Di Fusco", person.getLastName());
	}
	
	private static final class PersonProxy implements InvocationHandler {
		
		private final IPerson person;
		
		private PersonProxy(String firstName) {
			this(new Person(firstName));
		}
		
		private PersonProxy(IPerson person) {
			this.person = person;
		}
		
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("setLastName")) {
				String lastName = (String)args[0];
				person.setLastName("Di " + lastName);
				return null;
			}
			return method.invoke(person, args);
		}
	}
	
	public static class NoEmptyConstructorPerson extends Person { 
		public NoEmptyConstructorPerson(String firstName) { 
			super(firstName);
		}
	}
	
	@Test
	public void testForEachWithNoEmptyConstructor() {
		List<? extends Person> personInFamily = asList(new NoEmptyConstructorPerson("Domenico"), new NoEmptyConstructorPerson("Mario"), new NoEmptyConstructorPerson("Irma"));
		forEach(personInFamily).setLastName("Fusco");
		for (Person person : personInFamily) assertEquals("Fusco", person.getLastName());
	}
	
	public static final class FinalPerson extends Person { 
		public FinalPerson() { }
		public FinalPerson(String firstName) { 
			super(firstName);
		}
	}
	
	@Test
	public void testIllegalForEach() {
		try {
			forEach((List)null);
			fail("forEach on null should throw an exception");
		} catch (IllegalArgumentException e) { }
		
		try {
			forEach(new LinkedList<Person>());
			fail("forEach on empty iterable should throw an exception");
		} catch (IllegalArgumentException e) { }

        try {
            forEach(new LinkedList<Person>().iterator());
            fail("forEach on empty iterable should throw an exception");
        } catch (IllegalArgumentException e) { }

		try {
			forEach(asList(new FinalPerson("Domenico"), new FinalPerson("Mario"), new FinalPerson("Irma")));
			fail("forEach on empty iterable should throw an exception");
		} catch (UnproxableClassException e) {
			assertTrue(e.getMessage().contains("Unable to proxy the final class"));
		}

		forEach(new LinkedList<Person>(), Person.class);
        forEach(new LinkedList<Person>().iterator(), Person.class);
		forEach(asList(new FinalPerson("Domenico"), new FinalPerson("Mario"), new FinalPerson("Irma")), Person.class);
	}
	
    @Test
    public void testSelectOnNull() {
        Iterable<Person> nullIterable = null;
        List<Person> result = select(nullIterable, having(on(Person.class).getAge(), equalTo(35)));
        assertTrue(result.isEmpty());

        Iterator<Person> nullIterator = null;
        result = select(nullIterator, having(on(Person.class).getAge(), equalTo(35)));
        assertTrue(result.isEmpty());
    }

	@Test
	public void testSelectPersonWith4LettersName() {
		List<Person> family = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
		Collection<Person> results = select(family, hasNestedProperty("firstName.length", equalTo(4)));
		assertThat(results.size(), is(equalTo(1)));
		assertThat(results.iterator().next().getFirstName(), is(equalTo("Irma")));
	}
	
	@Test
	public void testFilterPersonWith4LettersName() {
		List<Person> family = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
		Collection<Person> results = filter(hasNestedProperty("firstName.length", equalTo(4)), family);
		assertThat(results.size(), is(equalTo(1)));
		assertThat(results.iterator().next().getFirstName(), is(equalTo("Irma")));
	}
	
	@Test
	public void testSortOnAge() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		List<Person> sorted = sort(meAndMyFriends, on(Person.class).getAge());
		assertSame(luca, sorted.get(0));
		assertSame(celestino, sorted.get(1));
		assertSame(me, sorted.get(2));
		assertSame(biagio, sorted.get(3));
	}
	
    @Test
    public void testSortOnAgeWithNull() {
        List<Person> meAndMyFriends = asList(me, luca, null, biagio, celestino);

        List<Person> sorted = sort(meAndMyFriends, on(Person.class).getAge());
        assertSame(luca, sorted.get(0));
        assertSame(celestino, sorted.get(1));
        assertSame(me, sorted.get(2));
        assertSame(biagio, sorted.get(3));
        assertNull(sorted.get(4));
    }

	@Test
	public void testSortOnAgeArgument() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		List<Person> sorted = sort(meAndMyFriends, on(Person.class).getAge());
		assertSame(luca, sorted.get(0));
		assertSame(celestino, sorted.get(1));
		assertSame(me, sorted.get(2));
		assertSame(biagio, sorted.get(3));

        List<Person> descSorted = sort(meAndMyFriends, on(Person.class).getAge(), Sort.DESCENDING);
        assertSame(biagio, descSorted.get(0));
        assertSame(me, descSorted.get(1));
        assertSame(luca, descSorted.get(2));
        assertSame(celestino, descSorted.get(3));
	}

    @Test
    public void testSortIgnoreCase() {
        luca.setFirstName("luca");
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

        List<Person> sorted = sort(meAndMyFriends, on(Person.class).getFirstName());
        assertSame(biagio, sorted.get(0));
        assertSame(celestino, sorted.get(1));
        assertSame(me, sorted.get(2));
        assertSame(luca, sorted.get(3));

        List<Person> sortedIgnoreCase = sort(meAndMyFriends, on(Person.class).getFirstName(), Sort.IGNORE_CASE);
        assertSame(biagio, sortedIgnoreCase.get(0));
        assertSame(celestino, sortedIgnoreCase.get(1));
        assertSame(luca, sortedIgnoreCase.get(2));
        assertSame(me, sortedIgnoreCase.get(3));

        List<Person> sortedDescIgnoreCase = sort(meAndMyFriends, on(Person.class).getFirstName(), Sort.DESCENDING + Sort.IGNORE_CASE);
        assertSame(me, sortedDescIgnoreCase.get(0));
        assertSame(luca, sortedDescIgnoreCase.get(1));
        assertSame(celestino, sortedDescIgnoreCase.get(2));
        assertSame(biagio, sortedDescIgnoreCase.get(3));

        luca.setFirstName("Luca");
    }

	@Test
	public void testFindOldest() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Person oldest = selectMax(meAndMyFriends, on(Person.class).getAge());
		assertSame(biagio, oldest);

		Person alphabeticalFirst = selectMin(meAndMyFriends, on(Person.class).getFirstName());
		assertSame(biagio, alphabeticalFirst);
	}
	
    @Test
    public void testFindOldestOnArray() {
        Person[] meAndMyFriends = new Person[] { me, luca, biagio, celestino };

        Person oldest = selectMax(meAndMyFriends, on(Person.class).getAge());
        assertSame(biagio, oldest);

        Person alphabeticalFirst = selectMin(meAndMyFriends, on(Person.class).getFirstName());
        assertSame(biagio, alphabeticalFirst);
    }

	@Test
	public void testSortOnNameLenght() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		List<Person> sorted = sort(meAndMyFriends, on(Person.class).getFirstName(), new Comparator<String>() {
			public int compare(String s1, String s2) { return s1.length() - s2.length(); }
		});
		assertSame(luca, sorted.get(0));
		assertSame(me, sorted.get(1));
		assertSame(biagio, sorted.get(2));
		assertSame(celestino, sorted.get(3));
	}
	
	@Test
	public void testSelectWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		Collection<Person> friends29aged = select(meAndMyFriends, having(on(Person.class).getAge(), is(equalTo(29))));
		assertEquals(2, friends29aged.size());
		Iterator<Person> friendsIterator = friends29aged.iterator();
		assertSame(luca, friendsIterator.next());
		assertSame(celestino, friendsIterator.next());
	}
	
    @Test
    public void testSelectIteratorWithHaving() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

        Iterator<Person> friends29agedIterator = selectIterator(meAndMyFriends, having(on(Person.class).getAge(), is(equalTo(29))));
        assertSame(luca, friends29agedIterator.next());
        assertSame(celestino, friends29agedIterator.next());
        assertFalse(friends29agedIterator.hasNext());
    }

    @Test
    public void testSelectOnForEach() {
        me.setBestFriend(biagio);
        biagio.setBestFriend(celestino);
        celestino.setBestFriend(luca);
        luca.setBestFriend(me);

        List<Person> friends29aged = select(forEach(me, luca, biagio, celestino).getBestFriend(), having(on(Person.class).getAge(), is(equalTo(29))));
        assertSame(celestino, friends29aged.get(0));
        assertSame(luca, friends29aged.get(1));
    }

	@Test
	public void testSelectWithHavingInOr() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		Collection<Person> friends29aged = select(meAndMyFriends, having(on(Person.class).getAge(), is(equalTo(29))).or(having(on(Person.class).getAge(), is(equalTo(35)))));
		assertEquals(3, friends29aged.size());
		Iterator<Person> friendsIterator = friends29aged.iterator();
		assertSame(me, friendsIterator.next());
		assertSame(luca, friendsIterator.next());
		assertSame(celestino, friendsIterator.next());
	}
	
	@Test
	public void testSelectDistinctAge() {
		Object meAndMyFriends = asList(me, luca, biagio, celestino);
		Collection<Person> distinctAgePersons = selectDistinct(meAndMyFriends, "age");
		assertEquals(3, distinctAgePersons.size());
	}
	
	@Test
	public void testSelectDistinctOnAge() {
		Object meAndMyFriends = asList(me, luca, biagio, celestino);
		Collection<Person> distinctAgePersons = selectDistinctArgument(meAndMyFriends, on(Person.class).getAge());
		assertEquals(3, distinctAgePersons.size());

        Collection<Integer> distinctAges = selectDistinct(extract(meAndMyFriends, on(Person.class).getAge()));
        assertEquals(3, distinctAges.size());
        assertTrue(distinctAges.contains(me.getAge()) && distinctAges.contains(biagio.getAge()) && distinctAges.contains(luca.getAge()));
	}
	
	@Test
	public void testArgumentIdentity() {
		assertTrue(on(Person.class).getAge() == on(Person.class).getAge());
        boolean isYoungerThan = on(Person.class).isYoungerThan(30);
        assertEquals("[public boolean ch.lambdaj.mock.Person.isYoungerThan(int) with args 30]", argument(isYoungerThan).toString());

		assertTrue(on(Person.class).isYoungerThan(30) == isYoungerThan);
		assertFalse(on(Person.class).isYoungerThan(25) == on(Person.class).isYoungerThan(30));
		assertTrue(on(Person.class).getGender() == on(Person.class).getGender());
	}
	
	@Test
	public void testSelectOnBooleanWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		List<Person> youngFriends = select(meAndMyFriends, having(on(Person.class).isYoungerThan(30)));
		assertEquals(2, youngFriends.size());
		assertSame(luca, youngFriends.get(0));
		assertSame(celestino, youngFriends.get(1));

		List<Person> youngestFriends = select(meAndMyFriends, having(on(Person.class).isYoungerThan(25)));
		assertTrue(youngestFriends.isEmpty());

		List<Person> notSoYoungFriends = select(meAndMyFriends, having(on(Person.class).isYoungerThan(40)));
		assertEquals(4, notSoYoungFriends.size());
	}
	
	@Test
	public void testSelectOnFailingMethod() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		List<Person> youngFriends = select(meAndMyFriends, having(on(Person.class).isFailingYoungerThan(30)));
		assertEquals(0, youngFriends.size());
	}
	
	@Test
	public void testSelectOnDateWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		List<Person> youngFriends = select(meAndMyFriends, having(on(Person.class).getBirthDate(), greaterThan(new GregorianCalendar(1975, 0, 1).getTime())));
		
		assertEquals(2, youngFriends.size());
		assertSame(luca, youngFriends.get(0));
		assertSame(celestino, youngFriends.get(1));
	}

	@Test
	public void testSelectOnEnumMustFailWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		List<Person> maleFriends = select(meAndMyFriends, having(on(Person.class).getGender(), equalTo(Gender.MALE)));
		assertEquals(4, maleFriends.size());
	}
	
    @Test
    public void testProject() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
        List<Map<String, Object>> projection = project(meAndMyFriends, as("name", on(Person.class).getFirstName()), as(on(Person.class).getAge()));

        assertEquals(meAndMyFriends.size(), projection.size());
        for (int i = 0; i < meAndMyFriends.size(); i++) {
            assertEquals(meAndMyFriends.get(i).getFirstName(), projection.get(i).get("name"));
            assertEquals(meAndMyFriends.get(i).getAge(), projection.get(i).get("age"));
        }
    }

    @Test
    public void testProjectDto() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
        List<PersonDto> meAndMyFriendsDto = project(meAndMyFriends, PersonDto.class, on(Person.class).getFirstName(), on(Person.class).getAge());

        assertEquals(meAndMyFriends.size(), meAndMyFriendsDto.size());
        for (int i = 0; i < meAndMyFriends.size(); i++) {
            assertEquals(meAndMyFriends.get(i).getFirstName(), meAndMyFriendsDto.get(i).getName());
            assertEquals(meAndMyFriends.get(i).getAge(), meAndMyFriendsDto.get(i).getAge());
        }
    }

    @Test
    public void testInvalidProjectDto() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
        try {
            project(meAndMyFriends, PersonDto.class, on(Person.class).getFirstName(), on(Person.class).getLastName(), on(Person.class).getAge());
            fail("Project using non existent constructor must fail");
        } catch (RuntimeException e) { }
        try {
            project(meAndMyFriends, PersonDto.class, on(Person.class).getFirstName(), on(Person.class).getLastName());
            fail("Project using non existent constructor must fail");
        } catch (RuntimeException e) { }
        try {
            project(meAndMyFriends, ValidatingPersonDto.class, on(Person.class).getFirstName(), on(Person.class).getAge());
            fail("Project invoking a constructor throwing and exception must fail");
        } catch (RuntimeException e) { }
    }

	@Test
	public void testFilter() {
		List<Integer> biggerThan3 = filter(greaterThan(3), asList(1, 2, 3, 4, 5));
		assertEquals(2, biggerThan3.size());
		assertEquals(4, (int)biggerThan3.get(0));
		assertEquals(5, (int)biggerThan3.get(1));
	}
	
    @Test
    public void testFilterArray() {
        List<Integer> biggerThan3 = filter(greaterThan(3), 1, 2, 3, 4, 5);
        assertEquals(2, biggerThan3.size());
        assertEquals(4, (int)biggerThan3.get(0));
        assertEquals(5, (int)biggerThan3.get(1));
    }

	@Test
	public void testFilterOnCustomMatcher() {
		Matcher<Integer> odd = new TypeSafeMatcher<Integer>() {
			@Override
			public boolean matchesSafely(Integer item) {
				return item % 2 == 1;
			}
			public void describeTo(Description description) {
				description.appendText("odd()");
			}
		};

		List<Integer> odds = filter(odd, 1, 2, 3, 4, 5);
		assertEquals(3, odds.size());
		assertEquals(1, (int)odds.get(0));
		assertEquals(3, (int)odds.get(1));
		assertEquals(5, (int)odds.get(2));
	}
	
	@Test
	public void testFilterWithHaving() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

		Collection<Person> oldFriends = filter(having(on(Person.class).getAge(), greaterThan(30)), meAndMyFriends);
		assertEquals(2, oldFriends.size());
		Iterator<Person> friendsIterator = oldFriends.iterator();
		assertSame(me, friendsIterator.next());
		assertSame(biagio, friendsIterator.next());
	}

    @Test
    public void testRepeatedSum() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
        assertEquals((Float)4.4f, sum(meAndMyFriends, on(Person.class).getFloat()));
        assertEquals((Integer)132, sum(meAndMyFriends, on(Person.class).getAge()));
        assertEquals((Float)4.4f, sum(meAndMyFriends, on(Person.class).getFloat()));
        assertEquals((Integer)132, sum(meAndMyFriends, on(Person.class).getAge()));
    }

	@Test
	public void testIllegalSumFrom() {
		try {
			sumFrom(null);
			fail("forEach on null should throw an exception");
		} catch (Exception e) { }
		try {
			sumFrom(new LinkedList<Person>());
			fail("forEach on empty iterable should throw an exception");
		} catch (Exception e) { }
	}

    @Test
    public void testSumOnEmptyList() {
        int age = sum(Collections.emptyList(), on(Person.class).getAge());
        assertEquals(0, age);

        double income = sum(Collections.emptyList(), on(Person.class).getIncome());
        assertEquals(0.0, income, 0.001);

        float floatIncome = sum(Collections.emptyList(), on(Person.class).getFloatIncome());
        assertEquals(0.0f, floatIncome, 0.001);
    }

	@Test
	public void testSumMinMaxFrom() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

        Person ageSummer = sumFrom(meAndMyFriends);
		assertThat(ageSummer.getAge(), is(equalTo(35+29+39+29)));
        // check that the sumFrom proxy works also on the second invocation
        assertThat(ageSummer.getAge(), is(equalTo(35+29+39+29)));

        assertThat(ageSummer.getAgeAsBigInteger(), is(equalTo(BigInteger.valueOf(35+29+39+29))));
        assertThat(ageSummer.getAgeAsBigDecimal(), is(equalTo(BigDecimal.valueOf(35+29+39+29))));

		int minAge = minFrom(meAndMyFriends).getAge();
		assertThat(minAge, is(equalTo(29)));

		int maxAge = maxFrom(meAndMyFriends).getAge();
		assertThat(maxAge, is(equalTo(39)));
	}

    @Test
    public void testSumInteger() {
        List<Integer> list = Arrays.asList(new Integer("1"), new Integer("2"), new Integer("3"));
        assertThat((Integer)sum(list), equalTo(new Integer("6")));
    }

    @Test
    public void testSumBigDecimal() {
        List<BigDecimal> list = Arrays.asList(new BigDecimal("1.1"), new BigDecimal("2.2"), new BigDecimal("3.3"));
        assertThat((BigDecimal)sum(list), equalTo(new BigDecimal("6.6")));
    }


    @Test
    public void testAvg() {
        assertEquals((Double)0.0, (Double)avg(new ArrayList<Person>()));

        assertEquals((Integer)0, (Integer)avg(new ArrayList<Person>(), on(Person.class).getAge()));
        assertEquals((Double)0.0, (Double)avg(new ArrayList<Person>(), on(Person.class).getIncome()));
        assertEquals(BigDecimal.ZERO, avg(new ArrayList<Person>(), on(Person.class).getBigDecimalIncome()));

        assertEquals((Integer)0, (Integer)avgFrom(new ArrayList<Person>(), Person.class).getAge());
        assertEquals((Double)0.0, (Double)avgFrom(new ArrayList<Person>(), Person.class).getIncome());
        assertEquals(BigDecimal.ZERO, avgFrom(new ArrayList<Person>(), Person.class).getBigDecimalIncome());

        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
        int averageAge = avg(meAndMyFriends, on(Person.class).getAge());
        assertEquals(averageAge, (35+29+39+29)/4);
        int averageAgeFrom = avgFrom(meAndMyFriends).getAge();
        assertEquals(averageAgeFrom, (35+29+39+29)/4);

        double averageIncome = avg(meAndMyFriends, on(Person.class).getIncome());
        assertEquals(averageIncome, (me.getIncome() + luca.getIncome() + biagio.getIncome() + celestino.getIncome())/4, 0.001);
        double averageIncomeFrom = avgFrom(meAndMyFriends).getIncome();
        assertEquals(averageIncomeFrom, (me.getIncome() + luca.getIncome() + biagio.getIncome() + celestino.getIncome())/4, 0.001);

        BigDecimal averageBigIncome = avg(meAndMyFriends, on(Person.class).getBigDecimalIncome());
        assertEquals(averageBigIncome.doubleValue(), (me.getIncome() + luca.getIncome() + biagio.getIncome() + celestino.getIncome())/4, 0.001);
        BigDecimal averageBigIncomeFrom = avgFrom(meAndMyFriends).getBigDecimalIncome();
        assertEquals(averageBigIncomeFrom.doubleValue(), (me.getIncome() + luca.getIncome() + biagio.getIncome() + celestino.getIncome())/4, 0.001);
    }

	@Test
	public void testEmptySumMinMaxFrom() {
		assertThat(0, is(equalTo(sumFrom(new ArrayList<Person>(), Person.class).getAge())));
		assertThat(0, is(equalTo(minFrom(new ArrayList<Person>(), Person.class).getAge())));
		assertThat(0, is(equalTo(maxFrom(new ArrayList<Person>(), Person.class).getAge())));
	}
	
	@Test
	public void testPlainSumMinMaxFrom() {
        Integer[] integerArray = new Integer[] { 1, 2, 3, 4, 5 };
        assertThat(15, is(equalTo(sum(integerArray))));
        assertThat(1, is(equalTo(min(integerArray))));
        assertThat(5, is(equalTo(max(integerArray))));

		List<Integer> integerList = asList(integerArray);
		assertThat(15, is(equalTo(sum(integerList))));
		assertThat(1, is(equalTo(min(integerList))));
		assertThat(5, is(equalTo(max(integerList))));

        assertThat(15, is(equalTo(sum(integerList.iterator()))));
        assertThat(1, is(equalTo(min(integerList.iterator()))));
        assertThat(5, is(equalTo(max(integerList.iterator()))));

        Long[] longArray = new Long[] { 1L, 2L, 3L, 4L, 5L };
        assertThat(15L, is(equalTo(sum(longArray))));
        Double[] doubleArray = new Double[] { 1.0, 2.0, 3.0, 4.0, 5.0 };
        assertThat(15.0, is(equalTo(sum(doubleArray))));
        Float[] floatArray = new Float[] { 1f, 2f, 3f, 4f, 5f };
        assertThat(15f, is(equalTo(sum(floatArray))));
	}
	
	@Test
	public void testTypedSumMinMax() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		assertThat(sum(meAndMyFriends, on(Person.class).getAge()), is(equalTo(35+29+39+29)));
		assertThat(min(meAndMyFriends, on(Person.class).getAge()), is(equalTo(29)));
		assertThat(max(meAndMyFriends, on(Person.class).getAge()), is(equalTo(39)));
	}
	
	@Test
	public void testTypedSum2() {
		List<Person> myFriends = asList(luca, biagio, celestino);
		forEach(myFriends).setBestFriend(me);

		int totalBestFriendAge = sum(myFriends, on(Person.class).getBestFriend().getAge());
		assertThat(totalBestFriendAge, is(equalTo(35*3)));
	}
	
	@Test
	public void testTypedMixedSums() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		int onPersonAge = on(Person.class).getAge();
		
		List<Person> myFriends = asList(luca, biagio, celestino);
		forEach(myFriends).setBestFriend(me);
		int onBestFriendsAge = on(Person.class).getBestFriend().getAge();

		int totalAge = sum(meAndMyFriends, onPersonAge);
		assertThat(totalAge, is(equalTo(35+29+39+29)));

		int totalBestFriendAge = sum(myFriends, onBestFriendsAge);
		assertThat(totalBestFriendAge, is(equalTo(35*3)));

		int totalMyFriendsAge = sum(myFriends, onPersonAge);
		assertThat(totalMyFriendsAge, is(equalTo(29+39+29)));
	}

    @Test
    public void testFlatten1() {
        List<Object> list = new ArrayList<Object>();
        list.add(me);
        Map<String, Person> map = new LinkedHashMap<String, Person>();
        map.put("celestino", celestino);
        map.put("luca", luca);
        list.add(map);
        list.add(new Person[] { biagio } );

        List<Person> flattened = flatten(list);
        assertEquals(me, flattened.get(0));
        assertEquals(celestino, flattened.get(1));
        assertEquals(luca, flattened.get(2));
        assertEquals(biagio, flattened.get(3));
    }

    @Test
    public void testFlatten2() {
        List<Object> list = new ArrayList<Object>();
        list.addAll(asList(asList(me), celestino));
        list.addAll(asList(asList(luca, biagio)));

        List<Person> flattened = flatten(list);
        assertEquals(me, flattened.get(0));
        assertEquals(celestino, flattened.get(1));
        assertEquals(luca, flattened.get(2));
        assertEquals(biagio, flattened.get(3));
    }

	@Test
	public void testCollectAges() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		List<Integer> ages = collect(meAndMyFriends, on(Person.class).getAge());
		assertThat(ages.get(0), is(equalTo(35)));
		assertThat(ages.get(1), is(equalTo(29)));
		assertThat(ages.get(2), is(equalTo(39)));
		assertThat(ages.get(3), is(equalTo(29)));
	}
	
	@Test
	public void testInvalidCollect() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		try {
			collect(meAndMyFriends, 29);
			fail("collect on non valid argument must fail");
		} catch (Exception e) { }
		try {
			collect(me, on(Person.class).getAge());
			fail("collect on non iterable object must fail");
		} catch (Exception e) { }
	}
	
	@Test
	public void testSelectUnique() {
        Person[] personArray = new Person[] { me, luca, biagio, celestino };
        Person person34Aged = selectUnique(personArray, having(on(Person.class).getAge(), equalTo(34)));
        assertNull(person34Aged);
        Person person35Aged = selectUnique(personArray, having(on(Person.class).getAge(), equalTo(35)));
        assertSame(me, person35Aged);

		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		person34Aged = selectUnique(meAndMyFriends, having(on(Person.class).getAge(), equalTo(34)));
		assertNull(person34Aged);
		person35Aged = selectUnique(meAndMyFriends, having(on(Person.class).getAge(), equalTo(35)));
		assertSame(me, person35Aged);
		
		try {
			selectUnique(meAndMyFriends, having(on(Person.class).getAge(), equalTo(29)));
			fail("Should throw a RuntimeException since there are two 29 years old persons");
		} catch (Exception e) { }

        person35Aged = selectUnique(meAndMyFriends.iterator(), having(on(Person.class).getAge(), equalTo(35)));
        assertSame(me, person35Aged);
	}

	@Test
	public void testSelectFirst() {
		Object meAndMyFriends = asList(me, luca, biagio, celestino);

		Person person34Aged = selectFirst(meAndMyFriends, having(on(Person.class).getAge(), equalTo(34)));
		assertNull(person34Aged);
		
		Person person29Aged = selectFirst(meAndMyFriends, having(on(Person.class).getAge(), equalTo(29)));
		assertSame(luca, person29Aged);
	}
	
	@Test
	public void testSelectStringsThatEndsWithD() {
		List<String> strings = asList("first", "second", "third");
		Collection<String> results = select(strings, endsWith("d"));

		assertThat(results.size(), is(equalTo(2)));
		assertThat(results, hasItems("second", "third"));
	}

	@Test
	public void testSelectDistinct() {
		Object strings = asList("first", "second", "third", "first", "second");

		Collection<String> results = selectDistinct(strings);
		assertThat(results.size(), is(equalTo(3)));

		results = selectDistinct(strings, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.length() - s2.length();
			}
		});
		assertThat(results.size(), is(equalTo(2)));
	}

	@Test
	public void testJoin() {
		List<String> strings = asList("first", "second", "third");
		String result = join(strings);
		assertThat(result, is(equalTo("first, second, third")));
	}

	@Test
	public void testJoinFrom() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		String result = joinFrom(exposures).getCountryName();
		assertThat(result, is(equalTo("france, brazil")));
	}

	@Test
	public void testEmptyJoinFrom() {
		assertEquals("", joinFrom(new ArrayList<Person>(), Person.class).getLastName());
		assertEquals("", joinFrom(new ArrayList<Person>(), Person.class, " - ").getLastName());
	}
	
	@Test
	public void testSelectFranceExposures() {
		Exposure franceExposure = new Exposure("france", "first");
		Exposure brazilExposure = new Exposure("brazil", "second");
		Collection<Exposure> exposures = asList(franceExposure, brazilExposure);
		Collection<Exposure> result = select(exposures, hasProperty("countryName", is(equalTo("france"))));

		assertThat(result.size(), is(equalTo(1)));
		assertThat(result, hasItem(franceExposure));
	}

	@Test
	public void testConcatUsingMockedString() {
		Text aText = new Text("a text");
		Text anotherText = new Text("another text");
		List<Text> strings = asList(aText, anotherText);

		assertThat(joinFrom(strings, "; ").toString(), is(equalTo("a text; another text")));
	}

	@Test
	public void testJoinStrings() {
		assertThat(join(forEach(asList("many", "strings"))), is(equalTo("many, strings")));
        assertThat(join(forEach("many", "strings")), is(equalTo("many, strings")));
		assertThat(join(asList("many", "strings")), is(equalTo("many, strings")));
        assertThat(join(new String[] { "many", "strings" }), is(equalTo("many, strings")));
		assertThat(join(new ArrayList<String>()), is(equalTo("")));
		assertThat(join(null), is(equalTo("")));
		assertThat(join(""), is(equalTo("")));
		assertThat(join(1), is(equalTo("1")));
		assertThat(join(1l), is(equalTo("1")));
		assertThat(join(1f), is(equalTo("1.0")));
		assertThat(join(1d), is(equalTo("1.0")));
	}

	@Test
	public void testJoinEmptyStringWithSeparatorAlwaysProduceEmptyString() {
		assertThat(join("", ";"), is(equalTo("")));
		assertThat(join("", ","), is(equalTo("")));
		assertThat(join("", "%"), is(equalTo("")));
		assertThat(join("", ":"), is(equalTo("")));
		assertThat(join("", "$"), is(equalTo("")));
		assertThat(join("", "."), is(equalTo("")));
	}
	
	@Test
	public void testExtract() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		Collection<String> countries = extract(exposures, on(Exposure.class).getCountryName());
		assertThat(countries, hasItem("france"));
		assertThat(countries, hasItem("brazil"));
	}

    @Test
    public void testExtractIterator() {
        Exposure[] exposures = new Exposure[] { new Exposure("france", "first"), new Exposure("brazil", "second") };
        Iterator<String> countriesIterator = extractIterator(exposures, on(Exposure.class).getCountryName());
        assertEquals("france", countriesIterator.next());
        assertEquals("brazil", countriesIterator.next());
        assertFalse(countriesIterator.hasNext());
    }

    @Test
    public void testNullSafeExtract() {
        Person p1 = new Person("Mario", "Fusco", 35);
        Person p2 = new Person("Luca", null, 29);
        Person p3 = new Person("Biagio", "Beatrice", 39);
        Person p4 = null;

        p1.setBestFriend(p2);
        p3.setBestFriend(p1);

        List<Person> persons = asList(p1, p2, p3, p4);
        List<String> lastNames = extract(persons, on(Person.class).getBestFriend().getLastName());

        assertNull(lastNames.get(0));
        assertNull(lastNames.get(1));
        assertEquals("Fusco", lastNames.get(2));
        assertNull(lastNames.get(3));
    }

	@Test
	public void testExtractString() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		Collection<String> countries = extractString(exposures);
		assertThat(countries, hasItem("france, first"));
		assertThat(countries, hasItem("brazil, second"));
	}

	@Test
	public void testExtractProperty() {
		List<Exposure> exposures = asList(new Exposure("france", "first"), new Exposure("brazil", "second"));
		Collection<String> countries = extractProperty(exposures, "countryName");
		assertThat(countries, hasItem("france"));
		assertThat(countries, hasItem("brazil"));
	}
	
	@Test
	public void testConvert() {
		List<String> strings = asList("first", "second", "third", "four");
		Collection<Integer> lengths = convert(strings, new StringLengthConverter());
		int i = 0;
		for (int length : lengths) assertEquals(strings.get(i++).length(), length);
	}

    @Test
    public void testConvertMap() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
        Map<String, Person> map = index(meAndMyFriends, on(Person.class).getFirstName());
        Map<String, String> convertedMap = convertMap(map, on(Person.class).getLastName());
        assertEquals("Fusco", convertedMap.get("Mario"));
    }

    @Test
    public void testCount() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino, sister, dad);
        Map<String, Integer> lastNameCounter = count(meAndMyFriends, on(Person.class).getLastName());
        assertThat(3, equalTo(lastNameCounter.get("Fusco")));
        assertThat(1, equalTo(lastNameCounter.get("Beatrice")));
        assertNull(lastNameCounter.get("Pippo"));
    }

	@SuppressWarnings("unchecked")
	@Test
	public void testStringPropertyExtractor() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		List<String> agesAsString = convert(meAndMyFriends, new StringPropertyExtractor("age"));
		for (int i = 0; i < meAndMyFriends.size(); i++)
			assertEquals(agesAsString.get(i), String.valueOf(meAndMyFriends.get(i).getAge()));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testNestedStringPropertyExtractor() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		List<String> agesAsString = convert(meAndMyFriends, new StringPropertyExtractor("bestFriend.age"));
		for (int i = 0; i < meAndMyFriends.size(); i++) {
			Person bestFriend = meAndMyFriends.get(i).getBestFriend();
			if (bestFriend == null) assertEquals(agesAsString.get(i), "");
			else assertEquals(agesAsString.get(i), String.valueOf(bestFriend.getAge()));
		}
	}
	
	@Test
	public void testIndex() {
		Exposure frenchExposure = new Exposure("france", "first");
		Exposure brazilianExposure = new Exposure("brazil", "second");
		List<Exposure> exposures = asList(frenchExposure, brazilianExposure);
		Map<String, Exposure> indexed = index(exposures, on(Exposure.class).getCountryName());
		assertSame(frenchExposure, indexed.get("france"));
		assertSame(brazilianExposure, indexed.get("brazil"));
	}

    private static class BigDecimalWrapper {
        public BigDecimal getBigDecimal() {
            return BigDecimal.ZERO;
        }
    }

    @Test
    public void testSumBigDecimalOnEmptyList() {
        List<BigDecimalWrapper> list = new ArrayList<BigDecimalWrapper>();
        
        BigDecimal result = sumFrom(list, BigDecimalWrapper.class).getBigDecimal();
        assertEquals(result, BigDecimal.ZERO);

        BigDecimal result2 = sum(list, on(BigDecimalWrapper.class).getBigDecimal());
        assertEquals(result2, BigDecimal.ZERO);
    }

    @Test
    public void testExists() {
        assertTrue("Anything", exists(Collections.singleton("foo"), anything()));
        assertFalse("Nothing", exists(Collections.singleton("foo"), not(anything())));
        assertFalse("Empty", exists(Collections.emptySet(), anything()));

        assertTrue("Has match", exists(Collections.singleton("foo"), equalTo("foo")));
        assertFalse("Has no match", exists(Collections.singleton("bar"), equalTo("foo")));
        assertTrue("One of many", exists(Arrays.asList("bar", "foo"), equalTo("foo")));
    }
}