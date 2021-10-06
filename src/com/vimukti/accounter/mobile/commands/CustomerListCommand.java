package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.Global;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CustomerListCommand extends AbstractCommand {

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

		list.add(new ShowListRequirement<Customer>("Customers", "", 20) {

			@Override
			protected String onSelection(Customer value) {
				return "updateCustomer " + value.getName();
			}

			@Override
			protected String getShowMessage() {

				return getMessages().payeeList(Global.get().Customer());
			}

			@Override
			protected String getEmptyString() {

				return getMessages().youDontHaveAny(Global.get().Customer());

			}

			@Override
			protected Record createRecord(Customer value) {
				Record customerRec = new Record(value);
				customerRec.add(getMessages().name(), value.getName());
				customerRec.add(getMessages().balance(), value.getCurrency()
						.getSymbol() + " " + value.getBalance());
				return customerRec;

			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createCustomer");

			}

			@Override
			protected boolean filter(Customer e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Customer> getLists(Context context) {

				return getCustomers(context);
			}

		});

	}

	private List<Customer> getCustomers(Context context) {
		String isActive = get(VIEW_BY).getValue();
		Set<Customer> customers = getCompany().getCustomers();
		List<Customer> result = new ArrayList<Customer>();
		for (Customer customer : customers) {
			if (isActive.equals(getMessages().active())) {
				if (customer.isActive()) {
					result.add(customer);
				}

			} else {
				if (!customer.isActive()) {
					result.add(customer);
				}

			}
		}

		return result;
	}
}