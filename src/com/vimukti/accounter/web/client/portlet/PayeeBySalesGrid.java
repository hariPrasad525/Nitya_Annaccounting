package com.vimukti.accounter.web.client.portlet;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.PayeesBySalesPortletData;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class PayeeBySalesGrid extends ListGrid<PayeesBySalesPortletData> {

	private int portletType;

	public PayeeBySalesGrid(int portletType) {
		super(false);
		this.portletType = portletType;
		this.getElement().addClassName("dashboard_grid_header");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected int getColumnType(int index) {
		if (portletType != TopPayeesBySalesPortlet.ITEM_PORTLET) {
			if (index == 0) {
				return ListGrid.COLUMN_TYPE_TEXT;
			} else {
				return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
			}
		} else {
			return ListGrid.COLUMN_TYPE_TEXT;
		}
	}

	@Override
	protected Object getColumnValue(PayeesBySalesPortletData obj, int index) {
		if (portletType == TopPayeesBySalesPortlet.ITEM_PORTLET) {
			switch (index) {
			case 0:
				return obj.getName();
			case 1:
				return DecimalUtil.round(obj.getQuantity());

			default:
				break;
			}
		} else {
			switch (index) {
			case 0:
				return obj.getName();
			case 1:
				return obj.getNoOfTrans();
			case 2:
				return DataUtils.amountAsStringWithCurrency(obj.getAmount(),
						getCompany().getCurrency(obj.getCurrency()));
			default:
				break;
			}
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(PayeesBySalesPortletData obj, int index) {
		return null;
	}

	@Override
	protected void onValueChange(PayeesBySalesPortletData obj, int index,
			Object value) {
	}

	@Override
	protected boolean isEditable(PayeesBySalesPortletData obj, int row,
			int index) {
		return false;
	}

	@Override
	protected void onClick(PayeesBySalesPortletData obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(PayeesBySalesPortletData obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(PayeesBySalesPortletData obj1,
			PayeesBySalesPortletData obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		if (portletType == TopPayeesBySalesPortlet.ITEM_PORTLET) {
			if (index == 1) {
				return 150;
			} else {
				return -1;
			}
		} else {
			if (index == 1) {
				return 100;
			} else if (index == 2) {
				return 150;
			} else {
				return -1;
			}
		}
	}

	@Override
	protected String[] getColumns() {
		if (portletType == TopPayeesBySalesPortlet.ITEM_PORTLET) {
			return new String[] { messages.item(), messages.quantity() };
		} else if (portletType == TopPayeesBySalesPortlet.CUSTOMER_PORTLET) {
			return new String[] { messages.payeeName(Global.get().Customer()),
					messages.noOfTransactions(), messages.amount() };
		} else if (portletType == TopPayeesBySalesPortlet.VENDOR_PORTLET) {
			return new String[] { messages.payeeName(Global.get().Vendor()),
					messages.noOfTransactions(), messages.amount() };
		}
		return null;
	}

	@Override
	protected String getHeaderStyle(int index) {
		if (portletType == TopPayeesBySalesPortlet.ITEM_PORTLET) {
			switch (index) {
			case 0:
				return "item";
			case 1:
				return "quantity";
			default:
				return "";
			}

		} else if (portletType == TopPayeesBySalesPortlet.CUSTOMER_PORTLET) {
			switch (index) {
			case 0:
				return "payeeName";
			case 1:
				return "noOfTransactions";
			case 2:
				return "amount";
			default:
				return "";
			}
		} else if (portletType == TopPayeesBySalesPortlet.VENDOR_PORTLET) {
			switch (index) {
			case 0:
				return "payeeName";
			case 1:
				return "noOfTransactions";
			case 2:
				return "amount";
			default:
				return "";
			}
		}
		return null;
	}

	@Override
	protected String getRowElementsStyle(int index) {
		if (portletType == TopPayeesBySalesPortlet.ITEM_PORTLET) {
			switch (index) {
			case 0:
				return "item-value";
			case 1:
				return "quantity-value";
			default:
				return "";
			}

		} else if (portletType == TopPayeesBySalesPortlet.CUSTOMER_PORTLET) {
			switch (index) {
			case 0:
				return "payeeName-value";
			case 1:
				return "noOfTransactions-value";
			case 2:
				return "amount-value";
			default:
				return "";
			}
		} else if (portletType == TopPayeesBySalesPortlet.VENDOR_PORTLET) {
			switch (index) {
			case 0:
				return "payeeName-value";
			case 1:
				return "noOfTransactions-value";
			case 2:
				return "amount-value";
			default:
				return "";
			}
		}
		return null;
	}

}
