package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Lists.ClientTDSInfo;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.VendorCombo;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.forms.DateItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TDSVendorsListView extends BaseView<ClientTDSInfo> implements
		AsyncCallback<ArrayList<ClientTDSInfo>> {

	private DateItem fromDate;
	private DateItem toDate;

	Label label;
	ArrayList<ClientTDSInfo> clientTDSInfos;

	private VendorCombo vendorCombo;
	private Button updateButton;
	private TDSVendorsTable grid;

	private boolean isTdsView;

	@Override
	public void init() {
		super.init();
		this.getElement().setId("TDSVendorsListView");
		createControls1();
	}

	public TDSVendorsListView(boolean isTdsView) {

		this.isTdsView = isTdsView;
	}

	private void createControls1() {
		if (saveAndCloseButton != null)
			this.saveAndCloseButton.setVisible(false);
		if (saveAndNewButton != null)
			this.saveAndNewButton.setText(messages.tdsfiling());

		label = new Label();
		label.removeStyleName("gwt-style");
//		label.setWidth("100%");
		label.addStyleName("label-title");
		label.setText(messages.tdsVendorsList());
		this.fromDate = new DateItem(messages.from(), "fromDate");

		this.toDate = new DateItem(messages.to(), "toDate");
		this.toDate.setEnteredDate(new ClientFinanceDate());

		this.fromDate.setEnteredDate(new ClientFinanceDate());
		this.vendorCombo = new VendorCombo(Global.get().messages()
				.payeeName(Global.get().Vendor()), true);
		vendorCombo.setValue(new String((messages.all())));

		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						filterListByVendor(selectItem);

					}

				});

		this.updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				filterListByDate();

			}

		});

		this.grid = new TDSVendorsTable();
		DynamicForm topForm = new DynamicForm("topForm");
		// topForm.setIsGroup(true);
		// topForm.setGroupTitle(messages.top());
		// topForm.setNumCols(6);
		topForm.add(fromDate, toDate);
		// topForm.setWidth("100%");

		Accounter.createHomeService().getPayBillsByTDS(this);

		StyledPanel topLayout = new StyledPanel("topLayout");
		topLayout.add(topForm);
		topLayout.add(updateButton);

		StyledPanel mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(topLayout);
		mainPanel.add(label);
		mainPanel.add(vendorCombo);
		mainPanel.add(grid);
		grid.getElement().getParentElement()
				.addClassName("recounciliation_grid");

		this.add(mainPanel);

	}

	private void filterListByVendor(ClientVendor selectItem) {

		List<ClientTDSInfo> cl = new ArrayList<ClientTDSInfo>();
		for (ClientTDSInfo clientTDSInfo : clientTDSInfos) {
			if (selectItem.getID() == clientTDSInfo.getVendor().getID()) {
				cl.add(clientTDSInfo);
			}

		}

		grid.setData(cl);

	}

	@Override
	protected String getViewTitle() {
		return messages.payees(Global.get().Vendors());
	}

	private void filterListByDate() {

		List<ClientTDSInfo> cl = new ArrayList<ClientTDSInfo>();
		for (ClientTDSInfo clientTDSInfo : clientTDSInfos) {
			if ((clientTDSInfo.getDate().after(fromDate.getDate()))
					&& (clientTDSInfo.getDate().before(toDate.getDate()))) {
				cl.add(clientTDSInfo);
			}

		}
		grid.setData(cl);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(ArrayList<ClientTDSInfo> result) {

		if (result != null) {
			clientTDSInfos = result;
			grid.setData(result);
		}

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}
}
