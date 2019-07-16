/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.utils;


import java.util.Arrays;
import java.util.List;

public final class StringUtils {

	private StringUtils() {	}

	private static final List<Character> PROPER_CASE_SEPERATOR = Arrays.asList(' ', '-');

	public static String toProperCase(String string) {
		StringBuilder builder = new StringBuilder();

		boolean capitalizeNext = true;
		for (char c : string.toCharArray()) {
			if (capitalizeNext) builder.append(Character.toUpperCase(c));
			else builder.append(Character.toLowerCase(c));

			capitalizeNext = PROPER_CASE_SEPERATOR.contains(c);
		}

		return builder.toString();
	}

}
