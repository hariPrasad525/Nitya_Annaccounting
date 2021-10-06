package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.banking.BankingSectionHomeView;
import com.vimukti.accounter.web.client.ui.customers.CustomerSectionHomeView;
import com.vimukti.accounter.web.client.ui.vendors.VendorSectionHomeView;

/**
 * Layout on which Widgets will float
 * 
 * @author vimukti9
 * 
 */
public class PortalLayout extends FlowPanel {

	private DashBoardView dashboardParent;
	private CustomerSectionHomeView customerParent;
	private VendorSectionHomeView vendorParent;

	private BankingSectionHomeView bankingParent;
	// private Timer timer;
	ClientUser user;

	public PortalLayout(BaseHomeView parent, int numColumns) {
		if (parent instanceof CustomerSectionHomeView) {
			this.customerParent = (CustomerSectionHomeView) parent;
		} else if (parent instanceof DashBoardView) {
			this.dashboardParent = (DashBoardView) parent;
		} else if (parent instanceof VendorSectionHomeView) {
			this.vendorParent = (VendorSectionHomeView) parent;
		} else {
			this.bankingParent = (BankingSectionHomeView) parent;
		}

		user = Accounter.getUser();
		// setBackgroundColor("#104E8B");
		// setCanDragResize(false);
		// setMembersMargin(4);
		for (int i = 0; i < numColumns; i++) {
			add(new PortalColumn(i));
		}

	}

	/**
	 * Adding specific portlet on Specific Column
	 * 
	 * @param portlet
	 * @param col
	 * @return PortalColumn
	 */

	public PortalColumn addPortlet(Portlet portlet, int col) {
		// PortalColumn fewestPortletsColumn = (PortalColumn) getMember(col);
		// fewestPortletsColumn.add(portlet);
		// portlet.addDragRepositionStopHandler(new DragRepositionStopHandler()
		// {
		//
		// public void onDragRepositionStop(DragRepositionStopEvent event) {
		// timer = new Timer() {
		//
		// @Override
		// public void run() {
		// setPreference();
		// }
		// };
		// timer.schedule(200);
		// }
		//
		// });
		//
		// portlet.addCloseClickHandler(new CloseClickHandler() {
		//
		// public void onCloseClick(CloseClientEvent event) {
		// setPreference();
		//
		// }
		//
		// });
		// return fewestPortletsColumn;
		return null;
	}

	/**
	 * Adding portlet on Columns in left to right direction
	 * 
	 * @param portlet
	 * @return PortalColumn
	 */

	public PortalColumn addPortlet(Portlet portlet) {
		// int fewestPortlets = Integer.MAX_VALUE;
		// PortalColumn fewestPortletsColumn = null;
		// for (int i = 0; i < getMembers().length; i++) {
		// int numPortlets = ((PortalColumn) getMember(i)).getMembers().length;
		// if (numPortlets < fewestPortlets) {
		// fewestPortlets = numPortlets;
		// fewestPortletsColumn = (PortalColumn) getMember(i);
		// fewestPortletsColumn.adjustForContent(true);
		// }
		// }
		// fewestPortletsColumn.add(portlet);
		//
		// portlet.addDragRepositionStopHandler(new DragRepositionStopHandler()
		// {
		//
		// public void onDragRepositionStop(DragRepositionStopEvent event) {
		// timer = new Timer() {
		//
		// @Override
		// public void run() {
		// setPreference();
		// }
		// };
		// timer.schedule(200);
		//
		// }
		//
		// });
		//
		// portlet.addCloseClickHandler(new CloseClickHandler() {
		//
		// public void onCloseClick(CloseClientEvent event) {
		// setPreference();
		//
		// }
		//
		// });
		// return fewestPortletsColumn;
		return null;
	}

	/**
	 * Getting info of all the Widgets for Setting Preferences
	 * 
	 * @return WidgetsString
	 */
	public String getWidgetsString() {
		// String widgetString = "";
		// String[] firstArray = new String[((PortalColumn) getMember(0))
		// .getMembers().length];
		// String[] secondArray = new String[((PortalColumn) getMember(1))
		// .getMembers().length];
		//
		// int index1 = 0;
		// int index2 = 0;
		//
		// for (int i = 0; i < getMembers().length; i++) {
		// Canvas[] canvasArray = ((PortalColumn) getMember(i)).getMembers();
		// for (int k = 0; k < canvasArray.length; k++) {
		// Portlet portlet = (Portlet) canvasArray[k];
		// portlet.setColumn(i);
		// portlet.setRow(k);
		// if (i == 0) {
		// firstArray[index1] = portlet.getName();
		// index1++;
		// } else {
		// secondArray[index2] = portlet.getName();
		// index2++;
		// }
		//
		// }
		//
		// }
		//
		// for (int i = 0; i < firstArray.length; i++) {
		// widgetString += firstArray[i] + "~";
		// }
		// widgetString += ",";
		// for (int i = 0; i < secondArray.length; i++) {
		// widgetString += secondArray[i] + "~";
		// }
		//
		// return widgetString;
		return null;
	}

	/**
	 * Setter for setting Company Preferences
	 */

	public void setPreference() {
		if (dashboardParent != null) {
			// user.getUserPreferences().setDashBoardPreferences(
			// getWidgetsString());
		} else if (customerParent != null) {
			// user.getUserPreferences().setCustomerSectionViewPreferences(
			// getWidgetsString());
		} else if (vendorParent != null) {
			// user.getUserPreferences().setVendorSectionViewPreferences(
			// getWidgetsString());
		} else {
			// user.getUserPreferences().setBankingSectionViewPreferences(
			// getWidgetsString());
		}
		savePreferences();
	}

	/**
	 * CallBack for Saving User Object for Preferences
	 */
	private void savePreferences() {

		AccounterAsyncCallback<Long> alterUserCallback = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				// TODO Auto-generated method stub

			}

			public void onResultSuccess(Long result) {
			}

		};
		Accounter.createCRUDService().update(user, alterUserCallback);
	}
}
