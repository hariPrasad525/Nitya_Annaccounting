package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientBox;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.VATBoxGrid;
import com.vimukti.accounter.web.client.ui.reports.AbstractReportView;
import com.vimukti.accounter.web.client.ui.reports.VAT100Report;
import com.vimukti.accounter.web.client.ui.reports.VAT100ReportAction;

public class FileVATView extends AbstractFileTAXView {

	private VATBoxGrid gridView;

	public FileVATView() {
		super();
	}

	protected ListGrid getGrid() {
		if (gridView == null) {
			gridView = new VATBoxGrid(false);
			// gridView.setCurrentView(this);
			// gridView.setCanEdit(true);
			// gridView.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
			gridView.isEnable = false;
			gridView.init();
			// gridView.setHeight("250px");
			gridView.addStyleName("file-vat");
			gridView.addEmptyMessage(messages.selectVatAgency());
		}
		return gridView;
	}

	@Override
	public ClientTAXReturn saveView() {
		ClientTAXReturn saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	private void updateTransaction() {
		if (this.selectedTaxAgency != null && !isInViewMode()) {
			data.setTransactionDate(new ClientFinanceDate().getDate());
		}

		ClientTAXReturn vatReturn = (ClientTAXReturn) data;

		double salesTaxamount = 0, purchaseTaxAmount = 0;
		for (ClientBox b : vatReturn.getBoxes()) {
			if (b.getBoxNumber() == 1 || b.getBoxNumber() == 2) {
				salesTaxamount = salesTaxamount + b.getAmount();
			} else if (b.getBoxNumber() == 4) {
				purchaseTaxAmount = purchaseTaxAmount + b.getAmount();
			} else if (b.getBoxNumber() == 10) {
				purchaseTaxAmount = purchaseTaxAmount - b.getAmount();
			}
		}

		double amount = vatReturn.getBoxes().get(4).getAmount()
				+ vatReturn.getBoxes().get(vatReturn.getBoxes().size() - 1)
						.getAmount();

		vatReturn.setSalesTaxTotal(vatReturn.getBoxes().get(2).getAmount());
		vatReturn.setPurchaseTaxTotal(vatReturn.getBoxes().get(3).getAmount());
		vatReturn.setTotalTAXAmount(amount);
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(data);
		// else {
		//
		// UIUtils.err(FinanceApplication.constants()
		// .pleaseSelectValidVATAgency());
		// return;
		// }

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		// Accounter.showInformation(FinanceApplication.constants()
		// .fileVATCreated());
		super.saveSuccess(result);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String errorString = AccounterExceptions.getErrorString(exception
				.getErrorCode());
		Accounter.showError(errorString);
	}

	@Override
	protected String getViewTitle() {
		return messages.fileTAX();
	}

	@Override
	protected void reloadGrid() {
		canSaveFileVat = true;
		gridView.removeAllRecords();
		ClientFinanceDate startDate = fromDate.getDate();
		ClientFinanceDate endDate = toDate.getDate();
		if (this.selectedTaxAgency == null || startDate.getDate() == 0
				|| endDate.getDate() == 0) {
			gridView.removeLoadingImage();
			gridView.addEmptyMessage(messages.selectVatAgency());
			disableprintButton();
			return;
		}
		gridView.addLoadingImagePanel();

		this.rpcUtilService.getVATReturn(this.selectedTaxAgency,
				fromDate.getDate(), toDate.getDate(),
				new AccounterAsyncCallback<ClientTAXReturn>() {

					@Override
					public void onException(AccounterException caught) {
						// gridView.clear();
						gridView.addEmptyMessage(messages
								.norecordstoshowinbetweentheselecteddates());
						// UIUtils.err(FinanceApplication.constants()
						// .failedToRetrieveVatBoxesForVATAgency()
						// + FileVATView.this.selectedVatAgency.getName());
						disableprintButton();
					}

					@Override
					public void onResultSuccess(ClientTAXReturn result) {
						gridView.removeLoadingImage();
						setData(result);

						getData().setTAXAgency(
								FileVATView.this.selectedTaxAgency.getID());

						gridView.clear();

						double box3Amt = 0.0;
						double box5Amt = 0.0;
						double box4Amt = 0.0;
						for (ClientBox box : result.getBoxes()) {

							if (box.getBoxNumber() == 1
									|| box.getBoxNumber() == 2) {
								box3Amt = box3Amt + box.getAmount();
							}
							if (box.getBoxNumber() == 3) {
								box.setAmount(box3Amt);
							}
							if (box.getBoxNumber() == 4) {
								box4Amt = box4Amt + box.getAmount();
							}
							if (box.getBoxNumber() == 4) {
								box.setAmount(box4Amt);
							}
							if (box.getBoxNumber() == 4) {
								box5Amt = box3Amt - box.getAmount();
							}

							if (box.getBoxNumber() == 5) {
								box.setAmount(box5Amt);
								// gridView.addFooterValue("You Owe VAT of  " +
								// box5Amt
								// + " to  " + selectedVatAgency.getName(), 1);
							}
							gridView.addData(box);
						}
						enableprintButton();

					}
				});

	}

	@Override
	protected void printTaxReturn() {
		AbstractReportView<VATSummary> report = new VAT100Report() {
			private boolean isSecondReuqest = false;

			@Override
			public void onSuccess(ArrayList<VATSummary> result) {
				super.onSuccess(result);
				print();
			}

			@Override
			public void makeReportRequest(long vatAgency,
					ClientFinanceDate startDate, ClientFinanceDate endDate) {
				if (isSecondReuqest) {
					this.startDate = startDate;
					this.endDate = endDate;
					super.makeReportRequest(vatAgency, startDate, endDate);
				} else {
					isSecondReuqest = true;
				}
			}
		};
		report.setAction(new VAT100ReportAction());
		report.init();
		report.initData();
		report.makeReportRequest(selectedTaxAgency.getID(),
				fromDate.getEnteredDate(), toDate.getEnteredDate());

	}

}
