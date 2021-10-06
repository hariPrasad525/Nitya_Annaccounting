package com.vimukti.accounter.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.vimukti.accounter.license.LicenseException;

public class Zip {

	public static byte[] compressBytes(String data)
			throws UnsupportedEncodingException, IOException {
		if (data == null) {
			throw new LicenseException("Cann't compress the null data");
		}
		byte[] input = data.getBytes("UTF-8");
		Deflater df = new Deflater();
		df.setInput(input);
		df.finish();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);

		byte[] buff = new byte[Short.MAX_VALUE];
		while (!df.finished()) {
			int count = df.deflate(buff);
			baos.write(buff, 0, count);
		}
		baos.close();
		byte[] output = baos.toByteArray();

		return output;
	}

	public static String extractBytes(byte[] input)
			throws UnsupportedEncodingException, IOException,
			DataFormatException {
		Inflater ifl = new Inflater();
		ifl.setInput(input);

		ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);
		byte[] buff = new byte[Short.MAX_VALUE];
		while (!ifl.finished()) {
			int count = ifl.inflate(buff);
			baos.write(buff, 0, count);
		}
		baos.close();
		byte[] output = baos.toByteArray();

		return new String(output);
	}

	public static void compressFile(String src, String dest)
			throws java.io.IOException {
		java.util.zip.GZIPOutputStream out = new java.util.zip.GZIPOutputStream(
				new java.io.FileOutputStream(dest));
		java.io.FileInputStream in = new java.io.FileInputStream(src);
		byte[] buf = new byte[Short.MAX_VALUE];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		// Complete the GZIP file
		out.finish();
		out.close();
	}

}