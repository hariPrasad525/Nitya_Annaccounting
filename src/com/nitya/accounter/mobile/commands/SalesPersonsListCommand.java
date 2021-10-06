package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nitya.accounter.core.SalesPerson;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.CommandsRequirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;

public class SalesPersonsListCommand extends AbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
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
		get(VIEW_BY).setDefaultValue(getMessages().active());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(VIEW_BY) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<SalesPerson>(getMessages()
				.salesPersonList(), "", 20) {

			@Override
			protected String onSelection(SalesPerson value) {
				return "updateSalesPerson " + value.getFirstName();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().salesPersonList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(SalesPerson salesPrson) {
				Record record = new Record(salesPrson);
				record.add(getMessages().firstName(), salesPrson.getFirstName());
				record.add(getMessages().jobTitle(), salesPrson.getJobTitle());
				record.add(getMessages().dateofBirth(),
						getDateByCompanyType(salesPrson.getDateOfBirth()
								.toClientFinanceDate()));
				record.add(getMessages().email(), salesPrson.getEmail());
				record.add(getMessages().phone(), salesPrson.getPhoneNo());
				record.add(getMessages().fax(), salesPrson.getFaxNo());
				record.add(getMessages().memo(), salesPrson.getMemo());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createSalesPerson");
			}

			@Override
			protected boolean filter(SalesPerson e, String name) {
				return e.getFirstName().startsWith(name);
			}

			@Override
			protected List<SalesPerson> getLists(Context context) {

				return getSalesPersons(context);
			}
		});

	}

	protected List<SalesPerson> getSalesPersons(Context context) {
		String isActive = get(VIEW_BY).getValue();
		List<SalesPerson> list = new ArrayList<SalesPerson>();
		Set<SalesPerson> salesPersons = context.getCompany().getSalesPersons();
		if (salesPersons != null) {
			for (SalesPerson person : salesPersons) {
				if (isActive.equals(getMessages().active())) {
					if (person.isActive()) {
						list.add(person);
					}
				} else {
					if (!person.isActive()) {
						list.add(person);
					}

				}
			}
		}
		return list;
	}

}
