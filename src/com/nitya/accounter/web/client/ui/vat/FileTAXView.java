package com.nitya.accounter.web.client.ui.vat;

import java.util.ArrayList;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.ClientTAXReturnEntry;
import com.nitya.accounter.web.client.core.NumberReportInput;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.grids.FileTAXGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.TAXReportsAction;
import com.nitya.accounter.web.client.ui.reports.TaxItemDetailReportView;

public class FileTAXView extends AbstractFileTAXView {

	private FileTAXGrid grid;

	@Override
	protected void reloadGrid() {

		if (selectedTaxAgency == null) {
			Accounter.showError(messages.pleaseSelect(messages.taxAgency()));
			return;
		}
		if (fromDate.getDate() == null || fromDate.getDate().getDate() == 0) {
			Accounter.showError(messages.pleaseSelect(messages.fromDate()));
			return;
		}
		if (toDate.getDate() == null || toDate.getDate().getDate() == 0) {
			Accounter.showError(messages.pleaseSelect(messages.toDate()));
			return;
		}
		canSaveFileVat = true;
		grid.removeAllRecords();
		grid.addLoadingImagePanel();

		rpcGetService.getTAXReturnEntries(selectedTaxAgency.getID(), fromDate
				.getDate().getDate(), toDate.getDate().getDate(),
				new AccounterAsyncCallback<ArrayList<ClientTAXReturnEntry>>() {

					@Override
					public void onException(AccounterException exception) {
						String errorString = AccounterExceptions
								.getErrorString(exception.getErrorCode());
						Accounter.showError(errorString);
						disableprintButton();
					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientTAXReturnEntry> result) {
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.setRecords(result);
							enableprintButton();
						} else {
							grid.addEmptyMessage(messages.selectTAXAgency());
						}
					}

				});
	}

	@Override
	protected void printTaxReturn() {
		TaxItemDetailReportView report = new TaxItemDetailReportView() {
			@Override
			public void export(int generationType) {
				UIUtils.generateReport(generationType, fromDate
						.getEnteredDate().getDate(), toDate.getEnteredDate()
						.getDate(), 165, new NumberReportInput((long) 0),
						new NumberReportInput(taxAgencyCombo.getSelectedValue()
								.getID()));
			}
		};
		report.setAction(TAXReportsAction.taxItemDetail());
		report.init();
		report.initData();
		report.print();
	}

	@Override
	protected ListGrid getGrid() {
		if (grid == null) {
			this.grid = new FileTAXGrid(false);
		}
		return grid;
	}

	@Override
	public ClientTAXReturn saveView() {
		updateTransaction();
		return super.saveView();
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(getData());
	}

	@Override
	public void restoreView(ClientTAXReturn viewDate) {
		if (viewDate != null) {
			ClientTAXAgency taxAgency = getCompany().getTaxAgency(
					viewDate.getTaxAgency());
			taxAgencyCombo.setComboItem(taxAgency);
			selectedTaxAgency = taxAgency;
			fromDate.setDateWithNoEvent(new ClientFinanceDate(viewDate
					.getPeriodStartDate()));
			toDate.setDateWithNoEvent(viewDate.getPeriodEndDate() == 0 ? new ClientFinanceDate()
					: new ClientFinanceDate(viewDate.getPeriodEndDate()));
			if (selectedTaxAgency != null) {
				reloadGrid();
			}
		}
		super.restoreView(viewDate);
	}

	private void updateTransaction() {
		ClientTAXReturn taxReturn = new ClientTAXReturn();
		if (selectedTaxAgency != null) {
			taxReturn.setTAXAgency(selectedTaxAgency.getID());
		}
		taxReturn.setTaxReturnEntries(grid.getRecords());
		taxReturn.setTransactionDate(new ClientFinanceDate().getDate());
		taxReturn.setPeriodStartDate(fromDate.getDate().getDate());
		taxReturn.setPeriodEndDate(toDate.getDate().getDate());
		setData(taxReturn);
	}
}
