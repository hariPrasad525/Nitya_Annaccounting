package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class CreditRatingListCommand extends AbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<CreditRating>("creditRatingList",
				getMessages().pleaseSelect(getMessages().creditRating()), 20) {
			@Override
			protected Record createRecord(CreditRating value) {
				Record record = new Record(value);
				record.add(value.getName());
				return record;
			}

			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// CreditRating value) {
			// commandList.add(new UserCommand("Edit Credit Rating", value
			// .getName()));
			//
			// commandList.add(new UserCommand("Delete Credit Rating", value
			// .getName()));
			// }
			@Override
			protected String onSelection(CreditRating value) {
				return "editCreditRating " + value.getName();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("editCreditRating");
			}

			@Override
			protected boolean filter(CreditRating e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<CreditRating> getLists(Context context) {
				return getCreditRatingList(context);
			}

			@Override
			protected String getShowMessage() {
				return getMessages().creditRatingList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

		});
	}

	private List<CreditRating> getCreditRatingList(Context context) {
		return new ArrayList<CreditRating>(context.getCompany()
				.getCreditRatings());
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
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

}
