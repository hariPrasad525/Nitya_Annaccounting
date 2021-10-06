package com.vimukti.accounter.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class InAppReceiptVerificationServlet extends HttpServlet {

	/**
	 * this servlet is used to verify the receipt it will get form the Accounter
	 * Application for inAppPurchase. The servlet will send the receipt data to
	 * the itunes verification url
	 */
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(InAppReceiptVerificationServlet.class);


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String receipt = req.getParameter("receipt");
		String isSandBox = req.getParameter("sandbox");

		// build the post data
		JSONObject object = new JSONObject();
		object.put("receipt-data", "receipt");
		String verificationResp = verifyReceipt(object, true);

	}

	/**
	 * receive the receipt data which will be in json string format. and check
	 * if it is sandbox or not
	 * 
	 * @param receiptData
	 * @param isSandbox
	 * @return
	 */
	String verifyReceipt(JSONObject receiptData, boolean isSandbox) {
		log.info("--------------------- APP STORE VERIFICATION STARTED ------------------------");
		String verificationResp = "VERIFIED";
		try {
			URL verifyUrl = null;
			if (isSandbox) {
				verifyUrl = new URL(
						"https://sandbox.itunes.apple.com/verifyReceipt");
			} else {
				verifyUrl = new URL(
						"https://buy.itunes.apple.com/verifyReceipt");
			}

			log.info("Opening App Store Verify Connection with URL "
					+ verifyUrl);
			URLConnection verifyConnection = verifyUrl.openConnection();
			verifyConnection.setDoOutput(true);
			verifyConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			PrintWriter pw = new PrintWriter(verifyConnection.getOutputStream());
			log.info("Writting Params to 'Verify Connection' - " + receiptData);
			pw.println(receiptData);
			pw.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					verifyConnection.getInputStream()));
			verificationResp = in.readLine();
			in.close();

			log.info("Response of App Store Verification - " + verificationResp);

		} catch (Exception e) {
			log.error("Exception while Verifying : ", e);
		}
		log.info("Verification Completed !!");

		return verificationResp;

	}
}
