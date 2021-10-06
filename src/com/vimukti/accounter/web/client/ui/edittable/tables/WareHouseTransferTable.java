package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientStockTransferItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class WareHouseTransferTable extends
		EditTable<ClientStockTransferItem> {

	@Override
	protected void initColumns() {

		if (!isInViewMode()) {
			this.addColumn(new CheckboxEditColumn<ClientStockTransferItem>() {

				@Override
				protected void onChangeValue(boolean value,
						ClientStockTransferItem row) {
					onSelectionChanged(row, value);
				}

				@Override
				public void render(IsWidget widget,
						RenderContext<ClientStockTransferItem> context) {
					super.render(widget, context);
					if (isInViewMode()) {
						((CheckBox) widget).setValue(true);
					}
				}

				@Override
				public String getValueAsString(ClientStockTransferItem row) {
					return "TODO";
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}
			});
		}
		TextEditColumn<ClientStockTransferItem> itemName = new TextEditColumn<ClientStockTransferItem>() {

			@Override
			protected String getValue(ClientStockTransferItem row) {
				return Accounter.getCompany().getItem(row.getItem()).getName();
			}

			@Override
			protected void setValue(ClientStockTransferItem row, String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.itemName();
			}

			@Override
			public int getWidth() {
				return 200;
			}

			@Override
			public String getValueAsString(ClientStockTransferItem row) {
				// TODO Auto-generated method stub
				return messages.itemName()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(itemName);

		if (!isInViewMode()) {
			TextEditColumn<ClientStockTransferItem> totalQuantity = new TextEditColumn<ClientStockTransferItem>() {

				@Override
				protected String getValue(ClientStockTransferItem row) {
					ClientItem item = Accounter.getCompany().getItem(
							row.getItem());
					if (item == null) {
						return String
								.valueOf(row.getTotalQuantity().getValue());
					} else {
						ClientUnit unit = Accounter.getCompany().getUnitById(
								row.getTotalQuantity().getUnit());
						StringBuffer data = new StringBuffer();
						data.append(String.valueOf(row.getTotalQuantity()
								.getValue()));
						data.append(" ");
						if (unit != null) {
							data.append(unit.getType());
						}
						return data.toString();
					}
				}

				@Override
				protected void setValue(ClientStockTransferItem row,
						String value) {
					// No Need
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.totalQuantity();
				}

				@Override
				public int getWidth() {
					return 200;
				}

				@Override
				public String getValueAsString(ClientStockTransferItem row) {
					return messages.totalQuantity()+" : "+getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}
			};
			this.addColumn(totalQuantity);
		}
		this.addColumn(new StockTransferQuantityColumn());
	}

	protected void onSelectionChanged(ClientStockTransferItem row, boolean value) {
		if (value) {
			row.getQuantity().setValue(row.getTotalQuantity().getValue());
			row.getQuantity().setUnit(row.getTotalQuantity().getUnit());
		} else {
			row.getQuantity().setValue(0);
			row.getQuantity().setUnit(0);
		}
		update(row);
	}

	protected abstract boolean isInViewMode();

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		List<ClientStockTransferItem> selectedRecords = getSelectedRecords();
		List<ClientStockTransferItem> allRows = getAllRows();
		if (allRows == null || allRows.isEmpty()) {
			result.addError(this, messages.youDontHaveAnyItemsToTransfer());
		} else if (selectedRecords == null || selectedRecords.isEmpty()) {
			result.addError(this, messages.pleaseSelectAtLeastOneRecord());
		} else {
			for (ClientStockTransferItem item : selectedRecords) {
				if (item.getQuantity().getValue() == 0
						|| item.getQuantity().getValue() < 0) {
					result.addError(this, messages
							.transferQuantityShouldntbeZeroForSelectedRecords());
				}
			}
		}

		return result;
	}

	public void removeAllRecords() {
		clear();
	}

	public List<ClientStockTransferItem> getSelectedRecords() {
		return super.getSelectedRecords(0);
	}
}
