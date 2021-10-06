package com.nitya.accounter.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.utils.HibernateUtil;

public class PaypalTransactionCallbackServlet extends BaseServlet {

	/**
	 * this servlet is called after the paypal integration is done.
	 */
	private static final long serialVersionUID = 1L;
	private boolean sandbox;
	private static String apiUserName;
	private static String apiPassword;
	private static String apiSignature;
	private static String applicationID;
	private String TOKEN;
	private String VERIFICATION_CODE;
	private String accountID;
	private static final String PAYPALINTEGRATION_VIEW = "/WEB-INF/paypalcompleted.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		sandbox = ServerConfiguration.isSandBoxPaypal();
		apiUserName = ServerConfiguration.getPaypalApiUserName();
		apiPassword = ServerConfiguration.getPaypalApiPassword();
		apiSignature = ServerConfiguration.getPaypalApiSignature();
		applicationID = ServerConfiguration.getPaypalApplicationID();
		accountID = req.getParameter("accountID");
		verificationSucessful(req, resp);

		/**
		 * dispatch user to this view to inform his the status and close the
		 * window.
		 */
		dispatch(req, resp, PAYPALINTEGRATION_VIEW);

	}

	/**
	 * this is called from paypal website after verification is done PayPal
	 * returns a verification code
	 * 
	 * @param req
	 * @param resp
	 */
	private void verificationSucessful(HttpServletRequest req,
			HttpServletResponse resp) {

		TOKEN = (String) req.getParameter("request_token");
		VERIFICATION_CODE = (String) req.getParameter("verification_code");
		log("TOKEN : " + TOKEN);
		log("VERIFICATION_CODE : " + VERIFICATION_CODE);

		getAccessToken(req);

	}

	private void getAccessToken(HttpServletRequest req) {

		try {

			URL paypalURL;

			if (sandbox) {
				paypalURL = new URL(
						"https://svcs.sandbox.paypal.com/Permissions/GetAccessToken/");
			} else {
				paypalURL = new URL(
						"https://svcs.paypal.com/Permissions/GetAccessToken/");
			}

			URLConnection permissionConnection = paypalURL.openConnection();
			permissionConnection.setDoOutput(true);
			permissionConnection.setRequestProperty("content-type",
					"application/json; charset=utf-8");

			// Construct HTTP header.
			// Sandbox API credentials for the API Caller account
			permissionConnection.setRequestProperty("X-PAYPAL-SECURITY-USERID",
					apiUserName);
			permissionConnection.setRequestProperty(
					"X-PAYPAL-SECURITY-PASSWORD", apiPassword);
			permissionConnection.setRequestProperty(
					"X-PAYPAL-SECURITY-SIGNATURE", apiSignature);

			// Sandbox Application ID
			permissionConnection.setRequestProperty("X-PAYPAL-APPLICATION-ID",
					applicationID);
			// Input and output formats
			permissionConnection.setRequestProperty(
					"X-PAYPAL-REQUEST-DATA-FORMAT", "JSON");
			permissionConnection.setRequestProperty(
					"X-PAYPAL-RESPONSE-DATA-FORMAT", "JSON");

			JSONObject requestEnv = new JSONObject();
			requestEnv.put("errorLanguage", "en_US");

			// payload input fields,
			JSONObject payLoadInput = new JSONObject();
			payLoadInput.put("requestEnvelope", requestEnv);
			payLoadInput.put("token", TOKEN);
			payLoadInput.put("verifier", VERIFICATION_CODE);

			OutputStreamWriter wr = new OutputStreamWriter(
					permissionConnection.getOutputStream());
			wr.write(payLoadInput.toString());
			wr.flush();

			// Get the response
			BufferedReader paypalResponse = new BufferedReader(
					new InputStreamReader(permissionConnection.getInputStream()));
			String readLine = paypalResponse.readLine();
			log("paypalResponse: " + readLine);

			JSONObject responseObject = new JSONObject(readLine);
			String token = (String) responseObject.get("token");
			String tokenSecret = (String) responseObject.get("tokenSecret");

			saveData(token, tokenSecret);

			wr.close();
			paypalResponse.close();
		} catch (Exception e) {
			log("ERROR: " + e);
		}

	}

	private void saveData(String paypalToken, String tokenSecret) {

		Session currentSession = HibernateUtil.getCurrentSession();
		Transaction transaction = currentSession.beginTransaction();

		Account object = (Account) currentSession.get(Account.class,
				Long.parseLong(accountID));

		ArrayList<Account> transactions = new ArrayList<Account>(currentSession
				.getNamedQuery("get.PayPalAccount.by.AccountID")
				.setParameter("accountID", Long.parseLong(accountID)).list());

		Account account = transactions.get(0);

		object.setPaypalToken(paypalToken);
		object.setPaypalSecretkey(tokenSecret);

		currentSession.save(object);
		transaction.commit();

	}

}
