package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;

public class AuditHistoryView extends BaseView {

	private static final String IS_MODIFIED = "isModified";
	private final int objType;
	private final long objID;
	private final StyledPanel mainStyledPanel;
	private final String pageName;
	private final long activityID;

	@Override
	protected void createButtons() {

	}

	public AuditHistoryView(ClientActivity obj) {
		super();
		this.getElement().setId("AuditHistoryView");
		objType = obj.getObjType();
		objID = obj.getObjectID();
		mainStyledPanel = new StyledPanel("mainStyledPanel");
		pageName = obj.getDataType();
		activityID = obj.getId();
	}

	@Override
	public void init() {

		super.init();
		this.getElement().setId("audithistory");
		createControls();
		// setSize("100%", "100%");

	}

	private void createControls() {
		Accounter.createHomeService().getAuditHistory(objType, objID,
				activityID, new AsyncCallback<ArrayList<ClientActivity>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<ClientActivity> result) {
						if (result != null) {

							showItems(result);
						}
					}

				});
	}

	protected void compareHistoryString(ArrayList<ClientActivity> result) {

		for (int i = 0; i < result.size() - 1; i++) {

			ClientActivity activity1 = result.get(i);
			ClientActivity activity2 = result.get(i + 1);

			String auditHistory1 = activity1.getAuditHistory();
			String auditHistory2 = activity2.getAuditHistory();

			JSONValue jSONValue1 = JSONParser.parseLenient(auditHistory1);
			JSONValue jSONValue2 = JSONParser.parseLenient(auditHistory2);

			compare(jSONValue1.isArray(), jSONValue2.isArray());
		}

	}

	private JSONArray addIsModifiedParameter(JSONArray present) {
		for (int i = 0; i < present.size(); i++) {
			// for string, number, boolean type
			JSONObject obj1 = present.get(i).isObject();
			if (obj1 != null && obj1.toString().length() > 3) {
				obj1.put(IS_MODIFIED, JSONBoolean.getInstance(true));
			}
		}

		return present;
	}

	private void compare(JSONArray prev, JSONArray present) {

		present = addIsModifiedParameter(present);

		for (int i = 0; i < prev.size(); i++) {

			JSONObject object1 = prev.get(i).isObject();
			if (object1 != null) {
				Set<String> keySet = object1.keySet();
				// There should be only one key anyway
				for (String key : keySet) {
					JSONValue value1 = object1.get(key);
					if (value1.isArray() == null) {
						// for comparing , if it is string
						JSONObject object2 = getObjectForKey(key, present);
						if (object2 == null) {
							continue;// nothing to compare
						}
						String value2 = object2.get(key).toString();
						if (value1.toString().equals(value2)) {
							object2.put(IS_MODIFIED,
									JSONBoolean.getInstance(false));
						}
					} else {

						// for checking , if it is an array
						JSONArray itemArray1 = value1.isArray();
						JSONObject object2 = getObjectForKey(key, present);
						if (object2 == null) {
							continue;// nothing to compare
						}
						JSONArray array2 = object2.get(key).isArray();
						if (array2 == null) {
							continue;// nothing to compare
						}

						for (int j = 0; j < itemArray1.size(); j++) {
							JSONArray a1 = itemArray1.get(j).isArray();
							JSONArray a2 = array2.get(j).isArray();

							if (a1 == null || a2 == null) {
								continue;// nothing to compare
							}
							// if sizes are equal then compare the arrays.
							if ((a1.size() == a2.size())) {
								compare(a1, a2);
							}

						}
						// If we have more rows than before then make them all
						// modified
						for (int j = itemArray1.size(); j < array2.size(); j++) {
							JSONArray a2 = array2.get(j).isArray();
							if (a2 != null) {
								addIsModifiedParameter(a2);
							}
						}
					}
				}

			}
		}
	}

	/**
	 * this method is used to return matching array from the second array
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */

	private JSONArray getMatchingArray(JSONArray a1, JSONArray a2) {
		String s1 = a1.toString();
		for (int i = 0; i < a2.size(); i++) {
			String s2 = a2.get(i).toString();
			if (s1.equals(s2)) {
				return a2.get(i).isArray();
			}
		}
		return null;

	}

	/**
	 * Return the object within this array which have the same key as we give
	 * 
	 * @param key
	 * @param array2
	 * @return
	 */
	private JSONObject getObjectForKey(String key, JSONArray array2) {
		for (int i = 0; i < array2.size(); i++) {
			JSONValue value = array2.get(i);
			JSONObject object = value.isObject();
			Set<String> keySet = object.keySet();
			String objKey = keySet.size() > 0 ? keySet.iterator().next() : null;
			if (objKey != null) {
				if (objKey.equals(key)) {
					return object;
				}
			}
		}
		return null;
	}

	protected void showItems(ArrayList<ClientActivity> activityList) {

		String name = pageName + messages.no() + ":" + Long.toString(objID);

		Label pageNameLabel = new Label();
		pageNameLabel.setText(name);
		pageNameLabel.addStyleName("historyPageTitle");

		mainStyledPanel.add(pageNameLabel);
		// mainStyledPanel.setWidth("100%");

		if (activityList.size() > 1) {
			JSONValue val = JSONParser.parseLenient(activityList.get(0)
					.getAuditHistory());
			mainStyledPanel.add(createControl(activityList.get(0), val));
			for (int i = 0; i < activityList.size() - 1; i++) {

				ClientActivity activity1 = activityList.get(i);
				ClientActivity activity2 = activityList.get(i + 1);

				String auditHistory1 = activity1.getAuditHistory();
				String auditHistory2 = activity2.getAuditHistory();

				JSONValue jSONValue1 = JSONParser.parseLenient(auditHistory1);
				JSONValue jSONValue2 = JSONParser.parseLenient(auditHistory2);

				compare(jSONValue1.isArray(), jSONValue2.isArray());

				mainStyledPanel.add(createControl(activityList.get(i + 1),
						jSONValue2));

			}
		} else {
			JSONValue val = JSONParser.parseLenient(activityList.get(0)
					.getAuditHistory());
			mainStyledPanel.add(createControl(activityList.get(0), val));

		}

		this.add(mainStyledPanel);

	}

	private DisclosurePanel createControl(ClientActivity activity,
			JSONValue jSONValue2) {

		// here we are creating the proper format of time to show
		DateTimeFormat datefmt = DateTimeFormat.getFormat(Accounter
				.getCompany().getPreferences().getDateFormat());
		String dateformat = datefmt.format(new Date(activity.getTime()));
		DateTimeFormat timefmt = DateTimeFormat.getFormat("h:mm a");
		String timeFormat = timefmt.format(new Date(activity.getTime()));

		// created the title
		String title = messages.Modifiedby(activity.getUserName(), dateformat,
				timeFormat);

		DisclosurePanel panel = new DisclosurePanel(title);
		// panel.setWidth("100%");
		JSONValue value = JSONParser.parseLenient(activity.getAuditHistory());
		AuditHistoryItem item = new AuditHistoryItem(jSONValue2.isArray());
		panel.setContent(item);
		return panel;
	}

	public AuditHistoryItem getHistoryItem(String currentItem,
			String previousItem) {
		return null;

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

}
