package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.RecurringConfirmDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class RecurringTransactionDialog extends
		BaseDialog<ClientRecurringTransaction> {

	private TextItem nameField;
	private DateField startDateField;
	private DateField endDateField;
	public TextItem occurrencesField;
	private TextItem daysInAdvanceField, daysBeforeToRemind;

	// private TextAndComboPairForm dueField;

	private IntervalValueInputField intervalValueField;

	private ClientRecurringTransaction data;

	private final AbstractTransactionBaseView<? extends ClientTransaction> view;

	private List<DynamicForm> dynamicForms;

	private StyledPanel intervalLayout;
	private DynamicForm endDateTypeForm;
	private DynamicForm nameForm;
	private DynamicForm daysAdvForm;
	private DynamicForm notificationForm;

	private SelectCombo actionComboField;
	private SelectCombo dayOfWeekCombo;
	private SelectCombo dayOfMonthCombo;
	private SelectCombo weekOfMonthCombo;
	private SelectCombo monthOfYearCombo;
	private SelectCombo intervalTypeCombo;
	private SelectCombo recurringTypeCombo;
	private SelectCombo endDateTypeCombo;

	private RadioButton onSpecificDayRadioBtn;
	private RadioButton onSpecificWeekRadioBtn;

	private LabelItem daysBeforeLabel, daysInAdvanceLabel;

	private CheckboxItem unbilledChargesCkBox, alertWhenRangeEnded,
			notifyAboutCreatedTransactions;

	private Panel mainLayout;

	/**
	 * for creating new Recurring transaction.
	 * 
	 * @param parentView
	 */
	public RecurringTransactionDialog(
			AbstractTransactionBaseView<? extends ClientTransaction> parentView) {
		this(parentView, null);
		this.getElement().setId("RecurringTransactionDialog");
	}

	/**
	 * For editing existing recurring transaction.
	 * 
	 * @param parentView
	 * @param transaction
	 */
	public RecurringTransactionDialog(
			AbstractTransactionBaseView<? extends ClientTransaction> parentView,
			ClientRecurringTransaction transaction) {
		super(messages.recurring(), messages.recurringDescription());
		this.getElement().setId("RecurringTransactionDialog");
		this.view = parentView;
		if (parentView == null) {

		}
		data = transaction;
		init();
	}

	protected void init() {
		createControls();
		initData();
	}

	private void createControls() {
		initCombos();
		intervalValueField = new IntervalValueInputField();
		endDateTypeForm = new DynamicForm("endDateTypeForm");

		unbilledChargesCkBox = new CheckboxItem(
				messages.includeUnbilledCharges(), "unbilledChargesCkBox");

		initRadioBtns();

		recurringTypeCombo
				.addSelectionChangeHandler(new RecurringTypeChangeHandler());

		intervalTypeCombo
				.addSelectionChangeHandler(new IntervalTypeChangeHandler());

		endDateTypeCombo
				.addSelectionChangeHandler(new EndDateTypeChangeHandler());

		dynamicForms = new ArrayList<DynamicForm>();

		nameField = new TextItem(messages.name(), "nameField");
		nameField.setRequired(true);

		daysInAdvanceField = new TextItem(messages.daysInAdvance(),
				"daysInAdvanceField");
		daysInAdvanceLabel = new LabelItem("", "daysInAdvanceLabel");
		daysInAdvanceLabel.setShowTitle(false);
		daysInAdvanceLabel.setValue(messages.toCreate());

		notifyAboutCreatedTransactions = new CheckboxItem(
				messages.notifyAboutCreatedTransactions(),
				"notifyAboutCreatedTransactions");

		daysBeforeToRemind = new TextItem(messages.remindMe(),
				"daysBeforeToRemind");
		daysBeforeLabel = new LabelItem(messages.daysBefore(),
				"daysBeforeLabel");
		daysBeforeLabel.setShowTitle(false);
		// daysBeforeLabel.setValue(messages.daysBefore());
		notificationForm = new DynamicForm("notificationForm");
		notificationForm.add(notifyAboutCreatedTransactions);

		occurrencesField = new TextItem(messages.endAfterSpecifiedOccurences(),
				"occurrencesField");
		occurrencesField.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				RecurringTransactionDialog.this.clearError(occurrencesField);
				String value = occurrencesField.getValue();
				if (value == null) {
					return;
				}
				try {
					int occurrences = Integer.parseInt(value);
					if (occurrences < 1) {
						throw new Exception();
					}
				} catch (Exception e) {
					e.printStackTrace();
					occurrencesField.highlight();
					RecurringTransactionDialog.this.addError(occurrencesField,
							messages.pleaseEnterValidNumber());
					occurrencesField.setValue("");
				}

			}
		});
		occurrencesField.setRequired(false);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		ClientFinanceDate financeDate = new ClientFinanceDate(cal.getTime());

		startDateField = new DateField(messages.startDate(), "startDateField");
		startDateField.setRequired(true);
		startDateField.setEnteredDate(financeDate);

		endDateField = new DateField(messages.endDate(), "endDateField");
		endDateField.setEnteredDate(financeDate);

		alertWhenRangeEnded = new CheckboxItem(
				messages.alertWhenRangeHasEnded(), "alertWhenRangeEnded");

		actionComboField = new SelectCombo(messages.action());
		actionComboField.initCombo(getActionOptions());
		actionComboField.setRequired(true);

		nameForm = new DynamicForm("nameForm");
		nameForm.add(nameField, recurringTypeCombo);

		DynamicForm intervalForm = new DynamicForm("intervalForm");
		intervalForm.add(intervalTypeCombo);

		daysAdvForm = new DynamicForm("daysAdvForm");
		daysAdvForm.add(daysInAdvanceField, daysInAdvanceLabel);

		DynamicForm dateRangeForm = new DynamicForm("dateRangeForm");
		dateRangeForm.add(startDateField);
		endDateTypeForm.add(endDateTypeCombo);

		dynamicForms.add(nameForm);
		dynamicForms.add(intervalForm);
		dynamicForms.add(daysAdvForm);
		dynamicForms.add(dateRangeForm);
		dynamicForms.add(endDateTypeForm);
		dynamicForms.add(notificationForm);

		intervalLayout = new StyledPanel("intervalLayout");
		// intervalLayout.setBorderWidth(1);

		mainLayout = new StyledPanel("mainLayout");
		mainLayout.add(nameForm);
		mainLayout.add(intervalForm);
		mainLayout.add(intervalLayout);
		mainLayout.add(daysAdvForm);
		mainLayout.add(dateRangeForm);
		mainLayout.add(endDateTypeForm);
		mainLayout.add(notificationForm);

		// if (canIncludeDueDateSection()) {
		// dynamicForms.add(dueField);
		// mainLayout.add(dueField);
		// }

		// --------

		mainLayout.addStyleName("fields-panel");
		setBodyLayout(mainLayout);

		okbtn.setText(messages.saveTemplate());
		// okbtn.setWidth("100px");

		initDataForNewTemplate();
	}

	private void initRadioBtns() {
		onSpecificWeekRadioBtn = new RadioButton(messages.monthly(),
				messages.onSpecificWeek());
		onSpecificWeekRadioBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// disable
				dayOfMonthCombo.setEnabled(false);

				// enable
				dayOfWeekCombo.setEnabled(true);
				weekOfMonthCombo.setEnabled(true);

			}
		});

		onSpecificDayRadioBtn = new RadioButton(messages.monthly(),
				messages.onSpecificDay());
		onSpecificDayRadioBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// disable
				weekOfMonthCombo.setEnabled(false);
				dayOfWeekCombo.setEnabled(false);

				// enable
				dayOfMonthCombo.setEnabled(true);

			}
		});
	}

	private void enableAllCombos() {
		dayOfMonthCombo.setEnabled(true);
		dayOfWeekCombo.setEnabled(true);
		weekOfMonthCombo.setEnabled(true);
	}

	private class EndDateTypeChangeHandler implements
			IAccounterComboSelectionChangeHandler<String> {

		@Override
		public void selectedComboBoxItem(String selectItem) {

			int selected = getEndDateTypeOptions().indexOf(selectItem);

			endDateTypeForm.clear();

			switch (selected) {
			case 0:// no end date
				endDateTypeForm.add(endDateTypeCombo);
				break;
			case 1:// end after __ occurrences
				endDateTypeForm.add(endDateTypeCombo, occurrencesField,
						alertWhenRangeEnded);

				break;
			case 2: // stop after date
				endDateTypeForm.add(endDateTypeCombo, endDateField,
						alertWhenRangeEnded);
			default:
				break;
			}
		}
	}

	private class RecurringTypeChangeHandler implements
			IAccounterComboSelectionChangeHandler<String> {

		@Override
		public void selectedComboBoxItem(String selectItem) {
			int selected = getRecurringTypeOptions().indexOf(selectItem);
			switch (selected) {
			case 0:
				daysAdvForm.clear();
				daysAdvForm.add(daysInAdvanceField, daysInAdvanceLabel);
				notificationForm.clear();
				notificationForm.add(notifyAboutCreatedTransactions);
				changeFieldsStausForNoneSelection(false);
				break;
			case 1:
				daysAdvForm.clear();
				notificationForm.clear();
				notificationForm.add(daysBeforeToRemind, daysBeforeLabel);
				changeFieldsStausForNoneSelection(false);
				break;
			case 2:
				changeFieldsStausForNoneSelection(true);
				break;
			}
			if (selected == 2) {// none-just template
				// disable rest of fields
				changeFieldsStausForNoneSelection(true);
			} else {
				// schedule or remainder
				// enable rest of fields
				changeFieldsStausForNoneSelection(false);
			}

		}

	}

	private class IntervalTypeChangeHandler implements
			IAccounterComboSelectionChangeHandler<String> {

		@Override
		public void selectedComboBoxItem(String selectedValue) {
			int selected = getIntervalTypeOptions().indexOf(selectedValue);
			StyledPanel panel = null;

			switch (selected) {
			case 0:// Daily
				panel = getDailyIntervalLayout();
				intervalValueField.setIntervalTypeLabel(messages.days());
				break;
			case 1: // weekly
				enableAllCombos();
				panel = getWeeklyIntervalLayout();
				intervalValueField.setIntervalTypeLabel(messages.weeks());

				break;
			case 2: // monthly
				panel = getMonthlyIntervalLayout();
				intervalValueField.setIntervalTypeLabel(messages.months());

				break;
			case 3: // yearly
				enableAllCombos();
				panel = getYearlyIntervalLayout();
				intervalValueField.setIntervalTypeLabel(messages.years());
				break;
			}

			intervalLayout.clear();

			if (panel != null) {
				intervalLayout.add(panel);
			}

			// set default value if not shown.
			if (intervalValueField.getIngervalValue() == 0) {
				intervalValueField.setIntervalValue(1);
			}

		}
	}

	private void initCombos() {
		recurringTypeCombo = createCombo(messages.recurringType(),
				getRecurringTypeOptions());
		recurringTypeCombo.setRequired(true);

		dayOfWeekCombo = createCombo(messages.dayOfWeek(), getWeekOptions());
		dayOfMonthCombo = createCombo(messages.dayOfMonth(),
				getMonthDayOptions());
		weekOfMonthCombo = createCombo(messages.weekOfMonth(),
				getMonthWeekOptions());
		monthOfYearCombo = createCombo(messages.month(), getMonthOptions());
		intervalTypeCombo = createCombo(messages.intervalType(),
				getIntervalTypeOptions());
		intervalTypeCombo.setRequired(true);
		endDateTypeCombo = createCombo(messages.endDateType(),
				getEndDateTypeOptions());
		endDateTypeCombo.setRequired(true);
	}

	private List<String> getEndDateTypeOptions() {
		List<String> options = new ArrayList<String>();
		options.add(messages.noEndDate());
		options.add(messages.endAfterOccurrences());
		options.add(messages.endDateAfter());
		return options;
	}

	private List<String> getRecurringTypeOptions() {
		List<String> options = new ArrayList<String>();
		options.add(messages.scheduled());
		options.add(messages.reminder());
		options.add(messages.unScheduled());
		return options;
	}

	private List<String> getMonthOptions() {
		List<String> options = new ArrayList<String>();
		options.add(DayAndMonthUtil.january());
		options.add(DayAndMonthUtil.february());
		options.add(DayAndMonthUtil.march());
		options.add(DayAndMonthUtil.april());
		options.add(DayAndMonthUtil.may_full());
		options.add(DayAndMonthUtil.june());
		options.add(DayAndMonthUtil.july());
		options.add(DayAndMonthUtil.august());
		options.add(DayAndMonthUtil.september());
		options.add(DayAndMonthUtil.october());
		options.add(DayAndMonthUtil.november());
		options.add(DayAndMonthUtil.december());
		return options;
	}

	private List<String> getMonthWeekOptions() {
		List<String> options = new ArrayList<String>();
		options.add(messages.first());
		options.add(messages.second());
		options.add(messages.third());
		options.add(messages.fourth());
		options.add(messages.last());
		return options;
	}

	private List<String> getMonthDayOptions() {
		List<String> days = new ArrayList<String>();
		for (int i = 1; i <= 28; i++) {
			days.add(String.valueOf(i));
		}
		days.add(messages.last());
		return days;
	}

	private List<String> getWeekOptions() {
		List<String> weeks = new ArrayList<String>();
		weeks.add(DayAndMonthUtil.sunday());
		weeks.add(DayAndMonthUtil.monday());
		weeks.add(DayAndMonthUtil.tuesday());
		weeks.add(DayAndMonthUtil.wednesday());
		weeks.add(DayAndMonthUtil.thursday());
		weeks.add(DayAndMonthUtil.friday());
		weeks.add(DayAndMonthUtil.saturday());
		return weeks;
	}

	private SelectCombo createCombo(String text, List<String> optoins) {
		SelectCombo combo = new SelectCombo(text);
		combo.initCombo(optoins);
		return combo;
	}

	private StyledPanel getDailyIntervalLayout() {
		StyledPanel panel = new StyledPanel("interval_Value");
		panel.add(intervalValueField);
		return panel;
	}

	private StyledPanel getWeeklyIntervalLayout() {
		StyledPanel panel = new StyledPanel("interval_Value");
		panel.add(intervalValueField);
		panel.add(dayOfWeekCombo.getMainWidget());
		return panel;
	}

	private StyledPanel getMonthlyIntervalLayout() {
		StyledPanel panel = new StyledPanel("interval_Value");
		panel.add(intervalValueField);
		panel.add(onSpecificDayRadioBtn);
		panel.add(dayOfMonthCombo.getMainWidget());
		panel.add(onSpecificWeekRadioBtn);

		StyledPanel hPanel = new StyledPanel("hPanel");
		hPanel.add(weekOfMonthCombo.getMainWidget());
		hPanel.add(dayOfWeekCombo.getMainWidget());
		panel.add(hPanel);

		if (!onSpecificDayRadioBtn.getValue()) {
			dayOfMonthCombo.setEnabled(false);
		}
		if (!onSpecificWeekRadioBtn.getValue()) {
			weekOfMonthCombo.setEnabled(false);
			dayOfWeekCombo.setEnabled(false);
		}

		return panel;
	}

	private StyledPanel getYearlyIntervalLayout() {
		StyledPanel panel = new StyledPanel("interval_Value");
		panel.add(intervalValueField);

		StyledPanel tempPanel = new StyledPanel("tempPanel");
		tempPanel.add(monthOfYearCombo.getMainWidget());
		tempPanel.add(dayOfMonthCombo.getMainWidget());

		panel.add(tempPanel);
		return panel;
	}

	protected void initData() {

		if (data == null) {
			data = new ClientRecurringTransaction();
			initDataForNewTemplate();
		} else {
			recurringTypeCombo.setSelectedItem(data.getType());
			nameField.setValue(data.getName());
			if (data.getType() == ClientRecurringTransaction.RECURRING_UNSCHEDULED) {
				changeFieldsStausForNoneSelection(true);
				return;
			} else if (data.getType() == ClientRecurringTransaction.RECURRING_REMINDER) {
				daysAdvForm.clear();
				notificationForm.clear();
				notificationForm.add(daysBeforeToRemind, daysBeforeLabel);
				changeFieldsStausForNoneSelection(false);
			} else if (data.getType() == ClientRecurringTransaction.RECURRING_SCHEDULED) {
				daysAdvForm.clear();
				daysAdvForm.add(daysInAdvanceField, daysInAdvanceLabel);
				notificationForm.clear();
				notificationForm.add(notifyAboutCreatedTransactions);
				changeFieldsStausForNoneSelection(false);
			}
			startDateField.setValue(new ClientFinanceDate(data.getStartDate()));
			unbilledChargesCkBox.setValue(data.isUnbilledChargesEnabled());
			daysInAdvanceField.setValue(String.valueOf(data
					.getDaysInAdvanceToCreate()));

			// FIXME This option may need to enable after implemented approval
			// process in transactions.
			// actionComboField.setSelected(getActionOptions().get(
			// data.getActionType()));

			// Interval fields
			updateIntervalValueFieldsFormObject();

			// end day fields
			updateEndDateVaulesFromObject();
		}
		daysBeforeToRemind
				.setValue(String.valueOf(data.getDaysBeforeToRemind()));
		notifyAboutCreatedTransactions.setValue(data
				.isNotifyCreatedTransaction());
	}

	private void initDataForNewTemplate() {
		recurringTypeCombo.setDefaultToFirstOption(true);

		intervalTypeCombo.setSelected(getIntervalTypeOptions().get(2));
		intervalValueField.setIntervalValue(1);
		intervalValueField.setIntervalTypeLabel(messages.months());
		onSpecificDayRadioBtn.setValue(true);
		weekOfMonthCombo.setEnabled(true);
		dayOfWeekCombo.setEnabled(true);
		dayOfMonthCombo.setEnabled(true);
		dayOfMonthCombo.setComboItem(String.valueOf(1));

		StyledPanel panel = getMonthlyIntervalLayout();
		intervalLayout.clear();
		if (panel != null) {
			intervalLayout.add(panel);
		}
		weekOfMonthCombo.setSelectedItem(0);
		dayOfWeekCombo.setSelectedItem(0);
		monthOfYearCombo.setSelectedItem(0);
		endDateTypeCombo.setSelectedItem(0);

	}

	private void updateEndDateVaulesFromObject() {
		int endDateType = data.getEndDateType();
		endDateTypeCombo.setSelectedItem(endDateType);
		switch (endDateType) {
		case 0: // no end date
			break;
		case 1: // after __ occurrences
			occurrencesField
					.setValue(String.valueOf(data.getOccurencesCount()));
			break;
		case 2: // after date
			endDateField.setValue(new ClientFinanceDate(data.getEndDate()));
			break;
		default:
			break;
		}

		// manually invoke event on field to update UI
		endDateTypeCombo.getHandler().selectedComboBoxItem(
				endDateTypeCombo.getSelectedValue());
	}

	private void updateIntervalValueFieldsFormObject() {
		int intervalType = data.getIntervalType();
		intervalValueField.setIntervalValue(data.getIntervalPeriod());

		switch (intervalType) {
		case 0:// daily
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(0));

			break;
		case 1: // weekly
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(1));
			dayOfWeekCombo.setSelectedItem(data.getWeekDay() - 1);
			break;
		case 2: // monthly_day
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(2));
			onSpecificDayRadioBtn.setValue(true);

			if (data.getDayOfMonth() != -1) {
				dayOfMonthCombo.setComboItem(String.valueOf(data
						.getDayOfMonth()));
			} else {
				dayOfMonthCombo.setComboItem(messages.last());
			}
			break;
		case 3: // monthly_week
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(2));
			onSpecificWeekRadioBtn.setValue(true);

			weekOfMonthCombo.setSelectedItem(data.getWeekOfMonth() - 1);
			dayOfWeekCombo.setSelectedItem(data.getWeekDay() - 1);
			break;
		case 4: // yearly
			intervalTypeCombo.setSelected(getIntervalTypeOptions().get(3));

			if (data.getDayOfMonth() != -1) {
				dayOfMonthCombo.setComboItem(String.valueOf(data
						.getDayOfMonth()));
			} else {
				dayOfMonthCombo.setComboItem(messages.last());
			}
			monthOfYearCombo.setSelectedItem(data.getMonth());
			break;
		default:
			break;
		}

		// manually invoke event on field to update UI
		intervalTypeCombo.getHandler().selectedComboBoxItem(
				intervalTypeCombo.getSelectedValue());
	}

	@Override
	protected boolean onOK() {
		boolean shouldValidate = getRecurringTypeOptions().indexOf(
				recurringTypeCombo.getSelectedValue()) == ClientRecurringTransaction.RECURRING_SCHEDULED;
		if (view == null || view.validateAndUpdateTransaction(shouldValidate)) {
			updateData();
			saveOrUpdate(data);
			okbtn.setEnabled(false);
			cancelBtn.setEnabled(false);
			return false;
		}
		return true;
	}

	@Override
	public void saveFailed(AccounterException exception) {
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
		okbtn.setEnabled(true);
		cancelBtn.setEnabled(true);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		this.removeFromParent();
		okbtn.setEnabled(true);
		cancelBtn.setEnabled(true);
		if (view != null) {
			view.recurringDialog = null;
			ClientTransaction transactionObject = view.getTransactionObject();
			transactionObject.setID(transctionId);
		}
		RecurringConfirmDialog success = new RecurringConfirmDialog();
		ViewManager.getInstance().showDialog(success);
		transctionId = 0;
	}

	long transctionId = 0;

	/**
	 * Assigns values to object from fields.
	 * 
	 * @param recTransaction
	 */
	private void updateData() {
		// common fields for all type of intervaltypes
		int index;

		index = getRecurringTypeOptions().indexOf(
				recurringTypeCombo.getSelectedValue());
		data.setType(index);
		data.setName(nameField.getValue());
		data.setStartDate(startDateField.getTime());
		data.setEndDate(endDateField.getTime());
		data.setUnbilledChargesEnabled(unbilledChargesCkBox.isChecked());
		try {
			String value = daysInAdvanceField.getValue();
			data.setDaysInAdvanceToCreate((value != null && !value.equals("")) ? Integer
					.parseInt(value) : 0);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// as this field is optional user may not enter value
			data.setDaysInAdvanceToCreate(0);
		}
		// Action type
		index = getActionOptions().indexOf(actionComboField.getSelectedValue());
		data.setActionType(index);

		// interval type
		index = getIntervalTypeOptions().indexOf(
				intervalTypeCombo.getSelectedValue());
		updateIntervalFromFields(index, data);

		// end day type
		index = getEndDateTypeOptions().indexOf(
				endDateTypeCombo.getSelectedValue());
		data.setEndDateType(index);
		updateEndDayValuesFromFields(index, data);

		data.setAlertWhenEnded(alertWhenRangeEnded.getValue());

		if (data.getType() == ClientRecurringTransaction.RECURRING_SCHEDULED) {
			data.setNotifyCreatedTransaction(notifyAboutCreatedTransactions
					.getValue());
			String value = daysBeforeToRemind.getValue();
			data.setDaysBeforeToRemind((value != null && !value.equals("")) ? Integer
					.parseInt(value) : 0);
		} else if (data.getType() == ClientRecurringTransaction.RECURRING_REMINDER) {
			try {
				String value = daysBeforeToRemind.getValue();
				data.setDaysBeforeToRemind((value != null && !value.equals("")) ? Integer
						.parseInt(value) : 0);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				// as this field is optional user may not enter value
				data.setDaysBeforeToRemind(0);
			}
		}

		// This condition is placed because when user wanted to edit recurring
		// schedule we need to save only schedule not with template. Template
		// will be saved separately. But first time we should save transaction
		// also as a template.
		ClientTransaction transaction = null;
		if (view != null) {
			transaction = view.getTransactionObject();
			transctionId = transaction.getID();
		}
		if (data.getId() == 0 && transaction != null) {
			transaction.setID(0);
			data.setTransaction(transaction);
		}
	}

	private void updateEndDayValuesFromFields(int index,
			ClientRecurringTransaction recTransaction) {
		switch (index) {
		case 0:// no end date
			break;
		case 1:// after __ occurrences
			recTransaction.setOccurencesCount(Integer.parseInt(occurrencesField
					.getValue()));
			break;
		case 2:// no end date
			recTransaction.setEndDate(endDateField.getTime());
			break;
		default:
			break;
		}
	}

	private void updateIntervalFromFields(int index,
			ClientRecurringTransaction recTransaction) {

		recTransaction.setIntervalPeriod(intervalValueField.getIngervalValue());
		switch (index) {
		case 0:// daily
			recTransaction
					.setIntervalType(ClientRecurringTransaction.INTERVAL_TYPE_DAILY);
			break;
		case 1:// Weekly
			recTransaction
					.setIntervalType(ClientRecurringTransaction.INTERVAL_TYPE_WEEKLY);
			recTransaction.setWeekDay(getWeekOptions().indexOf(
					dayOfWeekCombo.getSelectedValue()) + 1);
			break;
		case 2:// monthly
			if (onSpecificDayRadioBtn.getValue()) {
				recTransaction
						.setIntervalType(ClientRecurringTransaction.INTERVAL_TYPE_MONTHLY_DAY);
				// need only date
				if (dayOfMonthCombo.getSelectedIndex() < (dayOfMonthCombo
						.getComboItems().size() - 1)) {
					recTransaction.setDayOfMonth(Integer
							.parseInt(dayOfMonthCombo.getSelectedValue()));
				} else {
					recTransaction.setDayOfMonth(-1);
				}

			} else if (onSpecificWeekRadioBtn.getValue()) {
				recTransaction
						.setIntervalType(ClientRecurringTransaction.INTERVAL_TYPE_MONTHLY_WEEK);
				// need selected nth week, and selected week
				recTransaction.setWeekDay(getWeekOptions().indexOf(
						dayOfWeekCombo.getSelectedValue()) + 1);

				recTransaction.setWeekOfMonth(getMonthWeekOptions().indexOf(
						weekOfMonthCombo.getSelectedValue()) + 1);
			}

			break;
		case 3:// yearly
			recTransaction
					.setIntervalType(ClientRecurringTransaction.INTERVAL_TYPE_YEARLY);
			if (dayOfMonthCombo.getSelectedIndex() < (dayOfMonthCombo
					.getComboItems().size() - 1)) {
				recTransaction.setDayOfMonth(Integer.parseInt(dayOfMonthCombo
						.getSelectedValue()));
			} else {
				recTransaction.setDayOfMonth(-1);
			}
			recTransaction.setMonth(getMonthOptions().indexOf(
					monthOfYearCombo.getSelectedValue()));
			break;
		default:
			break;
		}

	}

	private List<String> getActionOptions() {
		List<String> options = new ArrayList<String>();
		options.add(messages.Saveasdraft());
		options.add(messages.approve());
		options.add(messages.Approveandsend());
		return options;
	}

	private List<String> getIntervalTypeOptions() {
		List<String> options = new ArrayList<String>();
		options.add(messages.daily());
		options.add(messages.weekly());
		options.add(messages.monthly());
		options.add(messages.yearly());
		return options;
	}

	@Override
	protected ValidationResult validate() {
		if (recurringTypeCombo.getSelectedIndex() == 2) {// none-just template
			return nameForm.validate();
		}

		ValidationResult result = super.validate();
		for (DynamicForm form : dynamicForms) {
			result.add(form.validate());
		}
		String selectedValue = dayOfWeekCombo.getSelectedValue();
		if (selectedValue == null) {
			result.addError(dayOfWeekCombo, "Please select day of the month");
		}
		ClientFinanceDate startDate = startDateField.getDate();
		if (!startDate.after(new ClientFinanceDate())) {
			result.addError(startDateField,
					messages.startDateMustBeAfterToday());
		}
		int endDateType = endDateTypeCombo.getSelectedIndex();
		switch (endDateType) {
		case 1:
			String value = occurrencesField.getValue();
			if (value == null || value.isEmpty()) {
				result.addError(occurrencesField,
						messages.pleaseEnterValidOccurrencesField());
			}
		case 2:
			ClientFinanceDate endDate = endDateField.getDate();
			if (!endDate.after(new ClientFinanceDate())) {
				result.addError(endDateField,
						messages.endDateMustBeAfterToday());
			} else if (endDate.before(startDate)) {
				result.addError(endDateField,
						messages.endDateMustBeAfterStartDate());
			}
			break;
		}

		return result;
	}

	/**
	 * Interval value field. <br>
	 * <br>
	 * Shows like "every [] days/weeks/months"
	 * 
	 * @author vimukti3
	 * 
	 */
	private class IntervalValueInputField extends DynamicForm {
		private final TextItem intervalValueField;
		private final LabelItem intervalTypeLabel;

		public IntervalValueInputField() {
			super("intervalValueInputField");
			intervalValueField = new TextItem(messages.every(),
					"intervalValueField");
			intervalValueField.setRequired(true);
			intervalTypeLabel = new LabelItem(messages.intervalType(),
					"intervalTypeLabel");
			intervalTypeLabel.setShowTitle(false);
			add(intervalValueField, intervalTypeLabel);
		}

		public void setIntervalValue(int intervalValue) {
			intervalValueField.setValue(String.valueOf(intervalValue));
		}

		public int getIngervalValue() {
			try {
				String value = intervalValueField.getValue();
				return (value != null && !value.equals("")) ? Integer
						.parseInt(value) : 0;
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		public void setIntervalTypeLabel(String intervalType) {
			intervalTypeLabel.setValue(intervalType);
		}
	}

	private void changeFieldsStausForNoneSelection(boolean selected) {
		startDateField.setEnabled(!selected);
		endDateField.setEnabled(!selected);
		occurrencesField.setEnabled(!selected);
		daysInAdvanceField.setEnabled(!selected);
		intervalValueField.setEnabled(!selected);
		endDateTypeForm.setEnabled(!selected);
		actionComboField.setEnabled(!selected);
		monthOfYearCombo.setEnabled(!selected);
		intervalTypeCombo.setEnabled(!selected);
		endDateTypeCombo.setEnabled(!selected);
		onSpecificDayRadioBtn.setEnabled(!selected);
		onSpecificWeekRadioBtn.setEnabled(!selected);
		unbilledChargesCkBox.setEnabled(!selected);
		notificationForm.setEnabled(!selected);
		if (getIntervalTypeOptions().indexOf(
				intervalTypeCombo.getSelectedValue()) == 2
				&& !selected) {
			if (onSpecificDayRadioBtn.getValue()) {
				dayOfMonthCombo.setEnabled(selected);

				weekOfMonthCombo.setEnabled(selected);
				dayOfWeekCombo.setEnabled(selected);
			} else if (onSpecificWeekRadioBtn.getValue()) {
				weekOfMonthCombo.setEnabled(!selected);
				dayOfWeekCombo.setEnabled(!selected);

				dayOfMonthCombo.setEnabled(selected);
			} else {
				dayOfWeekCombo.setEnabled(!selected);
				dayOfMonthCombo.setEnabled(!selected);
				weekOfMonthCombo.setEnabled(!selected);
			}
		} else {
			dayOfWeekCombo.setEnabled(!selected);
			dayOfMonthCombo.setEnabled(!selected);
			weekOfMonthCombo.setEnabled(!selected);
		}
	}

	@Override
	public void setFocus() {
		nameField.setFocus();
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
