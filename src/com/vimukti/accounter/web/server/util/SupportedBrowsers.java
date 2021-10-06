package com.vimukti.accounter.web.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupportedBrowsers {
	static Set<Pattern> patterns = new HashSet<Pattern>();
	private static final String FILE_NAME = "config/SupportedBrowsers.txt";
	static long lastChanged = 0;

	private static void load() {
		File fileToRead = new File(FILE_NAME);
		if (fileToRead.exists() && fileToRead.lastModified() != lastChanged) {
			patterns.clear();
			try {
				FileReader fr = new FileReader(fileToRead);
				BufferedReader br = new BufferedReader(fr);
				while (true) {
					String line = br.readLine();
					if (line == null)
						break;
					patterns.add(Pattern.compile(line.trim()));
				}
				lastChanged = fileToRead.lastModified();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static boolean check(String header) {
		load();
		for (Pattern p : patterns) {
			Matcher matcher = p.matcher(header);
			if (matcher.matches()) {
				return true;
			}
		}

		return false;

	}
}
