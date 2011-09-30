// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.mock;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.*;

/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class Exposure {
	private String countryName;

	private String insuredName;

	private String countryIso;

	private String countryFlag;

	public Exposure() {}

	public Exposure(String countryName, String insuredName) {
		this.countryName = countryName;
		this.insuredName = insuredName;
	}

	public String getCountryFlag() {
		return countryFlag;
	}

	public String getCountryIso() {
		return countryIso;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setCountryFlag(String countryFlag) {
		this.countryFlag = countryFlag;
	}

	public void setCountryIso(String countryIso) {
		this.countryIso = countryIso;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	@Override
	public String toString() {
		return join(forEach(asList(countryName, insuredName)));
	}
}
