package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.ui.core.AbstractView;

/**
 * 
 * @author ganesh.v
 * 
 */

public class SalesDetailesView extends AbstractView {

	private FlexTable flexTable;
	private FlexCellFormatter cellFormatter;
	// private ScrollPanel panel;
	private HTML billingAdress;
	private HTML shippingAdress;
	private HTML orderNumberField;
	private HTML customerNumberField;
	private HTML dueDateField;
	private HTML statusField;
	public ItemsGrid itemsGrid;

	public SalesDetailesView() {
		super();

		this.setStyleName("selectedview");
		// init();
	}

	/**
	 * here all guicomponents will create
	 */
	@Override
	public void init() {
		// this.setWidth("300px");
		// this.setHeight("100%");
		flexTable = new FlexTable();
		// flexTable.setWidth(345 + "px");
		// flexTable.setHeight("100%");
		// int offsetHeight = Window.getClientHeight();
		// panel = new ScrollPanel();
		// panel.setHeight("100%");
		// panel.setWidth(345 + "px");
		cellFormatter = flexTable.getFlexCellFormatter();
		createFields();
		// panel.add(flexTable);
		this.add(flexTable);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	/**
	 * here side view fileds will be create and inserte in flextable at
	 * particular locations
	 */
	public void createFields() {

		// Window.addWindowResizeListener(new WindowResizeListener() {
		//
		// @Override
		// public void onWindowResized(int width, int height) {
		// panel.setHeight((Window.getClientHeight() - 250) + "px");
		// }
		// });

		Label headerLabel = new Label();
		// headerLabel.setHeight("22px");
		headerLabel.setText(messages.salesOrderDetails());
		headerLabel.setStylePrimaryName("headerlabel");
		add(headerLabel);

		flexTable.clear();

		Label orderNumberLabel = new Label();
		orderNumberLabel.setText(messages.orderNumber());
		orderNumberLabel.setStyleName("selectedview_labelstyle");

		orderNumberField = new HTML();

		Label conLabel = new Label();
		conLabel.setText(messages.payeeOrderNumber(Global.get().customer()));
		conLabel.setStyleName("selectedview_labelstyle");

		customerNumberField = new HTML();

		Label dueDate = new Label();
		dueDate.setText(messages.dueDate());
		dueDate.setStyleName("selectedview_labelstyle");

		dueDateField = new HTML();

		Label statusLabel = new Label();
		statusLabel.setText(messages.status());
		statusLabel.setStyleName("selectedview_labelstyle");

		statusField = new HTML();

		Label itemsLabel = new Label();
		itemsLabel.setText(messages.items());
		itemsLabel.setStyleName("selectedview_labelstyle");

		Label billingAddress = new Label();
		billingAddress.setText(messages.billingAddress());
		billingAddress.setStyleName("selectedview_labelstyle");

		Label shippingAddress = new Label();
		shippingAddress.setText(messages.shippingAddress());
		shippingAddress.setStyleName("selectedview_textarea_label");

		billingAdress = new HTML("");
		billingAdress.setStyleName("selectedview_textareadata");

		shippingAdress = new HTML("");
		shippingAdress.setStyleName("selectedview_textareadata");

		itemsGrid = new ItemsGrid(false);
		itemsGrid.init();
		itemsGrid.addEmptyMessage(messages.noProductstoshow());

		flexTable.setWidget(1, 0, orderNumberLabel);
		flexTable.setWidget(2, 0, conLabel);
		flexTable.setWidget(3, 0, dueDate);
		flexTable.setWidget(4, 0, statusLabel);
		flexTable.setWidget(5, 0, itemsLabel);
		flexTable.setWidget(7, 0, billingAddress);
		if (getPreferences().isDoProductShipMents()) {
			flexTable.setWidget(9, 0, shippingAddress);
		}
		flexTable.setWidget(1, 1, orderNumberField);
		flexTable.setWidget(2, 1, customerNumberField);
		flexTable.setWidget(3, 1, dueDateField);
		flexTable.setWidget(4, 1, statusField);
		flexTable.setWidget(6, 0, itemsGrid);
		flexTable.setWidget(8, 0, billingAdress);
		if (getPreferences().isDoProductShipMents()) {
			flexTable.setWidget(10, 0, shippingAdress);
		}
		cellFormatter.setColSpan(6, 0, 2);
		cellFormatter.setColSpan(8, 0, 2);
		cellFormatter.setColSpan(10, 0, 2);

		// createButtonsLayout();
	}

	/**
	 * this method show the empty message when record id not fetched
	 */
	public void setEmptyMessage() {
		flexTable.clear();
		flexTable.setWidget(0, 0,
				new HTML(messages.selectATaskNotetoSeeTheDetails()));
	}

	/**
	 * this method will invoke while changing the selction of object
	 */
	public void refreshView() {
		itemsGrid.clear();
		itemsGrid.addEmptyMessage(messages.noRecordsToShow());
		orderNumberField.setText("");
		customerNumberField.setText("");
		dueDateField.setText("");
		statusField.setText("");
		billingAdress.setHTML("");
		shippingAdress.setHTML("");
	}

	/**
	 * this method will create the address by getting all corresponding addrss
	 * values
	 * 
	 * @param adress
	 * @return
	 */
	public String addressInfo(ClientAddress adress) {

		StringBuffer buffer = new StringBuffer();

		buffer.append(adress.getAddress1() != null ? String.valueOf(adress
				.getAddress1()) : "");
		buffer.append("<br>");
		buffer.append(adress.getStreet() != null ? String.valueOf(adress
				.getStreet()) : "");
		buffer.append("<br>");
		buffer.append(adress.getCity() != null ? String.valueOf(" "
				+ adress.getCity()) : "");
		buffer.append("<br>");
		buffer.append(adress.getStateOrProvinence() != null ? String
				.valueOf(" " + adress.getStateOrProvinence()) : "");
		buffer.append("<br>");
		buffer.append(adress.getZipOrPostalCode() != null ? String.valueOf(" "
				+ adress.getZipOrPostalCode()) : "");
		buffer.append("<br>");
		buffer.append(adress.getCountryOrRegion() != null ? String.valueOf(" "
				+ adress.getCountryOrRegion()) : "");

		return buffer.toString();
	}

	@Override
	public void fitToSize(int height, int width) {

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

}
