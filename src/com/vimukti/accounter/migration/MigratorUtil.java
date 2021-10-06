package com.vimukti.accounter.migration;

import java.util.regex.Pattern;

public class MigratorUtil {

	private static final Pattern EXCLUDED_CHARACTERS = Pattern.compile("[^a-zA-Z0-9+]");

	private static final Pattern START_WITH_NUMBERS = Pattern.compile("^[0-9]*");

	public static String asIdentifier(String param) {
		String replaceAll = EXCLUDED_CHARACTERS.matcher(param).replaceAll("");
		return START_WITH_NUMBERS.matcher(replaceAll).replaceAll("");
	}
}
