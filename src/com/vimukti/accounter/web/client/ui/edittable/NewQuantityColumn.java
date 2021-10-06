package com.vimukti.accounter.web.client.ui.edittable;

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
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.JNSI;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.UnitCombo;

public class NewQuantityColumn extends TextEditColumn<ClientTransactionItem> {

	PopupPanel popup = new PopupPanel(true);
	private boolean isUsedInPurcaseTransaction;

	public NewQuantityColumn(boolean isUsingInPurchaseTransaction) {
		this.isUsedInPurcaseTransaction = isUsingInPurchaseTransaction;
	}

	@Override
	public int getWidth() {
		return 80;
	}

	protected String getValue(ClientTransactionItem row) {
		ClientItem item = Accounter.getCompany().getItem(row.getItem());
		if (item == null) {
			ClientQuantity quantity = row.getQuantity();
			return quantity != null ? String.valueOf(quantity.getValue()) : "";
		} else {
			if (item.getType() == ClientItem.TYPE_INVENTORY_PART
					|| item.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
				StringBuffer data = new StringBuffer();
				data.append(String.valueOf(row.getQuantity().getValue()));

				if (isUnitsEnabled()) {
					ClientUnit unit = Accounter.getCompany().getUnitById(
							row.getQuantity().getUnit());
					data.append(" ");
					if (unit != null) {
						data.append(unit.getType());
					}
				}
				if (isMultipleWarehouseEnabled()) {
					data.append(" (W: ");
					ClientWarehouse warehouse = Accounter.getCompany()
							.getWarehouse(row.getWareHouse());
					if (warehouse != null)
						data.append(warehouse.getWarehouseCode());
					data.append(")");
				}

				return data.toString();
			} else {
				ClientQuantity value = row.getQuantity();
				if (value != null)
					return String.valueOf(value.getValue());
				else
					return "";
			}
		}
	}

	protected boolean isMultipleWarehouseEnabled() {
		return getPreferences().iswareHouseEnabled();
	}

	@Override
	public IsWidget getWidget(RenderContext<ClientTransactionItem> context) {
		final IsWidget widget = super.getWidget(context);
		final ClientTransactionItem row = context.getRow();
		// Checking the units and wareHouses is enabled or not.
		if (widget instanceof TextBox && isMultipleWarehouseEnabled()
				|| isUnitsEnabled()) {
			((TextBox) widget).addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					ClientItem item = Accounter.getCompany().getItem(
							row.getItem());
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

	protected ClientQuantity getQuantity(ClientTransactionItem row) {
		return row.getQuantity();
	}

	private void showPopUp(final ClientTransactionItem row) {
		if (popup.isShowing()) {
			return;
		}
		popup.clear();
		FlexTable table = new FlexTable();
		Label valueLabel = new Label(messages.value());
		Label unitLabel = new Label(messages.unit());
		Label wareHouseLabel = new Label(messages.wareHouse());

		ClientUnit unit = Accounter.getCompany().getUnitById(
				getQuantity(row).getUnit());
		ClientWarehouse wareHouse = Accounter.getCompany().getWarehouse(
				row.getWareHouse());
		ClientItem item = Accounter.getCompany().getItem(row.getItem());
		final TextBox valueBox = new TextBox();
		valueBox.setFocus(true);
		valueBox.setText(String.valueOf(getQuantity(row).getValue()));
		final UnitCombo unitBox = new UnitCombo("");
		if (item != null) {
			ClientMeasurement measurement = Accounter.getCompany()
					.getMeasurement(item.getMeasurement());
			unitBox.initCombo(measurement.getUnits());
		}
		unitBox.setSelected(unit.getName());
		unitBox.setShowTitle(false);
		final WarehouseCombo whCombo = new WarehouseCombo("") {
			@Override
			public void onAddNew() {
				// For hide pop up quantity dialog.
				if (popup.isShowing()) {
					popup.hide();
				}
				super.onAddNew();
			}
		};
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
		if (isUnitsEnabled()) {
			table.setWidget(0, 1, unitLabel);
			table.setWidget(1, 1, unitBox.getMainWidget());
			table.getCellFormatter().addStyleName(1, 1, "quantity_unit_width");
		}
		if (isMultipleWarehouseEnabled()) {
			table.setWidget(0, 2, wareHouseLabel);
			table.setWidget(1, 2, whCombo.getMainWidget());
		}
		popup.add(table);
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {
			boolean isClosed;

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (isClosed) {
					return;
				}
				isClosed = true;
				String value = valueBox.getValue();
				ClientUnit unit = unitBox.getSelectedValue();
				if (value.isEmpty()) {
					value = "1";
				}
				ClientQuantity quantity = new ClientQuantity();
				try {
					quantity.setValue(DataUtils.getAmountStringAsDouble(value));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (unit != null)
					quantity.setUnit(unit.getId());
				ClientItem item = Accounter.getCompany().getItem(row.getItem());

				if (!isUsedInPurcaseTransaction
						&& quantity.compareTo(item.getOnhandQty()) > 0
						&& item.getID() != 0) {
					Accounter.showInformation(messages
							.donnotHaveSufficientInventory(quantity.getValue(),
									item.getOnhandQty().getValue()));
				}

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

	protected boolean isUnitsEnabled() {
		return getPreferences().isUnitsEnabled();
	}

	protected void setQuantity(ClientTransactionItem row,
			ClientQuantity quantity) {
		if (quantity != null) {
			ClientQuantity oldQuantity = row.getQuantity();
			row.setQuantity(quantity);

			ClientCompany company = Accounter.getCompany();
			ClientUnit newUnit = company.getUnitById(quantity.getUnit());
			ClientUnit oldUnit = company.getUnitById(oldQuantity.getUnit());

			if (newUnit != null) {
				ClientQuantity convertToDefaultUnit = row.getQuantity()
						.convertToDefaultUnit(newUnit);
				row.setUnitPrice((row.getUnitPrice() * newUnit.getFactor())
						/ oldUnit.getFactor());
			}

			double lt = quantity.getValue() * row.getUnitPrice();
			Double disc = row.getDiscount();
			if (disc != null) {
				row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);
			}
			getTable().update(row);
		}
	}

	@Override
	protected String getColumnName() {
		if(Global.get().preferences().getIndustryType() == 33)
		{
			return messages2.hours();
		}
		else 
			return messages.quantity();
	
	}

	@Override
	protected void setValue(ClientTransactionItem row, String value) {
		try {
			if (value.isEmpty()) {
				value = "1";
			}
			ClientItem item = Accounter.getCompany().getItem(row.getItem());
			ClientQuantity quantity = row.getQuantity();
			if (item != null
					&& (item.getType() == ClientItem.TYPE_INVENTORY_PART
							|| item.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY || quantity == null)) {
				ClientCompany company = Accounter.getCompany();
				if (quantity == null) {
					quantity = new ClientQuantity();
				}
				if (quantity.getUnit() == 0) {
					ClientUnit defaultUnit = company.getMeasurement(
							item.getMeasurement()).getDefaultUnit();
					quantity.setUnit(defaultUnit.getId());
				}
				if (row.getWareHouse() == 0) {
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
	public String getValueAsString(ClientTransactionItem row) {
		return "Qty" + " : " + getValue(row);
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}
}
