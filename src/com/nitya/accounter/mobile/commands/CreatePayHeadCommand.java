package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.Account;
import com.nitya.accounter.core.AttendanceOrProductionType;
import com.nitya.accounter.core.PayHead;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.requirements.AccountRequirement;
import com.nitya.accounter.mobile.requirements.AttendanceOrProductionTypeRequirement;
import com.nitya.accounter.mobile.requirements.BooleanRequirement;
import com.nitya.accounter.mobile.requirements.NameRequirement;
import com.nitya.accounter.mobile.requirements.PayHeadRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.mobile.utils.CommandUtils;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAttendancePayHead;
import com.nitya.accounter.web.client.core.ClientComputionPayHead;
import com.nitya.accounter.web.client.core.ClientFlatRatePayHead;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.ListFilter;

public class CreatePayHeadCommand extends AbstractCommand {

	private static final String PAY_HEAD_NAME = "payHeadName";
	private static final String PAY_HEAD_TYPE = "payHeadType";
	private static final String PAY_HEAD_SLIPNAME = "payHeadSlipName";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String AFFECT_NET_SALARY = "affectNetSalary";
	private static final String CALCULATION_TYPE = "calculationType";
	private static final String EARNING_DEDUCTION_ON = "earningDeduction";
	private static final String PAY_HEADS = "payheads";
	private static final String PER_DAY_CALCULATION_BASIS = "perdayCalculationBasis";
	private static final String CALCULATION_PERIOD = "calculationPeriod";
	private static final String PRODUCTION_TYPE = "productionType";
	private static final String COMPUTE_ON = "computeOn";

	private ClientPayHead payHead;
	private List<String> payHeadTypesList = new ArrayList<String>();
	private List<String> calculationTypesList = new ArrayList<String>();
	private List<String> attendanceTypesList = new ArrayList<String>();
	private List<String> perDayCalTypesList = new ArrayList<String>();
	private List<String> calPeriodTypesList = new ArrayList<String>();
	private List<String> computationTypeList = new ArrayList<String>();

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(PAY_HEAD_NAME, getMessages().pleaseEnter(
				getMessages().name()), getMessages().name(), false, true) {
			@Override
			public void setValue(Object value) {
				if (CreatePayHeadCommand.this.isPayHeadExists((String) value)) {
					addFirstMessage(getMessages().alreadyExist());
					return;
				}
				addFirstMessage(getMessages().pleaseEnter(
						getMessages().payhead()));
				super.setValue(value);
			}
		});

		list.add(new NameRequirement(PAY_HEAD_SLIPNAME, getMessages()
				.pleaseEnter(getMessages().paySlipName()), getMessages()
				.paySlipName(), true, true));

		list.add(new StringListRequirement(PAY_HEAD_TYPE, getMessages()
				.pleaseEnter(getMessages().payHeadType()), getMessages()
				.payHeadType(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payHeadType());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().payHeadType());
			}

			@Override
			protected List<String> getLists(Context context) {
				return payHeadTypesList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(AFFECT_NET_SALARY, true) {

			@Override
			protected String getTrueString() {
				return getMessages().affectNetSalary() + " "
						+ getMessages().YES();
			}

			@Override
			protected String getFalseString() {
				return getMessages().affectNetSalary() + " "
						+ getMessages().NO();
			}
		});
		list.add(new AccountRequirement(EXPENSE_ACCOUNT, getMessages()
				.pleaseEnterNameOrNumber(getMessages().expenseAccount()),
				getMessages().expenseAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account acc) {
							return acc.getIsActive()
									&& acc.getType() != Account.TYPE_EXPENSE;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(CALCULATION_TYPE, getMessages()
				.pleaseEnter(getMessages().calculationType()), getMessages()
				.calculationType(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().calculationType());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().calculationType());
			}

			@Override
			protected List<String> getLists(Context context) {
				return calculationTypesList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(EARNING_DEDUCTION_ON, getMessages()
				.pleaseEnter(getMessages().deductionOn()), getMessages()
				.deductionOn(), false, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(CALCULATION_TYPE).getValue();
				if (value.equals(getMessages().production())
						|| value.equals(getMessages().attendance())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}

			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().deductionOn());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().deductionOn());
			}

			@Override
			protected List<String> getLists(Context context) {
				return attendanceTypesList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new PayHeadRequirement(PAY_HEADS) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(EARNING_DEDUCTION_ON).getValue();
				if (value != null && value.equals(getMessages().otherPayhead())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}

			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().deductionOn());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().deductionOn());
			}

			@Override
			protected List<PayHead> getLists(Context context) {
				return getpayHeadslist();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(PER_DAY_CALCULATION_BASIS,
				getMessages().pleaseEnter(
						getMessages().perDayCalculationBasis()), getMessages()
						.perDayCalculationBasis(), false, false, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(CALCULATION_TYPE).getValue();
				if (value.equals(getMessages().attendance())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().perDayCalculationBasis());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().perDayCalculationBasis());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPerDayCalculationTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(CALCULATION_PERIOD, getMessages()
				.pleaseEnter(getMessages().calculationPeriod()), getMessages()
				.calculationPeriod(), false, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(CALCULATION_TYPE).getValue();
				if (value.equals(getMessages().asComputedValue())
						|| value.equals(getMessages().flatRate())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().calculationPeriod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().calculationPeriod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCalculationPeriodTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new AttendanceOrProductionTypeRequirement(PRODUCTION_TYPE,
				getMessages().pleaseSelect(getMessages().productionType()),
				getMessages().productionType()) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(CALCULATION_TYPE).getValue();
				if (value.equals(getMessages().production())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}
			

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().productionType());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().productionType());
			}
		});

		list.add(new StringListRequirement(COMPUTE_ON, getMessages()
				.pleaseEnter(getMessages().computedOn()), getMessages()
				.computedOn(), false, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(CALCULATION_TYPE).getValue();
				if (value.equals(getMessages().asComputedValue())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().computedOn());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().computedOn());
			}

			@Override
			protected List<String> getLists(Context context) {
				return computationTypeList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

	}

	private void initComputationType() {
		String[] computationTypes = { getMessages().onDeductionTotal(),
				getMessages().onEarningTotal(), getMessages().onSubTotal() };
		for (int i = 0; i < computationTypes.length; i++) {
			computationTypeList.add(computationTypes[i]);
		}
	}

	protected List<String> getCalculationPeriodTypes() {
		String[] calPeriod = { getMessages().days(), getMessages().weeks(),
				getMessages().months() };
		calPeriodTypesList = new ArrayList<String>();
		for (String string : calPeriod) {
			calPeriodTypesList.add(string);
		}
		return calPeriodTypesList;
	}

	protected List<String> getPerDayCalculationTypes() {
		String[] perDayCalculations = { getMessages().asPerCalendarPeriod(),
				getMessages().days30(), getMessages().userDefinedCalendar() };
		perDayCalTypesList = new ArrayList<String>();
		for (String string : perDayCalculations) {
			perDayCalTypesList.add(string);
		}
		return perDayCalTypesList;
	}

	protected List<PayHead> getpayHeadslist() {
		List<PayHead> payheadsList = new ArrayList<PayHead>();
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("list.Payhead").setEntity(
				"company", getCompany());
		payheadsList = query.list();
		return payheadsList;
	}

	private void initEarningDeductionList() {
		String[] attendanceTypes = { getMessages().otherPayhead(),
				getMessages().onEarningTotal(), getMessages().onSubTotal(),
				getMessages().rate() };
		attendanceTypesList = new ArrayList<String>();
		for (String string : attendanceTypes) {
			attendanceTypesList.add(string);
		}
	}

	private void initCalculationTypes() {
		String[] calType = { getMessages().attendance(),
				getMessages().flatRate(), getMessages().production() };
		calculationTypesList = new ArrayList<String>();
		for (String string : calType) {
			calculationTypesList.add(string);
		}
	}

	private void initPayHeads() {
		String[] types = { getMessages().earningsForEmployees(),
				getMessages().deductionsForEmployees(),
				getMessages().employeesStatutoryDeductions(),
				getMessages().employeesStatutoryContributions(),
				getMessages().employeesOtherCharges(), getMessages().bonus(),
				getMessages().employeesOtherCharges(), getMessages2().perMile(),
                getMessages().loansAndAdvances(),
				getMessages().reimbursmentsToEmployees() };
		payHeadTypesList = new ArrayList<String>();
		for (String string : types) {
			payHeadTypesList.add(string);
		}
	}

	protected boolean isPayHeadExists(String value) {
		List<PayHead> getpayHeadslist = getpayHeadslist();
		for (PayHead payHead : getpayHeadslist) {
			if (this.payHead.getID() != payHead.getID()
					&& payHead.getName().equals(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().invoice()));
				return "payheadList";
			}
			long numberFromString = getNumberFromString(string);
			payHead = (ClientPayHead) CommandUtils.getClientObjectById(
					numberFromString, AccounterCoreType.PAY_HEAD,
					getCompanyId());
			if (payHead == null) {
				return "payheadList" + string;
			}
			setValues();
		} else {
			payHead = new ClientPayHead();
		}
		return null;
	}

	private void setValues() {
		get(PAY_HEAD_NAME).setValue(payHead.getName());
		get(PAY_HEAD_TYPE).setValue(
				ClientPayHead.getPayHeadType(payHead.getType()));
		get(PAY_HEAD_SLIPNAME).setValue(payHead.getNameToAppearInPaySlip());
		get(EXPENSE_ACCOUNT).setValue(
				getServerObject(Account.class, payHead.getExpenseAccount()));
		get(AFFECT_NET_SALARY).setValue(payHead.isAffectNetSalary());
		get(CALCULATION_TYPE).setValue(
				ClientPayHead.getCalculationType(payHead.getCalculationType()));

		String calType = ClientPayHead.getCalculationType(payHead
				.getCalculationType());
		if (calType.equals(getMessages().attendance())
				|| calType.equals(getMessages().production())) {
			ClientAttendancePayHead attendance = (ClientAttendancePayHead) payHead;
			get(PRODUCTION_TYPE).setValue(
					getServerObject(AttendanceOrProductionType.class,
							attendance.getProductionType()));
			String attendanceType = ClientAttendancePayHead
					.getAttendanceType(attendance.getAttendanceType());
			get(EARNING_DEDUCTION_ON).setValue(attendanceType);

			if (attendance.getAttendanceType() == ClientAttendancePayHead.PAY_HEAD) {
				get(PAY_HEADS)
						.setValue(
								getServerObject(PayHead.class,
										attendance.getPayhead()));
			}

			get(CALCULATION_PERIOD).setValue(
					getCalculationPeriod(attendance.getCalculationPeriod()));
			get(PER_DAY_CALCULATION_BASIS).setValue(
					getPerdayCalculationBasis(attendance
							.getPerDayCalculationBasis()));

		} else if (calType.equals(getMessages().asComputedValue())) {
			ClientComputionPayHead computation = (ClientComputionPayHead) payHead;

			get(CALCULATION_PERIOD).setValue(
					getCalculationPeriod(computation.getCalculationPeriod()));
			String compType = computationTypeList.get(computation
					.getComputationType() - 1);
			get(COMPUTE_ON).setValue(compType);
			if (compType.equals(getMessages().onSpecifiedFormula())) {
				// TODO
			}

		} else if (calType.equals(getMessages().flatRate())) {
			ClientFlatRatePayHead flatrate = (ClientFlatRatePayHead) payHead;

			get(CALCULATION_PERIOD).setValue(
					getCalculationPeriod(flatrate.getCalculationPeriod()));
		}
	}

	private String getPerdayCalculationBasis(int perDayCalculationBasis) {
		switch (perDayCalculationBasis) {
		case ClientAttendancePayHead.PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD:
			return getMessages().asPerCalendarPeriod();

		case ClientAttendancePayHead.PER_DAY_CALCULATION_30_DAYS:
			return getMessages().days30();

		case ClientAttendancePayHead.PER_DAY_CALCULATION_USER_DEFINED_CALANDAR:
			return getMessages().userDefindCalendar();

		default:
			return null;
		}
	}

	private String getCalculationPeriod(int calculationPeriod) {
		switch (calculationPeriod) {
		case ClientPayHead.CALCULATION_PERIOD_DAYS:
			return getMessages().days();

		case ClientPayHead.CALCULATION_PERIOD_MONTHS:
			return getMessages().months();

		case ClientPayHead.CALCULATION_PERIOD_WEEKS:
			return getMessages().weeks();

		default:
			return null;
		}
	}

	@Override
	protected String getWelcomeMessage() {
		return payHead.getID() == 0 ? getMessages().creating(
				getMessages().payhead()) : getMessages().updating(
				getMessages().payhead());
	}

	@Override
	protected String getDetailsMessage() {
		return payHead.getID() == 0 ? getMessages().readyToCreate(
				getMessages().payhead()) : getMessages().readyToUpdate(
				getMessages().payhead());
	}

	@Override
	protected void setDefaultValues(Context context) {
		initEarningDeductionList();
		initPayHeads();
		initCalculationTypes();
		initComputationType();
		get(AFFECT_NET_SALARY).setDefaultValue(true);
	}

	@Override
	public String getSuccessMessage() {
		return payHead.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().payhead()) : getMessages().updateSuccessfully(
				getMessages().payhead());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String selectedValue = get(CALCULATION_TYPE).getValue();
		if (selectedValue == null) {
			return new Result(getMessages().pleaseSelect(
					getMessages().calculationType()));
		}
		if (selectedValue.equals(getMessages().attendance())
				|| selectedValue.equals(getMessages().production())) {
			ClientAttendancePayHead payhead = new ClientAttendancePayHead();
			payhead.setAttendanceType(attendanceTypesList.indexOf(get(
					EARNING_DEDUCTION_ON).getValue()) + 1);
			String attendanceType = ClientAttendancePayHead
					.getAttendanceType(payhead.getAttendanceType());

			if (attendanceType.equals(getMessages().otherPayhead())) {
				payhead.setPayhead(((PayHead) get(PAY_HEADS).getValue())
						.getID());
			}
			payhead.setID(payhead.getID());
			if (selectedValue.equals(getMessages().attendance())) {
				String period = get(CALCULATION_PERIOD).getValue();
				payhead.setCalculationPeriod(calPeriodTypesList.indexOf(period) + 1);
				String perDayBasis = get(PER_DAY_CALCULATION_BASIS).getValue();
				payhead.setPerDayCalculationBasis(perDayCalTypesList
						.indexOf(perDayBasis) + 1);
			}

			if (selectedValue.equals(getMessages().production())) {
				AttendanceOrProductionType productionType = get(PRODUCTION_TYPE)
						.getValue();
				payhead.setProductionType(productionType.getID());
				payhead.setPerDayCalculationBasis(ClientAttendancePayHead.PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD);
			}
			this.payHead = payhead;

		} else if (selectedValue.equals(getMessages().asComputedValue())) {
			ClientComputionPayHead payhead = new ClientComputionPayHead();
			payhead.setID(this.payHead.getID());
			String period = get(CALCULATION_PERIOD).getValue();
			payhead.setCalculationPeriod(calPeriodTypesList.indexOf(period) + 1);
			String computeOn = get(COMPUTE_ON).getValue();
			payhead.setComputationType(computationTypeList.indexOf(computeOn) + 1);
			this.payHead = payhead;
		} else if (selectedValue.equals(getMessages().flatRate())) {
			ClientFlatRatePayHead payHead = new ClientFlatRatePayHead();
			payHead.setID(this.payHead.getID());
			String period = get(CALCULATION_PERIOD).getValue();
			payHead.setCalculationPeriod(calPeriodTypesList.indexOf(period) + 1);
			this.payHead = payHead;
		}

		payHead.setName((String) get(PAY_HEAD_NAME).getValue());
		String payHeadType = get(PAY_HEAD_TYPE).getValue();
		payHead.setType(payHeadTypesList.indexOf(payHeadType) + 1);
		String calType = get(CALCULATION_TYPE).getValue();
		payHead.setCalculationType(calculationTypesList.indexOf(calType) + 1);
		payHead.setRoundingMethod(1);
		payHead.setNameToAppearInPaySlip((String) get(PAY_HEAD_SLIPNAME)
				.getValue());
		boolean affectNetSalary = true;
		if (payHead.getType() == ClientPayHead.TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS) {
			affectNetSalary = false;
		}
		
		payHead.setAffectNetSalary(affectNetSalary);
		Account account = get(EXPENSE_ACCOUNT).getValue();
		payHead.setExpenseAccount(account.getID());
		create(payHead, context);
		return null;
	}
}
