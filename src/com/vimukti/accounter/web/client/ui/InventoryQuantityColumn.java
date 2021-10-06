package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.settings.UnitCombo;

public class InventoryQuantityColumn extends
		TextEditColumn<ClientInventoryAssemblyItem> {

	PopupPanel popup = new PopupPanel(true);

	@Override
	public int getWidth() {
		return 150;
	}

	protected String getValue(ClientInventoryAssemblyItem row) {

		ClientItem item = null;
		if (row.getInventoryItem() != 0) {
			item = Accounter.getCompany().getItem(row.getInventoryItem());
		}
		ClientQuantity quantity = getQuantity(row);
		if (item == null) {
			return quantity != null ? String.valueOf(quantity.getValue()) : "";
		} else {
			if (item.getType() == ClientItem.TYPE_INVENTORY_PART
					|| item.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
				StringBuffer data = new StringBuffer();
				data.append(String.valueOf(quantity.getValue()));
				if (getPreferences().isUnitsEnabled()) {
					ClientUnit unit = Accounter.getCompany().getUnitById(
							quantity.getUnit());
					data.append(" ");
					if (unit != null) {
						data.append(unit.getType());
					}
				}
				if (getPreferences().iswareHouseEnabled()) {
					data.append(" (W: ");
					ClientWarehouse warehouse = Accounter.getCompany()
							.getWarehouse(row.getWarehouse());
					if (warehouse != null)
						data.append(warehouse.getWarehouseCode());
					data.append(")");
				}

				return data.toString();
			} else {
				if (quantity != null)
					return String.valueOf(quantity.getValue());
				else
					return "";
			}
		}
	}

	protected ClientQuantity getQuantity(ClientInventoryAssemblyItem row) {
		return row.getQuantity();
	}

	@Override
	public IsWidget getWidget(RenderContext<ClientInventoryAssemblyItem> context) {
		final IsWidget widget = super.getWidget(context);
		final ClientInventoryAssemblyItem row = context.getRow();
		if (widget instanceof TextBox && getPreferences().iswareHouseEnabled()
				|| getPreferences().isUnitsEnabled()) {
			((TextBox) widget).addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					if (row.getInventoryItem() == 0) {
						return;
					}
					ClientItem item = Accounter.getCompany().getItem(
							row.getInventoryItem());
					if (item != null
							&& (item.getType() == ClientItem.TYPE_INVENTORY_PART || item
									.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY)) {
						showPopUp(row);
						((TextBox) widget).setFocus(false);
					}
				}
			});
		}
		return widget;
	}

	private void showPopUp(final ClientInventoryAssemblyItem row) {
		if (popup.isShowing()) {
			return;
		}
		popup.clear();
		FlexTable table = new FlexTable();
		Label valueLabel = new Label(messages.value());
		Label unitLabel = new Label(messages.unit());
		Label wareHouseLabel = new Label(messages.wareHouse());

		ClientUnit unit = Accounter.getCompany().getUnitById(
				row.getQuantity().getUnit());
		ClientWarehouse wareHouse = Accounter.getCompany().getWarehouse(
				row.getWarehouse());
		ClientItem item = Accounter.getCompany()
				.getItem(row.getInventoryItem());
		final TextBox valueBox = new TextBox();
		valueBox.setFocus(true);
		valueBox.setText(String.valueOf(row.getQuantity().getValue()));
		final UnitCombo unitBox = new UnitCombo("");
		if (item != null) {
			ClientMeasurement measurement = Accounter.getCompany()
					.getMeasurement(item.getMeasurement());
			unitBox.initCombo(measurement.getUnits());
		}
		unitBox.setSelected(unit.getName());
		unitBox.setShowTitle(false);
		final WarehouseCombo whCombo = new WarehouseCombo("");
		whCombo.setComboItem(wareHouse);
		whCombo.setShowTitle(false);
		whCombo.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientWarehouse>() {

			@Override
			public void selectedComboBoxItem(ClientWarehouse selectItem) {
				if (!popup.isShowing()) {
					if (selectItem != null) {
						row.setWareHouse(selectItem.getID());
						getTable().update(row);
					}
				}
			}
		});

		table.setWidget(0, 0, valueLabel);
		table.setWidget(1, 0, valueBox);
		if (getPreferences().isUnitsEnabled()) {
			table.setWidget(0, 1, unitLabel);
			table.setWidget(1, 1, unitBox.getMainWidget());
			table.getCellFormatter().addStyleName(1, 1, "quantity_unit_width");
		}
		if (getPreferences().iswareHouseEnabled()) {
			table.setWidget(0, 2, wareHouseLabel);
			table.setWidget(1, 2, whCombo.getMainWidget());
		}
		popup.add(table);
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {

				String value = valueBox.getValue();
				ClientUnit unit = unitBox.getSelectedValue();
				if (value.isEmpty()) {
					value = "1";
				}
				ClientQuantity quantity = row.getQuantity();
				try {
					quantity.setValue(DataUtils.getAmountStringAsDouble(value));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (unit != null)
					quantity.setUnit(unit.getId());
				setQuantity(row, quantity);
				if (whCombo.getSelectedValue() != null)
					row.setWareHouse(whCombo.getSelectedValue().getID());
				getTable().update(row);
			}
		});
		Widget widget = getTable().getWidget(row, this);
		popup.setAutoHideEnabled(true);
		popup.addStyleName("quantityPopup");
		popup.showRelativeTo(widget);
	}

	public void setQuantity(ClientInventoryAssemblyItem row,
			ClientQuantity quantity) {
		if (quantity != null) {
			row.setQuantity(quantity);
			ClientCompany company = Accounter.getCompany();
			ClientUnit unitById = company.getUnitById(row.getQuantity()
					.getUnit());
			double lt = 0;
			double unitPrice = 0;
			if (row.getInventoryItem() != 0) {
				unitPrice = Accounter.getCompany()
						.getItem(row.getInventoryItem()).getAverageCost();
			}

			if (unitById != null) {
				ClientQuantity convertToDefaultUnit = row.getQuantity()
						.convertToDefaultUnit(unitById);
				unitPrice = (unitPrice * convertToDefaultUnit.getValue())
						/ quantity.getValue();
			}
			lt = unitPrice * quantity.getValue();
			row.setLineTotal(lt);
			getTable().update(row);
		}
	}

	@Override
	protected String getColumnName() {
		return messages.quantity();
	}

	@Override
	protected void setValue(ClientInventoryAssemblyItem row, String value) {
		try {
			if (value.isEmpty()) {
				value = "1";
			}
			ClientItem item = Accounter.getCompany().getItem(
					row.getInventoryItem());
			ClientQuantity quantity = row.getQuantity();
			if (item != null
					&& (item.getType() == ClientItem.TYPE_INVENTORY_PART || quantity == null)) {
				ClientCompany company = Accounter.getCompany();
				if (quantity == null) {
					quantity = new ClientQuantity();
				}
				if (quantity.getUnit() == 0) {
					ClientUnit defaultUnit = company.getMeasurement(
							item.getMeasurement()).getDefaultUnit();
					quantity.setUnit(defaultUnit.getId());
				}
				if (row.getWarehouse() == 0) {
					ClientWarehouse warehouse = company.getWarehouse(company
							.getDefaultWarehouse());
					row.setWareHouse(warehouse.getID());
				}
			}
			if (quantity != null) {
				quantity.setValue(DataUtils.getAmountStringAsDouble(JNSI
						.getCalcultedAmount(value)));
			}
			setQuantity(row, quantity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getValueAsString(ClientInventoryAssemblyItem row) {
		return "(Qty :" + getValue(row).toString() + ")";
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}
}
