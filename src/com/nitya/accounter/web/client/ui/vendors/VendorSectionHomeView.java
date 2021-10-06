package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.PayeeList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.portlet.IPortletPage;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.BaseHomeView;
import com.nitya.accounter.web.client.ui.PortalLayout;
import com.nitya.accounter.web.client.ui.Portlet;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.WidgetCreator;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LinkItem;
import com.nitya.accounter.web.client.ui.grids.VendorListGrid;

public class VendorSectionHomeView extends BaseHomeView implements IPortletPage {

	ArrayList<String> addablePortletList = new ArrayList<String>();

	private VendorSectionHomeView vendorSectionView = null;
	private PortalLayout portalLayout;

	private String vendorSectionPreference;
	private String[] widgetOnSectionPage;

	private Portlet[] portlet;

	private WidgetCreator creator;
	private String[] secondColumn;

	private String[] firstColumn;

	public VendorSectionHomeView() {
		vendorSectionView = this;
		// vendorSectionPreference = FinanceApplication.getUser()
		// .getUserPreferences().getVendorSectionViewPreferences();
	}

	@Override
	public void init() {
		payeeGrid = new VendorListGrid(false);
		payeeGrid.init();
		super.init();
		// setSize("100%", "100%");

	}

	private void createControl() {
		creator = new WidgetCreator();

		StyledPanel addWidgetLinkLayout = new StyledPanel("addWidgetLinkLayout");
		// addWidgetLinkLayout.setHeight(20);

		LinkItem addWidgetLink = new LinkItem();
		addWidgetLink.setLinkTitle(messages.addWidget());
		addWidgetLink.setShowTitle(false);
		// addWidgetLink.setAlign(Alignment.RIGHT);

		addWidgetLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// new AddWidgetDialog(vendorSectionView);
			}

		});

		DynamicForm form = new DynamicForm("form");
		form.add(addWidgetLink);

		addWidgetLinkLayout.add(form);

		portalLayout = new PortalLayout(this, 2);
		// portalLayout.setWidth100();
		// portalLayout.setHeight100();

		String[] portletArray = { "" };// vendorSectionPreference.split(",");
		if (portletArray.length > 2) {
			widgetOnSectionPage = portletArray;
			portlet = new Portlet[portletArray.length];
			for (int i = 0; i < portletArray.length; i++) {

				final int index = i;
				if (portletArray[i].equals("")) {

				} else {
					// portlet[i] = creator.getWidgetByName(portletArray[i]);
					// portlet[i].addCloseHandler(new CloseHandler() {
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

				final int index = i;
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

					final int index = i + firstColumn.length;
					if (secondColumn[i].equals("")) {

					} else {
						// portlet[index] = creator
						// .getWidgetByName(secondColumn[i]);
						// portlet[index].addCloseHandler(new CloseHandler() {
						//
						// @Override
						// public void onClose(CloseEvent event) {
						// addablePortletList
						// .add(portlet[index].getName());
						// portlet[index].hide();
						//
						// }
						//
						// });
						// portalLayout.addPortlet(portlet[index], 1);
					}
				}
			}
		}
		getAddableWidgets(widgetOnSectionPage);
	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_VENDOR,
				true, start, length, false,
				new AccounterAsyncCallback<PaginationList<PayeeList>>() {

					@Override
					public void onResultSuccess(PaginationList<PayeeList> result) {
						payeeGrid.setRecords(result);
						payeeGrid.sort(12, false);
						updateRecordsCount(result.getStart(),
								payeeGrid.getTableRowCount(),
								result.getTotalCount());
					}

					@Override
					public void onException(AccounterException caught) {
					}
				});
	}

	public void getAddableWidgets(String[] widgetOnSectionPage) {
		String[] totalWidget = { messages.newPayee(Global.get().Vendor()),
				messages.itemPurchase(), messages.billPaid(),
				messages.cashPurchase() };

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

	@Override
	public void fitToSize(int height, int width) {
		// if (UIUtils.isMSIEBrowser())
		// this.listGrid.setHeight((height - 140) + "px");
		// else
		// this.payeeGrid.setHeight((height - 130) + "px");

	}

	public void setPrevoiusOutput(Object preObject) {
		Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_VENDOR,
				true, 0, 0, false,
				new AccounterAsyncCallback<PaginationList<PayeeList>>() {

					@Override
					public void onResultSuccess(PaginationList<PayeeList> result) {
						payeeGrid.clear();
						payeeGrid.addRecords(result);
						payeeGrid.sort(12, false);
					}

					@Override
					public void onException(AccounterException caught) {
					}
				});
	}
}
