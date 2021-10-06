package com.nitya.accounter.web.client.ui.customers;
//package com.nitya.accounter.web.client.ui.customers;
//
//import java.util.List;
//
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.nitya.accounter.web.client.Global;
//import com.nitya.accounter.web.client.core.ClientEstimate;
//import com.nitya.accounter.web.client.core.Utility;
//import com.nitya.accounter.web.client.ui.core.BaseDialog;
//import com.nitya.accounter.web.client.ui.grids.DialogGrid;
//import com.nitya.accounter.web.client.ui.widgets.DateUtills;
//
//public class SalesQuoteListDialog extends BaseDialog<ClientEstimate> {
//
//	private SalesOrderView salesView;
//	public DialogGrid<ClientEstimate> grid;
//	private List<ClientEstimate> estimates;
//	private ClientEstimate selectedEstimate;
//
//	public SalesQuoteListDialog(SalesOrderView parentView,
//			List<ClientEstimate> estimates) {
//		super(messages.quoteList());
//		salesView = parentView;
//		this.estimates = estimates;
//		createControl();
//		setWidth("600px");
//		setQuoteList(estimates);
//		show();
//		center();
//	}
//
//	private void createControl() {
//
//		StyledPanel mainLayout = new StyledPanel();
//		mainLayout.setSize("100%", "100%");
//		mainLayout.setSpacing(3);
//		Label infoLabel = new Label(messages.selectQuote());
//
//		mainLayout.add(infoLabel);
//
//		grid = new DialogGrid<ClientEstimate>(true);
//		grid.addColumns(messages.date(), messages.no(), messages.type(),
//				messages.payeeName(Global.get().Customer()), messages.total());
//		grid.setCellsWidth(70, 30, 60, -1, 60);
//		grid.setView(this);
//		grid.init();
//		setQuoteList(estimates);
//		mainLayout.add(grid);
//		mainLayout.setSize("100%", "100%");
//		setBodyLayout(mainLayout);
//	}
//
//	protected ClientEstimate getEstimate(long estimateId) {
//		for (ClientEstimate estimate : estimates) {
//			if (estimate != null) {
//				if (estimate.getID() == estimateId)
//					return estimate;
//			}
//		}
//		return null;
//	}
//
//	public void setQuoteList(List<ClientEstimate> result) {
//		if (result == null)
//			return;
//		this.estimates = result;
//		grid.removeAllRecords();
//		if (result.isEmpty()) {
//			grid.addEmptyMessage(messages.noRecordsToShow());
//		}
//		for (ClientEstimate rec : estimates) {
//			grid.addData(rec);
//		}
//	}
//
//	public Object getGridColumnValue(ClientEstimate estimate, int index) {
//		if (estimate != null) {
//			switch (index) {
//			case 0:
//				return DateUtills.getDateAsString(estimate.getDate());
//			case 1:
//				return estimate.getNumber();
//			case 2:
//				return Utility.getTransactionName(estimate.getType());
//			case 3:
//				return company.getCustomer(estimate.getCustomer()).getName();
//			case 4:
//				return amountAsString(estimate.getTotal());
//			}
//		}
//		return null;
//
//	}
//
//	public void setFocus() {
//
//	}
//
//	@Override
//	protected boolean onOK() {
//		List<ClientEstimate> selectedEstimates = (List<ClientEstimate>) grid
//				.getSelectedRecords();
//		for (ClientEstimate estimate : selectedEstimates) {
//			if (salesView != null || selectedEstimate != null)
//				salesView.selectedQuote(estimate);
//		}
//		return true;
//	}
//
// }
