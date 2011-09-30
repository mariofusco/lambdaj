package ch.lambdaj.function.matcher;

import static ch.lambdaj.function.matcher.HasNestedPropertyWithValue.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.*;
import org.junit.*;

import ch.lambdaj.mock.*;

public class HasNestedPropertyWithValueTest {

	@Test
	public void testHasNestedProperty() {
		Person me = new Person("Mario", "Fusco", 35);
		Person biagio = new Person("Biagio", "Beatrice", 39);
		me.setBestFriend(biagio);
		
		Matcher<?> matcher = hasNestedProperty("bestFriend.age", is(equalTo(biagio.getAge())));
		assertTrue(matcher.matches(me));
		assertFalse(matcher.matches(biagio));
		
		Description description = new StringDescription();
		matcher.describeTo(description);
		assertEquals("hasProperty(\"bestFriend.age\", is <39>)", description.toString());
	}
}
