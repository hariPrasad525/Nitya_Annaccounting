package com.nitya.accounter.web.client.ui.grids;

public class ConversionBalancesGrid extends ListGrid {

	public ConversionBalancesGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXT;
		default:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		}
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected Object getColumnValue(Object obj, int index) {

		return null;
	}

	@Override
	protected String[] getSelectValues(Object obj, int index) {

		return null;
	}

	@Override
	protected boolean isEditable(Object obj, int row, int index) {

		return false;
	}

	@Override
	protected void onClick(Object obj, int row, int index) {

	}

	@Override
	public void onDoubleClick(Object obj) {

	}

	@Override
	protected void onValueChange(Object obj, int index, Object value) {

	}

	@Override
	protected int sort(Object obj1, Object obj2, int index) {

		return 0;
	}

	@Override
	protected int getCellWidth(int index) {

		return 0;
	}

	@Override
	protected String[] getColumns() {

		return null;
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "item";
		case 1:
			return "quantity";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "item-value";
		case 1:
			return "quantity-value";
		default:
			return "";
		}
	}

}
