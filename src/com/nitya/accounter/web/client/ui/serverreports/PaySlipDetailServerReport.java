package com.nitya.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.PaySlipDetail;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.reports.PaySlipDetailReport;
import com.nitya.accounter.web.client.ui.reports.Section;

public class PaySlipDetailServerReport extends
		AbstractFinaneReport<PaySlipDetail> {

	private int type;
	protected final List<Section<PaySlipDetail>> endedSections = new ArrayList<Section<PaySlipDetail>>();

	public PaySlipDetailServerReport(PaySlipDetailReport paySlipDetailReport) {
		this.reportView = paySlipDetailReport;
	}

	public PaySlipDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.name(), messages.earnings(),
				messages.deductions(), Global.get().messages2().deductionsFromCompany(), messages.value() };
	}

	@Override
	public String getTitle() {
		return messages.payslipDetail();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.name(), messages.earnings(),
				messages.deductions(), Global.get().messages2().deductionsFromCompany(), messages.value(), "" };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT };
	}

	@Override
	public void initRecords(List<PaySlipDetail> records) {
		initGrid();
		removeAllRows();
		row = -1;
		this.records = records;

		for (PaySlipDetail record : records) {
			processRecord(record);
			Object[] values = new Object[this.getColunms().length];
			Object[] updatedValues = new Object[this.getColunms().length];
			for (int x = 0; x < this.getColunms().length; x++) {
				values[x] = getColumnData(record, x);
				updatedValues[x] = getColumnData(record, x);
				if (x == 1 || x == 2 || x == 3) {
					updatedValues[x] =  x == 3 ? record.getEmployeerDeduction() : record.getAmount();
				}
			}
			updateTotals(updatedValues);
			addRow(record, 2, values, false, false, false);
		}
		endAllSections();
		sections.clear();
		endedSections.clear();
		endStatus();
		showRecords();
	}

	@Override
	public void endSection() {
		try {
			this.sectionDepth--;
			if (sectionDepth >= 1 && !sections.isEmpty()) {
				Section<PaySlipDetail> s = sections.remove(sectionDepth);
				Object[] data = s.data;
				Double value1 = data[1] != null ?  DataUtils.getAmountStringAsDouble(data[1].toString()) : 0.0;
				Double value2 = data[2] != null ?  DataUtils.getAmountStringAsDouble(data[2].toString()) : 0.0;
				Double value3 = data[3] != null ?  DataUtils.getAmountStringAsDouble(data[3].toString()) : 0.0;
				if (data[1] != null) {
					data[1] = getAmountAsString(null, value1);
				}
				if (data[2] != null) {
					data[2] = getAmountAsString(null, value2);
				}
				if (data[3] != null) {
					data[3] = getAmountAsString(null, value3);
				}
				s.endSection();
				if(sectionDepth == 1)
					endedSections.add(s);
			} else if (sectionDepth == 0 && !sections.isEmpty()) {
				Section<PaySlipDetail> s = sections.remove(sectionDepth);
				for(Section<PaySlipDetail> section:endedSections) {
					if(section != null && section.isEnded()) {
						Object[] data = section.data;
						Double value1 = data[1] != null ?  DataUtils.getAmountStringAsDouble(data[1].toString()) : 0.0;
						Double value2 = data[2] != null ?  DataUtils.getAmountStringAsDouble(data[2].toString()) : 0.0;
						if (data[1] != null) {
							s.data[1] = getAmountAsString(null, value1);
						}
						if (data[2] != null) {
							s.data[2] = getAmountAsString(null,  value2);
						}
						s.data[3] = "0.0";
						Double empNetSalary =  DataUtils.getAmountStringAsDouble(s.data[1].toString()) -  DataUtils.getAmountStringAsDouble(s.data[2].toString());
						String amountAsString = DataUtils.amountAsStringWithCurrency(empNetSalary,
								Accounter.getCompany().getPrimaryCurrency());
						s.data[5] = getMessages2().takeHome() +  " = " + amountAsString;
						s.isaddFooter = true;
					}
				}
				s.endSection();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processRecord(PaySlipDetail record) {
		if (sectionDepth == 0) {
			this.type = record.getType();
			addSection(new String[] { "" }, new String[] { "" }, new int[] { 1, 2, 3 });
		} else if (sectionDepth == 1) {
			this.type = record.getType();
			String typeName = getItemTypeName(type);
			addSection(new String[] { typeName },
					type != 1 ? new String[] { getMessages().total() }
							: new String[] {},
					type != 1 ? type == 2 ? new int[] { 1 } : new int[] { 2, 3 }
							: new int[] {});
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (this.type != record.getType()) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	private String getItemTypeName(int type) {
		if (type == 1) {
			return messages.attendance();
		} else if (type == 2) {
			return messages.earnings();
		} else {
			return messages.deductions();
		}
	}

	@Override
	public Object getColumnData(PaySlipDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getType() == 2 ? getAmountAsString(record,
					record.getAmount()) : 0;

		case 2:
			return record.getType() == 3 ? getAmountAsString(record,
					record.getAmount()) : 0;

		case 3:
			   return record.getEmployeerDeduction();
		case 4:
			return record.getType() == 1 ? getAmountAsString(record,
					record.getAmount() == null ? 0.0 : record.getAmount()) : 0;

		default:
			break;
		}
		return null;
	}
	
	private boolean isEarningType(int type) {
		return type == 2 || (type == 1 && Accounter.getCompany().getPreferences().isPayrollOnly());
	}

	protected String getAmountAsString(PaySlipDetail detail, double amount) {
		return String.valueOf(amount);
	}

	@Override
	public ClientFinanceDate getStartDate(PaySlipDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(PaySlipDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}
}
