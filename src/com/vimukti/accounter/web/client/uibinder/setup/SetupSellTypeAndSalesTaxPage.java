/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Administrator
 * 
 */
public class SetupSellTypeAndSalesTaxPage extends AbstractSetupPage {

	private static SetupSellTypeAndSalesTaxPageUiBinder uiBinder = GWT
			.create(SetupSellTypeAndSalesTaxPageUiBinder.class);
	@UiField
	FlowPanel viewPanel;
	@UiField
	RadioButton servicesOnly;
	@UiField
	Label servicesOnlyText;
	@UiField
	RadioButton productsOnly;
	@UiField
	Label productsOnlyText;
	@UiField
	RadioButton both;
	@UiField
	Label bothText;
	@UiField
	FlowPanel sell;

	@UiField
	Label headerLabel;
	@UiField
	RadioButton onepeTransactionRadioButton;
	@UiField
	Label oneperTransactionLabel;
	@UiField
	RadioButton oneperdetaillineRadioButton;
	@UiField
	Label oneperdetaillineLabel;
	@UiField
	CheckBox enableTaxCheckbox;
	@UiField
	Label enableTaxLabel;
	@UiField
	Label taxItemTransactionLabel;
	@UiField
	CheckBox trackCheckbox;
	@UiField
	Label trackLabel;
	@UiField
	FlowPanel hidePanel;
	@UiField
	CheckBox inventoryCheckBox;
	@UiField
	FlowPanel hiddenPanel;
	@UiField
	FlowPanel totalPanel;
	@UiField
	CheckBox warehousesCheckBox;
	@UiField
	Label wareHouseCommentLabel;
	@UiField
	Label inventoryCommentLabel;
	@UiField
	ListBox inventorySchemeListBox;
	@UiField
	CheckBox unitsCheckBox;
	@UiField
	Label inventoryScheme;

	interface SetupSellTypeAndSalesTaxPageUiBinder extends
			UiBinder<Widget, SetupSellTypeAndSalesTaxPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupSellTypeAndSalesTaxPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.whatDoYouSell());

		servicesOnly.setText(messages.services_labelonly());
		servicesOnlyText.setText(messages.servicesOnly());
		productsOnly.setText(messages.products_labelonly());
		productsOnlyText.setText(messages.productsOnly());
		both.setText(messages.bothservicesandProduct_labelonly());
		bothText.setText(messages.bothServicesandProducts());

		trackCheckbox.setText(messages.chargeOrTrackTax());
		trackLabel.setText(messages.descChrageTrackTax());
		taxItemTransactionLabel.setText(messages.taxtItemTransaction());
		onepeTransactionRadioButton.setText(messages.onepertransaction());
		oneperTransactionLabel.setText(messages.oneperDescription());
		oneperdetaillineRadioButton.setText(messages.oneperdetailline());
		oneperdetaillineLabel.setText(messages.oneperDetailDescription());
		enableTaxCheckbox.setText(messages.enableTracking());
		enableTaxLabel.setText(messages.enableTrackingDescription());

		inventorySchemeListBox.addItem(messages.firstInfirstOut());
		inventorySchemeListBox.addItem(messages.lastInfirstOut());
		inventorySchemeListBox.addItem(messages.average());
		inventoryScheme.setText(messages.inventoryScheme());
		unitsCheckBox.setText(messages.enableUnits());

		trackCheckbox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hidePanel.setVisible(trackCheckbox.getValue());

			}
		});

		inventoryCheckBox.setVisible(Accounter
				.hasPermission(Features.INVENTORY));
		inventoryCheckBox.setText(messages.enableInventoryTracking());
		inventoryCommentLabel.setText(messages.inventoryTrackingComment());
		warehousesCheckBox.setText(messages.haveMultipleWarehouses());
		wareHouseCommentLabel.setText(messages.multipleWarehousesComment());

		servicesOnly.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				inventoryCheckBox.setValue(false);
				warehousesCheckBox.setValue(false);
				totalPanel.setVisible(false);

			}
		});
		productsOnly.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				totalPanel.setVisible(productsOnly.getValue());

			}
		});
		both.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				totalPanel.setVisible(both.getValue());

			}
		});

		inventoryCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hiddenPanel.setVisible(inventoryCheckBox.getValue());
				totalPanel.setVisible(inventoryCheckBox.getValue());
			}
		});
	}

	@Override
	public void onLoad() {
		boolean sellServices = preferences.isSellServices();
		boolean sellProducts = preferences.isSellProducts();

		if (sellServices && sellProducts) {
			both.setValue(true);
		} else if (sellServices) {
			servicesOnly.setValue(true);
		} else if (sellProducts) {
			productsOnly.setValue(true);
		}

		trackCheckbox.setValue(preferences.isTrackTax());
		hidePanel.setVisible(preferences.isTrackTax());
		enableTaxCheckbox.setValue(preferences.isTrackTax());
		if (preferences.isTaxPerDetailLine())
			oneperdetaillineRadioButton.setValue(true);
		else
			onepeTransactionRadioButton.setValue(true);
		if (sellServices) {
			totalPanel.setVisible(false);
		}
		if ((sellProducts && sellServices) || sellProducts) {
			totalPanel.setVisible(true);
		}
		if (preferences.isInventoryEnabled()) {
			inventoryCheckBox.setValue(true);
			hiddenPanel.setVisible(true);
			warehousesCheckBox.setValue(preferences.iswareHouseEnabled());
			inventoryCheckBox.setValue(true);
			hiddenPanel.setVisible(true);
			unitsCheckBox.setVisible(true);
			// for set the inventory schema value to option
			inventorySchemeListBox.setSelectedIndex(preferences
					.getActiveInventoryScheme() - 1);
		} else {
			hiddenPanel.setVisible(false);
			inventoryCommentLabel.setVisible(false);
		}
	}

	public void onSave() {
		if (servicesOnly.getValue()) {
			preferences.setSellServices(true);
			preferences.setSellProducts(false);
		} else if (productsOnly.getValue()) {
			preferences.setSellProducts(true);
			preferences.setSellServices(false);
		} else if (both.getValue()) {
			preferences.setSellServices(true);
			preferences.setSellProducts(true);
		}

		preferences.setTaxTrack(trackCheckbox.getValue());
		preferences.setTaxPerDetailLine(oneperdetaillineRadioButton.getValue());
		preferences.setTrackPaidTax(enableTaxCheckbox.getValue());
		preferences.setInventoryEnabled(inventoryCheckBox.getValue());
		if (inventoryCheckBox.getValue()) {
			preferences.setwareHouseEnabled(warehousesCheckBox.getValue());
			preferences.setUnitsEnabled(unitsCheckBox.getValue());
			// for inventory scheme options
			if (inventorySchemeListBox.getSelectedIndex() != -1) {
				preferences.setActiveInventoryScheme((inventorySchemeListBox
						.getSelectedIndex() + 1));
			}
		}
	}

	@Override
	protected boolean validate() {
		/*
		 * if ((!(servicesOnly.getValue() || productsOnly.getValue() || both
		 * .getValue()))) { Accounter.showError(accounterMessages
		 * .pleaseEnter(messages.details())); return false; } else
		 */if (!(servicesOnly.getValue() || productsOnly.getValue() || both
				.getValue())) {
			Accounter.showMessage(messages.whatDoYouSell());
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getViewName() {
		return messages.whatDoYouSell();
	}
	
	@Override
	public boolean canShow() {
		return !isSkip && !isPayrollSetup;
	}

}
