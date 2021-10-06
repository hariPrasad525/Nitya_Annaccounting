package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

public interface IMigrator<T> {

	JSONObject migrate(T obj, MigratorContext context) throws JSONException;

	void addRestrictions(Criteria criteria);

}
