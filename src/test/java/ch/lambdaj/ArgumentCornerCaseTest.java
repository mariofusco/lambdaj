// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import org.junit.*;

import java.util.*;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

/**
 * @author Mario Fusco
 */
public class ArgumentCornerCaseTest {

    @Test
    public void testSimpleGet() {
        Date expectedMin = new Date(System.currentTimeMillis()-500);

        Intervals intervals1 = new Intervals(new Interval(new Date(), new Date()));
        Intervals intervals2 = new Intervals(new Interval(expectedMin, new Date(System.currentTimeMillis()-400)));
        Intervals intervals3 = new Intervals(new Interval(new Date(System.currentTimeMillis()-300), new Date(System.currentTimeMillis()-200)));

        List<Intervals> intervals = asList(intervals1, intervals2, intervals3);
        Date dateArgument = on(Intervals.class).get().getStart();
        Date min = min(intervals, on(Intervals.class).get().getStart());

        assertEquals("get.start", argument(dateArgument).getInkvokedPropertyName());
        assertEquals(expectedMin, min);
    }

    public static class Intervals {
        List<Interval> intervals;

        public Intervals(Interval interval) {
            intervals = new ArrayList<Interval>();
            intervals.add(interval);
        }

        public Intervals(List<Interval> intervals) {
            this.intervals = intervals;
        }

        public List<Interval> getIntervals() { return intervals; }

        public Interval get() { return intervals.get(0); }
    }

    public static class Interval {
        private Date start;
        private Date end;

        public Interval(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        public Date getStart() { return start; }
        public Date getEnd() { return end; }
    }
}
