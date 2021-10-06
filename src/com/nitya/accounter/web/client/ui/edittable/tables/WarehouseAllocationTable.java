package com.nitya.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ClientWareHouseAllocation;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.QuantityColumn;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;

public class WarehouseAllocationTable extends
		EditTable<ClientWareHouseAllocation> {

	@Override
	protected void initColumns() {

		this.addColumn(new TextEditColumn<ClientWareHouseAllocation>() {

			@Override
			protected String getValue(ClientWareHouseAllocation row) {
				ClientItem item = Accounter.getCompany().getItem(row.getItem());
				return item.getName();
			}

			@Override
			protected void setValue(ClientWareHouseAllocation row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			public int getWidth() {
				return 150;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public String getValueAsString(ClientWareHouseAllocation row) {
				return "";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new QuantityColumn<ClientWareHouseAllocation>() {

			@Override
			protected ClientQuantity getQuantity(ClientWareHouseAllocation row) {
				return row.getQuantity();
			}

			@Override
			protected void setQuantity(ClientWareHouseAllocation row,
					ClientQuantity d) {
				row.setQuantity(d);
			}

			@Override
			public String getValueAsString(ClientWareHouseAllocation row) {
				return "";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new WareHouseColumn());
	}

	public void addOrUpdateRecords(ClientTransactionItem transactionItem) {
		// FIXME Need to fix.
		long item = transactionItem.getItem();
		ClientQuantity quantity = transactionItem.getQuantity();
		if (item == 0) {
			return;
		}
		List<ClientWareHouseAllocation> records = getRecordsForItem(item);
		if (records.isEmpty()) {
			ClientWareHouseAllocation newRecord = new ClientWareHouseAllocation();
			newRecord.setItem(item);
			newRecord.setQuantity(transactionItem.getQuantity());
			this.add(newRecord);
		} else {
			if (quantity.getValue() == 0) {
				deleteRecords(records);
			} else {
				if (records.size() == 1) {
					ClientWareHouseAllocation allocation = records.get(0);
					allocation.setQuantity(quantity);
					this.update(allocation);
				} else {
					boolean completed;
					double value = quantity.getValue();
					for (ClientWareHouseAllocation row : records) {
						if (!DecimalUtil.isLessThan(row.getQuantity()
								.getValue(), value)) {
							row.setQuantity(quantity);
							this.update(row);
							records.remove(row);
							deleteRecords(records);
							break;
						}
					}
				}
			}
		}

	}

	public void deleteRecords(List<ClientWareHouseAllocation> records) {
		for (ClientWareHouseAllocation removable : records) {
			this.delete(removable);
		}
	}

	public List<ClientWareHouseAllocation> getRecordsForItem(long item) {
		List<ClientWareHouseAllocation> allRows = this.getAllRows();
		List<ClientWareHouseAllocation> result = new ArrayList<ClientWareHouseAllocation>();
		for (ClientWareHouseAllocation record : allRows) {
			if (record.getItem() == item) {
				result.add(record);
			}
		}
		return result;
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}
}
