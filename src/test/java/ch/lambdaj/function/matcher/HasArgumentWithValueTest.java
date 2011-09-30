package ch.lambdaj.function.matcher;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.*;
import org.junit.*;

import ch.lambdaj.mock.*;

public class HasArgumentWithValueTest {

	@Test
	public void testHaving() {
		Person me = new Person("Mario", "Fusco", 35);
		Person biagio = new Person("Biagio", "Beatrice", 39);
		me.setBestFriend(biagio);
		
		Matcher<?> matcher = having(on(Person.class).getBestFriend().getAge(), is(equalTo(biagio.getAge())));
		assertTrue(matcher.matches(me));
		assertFalse(matcher.matches(biagio));
		
		Description description = new StringDescription();
		matcher.describeTo(description);
		assertEquals("hasArgument(\"bestFriend.age\", is <39>)", description.toString());
	}
}
