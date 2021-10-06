package com.nitya.accounter.migration;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import com.nitya.accounter.core.CreatableObject;

public class CommonFieldsMigrator {

	public static void migrateCommonFields(CreatableObject obj, JSONObject json, MigratorContext context)
			throws JSONException {
		Timestamp createdDate = obj.getCreatedDate();
		if (createdDate != null) {
			json.put("createdDate", String.valueOf(createdDate.getTime()));
		}
		Timestamp lastModifiedDate = obj.getLastModifiedDate();
		if (lastModifiedDate != null) {
			json.put("lastModifiedDate", String.valueOf(lastModifiedDate.getTime()));
		}
	}
}
