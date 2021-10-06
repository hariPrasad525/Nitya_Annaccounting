package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.portlet.IPortletPage;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddWidgetDialog;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.PortalLayout;
import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.WidgetCreator;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.grids.ChartOfAccountsListGrid;

public class BankingSectionHomeView extends BaseHomeView implements IPortletPage{

	ArrayList<String> addablePortletList = new ArrayList<String>();
	private BankingSectionHomeView bankingSectionView = null;
	private PortalLayout portalLayout;
	private String bankingSectionPreference;
	private String[] widgetOnSectionPage;

	private Portlet[] portlet;

	private WidgetCreator creator;
	private String[] secondColumn;
	ChartOfAccountsListGrid accounts;
	private List<ClientAccount> listOfAccounts;

	private String[] firstColumn;

	public BankingSectionHomeView() {
		bankingSectionView = this;
		// bankingSectionPreference = FinanceApplication.getUser() != null ?
		// FinanceApplication
		// .getUser().getUserPreferences()
		// .getBankingSectionViewPreferences()
		// : "";
	}

	@Override
	public void init() {
		getLeftLayout().add(createControl());
		//setSize("100%", "100%");
	}

	private StyledPanel createControl() {
		creator = new WidgetCreator();
		StyledPanel addWidgetLinkLayout = new StyledPanel("addWidgetLinkLayout");
		// addWidgetLinkLayout.setHeight(20);

		LinkItem addWidgetLink = new LinkItem();
		addWidgetLink.setLinkTitle(messages.addWidget());
		addWidgetLink.setShowTitle(false);
		// addWidgetLink.setAlign(Alignment.RIGHT);

		addWidgetLink.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				new AddWidgetDialog(bankingSectionView);
			}

		});

		DynamicForm form = new DynamicForm("form");
		form.add(addWidgetLink);

		addWidgetLinkLayout.add(form);

		portalLayout = new PortalLayout(this, 2);
		// portalLayout.setWidth100();
		// portalLayout.setHeight100();

		String[] portletArray = bankingSectionPreference.split(",");
		if (portletArray.length > 2) {
			widgetOnSectionPage = portletArray;
			portlet = new Portlet[portletArray.length];
			for (int i = 0; i < portletArray.length; i++) {
				// final int index = i;
				if (portletArray[i].equals("")) {

				} else {
					// portlet[i] = creator.getWidgetByName(portletArray[i]);
					// portlet[i].addCloseHandler(new CloseHandler() {
					//
					// @Override
					// public void onClose(CloseEvent event) {
					// addablePortletList.add(portlet[index].getName());
					// portlet[index].hide();
					//
					// }
					//
					// });
					// portalLayout.addPortlet(portlet[i]);
				}
			}

		} else {
			firstColumn = portletArray[0].split("~");
			if (portletArray.length > 1) {
				secondColumn = portletArray[1].split("~");
				widgetOnSectionPage = new String[firstColumn.length
						+ secondColumn.length];
			} else {
				widgetOnSectionPage = new String[firstColumn.length];
			}

			int widgetIndex = 0;
			for (int i = 0; i < firstColumn.length; i++) {
				widgetOnSectionPage[widgetIndex] = firstColumn[i];
				widgetIndex++;
			}
			if (portletArray.length > 1) {
				for (int i = 0; i < secondColumn.length; i++) {

					widgetOnSectionPage[widgetIndex] = secondColumn[i];
					widgetIndex++;
				}
			}
			if (portletArray.length > 1) {
				portlet = new Portlet[firstColumn.length + secondColumn.length];
			} else {
				portlet = new Portlet[firstColumn.length];
			}
			for (int i = 0; i < firstColumn.length; i++) {
				// final int index = i;
				if (firstColumn[i].equals("")) {

				} else {
					// portlet[i] = creator.getWidgetByName(firstColumn[i]);
					// portlet[i].addCloseHandler(new CloseHandler() {
					//
					// @Override
					// public void onClose(CloseEvent event) {
					// addablePortletList.add(portlet[index].getName());
					// portlet[index].hide();
					//
					// }
					//
					// });
					// portalLayout.addPortlet(portlet[i], 0);
				}
			}

			if (portletArray.length > 1) {
				for (int i = 0; i < secondColumn.length; i++) {
					// final int index = i + firstColumn.length;
					if (secondColumn[i].equals("")) {

					} else {
						// portlet[index] = creator
						// .getWidgetByName(secondColumn[i]);
						// portlet[index]
						// .addCloseClickHandler(new CloseClickHandler() {
						//
						// public void onCloseClick(
						// CloseClientEvent event) {
						// addablePortletList.add(portlet[index]
						// .getName());
						// portlet[index].destroy();
						//
						// }
						//
						// });
						// portlet[index]
						// .addCloseHandler(new CloseHandler<PopupPanel>() {
						//
						// @Override
						// public void onClose(
						// CloseEvent<PopupPanel> event) {
						// addablePortletList.add(portlet[index]
						// .getName());
						// portlet[index].hide();
						//
						// }
						// });
						// portalLayout.addPortlet(portlet[index], 1);
					}
				}
			}
		}

		getAddableWidgets(widgetOnSectionPage);
		accounts = new ChartOfAccountsListGrid(false);
		accounts.init();
		StyledPanel leftLayout = new StyledPanel("leftLayout");
		listOfAccounts = Accounter.getCompany().getAccounts();
		filterList(true);
		// leftLayout.add(addWidgetLinkLayout);
		leftLayout.add(portalLayout);
		leftLayout.add(accounts);
		return leftLayout;

	}

	public void getAddableWidgets(String[] widgetOnSectionPage) {
		String[] totalWidget = { messages.bankingSummary(),
				messages.checkIssued(), messages.deposit(),
				messages.fundTransfered(), messages.creditCardCharges() };
		boolean isAvailable = false;

		for (int i = 0; i < totalWidget.length; i++) {
			for (int k = 0; k < widgetOnSectionPage.length; k++) {
				if (totalWidget[i].equals(widgetOnSectionPage[k])) {
					isAvailable = true;
					break;
				} else {
					isAvailable = false;
				}

			}
			if (!isAvailable) {
				addablePortletList.add(totalWidget[i]);
				isAvailable = false;
			}
		}

	}

	public PortalLayout getPortalLayout() {
		return portalLayout;
	}

	public void setPortalLayout(PortalLayout portalLayout) {
		this.portalLayout = portalLayout;
	}

	public ArrayList<String> getAddablePortletList() {
		return addablePortletList;
	}

	public void setAddablePortletList(ArrayList<String> addablePortletList) {
		this.addablePortletList = addablePortletList;
	}

	protected void filterList(boolean isActive) {
		accounts.removeAllRecords();
		for (ClientAccount account : listOfAccounts) {
			if (isActive) {
				if (account.getIsActive() == true)
					accounts.addData(account);
			} else if (account.getIsActive() == false) {
				accounts.addData(account);
			}

		}

		accounts.sort(12, false);

	}

	@Override
	public void fitToSize(int height, int width) {
//		this.accounts.setHeight((height - 130) + "px");

	}

	@Override
	public void setFocus() {

	}
}
