package ch.lambdaj.function.closure;

import org.junit.*;

import java.util.*;

import static ch.lambdaj.Lambda.*;
import static junit.framework.Assert.assertEquals;

/**
 * @author Mario Fusco
 */
public class FaillingClosureTest {

    public <T> T speculate(Collection<Closure> closures){
        T result = null;
        boolean found = false;
        for (Iterator<Closure> it = closures.iterator();it.hasNext() && !found;){
            try{
                Closure c = it.next();
                T o = (T) c.apply(new Object[]{});
                if (o != null){
                    result = o;
                    found = true;
                }
            } catch (Exception ex){
                System.out.println("Some closure failed");
            }
        }
        return result;
    }

    @Test
    public void testSpeculation(){
        Integer uno = 1; // Using Integer objects just for example purposes
        Integer dos = 2;
        Integer tres = null;
        Closure goodClosure = closure();{ of(this).sum(uno,dos); }
        Closure badClosure = closure();{ of(this).sum(uno,tres); }
        List<Closure> closures = Arrays.asList(goodClosure,badClosure);
         /* Looking for the right calculation */
        Integer i = this.speculate(closures);
         /* Print whatever the speculation returns */
        assertEquals(i, (Integer)3);
    }

    public int sum(Integer uno,Integer dos){
            return uno+dos;
    }
}
