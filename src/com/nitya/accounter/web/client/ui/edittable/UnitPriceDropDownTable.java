package com.nitya.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ItemUnitPrice;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

public class UnitPriceDropDownTable extends
		AbstractDropDownTable<ItemUnitPrice> {

	private List<ItemUnitPrice> list;

	public UnitPriceDropDownTable(List<ItemUnitPrice> newData) {
		super(newData, false);
		this.list = newData;
	}

	public void setList(List<ItemUnitPrice> list) {
		this.list = list;
	}

	@Override
	public List<ItemUnitPrice> getTotalRowsData() {
		return list;
	}

	@Override
	protected ItemUnitPrice getAddNewRow() {
		ItemUnitPrice payee = new ItemUnitPrice() {

			@Override
			public String getDisplayName() {
				return null;
			}

			@Override
			public String getUnitPrice() {
				return null;
			}

		};
		payee.setUnitPrice(messages.comboDefaultAddNew(messages.payee()));
		return payee;
	}

	@Override
	public void initColumns() {
		TextColumn<ItemUnitPrice> date = new TextColumn<ItemUnitPrice>() {

			@Override
			public String getValue(ItemUnitPrice row) {
				if (row.getDate() == 0) {
					return null;
				}
				return DateUtills.getDateAsString(new ClientFinanceDate(row
						.getDate()).getDateAsObject());
			}
		};
		this.addColumn(date);

		TextColumn<ItemUnitPrice> number = new TextColumn<ItemUnitPrice>() {

			@Override
			public String getValue(ItemUnitPrice row) {
				return row.getNumber();
			}
		};
		this.addColumn(number);

		TextColumn<ItemUnitPrice> amount = new TextColumn<ItemUnitPrice>() {

			@Override
			public String getValue(ItemUnitPrice row) {
				return row.getUnitPrice();
			}
		};
		this.addColumn(amount);

	}

	@Override
	protected boolean filter(ItemUnitPrice t, String string) {
		if (t.getUnitPrice() != null
				&& t.getUnitPrice().toLowerCase().startsWith(string)) {
			return true;
		}
		return false;
	}

	@Override
	protected String getDisplayValue(ItemUnitPrice value) {
		return value.getUnitPrice();
	}

	@Override
	protected void addNewItem(String text) {

	}

	@Override
	protected void addNewItem() {

	}

}
