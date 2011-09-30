// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import net.sf.json.*;
import net.sf.json.util.*;


/**
 * @author Mario Fusco
 * @author Luca Marrocco
 */
public class Strings {

	public static String json(Object object) {
		if (JSONUtils.isArray(object)) return JSONArray.fromObject(object).toString();
		if (JSONUtils.isObject(object)) return JSONObject.fromObject(object).toString();
		return object.toString();
	}

	public static String removeApos(String json) {
		return json.replaceAll("\"", "");
	}

	public static String removeString(String json, String match) {
		return json.replace(match, "");
	}
}
