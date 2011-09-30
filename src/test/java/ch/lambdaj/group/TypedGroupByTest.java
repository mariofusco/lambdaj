package ch.lambdaj.group;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Strings.*;
import static java.util.Arrays.*;

import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

import ch.lambdaj.mock.*;

public class TypedGroupByTest {

	private final Person me = new Person("Mario", "Fusco", 35);
	private final Person luca = new Person("Luca", "Marrocco", 29);
	private final Person biagio = new Person("Biagio", "Beatrice", 39);
	private final Person celestino = new Person("Celestino", "Bellone", 29);

	@Test
	public void testGroupByAge() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()));
		assertFalse(group.isLeaf());
		assertEquals(4, group.getSize());
		assertEquals(3, group.subgroups().size());
		
		Set<String> keys = group.keySet();
		assertTrue(keys.contains("29"));
		assertTrue(keys.contains("35"));
		assertTrue(keys.contains("39"));
		
		assertTrue(group.findAll().contains(me));
		assertTrue(group.findAll().contains(biagio));
		assertTrue(group.findAll().contains(luca));
		assertTrue(group.findAll().contains(celestino));
		
		Group<Person> group29aged = group.findGroup(29);
		assertTrue(group29aged.isLeaf());
		assertEquals(2, group29aged.getSize());
		assertEquals(0, group29aged.keySet().size());
		assertSame(group29aged, group29aged.findGroup(null));
		assertEquals(0, group29aged.subgroups().size());
		
		Collection<Person> persons29Aged = group29aged.findAll();
		assertSame(persons29Aged, group29aged.find(null));
		assertTrue(persons29Aged.contains(luca));
		assertTrue(persons29Aged.contains(celestino));
	}
	
	@Test
	public void testGroupByAgeAsPersons() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()).as("persons"));
		String json = json(group);
		System.out.println(json);
	}

	@Test
	public void testGroupByAgeAsPersonsWithFirstName() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()).as("persons").head(on(Person.class).getFirstName()));
		String json = json(group);
		System.out.println(json);
	}
	
	@Test
	public void testGroupByAgeAsPersonsWithName() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()).as("persons").head(on(Person.class).getFirstName(), "name"));
		assertEquals(0, group.getHeads().size());
		assertEquals("", group.getHeadValue("name"));

		Group<Person> group35aged = group.findGroup("35");
		assertEquals(2, group35aged.getHeads().size());
		assertTrue(group35aged.getHeads().contains("age"));
		assertEquals("35", group35aged.getHeadValue("age"));
		assertTrue(group35aged.getHeads().contains("name"));
		assertEquals("Mario", group35aged.getHeadValue("name"));
	}
	
	@Test
	public void testGroupByAgeAndName() {
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()), by(on(Person.class).getFirstName()));

		Group<Person> group29aged = group.findGroup(29);
		assertFalse(group29aged.isLeaf());
		
		Collection<Person> persons29Aged = group29aged.findAll();
		assertTrue(persons29Aged.contains(luca));
		assertTrue(persons29Aged.contains(celestino));
		
		Group<Person> groupLuca = group29aged.findGroup("Luca");
		assertTrue(groupLuca.findAll().contains(luca));
	}

    @Test
    public void testGroupAndSubgroup() {
        List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

        Group<Person> group = group(meAndMyFriends, by(on(Person.class).getAge()), by(on(Person.class).getFirstName()));
        assertEquals(4, group.getSize());
        assertEquals(3, group.subgroups().size());
        assertFalse(group.isLeaf());
        assertEquals(me, group.first());
        assertNull(group.key());

        Group<Person> subgroup = group.subgroups().get(0);
        assertFalse(subgroup.isLeaf());
        assertEquals(me, subgroup.first());
        assertEquals(me.getAge(), subgroup.key());

        Group<Person> subsubgroup = subgroup.subgroups().get(0);
        assertTrue(subsubgroup.isLeaf());
        assertEquals(me, subgroup.first());
        assertEquals(me.getFirstName(), subsubgroup.key());
    }
}
