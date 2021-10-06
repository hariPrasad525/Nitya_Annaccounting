package com.vimukti.accounter.main;

public class Main {

	static final String HOME = System.getProperty("user.home");

	static final String FS = System.getProperty("file.separator");

	protected static boolean checkDebugMode(String[] args) {
		for (String arg : args) {
			if (arg.equals("-debug")) {
				return true;
			}
		}
		return false;
	}

	protected static String getArgument(String[] args, String name) {
		for (int i = 0; i < args.length - 1; i++) {
			String arg = args[i];
			if (arg.equals(name)) {
				return args[i + 1];
			}
		}
		return null;
	}

}
