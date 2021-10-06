package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class CreateEmployeeCommand extends AbstractCommand {

	private static final String EMPLOYEE_NAME = "employeeName";
	private static final String DATE_OF_BIRTH = "dateOfBirth";
	private static final String GENDER = "gender";
	private static final String ACTIVE = "active";
	private static final String CONTACT = "contact";
	private static final String EMAIL = "email";
	private static final String ADDRESS = "address";
	private static final String BANK_NAME = "bankName";
	private static final String BANK_ACCOUNT_NUM = "bankAccountNum";
	private static final String BANK_BRANCH = "bankBranch";
	private static final String EMPLOYEE_ID = "employeeId";
	private static final String DATE_OF_HIRE = "dateOfHire";
	private static final String EMP_GROUP = "empGroup";// TODO
	private static final String DESIGNATION = "designation";
	private static final String WORKING_LOCATION = "workingLocation";
	private static final String INACTIVE_REASON = "inactivereason";
	private static final String LAST_WORKING_DAY = "lastWorkingDay";

	private ClientEmployee employee;

	private ArrayList<String> listOfReaons;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		initReasons();
		list.add(new NameRequirement(EMPLOYEE_NAME, getMessages().pleaseEnter(
				getMessages().employeeName()), getMessages().employeeName(),
				false, true) {
			@Override
			public void setValue(Object value) {
				if (CreateEmployeeCommand.this.isEmpExists((String) value)) {
					addFirstMessage(getMessages().alreadyExist());
					return;
				}
				addFirstMessage(getMessages().pleaseEnter(
						getMessages().employeeName()));
				super.setValue(value);
			}
		});

		list.add(new DateRequirement(DATE_OF_BIRTH, getMessages().pleaseEnter(
				getMessages().dateofBirth()), getMessages().pleaseEnter(
				getMessages().dateofBirth()), true, true));

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

		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return "This Employee is Active";
			}

			@Override
			protected String getFalseString() {
				return "This Employee is In-Active";
			}
		});

		list.add(new DateRequirement(LAST_WORKING_DAY, getMessages()
				.pleaseSelect(getMessages2().lastDate()), getMessages2()
				.lastDate(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Boolean isActive = CreateEmployeeCommand.this.get(ACTIVE)
						.getValue();
				if (!isActive) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new StringListRequirement(INACTIVE_REASON, getMessages()
				.pleaseSelect(getMessages2().reasonForInactive()),
				getMessages2().reasonForInactive(), false, true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				Boolean isActive = CreateEmployeeCommand.this.get(ACTIVE)
						.getValue();
				if (!isActive) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages2().reasonForInactive());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages2().reasonForInactive());
			}

			@Override
			protected List<String> getLists(Context context) {
				return CreateEmployeeCommand.this.getInActiveReasons();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringRequirement(CONTACT, getMessages().pleaseSelect(
				getMessages().contact()), CONTACT, true, true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new AddressRequirement(ADDRESS, getMessages().pleaseEnter(
				getMessages().address()), getMessages().address(), true, true));
		list.add(new StringRequirement(BANK_NAME, getMessages().pleaseEnter(
				getMessages().bankName()), getMessages().bankName(), true, true));

		list.add(new StringRequirement(BANK_BRANCH, getMessages().pleaseEnter(
				getMessages().bankBranch()), getMessages().bankBranch(), true,
				true));

		list.add(new StringRequirement(BANK_ACCOUNT_NUM, getMessages()
				.pleaseEnter(getMessages().bankAccountNumber()), getMessages()
				.bankAccountNumber(), true, true));
		list.add(new StringRequirement(EMPLOYEE_ID, getMessages().pleaseEnter(
				getMessages().employeeID()), getMessages().employeeID(), true,
				true));

		list.add(new DateRequirement(DATE_OF_HIRE, getMessages().pleaseEnter(
				getMessages().dateofHire()), getMessages().pleaseEnter(
				getMessages().dateofHire()), true, true));

		list.add(new StringRequirement(DESIGNATION, getMessages().pleaseEnter(
				getMessages().designation()), getMessages().designation(),
				true, true));

		list.add(new StringRequirement(WORKING_LOCATION, getMessages()
				.pleaseEnter(getMessages().workingLocation()), getMessages()
				.workingLocation(), true, true));
	}

	private void initReasons() {
		String[] reasons = { getMessages2().gotNewJobOffer(),
				getMessages2().quitWithOutAjob(),
				getMessages2().lackofPerformance(),
				getMessages2().disputesbetweenCoworkers(),
				getMessages2().nosatisfactionwithJob(),
				getMessages2().notenoughHours(),
				getMessages2().jobwasTemporary(),
				getMessages2().contractended(),
				getMessages2().workwasSeasonal(),
				getMessages2().betteropportunity(),
				getMessages2().seekinggrowth(), getMessages2().careerchange(),
				getMessages2().returnedtoSchool(), getMessages2().relocated(),
				getMessages2().raisedaFamily(), getMessages().other() };
		listOfReaons = new ArrayList<String>();
		for (int i = 0; i < reasons.length; i++) {
			listOfReaons.add(reasons[i]);
		}
	}

	protected List<String> getInActiveReasons() {
		return listOfReaons;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "employeeList";
			}
			long numberFromString = getNumberFromString(string);
			employee = (ClientEmployee) CommandUtils.getClientObjectById(
					numberFromString, AccounterCoreType.EMPLOYEE,
					getCompanyId());
			if (employee == null) {
				return "employeeList" + string;
			}
			setValues();
		} else {
			employee = new ClientEmployee();
		}
		return null;
	}

	private Map<Integer, ClientAddress> allAddresses = new HashMap<Integer, ClientAddress>();

	private void setAddresses(Set<ClientAddress> addresses) {
		if (addresses != null) {
			Iterator<ClientAddress> it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress addr = (ClientAddress) it.next();
				if (addr != null) {
					allAddresses.put(addr.getType(), addr);
				}
			}
		}
	}

	private void setValues() {

		get(EMPLOYEE_NAME).setValue(employee.getName());
		get(DATE_OF_BIRTH).setValue(
				new ClientFinanceDate(employee.getDateOfBirth()));
		if (employee.getGender() != -1) {
			get(GENDER).setValue(getGenders().get(employee.getGender()));
		}
		get(CONTACT).setValue(employee.getPhoneNo());
		get(EMAIL).setValue(employee.getEmail());
		get(ACTIVE).setValue(employee.isActive());

		setAddresses(employee.getAddress());

		ClientAddress toBeShown = allAddresses.get(ClientAddress.TYPE_BILL_TO);
		if (toBeShown != null) {
			get(ADDRESS).setValue(toBeShown);
		}

		get(EMPLOYEE_ID).setValue(employee.getNumber());
		get(DATE_OF_HIRE).setValue(
				new ClientFinanceDate(employee.getPayeeSince()));
		// employeeGroupCombo.setGroupValue(employee.getGroup());
		get(DESIGNATION).setValue(employee.getDesignation());
		get(WORKING_LOCATION).setValue(employee.getLocation());
		get(BANK_ACCOUNT_NUM).setValue(employee.getBankAccountNo());
		get(BANK_BRANCH).setValue(employee.getBankBranch());
		get(BANK_NAME).setValue(employee.getBankName());
		if (employee.getReasonType() != -1) {
			get(INACTIVE_REASON).setValue(
					listOfReaons.get(employee.getReasonType()));
		}

		get(LAST_WORKING_DAY).setValue(
				new ClientFinanceDate(employee.getLastDate()));

	}

	@Override
	protected String getWelcomeMessage() {
		return employee.getID() == 0 ? getMessages().creating(
				getMessages().employee()) : getMessages().updating(
				getMessages().employee());
	}

	@Override
	protected String getDetailsMessage() {
		return employee.getID() == 0 ? getMessages().readyToCreate(
				getMessages().employee()) : getMessages().readyToUpdate(
				getMessages().employee());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE_OF_HIRE).setDefaultValue(new ClientFinanceDate());
		get(LAST_WORKING_DAY).setDefaultValue(new ClientFinanceDate());
		get(DATE_OF_BIRTH).setDefaultValue(new ClientFinanceDate());
		get(ACTIVE).setValue(true);
	}

	@Override
	public String getSuccessMessage() {
		return employee.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().employee()) : getMessages().updateSuccessfully(
				getMessages().employee());
	}

	protected boolean isEmpExists(String value) {
		if (this.employee.getID() != 0
				&& this.employee.getName().equalsIgnoreCase(value)) {
			return false;
		}
		Set<Payee> payees = getCompany().getPayees();
		for (Payee payee : payees) {
			if (payee.getType() == Payee.TYPE_EMPLOYEE) {
				if (payee.getName().equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	private List<String> getGenders() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(getMessages().unspecified());
		list.add(getMessages().male());
		list.add(getMessages().female());
		return list;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		employee.setName((String) get(EMPLOYEE_NAME).getValue());

		employee.setAddress(getAddresss());
		employee.setBankAccountNo((String) get(BANK_ACCOUNT_NUM).getValue());
		employee.setBankName((String) get(BANK_NAME).getValue());
		employee.setBankBranch((String) get(BANK_BRANCH).getValue());
		Boolean isActive = get(ACTIVE).getValue();
		employee.setActive(isActive);
		employee.setPhoneNo((String) get(CONTACT).getValue());
		// employee.setPanNumber(panItem.getValue());
		ClientFinanceDate dateOfBirth = get(DATE_OF_BIRTH).getValue();
		employee.setDateOfBirth(dateOfBirth.getDate());
		ClientFinanceDate dateOfHire = get(DATE_OF_HIRE).getValue();
		employee.setPayeeSince(dateOfHire.getDate());
		employee.setDesignation((String) get(DESIGNATION).getValue());
		employee.setEmail((String) get(EMAIL).getValue());
		employee.setContacts(new HashSet<ClientContact>());
		// if (employeeGroupCombo.getSelectedValue() != null) {
		// employee.setGroup(employeeGroupCombo.getSelectedValue().getID());
		// }
		employee.setLocation((String) get(WORKING_LOCATION).getValue());
		employee.setNumber((String) get(EMPLOYEE_ID).getValue());
		ClientFinanceDate lastWorkingDate = get(LAST_WORKING_DAY).getValue();
		employee.setLastDate(lastWorkingDate.getDate());
		employee.setReasonType(getReasonType());
		int genderType = -1;
		String selectedValue = get(GENDER).getValue();
		if (selectedValue != null) {
			if (selectedValue.equals(getMessages().unspecified())) {
				genderType = 0;
			} else if (selectedValue.equals(getMessages().male())) {
				genderType = 1;
			} else if (selectedValue.equals(getMessages().female())) {
				genderType = 2;
			}
		}
		employee.setGender(genderType);
		create(employee, context);
		return null;
	}

	private int getReasonType() {

		int reasonType = -1;
		String selectedValue = get(INACTIVE_REASON).getValue();
		if (selectedValue != null) {
			if (selectedValue.equals(getMessages2().gotNewJobOffer())) {
				reasonType = 0;
			} else if (selectedValue.equals(getMessages2().quitWithOutAjob())) {
				reasonType = 1;
			} else if (selectedValue.equals(getMessages2().lackofPerformance())) {
				reasonType = 2;
			} else if (selectedValue.equals(getMessages2()
					.disputesbetweenCoworkers())) {
				reasonType = 3;
			} else if (selectedValue.equals(getMessages2()
					.nosatisfactionwithJob())) {
				reasonType = 4;
			} else if (selectedValue.equals(getMessages2().notenoughHours())) {
				reasonType = 5;
			} else if (selectedValue.equals(getMessages2().jobwasTemporary())) {
				reasonType = 6;
			} else if (selectedValue.equals(getMessages2().contractended())) {
				reasonType = 7;
			} else if (selectedValue.equals(getMessages2().workwasSeasonal())) {
				reasonType = 8;
			} else if (selectedValue.equals(getMessages2().betteropportunity())) {
				reasonType = 9;
			} else if (selectedValue.equals(getMessages2().seekinggrowth())) {
				reasonType = 10;
			} else if (selectedValue.equals(getMessages2().careerchange())) {
				reasonType = 11;
			} else if (selectedValue.equals(getMessages2().returnedtoSchool())) {
				reasonType = 12;
			} else if (selectedValue.equals(getMessages2().relocated())) {
				reasonType = 13;
			} else if (selectedValue.equals(getMessages2().raisedaFamily())) {
				reasonType = 14;
			} else if (selectedValue.equals(getMessages().other())) {
				reasonType = 15;
			}

		}
		return reasonType;

	}

	private Set<ClientAddress> getAddresss() {
		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType("company"));
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses
					.put(UIUtils.getAddressType("company"), selectedAddress);
		}
		Collection<ClientAddress> add = allAddresses.values();
		Iterator<ClientAddress> it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			Set<ClientAddress> hashSet = new HashSet<ClientAddress>();
			hashSet.add(a);
			return hashSet;
		}
		return null;
	}
}
