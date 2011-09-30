// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.*;

import ch.lambdaj.function.closure.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.mock.*;

/**
 * @author Mario Fusco
 */
public class ClosureTest {
	
	public Integer add(Integer val1, Integer val2) {
		return val1 + val2;
	}

	public int doNonCommutativeOpOnInt(int val1, int val2, int val3, int val4) {
		return (val1 - val2) * (val3 - val4);
	}

    public interface NonCommutativeDoer {
		int nonCommutativeDoOnInt(int val1, int val2, int val3, int val4);
	}

	@Test
	public void testSystemOut() {
		Closure1<String> println = closure(String.class); { of(System.out).println(var(String.class)); }
		println.each("mickey mouse", "donald duck", "uncle scrooge");
	}
	
	@Test
	public void testOnList() {
		StringWriter sw = new StringWriter();
		Closure writer = closure(); { of(sw).write(var(String.class)); }
        assertEquals(1, writer.getFreeVarsNumber());
		writer.each(asList("first", "second", "third"));
		assertEquals("firstsecondthird", sw.toString());
	}

	@Test
	public void testOnArray() {
		StringWriter sw = new StringWriter();
		Closure writer = closure(); { of(sw).append(var(String.class)); }
		writer.each("first", "second", "third");
		assertEquals("firstsecondthird", sw.toString());
		
		writer.apply("forth");
		assertEquals("firstsecondthirdforth", sw.toString());
		
		try {
			writer.apply(3);
			fail("An invocation with the wrong parameter type must fail");
		} catch (WrongClosureInvocationException wcie) { }
		
		try {
			writer.apply("fifth", "sixth");
			fail("An invocation with the wrong parameter number must fail");
		} catch (WrongClosureInvocationException wcie) { }
	}

	@Test
	public void testTypedOnList() {
		StringWriter sw = new StringWriter();
		Closure1<String> writer = closure(String.class); { of(sw).append(var(String.class)); }
		writer.each(asList("first", "second", "third"));
		assertEquals("firstsecondthird", sw.toString());
		writer.apply("forth");
		assertEquals("firstsecondthirdforth", sw.toString());
	}

	@Test
	public void testTypedOnArray() {
		StringBuilder sb = new StringBuilder();
		Closure1<String> appender = closure(String.class); { 
			try {
				of(sb, Appendable.class).append(var(String.class));
			} catch (IOException e) { } 
		} 
		appender.each("first", "second", "third");
		assertEquals("firstsecondthird", sb.toString());
	}
	
	@Test
	public void testAddOnInteger() {
		Closure2<Integer, Integer> adder = closure(Integer.class, Integer.class); { 
			of(this).add(var(Integer.class), var(Integer.class));
		} 
		int sum = (Integer)adder.apply(2, 3);
		assertEquals(2 + 3, sum);
	}

	@Test
	public void testDo4OnInt() {
		Closure4<Integer, Integer, Integer, Integer> adder = closure(Integer.class, Integer.class, Integer.class, Integer.class); {
			of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), var(Integer.class));
		} 
		int result = (Integer)adder.apply(5, 2, 4, 3);
		assertEquals((5 - 2) * (4 - 3), result);
	}

    @Test
    public void testUntypedDo4OnInt() {
        Closure4<Integer, Integer, Integer, Integer> adder = closure(Integer.class, Integer.class, Integer.class, Integer.class)
            .of(this, "doNonCommutativeOpOnInt", var(Integer.class), var(Integer.class), var(Integer.class), var(Integer.class));
        int result = (Integer)adder.apply(5, 2, 4, 3);
        assertEquals((5 - 2) * (4 - 3), result);
    }

	@Test
	public void testDo2OnInt() {
		Closure2<Integer, Integer> adder = closure(Integer.class, Integer.class); { 
			of(this).doNonCommutativeOpOnInt(var(Integer.class), 2, var(Integer.class), 3);
		} 
		int result = (Integer)adder.apply(5, 4);
        assertEquals((5 - 2) * (4 - 3), result);
	}

	@Test
	public void testCurry() {
        Closure4<Integer, Integer, Integer, Integer> closure4 = closure(Integer.class, Integer.class, Integer.class, Integer.class); {
            of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), var(Integer.class));
        }

        assertEquals(4, closure4.getFreeVarsNumber());
        int result = (Integer)closure4.apply(5, 2, 7, 3);
        assertEquals((5 - 2) * (7 - 3), result);

        result = (Integer)closure4.curry1(5).apply(4, 7, 3);
        assertEquals((5 - 4) * (7 - 3), result);
        result = (Integer)closure4.curry2(2).apply(5, 9, 3);
        assertEquals((5 - 2) * (9 - 3), result);
        result = (Integer)closure4.curry3(7).apply(5, 2, 1);
        assertEquals((5 - 2) * (7 - 1), result);
        result = (Integer)closure4.curry4(1).apply(5, 2, 6);
        assertEquals((5 - 2) * (6 - 1), result);

        Closure3<Integer, Integer, Integer> closure3 = closure(Integer.class, Integer.class, Integer.class); {
            of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), 0);
        }
        assertEquals(3, closure3.getFreeVarsNumber());
		result = (Integer)closure3.apply(5, 2, 4);
		assertEquals((5 - 2) * 4, result);
        result = (Integer)closure3.curry1(8).apply(3, 4);
        assertEquals((8 - 3) * 4, result);
        result = (Integer)closure3.curry3(4).apply(5, 2);
        assertEquals((5 - 2) * 4, result);

		Closure2<Integer, Integer> closure2 = closure3.curry2(2);
		result = (Integer)closure2.apply(7, 3);
        assertEquals(2, closure2.getFreeVarsNumber());
		assertEquals((7 - 2) * 3, result);

		Closure1<Integer> closure1 = closure2.curry2(5);
		result = (Integer)closure1.apply(4);
        assertEquals(1, closure1.getFreeVarsNumber());
		assertEquals((4 - 2) * 5, result);

        Converter<Integer, Integer> converter = closure1.cast(Converter.class);
        assertEquals((4 - 2) * 5, converter.convert(4).intValue());

        Iterator<?> results = closure1.each(4, 5, 6).iterator();
        assertEquals((4 - 2) * 5, results.next());
        assertEquals((5 - 2) * 5, results.next());
        assertEquals((6 - 2) * 5, results.next());
        assertFalse(results.hasNext());

		Closure0 closure0 = closure1.curry(9);
		result = (Integer)closure0.apply();
        assertEquals(0, closure0.getFreeVarsNumber());
		assertEquals((9 - 2) * 5, result);
	}

    @Test
    public void testCast() {
        Closure4<Integer, Integer, Integer, Integer> closure4 = closure(Integer.class, Integer.class, Integer.class, Integer.class); {
            of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), var(Integer.class));
        }
        NonCommutativeDoer doer = closure4.cast(NonCommutativeDoer.class);
        int result = doer.nonCommutativeDoOnInt(5, 2, 4, 3);
        assertEquals((5 - 2) * (4 - 3), result);
    }

	@Test
	public void testWrongCurry() {
		Closure closure = closure(); { of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), 10); }
		try {
			closure.curry(3, 4);
			fail("Curry on wrong argument position must fail");
		} catch (IllegalArgumentException iae) { }
	}

	@Test
	public void testWrongCast() {
		Closure closure = closure(); { of(this).doNonCommutativeOpOnInt(var(Integer.class), var(Integer.class), var(Integer.class), 10); }
		try {
			closure.cast(String.class);
			fail("Closure cast on concrete class must fail");
		} catch (IllegalArgumentException iae) { }
		try {
			closure.cast(Iterator.class);
			fail("Closure cast on interface having more than one method  must fail");
		} catch (IllegalArgumentException iae) { }
	}

    public void setAgeOnPerson(Person person, int age) {
        person.setAge(age);
    }

	@Test
	public void testClosureOnNonFinalArgument() {
		Person me = new Person("Mario", "Fusco");
		Closure2<Person, Integer> ageSetter = closure(Person.class, Integer.class); { 
			of(this).setAgeOnPerson(var(Person.class), var(Integer.class));
		}
		ageSetter.apply(me, 35);
		assertEquals(35, me.getAge());
		
		Closure1<Integer> ageSetterOnMyself = ageSetter.curry1(me);
		ageSetterOnMyself.apply(36);
		assertEquals(36, me.getAge());
	}
	
    @Test
    public void testClosureOnClass() {
        Closure2<Person, Integer> ageSetter = closure(Person.class, Integer.class); {
            of(Person.class).setAge(var(Integer.class));
        }
        testClosureOnClass(ageSetter);
    }

    @Test
    public void testNoThreadLocalClosureOnClass() {
        Closure2<Person, Integer> ageSetter = new Closure2<Person, Integer>() {{
            of(Person.class).setAge(var(Integer.class));
        }};
        testClosureOnClass(ageSetter);
    }

    private void testClosureOnClass(Closure2<Person, Integer> ageSetter) {
        Person me = new Person("Mario", "Fusco");
        ageSetter.apply(me, 35);
        assertEquals(35, me.getAge());

        Closure1<Integer> ageSetterOnMyself = ageSetter.curry1(me);
        ageSetterOnMyself.apply(36);
        assertEquals(36, me.getAge());
    }

    @Test
    public void testReturningClosureOnClass() {
        Closure1<Person> ageGetter = closure(Person.class); {
            of(Person.class).getAge();
        }

        Person me = new Person("Mario", "Fusco", 35);
        assertEquals(35, ageGetter.apply(me));

        Closure0 ageGetterOnMyself = ageGetter.curry(me);
        assertEquals(35, ageGetterOnMyself.apply());
    }

    @Test
    public void testWrongClosureOnClass() {
        Closure2<Integer, Person> ageSetter = closure(Integer.class, Person.class); {
            of(Person.class).setAge(var(Integer.class));
        }
        Person me = new Person("Mario", "Fusco");
        try {
            ageSetter.apply(35, me);
            fail("must throw WrongClosureInvocationException");
        } catch (WrongClosureInvocationException e) { }
    }

    @Test
    public void tesStaticClosure() {
        Closure1<String> intParser = closure(String.class).of(Integer.class, "parseInt", var(String.class));
        assertEquals(666, intParser.apply("666"));

        Closure intParser0 = closure().of(Integer.class, "parseInt", "666");
        assertEquals(666, intParser0.apply());
    }

    @Test
    public void tesFinalClosure() {
        Closure1<String> toUpperCase = closure(String.class).of(String.class, "toUpperCase");
        assertEquals("MARIO", toUpperCase.apply("mario"));

        Closure toUpperCaseMe = closure().of("mario", "toUpperCase");
        assertEquals("MARIO", toUpperCaseMe.apply());
    }

    @Test
    public void tesFinalClosureWithParams() {
        Closure3<String, Integer, Integer> varSubstring = closure(String.class, Integer.class, Integer.class)
            .of(String.class, "substring", var(Integer.class), var(Integer.class));
        assertEquals("ar", varSubstring.apply("mario", 1, 3));

        Closure2<Integer, Integer> fixedSubstring = closure(Integer.class, Integer.class)
            .of("mario", "substring", var(Integer.class), var(Integer.class));
        assertEquals("mari", fixedSubstring.apply(0, 4));

        Closure1<String> substring3From2 = closure(String.class).of(String.class, "substring", 2, 5);
        assertEquals("rio", substring3From2.apply("mario"));
    }

    @Test
    public void testNotExistingMethod() {
        try {
            closure().of(String.class, "mario");
            fail("closure creation on non existing method must fail");
        } catch (Exception e) { }
    }

    @Test
    public void testToStringAsClosure() {
        Closure stringifier = closure().of(Object.class, "toString");
        assertEquals("mario", stringifier.apply("mario"));
        assertEquals("123", stringifier.apply(123));

        try {
            stringifier.apply();
            fail("stringifier without any param must fail");
        } catch (WrongClosureInvocationException e) { }
    }

    @Test
    public void testInvocationOnVar() {
        Person me = new Person("Mario");
        Closure ageSetter = closure(); {
            of(me).setAge(var(Person.class).getAge());
        }
        ageSetter.apply(new Person("Mario", 37));
        assertEquals(37, me.getAge());
    }
}
