//package com.vimukti.accounter.web.client.ui.customers;
//
//import java.util.List;
//
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.vimukti.accounter.web.client.AccounterAsyncCallback;
//import com.vimukti.accounter.web.client.Global;
//import com.vimukti.accounter.web.client.core.AccounterCoreType;
//import com.vimukti.accounter.web.client.core.ClientCustomer;
//import com.vimukti.accounter.web.client.core.ClientEstimate;
//import com.vimukti.accounter.web.client.core.ClientSalesOrder;
//import com.vimukti.accounter.web.client.core.ClientTAXCode;
//import com.vimukti.accounter.web.client.core.ClientTransaction;
//import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
//import com.vimukti.accounter.web.client.exception.AccounterException;
//import com.vimukti.accounter.web.client.ui.Accounter;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.core.BaseDialog;
//import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
//import com.vimukti.accounter.web.client.ui.grids.ListGrid;
//
//public class CustomerQuoteListDialog extends
//		BaseDialog<EstimatesAndSalesOrdersList> {
//	public DialogGrid<EstimatesAndSalesOrdersList> grid;
//	private InvoiceView invoiceView;
//	private List<EstimatesAndSalesOrdersList> estimatesAndSalesOrder;
//
//	public ClientCustomer preCustomer;
//	private ClientTAXCode taxCode;
//
//	public CustomerQuoteListDialog(InvoiceView parentView,
//			List<EstimatesAndSalesOrdersList> estimatesAndSalesOrder) {
//		super(messages.quotesList(), "");
//		invoiceView = parentView;
//		this.estimatesAndSalesOrder = estimatesAndSalesOrder;
//		if (getPreferences().isSalesOrderEnabled()
//				&& getPreferences().isDoyouwantEstimates()) {
//			setText(messages.quoteAndSalesOrderList());
//		} else if (getPreferences().isDoyouwantEstimates()) {
//			setText(messages.quoteList());
//
//		} else if (getPreferences().isSalesOrderEnabled()) {
//			setText(messages.salesOrderList());
//		}
//		createControl();
//		setWidth("600px");
//		show();
//		center();
//
//	}
//
//	private void createControl() {
//
//		StyledPanel mainLayout = new StyledPanel();
//		mainLayout.setSize("100%", "100%");
//		mainLayout.setSpacing(3);
//		Label infoLabel = null;
//		if (getPreferences().isSalesOrderEnabled()
//				&& getPreferences().isDoyouwantEstimates()) {
//			infoLabel = new Label(messages.selectQuoteOrSalesOrder());
//		} else if (getPreferences().isDoyouwantEstimates()) {
//			infoLabel = new Label(messages.selectQuote());
//		} else if (getPreferences().isSalesOrderEnabled()) {
//			infoLabel = new Label(messages.selectSalesOrder());
//		}
//		mainLayout.add(infoLabel);
//
//		grid = new DialogGrid<EstimatesAndSalesOrdersList>(true);
//		grid.addColumns(messages.date(), messages.no(),
//				messages.type(), messages.payeeName(
//						Global.get().Customer()), messages.total(),
//				messages.remainingTotal());
//		grid.setView(this);
//		grid.setCellsWidth(70, 30, 80, -1, 60, 95);
//		grid.init();
//		grid.setColumnTypes(ListGrid.COLUMN_TYPE_TEXT,
//				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
//				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
//				ListGrid.COLUMN_TYPE_DECIMAL_TEXT);
//
//		setQuoteList(estimatesAndSalesOrder);
//		mainLayout.add(grid);
//
//		mainLayout.setSize("100%", "100%");
//		setBodyLayout(mainLayout);
//	}
//
//	protected void setRecord(List<EstimatesAndSalesOrdersList> records) {
//
//		if (records != null)
//			for (EstimatesAndSalesOrdersList rec : records) {
//				if (rec.getType() == ClientTransaction.TYPE_ESTIMATE) {
//					getEstimate(rec);
//				} else {
//					getSalesOrder(rec);
//				}
//			}
//
//	}
//
//	private void getSalesOrder(EstimatesAndSalesOrdersList record) {
//		AccounterAsyncCallback<ClientSalesOrder> callback = new AccounterAsyncCallback<ClientSalesOrder>() {
//
//			@Override
//			public void onException(AccounterException caught) {
//				Accounter.showError(messages.errorLoadingSalesOrder());
//
//			}
//
//			@Override
//			public void onResultSuccess(ClientSalesOrder result) {
//			}
//
//		};
//
//		rpcGetService.getObjectById(AccounterCoreType.SALESORDER,
//				record.getTransactionId(), callback);
//
//	}
//
//	private void getEstimate(EstimatesAndSalesOrdersList record) {
//		AccounterAsyncCallback<ClientEstimate> callback = new AccounterAsyncCallback<ClientEstimate>() {
//
//			@Override
//			public void onException(AccounterException caught) {
//				Accounter.showError(messages.errorLoadingQuote());
//
//			}
//
//			@Override
//			public void onResultSuccess(ClientEstimate result) {
//			}
//
//		};
//		rpcGetService.getObjectById(AccounterCoreType.ESTIMATE,
//				record.getTransactionId(), callback);
//
//	}
//
//	public void setQuoteList(List<EstimatesAndSalesOrdersList> result) {
//		if (result == null)
//			return;
//		this.estimatesAndSalesOrder = result;
//		grid.removeAllRecords();
//
//		for (EstimatesAndSalesOrdersList rec : estimatesAndSalesOrder) {
//			grid.addData(rec);
//		}
//	}
//
//	public Object getGridColumnValue(EstimatesAndSalesOrdersList estimate,
//			int index) {
//		if (estimate != null) {
//			switch (index) {
//			case 0:
//				return UIUtils.getDateByCompanyType(estimate.getDate());
//			case 1:
//				return estimate.getTransactionNumber();
//			case 2:
//				if (estimate.getType() == ClientTransaction.TYPE_ESTIMATE) {
//					return messages.quote();
//				} else {
//					return messages.salesOrder();
//				}
//			case 3:
//				return estimate.getCustomerName();
//			case 4:
//				return amountAsString(estimate.getTotal());
//			case 5:
//				return amountAsString(estimate.getRemainingTotal());
//			}
//		}
//		return null;
//
//	}
//
//	@Override
//	protected boolean onOK() {
//		List<EstimatesAndSalesOrdersList> record = (List<EstimatesAndSalesOrdersList>) grid
//				.getSelectedRecords();
//		setRecord(record);
//		return true;
//	}
//
//	public void setTaxCode(ClientTAXCode code) {
//		this.taxCode = code;
//	}
//
//	public ClientTAXCode getTaxCode() {
//		return taxCode;
//	}
//
//	@Override
//	public void setFocus() {
//		// TODO Auto-generated method stub
//
//	}
//}
