package com.nitya.accounter.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;

import com.nitya.accounter.main.ServerConfiguration;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.exceptions.OAuthException;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.NVPCallerServices;
import com.paypal.sdk.util.OAuthSignature;
import com.paypal.sdk.util.OAuthSignature.HTTPMethod;

public class PaypalTransactionDetails {

	private String timeStamp;
	private String signature;
	private String transactionID;
	private String apiSignature;
	private static String apiUserName;
	private static String apiPassword;
	private static String accessToken;
	private static String tokenSecret;
	private static HTTPMethod httpMethod = OAuthSignature.HTTPMethod.POST;
	private static String scriptURI = "https://api-3t.sandbox.paypal.com/nvp";
	private static Map queryParams = null;

	public PaypalTransactionDetails(String transactionID, String paypalToken,
			String paypalSecretkey) {

		apiUserName = ServerConfiguration.getPaypalApiUserName();
		apiPassword = ServerConfiguration.getPaypalApiPassword();
		this.accessToken = paypalToken;
		this.tokenSecret = paypalSecretkey;
		apiSignature = ServerConfiguration.getPaypalApiSignature();
		this.transactionID = transactionID;

	}

	public void createHeader() throws JSONException, IOException {

		Map map = null;
		try {
			map = OAuthSignature.getAuthHeader(apiUserName, apiPassword,
					this.accessToken, this.tokenSecret, httpMethod, scriptURI,
					queryParams);
		} catch (OAuthException e) {
			e.printStackTrace();
		}
		// Display Signature and Timestamp to console.
		Iterator itr = map.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			System.out.println(entry.getKey() + ": " + entry.getValue());
			if (entry.getKey().equals("TimeStamp")) {
				timeStamp = entry.getValue().toString();
			}
			if (entry.getKey().equals("Signature")) {
				signature = entry.getValue().toString();
			}
		}

	}

	public void getTransactionDetails() {

		NVPCallerServices caller = null;
		NVPEncoder encoder = new NVPEncoder();
		NVPDecoder decoder = new NVPDecoder();

		try {
			caller = new NVPCallerServices();
			APIProfile profile = ProfileFactory.createPermissionAPIProfile();

			/*
			 * WARNING: Do not embed plaintext credentials in your application
			 * code. Doing so is insecure and against best practices. Your API
			 * credentials must be handled securely. Please consider encrypting
			 * them for use in any production environment, and ensure that only
			 * authorized individuals may view or modify them.
			 */
			profile.setAPIUsername(apiUserName);
			profile.setAPIPassword(apiPassword);
			profile.setOauth_Token(accessToken);
			profile.setOauth_Signature(signature);
			profile.setOauth_Timestamp(timeStamp);
			profile.setEnvironment("sandbox");
			// profile.setSubject("");
			caller.setAPIProfile(profile);

			encoder.add("VERSION", "51.0");
			encoder.add("METHOD", "GetTransactionDetails");

			// Add request-specific fields to the request string.
			encoder.add("TRANSACTIONID", transactionID);
			System.out.println("TRansaction ID : " + transactionID);

			// Execute the API operation and obtain the response.
			String NVPRequest = encoder.encode();
			String NVPResponse = caller.call(NVPRequest);
			decoder.decode(NVPResponse);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String string = decoder.get("ACK");
		System.out.println(string);
	}

}
