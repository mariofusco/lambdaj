// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static ch.lambdaj.function.argument.ArgumentsFactory.createArgumentPlaceholder;
import static ch.lambdaj.function.argument.ArgumentsFactory.registerFinalClassArgumentCreator;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

public class ArgumentsFactoryTest {

	@Test
	public void testCreateArgumentPlaceholder() {
		assertThat(createArgumentPlaceholder(String.class), instanceOf(String.class));
        assertThat(createArgumentPlaceholder(StringBuilder.class), instanceOf(StringBuilder.class));
		assertThat(createArgumentPlaceholder(Integer.class), instanceOf(Integer.class));
		assertThat(createArgumentPlaceholder(Long.class), instanceOf(Long.class));
		assertThat(createArgumentPlaceholder(Long.TYPE), instanceOf(Long.class));
		assertThat(createArgumentPlaceholder(Float.class), instanceOf(Float.class));
		assertThat(createArgumentPlaceholder(Double.class), instanceOf(Double.class));
		assertThat(createArgumentPlaceholder(Short.class), instanceOf(Short.class));
		assertThat(createArgumentPlaceholder(Short.TYPE), instanceOf(Short.class));
		assertThat(createArgumentPlaceholder(Byte.class), instanceOf(Byte.class));
		assertThat(createArgumentPlaceholder(Boolean.class), instanceOf(Boolean.class));
        assertThat(createArgumentPlaceholder(Character.class), instanceOf(Character.class));
        assertThat(createArgumentPlaceholder(Character.TYPE), instanceOf(Character.class));
		assertThat(createArgumentPlaceholder(Date.class), instanceOf(Date.class));
		assertThat(createArgumentPlaceholder(UUID.class), instanceOf(UUID.class));
	}

    @Test
    public void testCreateArgumentPlaceholderForUnknownClass() {
        assertFalse(createArgumentPlaceholder(IntegerWrapper.class).equals(createArgumentPlaceholder(IntegerWrapper.class)));
        assertFalse(createArgumentPlaceholder(StringWrapper.class).equals(createArgumentPlaceholder(StringWrapper.class)));

        try {
            createArgumentPlaceholder(UnistatiableClass.class);
            fail("Should not be possible to instanciate an argument placeholder for UnistatiableClass");
        } catch (ArgumentConversionException e) { }
    }

    @Test
    public void testArgumentsFactoryForFinalClasses() {
        assertFalse(createArgumentPlaceholder(DateTime.class).equals(createArgumentPlaceholder(DateTime.class)));
        registerFinalClassArgumentCreator(LocalDate.class, new LocalDateArgumentCreator());
        assertFalse(createArgumentPlaceholder(LocalDate.class).equals(createArgumentPlaceholder(LocalDate.class)));
        ArgumentsFactory.deregisterFinalClassArgumentCreator(LocalDate.class);
        assertTrue(createArgumentPlaceholder(LocalDate.class).equals(createArgumentPlaceholder(LocalDate.class)));
    }

    public static class LocalDateArgumentCreator implements FinalClassArgumentCreator<LocalDate> {
        private final long MSECS_IN_DAY = 1000L * 60L * 60L * 24L;
        public LocalDate createArgumentPlaceHolder(int seed) {
            return new LocalDate((long)seed * MSECS_IN_DAY);
        }
    }

    public static final class IntegerWrapper {
        private int value;
        public IntegerWrapper(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof IntegerWrapper && ((IntegerWrapper)obj).value == value;
        }
    }

    public static final class StringWrapper {
        private String value;
        public StringWrapper(String value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof StringWrapper && ((StringWrapper)obj).value.equals(value);
        }
    }

    public static final class UnistatiableClass {
        private int intValue;
        private String stringValue;
        private long longValue;

        public UnistatiableClass(int intValue, String stringValue, long longValue) {
            this.intValue = intValue;
            this.stringValue = stringValue;
            this.longValue = longValue;
        }
    }

}
