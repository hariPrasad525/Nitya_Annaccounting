package com.nitya.accounter.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.nitya.accounter.main.ServerConfiguration;

public class PaypalTransactionPermissionServlet extends BaseServlet {

	/**
	 * this servlet is used to get the permission for paypal account. Which will
	 * store the token provided by Paypal so that we can import transactions
	 * from paypal to accounter
	 */
	private static final long serialVersionUID = 1L;
	private boolean sandbox;
	private String accountID;
	private static String apiUserName;
	private static String apiPassword;
	private static String apiSignature;
	private static String applicationID;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		sandbox = ServerConfiguration.isSandBoxPaypal();
		apiUserName = ServerConfiguration.getPaypalApiUserName();
		apiPassword = ServerConfiguration.getPaypalApiPassword();
		apiSignature = ServerConfiguration.getPaypalApiSignature();
		applicationID = ServerConfiguration.getPaypalApplicationID();

		accountID = req.getParameter("accountID");
		getPermisson(resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

	/**
	 * gets the permission token
	 * 
	 * @param resp
	 */
	public void getPermisson(HttpServletResponse resp) {

		try {

			URL paypalURL;

			if (sandbox) {
				paypalURL = new URL(
						"https://svcs.sandbox.paypal.com/Permissions/RequestPermissions/");
			} else {
				paypalURL = new URL(
						"https://svcs.paypal.com/Permissions/RequestPermissions/");
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

			List<String> permissions = new ArrayList<String>();
			permissions.add("TRANSACTION_SEARCH");
			permissions.add("TRANSACTION_DETAILS");
			
			payLoadInput.put("scope", permissions);
			payLoadInput.put("callback",
					"http://183.82.98.44/main/paypalcallbackservlet?accountID="
							+ accountID);
			payLoadInput.put("requestEnvelope", requestEnv);

			OutputStreamWriter wr = new OutputStreamWriter(
					permissionConnection.getOutputStream());
			wr.write(payLoadInput.toString());
			wr.flush();

			// Get the response
			BufferedReader paypalResponse = new BufferedReader(
					new InputStreamReader(permissionConnection.getInputStream()));
			String readLine = paypalResponse.readLine();
			System.out.println("paypalResponse: " + readLine);

			JSONObject responseJsonObject = new JSONObject(readLine);
			Object token = responseJsonObject.get("token");

			JSONObject responseEnvelope = responseJsonObject
					.getJSONObject("responseEnvelope");
			Object sucessMessage = responseEnvelope.get("ack");

			if (sucessMessage.toString().equals("Success")) {
				openURL(resp, "_grant-permission", token.toString());

			} else {
				log("error in paypal response");
			}

		} catch (Exception e) {
			log("ERROR: " + e);
		}
	}

	/**
	 * Redirect user to the url created. sending the tokens and the cmd.
	 * 
	 * @param resp
	 * 
	 * @param cmd
	 * @param token
	 */
	public void openURL(HttpServletResponse resp, String cmd, String token) {

		try {

			String paypalURL;
			if (sandbox) {
				paypalURL = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=";
			} else {
				paypalURL = "https://www.paypal.com/cgi-bin/webscr?cmd=";
			}
			String location = paypalURL + cmd + "&request_token=" + token;
			log("redirecting to : " + location);
			resp.sendRedirect(location);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
