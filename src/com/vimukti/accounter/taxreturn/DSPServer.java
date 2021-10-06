package com.vimukti.accounter.taxreturn;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.thoughtworks.xstream.XStream;
import com.vimukti.accounter.taxreturn.core.GovTalkError;
import com.vimukti.accounter.taxreturn.core.GovTalkErrors;
import com.vimukti.accounter.taxreturn.core.GovTalkMessage;
import com.vimukti.accounter.taxreturn.core.ResponseEndPoint;

public class DSPServer extends Thread {
	private static LinkedBlockingQueue<DSPMessage> queue = new LinkedBlockingQueue<DSPMessage>();
	private static final String SUBMISSION_URL = "http://localhost:5665/LTS/LTSPostServlet";
	// https://www.tpvs.hmrc.gov.uk/HMRC/VATDEC;
	// http://localhost:5665/LTS/LTSPostServlet;
	// https://secure.gateway.gov.uk/submission
	private XStream xStream;
	private static DSPServer instance = new DSPServer();

	private DSPServer() {
		init();
		xStream = XStreamUtil.getXstream();
	}

	@Override
	public void run() {
		while (true) {
			try {
				DSPMessage take = queue.take();
				submit(take);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void init() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) throws Exception {
		new DSPServer().send(SUBMISSION_URL,
				GovTalkMessageGenerator.getRequestMessage(Test.getMessage()));
	}

	public void submit(DSPMessage message) throws Exception {

		// Request
		GovTalkMessage response = send(SUBMISSION_URL,
				GovTalkMessageGenerator.getRequestMessage(message));

		if (isContainErrors(response)) {
			// TODO
			GovTalkErrors govTalkErrors = response.getGovtTalkDetails()
					.getGovTalkErrors();
			System.err.println(govTalkErrors.getErrors().get(0).getText());
			return;
		}

		ResponseEndPoint responseEndPoint = response.getHeader()
				.getMessageDatails().getResponseEndPoint();
		wait(Long.parseLong(responseEndPoint.getPollInterval()));

		// Poll "https://secure.gateway.gov.uk/poll"
		response = send(
				responseEndPoint.getValue(),
				GovTalkMessageGenerator.getPollMessage(response.getHeader()
						.getMessageDatails().getCorrelationID()));
		if (isContainErrors(response)) {
			// TODO
		}

		responseEndPoint = response.getHeader().getMessageDatails()
				.getResponseEndPoint();
		wait(Long.parseLong(responseEndPoint.getPollInterval()));

		// Re-Poll
		response = send(
				responseEndPoint.getValue(),
				GovTalkMessageGenerator.getPollMessage(response.getHeader()
						.getMessageDatails().getCorrelationID()));

		if (isContainErrors(response)) {
			// TODO
		}

		// DELETE
		response = send(
				response.getHeader().getMessageDatails().getResponseEndPoint()
						.getValue(),
				GovTalkMessageGenerator.getDeleteMessage(response.getHeader()
						.getMessageDatails().getCorrelationID()));
	}

	private boolean isContainErrors(GovTalkMessage response) {
		if (response == null) {
			return true;
		}
		GovTalkErrors govTalkErrors = response.getGovtTalkDetails()
				.getGovTalkErrors();
		if (govTalkErrors == null) {
			return false;
		}
		List<GovTalkError> errors = govTalkErrors.getErrors();
		return !errors.isEmpty();
	}

	// private String getMD5(String password) throws Exception {
	// byte[] digest = MessageDigest.getInstance("MD5").digest(
	// password.toLowerCase().getBytes("UTF-8"));
	// return new Base64Encoder().encode(digest);
	// }

	private GovTalkMessage send(String path, GovTalkMessage message) {
		try {
			URL ourURL = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) ourURL
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; JVM)");
			conn.setDoOutput(true);

			// FileInputStream fileInputStream = new FileInputStream(
			// new File(
			// "C:/Users/vimukti04/Desktop/accounter/HMRC-VAT/LTS3.10/HMRCTools/TestData/New folder",
			// "vatrequest.xml"));
			// byte[] data = new byte[fileInputStream.available()];
			// fileInputStream.read(data);
			// OutputStream outputStream = conn.getOutputStream();
			// outputStream.write(data);
			// outputStream.flush();
			// outputStream.close();

			message.toXML(conn.getOutputStream());

			InputStream inputStream = conn.getInputStream();
			byte[] data1 = new byte[inputStream.available()];
			inputStream.read(data1);
			System.out.println(new String(data1));

			return null;// (GovTalkMessage)
						// xStream.fromXML(conn.getInputStream());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized static void put(DSPMessage message) {
		try {
			queue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static DSPServer getInstance() {
		return instance;
	}
}
