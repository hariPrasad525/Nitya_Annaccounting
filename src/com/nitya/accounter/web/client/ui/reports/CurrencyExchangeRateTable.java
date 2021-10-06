package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

public class CurrencyExchangeRateTable extends EditTable<CurrencyExchangeRate> {

	@Override
	protected void initColumns() {
		addColumn(new TextEditColumn<CurrencyExchangeRate>() {
			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.currency();
			}

			@Override
			protected String getValue(CurrencyExchangeRate row) {
				return row.getCurrencyName();
			}

			@Override
			protected void setValue(CurrencyExchangeRate row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getValueAsString(CurrencyExchangeRate row) {
				return messages.currency()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		});

		addColumn(new AmountColumn<CurrencyExchangeRate>(null, false) {

			@Override
			protected String getColumnName() {
				return messages.exchangeRate();
			}

			@Override
			protected Double getAmount(CurrencyExchangeRate row) {
				return row.getExchangeRate();
			}

			@Override
			protected void setAmount(CurrencyExchangeRate row, Double value) {
				row.setExchangeRate(value);
			}

			@Override
			public String getValueAsString(CurrencyExchangeRate row) {
				return messages.exchangeRate()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		});
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
