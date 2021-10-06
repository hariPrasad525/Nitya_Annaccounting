package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import java.util.ArrayList;
//
//import com.google.gwt.user.client.Timer;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.nitya.accounter.web.client.core.AccounterCoreType;
//import com.nitya.accounter.web.client.core.ClientAccount;
//import com.nitya.accounter.web.client.core.IAccounterCore;
//import com.nitya.accounter.web.client.ui.core.WidgetCreator;
//import com.nitya.accounter.web.client.ui.grids.CompanyFinancialWidgetGrid;
//import com.nitya.accounter.web.client.ui.grids.CustomerWidgetGrid;
//
//public class FinanceDashboard extends BaseHomeView {
//
//	ArrayList<String> addablePortletList = new ArrayList<String>();
//
//	
//	private FinanceDashboard dashboard = null;
//	private String dashboardPreference;
//	private PortalLayout portalLayout;
//	private Portlet[] portlet;
//	private WidgetCreator creator;
//	private String[] widgetOnSectionPage;
//	private CustomerWidgetGrid customerWidgetGrid;
//	private CompanyFinancialWidgetGrid grid;
//
//	private Timer timer;
//
//	// private String[] secondColumn;
//	//
//	// private String[] firstColumn;
//
//	public FinanceDashboard() {
//		dashboard = this;
//		dashboardPreference = messages
//				.welcomeBankingSummary();
//		/*
//		 * FinanceApplication.getUser().getUserPreferences()
//		 * .getDashBoardPreferences();
//		 */
//
//	}
//
//	@Override
//	public void init() {
//		getLeftLayout().add(createControl());
//		setSize("100%", "100%");
//
//	}
//
//	private StyledPanel createControl() {
//		creator = new WidgetCreator();
//
//		// StyledPanel addWidgetLinkLayout = new StyledPanel();
//		// // addWidgetLinkLayout.setHeight(20);
//		//
//		// Label addWidgetLink = new Label(FinanceApplication
//		// .constants().addWidget());
//		//
//		// addWidgetLink.addClickHandler(new ClickHandler() {
//		//
//		// public void onClick(ClickEvent event) {
//		// new AddWidgetDialog(dashboard);
//		// }
//		//
//		// });
//		//
//		// addWidgetLinkLayout.add(addWidgetLink);
//
//		portalLayout = new PortalLayout(this, 1);
//
//		widgetOnSectionPage = dashboardPreference.split(",");
//		portlet = new Portlet[widgetOnSectionPage.length];
//		for (int i = 0; i < widgetOnSectionPage.length; i++) {
//			
//			final int index = i;
//			if (widgetOnSectionPage[i].equals("")) {
//
//			} else {
//				portlet[i] = creator.getWidgetByName(widgetOnSectionPage[i]);
//				// portlet[i].addCloseClickHandler(new CloseClickHandler() {
//				//					
//				// public void onCloseClick(CloseClientEvent event) {
//				// addablePortletList.add(portlet[index].getName());
//				// portlet[index].destroy();
//				//					
//				// }
//				//					
//				// });
//
//				portalLayout.add(portlet[i]);
//				portlet[i].refreshClicked();
//			}
//		}
//		// timer = new Timer() {
//		//
//		// @Override
//		// public void run() {
//		// for (Portlet plet : portlet) {
//		// plet.refreshClicked();
//		// }
//		// }
//		// };
//		// timer.scheduleRepeating(60000);
//		// } else {
//		// firstColumn = portletArray[0].split("~");
//		// if (portletArray.length > 1) {
//		// secondColumn = portletArray[1].split("~");
//		// widgetOnSectionPage = new String[firstColumn.length
//		// + secondColumn.length];
//		// } else {
//		// widgetOnSectionPage = new String[firstColumn.length];
//		// }
//		//
//		// int widgetIndex = 0;
//		// for (int i = 0; i < firstColumn.length; i++) {
//		// widgetOnSectionPage[widgetIndex] = firstColumn[i];
//		// widgetIndex++;
//		// }
//		// if (portletArray.length > 1) {
//		// for (int i = 0; i < secondColumn.length; i++) {
//		//
//		// widgetOnSectionPage[widgetIndex] = secondColumn[i];
//		// widgetIndex++;
//		// }
//		// }
//		// if (portletArray.length > 1) {
//		// portlet = new Portlet[firstColumn.length + secondColumn.length];
//		// } else {
//		// portlet = new Portlet[firstColumn.length];
//		// }
//		// for (int i = 0; i < firstColumn.length; i++) {
//		// final int index = i;
//		// if (firstColumn[i].equals("")) {
//		//
//		// } else {
//		// portlet[i] = creator.getWidgetByName(firstColumn[i]);
//		// // portlet[i].addCloseClickHandler(new CloseClickHandler() {
//		// //
//		// // public void onCloseClick(CloseClientEvent event) {
//		// // addablePortletList.add(portlet[index].getName());
//		// // portlet[index].destroy();
//		// //
//		// // }
//		// //
//		// // });
//		// portalLayout.add(portlet[i]);
//		// }
//		// }
//		//
//		// if (portletArray.length > 1) {
//		// for (int i = 0; i < secondColumn.length; i++) {
//		// final int index = i + firstColumn.length;
//		// if (secondColumn[i].equals("")) {
//		//
//		// } else {
//		// portlet[index] = creator
//		// .getWidgetByName(secondColumn[i]);
//		// // portlet[index]
//		// // .addCloseClickHandler(new CloseClickHandler() {
//		// //
//		// // public void onCloseClick(
//		// // CloseClientEvent event) {
//		// // addablePortletList.add(portlet[index]
//		// // .getName());
//		// // portlet[index].destroy();
//		// //
//		// // }
//		// //
//		// // });
//		// portalLayout.add(portlet[index]);
//		// }
//		// }
//		// }
//		// }
//
//		getAddableWidgets(widgetOnSectionPage);
//
//		StyledPanel leftLayout = new StyledPanel();
//		leftLayout.setSize("100%", "100%");
//		// leftLayout.add(addWidgetLinkLayout);
//		leftLayout.add(portalLayout);
//		return leftLayout;
//
//	}
//
//	public void refreshGrids(final IAccounterCore accounterCoreObject) {
//
//		timer = new Timer() {
//
//			@Override
//			public void run() {
//				IAccounterCore core = accounterCoreObject;
//				if (core.getObjectType() == AccounterCoreType.CUSTOMER
//						|| ((ClientAccount) core).getType() == ClientAccount.TYPE_INCOME) {
//					portlet[1].refreshClicked();
//				} else {
//					if (core.getObjectType() == AccounterCoreType.ACCOUNT)
//						portlet[0].refreshClicked();
//				}
//				
//			}
//
//		};
//		timer.schedule(600);
//
//	}
//
//	public void getAddableWidgets(String[] widgetOnSectionPage) {
//		String[] totalWidget = {
//				messages.welcome(),
//				messages.bankingSummary(),
//				messages.profitAndLoss(),
//				messages.creditOverview(),
//				messages.debitOverview(),
//				messages.latestQuote(),
//				messages.expenses() };
//
//		boolean isAvailable = false;
//
//		for (int i = 0; i < totalWidget.length; i++) {
//			for (int k = 0; k < widgetOnSectionPage.length; k++) {
//				if (totalWidget[i].equals(widgetOnSectionPage[k])) {
//					isAvailable = true;
//					break;
//				} else {
//					isAvailable = false;
//				}
//
//			}
//			if (!isAvailable) {
//				addablePortletList.add(totalWidget[i]);
//				isAvailable = false;
//			}
//		}
//
//	}
//
//	public PortalLayout getPortalLayout() {
//		return portalLayout;
//	}
//
//	public void setPortalLayout(PortalLayout portalLayout) {
//		this.portalLayout = portalLayout;
//	}
//
//	public ArrayList<String> getAddablePortletList() {
//		return addablePortletList;
//	}
//
//	public void setAddablePortletList(ArrayList<String> addablePortletList) {
//		this.addablePortletList = addablePortletList;
//	}
//
//	@Override
//	public boolean shouldSaveInHistory() {
//		// Company Home Should always be in History so shouldSaveInHistory
//		// should true
//		return true;
//	}
//
//	@Override
//	public void fitToSize(int height, int width) {
//
//		// if (height > 0) {
//		// if (height - 205 > 220) {
//		// creator.setCustomerWidgetHeight(height - 305);
//		// creator.setCompanyFinancialWidgetHeight(205);
//		// } else {
//		// creator.setCustomerWidgetHeight(height - 260);
//		// creator.setCompanyFinancialWidgetHeight(205);
//		// }
//		//
//		// }
//		if (height > 0) {
//			creator.setCompanyFinancialWidgetHeight(205);
//			creator.setCustomerWidgetHeight(height - 200);
//		}
//	}
//
//	@Override
//	protected void onAttach() {
//		creator.setContinueRequest(true);
//		// timer.scheduleRepeating(60000);
//		super.onAttach();
//	}
//
//	@Override
//	protected void onUnload() {
//		creator.setContinueRequest(false);
//		if (timer != null)
//			timer.cancel();
//		super.onUnload();
//	}
//
// }