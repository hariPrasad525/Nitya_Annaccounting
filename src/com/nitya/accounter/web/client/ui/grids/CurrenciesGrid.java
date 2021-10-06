package com.nitya.accounter.web.client.ui.grids;
//package com.nitya.accounter.web.client.ui.grids;
//
//import java.util.List;
//
//import com.nitya.accounter.web.client.core.ClientCurrency;
//import com.nitya.accounter.web.client.core.ValidationResult;
//import com.nitya.accounter.web.client.ui.Accounter;
//
//public class CurrenciesGrid extends BaseListGrid<ClientCurrency> {
//	public CurrenciesGrid() {
//		super(true);
//	}
//
//	@Override
//	protected int getColumnType(int index) {
//		return super.getColumnType(index);
//	}
//
//	@Override
//	public void init() {
//		super.init();
//	}
//
//	@Override
//	public void addRecords(List<ClientCurrency> list) {
//		super.addRecords(list);
//	}
//
//	@Override
//	protected Object getColumnValue(ClientCurrency obj, int index) {
//		switch (index) {
//		case 0:
//			return obj.getFormalName();
//		case 1:
//			return obj.getName();
//		default:
//			break;
//		}
//		return null;
//	}
//
//	@Override
//	protected String[] getSelectValues(ClientCurrency obj, int index) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected void onValueChange(ClientCurrency obj, int index, Object value) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected boolean isEditable(ClientCurrency obj, int row, int index) {
//		return false;
//	}
//
//	@Override
//	protected void onClick(ClientCurrency obj, int row, int index) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onDoubleClick(ClientCurrency obj) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected int sort(ClientCurrency obj1, ClientCurrency obj2, int index) {
//
//		switch (index) {
//		case 1:
//			return obj1.getFormalName().compareTo(obj2.getFormalName());
//
//		case 2:
//			return obj1.getFormalName().compareTo(obj2.getFormalName());
//
//		default:
//			break;
//		}
//
//		return 0;
//
//	}
//
//	@Override
//	protected int getCellWidth(int index) {
//		if (index == 0)
//			return 50;
//		if (index == 1)
//			return 100;
//		if (index == 2)
//			return 60;
//		return -1;
//
//	}
//
//	@Override
//	protected String[] getColumns() {
//		return new String[] { messages.currencyCode(),
//				messages.currencyName() };
//	}
//
//	@Override
//	protected int[] setColTypes() {
//		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT };
//	}
//
//	@Override
//	protected void executeDelete(ClientCurrency object) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public ValidationResult validateGrid() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//}
