// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.mock;

import ch.lambdaj.group.*;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
@SuppressWarnings("serial")
public class ExposureBy extends GroupConditions {

	public static ExposureBy byExposure() {
		return new ExposureBy();
	}

	public ExposureBy insuredName() {
		by("insuredName");
		return this;
	}

	public ExposureBy countryName() {
		by("countryName");
		return this;
	}

	public ExposureBy asInsureds() {
		as("insureds");
		return this;
	}

	public ExposureBy asCountries() {
		as("countries");
		return this;
	}

	public ExposureBy asExposures() {
		as("exposures");
		return this;
	}

	public ExposureBy headCountryIso() {
		head("countryIso");
		return this;
	}

	public ExposureBy headCountryIsoAsCode() {
		head("countryIso", "code");
		return this;
	}
}