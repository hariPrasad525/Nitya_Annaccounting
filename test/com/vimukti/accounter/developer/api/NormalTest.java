package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.mortbay.util.UrlEncoded;
import org.postgresql.util.Base64;

public class NormalTest {
	private static final String SIGNATURE = "Signature";
	private static final String ALGORITHM = "hmacSHA256";
	private static String prefixUrl = "http://localhost:8890/api/xmlreports/";
	private static final String DATE_FORMAT = "yyyy.MM.dd G 'at' HH:mm:ss z";

	private String apikey = "v0l5mtpv";
	private String secretKey = "f8a2yrcoo2zpp5cb";
	private long companyId = 1;
	private SimpleDateFormat simpleDateFormat;

	public NormalTest() {
		simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
	}

	public static void main(String[] args) {
		NormalTest test = new NormalTest();
		try {
			test.testSalesByCustomerSummary();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String doSigning(String url) {
		byte[] secretKeyBytes = secretKey.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, ALGORITHM);
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(keySpec);
			byte[] doFinal = mac.doFinal(url.getBytes());
			String encode = Base64.encodeBytes(doFinal);
			return encode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getURLEncode(String string) {
		return new UrlEncoded(string).encode();
	}

	private void testSalesByCustomerSummary() throws IOException {
		String exprDate = simpleDateFormat.format(System.currentTimeMillis());
		String queryStr = "ApiKey=" + getURLEncode(apikey) + "&CompanyId="
				+ getURLEncode(companyId) + "&Expire=" + getURLEncode(exprDate)
				+ "&StartDate=" + getURLEncode("2011.07.01 AD at 17:18:31 IST")
				+ "&EndDate=" + getURLEncode("2011.08.30 AD at 17:18:31 IST");

		String signature = doSigning(queryStr);
		String queryString = queryStr + "&" + SIGNATURE + "="
				+ getURLEncode(signature);
		sendRequest(prefixUrl + "salesbycustomersummary?" + queryString);
	}

	private String getURLEncode(long num) {
		return getURLEncode(num + "");
	}

	private void sendRequest(String urlStr) throws IOException {

		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		InputStream stream = connection.getInputStream();
		int i = stream.available();
		byte[] data = new byte[i];
		stream.read(data);
		System.out.println(new String(data));
	}
}
