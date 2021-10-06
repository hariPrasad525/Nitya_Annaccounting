package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class ChalanDetailsListGrid extends BaseListGrid<ClientTDSChalanDetail> {

	private boolean fromTransactionCenter;

	public ChalanDetailsListGrid(boolean fromTransactionCenter) {
		super(false, true);
		this.getElement().setId("ChalanDetailsListGrid");
		this.fromTransactionCenter = fromTransactionCenter;
		if (fromTransactionCenter) {
			this.setWidth("760px");
		}
	}

	public void initWithItems(List<ClientTDSChalanDetail> allItems) {
		this.setRecords(allItems);

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientTDSChalanDetail object) {
		deleteObject(object);
	}

	@Override
	protected Object getColumnValue(ClientTDSChalanDetail obj, int index) {
		switch (index) {
		case 0: {
			if (obj.getFormType() == 1) {
				return messages.form26Q();
			} else if (obj.getFormType() == 2) {
				return messages.form27Q();
			} else if (obj.getFormType() == 3) {
				return messages.form27EQ();
			} else {
				return messages.form26Q();
			}
		}
		case 1: {
			String assessmentYear = Integer.toString(obj
					.getAssesmentYearStart() - 1)
					+ "-"
					+ Integer.toString(obj.getAssessmentYearEnd() - 1);
			return assessmentYear;
		}
		case 2:
			return obj.getIncomeTaxAmount() + obj.getSurchangePaidAmount()
					+ obj.getEducationCessAmount()
					+ obj.getInterestPaidAmount() + obj.getPenaltyPaidAmount()
					+ obj.getOtherAmount();
		case 3: {

			if (obj.getChalanPeriod() == 1) {
				return "Q1" + " " + DayAndMonthUtil.apr() + " - "
						+ DayAndMonthUtil.jun();
			} else if (obj.getChalanPeriod() == 2) {
				return "Q2" + " " + DayAndMonthUtil.jul() + " - "
						+ DayAndMonthUtil.sep();
			} else if (obj.getChalanPeriod() == 3) {
				return "Q3" + " " + DayAndMonthUtil.oct() + " - "
						+ DayAndMonthUtil.dec();
			} else if (obj.getChalanPeriod() == 4) {
				return "Q4" + " " + DayAndMonthUtil.jan() + " - "
						+ DayAndMonthUtil.mar();
			} else {
				return "Q4" + " " + DayAndMonthUtil.jan() + " - "
						+ DayAndMonthUtil.mar();
			}
		}
		case 4:
			return new ClientFinanceDate(obj.getDateTaxPaid());
		case 5:
			return obj.getBankBsrCode();
		case 6:
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}

		return null;
	}

	@Override
	public void onDoubleClick(ClientTDSChalanDetail obj) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			ReportsRPC.openTransactionView(obj.getType(), obj.getID());
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.formType(), messages.financialYear(),
				messages.totalAmount(), messages.challanPeriod(),
				messages.dateofPayment(), messages.bankBSRCode(), "" };
	}

	@Override
	protected void onClick(ClientTDSChalanDetail obj, int row, int col) {
		if (col == 6) {
			showWarnDialog(obj);
		}
	}

	@Override
	protected int getCellWidth(int index) {

		if (index == 0)
			return 105;
		if (index == 1) {
			if (fromTransactionCenter) {
				return 100;
			} else {
				return 173;
			}
		}
		if (index == 2)
			return 128;
		if (index == 3)
			return 138;
		if (index == 4) {
			if (fromTransactionCenter) {
				return 120;
			}
			return 170;
		}
		if (index == 5) {
			if (fromTransactionCenter) {
				return 100;
			}
			return 163;
		}
		if (index == 6) {
			return 15;
		}
		return -1;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "formType", "assessmentYear", "totalAmount",
				"challanPeriod", "dateofPayment", "bankBSRCode", "unknown" };

	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "formTypeValue", "assessmentYearValue",
				"totalAmountValue", "challanPeriodValue", "dateofPaymentValue",
				"bankBSRCodeValue", "unknownValue", };
	}
}
