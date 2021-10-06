package com.nitya.accounter.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONException;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.PaypalTransation;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.utils.HibernateUtil;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.exceptions.OAuthException;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.NVPCallerServices;
import com.paypal.sdk.util.OAuthSignature;
import com.paypal.sdk.util.OAuthSignature.HTTPMethod;

public class PaypalTransactionSearch {

	private ArrayList<PaypalTransation> paypalTransaction;
	private String timeStamp;
	private String signature;
	private Long accountID;
	private static String apiUserName;
	private static String apiPassword;
	private static String accessToken;
	private static String tokenSecret;
	private static HTTPMethod httpMethod = OAuthSignature.HTTPMethod.POST;
	private static String scriptURI = "https://api.sandbox.paypal.com/nvp";
	private static Map queryParams = null;

	public ArrayList<PaypalTransation> getTransaction(String startDate,
			Company company, String accessToken, String tokenSecret,
			Long accountID) throws JSONException, IOException {

		paypalTransaction = new ArrayList<PaypalTransation>();
		apiUserName = ServerConfiguration.getPaypalApiUserName();
		apiPassword = ServerConfiguration.getPaypalApiPassword();
		this.accessToken = accessToken;
		this.tokenSecret = tokenSecret;
		this.accountID = accountID;

		Map map = null;
		try {
			map = OAuthSignature.getAuthHeader(apiUserName, apiPassword,
					accessToken, tokenSecret, httpMethod, scriptURI,
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

		readPaypalData(startDate, company);

		return paypalTransaction;
	}

	private void createTransactionList(NVPDecoder decoder, Company company) {

		Map<String, String> map = new HashMap<String, String>();
		map = decoder.getMap();

		List<String> dateList = new ArrayList<String>();
		List<String> timeList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		List<String> emailList = new ArrayList<String>();
		List<String> nameList = new ArrayList<String>();
		List<String> transactoinList = new ArrayList<String>();
		List<String> statusList = new ArrayList<String>();
		List<String> amtList = new ArrayList<String>();
		List<String> feeList = new ArrayList<String>();
		List<String> netList = new ArrayList<String>();
		List<String> currencyList = new ArrayList<String>();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key.contains("L_TIMESTAMP")) {
				dateList.add(value);
			} else if (key.contains("L_TIMEZONE")) {
				timeList.add(value);
			} else if (key.contains("L_TYPE")) {
				typeList.add(value);
			} else if (key.contains("L_EMAIL")) {
				emailList.add(value);
			} else if (key.contains("L_NAME")) {
				nameList.add(value);
			} else if (key.contains("L_TRANSACTIONID")) {
				transactoinList.add(value);
			} else if (key.contains("L_STATUS")) {
				statusList.add(value);
			} else if (key.contains("L_AMT")) {
				amtList.add(value);
			} else if (key.contains("L_FEEAMT")) {
				feeList.add(value);
			} else if (key.contains("L_NETAMT")) {
				netList.add(value);
			} else if (key.contains("L_CURRENCYCODE")) {
				currencyList.add(value);
			} else {
			}
		}

		Session currentSession = HibernateUtil.getCurrentSession();
		Transaction transaction2 = currentSession.getTransaction();
		transaction2.begin();

		for (int j = 0; j < nameList.size(); j++) {
			PaypalTransation transaction = new PaypalTransation();
			transaction.setDate((dateList.size() > j) ? dateList.get(j) : "");
			transaction.setTimeZone((timeList.size() > j) ? timeList.get(j)
					: "");
			transaction.setType((typeList.size() > j) ? typeList.get(j) : "");
			transaction
					.setEmail((emailList.size() > j) ? emailList.get(j) : "");
			transaction.setBuyerName((nameList.size() > j) ? nameList.get(j)
					: "");
			transaction.setTransactionID(transactoinList.get(j));
			transaction
					.setTransactionStatus((statusList.size() > j) ? statusList
							.get(j) : "");
			transaction.setGrossAmount((amtList.size() > j) ? amtList.get(j)
					: "");
			transaction.setPaypalFees((feeList.size() > j) ? feeList.get(j)
					: "");
			transaction
					.setNetAmount((netList.size() > j) ? netList.get(j) : "");
			transaction
					.setCurrencyCode((currencyList.size() > j) ? currencyList
							.get(j) : "");
			transaction.setCompany(company);
			transaction.setAccountID(accountID);

			ArrayList<PaypalTransation> transactions = new ArrayList<PaypalTransation>(
					currentSession
							.getNamedQuery("list.CheckPaypalTransactions")
							.setEntity("company", company)
							.setParameter("transactionID",
									transactoinList.get(j)).list());
			if (transactions.size() < 1) {
				paypalTransaction.add(transaction);
				currentSession.save(transaction);

				Account account = (Account) currentSession.get(Account.class,
						accountID);
				FinanceDate date = new FinanceDate();
				account.setEndDate(date);
				currentSession.update(account);

			}
		}
		transaction2.commit();

	}

	public void readPaypalData(String startDate, Company company) {

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
			profile.setSubject("");
			caller.setAPIProfile(profile);

			encoder.add("VERSION", "51.0");
			encoder.add("METHOD", "TransactionSearch");

			// Add request-specific fields to the request string.
			// encoder.add("TRXTYPE", "Q");

			DateFormat dfRead = new SimpleDateFormat("MM/dd/yyyy");
			if (startDate != null && !startDate.equals("")) {
				Calendar startDateObj = Calendar.getInstance();
				startDateObj.setTime(dfRead.parse(startDate));
				encoder.add("STARTDATE", startDateObj.get(Calendar.YEAR) + "-"
						+ (startDateObj.get(Calendar.MONTH) + 1) + "-"
						+ startDateObj.get(Calendar.DAY_OF_MONTH)
						+ "T00:00:00Z");
			}

			// if (endDate != null && !endDate.equals("")) {
			// Calendar endDateObj = Calendar.getInstance();
			// endDateObj.setTime(dfRead.parse(endDate));
			// encoder.add("ENDDATE", endDateObj.get(Calendar.YEAR) + "-"
			// + (endDateObj.get(Calendar.MONTH) + 1) + "-"
			// + endDateObj.get(Calendar.DAY_OF_MONTH) + "T24:00:00Z");
			// }
			encoder.add("TRANSACTIONCLASS", "All");
			// Date format from server is 2006-9-6T0:0:0.
			// encoder.add("TRANSACTIONID", transactionID);

			// Execute the API operation and obtain the response.
			String request = encoder.encode();
			String response = (String) caller.call(request);

			decoder.decode(response);

			Map map = decoder.getMap();
			createTransactionList(decoder, company);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
