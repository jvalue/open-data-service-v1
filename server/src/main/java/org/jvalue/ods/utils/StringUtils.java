package org.jvalue.ods.utils;


import java.util.Arrays;
import java.util.List;

public final class StringUtils {

	private StringUtils() { }

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


	public static String getUriRootElement(final String uri) {
	    String[] addrParts = uri.split("/");

	      if(addrParts.length == 1) { //path is already pointing at root level
	        return "";
	    }
	    else {
	        return addrParts[addrParts.length - 2];
        }
    }
}
