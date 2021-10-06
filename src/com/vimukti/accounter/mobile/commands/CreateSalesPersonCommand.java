package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PhoneRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ListFilter;

public class CreateSalesPersonCommand extends AbstractCommand {

	private static final String SALES_PERSON_NAME = "salesPersonName";
	private static final String FILE_AS = "fileAs";
	private static final String JOB_TITLE = "jobTitle";
	private static final String ADDRESS = "address";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String E_MAIL = "eMail";
	private static final String WEB_PAGE_ADDRESS = "webPageAddress";
	private static final String GENDER = "gender";
	private static final String DO_BIRTH = "dateOfBirth";
	private static final String DO_HIRE = "dateOfHire";
	private static final String DO_LASTREVIEW = "dateOfLastReview";
	private static final String DO_RELEASE = "dateOfRelease";
	private static final String MEMO = "memo";
	private static final String ACTIVE = "active";
	ClientSalesPerson salesPerson;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(SALES_PERSON_NAME, getMessages()
				.pleaseEnter(getMessages().salesPersonName()), getMessages()
				.salesPersonName(), false, true) {

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				get(FILE_AS).setValue(value);
			}
		});

		list.add(new NameRequirement(FILE_AS, getMessages().pleaseEnter(
				getMessages().fileAs() + " " + getMessages().name()),
				getMessages().fileAs(), true, true));

		list.add(new NameRequirement(JOB_TITLE, getMessages().pleaseEnter(
				getMessages().jobTitle()), getMessages().jobTitle(), true, true));

		list.add(new AddressRequirement(ADDRESS, getMessages().pleaseEnter(
				getMessages().address()), getMessages().address(), true, true));
		list.add(new PhoneRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));
		list.add(new NumberRequirement(FAX, getMessages().pleaseEnter(
				getMessages().fax()), getMessages().fax(), true, true));

		list.add(new AccountRequirement(EXPENSE_ACCOUNT,
				getMessages()
						.pleaseSelect(
								getMessages().expense() + " "
										+ getMessages().account()),
				getMessages().expense() + " " + getMessages().account(), true,
				false, new ChangeListner<Account>() {

					@Override
					public void onSelection(Account value) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			protected String getSetMessage() {
				return "";
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getIsActive()
									&& (e.getType() == Account.TYPE_EXPENSE || e
											.getType() == Account.TYPE_OTHER_ASSET)) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected boolean filter(Account e, String name) {
				return false;
			}
		});
		list.add(new EmailRequirement(E_MAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));
		list.add(new URLRequirement(WEB_PAGE_ADDRESS, getMessages()
				.pleaseEnter(getMessages().webPageAddress()), getMessages()
				.webPageAddress(), true, true));
		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().active();
			}

			@Override
			protected String getFalseString() {
				return getMessages().inActive();
			}
		});

		list.add(new StringListRequirement(GENDER, getMessages().pleaseEnter(
				getMessages().gender()), getMessages().gender(), true, true,
				null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().gender());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getGenders();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().gender());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new DateRequirement(DO_BIRTH, getMessages().pleaseEnter(
				getMessages().dateofBirth()), getMessages().dateofBirth(),
				false, true));
		list.add(new DateRequirement(DO_HIRE, getMessages().pleaseEnter(
				getMessages().dateofHire()), getMessages().dateofHire(), false,
				true));
		list.add(new DateRequirement(DO_LASTREVIEW, getMessages().pleaseEnter(
				getMessages().dateofLastReview()), getMessages()
				.dateofLastReview(), false, true));
		list.add(new DateRequirement(DO_RELEASE, getMessages().pleaseEnter(
				getMessages().dateofRelease()), getMessages().dateofRelease(),
				false, true));
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(SALES_PERSON_NAME).getValue();
		salesPerson.setFirstName(name);

		String fileAs = get(FILE_AS).getValue();
		salesPerson.setFileAs(fileAs);

		String jobTitle = get(JOB_TITLE).getValue();
		salesPerson.setJobTitle(jobTitle);

		ClientAddress address = get(ADDRESS).getValue();
		salesPerson.setAddress(address);

		String phone = get(PHONE).getValue();
		salesPerson.setPhoneNo(phone);

		boolean isactive = (Boolean) get(ACTIVE).getValue();
		salesPerson.setActive(isactive);

		String fax = get(FAX).getValue();
		salesPerson.setFaxNo(fax);

		Account value = get(EXPENSE_ACCOUNT).getValue();
		salesPerson.setExpenseAccount(value != null ? value.getID() : 0);

		String email = get(E_MAIL).getValue();
		salesPerson.setEmail(email);

		String webPage = get(WEB_PAGE_ADDRESS).getValue();
		salesPerson.setWebPageAddress(webPage);

		String gender = get(GENDER).getValue();
		salesPerson.setGender(gender);

		ClientFinanceDate do_birth = get(DO_BIRTH).getValue();
		salesPerson.setDateOfBirth(do_birth);

		ClientFinanceDate do_hire = get(DO_HIRE).getValue();
		salesPerson.setDateOfHire(do_hire);

		// if (do_birth.getDate() != 0) {
		// long mustdate = new ClientFinanceDate().getDate() - 180000;
		// if ((new ClientFinanceDate(mustdate).before(do_birth))) {
		// Result result = new Result();
		// result.add(getMessages().dateofBirthshouldshowmorethan18years()
		// + ". Because Sales Person should have 18 years");
		// return result;
		// }
		// }

		ClientFinanceDate do_lastreview = get(DO_LASTREVIEW).getValue();
		salesPerson.setDateOfLastReview(do_lastreview);

		ClientFinanceDate do_release = get(DO_RELEASE).getValue();
		salesPerson.setDateOfRelease(do_release);

		String memo = get(MEMO).getValue();
		salesPerson.setMemo(memo);
		create(salesPerson, context);
		return null;

	}

	private List<String> getGenders() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(getMessages().unspecified());
		list.add(getMessages().male());
		list.add(getMessages().female());
		return list;
	}

	@Override
	protected String getDetailsMessage() {
		if (salesPerson.getID() == 0) {
			return getMessages().readyToCreate(getMessages().salesPerson());
		} else {
			return getMessages().readyToUpdate(getMessages().salesPerson());
		}
	}

	@Override
	public String getSuccessMessage() {
		if (salesPerson.getID() == 0) {
			return getMessages()
					.createSuccessfully(getMessages().salesPerson());
		} else {
			return getMessages()
					.updateSuccessfully(getMessages().salesPerson());
		}
	}

	@Override
	protected String getWelcomeMessage() {
		if (salesPerson.getID() == 0) {
			return getMessages().creating(getMessages().salesPerson());
		} else {
			return "Updateing" + getMessages().salesPerson() + " "
					+ salesPerson.getDisplayName();
		}
	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = salesPerson.getID();
		return id != 0 ? "deleteSalesPerson " + id : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().salesPerson()));
				return "salesPersonList";
			}
			ClientSalesPerson salesPersonByName = CommandUtils
					.getSalesPersonByName(context.getCompany(), string);
			if (salesPersonByName == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().salesPerson()));
				return "salesPersonList " + string.trim();
			}
			salesPerson = salesPersonByName;
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(SALES_PERSON_NAME).setValue(string);
			}
			salesPerson = new ClientSalesPerson();
		}
		return null;
	}

	private void setValues() {
		get(SALES_PERSON_NAME).setValue(salesPerson.getName());
		get(FILE_AS).setValue(salesPerson.getFileAs());
		get(JOB_TITLE).setValue(salesPerson.getJobTitle());
		get(ADDRESS).setValue(salesPerson.getAddress());
		get(PHONE).setValue(salesPerson.getPhoneNo());
		get(ACTIVE).setValue(salesPerson.isActive());
		get(FAX).setValue(salesPerson.getFaxNo());
		get(EXPENSE_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(
						salesPerson.getExpenseAccount(),
						AccounterCoreType.ACCOUNT));
		get(E_MAIL).setValue(salesPerson.getEmail());
		get(WEB_PAGE_ADDRESS).setValue(salesPerson.getWebPageAddress());
		get(GENDER).setValue(salesPerson.getGender());
		get(DO_BIRTH).setValue(
				new ClientFinanceDate(salesPerson.getDateOfBirth()));
		get(DO_HIRE).setValue(
				new ClientFinanceDate(salesPerson.getDateOfHire()));
		get(DO_LASTREVIEW).setValue(
				new ClientFinanceDate(salesPerson.getDateOfLastReview()));
		get(DO_RELEASE).setValue(
				new ClientFinanceDate(salesPerson.getDateOfRelease()));
		get(MEMO).setValue(salesPerson.getMemo());
	}

	@Override
	protected void setDefaultValues(Context context) {
		String name = get(SALES_PERSON_NAME).getValue();
		if (get(FILE_AS).getValue() == null)
			get(FILE_AS).setDefaultValue(name);
		get(JOB_TITLE).setDefaultValue("");
		get(ADDRESS).setDefaultValue(new ClientAddress());
		get(PHONE).setDefaultValue("");
		get(FAX).setDefaultValue("");
		get(EXPENSE_ACCOUNT).setDefaultValue(null);
		get(E_MAIL).setDefaultValue("");
		get(WEB_PAGE_ADDRESS).setDefaultValue("");
		get(GENDER).setDefaultValue(getMessages().unspecified());
		get(MEMO).setDefaultValue("");
		get(ACTIVE).setDefaultValue(Boolean.TRUE);

	}

}
