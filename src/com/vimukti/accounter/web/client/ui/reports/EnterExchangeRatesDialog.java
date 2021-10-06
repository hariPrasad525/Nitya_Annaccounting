package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class EnterExchangeRatesDialog extends BaseDialog {

	private CurrencyExchangeRateTable table;
	private DateItem enteredDateItem;
	private boolean fromReportsHome;

	public EnterExchangeRatesDialog(boolean fromReportsHome) {
		super(messages.enterExchangeRates());
		this.fromReportsHome = fromReportsHome;
		this.getElement().setId("EnterExchangeRatesDialog");
		createControls();
	}

	private void createControls() {
		this.enteredDateItem = new DateItem(messages.enterDate(),
				"enteredDateItem");
		enteredDateItem.setEnteredDate(new ClientFinanceDate());
		enteredDateItem.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				updateExchangeRates(date);
			}
		});
		DynamicForm dynamicForm = new DynamicForm("dynamicForm");
		dynamicForm.add(enteredDateItem);
		updateExchangeRates(enteredDateItem.getDate());
		this.table = new CurrencyExchangeRateTable();
		this.table.addStyleName("exchange-table");

		StyledPanel panel = new StyledPanel("panel");
		panel.add(enteredDateItem);
		panel.add(table);
		setBodyLayout(panel);
	}

	protected void updateExchangeRates(ClientFinanceDate date) {
		AccounterAsyncCallback<ArrayList<CurrencyExchangeRate>> callback = new AccounterAsyncCallback<ArrayList<CurrencyExchangeRate>>() {

			@Override
			public void onException(AccounterException exception) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResultSuccess(ArrayList<CurrencyExchangeRate> result) {
				if (table != null) {
					if (result == null && result.isEmpty()) {
						return;
					} else {
						table.setAllRows(result);
					}
				}
			}
		};
		Accounter.createReportService().getExchangeRatesOfDate(date.getDate(),
				callback);
	}

	@Override
	protected boolean onOK() {
		CompanyAndFinancialReportsAction action = CompanyAndFinancialReportsAction
				.unRelealisedLossAndGrains();
		List<CurrencyExchangeRate> allExchangeRates = table.getAllRows();
		Map<Long, Double> map = new HashMap<Long, Double>();
		for (CurrencyExchangeRate rate : allExchangeRates) {
			map.put(rate.getCurrencyId(), rate.getExchangeRate());
		}
		action.setEnterDate(enteredDateItem.getDate());
		action.run(map, false);
		return true;
	}

	@Override
	protected boolean onCancel() {
		if (fromReportsHome) {
			return true;
		}
		return super.onCancel();
	}

	@Override
	public void setFocus() {

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		List<CurrencyExchangeRate> allRows = table.getAllRows();
		if (allRows == null || allRows.isEmpty()) {
			result.addError(table,
					messages.youDontHaveAnyOtherCurrenciesToSeeThisReport());
		}
		return result;
	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}

}
