package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientETDSFillingItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.BankDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public abstract class ETdsCellTable extends EditTable<ClientETDSFillingItem> {

	private List<ClientBank> getDeducteeCodes() {

		ArrayList<ClientBank> list = new ArrayList<ClientBank>();
		ClientBank a = new ClientBank();
		a.setName("1");
		ClientBank b = new ClientBank();
		b.setName("2");
		list.add(a);
		list.add(b);
		return list;
	}

	private List<ClientBank> getRemarkValues() {
		ArrayList<ClientBank> list = new ArrayList<ClientBank>();
		ClientBank a = new ClientBank();
		a.setName("A");
		ClientBank b = new ClientBank();
		b.setName("B");
		list.add(a);
		list.add(b);

		return list;
	}

	private List<ClientBank> getGrossingUpIndicators() {
		ArrayList<ClientBank> list = new ArrayList<ClientBank>();
		ClientBank a = new ClientBank();
		a.setName(messages.yes());
		ClientBank b = new ClientBank();
		b.setName(messages.no());
		list.add(a);
		list.add(b);

		return list;
	}

	@Override
	protected void initColumns() {

		TextEditColumn<ClientETDSFillingItem> serialNoColumn = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return Integer.toString(object.getSerialNo());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.SRNo();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.SRNo()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> bankBsrCodeColumn = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return object.getBankBSRCode();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.bankBSRCode();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("100px");
				return 100;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.bankBSRCode()+" : "+getValue(row) ;
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> dateTaxDepositedColumn = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DateUtills.getDateAsString(object.getDateTaxDeposited());
			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.dateTaxDeposited();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("115px");
				return 115;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.dateTaxDeposited()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> chalanSerialCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return Long.toString(object.getChalanSerialNumber());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.challanSerialNo();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("110px");
				return 110;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.challanSerialNo()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> sectionCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return object.getSectionForPayment();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.sectionForPayment();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("120px");
				return 120;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.sectionForPayment()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> tdsTotalCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return Double.toString(object.getTotalTDSfordeductees());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.totalTDSToBeAllocatedAmongAllDeductees();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("155px");
				return 275;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.totalTDSToBeAllocatedAmongAllDeductees()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> deducteePanCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return object.getPanOfDeductee();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				row.setPanOfDeductee(value);
			}

			@Override
			protected String getColumnName() {
				return messages.panOfTheDeductee();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("125px");
				return 100;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.panOfTheDeductee()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

		};

		TextEditColumn<ClientETDSFillingItem> deducteeNameCOl = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return Accounter.getCompany().getPayee(object.getDeducteeID())
						.getName();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {

			}

			@Override
			protected String getColumnName() {
				return messages.deducteeName();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("100px");
				return 100;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return  messages.deducteeName()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> paymentDateCOl = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DateUtills.getDateAsString(object.getDateOFpayment());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {

			}

			@Override
			protected String getColumnName() {
				return messages.dateOfPaymentOrCredit();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("145px");
				return 145;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.dateOfPaymentOrCredit()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> amountPaidCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getAmountPaid());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.amountPaidOrCredited();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("110px");
				return 110;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.amountPaidOrCredited()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> TDSCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getTds());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.tds();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.tds()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> surchargeCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getSurcharge());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.surcharge();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.surcharge()+ " : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> educationCessCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getEducationCess());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.educationCess();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("95px");
				return 95;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.educationCess()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> totalTaxDeductedCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object
						.getTotalTaxDEducted());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {

			}

			@Override
			protected String getColumnName() {
				return messages.totalTaxDeducted();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("110px");
				return 110;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.totalTaxDeducted()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};

		TextEditColumn<ClientETDSFillingItem> totalTaxDepositedCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object
						.getTotalTaxDeposited());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.totalTaxDeposited();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("115px");
				return 115;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.totalTaxDeposited()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 2;
			}
		};

		TextEditColumn<ClientETDSFillingItem> dateDeductionCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DateUtills.getDateAsString(object.getDateofDeduction());

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {

			}

			@Override
			protected String getColumnName() {
				return messages.dateOfDeduction();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("105px");
				return 105;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.dateOfDeduction()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 2;
			}
		};

		final BankDropDownTable deducteeCodeTable = new BankDropDownTable(
				getDeducteeCodes());

		ComboColumn<ClientETDSFillingItem, ClientBank> deducteeCodeCol = new ComboColumn<ClientETDSFillingItem, ClientBank>() {

			@Override
			public ClientBank getValue(ClientETDSFillingItem object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getColumnName() {
				return messages.deducteeCode();
			}

			@Override
			protected void setValue(ClientETDSFillingItem row,
					ClientBank newValue) {
				row.setCompanyCode(newValue.getName());
			}

			@Override
			public AbstractDropDownTable<ClientBank> getDisplayTable(
					ClientETDSFillingItem row) {
				return deducteeCodeTable;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("225px");
				return 0;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.deducteeCode()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 2;
			}
		};

		final BankDropDownTable remarkCodeTable = new BankDropDownTable(
				getRemarkValues());

		ComboColumn<ClientETDSFillingItem, ClientBank> remarkCol = new ComboColumn<ClientETDSFillingItem, ClientBank>() {

			@Override
			public ClientBank getValue(ClientETDSFillingItem object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getColumnName() {
				return messages.remarkTDSChallan();
			}

			@Override
			protected void setValue(ClientETDSFillingItem row,
					ClientBank newValue) {
				row.setRemark(newValue.getName());
			}

			@Override
			public AbstractDropDownTable<ClientBank> getDisplayTable(
					ClientETDSFillingItem row) {
				return remarkCodeTable;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("260px");
				return 0;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				// TODO Auto-generated method stub
				return messages.remarkTDSChallan()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 2;
			}
		};

		// SelectionCell deducteeCodeCol = new SelectionCell(null);

		TextEditColumn<ClientETDSFillingItem> rateCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return DataUtils.getAmountAsStrings(object.getTaxRate());
			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.rateAtWhichDeducted();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
			
			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("110px");
				return 0;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.rateAtWhichDeducted()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 2;
			}
		};

		TextEditColumn<ClientETDSFillingItem> bookEntryCol = new TextEditColumn<ClientETDSFillingItem>() {

			@Override
			public String getValue(ClientETDSFillingItem object) {
				return object.getBookEntry();

			}

			@Override
			protected void setValue(ClientETDSFillingItem row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getColumnName() {
				return messages.paidByBookEntryOrOtherwise();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("105px");
				return 0;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.paidByBookEntryOrOtherwise()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 2;
			}
		};

		final BankDropDownTable grossingUpTable = new BankDropDownTable(
				getGrossingUpIndicators());

		ComboColumn<ClientETDSFillingItem, ClientBank> grossingUpCol = new ComboColumn<ClientETDSFillingItem, ClientBank>() {

			@Override
			public ClientBank getValue(ClientETDSFillingItem object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getColumnName() {
				return messages.grossingUpIndicator();
			}

			@Override
			protected void setValue(ClientETDSFillingItem row,
					ClientBank newValue) {
				row.setGrossingUpIndicator(newValue.getName());
			}

			@Override
			public AbstractDropDownTable<ClientBank> getDisplayTable(
					ClientETDSFillingItem row) {
				return grossingUpTable;
			}

			@Override
			public int getWidth() {
				getHeader().asWidget().setWidth("100px");
				return 0;
			}

			@Override
			public String getValueAsString(ClientETDSFillingItem row) {
				return messages.grossingUpIndicator()+" : "+getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 2;
			}
		};

		this.addColumn(serialNoColumn);
		this.addColumn(bankBsrCodeColumn);
		this.addColumn(dateTaxDepositedColumn);
		this.addColumn(chalanSerialCol);
		this.addColumn(sectionCol);
		this.addColumn(tdsTotalCol);
		this.addColumn(deducteePanCol);
		this.addColumn(deducteeNameCOl);
		this.addColumn(paymentDateCOl);
		this.addColumn(amountPaidCol);
		this.addColumn(TDSCol);
		this.addColumn(surchargeCol);
		this.addColumn(educationCessCol);
		this.addColumn(totalTaxDeductedCol);
		this.addColumn(totalTaxDepositedCol);
		this.addColumn(dateDeductionCol);
		this.addColumn(deducteeCodeCol);
		this.addColumn(remarkCol);
		this.addColumn(rateCol);
		this.addColumn(bookEntryCol);
		if (is27Q()) {
			this.addColumn(grossingUpCol);
		}
	}

	protected abstract boolean is27Q();

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
