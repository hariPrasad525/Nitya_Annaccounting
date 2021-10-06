package com.nitya.accounter.core;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuditWriterImpl implements AuditWriter {

	private static final String GAP = "GAP";
	JSONArray auditJsonArray = new JSONArray();

	@Override
	public AuditWriter put(String key, String value) {

		JSONObject jsonObject = new JSONObject();
		try {
			if (value != null)
				jsonObject.put(key, value);
			else
				jsonObject.put(key, " ");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		auditJsonArray.put(jsonObject);

		return this;
	}

	@Override
	public AuditWriter put(String key, boolean value) {

		JSONObject boolObject = new JSONObject();
		try {
			boolObject.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		auditJsonArray.put(boolObject);

		return this;
	}

	@Override
	public AuditWriter put(String key, int value) {

		JSONObject intObject = new JSONObject();
		try {
			intObject.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		auditJsonArray.put(intObject);

		return this;
	}

	@Override
	public AuditWriter put(String key, double value) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		auditJsonArray.put(jsonObject);

		return this;
	}

	@Override
	public AuditWriter put(String key, Double value) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		auditJsonArray.put(jsonObject);

		return this;
	}

	@Override
	public AuditWriter put(String key,
			Collection<? extends IAccounterServerCore> value) {
		JSONArray array = new JSONArray();
		for (IAccounterServerCore a : value) {
			AuditWriterImpl impl = new AuditWriterImpl();
			try {
				a.writeAudit(impl);
				array.put(impl.getJSON());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		this.put(key, array);
		return this;
	}

	private AuditWriter put(String key, JSONArray array) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(key, array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		auditJsonArray.put(jsonObject);

		return this;

	}

	private JSONArray getJSON() {
		return auditJsonArray;
	}

	@Override
	public AuditWriter gap() {

		JSONObject gapObject = new JSONObject();
		auditJsonArray.put(gapObject);

		return this;
	}

	@Override
	public String toString() {
		return getJSON().toString();
	}

}
