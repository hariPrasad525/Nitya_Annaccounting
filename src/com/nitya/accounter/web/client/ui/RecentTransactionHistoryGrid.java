package com.nitya.accounter.web.client.ui;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.nitya.accounter.core.Estimate;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.RecentTransactionsList;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class RecentTransactionHistoryGrid extends
		ListGrid<RecentTransactionsList> {

	public RecentTransactionHistoryGrid() {
		super(false);
		this.getElement().addClassName("dashboard_grid_header");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 1:
			return ListGrid.COLUMN_TYPE_LINK;
		case 2:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		default:
			return ListGrid.COLUMN_TYPE_LABEL;
		}
	}

	@Override
	protected Object getColumnValue(RecentTransactionsList obj, int index) {
		switch (index) {
		case 1:
			if (obj.getEstimateType() != 0) {
				return getStringByType(obj.getEstimateType());
			} else {
				return Utility.getTransactionName(obj.getType());
			}
		case 2:
			return DataUtils.amountAsStringWithCurrency(
					Math.abs(obj.getAmount()),
					getCompany().getCurrency(obj.getCurrecyId()));
		case 0:
			return /*DateUtills.getSelectedFormatDate(
					DateUtills.getDateAsString(obj.getTransactionDate()),
					getCompany().getPreferences().getDateFormat());*/
					getDateByCompanyType(obj.getTransactionDate());
		case 3:
			return obj.getName();
		default:
			break;
		}
		return null;
	}

	private String getStringByType(int type) {
		String title = null;
		if (type == Estimate.QUOTES) {
			title = messages.quote();
		} else if (type == Estimate.CHARGES) {
			title = messages.charge();
		} else if (type == Estimate.CREDITS) {
			title = messages.credit();
		} else if (type == Estimate.SALES_ORDER) {
			title = messages.salesOrder();
		}
		return title;
	}

	@Override
	protected String[] getSelectValues(RecentTransactionsList obj, int index) {
		return null;
	}

	@Override
	protected void onValueChange(RecentTransactionsList obj, int index,
			Object value) {

	}

	@Override
	protected boolean isEditable(RecentTransactionsList obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(RecentTransactionsList obj, int row, int index) {
		if (!Utility.isUserHavePermissions(obj.getType())) {
			return;
		}
		ReportsRPC.openTransactionView(obj.getType(), obj.getID());
	}

	@Override
	public void onDoubleClick(RecentTransactionsList obj) {
		if (!Utility.isUserHavePermissions(obj.getType())) {
			return;
		}
		ReportsRPC.openTransactionView(obj.getType(), obj.getID());
	}

	@Override
	protected int sort(RecentTransactionsList obj1,
			RecentTransactionsList obj2, int index) {
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 100;
		case 3:
			return 100;
		default:
			break;
		}
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "", "", "", "" };
	}
	public String getDateByCompanyType(ClientFinanceDate date) {
		
		if (date == null) {
			return "";
		}
		String dateFormat = Global.get().preferences().getDateFormat();
		if (dateFormat == null) {
			dateFormat = "dd/MM/yyyy";
		}
		DateTimeFormat dateFormatter = DateTimeFormat.getFormat(dateFormat);
		String format = dateFormatter.format(date.getDateAsObject());
		return format;

	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "col-1";
		case 1:
			return "col-2";
		case 2:
			return "col-2";
		case 3:
			return "col-3";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "col-1-value";
		case 1:
			return "col-2-value";
		case 2:
			return "col-2-value";
		case 3:
			return "col-3-value";
		default:
			return "";
		}
	}
}
