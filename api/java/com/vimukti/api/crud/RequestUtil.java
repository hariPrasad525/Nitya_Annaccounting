package com.vimukti.api.crud;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.vimukti.accounter.developer.api.ApiSerializationFactory;
import com.vimukti.api.core.APIConstants;

public class RequestUtil {
	private static final String DATE_FORMAT = "yyyy.MM.dd G 'at' HH:mm:ss z";
	private static final String PREFIX_PATH = "/company/api";

	public static final int REQUEST_CRUD = 1;
	public static final int REQUEST_LISTS = 2;
	public static final int REQUEST_REPORTS = 3;
	public static final int REQUEST_LOGIN = 4;
	public static final int REQUEST_OPERATIONS = 5;
	public static final int REQUEST_UPLOAD_FILE = 6;

	public static final int METHOD_GET = 1;
	public static final int METHOD_PUT = 2;
	public static final int METHOD_DELETE = 3;
	public static final int METHOD_POST = 4;

	private static Map<Integer, String> paths;
	private static ApiSerializationFactory json;
	private static ApiSerializationFactory xml;

	protected static SimpleDateFormat simpleDateFormat;

	static {
		simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

		json = new ApiSerializationFactory(true);
		xml = new ApiSerializationFactory(false);

		paths = new HashMap<Integer, String>();
		paths.put(REQUEST_CRUD, "/crud");
		paths.put(REQUEST_LISTS, "/lists");
		paths.put(REQUEST_REPORTS, "/reports");
		paths.put(REQUEST_LOGIN, "/login");
		paths.put(REQUEST_OPERATIONS, "/operations");
	}

	public static String getPath(int type, int serializationType) {
		String st = "";
		switch (serializationType) {
		case APIConstants.SERIALIZATION_JSON:
			st = "/json";
			break;
		case APIConstants.SERIALIZATION_XML:
			st = "/xml";
			break;
		default:
			break;
		}
		return PREFIX_PATH + st + paths.get(type);
	}

	public static String getDateString(Date date) {
		return simpleDateFormat.format(date);
	}

	public static ApiSerializationFactory getSerializer(int type) {
		switch (type) {
		case APIConstants.SERIALIZATION_JSON:
			return json;
		case APIConstants.SERIALIZATION_XML:
			return xml;
		default:
			return null;
		}

	}

	public static String makeQueryString(Map<String, String> map) {
		Set<Entry<String, String>> entrySet = map.entrySet();
		StringBuilder builder = new StringBuilder();
		for (Entry<String, String> entry : entrySet) {
			builder.append(entry.getKey());
			builder.append("=");
			builder.append(getURLEncode(entry.getValue()));
			builder.append("&");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	private static String getURLEncode(String string) {
		try {
			return URLEncoder.encode(string, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}
}
