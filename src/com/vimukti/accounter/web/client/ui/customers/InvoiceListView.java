package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BrandingThemeComboAction;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.grids.InvoiceListGrid;

public class InvoiceListView extends TransactionsListView<InvoicesList>
		implements IPrintableView {

	private List<InvoicesList> listOfInvoices;

	// private static String DELETE = "Deleted";
	private ClientBrandingTheme brandingTheme;

	private int transactionType;
	public int viewId;

	public InvoiceListView() {
		super(messages.open());
		isDeleteDisable = true;
		// getLastandOpenedFiscalYearEndDate();
	}

	public InvoiceListView(String viewType) {
		super(viewType);
		isDeleteDisable = true;
		// getLastandOpenedFiscalYearEndDate();
	}

	public InvoiceListView(int typeInvoicesOnly) {
		super(messages.open());
		isDeleteDisable = true;
		this.transactionType = typeInvoicesOnly;

		if (transactionType == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO
				|| transactionType == ClientTransaction.TYPE_CASH_SALES) {
			setViewType(messages.all());
		}
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (transactionType == 0
					|| transactionType == ClientTransaction.TYPE_INVOICE) {
				return new NewInvoiceAction();
			} else if (transactionType == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				return new NewCreditsAndRefundsAction();
			} else if (transactionType == ClientTransaction.TYPE_CASH_SALES) {
				return new NewCashSaleAction();
			}
		}
		return null;
	}

	@Override
	public void exportToCsv() {
		if (viewSelect.getSelectedValue().equals(messages.drafts())) {
			showDialogBox(messages.ERROR(),
					messages.draftsTransactionsCannotBePrinted());
		} else {

			setViewId(checkViewType(getViewType()));
			Accounter.createExportCSVService().getInvoiceListExportCsv(
					getStartDate().getDate(), getEndDate().getDate(),
					transactionType, getViewId(),
					getExportCSVCallback(getListViewHeading()));
		}
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (transactionType == 0
					|| transactionType == ClientTransaction.TYPE_INVOICE) {
				return messages.addaNewInvoice();
			} else if (transactionType == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				return messages.newCreditNotes();
			} else if (transactionType == ClientTransaction.TYPE_CASH_SALES) {
				return messages.newCashSale();
			}
		}
		return "";
	}

	@Override
	protected String getListViewHeading() {
		if (transactionType == 0
				|| transactionType == ClientTransaction.TYPE_INVOICE) {
			return messages.invoiceList();
		} else if (transactionType == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
			return messages.customerCreditNotes();
		} else if (transactionType == ClientTransaction.TYPE_CASH_SALES) {
			return messages.cashSales();
		}
		return messages.invoiceList();
	}

	@Override
	public void updateInGrid(InvoicesList objectTobeModified) {

	}

	@Override
	public void onSuccess(PaginationList<InvoicesList> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), result.size(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.removeLoadingImage();
		listOfInvoices = result;
		viewSelect.setComboItem(getViewType());
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), result.size(),
				result.getTotalCount());
	}

	@Override
	protected void initGrid() {
		grid = new InvoiceListGrid(this.transactionType);
		grid.init();
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		if (transactionType == 0
				|| transactionType == ClientTransaction.TYPE_INVOICE) {
			listOfTypes.add(messages.overDue());
		}
		listOfTypes.add(messages.open());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.drafts());
		if (getViewType() != null && !getViewType().equals("")) {
			viewSelect.setComboItem(getViewType());
		} else {
			if (transactionType == 0
					|| transactionType == ClientTransaction.TYPE_INVOICE) {
				viewSelect.setComboItem(messages.open());
			} else {
				viewSelect.setComboItem(messages.all());
			}
		}
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		if (text.equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (text.equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (text.equalsIgnoreCase(messages.overDue())) {
			setViewId(VIEW_OVERDUE);
		} else if (text.equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		} else if (text.equalsIgnoreCase(messages.drafts())) {
			setViewId(VIEW_DRAFT);
		}

		onPageChange(start, getPageSize());

		// grid.removeAllRecords();
		// if (text.equals(messages.all())) {
		// for (InvoicesList invoice : listOfInvoices) {
		// invoice.setPrint(false);
		// grid.addData(invoice);
		// }
		// } else if (text.equals(messages.open())) {
		// for (InvoicesList invoice : listOfInvoices) {
		// if (invoice.getBalance() != null
		// && DecimalUtil.isGreaterThan(invoice.getBalance(), 0)
		// && invoice.getDueDate() != null
		// && (invoice.getStatus() !=
		// ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
		// && !invoice.isVoided()) {
		// invoice.setPrint(false);
		// grid.addData(invoice);
		// }
		// }
		//
		// } else if (text.equals(messages.overDue())) {
		// for (InvoicesList invoice : listOfInvoices) {
		// if (invoice.getBalance() != null
		// && DecimalUtil.isGreaterThan(invoice.getBalance(), 0)
		// && invoice.getDueDate() != null
		// && (invoice.getDueDate().compareTo(
		// new ClientFinanceDate()) < 0)
		// && !invoice.isVoided()) {
		// invoice.setPrint(false);
		// grid.addData(invoice);
		// }
		// }
		// } else if (text.equals(messages.voided())) {
		// for (InvoicesList invoice : listOfInvoices) {
		// if (invoice.isVoided()) {
		// invoice.setPrint(false);
		// grid.addData(invoice);
		// }
		// }
		// } else if (text.equals(messages.draft())) {
		// for (InvoicesList invoice : listOfInvoices) {
		// if (invoice.getSaveStatus() == ClientTransaction.STATUS_DRAFT) {
		// invoice.setPrint(false);
		// grid.addData(invoice);
		// }
		// }
		//
		// }
		//
		// if (grid.getRecords().isEmpty()) {
		// grid.addEmptyMessage(messages.noRecordsToShow());
		// }
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
	}

	@Override
	public void print() {

		if (viewSelect.getSelectedValue().equals(messages.drafts())) {
			showDialogBox(messages.ERROR(),
					messages.draftsTransactionsCannotBePrinted());

		} else {

			ArrayList<Integer> v = new ArrayList<Integer>();

			boolean isWriteCheck_cashsale = false;
			for (InvoicesList invoice : listOfInvoices) {

				if (invoice.isPrint()) {
					if (invoice.getType() == ClientTransaction.TYPE_INVOICE) {
						if (!v.contains(ClientTransaction.TYPE_INVOICE))
							v.add(ClientTransaction.TYPE_INVOICE);
					} else if (invoice.getType() == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
						if (!v.contains(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO))
							v.add(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
					} else if (invoice.getType() == ClientTransaction.TYPE_CASH_SALES) {
						if (!v.contains(ClientTransaction.TYPE_CASH_SALES))
							v.add(ClientTransaction.TYPE_CASH_SALES);
					} else if (invoice.getType() == ClientTransaction.TYPE_WRITE_CHECK) {
						if (!v.contains(ClientTransaction.TYPE_WRITE_CHECK))
							v.add(ClientTransaction.TYPE_WRITE_CHECK);
						isWriteCheck_cashsale = true;
					}
				}
			}

			String errorMessage = Global.get().messages()
					.pleaseSelectReportsOfSameType();
			String emptymsg = Global.get().messages()
					.pleaseSelectAtLeastOneReport();
			String cashsalemsg = Global.get().messages()
					.PrintIsNotProvidedForCashSale();
			if (v.size() == 0) {// no reports are selected
				showDialogBox(messages.selectReports(), emptymsg);
			} else if (v.size() > 1) {
				// if one than one report type is selected
				showDialogBox(messages.selectReports(), errorMessage);
			} else {
				if (!isWriteCheck_cashsale) {

					ArrayList<ClientBrandingTheme> themesList = Accounter
							.getCompany().getBrandingTheme();
					if (themesList.size() > 1) {
						// if there are more than one branding themes, then show
						// branding
						// theme dialog box
						new BrandingThemeComboAction().run(listOfInvoices);
					} else {
						// else print directly
						brandingTheme = themesList.get(0);
						printDocument();
					}

				} else {
					// if other reports are selected cash sale or write check
					showDialogBox(messages.selectReports(), cashsalemsg);
				}
				// ActionFactory.getInvoiceListViewAction().run(listOfInvoices);
			}
		}

	}

	public void showDialogBox(String title, String description) {
		InvoicePrintDialog printDialog = new InvoicePrintDialog(title, "",
				description);
		ViewManager.getInstance().showDialog(printDialog);
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.invoices();
	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	/**
	 * used to print the documents based on the selected multiple objects
	 */
	public void printDocument() {

		// for printing multiple documents
		StringBuffer ids = new StringBuffer();

		for (int i = 0; i < listOfInvoices.size(); i++) {
			InvoicesList invoice = listOfInvoices.get(i);

			if (invoice.isPrint()) {

				String id = String.valueOf(invoice.getTransactionId());

				ids = ids.append(id + ",");

			}
		}

		String[] arrayIds = ids.toString().split(",");
		int type = 0;
		for (int i = 0; i < listOfInvoices.size(); i++) {
			InvoicesList invoicesList = listOfInvoices.get(i);
			if (invoicesList.isPrint()) {
				if (Integer.parseInt(arrayIds[0]) == invoicesList
						.getTransactionId()) {

					type = invoicesList.getType();
				}
			}

		}

		if (type != 0) {
			UIUtils.downloadMultipleAttachment(ids.toString(), type,
					brandingTheme.getID());
		}

	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {
		setViewId(checkViewType(getViewType()));
		Accounter.createHomeService().getInvoiceList(getStartDate().getDate(),
				getEndDate().getDate(), transactionType, getViewId(), start,
				length, this);
	}

	private int checkViewType(String view) {
		if (getViewType().equalsIgnoreCase(messages.open())) {
			setViewId(VIEW_OPEN);
		} else if (getViewType().equalsIgnoreCase(messages.voided())) {
			setViewId(VIEW_VOIDED);
		} else if (getViewType().equalsIgnoreCase(messages.overDue())) {
			setViewId(VIEW_OVERDUE);
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		} else if (getViewType().equalsIgnoreCase(messages.drafts())) {
			setViewId(VIEW_DRAFT);
		}

		return getViewId();
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}
}
