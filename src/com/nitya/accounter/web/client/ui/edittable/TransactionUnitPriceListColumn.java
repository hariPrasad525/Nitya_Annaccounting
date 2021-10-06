package com.nitya.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ItemUnitPrice;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class TransactionUnitPriceListColumn extends
		ListColumn<ClientTransactionItem, ItemUnitPrice> {

	private ClientPayee payee;

	protected List<ItemUnitPrice> list = new ArrayList<ItemUnitPrice>();

	private boolean isSales;

	UnitPriceDropDownTable dropdown = new UnitPriceDropDownTable(list);

	public TransactionUnitPriceListColumn(boolean isSales) {
		this.isSales = isSales;
	}

	@Override
	protected ItemUnitPrice getValue(ClientTransactionItem row) {
		ItemUnitPrice payeeUnitPrice = new ItemUnitPrice();
		payeeUnitPrice.setUnitPrice(row.getUnitPrice() != null ? ""
				+ row.getUnitPrice() : "");
		return payeeUnitPrice;
	}

	@Override
	protected String getColumnName() {
		if(Global.get().preferences().getIndustryType() == 33)
		{
			return messages2.payTypeHourly()+" "+messages.rate();
		}
		else
			return messages.unitPrice();
		
	}

	@Override
	protected void setValue(ClientTransactionItem row, ItemUnitPrice newValue) {
		row.setUnitPrice(Double.parseDouble(newValue.getUnitPrice()));
		if (row.getQuantity() != null && row.getUnitPrice() != null
				&& row.getDiscount() != null) {
			double lt = row.getQuantity().getValue() * row.getUnitPrice();
			double disc = row.getDiscount();
			row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
					* disc / 100)) : lt);
		}
		getTable().update(row);
	}

	@Override
	public AbstractDropDownTable<ItemUnitPrice> getDisplayTable(
			ClientTransactionItem row) {
		return dropdown;
	}

	@Override
	public int getWidth() {
		return 100;
	}

	public void setPayee(ClientPayee payee) {
		this.payee = payee;
	}

	private void resetList(final ClientTransactionItem row) {
		if (payee == null || row.getItem() == 0) {
			list = new ArrayList<ItemUnitPrice>();
			dropdown.setList(list);
			dropdown.reInitData();
			return;
		}
		AccounterAsyncCallback<ArrayList<ItemUnitPrice>> callback = new AccounterAsyncCallback<ArrayList<ItemUnitPrice>>() {

			@Override
			public void onException(AccounterException exception) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResultSuccess(ArrayList<ItemUnitPrice> result) {
				list = result;
				ClientItem item = Accounter.getCompany().getItem(row.getItem());
				ItemUnitPrice payeeUnitPrice = new ItemUnitPrice();
				payeeUnitPrice.setNumber("Actual Price");
				payeeUnitPrice.setUnitPrice(""
						+ (isSales ? item.getSalesPrice() : item
								.getPurchasePrice()));
				list.add(0, payeeUnitPrice);
				dropdown.setList(list);
				dropdown.reInitData();
				editComplete(row);
			}
		};

		Accounter.createHomeService().getUnitPricesByPayee(isSales,
				payee.getID(), row.getItem(), callback);
	}

	@Override
	protected void setNewValue(ClientTransactionItem row, String text) {
		ItemUnitPrice value = new ItemUnitPrice();
		value.setUnitPrice(text);
		this.setValue(row, value);
	}

	@Override
	protected void updateList(ClientTransactionItem row) {
		resetList(row);
	}

	@Override
	public String getValueAsString(ClientTransactionItem row) {
		return getColumnName()+" : "+getValue(row);
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}
}
