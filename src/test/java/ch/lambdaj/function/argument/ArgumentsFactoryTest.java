package ch.lambdaj.function.argument;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

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

        public UnistatiableClass(int intValue, String stringValue) {
            this.intValue = intValue;
            this.stringValue = stringValue;
        }
    }

}
