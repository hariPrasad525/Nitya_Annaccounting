package com.nitya.accounter.web.client.portlet;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.PayeesBySalesPortletData;
import com.nitya.accounter.web.client.ui.Portlet;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.reports.TopPayeesBySalesPortletToolBar;

public class TopPayeesBySalesPortlet extends Portlet {
	public static final int CUSTOMER_PORTLET = 1;
	public static final int VENDOR_PORTLET = 2;
	public static final int ITEM_PORTLET = 3;

	private TopPayeesBySalesPortletToolBar toolBar;
	private StyledPanel gridPanel;
	private int portletType;

	public TopPayeesBySalesPortlet(ClientPortletConfiguration configuration,
			int portletType) {
		super(configuration, messages.topVendorsByExpense(Global.get()
				.vendors()), "", "100%");
		this.getElement().setId("TopPayeesBySalesPortlet");
		this.portletType = portletType;
		setPortletTitle();
	}

	private void setPortletTitle() {
		String title = "";
		if (portletType == CUSTOMER_PORTLET) {
			title = messages.topCustomersBySales(Global.get().Customers());
		} else if (portletType == VENDOR_PORTLET) {
			title = messages.topVendorsByExpense(Global.get().vendors());
		} else if (portletType == ITEM_PORTLET) {
			title = messages.topItemsBySales();
		}
		this.setPortletTitle(title);
	}

	@Override
	public void createBody() {
		gridPanel = new StyledPanel("gridPanel");
		createToolBar();
		this.body.add(gridPanel);
	}

	private void createToolBar() {
		toolBar = new TopPayeesBySalesPortletToolBar() {

			@Override
			protected void refreshPortletData() {
				TopPayeesBySalesPortlet.this.clearGrid();
				dateRangeItemCombo.setSelected(portletConfigData
						.get(DATE_RANGE));
				limitCombo.setSelected(portletConfigData.get(LIMIT));
				TopPayeesBySalesPortlet.this.getConfiguration().setPortletMap(
						portletConfigData);
				dateRangeChanged(portletConfigData.get(DATE_RANGE));
				TopPayeesBySalesPortlet.this.updateData(startDate, endDate,
						portletConfigData.get(LIMIT));
				updateConfiguration();
			}

			@Override
			protected void initOrSetConfigDataToPortletConfig() {
				if (TopPayeesBySalesPortlet.this.getConfiguration()
						.getPortletMap().get(DATE_RANGE) != null) {
					portletConfigData.put(DATE_RANGE,
							TopPayeesBySalesPortlet.this.getConfiguration()
									.getPortletMap().get(DATE_RANGE));
				} else {
					portletConfigData.put(DATE_RANGE,
							messages.financialYearToDate());
				}
				if (TopPayeesBySalesPortlet.this.getConfiguration()
						.getPortletMap().get(LIMIT) != null) {
					portletConfigData.put(LIMIT, TopPayeesBySalesPortlet.this
							.getConfiguration().getPortletMap().get(LIMIT));
				} else {
					portletConfigData.put(LIMIT, "5");
				}

			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				dateRangeItemCombo.setSelected(defaultDateRange);
				dateRangeChanged(defaultDateRange);
				TopPayeesBySalesPortlet.this
						.updateData(startDate, endDate, "5");
				// updateConfiguration();
			}
		};
		this.body.add(toolBar);
	}

	protected void updateData(ClientFinanceDate startDate,
			ClientFinanceDate endDate, String limit) {
		AsyncCallback<ArrayList<PayeesBySalesPortletData>> callback = new AsyncCallback<ArrayList<PayeesBySalesPortletData>>() {

			private PayeeBySalesGrid grid;

			@Override
			public void onSuccess(ArrayList<PayeesBySalesPortletData> result) {
				grid = new PayeeBySalesGrid(portletType);
				grid.init();
				if (result != null && !(result.isEmpty())) {
					grid.setRecords(result);
				} else {
					grid.addEmptyMessage(messages.noTransactionsToshow());
				}
				gridPanel.add(grid);
				completeInitialization();
			}

			@Override
			public void onFailure(Throwable caught) {
				completeInitialization();
			}
		};
		if (portletType == CUSTOMER_PORTLET) {
			Accounter.createHomeService().getTopCustomersBySalesPortletData(
					startDate, endDate, Integer.valueOf(limit), callback);
		} else if (portletType == VENDOR_PORTLET) {
			Accounter.createHomeService().getTopVendorsBySalesPortletData(
					startDate, endDate, Integer.valueOf(limit), callback);
		} else if (portletType == ITEM_PORTLET) {
			Accounter.createHomeService().getItemsBySalesQuantity(startDate,
					endDate, Integer.valueOf(limit), callback);
		}
	}

	protected void clearGrid() {
		gridPanel.clear();
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
