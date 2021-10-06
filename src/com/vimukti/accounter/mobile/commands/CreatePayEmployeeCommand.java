package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.EmployeeGroup;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.PayStructureDestination;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EmployeeAndEmployeeGroupRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PayEmployeeTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayEmployee;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPayEmployee;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class CreatePayEmployeeCommand extends AbstractTransactionCommand {

	ClientPayEmployee payEmployee;

	private static final String PAY_EMPLOYEE_OR_GROUP = "employeeOrGroup";

	private static final String PAY_FROM = "payFrom";

	private static final String PAY_EMPLOYEE_TABLE = "payEmployeeTable";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages().selectATransactionToUpdate(getMessages().invoice()));
				return "payEmployeeList";
			}
			long numberFromString = getNumberFromString(string);
			payEmployee = (ClientPayEmployee) CommandUtils.getClientObjectById(numberFromString,
					AccounterCoreType.TYPE_PAYEMPLOYEE, getCompanyId());
			if (payEmployee == null) {
				return "payEmployeeList" + string;
			}
			setValues();
		} else {
			payEmployee = new ClientPayEmployee();
		}
		setTransaction(payEmployee);
		return null;
	}

	private void setValues() {
		long employee = payEmployee.getEmployee();
		if (employee == 0) {
			employee = payEmployee.getEmployeeGroup();
		}
		get(PAY_EMPLOYEE_OR_GROUP).setValue(payEmployee.getEmployee() != 0 ?
				getServerObject(Employee.class, employee) : getServerObject(EmployeeGroup.class, employee));

		get(DATE).setValue(payEmployee.getDate());
		get(NUMBER).setValue(payEmployee.getNumber());
		get(PAY_FROM).setValue(getServerObject(Account.class, payEmployee.getPayAccount()));
		get(PAY_EMPLOYEE_TABLE).setValue(payEmployee.getTransactionPayEmployee());
	}

	@Override
	protected String getWelcomeMessage() {
		return payEmployee.getID() == 0 ? getMessages().creating(getMessages().payEmployee())
				: getMessages().updating(getMessages().payEmployee());
	}

	@Override
	protected String getDetailsMessage() {
		return payEmployee.getID() == 0 ? getMessages().readyToCreate(getMessages().payEmployee())
				: getMessages().readyToUpdate(getMessages().payEmployee());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(ClientTransaction.TYPE_PAY_EMPLOYEE, context.getCompany()));
	}

	@Override
	public String getSuccessMessage() {
		return payEmployee.getID() == 0 ? getMessages().createSuccessfully(getMessages().payEmployee())
				: getMessages().updateSuccessfully(getMessages().payEmployee());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(getMessages().date()), getMessages().date(), false,
				true));

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(getMessages().number()),
				getMessages().number(), false, true));

		list.add(new EmployeeAndEmployeeGroupRequirement(PAY_EMPLOYEE_OR_GROUP,
				getMessages().pleaseSelect(getMessages().employeeOrGroup()), getMessages().employeeOrGroup(),
				new ChangeListner<PayStructureDestination>() {

					@Override
					public void onSelection(PayStructureDestination value) {
						CreatePayEmployeeCommand.this.employeeChanged();
					}
				}));

		list.add(new AccountRequirement(PAY_FROM, getMessages().pleaseSelect(getMessages().payFrom()),
				getMessages().payFrom(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return CreatePayEmployeeCommand.this.getAccounts();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().bankAccounts());
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add(new UserCommand("createBankAccount", getMessages().bank()));
			}
		});

		list.add(new PayEmployeeTableRequirement(PAY_EMPLOYEE_TABLE, getMessages().pleaseSelect(getMessages().item()),
				getMessages().item()) {

			@Override
			protected List<ClientTransactionPayEmployee> getList() {
				return CreatePayEmployeeCommand.this.transactionPayEmployeeList;
			}
		});
	}

	List<ClientTransactionPayEmployee> transactionPayEmployeeList = new ArrayList<ClientTransactionPayEmployee>();

	protected void employeeChanged() {
		get(PAY_EMPLOYEE_TABLE).setValue(new ArrayList<ClientTransactionPayEmployee>());
		initTransactionPayEmployees();
	}

	protected void initTransactionPayEmployees() {
		transactionPayEmployeeList = new ArrayList<ClientTransactionPayEmployee>();
		ClientPayStructureDestination structureDestination = get(PAY_EMPLOYEE_OR_GROUP).getValue();
		try {
			transactionPayEmployeeList = new FinanceTool().getPayrollManager()
					.getTransactionPayEmployeeList(structureDestination, getCompanyId());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	protected List<Account> getAccounts() {
		List<Account> filteredList = new ArrayList<Account>();
		for (Account obj : getCompany().getAccounts()) {
			if (new ListFilter<Account>() {

				@Override
				public boolean filter(Account e) {
					return Arrays.asList(Account.TYPE_BANK).contains(e.getType());
				}
			}.filter(obj)) {
				filteredList.add(obj);
			}
		}
		return filteredList;
	}

	@Override
	protected Currency getCurrency() {
		return getCompany().getPrimaryCurrency();
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientPayStructureDestination destination = get(PAY_EMPLOYEE_OR_GROUP).getValue();
		payEmployee.setEmployee(destination instanceof Employee ? destination.getID() : 0);
		payEmployee.setEmployeeGroup(destination instanceof EmployeeGroup ? destination.getID() : 0);
		ClientFinanceDate date = get(DATE).getValue();
		payEmployee.setTransactionDate(date.getDate());
		String number = get(NUMBER).getValue();
		payEmployee.setNumber(number);
		List<ClientTransactionPayEmployee> transactionPayEmployee = get(PAY_EMPLOYEE_TABLE).getValue();
		payEmployee.setTransactionPayEmployee(transactionPayEmployee);
		long payAccount = ((Account) get(PAY_FROM).getValue()).getID();
		payEmployee.setPayAccount(payAccount);
		payEmployee.setTotal(getTotal(transactionPayEmployee));
		create(payEmployee, context);
		return null;
	}

	private double getTotal(List<ClientTransactionPayEmployee> transactionPayEmployee) {
		double total = 0;
		for (ClientTransactionPayEmployee clientTransactionPayEmployee : transactionPayEmployee) {
			total += clientTransactionPayEmployee.getPayment();
		}
		return total;
	}

}
