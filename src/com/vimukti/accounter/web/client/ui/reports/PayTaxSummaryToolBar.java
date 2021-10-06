package com.vimukti.accounter.web.client.ui.reports;

import java.util.Arrays;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientLiveTaxRate;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

public class PayTaxSummaryToolBar extends PayHeadEmployeeToolBar {
	
	SelectCombo taxTypeCombo;

	public PayTaxSummaryToolBar(AbstractReportView reportView) {
		super(reportView);
	}
	
	@Override
	protected void createControls() {
		taxTypeCombo = new SelectCombo(messages.taxType());
		taxTypeCombo.initCombo(Arrays.asList(ClientLiveTaxRate.TAX_TYPE_FEDERAL, ClientLiveTaxRate.TAX_TYPE_STATE));
		taxTypeCombo.setSelected(ClientLiveTaxRate.TAX_TYPE_FEDERAL);
		taxTypeCombo.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

			@Override
			public void selectedComboBoxItem(String selectItem) {
				changeDates(getStartDate(), getEndDate());
			}
		});
		super.createControls();
		
		String[] dateRangeArray = { messages.thisFinancialQuarter(), messages2.firstQuarter(), messages2.secondQuarter(),messages2.thirdQuarter(), messages2.fourthQuarter()};
		dateRangeItemCombo.initCombo(Arrays.asList(dateRangeArray));
		dateRangeItemCombo.setSelectedItem(0);
	}
	
	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		reportview.makeReportRequest(startDate, endDate);
	}

	@Override
	protected void addFormItems() {
		addItems(taxTypeCombo, employeeCombo, dateRangeItemCombo);
	}
	
	public String getTaxType() {
		return taxTypeCombo.getSelectedValue();
	}

}
