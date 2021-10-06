/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

/**
 * @author vimukti36
 * 
 */
public class ProductAndServicesOption extends AbstractPreferenceOption {

	LabelItem headerLabel;
	StyledPanel viewPanel;
	LabelItem servicesOnlyText;
	SelectCombo servicesProductsTypeCombo;
	LabelItem productsOnlyText;
	LabelItem bothText;
	CheckboxItem warehousesCheckBox;
	CheckboxItem inventoryCheckBox;
	CheckboxItem unitsCheckBox;

	StyledPanel invetoryPanel;
	StyledPanel hiddenPanel;
	StyledPanel subpanel;
	SelectCombo inventorySchemeListBox;

	public String[] inventorySchemeList = { messages.firstInfirstOut(),
			messages.lastInfirstOut(), messages.average() };
	public String[] serviceOrProductList = { messages.products_labelonly(),
			messages.services_labelonly(),
			messages.bothservicesandProduct_labelonly() };

	public static final int INVENTORY_SCHME_FIFO = 1;
	public static final int INVENTORY_SCHME_LIFO = 2;
	public static final int INVENTORY_SCHME_AVERAGE = 3;

	interface ProductAndServicesOptionUiBinder extends
			UiBinder<Widget, ProductAndServicesOption> {
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
	public ProductAndServicesOption() {
		super("");
		createControls();
		initData();
	}

	public void createControls() {

		headerLabel = new LabelItem(messages.whatDoYouSell(), "header");
		// servicesOnlyText.setText(constants.whatDoYouSell());
		servicesOnlyText = new LabelItem(messages.products_labelonly() + " : "
				+ messages.productsOnly(), "servicesOnlyText");
		productsOnlyText = new LabelItem(messages.services_labelonly() + " : "
				+ messages.servicesOnly(), "productsOnlyText");
		bothText = new LabelItem(messages.bothservicesandProduct_labelonly()
				+ " : " + messages.bothServicesandProducts(), "bothText");

		servicesProductsTypeCombo = new SelectCombo(messages.type());
		for (int i = 0; i < serviceOrProductList.length; i++) {
			servicesProductsTypeCombo.addItem(serviceOrProductList[i]);
		}
		servicesProductsTypeCombo.setComboItem(serviceOrProductList[1]);
		subpanel = new StyledPanel("sell_panel");
		subpanel.add(servicesOnlyText);
		subpanel.add(productsOnlyText);
		subpanel.add(bothText);
		subpanel.add(servicesProductsTypeCombo);

		inventoryCheckBox = new CheckboxItem(messages.inventoryTracking(),
				"inventoryCheckBox");
		inventoryCheckBox.setVisible(Accounter
				.hasPermission(Features.INVENTORY));

		warehousesCheckBox = new CheckboxItem(
				messages.haveMultipleWarehouses(), "warehousesCheckBox");
		unitsCheckBox = new CheckboxItem(messages.enableUnits(),
				"unitsCheckBox");

		inventorySchemeListBox = new SelectCombo(messages.inventoryScheme());
		for (int i = 0; i < inventorySchemeList.length; i++) {
			inventorySchemeListBox.addComboItem(inventorySchemeList[i]);
		}
		inventorySchemeListBox.setComboItem(inventorySchemeList[0]);
		hiddenPanel = new StyledPanel("productAndServocehiddenPanel");
		hiddenPanel.add(warehousesCheckBox);
		hiddenPanel.add(unitsCheckBox);
		hiddenPanel.add(inventorySchemeListBox);

		servicesProductsTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(messages.services_labelonly())) {
							inventoryCheckBox.setValue(false);
							warehousesCheckBox.setValue(false);
							unitsCheckBox.setValue(false);
							invetoryPanel.setVisible(false);
						} else if (selectItem.equals(messages
								.products_labelonly())) {
							invetoryPanel.setVisible(true);
							hiddenPanel.setVisible(inventoryCheckBox.getValue());
						} else if (selectItem.equals(messages
								.bothservicesandProduct_labelonly())) {
							invetoryPanel.setVisible(true);
							hiddenPanel.setVisible(inventoryCheckBox.getValue());
						}

					}
				});

		inventoryCheckBox.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				hiddenPanel.setVisible(inventoryCheckBox.getValue());
				if (inventoryCheckBox.getValue()) {
					// for set the inventory schema value to option
					inventorySchemeListBox
							.setSelectedItem(getCompanyPreferences()
									.getActiveInventoryScheme() - 1);
				}
			}
		});
		invetoryPanel = new StyledPanel("inventorysuboption");
		invetoryPanel.add(inventoryCheckBox);
		add(subpanel);
		add(invetoryPanel);
		invetoryPanel.add(hiddenPanel);
	}

	@Override
	public String getTitle() {
		return messages.productAndServices();
	}

	@Override
	public void onSave() {

		if (servicesProductsTypeCombo.getSelectedValue().equals(
				messages.services_labelonly())) {
			getCompanyPreferences().setSellServices(true);
			getCompanyPreferences().setSellProducts(false);
		}
		if (servicesProductsTypeCombo.getSelectedValue().equals(
				messages.products_labelonly())) {
			getCompanyPreferences().setSellProducts(true);
			getCompanyPreferences().setSellServices(false);
		}
		if (servicesProductsTypeCombo.getSelectedValue().equals(
				messages.bothservicesandProduct_labelonly())) {
			getCompanyPreferences().setSellServices(true);
			getCompanyPreferences().setSellProducts(true);
		}
		getCompanyPreferences().setInventoryEnabled(
				inventoryCheckBox.getValue());
		if (inventoryCheckBox.getValue()) {
			getCompanyPreferences().setwareHouseEnabled(
					warehousesCheckBox.getValue());
			getCompanyPreferences().setUnitsEnabled(unitsCheckBox.getValue());
			// for inventory scheme options
			if (inventorySchemeListBox.getSelectedIndex() != -1) {
				getCompanyPreferences().setActiveInventoryScheme(
						(inventorySchemeListBox.getSelectedIndex() + 1));
			}
		}
	}

	@Override
	public String getAnchor() {
		return messages.productAndServices();
	}

	@Override
	public void initData() {

		boolean sellServices = getCompanyPreferences().isSellServices();
		if (sellServices) {
			servicesProductsTypeCombo
					.setSelected(messages.services_labelonly());
			invetoryPanel.setVisible(false);
		}

		boolean sellProducts = getCompanyPreferences().isSellProducts();

		if (sellProducts) {
			servicesProductsTypeCombo
					.setSelected(messages.products_labelonly());
			invetoryPanel.setVisible(true);
		}

		if (sellServices && sellProducts) {
			servicesProductsTypeCombo.setSelected(messages
					.bothservicesandProduct_labelonly());
			invetoryPanel.setVisible(true);
		}

		boolean inventoryEnabled = getCompanyPreferences().isInventoryEnabled();
		hiddenPanel.setVisible(inventoryEnabled);
		inventoryCheckBox.setValue(inventoryEnabled);
		if (inventoryEnabled) {
			warehousesCheckBox.setValue(getCompanyPreferences()
					.iswareHouseEnabled());
			unitsCheckBox.setValue(getCompanyPreferences().isUnitsEnabled());
			// for set the inventory schema value to option
			inventorySchemeListBox.setSelectedItem(getCompanyPreferences()
					.getActiveInventoryScheme() - 1);
		}
	}

}
