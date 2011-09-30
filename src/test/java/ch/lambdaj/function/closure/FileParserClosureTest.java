// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import static ch.lambdaj.Lambda.*;
import static org.junit.Assert.*;

import org.junit.*;

import java.io.*;

/**
 * @author Mario Fusco
 */
public class FileParserClosureTest {

    private int lineCounter = 0;

    void countNonEmptyLine(String line) {
        if (line != null && line.trim().length() > 0) lineCounter++;
    }

    @Test
    public void testPrintFile() {
        Closure1<String> lineReader = closure(String.class); { of(System.out).println(var(String.class)); }
        readFileByLine("commedia.txt", lineReader);
    }

    @Test
    public void testReadFile() {
        StringWriter sw = new StringWriter();
        Closure1<String> lineReader = closure(String.class); { of(sw).write(var(String.class)); }
        readFileByLine("commedia.txt", lineReader);
    }

    @Test
    public void testCountFile() {
        lineCounter = 0;
        Closure1<String> lineReader = closure(String.class); { of(this).countNonEmptyLine(var(String.class)); }
        readFileByLine("commedia.txt", lineReader);
        assertEquals(9, lineCounter);
    }

    private void readFileByLine(String fileName, Closure1<String> lineReader) {
        BufferedReader reader = null;
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
            reader = new BufferedReader(new InputStreamReader(stream));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lineReader.apply(line);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Error while reading file " + fileName, ioe);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ioe) {
                throw new RuntimeException("Error while closing file reader", ioe);
            }
        }
    }
}
