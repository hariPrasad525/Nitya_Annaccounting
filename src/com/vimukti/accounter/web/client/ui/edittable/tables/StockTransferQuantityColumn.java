package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientStockTransferItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.edittable.QuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.settings.UnitCombo;

public class StockTransferQuantityColumn extends
		QuantityColumn<ClientStockTransferItem> {

	PopupPanel popup = new PopupPanel(true);

	protected ClientQuantity getQuantity(ClientStockTransferItem row) {
		return row.getQuantity();
	}

	protected void setQuantity(ClientStockTransferItem row, ClientQuantity d) {
		row.setQuantity(d);
		getTable().update(row);
	}

	@Override
	public IsWidget getWidget(RenderContext<ClientStockTransferItem> context) {
		final IsWidget widget = super.getWidget(context);
		final ClientStockTransferItem row = context.getRow();
		if (widget instanceof TextBox && getPreferences().isUnitsEnabled()) {
			((TextBox) widget).addFocusListener(new FocusListener() {

				@Override
				public void onLostFocus(Widget sender) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFocus(Widget sender) {
					showPopUp(row);
					((TextBox) widget).setFocus(false);
				}
			});
		}
		return widget;
	}

	private void showPopUp(final ClientStockTransferItem row) {
		if (popup.isShowing()) {
			return;
		}
		popup.clear();
		FlexTable table = new FlexTable();
		Label valueLabel = new Label(messages.value());
		Label unitLabel = new Label(messages.unit());

		ClientUnit unit = Accounter.getCompany().getUnitById(
				row.getQuantity().getUnit());
		ClientItem item = Accounter.getCompany().getItem(row.getItem());
		final TextBox valueBox = new TextBox();
		valueBox.setFocus(true);
		valueBox.setText(String.valueOf(row.getQuantity().getValue()));
		final UnitCombo unitBox = new UnitCombo("");
		if (item != null) {
			ClientMeasurement measurement = Accounter.getCompany()
					.getMeasurement(item.getMeasurement());
			unitBox.initCombo(measurement.getUnits());
			unitBox.setComboItem(measurement.getUnits().get(0));
		}
		unitBox.setShowTitle(false);

		table.setWidget(0, 0, valueLabel);
		table.setWidget(1, 0, valueBox);
		table.setWidget(0, 1, unitLabel);
		table.setWidget(1, 1, unitBox.getMainWidget());
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
				getTable().update(row);
			}
		});
		Widget widget = getTable().getWidget(row, this);
		popup.setAutoHideEnabled(true);
		popup.showRelativeTo(widget);
	}

	protected String getValue(ClientStockTransferItem row) {
		ClientItem item = Accounter.getCompany().getItem(row.getItem());
		if (item == null || row.getQuantity() == null) {
			return "";
		} else {
			ClientUnit unit = Accounter.getCompany().getUnitById(
					row.getQuantity().getUnit());
			StringBuffer data = new StringBuffer();
			data.append(String.valueOf(row.getQuantity().getValue()));
			data.append(" ");
			if (unit != null) {
				data.append(unit.getType());
			}
			return data.toString();
		}
	}

	@Override
	protected void setValue(ClientStockTransferItem row, String value) {
		try {
			if (value.isEmpty()) {
				value = "1";
			}
			ClientQuantity quantity = row.getQuantity();
			quantity.setValue(DataUtils.getAmountStringAsDouble(value));
			setQuantity(row, quantity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getColumnName() {
		return messages.transferQuantity();
	}

	@Override
	public String getValueAsString(ClientStockTransferItem row) {
		return messages.transferQuantity()+" : "+getValue(row);
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}

}
