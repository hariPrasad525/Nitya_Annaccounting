package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.core.Currency;
import com.nitya.accounter.core.Payee;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.requirements.CommandsRequirement;
import com.nitya.accounter.mobile.requirements.ShowListRequirement;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.Lists.PayeeList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;

public class VendorsListCommand extends AbstractCommand {
	private static final String VENDOR_TYPE = "vendorType";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(VENDOR_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<PayeeList>("vendorssList", Global
				.get().vendor(), 20) {

			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// PayeeList value) {
			// commandList.add(new UserCommand("update vendor ", value
			// .getPayeeName()));
			// commandList.add(new UserCommand("deleteVendor", value.getID()));
			// }

			@Override
			protected String onSelection(PayeeList value) {
				if (value.getType() == Payee.TYPE_CUSTOMER) {
					return "updateCustomer " + value.getPayeeName();
				} else if (value.getType() == Payee.TYPE_VENDOR) {
					return "updateVendor " + value.getPayeeName();
				} else if (value.getType() == Payee.TYPE_TAX_AGENCY) {
					return "updateTAXAgency " + value.getPayeeName();
				}
				return "";
			}

			@Override
			protected String getShowMessage() {
				return "Vendor List";
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(PayeeList value) {
				Record record = new Record(value);
				record.add(getMessages().payeeName(Global.get().Vendor()),
						value.getPayeeName());
				Currency currency = (Currency) HibernateUtil
						.getCurrentSession().get(Currency.class,
								value.getCurrecny());
				record.add(
						getMessages().balance(),
						getAmountWithCurrency(value.getBalance(),
								currency.getSymbol()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newVendor");
			}

			@Override
			protected boolean filter(PayeeList e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<PayeeList> getLists(Context context) {
				return getVendorList(context);
			}

		});
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
		get(VENDOR_TYPE).setDefaultValue(getMessages().activate());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().success();
	}

	private ArrayList<PayeeList> getVendorList(Context context) {
		FinanceTool financeTool = new FinanceTool();
		String isActive = get(VENDOR_TYPE).getValue();
		try {
			return financeTool.getPayeeList(ClientTransaction.CATEGORY_VENDOR,
					(isActive.equals(getMessages().active()) ? true : false),
					0, -1, context.getCompany().getID());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;

	}

}
