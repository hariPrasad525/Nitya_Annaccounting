package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientAttendancePayHead;
import com.vimukti.accounter.web.client.core.ClientComputaionFormulaFunction;
import com.vimukti.accounter.web.client.core.ClientComputationSlab;
import com.vimukti.accounter.web.client.core.ClientComputionPayHead;
import com.vimukti.accounter.web.client.core.ClientFlatRatePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientUserDefinedPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.AttendanceOrProductionTypeCombo;
import com.vimukti.accounter.web.client.ui.combo.DepreciationAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayheadCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewPayHeadView extends BaseView<ClientPayHead> {

	private StyledPanel panel;
	private TextItem nameItem, payslipNameItem;
	private SelectCombo typeCombo, calculationTypeCombo,
			calculationPeriodCombo, roundingMethodCombo;
	RadioGroupItem affectNetSalarytem;
	String[] types = { messages.earningsForEmployees(),
			messages.deductionsForEmployees(),
			messages.employeesStatutoryDeductions(),
			messages.employeesStatutoryContributions(),
			messages.employeesOtherCharges(), messages.bonus(),
			messages.loansAndAdvances(), messages.reimbursmentsToEmployees() };

	String[] calType = { messages.attendance(), messages.asComputedValue(),
			messages.flatRate(), messages.production(),
			messages.asUserDefined() };

	String[] calPeriod = { messages.days(), messages.weeks(), messages.months() };

	String[] roundingMethods = { messages.downward(), messages.normal(),
			messages.upward() };

	String[] perDayCalculations = { messages.asPerCalendarPeriod(),
			messages.days30(), messages.userDefinedCalendar() };

	String[] computationTypes = { messages.onDeductionTotal(),
			messages.onEarningTotal(), messages.onSubTotal(),
			messages.onSpecifiedFormula() };

	String[] attendanceTypes = { messages.otherPayhead(),
			messages.onEarningTotal(), messages.onSubTotal(), messages.rate() };

	List<String> typeList = new ArrayList<String>();
	List<String> calTypeList = new ArrayList<String>();
	List<String> calPeriodList = new ArrayList<String>();
	List<String> roundingList = new ArrayList<String>();
	private List<String> perDayCalcList = new ArrayList<String>();
	private List<String> computationTypeList = new ArrayList<String>();
	private List<String> attendanceTypeList = new ArrayList<String>();
	private AttendanceOrProductionTypeCombo productionTypeCombo;
	private SelectCombo perdayCalculationCombo;
	private SelectCombo computationTypeCombo, attendanceTypeCombo;
	private ComputationSlabTable slabTable;
	private DynamicForm formulaForm, computationForm, calculationForm,
			attendanceForm, flatrateForm, productionForm;

	private List<ClientComputaionFormulaFunction> formulas = new ArrayList<ClientComputaionFormulaFunction>();
	private DepreciationAccountCombo accountCombo;
	private AccountCombo liabilityCombo;
	private AccountCombo assetCombo;

	private AddNewButton itemTableButton;
	private LabelItem formula;
	private DynamicForm mainform, leftForm, rightForm, attendanceLeftForm,
			attendanceRightForm;
	private DynamicForm compuPeriodAndTypeForm;
	private ComputationFormulaDialog dialog;
	private AttendanceOrProductionTypeCombo userDefinedCalendarCombo;
	private DynamicForm userDefinedCalendarForm;
	private DynamicForm attendanceTypeForm;
	private PayheadCombo payheadCombo;
	private StyledPanel tablePanel;

	public NewPayHeadView() {
		this.getElement().setId("NewPayHeadView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientPayHead());
			calculationPeriodCombo.setComboItem(messages.months());
		} else {
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientPayHead data) {
		if (data.getID() == 0) {
			return;
		}
		nameItem.setValue(data.getName());
		typeCombo.setComboItem(ClientPayHead.getPayHeadType(data.getType()));
		affectNetSalarytem.setValue(data.isAffectNetSalary() ? messages.yes()
				: messages.no());
		payslipNameItem.setValue(data.getNameToAppearInPaySlip());
		if (data.getRoundingMethod() != 0) {
			roundingMethodCombo.setComboItem(roundingList.get(data
					.getRoundingMethod() - 1));
		}
		long expenseAccount = data.getExpenseAccount();
		if (expenseAccount != 0L) {
			accountCombo.setComboItem(getCompany().getAccount(expenseAccount));
		}
		long liabilityAccount = data.getLiabilityAccount();
		if (liabilityAccount != 0L) {
			liabilityCombo.setComboItem(getCompany().getAccount(
					liabilityAccount));
		}

		long assetAccount = data.getAssetAccount();
		if (assetAccount != 0L) {
			assetCombo.setComboItem(getCompany().getAccount(assetAccount));
		}

		doVisibleAccounts();
		calculationTypeCombo.setComboItem(ClientPayHead.getCalculationType(data
				.getCalculationType()));
		if (data.getCalculationType() == 0) {
			return;
		}
		
		String calType = ClientPayHead.getCalculationType(data
				.getCalculationType());
		calculationTypeChanged(calType);
		if (calType.equals(messages.attendance())
				|| calType.equals(messages.production())) {
			ClientAttendancePayHead attendance = (ClientAttendancePayHead) data;
			productionTypeCombo.setSelectedId(attendance.getProductionType());
			payheadCombo.setSelectedId(attendance.getPayhead());
			String attendanceType = ClientAttendancePayHead
					.getAttendanceType(attendance.getAttendanceType());
			attendanceTypeCombo.setComboItem(attendanceType);
			attendanceTypeChanged(attendanceType);

			if (attendance.getAttendanceType() == ClientAttendancePayHead.PAY_HEAD) {
				payheadCombo.setSelectedId(attendance.getPayhead());
			}

			calculationPeriodCombo.setComboItem(getCalculationPeriod(attendance
					.getCalculationPeriod()));
			perdayCalculationCombo
					.setComboItem(getPerdayCalculationBasis(attendance
							.getPerDayCalculationBasis()));
		} else if (calType.equals(messages.asComputedValue())) {
			ClientComputionPayHead computation = (ClientComputionPayHead) data;

			calculationPeriodCombo
					.setComboItem(getCalculationPeriod(computation
							.getCalculationPeriod()));
			String compType = computationTypeList.get(computation
					.getComputationType() - 1);
			computationTypeCombo.setComboItem(compType);
			if (compType.equals(messages.onSpecifiedFormula())) {
				this.formulas = computation.getFormulaFunctions();
			}
			slabTable.setAllRows(computation.getSlabs());

		} else if (calType.equals(messages.flatRate())) {
			ClientFlatRatePayHead flatrate = (ClientFlatRatePayHead) data;

			calculationPeriodCombo.setComboItem(getCalculationPeriod(flatrate
					.getCalculationPeriod()));
		}
	}

	private String getPerdayCalculationBasis(int perDayCalculationBasis) {
		switch (perDayCalculationBasis) {
		case ClientAttendancePayHead.PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD:
			return messages.asPerCalendarPeriod();

		case ClientAttendancePayHead.PER_DAY_CALCULATION_30_DAYS:
			return messages.days30();

		case ClientAttendancePayHead.PER_DAY_CALCULATION_USER_DEFINED_CALANDAR:
			return messages.userDefindCalendar();

		default:
			return null;
		}
	}

	private String getCalculationPeriod(int calculationPeriod) {
		switch (calculationPeriod) {
		case ClientPayHead.CALCULATION_PERIOD_DAYS:
			return messages.days();

		case ClientPayHead.CALCULATION_PERIOD_MONTHS:
			return messages.months();

		case ClientPayHead.CALCULATION_PERIOD_WEEKS:
			return messages.weeks();

		default:
			return null;
		}
	}

	protected void createControls() {
		panel = new StyledPanel("panel");

		for (int i = 0; i < types.length; i++) {
			typeList.add(types[i]);
		}

		for (int i = 0; i < calType.length; i++) {
			calTypeList.add(calType[i]);
		}

		for (int i = 0; i < calPeriod.length; i++) {
			calPeriodList.add(calPeriod[i]);
		}

		for (int i = 0; i < roundingMethods.length; i++) {
			roundingList.add(roundingMethods[i]);
		}

		for (int i = 0; i < perDayCalculations.length; i++) {
			perDayCalcList.add(perDayCalculations[i]);
		}

		for (int i = 0; i < computationTypes.length; i++) {
			computationTypeList.add(computationTypes[i]);
		}

		for (int i = 0; i < attendanceTypes.length; i++) {
			attendanceTypeList.add(attendanceTypes[i]);
		}

		nameItem = new TextItem(messages.name(), "nameItem");
		nameItem.setRequired(true);
		nameItem.setEnabled(!isInViewMode());

		typeCombo = new SelectCombo(messages.payHeadType(), false);
		typeCombo.initCombo(typeList);
		typeCombo.setRequired(true);
		typeCombo.setEnabled(!isInViewMode());
		typeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						doVisibleAccounts();
					}
				});

		affectNetSalarytem = new RadioGroupItem(messages.affectNetSalary());
		affectNetSalarytem.setValueMap(messages.yes(), messages.no());
		affectNetSalarytem.setDefaultValue(messages.yes());
		affectNetSalarytem.setEnabled(!isInViewMode());
		affectNetSalarytem.setVisible(false);

		payslipNameItem = new TextItem(messages.paySlipName(),
				"payslipNameItem");
		payslipNameItem.setEnabled(!isInViewMode());

		roundingMethodCombo = new SelectCombo(messages.roundingMethod(), false);
		roundingMethodCombo.initCombo(roundingList);
		roundingMethodCombo.setEnabled(!isInViewMode());

		accountCombo = new DepreciationAccountCombo(messages.expenseAccount());
		accountCombo.setEnabled(!isInViewMode());

		liabilityCombo = new AccountCombo(messages2.statutoryLiabilityAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				List<ClientAccount> accounts = new ArrayList<ClientAccount>();
				for (ClientAccount account : getCompany().getActiveAccounts()) {
					if (account.getBaseType() == ClientAccount.BASETYPE_LIABILITY)
						accounts.add(account);
				}
				return accounts;
			}

			public void onAddNew() {
				NewAccountAction action = new NewAccountAction();
				List<Integer> options = new ArrayList<Integer>();
				options.add(ClientAccount.TYPE_ACCOUNT_PAYABLE);
				options.add(ClientAccount.TYPE_CREDIT_CARD);
				options.add(ClientAccount.TYPE_LONG_TERM_LIABILITY);
				options.add(ClientAccount.TYPE_OTHER_CURRENT_LIABILITY);
				options.add(ClientAccount.TYPE_PAYROLL_LIABILITY);
				action.setAccountTypes(options);
				action.setCallback(new ActionCallback<ClientAccount>() {

					@Override
					public void actionResult(ClientAccount result) {
						addItemThenfireEvent(result);
					}
				});
				action.run(null, true);

			}
		};
		liabilityCombo.setEnabled(!isInViewMode());

		assetCombo = new AccountCombo(messages2.assetAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				List<ClientAccount> accounts = new ArrayList<ClientAccount>();
				for (ClientAccount account : getCompany().getActiveAccounts()) {
					if (account.getBaseType() == ClientAccount.BASETYPE_ASSET)
						accounts.add(account);
				}
				return accounts;
			}

			public void onAddNew() {
				NewAccountAction action = new NewAccountAction();
				List<Integer> options = new ArrayList<Integer>();
				options.add(ClientAccount.TYPE_CASH);
				options.add(ClientAccount.TYPE_BANK);
				options.add(ClientAccount.TYPE_ACCOUNT_RECEIVABLE);
				options.add(ClientAccount.TYPE_OTHER_CURRENT_ASSET);
				options.add(ClientAccount.TYPE_INVENTORY_ASSET);
				options.add(ClientAccount.TYPE_FIXED_ASSET);
				options.add(ClientAccount.TYPE_OTHER_ASSET);
				action.setAccountTypes(options);
				action.setCallback(new ActionCallback<ClientAccount>() {

					@Override
					public void actionResult(ClientAccount result) {
						addItemThenfireEvent(result);
					}
				});
				action.run(null, true);

			}
		};
		assetCombo.setEnabled(!isInViewMode());

		calculationTypeCombo = new SelectCombo(messages.calculationType(),
				false);
		calculationTypeCombo.initCombo(calTypeList);
		calculationTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						calculationTypeChanged(selectItem);
					}
				});
		calculationTypeCombo.setRequired(true);
		calculationTypeCombo.setEnabled(!isInViewMode());

		calculationPeriodCombo = new SelectCombo(messages.calculationPeriod(),
				false);
		calculationPeriodCombo.initCombo(calPeriodList);
		calculationPeriodCombo.setEnabled(!isInViewMode());
		calculationPeriodCombo.setRequired(true);

		productionTypeCombo = new AttendanceOrProductionTypeCombo(
				ClientAttendanceOrProductionType.TYPE_PRODUCTION,
				messages.productionType(), "productionTypeCombo");
		productionTypeCombo.setRequired(true);
		productionTypeCombo.setEnabled(!isInViewMode());

		attendanceTypeCombo = new SelectCombo(messages.deductionOn());
		attendanceTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						attendanceTypeChanged(selectItem);
					}
				});
		attendanceTypeCombo.initCombo(attendanceTypeList);
		attendanceTypeCombo.setEnabled(!isInViewMode());
		attendanceTypeCombo.setRequired(true);

		payheadCombo = new PayheadCombo(messages.payhead()) {
			@Override
			protected void setComboItem() {
				super.setComboItem();
				if (!formulas.isEmpty()) {
					prepareFormula(formulas);
				}
			}
		};

		payheadCombo.setEnabled(!isInViewMode());
		payheadCombo.setRequired(true);

		perdayCalculationCombo = new SelectCombo(
				messages.perDayCalculationBasis(), false);
		perdayCalculationCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						perDayCalculationChanged(selectItem);
					}
				});
		perdayCalculationCombo.initCombo(perDayCalcList);
		perdayCalculationCombo.setEnabled(!isInViewMode());
		perdayCalculationCombo.setRequired(true);

		userDefinedCalendarCombo = new AttendanceOrProductionTypeCombo(
				ClientAttendanceOrProductionType.TYPE_USER_DEFINED_CALENDAR,
				messages.userDefinedCalendar(), "userDefinedCalendarCombo");
		userDefinedCalendarCombo.setEnabled(!isInViewMode());
		userDefinedCalendarCombo.setRequired(true);

		computationTypeCombo = new SelectCombo(messages.compute(), false);
		computationTypeCombo.initCombo(computationTypeList);
		computationTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						computationTypeChanged(selectItem);
					}
				});
		computationTypeCombo.setEnabled(!isInViewMode());
		computationTypeCombo.setRequired(true);

		slabTable = new ComputationSlabTable();
		slabTable.setEnabled(!isInViewMode());

		formula = new LabelItem(messages.specifiedFormula(), "formula");

		leftForm = new DynamicForm("leftForm");
		rightForm = new DynamicForm("rightForm");
		mainform = new DynamicForm("form");

		leftForm.add(nameItem, typeCombo, affectNetSalarytem,
				calculationTypeCombo);
		rightForm
				.add(payslipNameItem, accountCombo, liabilityCombo, assetCombo);

		mainform.add(leftForm);
		mainform.add(rightForm);

		calculationForm = new DynamicForm("calculationForm");

		panel.add(mainform);
		panel.add(calculationForm);
		Label lab1 = new Label(messages.payhead());
		lab1.setStyleName("label-title");
		this.add(lab1);
		this.add(panel);
		setSize("100%", "100%");
	}

	protected void doVisibleAccounts() {
		int payHeadType = typeCombo.getSelectedIndex() + 1;

		if (payHeadType == ClientPayHead.TYPE_LOANS_AND_ADVANCES) {
			assetCombo.setVisible(true);
		} else {
			assetCombo.setVisible(false);
		}

		if (payHeadType != ClientPayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS
				&& payHeadType != ClientPayHead.TYPE_LOANS_AND_ADVANCES) {
			accountCombo.setVisible(true);
		} else {
			accountCombo.setVisible(false);
		}

		if (payHeadType == ClientPayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS
				|| payHeadType == ClientPayHead.TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS) {
			liabilityCombo.setVisible(true);
		} else {
			liabilityCombo.setVisible(false);
		}

	}

	protected void attendanceTypeChanged(String selectItem) {
		if (selectItem == null) {
			return;
		}
		attendanceTypeForm.clear();
		if (selectItem.equals(messages.otherPayhead())) {
			attendanceTypeForm.add(payheadCombo);
		}
	}

	protected void perDayCalculationChanged(String selectItem) {
		userDefinedCalendarForm.clear();
		if (selectItem.equals(messages.userDefinedCalendar())) {
			userDefinedCalendarForm.add(userDefinedCalendarCombo);
		}
	}

	protected void computationTypeChanged(String selectItem) {
		if (selectItem.equals(messages.onSpecifiedFormula())) {
			if (dialog == null) {
				dialog = new ComputationFormulaDialog(
						messages.computationFormula()) {
					@Override
					protected boolean onOK() {

						return super.onOK();
					}
				};
				dialog.addCallback(new ValueCallBack<List<ClientComputaionFormulaFunction>>() {

					@Override
					public void execute(
							List<ClientComputaionFormulaFunction> value) {
						NewPayHeadView.this.formulas = value;
						prepareFormula(value);
					}
				});
			}
			if (!formulas.isEmpty()) {
				dialog.setData(formulas);
			}
			ViewManager.getInstance().showDialog(dialog);
		} else {
			dialog = null;
			formulaForm.clear();
		}
	}

	protected void prepareFormula(List<ClientComputaionFormulaFunction> formulas) {
		for (ClientComputaionFormulaFunction formula : formulas) {
			formula.setClientPayHead(payheadCombo.getPayHead(formula
					.getPayHead()));
		}
		String string = UIUtils.prepareFormula(formulas);
		formula.setValue(string);
		formulaForm.add(formula);
	}

	protected void calculationTypeChanged(String selectItem) {
		calculationForm.clear();
		if (selectItem.equals(messages.attendance())
				|| selectItem.equals(messages.production())) {
			attendanceForm = new DynamicForm("attendanceForm");

			attendanceLeftForm = new DynamicForm("attendanceLeftForm");
			attendanceRightForm = new DynamicForm("attendanceRightForm");
			userDefinedCalendarForm = new DynamicForm("userDefinedCalendarForm");
			attendanceTypeForm = new DynamicForm("attendanceTypeForm");

			attendanceLeftForm.add(attendanceTypeCombo);
			attendanceLeftForm.add(attendanceTypeForm);
			// attendanceLeftForm.add(calculationPeriodCombo);
			attendanceRightForm.add(perdayCalculationCombo);
			attendanceRightForm.add(userDefinedCalendarForm);

			attendanceForm.add(attendanceLeftForm);
			if (selectItem.equals(messages.attendance())) {
				attendanceForm.add(attendanceRightForm);
			}

			calculationForm.add(attendanceForm);

			if (selectItem.equals(messages.production())) {
				productionForm = new DynamicForm("productionForm");
				productionForm.add(productionTypeCombo);
				calculationForm.add(productionForm);

			}

		} else if (selectItem.equals(messages.asComputedValue())) {
			formulaForm = new DynamicForm("computationForm");
			computationForm = new DynamicForm("computationForm");
			compuPeriodAndTypeForm = new DynamicForm("compuPeriodAndTypeForm");
			this.tablePanel = new StyledPanel("addnew_edit_panel");
			tablePanel.add(slabTable);

			compuPeriodAndTypeForm.add(calculationPeriodCombo,
					computationTypeCombo);
			computationForm.add(compuPeriodAndTypeForm);
			computationForm.add(formulaForm);
			computationForm.add(tablePanel);
			calculationForm.add(computationForm);

		} else if (selectItem.equals(messages.flatRate())) {
			flatrateForm = new DynamicForm("flaterateForm");
			flatrateForm.add(calculationPeriodCombo);
			calculationForm.add(flatrateForm);

		}
		updateButtons();
	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));

				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = data.getObjectType();
		this.rpcDoSerivce.canEdit(type, data.getID(), editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);

		nameItem.setEnabled(!isInViewMode());
		computationTypeCombo.setEnabled(!isInViewMode());
		slabTable.setEnabled(!isInViewMode());
		if (itemTableButton != null) {
			itemTableButton.setEnabled(!isInViewMode());
		}
		userDefinedCalendarCombo.setEnabled(!isInViewMode());
		productionTypeCombo.setEnabled(!isInViewMode());
		payheadCombo.setEnabled(!isInViewMode());
		accountCombo.setEnabled(!isInViewMode());
		liabilityCombo.setEnabled(!isInViewMode());
		assetCombo.setEnabled(!isInViewMode());
		slabTable.setEnabled(!isInViewMode());
		computationTypeCombo.setEnabled(!isInViewMode());
		attendanceTypeCombo.setEnabled(!isInViewMode());
		perdayCalculationCombo.setEnabled(!isInViewMode());
		typeCombo.setEnabled(!isInViewMode());
		calculationPeriodCombo.setEnabled(!isInViewMode());
		payslipNameItem.setEnabled(!isInViewMode());
		roundingMethodCombo.setEnabled(!isInViewMode());
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		result.add(leftForm.validate());
		result.add(rightForm.validate());

		if (result.haveErrors()) {
			return result;
		}
		// Checking The PayHead Name
		String payHeadName = nameItem.getValue();
		ClientPayHead payHeadByName = getCompany()
				.getPayHeadByName(payHeadName);
		if (payHeadByName != null
				&& !(this.getData().getID() == payHeadByName.getID())) {
			result.addError(nameItem, messages.alreadyExist());
			return result;
		}
		String selectedValue = calculationTypeCombo.getSelectedValue();
		if (selectedValue.equals(messages.attendance())) {
			result.add(attendanceLeftForm.validate());
			result.add(attendanceRightForm.validate());
			result.add(userDefinedCalendarForm.validate());
			result.add(attendanceTypeForm.validate());

		} else if (selectedValue.equals(messages.asComputedValue())) {
			result.add(compuPeriodAndTypeForm.validate());
			result.add(slabTable.validate());
			if (result.haveErrors()) {
				return result;
			}
		} else if (selectedValue.equals(messages.flatRate())) {
			result.add(flatrateForm.validate());

		} else if (selectedValue.equals(messages.production())) {
			result.add(productionForm.validate());
		}

		int payHeadType = typeCombo.getSelectedIndex() + 1;
		if (payHeadType == ClientPayHead.TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS
				|| payHeadType == ClientPayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS) {
			if (liabilityCombo.getSelectedValue() == null) {
				result.addError(liabilityCombo,
						"Statutory Liability Account should not be null.");
			}
		}

		if (payHeadType == ClientPayHead.TYPE_LOANS_AND_ADVANCES) {
			if (assetCombo.getSelectedValue() == null) {
				result.addError(liabilityCombo,
						"Asset Account should not be null.");
			}
		}
		if (payHeadType != ClientPayHead.TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS
				&& payHeadType != ClientPayHead.TYPE_LOANS_AND_ADVANCES) {
			if (accountCombo.getSelectedValue() == null) {
				result.addError(accountCombo,
						"Expense Account should not be null.");
			}
		}
		return result;
	}

	private void updateData() {

		String selectedValue = calculationTypeCombo.getSelectedValue();
		if (selectedValue == null) {
			return;
		}
		if (selectedValue.equals(messages.attendance())
				|| selectedValue.equals(messages.production())) {
			ClientAttendancePayHead payhead = new ClientAttendancePayHead();
			payhead.setAttendanceType(attendanceTypeCombo.getSelectedIndex() + 1);
			String attendanceType = ClientAttendancePayHead
					.getAttendanceType(payhead.getAttendanceType());

			if (attendanceType.equals(messages.otherPayhead())) {
				payhead.setPayhead(payheadCombo.getSelectedValue().getID());
			}
			payhead.setID(data.getID());
			if (selectedValue.equals(messages.attendance())) {
				payhead.setCalculationPeriod(calculationPeriodCombo
						.getSelectedIndex() + 1);
				payhead.setPerDayCalculationBasis(perdayCalculationCombo
						.getSelectedIndex() + 1);
			}

			if (selectedValue.equals(messages.production())) {
				payhead.setProductionType(productionTypeCombo
						.getSelectedValue().getID());
				payhead.setPerDayCalculationBasis(ClientAttendancePayHead.PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD);
				if (attendanceType.equals(messages.otherPayhead())) {
					payhead.setPayhead(payheadCombo.getSelectedValue().getID());
				}
			}
			data = payhead;

		} else if (selectedValue.equals(messages.asComputedValue())) {
			ClientComputionPayHead payhead = new ClientComputionPayHead();
			payhead.setID(data.getID());
			payhead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex() + 1);
			payhead.setComputationType(computationTypeCombo.getSelectedIndex() + 1);
			if (computationTypeCombo.getSelectedValue().equals(
					messages.onSpecifiedFormula())) {
				payhead.setFormulaFunctions(this.formulas);
			} else {
				payhead.setFormulaFunctions(new ArrayList<ClientComputaionFormulaFunction>());
			}
			payhead.setSlabs(slabTable.getAllRows());
			data = payhead;

		} else if (selectedValue.equals(messages.flatRate())) {
			ClientFlatRatePayHead payHead = new ClientFlatRatePayHead();
			payHead.setID(data.getID());
			payHead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex() + 1);
			data = payHead;

		} else if (selectedValue.equals(messages.asUserDefined())) {
			ClientUserDefinedPayHead userDefinedPayHead = new ClientUserDefinedPayHead();
			userDefinedPayHead.setID(data.getID());
			data = userDefinedPayHead;
		}

		data.setName(nameItem.getValue());
		data.setType(typeCombo.getSelectedIndex() + 1);
		data.setCalculationType(calculationTypeCombo.getSelectedIndex() + 1);
		data.setRoundingMethod(roundingMethodCombo.getSelectedIndex() + 1);
		data.setNameToAppearInPaySlip(payslipNameItem.getValue());

		// Update Accounts
		long expenseAccountId = 0l;
		ClientAccount expenseAccount = accountCombo.getSelectedValue();
		if (expenseAccount != null) {
			expenseAccountId = expenseAccount.getID();
		}
		data.setExpenseAccount(expenseAccountId);
		long liabilityId = 0l;
		ClientAccount liability = liabilityCombo.getSelectedValue();
		if (liability != null) {
			liabilityId = liability.getID();
		}
		data.setLiabilityAccount(liabilityId);

		long assetAccountId = 0l;
		ClientAccount assetAccount = assetCombo.getSelectedValue();
		if (assetAccount != null) {
			assetAccountId = assetAccount.getID();
		}
		data.setAssetAccount(assetAccountId);

		// IF IT IS NOT S.CONTRIBUTIO, THEN IT WILL AFFECT NET SALARY.
		boolean affectNetSalary = true;
		if (data.getType() == ClientPayHead.TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS) {
			affectNetSalary = false;
		}
		data.setAffectNetSalary(affectNetSalary);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		String errorString = AccounterExceptions
				.getErrorString(accounterException);
		Accounter.showError(errorString);
	}

	@Override
	public void setFocus() {
		nameItem.setFocus();
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
		return messages.newPayHead();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		if (tablePanel == null) {
			return;
		}
		itemTableButton = getItemButton();
		addButton(tablePanel, itemTableButton);
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		if (tablePanel == null) {
			return;
		}
		removeButton(tablePanel, itemTableButton);
	}

	private AddNewButton getItemButton() {
		itemTableButton = new AddNewButton();
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientComputationSlab row = new ClientComputationSlab();
				slabTable.addRow(row);
			}
		});
		itemTableButton.setEnabled(!isInViewMode());
		return itemTableButton;
	}
}
