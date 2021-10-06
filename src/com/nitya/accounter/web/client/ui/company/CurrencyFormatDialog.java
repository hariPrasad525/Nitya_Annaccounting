package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.i18n.AccounterNumberFormat;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.JNSI;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.IntegerField;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class CurrencyFormatDialog extends BaseDialog {

	private static final char CURRENCY_SIGN = '\u00A4';

	private TextItem positiveTextItem, negativeTextItem, decimalSymbolItem,
			digitGroupingSymbolItem, currencySymbolItem;

	private IntegerField noOfDigitsAfterDecimalText;

	private SelectCombo positiveCurrencyFormatCombo,
			negativeCurrencyFormatCombo, digitGroupingCombo;

	private int posNum;

	private int negNum;

	private String currencySymbol;

	private String decimalSymbol;

	private String digitGroupSymbol;

	private String digitGroupingFormat = "123,456,789";

	private long noOfDigitsAfterDecimal;

	Map<String, String> groups = new HashMap<String, String>();

	private ClientCompanyPreferences preferences;

	String[] posForValue = { "S1D1", "1D1S", "S 1D1", "1D1 S" };
	String[] negForValue = { "(S1D1)", "-S1D1", "S-1D1", "S1D1-", "(1D1S)",
			"-1D1S", "1D1-S", "1D1S-", "1D1S-", "-S 1D1", "1D1 S-", "S 1D1-",
			"S -1D1", "1D1- S", "(S 1D1)", "(1D1 S)" };

	public CurrencyFormatDialog(String string) {
		super(string);
		this.getElement().setId("CurrencyFormatDialog");
		this.preferences = Accounter.getCompany().getPreferences();
		assignGroupValues();
		createControl();
		initData(preferences);
	}

	private void createControl() {

		VerticalPanel allPanels = new VerticalPanel();
		allPanels.setWidth("100%");

		CaptionPanel panel = new CaptionPanel(messages.exampleFormat());
		DOM.setStyleAttribute(panel.getElement(), "border", "1px solid #ccc");

		Label example = new Label(messages.currencyFormat());
		example.setStyleName("currency_format_label");

		DynamicForm formatForm = new DynamicForm("formatForm");

		positiveTextItem = new TextItem(messages.positive(),"positiveTextItem");
		positiveTextItem.setEnabled(!true);
		negativeTextItem = new TextItem(messages.negative(),"negativeTextItem");
		negativeTextItem.setEnabled(!true);

		formatForm.add(positiveTextItem, negativeTextItem);

		DynamicForm form = new DynamicForm("form");
		form.addStyleName("currency_format_align");

		currencySymbolItem = new TextItem(messages.currencySymbol(),"currencySymbolItem");
		currencySymbolItem.setValue(getCompanyPreferences()
				.getPrimaryCurrency().getSymbol());
		currencySymbolItem.setEnabled(!true);
		positiveCurrencyFormatCombo = new SelectCombo(
				messages.postiveCurrencyFormat(), false);
		positiveCurrencyFormatCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						posNum = positiveCurrencyFormatCombo.getSelectedIndex();
						update();
					}
				});

		negativeCurrencyFormatCombo = new SelectCombo(
				messages.negativeCurrencyFormat(), false);
		negativeCurrencyFormatCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						negNum = negativeCurrencyFormatCombo.getSelectedIndex();
						update();
					}
				});

		decimalSymbolItem = new TextItem(messages.decimalSymbol(),"decimalSymbolItem");
		decimalSymbolItem.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				String value = decimalSymbolItem.getValue();
				String readNumber = JNSI.readNumber(value);
				if (readNumber == null || readNumber.equals("null")
						|| readNumber.isEmpty()) {
					decimalSymbol = value;
					initPositiveFormatValues();
					initNegativeFormatValues();
					update();
				} else {
					decimalSymbolItem.setValue(decimalSymbol);
					Accounter.showError(messages
							.decimalSymbolShouldNotContainNumericalValues());
				}
			}
		});
		noOfDigitsAfterDecimalText = new IntegerField(this,
				messages.noOfDigitsAfterDecimal());
		noOfDigitsAfterDecimalText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				noOfDigitsAfterDecimal = noOfDigitsAfterDecimalText.getNumber();
				update();
			}
		});

		digitGroupingSymbolItem = new TextItem(messages.digitGroupingDecimal(),"digitGroupingSymbolItem");
		digitGroupingSymbolItem.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				String value = digitGroupingSymbolItem.getValue();
				String readNumber = JNSI.readNumber(value);
				if (readNumber == null || readNumber.equals("null")
						|| readNumber.isEmpty()) {
					if (!digitGroupSymbol.isEmpty()) {
						digitGroupingFormat = digitGroupingFormat.replaceAll(
								digitGroupSymbol, value);
					}
					digitGroupSymbol = value;
					initDigitGroupingValues();
					update();
				} else {
					digitGroupingSymbolItem.setValue(digitGroupSymbol);
					Accounter.showError(messages
							.digitGroupingSymbolShouldNotContainNumericalValues());
				}
			}
		});
		digitGroupingCombo = new SelectCombo(messages.digitGrouping(), false);
		digitGroupingCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						digitGroupingFormat = selectItem;
						update();
					}
				});
		digitGroupingCombo.setRequired(true);
		form.add(currencySymbolItem, positiveCurrencyFormatCombo,
				negativeCurrencyFormatCombo, noOfDigitsAfterDecimalText,
				decimalSymbolItem, digitGroupingSymbolItem, digitGroupingCombo);

		panel.setContentWidget(formatForm);

		allPanels.add(panel);
		allPanels.add(form);
		bodyLayout.add(allPanels);

	}

	public void initData(ClientCompanyPreferences preferences) {
		currencySymbol = preferences.getPrimaryCurrency().getSymbol();
		decimalSymbol = preferences.getDecimalCharacter();
		noOfDigitsAfterDecimal = preferences.getDecimalNumber();
		if (decimalSymbol != null) {
			decimalSymbolItem.setValue(decimalSymbol);
		}

		noOfDigitsAfterDecimalText.setValue(String
				.valueOf(noOfDigitsAfterDecimal));

		digitGroupSymbol = preferences.getDigitGroupCharacter();

		if (digitGroupSymbol != null) {
			digitGroupingSymbolItem.setValue(digitGroupSymbol);
		}

		String currencyFormat = preferences.getCurrencyFormat();
		String[] split = currencyFormat.split(";");
		String positive = split[0];// ¤#,##0.00

		String pv = positive.replaceAll(String.valueOf(CURRENCY_SIGN), "");// ¤#,##
		pv = pv.replaceAll(" ", "");// #,##0.00

		String pos = positive.replace(pv, "1D1");// ¤1.1
		pos = pos.replace(CURRENCY_SIGN, 'S');
		posNum = getIndex(posForValue, pos);

		String negative = split[1];// (¤#,##0.00)
		String neg = negative.replace(pv, "1D1");// (¤1.1)
		neg = neg.replace(CURRENCY_SIGN, 'S');
		negNum = getIndex(negForValue, neg);

		for (String key : groups.keySet()) {
			if (groups.get(key).equals(preferences.getDigitGroupingFormat())) {
				digitGroupingFormat = key;
			}
		}

		digitGroupingFormat = digitGroupingFormat.replaceAll(",",
				digitGroupSymbol);
		initPositiveFormatValues();
		initNegativeFormatValues();
		initDigitGroupingValues();
		update();
	}

	private String getDecimalString() {
		String value = "0.";
		for (int i = 0; i < noOfDigitsAfterDecimal; i++) {
			value += "0";
		}
		return value;
	}

	protected void initDigitGroupingValues() {
		List<String> values = new ArrayList<String>();
		ArrayList<String> list = new ArrayList<String>(groups.keySet());
		for (int i = 0; i < list.size(); i++) {
			String value = list.get(i);
			value = value.replaceAll(",",
					AccounterNumberFormat.quoteReplacement(digitGroupSymbol));
			values.add(value);
		}
		digitGroupingCombo.initCombo(values);
		digitGroupingCombo.setComboItem(digitGroupingFormat);
	}

	protected void update() {
		String value = digitGroupingCombo.getSelectedValue();
		value += decimalSymbol;
		for (int i = 0; i < noOfDigitsAfterDecimal; i++) {
			value += "0";
		}

		if (noOfDigitsAfterDecimal == 0) {
			decimalSymbolItem.setEnabled(!true);
			decimalSymbolItem.setValue("");
			decimalSymbol = "";
		} else {
			decimalSymbolItem.setEnabled(!false);
		}

		String pValue = posForValue[posNum].replaceAll("S",
				AccounterNumberFormat.quoteReplacement(currencySymbol))
				.replaceAll("1D1", value);

		String nValue = negForValue[negNum].replaceAll("S",
				AccounterNumberFormat.quoteReplacement(currencySymbol))
				.replaceAll("1D1", (value));

		positiveTextItem.setValue(pValue);
		negativeTextItem.setValue(nValue);
	}

	protected void initNegativeFormatValues() {
		List<String> vals = getValues(negForValue);
		negativeCurrencyFormatCombo.initCombo(vals);
		negativeCurrencyFormatCombo.setSelectedItem(negNum);
	}

	protected void initPositiveFormatValues() {
		List<String> vals = getValues(posForValue);
		positiveCurrencyFormatCombo.initCombo(vals);
		positiveCurrencyFormatCombo.setSelectedItem(posNum);
	}

	private List<String> getValues(String[] rawData) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < rawData.length; i++) {
			String value = rawData[i];
			value = value.replaceAll("S",
					AccounterNumberFormat.quoteReplacement(currencySymbol));
			value = value.replaceAll("D",
					AccounterNumberFormat.quoteReplacement(decimalSymbol));
			list.add(value);
		}
		return list;
	}

	private ClientCompanyPreferences getCompanyPreferences() {
		return getCompany().getPreferences();
	}

	private int getIndex(String[] values, String pos) {
		for (int i = 0; i < values.length; i++) {
			if (values[i].endsWith(pos)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	protected boolean onOK() {
		ClientCompanyPreferences companyPreferences = getCompanyPreferences();
		companyPreferences.setDecimalNumber((int) noOfDigitsAfterDecimal);
		companyPreferences.setDecimalCharacte(decimalSymbol);
		companyPreferences.setDigitGroupCharacter(digitGroupSymbol);
		String format = digitGroupingFormat;
		if (digitGroupSymbol != null && !digitGroupSymbol.isEmpty()) {
			format = digitGroupingFormat.replaceAll(digitGroupSymbol, ",");
		}
		companyPreferences.setDigitGroupingFormat(groups.get(format));

		String value = groups.get(format);
		// String value = "#,##";

		value += getDecimalString();

		String pValue = posForValue[posNum].replaceAll("S",
				String.valueOf(CURRENCY_SIGN)).replaceAll("1D1", value);

		String nValue = negForValue[negNum].replaceAll("S",
				String.valueOf(CURRENCY_SIGN)).replaceAll("1D1", value);

		String currencyPattern = pValue + ";" + nValue;
		companyPreferences.setCurrencyFormat(currencyPattern);// @#,##0.00 S1D1
		Global.get().getFormater().setCurrencyFormat(companyPreferences);
		this.removeFromParent();
		return true;
	}

	@Override
	protected boolean onCancel() {
		this.removeFromParent();
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void assignGroupValues() {
		groups.put("123456789", "##");
		groups.put("123,456,789", "###,###,##");
		groups.put("123456,789", "######,##");
		groups.put("12,34,56,789", "##,##,##,##");
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (noOfDigitsAfterDecimal > 0
				&& (decimalSymbol == null || decimalSymbol.isEmpty())) {
			result.addError(decimalSymbolItem,
					messages.pleaseEnterDecimalSymbol());
		}
		return result;
	}

}
