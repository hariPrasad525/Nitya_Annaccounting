package com.nitya.accounter.mobile;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.server.i18n.ServerSideMessages;

public class MobileServerMessages {
	public static String getMessage(String key) {
		String message = ServerSideMessages.getMessage(key);
		String innerMsg = getInnerMessage(message);
		if (innerMsg != null) {
			return innerMsg;
		}
		int a = message.indexOf('{');
		int b = message.indexOf('}');
		if (a != -1) {
			String innerKey = message.substring(a + 1, b);
			message = message.replaceFirst("\\{.+?\\}",
					getInnerMessage(innerKey));
		}
		return message;
	}

	private static String getInnerMessage(String innerKey) {
		String global = null;
		if (innerKey.equals("Customer")) {
			global = Global.get().Customer();
		} else if (innerKey.equals("customer")) {
			global = Global.get().customer();
		} else if (innerKey.equals("Vendor")) {
			global = Global.get().Vendor();
		} else if (innerKey.equals("vendor")) {
			global = Global.get().vendor();
		} else if (innerKey.equals("Location") || innerKey.equals("location")
				|| innerKey.equals("locations") || innerKey.equals("Locations")) {
			global = Global.get().Location();
		} else if (innerKey.equals("customers")) {
			global = Global.get().customers();
		} else if (innerKey.equals("vendors")) {
			global = Global.get().vendors();
		} else if (innerKey.equals("Customers")) {
			global = Global.get().Customers();
		} else if (innerKey.equals("Vendors")) {
			global = Global.get().Vendors();
		} else if (innerKey.equals("about")) {
			global = ServerSideMessages.getMessage("About");
		}
		return global;
	}
}
