// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import org.junit.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CreatingArgumentFromArrayTest {

    @Test
    public void testCreatingArgumentFromArray() {
        ICompilationUnit compilationUnit = mock(ICompilationUnit.class);
        ICompilationUnit[] units = new ICompilationUnit[] { compilationUnit };

        ICompilationUnit[] argument = ArgumentsFactory.createArgument(units.getClass());

        assertThat(argument, is(instanceOf(ICompilationUnit[].class)));
    }

    public interface ICompilationUnit { }

    public interface IPackageFragment {
        ICompilationUnit[] getCompilationUnits();
    }
}
