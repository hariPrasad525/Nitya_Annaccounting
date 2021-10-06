package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.QuoteListGrid;

public class QuoteListView extends TransactionsListView<ClientEstimate>
		implements IPrintableView {

	int viwType = -1;
	private final int type;

	public QuoteListView(int type) {
		super(messages.open());
		this.type = type;
		// isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (type == ClientEstimate.QUOTES) {
				return new NewQuoteAction(type);
			} else if (type == ClientEstimate.SALES_ORDER) {
				if (getPreferences().isSalesOrderEnabled()) {
					return new NewQuoteAction(type);
				}
				return null;
			} else if (getPreferences().isDelayedchargesEnabled()) {
				if (type == ClientEstimate.CHARGES) {
					return new NewQuoteAction(type);
				} else if (type == ClientEstimate.CREDITS) {
					return new NewQuoteAction(type);
				}
			}
		}
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (type == ClientEstimate.QUOTES) {
				return messages.addaNewQuote();
			} else if (type == ClientEstimate.SALES_ORDER) {
				if (getPreferences().isSalesOrderEnabled()) {
					return messages.addaNew(messages.salesOrder());
				}
				return "";
			} else if (getPreferences().isDelayedchargesEnabled()) {
				if (type == ClientEstimate.CHARGES) {
					return messages.addNewCharge();
				} else if (type == ClientEstimate.CREDITS) {
					return messages.addNew(messages.credit());
				}
			}
		}
		return "";
	}

	@Override
	protected String getListViewHeading() {
		if (type == ClientEstimate.CHARGES) {
			return messages.chargesList();
		} else if (type == ClientEstimate.CREDITS) {
			return messages.creditsList();
		} else if (type == ClientEstimate.SALES_ORDER) {
			return messages.salesOrderList();
		}
		return messages.quotesList();
	}

	@Override
	public void initListCallback() {
		onPageChange(0, getPageSize());
	}

	@Override
	public void onSuccess(PaginationList<ClientEstimate> result) {
		grid.setViewType(viewSelect.getSelectedValue());
		grid.removeLoadingImage();
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.sort(10, false);
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	@Override
	public void updateInGrid(ClientEstimate objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new QuoteListGrid(type);
		grid.init();

	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.open());
		if (type == ClientEstimate.SALES_ORDER) {
			listOfTypes.add(messages.completed());
			listOfTypes.add(messages.cancelled());
		} else if (type == ClientEstimate.QUOTES) {
			listOfTypes.add(messages.rejected());
			listOfTypes.add(messages.accepted());
			listOfTypes.add(messages.close());
			listOfTypes.add(messages.applied());
		}
		listOfTypes.add(messages.expired());
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.drafts());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		this.setViewType(text);
		onPageChange(start, getPageSize());
		// grid.removeAllRecords();
		// for (ClientEstimate estimate : listOfEstimates) {
		// if (text.equals(messages.open())) {
		// if (estimate.getStatus() == ClientEstimate.STATUS_OPEN)
		// grid.addData(estimate);
		// continue;
		// }
		// if (text.equals(messages.rejected())) {
		// if (estimate.getStatus() == ClientEstimate.STATUS_REJECTED)
		// grid.addData(estimate);
		// continue;
		// }
		// if (text.equals(messages.accepted())) {
		// if (estimate.getStatus() == ClientEstimate.STATUS_ACCECPTED)
		// grid.addData(estimate);
		// continue;
		// }
		// if (text.equals(messages.expired())) {
		// ClientFinanceDate expiryDate = new ClientFinanceDate(
		// estimate.getExpirationDate());
		// if (expiryDate.before(new ClientFinanceDate()))
		// grid.addData(estimate);
		// continue;
		// }
		// if (text.equals(messages.applied())) {
		// if (estimate.getStatus() == ClientEstimate.STATUS_APPLIED)
		// grid.addData(estimate);
		// continue;
		// }
		// if (text.equals(messages.close())) {
		// if (estimate.getStatus() == ClientEstimate.STATUS_CLOSE)
		// grid.addData(estimate);
		// continue;
		// }
		// if (text.equals(messages.all())) {
		// grid.addData(estimate);
		// }
		// if (text.equalsIgnoreCase(messages.drafts())) {
		// if (estimate.getSaveStatus() == ClientEstimate.STATUS_DRAFT) {
		// grid.addData(estimate);
		// }
		// }
		// }
		// if (grid.getRecords().isEmpty())
		// grid.addEmptyMessage(messages.noRecordsToShow());

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		if (type == ClientEstimate.CREDITS) {
			return messages.credits();
		} else if (type == ClientEstimate.CHARGES) {
			return messages.Charges();
		} else if (type == ClientEstimate.SALES_ORDER) {
			return messages.salesOrders();
		}
		return messages.quotes();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {
		viwType = -1;
		if (getViewType().equalsIgnoreCase(messages.open())) {
			viwType = ClientEstimate.STATUS_OPEN;
		} else if (getViewType().equalsIgnoreCase(messages.rejected())) {
			viwType = ClientEstimate.STATUS_REJECTED;
		} else if (getViewType().equalsIgnoreCase(messages.accepted())) {
			viwType = ClientEstimate.STATUS_ACCECPTED;
		} else if (getViewType().equalsIgnoreCase(messages.applied())) {
			viwType = ClientTransaction.STATUS_COMPLETED;
		} else if (getViewType().equalsIgnoreCase(messages.close())) {
			viwType = ClientEstimate.STATUS_CLOSE;
		} else if (getViewType().equalsIgnoreCase(messages.drafts())) {
			viwType = ClientTransaction.STATUS_DRAFT;
		} else if (getViewType().equalsIgnoreCase(messages.expired())) {
			viwType = 6;
		} else if (getViewType().equalsIgnoreCase(messages.completed())) {
			viwType = ClientTransaction.STATUS_COMPLETED;
		} else if (getViewType().equalsIgnoreCase(messages.cancelled())) {
			viwType = ClientTransaction.STATUS_CANCELLED;
		}
		grid.removeAllRecords();
		grid.addLoadingImagePanel();
		Accounter.createHomeService().getEstimates(type, viwType,
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, this);
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getEstimatesExportCsv(type, viwType,
				getStartDate().getDate(), getEndDate().getDate(),
				getExportCSVCallback(getViewTitle()));
	}
}
