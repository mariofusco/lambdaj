package ch.lambdaj;

import org.junit.*;

import static ch.lambdaj.Lambda.sumFrom;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

/**
 * @author Mario Fusco
 */
public class ConstructorBugTest {
    static class NonInterfaceItem {

      private int value;

      public NonInterfaceItem() {
        setValue(10); // Set default value
      }

      public int getValue() {
        return value;
      }

      public void setValue(int value) {
        this.value = value;
      }
    }

    @Test
    public void callsFromConstructorAreForwarded() {
      NonInterfaceItem item1 = new NonInterfaceItem();
      item1.setValue(20);

      NonInterfaceItem item2 = new NonInterfaceItem();
      item2.setValue(50);

      assertEquals(70, sumFrom(asList(item1, item2)).getValue()); // Fail - is 20
      assertEquals(20, item1.getValue()); // Fail - is 10
      assertEquals(50, item2.getValue()); // Fail - is 10
    }

}
