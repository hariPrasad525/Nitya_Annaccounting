package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.nitya.accounter.web.client.core.ClientBudgetItem;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

public class AddBudgetAmountDialogue extends BaseDialog {

	private static final String MONTHS = messages.months();
	private static final String QUARTERS = messages.Quarters();
	private static final String YEARS = messages.years();

	private static final String NEAREST_AMOUNT = messages.NearestAmount();
	private static final String NEAREST_TEN = messages.NearestTen();
	private static final String NEAREST_HUNDRED = messages.NearestHundred();
	private static final String NONE = messages.none();
	protected static final int CALANDERTYPE_DAYS = 1;
	protected static final int CALANDERTYPE_MONTHS = 2;

	AmountField janAmount, febAmount, marAmount, aprAmount, mayAmount,
			junAmount, julAmount, augAmount, septAmount, octAmount, novAmount,
			decAmount, quater1Amount, quater2Amount, quater3Amount,
			quater4Amount, annualAmount;

	HashMap<String, String> newMap;
	int type = 0;

	DynamicForm budgetInfoForm;
	DynamicForm budgetAddForm;
	SelectCombo budgetAddBy;
	ClientBudgetItem defaultValues = new ClientBudgetItem();
	private DisclosurePanel discloserPanel;
	private SelectCombo budgetRoundOfMethod;

	int calendarType = 2;
	int roundOff = 0;

	public AddBudgetAmountDialogue(String title, String desc,
			HashMap<String, String> map, ClientBudgetItem budgetItem) {
		super(title, desc);
		this.getElement().setId("AddBudgetAmountDialogue");
		newMap = new HashMap<String, String>();
		defaultValues = budgetItem;
		map = newMap;
		createControls();
	}

	public AddBudgetAmountDialogue(String budgetTitle, String desc,
			ArrayList<Object> mapList, ClientBudgetItem object) {

		super(budgetTitle, desc);
		this.getElement().setId("AddBudgetAmountDialogue");
		newMap = new HashMap<String, String>();
		defaultValues = object;
		newMap = (HashMap<String, String>) mapList.get(0);
		type = (Integer) mapList.get(1);
		createControls();

	}

	private void createControls() {
		StyledPanel verticalPanel = new StyledPanel("verticalPanel");

		budgetAddBy = new SelectCombo(messages.budgetAddBy());
		budgetAddBy.initCombo(getStartWithList());
		budgetAddBy.setSelectedItem(type);
		budgetAddBy
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem == MONTHS) {
							type = 0;
							resetView(type);

						} else if (selectItem == QUARTERS) {
							type = 1;
							resetView(type);

						} else if (selectItem == YEARS) {
							type = 2;
							resetView(type);

						}
					}

				});

		janAmount = new AmountField(DayAndMonthUtil.january(), this);
		janAmount.setRequired(false);
		// janAmount.setWidth(100);

		febAmount = new AmountField(DayAndMonthUtil.february(), this);
		febAmount.setRequired(false);
		// febAmount.setWidth(100);

		marAmount = new AmountField(DayAndMonthUtil.march(), this);
		marAmount.setRequired(false);
		// marAmount.setWidth(100);

		aprAmount = new AmountField(DayAndMonthUtil.april(), this);
		aprAmount.setRequired(false);
		// aprAmount.setWidth(100);

		mayAmount = new AmountField(DayAndMonthUtil.may_full(), this);
		mayAmount.setRequired(false);
		// mayAmount.setWidth(100);

		junAmount = new AmountField(DayAndMonthUtil.june(), this);
		junAmount.setRequired(false);
		// junAmount.setWidth(100);

		julAmount = new AmountField(DayAndMonthUtil.july(), this);
		julAmount.setRequired(false);
		// julAmount.setWidth(100);

		augAmount = new AmountField(DayAndMonthUtil.august(), this);
		augAmount.setRequired(false);
		// augAmount.setWidth(100);

		septAmount = new AmountField(DayAndMonthUtil.september(), this);
		septAmount.setRequired(false);
		// septAmount.setWidth(100);

		octAmount = new AmountField(DayAndMonthUtil.october(), this);
		octAmount.setRequired(false);
		// octAmount.setWidth(100);

		novAmount = new AmountField(DayAndMonthUtil.november(), this);
		novAmount.setRequired(false);
		// novAmount.setWidth(100);/

		decAmount = new AmountField(DayAndMonthUtil.december(), this);
		decAmount.setRequired(false);
		// decAmount.setWidth(100);

		annualAmount = new AmountField(messages.annual(), this);
		annualAmount.setRequired(false);
		// annualAmount.setWidth(100);

		budgetAddForm = UIUtils.form(messages.chartOfAccountsInformation());
		// budgetAddForm.setWidth("100%");/
		budgetAddForm.add(janAmount, febAmount, marAmount, aprAmount,
				mayAmount, junAmount, julAmount, augAmount, septAmount,
				octAmount, novAmount, decAmount);

		budgetInfoForm = UIUtils.form(messages.chartOfAccountsInformation());
		// budgetInfoForm.setWidth("100%");

		budgetInfoForm.add(budgetAddBy);

		budgetRoundOfMethod = new SelectCombo(messages.RoundOffBudgetAmountto());
		budgetRoundOfMethod.initCombo(getBudgetCalculationTypesList());
		budgetRoundOfMethod.setSelected(NONE);
		budgetRoundOfMethod
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(NEAREST_AMOUNT)) {
							roundOff = 1;
						} else if (selectItem.equals(NEAREST_TEN)) {
							roundOff = 2;
						} else if (selectItem.equals(NEAREST_HUNDRED)) {
							roundOff = 3;
						} else if (selectItem.equals(NONE)) {
							roundOff = 0;
						}
					}

				});

		DynamicForm advanceCalculationVPanel = new DynamicForm(
				"advanceCalculationVPanel");
		advanceCalculationVPanel.add(budgetRoundOfMethod);

		StyledPanel vPanel = new StyledPanel("vPanel");
		vPanel.add(advanceCalculationVPanel);

		final String[] stringArray = {
				messages.CalendarDaysAnnualAmountNoofDaysinMonth365or366(),
				messages.CalendarMonthAnnualAmount12() };

		for (int i = 0; i < stringArray.length; i++) {
			String sport = stringArray[i];
			final RadioButton radioButton = new RadioButton(messages.sport(),
					sport);
			radioButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (radioButton == event.getSource()) {
						System.out.println(radioButton.getText());
						if ((radioButton.getText().toString())
								.endsWith(stringArray[0]))
							calendarType = CALANDERTYPE_DAYS;
						else if ((radioButton.getText().toString())
								.endsWith(stringArray[1]))
							calendarType = CALANDERTYPE_MONTHS;
					}
				}
			});
			if (i == 1) {
				radioButton.setValue(true);
			}
			vPanel.add(radioButton);
		}

		discloserPanel = new DisclosurePanel(
				messages.AdvanceBudgetCalculation());
		discloserPanel.setContent(vPanel);

		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		horizontalPanel.add(budgetInfoForm);

		verticalPanel.add(horizontalPanel);
		verticalPanel.add(budgetAddForm);
		verticalPanel.add(discloserPanel);

		setBodyLayout(verticalPanel);
		setDefaultValues();
		resetView(type);

	}

	private List<String> getStartWithList() {
		List<String> list = new ArrayList<String>();

		list.add(MONTHS);
		list.add(QUARTERS);
		list.add(YEARS);

		return list;
	}

	private List<String> getBudgetCalculationTypesList() {
		List<String> list = new ArrayList<String>();

		list.add(NEAREST_AMOUNT);
		list.add(NEAREST_TEN);
		list.add(NEAREST_HUNDRED);
		list.add(NONE);

		return list;
	}

	private void resetView(int type2) {
		if (type2 == 0) {

			budgetAddForm.clear();
			budgetAddForm.add(janAmount, febAmount, marAmount, aprAmount,
					mayAmount, junAmount, julAmount, augAmount, septAmount,
					octAmount, novAmount, decAmount);

		} else if (type2 == 1) {

			quater1Amount = new AmountField(messages.quarterPeriod("1",
					DayAndMonthUtil.jan(), DayAndMonthUtil.apr()), this);
			quater1Amount.setRequired(false);
			// quater1Amount.setWidth(100);
			quater1Amount.setAmount(janAmount.getAmount()
					+ febAmount.getAmount() + marAmount.getAmount());

			quater2Amount = new AmountField(messages.quarterPeriod("2",
					DayAndMonthUtil.apr(), DayAndMonthUtil.mayS()), this);
			quater2Amount.setRequired(false);
			// quater2Amount.setWidth(100);
			quater2Amount.setAmount(aprAmount.getAmount()
					+ mayAmount.getAmount() + junAmount.getAmount());

			quater3Amount = new AmountField(messages.quarterPeriod("3",
					DayAndMonthUtil.jun(), DayAndMonthUtil.sep()), this);
			quater3Amount.setRequired(false);
			// quater3Amount.setWidth(100);
			quater3Amount.setAmount(julAmount.getAmount()
					+ augAmount.getAmount() + septAmount.getAmount());

			quater4Amount = new AmountField(messages.quarterPeriod("4",
					DayAndMonthUtil.oct(), DayAndMonthUtil.dec()), this);
			quater4Amount.setRequired(false);
			// quater4Amount.setWidth(100);
			quater4Amount.setAmount(octAmount.getAmount()
					+ novAmount.getAmount() + decAmount.getAmount());

			budgetAddForm.clear();
			budgetAddForm.add(quater1Amount, quater2Amount, quater3Amount,
					quater4Amount);

		} else if (type2 == 2) {
			annualAmount.setAmount(annualAmount.getAmount());
			budgetAddForm.clear();
			budgetAddForm.add(annualAmount);
		}

	}

	@Override
	protected boolean onOK() {

		if (budgetAddBy.getSelectedValue() == MONTHS) {

			newMap.put(DayAndMonthUtil.jan(),
					janAmount.getAmount() != null ? janAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.feb(),
					febAmount.getAmount() != null ? febAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.mar(),
					marAmount.getAmount() != null ? marAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.apr(),
					aprAmount.getAmount() != null ? aprAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.mayS(),
					mayAmount.getAmount() != null ? mayAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.jun(),
					junAmount.getAmount() != null ? junAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.jul(),
					julAmount.getAmount() != null ? julAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.aug(),
					augAmount.getAmount() != null ? augAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.sep(),
					septAmount.getAmount() != null ? septAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.oct(),
					octAmount.getAmount() != null ? octAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.nov(),
					novAmount.getAmount() != null ? novAmount.getAmount()
							.toString() : "0");

			newMap.put(DayAndMonthUtil.dec(),
					decAmount.getAmount() != null ? decAmount.getAmount()
							.toString() : "0");

			double total = janAmount.getAmount() + febAmount.getAmount()
					+ marAmount.getAmount() + aprAmount.getAmount()
					+ mayAmount.getAmount() + junAmount.getAmount()
					+ julAmount.getAmount() + augAmount.getAmount()
					+ septAmount.getAmount() + octAmount.getAmount()
					+ novAmount.getAmount() + decAmount.getAmount();

			newMap.put(messages.total(), Double.toString(total));

		} else if (budgetAddBy.getSelectedValue() == QUARTERS) {

			String one, two, three, four;

			if (quater1Amount.getAmount() != null)
				one = Double.toString(round(0,
						quater1Amount.getAmount() / 3.00, 2));
			else
				one = "0.00";

			if (quater2Amount.getAmount() != null)
				two = Double.toString(round(0,
						quater2Amount.getAmount() / 3.00, 2));
			else
				two = "0.00";

			if (quater3Amount.getAmount() != null)
				three = Double.toString(round(0,
						quater3Amount.getAmount() / 3.00, 2));
			else
				three = "0.00";

			if (quater4Amount.getAmount() != null)
				four = Double.toString(round(0,
						quater4Amount.getAmount() / 3.00, 2));
			else
				four = "0.00";

			newMap.put(DayAndMonthUtil.jan(), one);

			newMap.put(DayAndMonthUtil.feb(), one);

			newMap.put(DayAndMonthUtil.mar(), one);

			newMap.put(DayAndMonthUtil.apr(), two);

			newMap.put(DayAndMonthUtil.mayS(), two);

			newMap.put(DayAndMonthUtil.jun(), two);

			newMap.put(DayAndMonthUtil.jul(), three);

			newMap.put(DayAndMonthUtil.aug(), three);

			newMap.put(DayAndMonthUtil.sep(), three);

			newMap.put(DayAndMonthUtil.oct(), four);

			newMap.put(DayAndMonthUtil.nov(), four);

			newMap.put(DayAndMonthUtil.dec(), four);

			double total = quater1Amount.getAmount()
					+ quater2Amount.getAmount() + quater3Amount.getAmount()
					+ quater4Amount.getAmount();

			newMap.put(messages.total(), Double.toString(total));

		} else if (budgetAddBy.getSelectedValue() == YEARS) {

			if (annualAmount.getAmount() != null) {

				newMap.put(DayAndMonthUtil.jan(), Double
						.toString(getAmountByYear(calendarType, 31,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.feb(), Double
						.toString(getAmountByYear(calendarType, 28,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.mar(), Double
						.toString(getAmountByYear(calendarType, 31,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.apr(), Double
						.toString(getAmountByYear(calendarType, 30,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.mayS(), Double
						.toString(getAmountByYear(calendarType, 31,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.jun(), Double
						.toString(getAmountByYear(calendarType, 30,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.jul(), Double
						.toString(getAmountByYear(calendarType, 31,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.aug(), Double
						.toString(getAmountByYear(calendarType, 31,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.sep(), Double
						.toString(getAmountByYear(calendarType, 30,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.oct(), Double
						.toString(getAmountByYear(calendarType, 31,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.nov(), Double
						.toString(getAmountByYear(calendarType, 30,
								annualAmount.getAmount())));

				newMap.put(DayAndMonthUtil.dec(), Double
						.toString(getAmountByYear(calendarType, 31,
								annualAmount.getAmount())));
			} else {
				String one;
				one = "0.00";

				newMap.put(DayAndMonthUtil.jan(), one);

				newMap.put(DayAndMonthUtil.feb(), one);

				newMap.put(DayAndMonthUtil.mar(), one);

				newMap.put(DayAndMonthUtil.apr(), one);

				newMap.put(DayAndMonthUtil.mayS(), one);

				newMap.put(DayAndMonthUtil.jun(), one);

				newMap.put(DayAndMonthUtil.jul(), one);

				newMap.put(DayAndMonthUtil.aug(), one);

				newMap.put(DayAndMonthUtil.sep(), one);

				newMap.put(DayAndMonthUtil.oct(), one);

				newMap.put(DayAndMonthUtil.nov(), one);

				newMap.put(DayAndMonthUtil.dec(), one);
			}

			double total = annualAmount.getAmount();

			newMap.put(messages.total(), Double.toString(total));
		}

		ArrayList<Object> mapList = new ArrayList<Object>();
		mapList.add(newMap);
		mapList.add(type);

		getCallback().actionResult(mapList);
		return true;
	}

	private double getAmountByYear(int calendarType, int NoOfDays, double amount) {
		double result;
		if (calendarType == 1) {
			result = (amount * NoOfDays) / 365;
		} else {
			result = amount / 12;
		}
		double value = calculateValue(result, NoOfDays);
		return round(31, value, 2);

	}

	public void setDefaultValues() {

		janAmount.setAmount(defaultValues.getJanuaryAmount());
		febAmount.setAmount(defaultValues.getFebruaryAmount());
		marAmount.setAmount(defaultValues.getMarchAmount());
		aprAmount.setAmount(defaultValues.getAprilAmount());
		mayAmount.setAmount(defaultValues.getMayAmount());
		junAmount.setAmount(defaultValues.getJuneAmount());
		julAmount.setAmount(defaultValues.getJulyAmount());
		augAmount.setAmount(defaultValues.getAugustAmount());
		septAmount.setAmount(defaultValues.getSpetemberAmount());
		octAmount.setAmount(defaultValues.getOctoberAmount());
		novAmount.setAmount(defaultValues.getNovemberAmount());
		decAmount.setAmount(defaultValues.getDecemberAmount());
		annualAmount.setAmount(defaultValues.getTotalAmount());
	}

	@Override
	public void setFocus() {
		budgetAddBy.setFocus();

	}

	public double round(int month, double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);

		double valueFinal = tmp / factor;

		double valueToReturn = calculateValue(valueFinal, month);

		return valueToReturn;
	}

	private double calculateValue(double valueFinal, int monthDays) {

		long round2 = Math.round(valueFinal);

		if (roundOff == 1) {
			valueFinal = round2;
		} else if (roundOff == 2) {
			round2 = (round2 / 10);
			round2 = round2 * 10;
			valueFinal = round2;
		} else if (roundOff == 3) {
			round2 = (round2 / 100);
			round2 = round2 * 100;
			valueFinal = round2;
		}

		return valueFinal;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
