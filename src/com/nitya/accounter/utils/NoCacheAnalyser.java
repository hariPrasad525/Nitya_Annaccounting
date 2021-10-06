package com.nitya.accounter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoCacheAnalyser {

	public static final Permitation SAFARI = new Permitation("true",
			"not_tablet", "safari");
	public static final Permitation IE10 = new Permitation("true", "ie10",
			"ie9");
	public static final Permitation IE9 = new Permitation("true", "not_tablet",
			"ie9");
	public static final Permitation FIREFOX = new Permitation("true",
			"not_tablet", "gecko1_8");
	public static final Permitation IPAD = new Permitation("true", "ipad",
			"safari");
	public static final Permitation ANDROID = new Permitation("true",
			"android", "safari");

	private Map<Permitation, String> perMap = new HashMap<Permitation, String>();

	public NoCacheAnalyser(String data) {
		String lines[] = data.split("\n");
		Pattern pattern = Pattern.compile(",(..)='([^']+)'");
		Pattern mapping = Pattern.compile("G\\(\\[(..),(..),(..)\\],(..)\\);");

		Map<String, String> variables = new HashMap<String, String>();
		for (String line : lines) {
			// System.out.println("Line:" +line);
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				variables.put(matcher.group(1), matcher.group(2));
			}
			matcher = mapping.matcher(line);
			while (matcher.find()) {
				String a = variables.get(matcher.group(1));
				String b = variables.get(matcher.group(2));
				String c = variables.get(matcher.group(3));
				String res = variables.get(matcher.group(4));
				perMap.put(new Permitation(a, b, c), res);
			}
		}

	}

	public Map<Permitation, String> permitations() {
		return perMap;
	}

	public static class Permitation {
		Permitation(String a, String b, String c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}

		String a;
		String b;
		String c;

		@Override
		public int hashCode() {
			return (a + b + c).hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj instanceof Permitation) {
				Permitation x = (Permitation) obj;
				return this.a.equals(x.a) && this.b.equals(x.b)
						&& this.c.equals(x.c);
			}
			return false;
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: java NoCacheAnalyser noche.js");
			return;
		}
		String data = readFile(args[0]);
		NoCacheAnalyser noc = new NoCacheAnalyser(data);
		System.out.println("Safari:" + noc.get(SAFARI));
		System.out.println("Chrome:" + noc.get(SAFARI));
		System.out.println("IPad:" + noc.get(IPAD));
		System.out.println("Android:" + noc.get(ANDROID));
		System.out.println("IE9:" + noc.get(IE9));
		System.out.println("IE10:" + noc.get(IE10));
		System.out.println("Firefox:" + noc.get(FIREFOX));
	}

	public String get(Permitation perm) {
		return perMap.get(perm);
	}

	private static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

}
