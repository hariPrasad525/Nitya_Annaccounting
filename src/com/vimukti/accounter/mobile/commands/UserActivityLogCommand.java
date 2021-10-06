package com.vimukti.accounter.mobile.commands;

import java.text.SimpleDateFormat;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.server.FinanceTool;

public class UserActivityLogCommand extends AbstractCommand {

	// private static final String FROM_DATE = "fromdate";
	// private static final String END_DATE = "enddate";
	private int startIndex = 0;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().fromDate()), getMessages().fromDate(), true, true));

		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getMessages().toDate()), getMessages().toDate(), true, true));

		list.add(new ShowListRequirement<ClientActivity>("activitylog", "", 20) {

			@Override
			protected List<ClientActivity> getLists(Context context) {
				return getActivityList(context);
			}

			@Override
			protected String onSelection(ClientActivity value) {
				return null;
			}

			@Override
			protected Record createRecord(ClientActivity value) {
				Record record = new Record(value);
				SimpleDateFormat format = new SimpleDateFormat(getPreferences()
						.getDateFormat());
				String format2 = format.format(value.getTime());
				record.add(getMessages().date(), format2);
				record.add(getMessages().userName(), value.getUserName());
				record.add(getMessages().activity(), getActivityDataType(value));
				record.add(getMessages().amount(), getAmountWithCurrency(value
						.getAmount() == null ? 0.0 : value.getAmount()));
				return record;
			}

			@Override
			protected boolean filter(ClientActivity e, String name) {
				return e.getName() == null ? true : e.getName()
						.startsWith(name);
			}

			@Override
			protected String getShowMessage() {
				return getMessages().usersActivityLogTitle();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}
		});
	}

	private List<ClientActivity> getActivityList(Context context) {

		ClientFinanceDate startDate = get(FROM_DATE).getValue();
		ClientFinanceDate endDate = get(TO_DATE).getValue();

		List<ClientActivity> activities = new FinanceTool().getUserManager()
				.getUsersActivityLog(startDate, endDate, 0, -1,
						context.getCompany().getId(), 0);

		startIndex += activities.size();

		return activities;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(FROM_DATE).setDefaultValue(new ClientFinanceDate());
		get(TO_DATE).setDefaultValue(new ClientFinanceDate());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	protected String getActivityDataType(ClientActivity activity) {
		String dataType = "";
		StringBuffer buffer = new StringBuffer();
		int type = activity.getActivityType();
		switch (type) {
		case 0:
			return dataType = getMessages().loggedIn();
		case 1:
			return dataType = getMessages().loggedOut();
		case 2:
			buffer.append(getMessages().added());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 3:
			buffer.append(getMessages().edited());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 4:
			buffer.append(getMessages().deleted());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 5:
			return dataType = getMessages().updatedPreferences();
		default:
			break;
		}
		return null;
	}
}
