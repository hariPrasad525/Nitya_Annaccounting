package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.ui.settings.RolePermissions;
import com.nitya.accounter.web.client.ui.vendors.PayBillsAction;

public class WhoIOwePortlet extends Portlet {
	private DashboardOweGrid grid;

	public WhoIOwePortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.whoIOwe(), "", "100%");
		this.getElement().setId("WhoIOwePortlet");
	}

	@Override
	public void createBody() {

		AsyncCallback<ArrayList<ClientPayee>> callback = new AsyncCallback<ArrayList<ClientPayee>>() {

			@Override
			public void onSuccess(ArrayList<ClientPayee> result) {
				grid = new DashboardOweGrid();
				grid.init();
				if (result != null && !(result.isEmpty())) {
					grid.setRecords(result);
				} else {
					grid.addEmptyMessage(messages.noRecordsToShow());
				}
				body.add(grid);
				if (Accounter.getUser().getPermissions()
						.getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES
						&& getPreferences().isKeepTrackofBills()) {
					createLink();
				}
				completeInitialization();
			}

			@Override
			public void onFailure(Throwable caught) {
				completeInitialization();
				// TODO Auto-generated method stub

			}
		};
		Accounter.createHomeService().getOwePayees(TYPE_I_OWE, callback);
	}

	private void createLink() {
		StyledPanel linkPanel = new StyledPanel("linkPanel");
		Anchor payBillLink = new Anchor(messages.addaNew(messages.payBill()));
		payBillLink.addStyleName("portlet_link");
		payBillLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new PayBillsAction().run();
			}
		});
		linkPanel.add(payBillLink);
		body.add(linkPanel);
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
