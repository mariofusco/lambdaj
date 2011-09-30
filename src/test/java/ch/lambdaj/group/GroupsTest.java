// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.mock.ExposureBy.*;
import static java.util.Arrays.*;
import static org.hamcrest.collection.IsCollectionContaining.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import ch.lambdaj.mock.*;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class GroupsTest {

	@Before
	public void setUp() {
		FexFrance1 = new Exposure() {
			{
				setCountryName("France");
				setInsuredName("Fex France 1");
			}
		};

		FexFrance2 = new Exposure() {
			{
				setCountryName("France");
				setInsuredName("Fex France 2");
			}
		};

		FexCanada1 = new Exposure() {
			{
				setCountryName("Canada");
				setInsuredName("Fex Canada 1");
			}
		};

		FexCanada2 = new Exposure() {
			{
				setCountryName("Canada");
				setInsuredName("Fex Canada 2");
			}
		};

		exposures = asList(FexFrance1, FexFrance2, FexCanada1, FexCanada2);
	}

	private Exposure FexFrance1, FexFrance2, FexCanada1, FexCanada2;

	private List<Exposure> exposures;

	@Test
	public void testByCriteria() {
		byExposure().countryName().insuredName();
	}

	@Test
	public void testGroupByCountry() {
		Group<Exposure> group = group(exposures, "countryName");

		assertThat(group.keySet(), hasItems("France", "Canada"));

		Iterable<Exposure> groupFrance = group.find("France");
		assertThat(groupFrance, hasItems(FexFrance1, FexFrance2));

		Iterable<Exposure> groupCanada = group.find("Canada");
		assertThat(groupCanada, hasItems(FexCanada1, FexCanada2));
	}

	@Test
	public void testGroupByInsuredName() {
		Group<Exposure> group = group(exposures, "insuredName");

		assertThat(group.keySet(), hasItems("Fex France 1", "Fex France 2", "Fex Canada 1", "Fex Canada 2"));

		Iterable<Exposure> groupFrance = group.find("Fex France 2");
		assertThat(groupFrance, hasItems(FexFrance2));

		Iterable<Exposure> groupCanada = group.find("Fex Canada 1");
		assertThat(groupCanada, hasItems(FexCanada1));
	}

	@Test
	public void testGroupByCountryAndInsuredName() {
		Group<Exposure> group = group(exposures, "countryName", "insuredName");

		assertThat(group.keySet(), hasItems("France", "Canada"));

		Group<Exposure> groupFrance = group.findGroup("France");
		assertThat(groupFrance.findAll(), hasItems(FexFrance1, FexFrance2));

		Iterable<Exposure> groupFexFrance = groupFrance.find("Fex France 1");
		assertThat(groupFexFrance, hasItems(FexFrance1));

		Group<Exposure> groupCanada = group.findGroup("Canada");
		assertThat(groupCanada.findAll(), hasItems(FexCanada1, FexCanada2));

		Iterable<Exposure> groupFexCanada = groupCanada.find("Fex Canada 2");
		assertThat(groupFexCanada, hasItems(FexCanada2));
	}

	@Test
	public void testGroupTypedByCountryAndInsuredName() {
		Group<Exposure> group = group(exposures, byExposure().countryName().insuredName());

		assertThat(group.keySet(), hasItems("France", "Canada"));

		Group<Exposure> groupFrance = group.findGroup("France");
		assertThat(groupFrance.findAll(), hasItems(FexFrance1, FexFrance2));

		Iterable<Exposure> groupFexFrance = groupFrance.find("Fex France 1");
		assertThat(groupFexFrance, hasItems(FexFrance1));

		Group<Exposure> groupCanada = group.findGroup("Canada");
		assertThat(groupCanada.findAll(), hasItems(FexCanada1, FexCanada2));

		Iterable<Exposure> groupFexCanada = groupCanada.find("Fex Canada 2");
		assertThat(groupFexCanada, hasItems(FexCanada2));
	}
}
