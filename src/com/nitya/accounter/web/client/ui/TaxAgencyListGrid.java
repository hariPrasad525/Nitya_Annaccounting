package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.nitya.accounter.web.client.core.ClientCompany;
//import com.nitya.accounter.web.client.core.ClientPaySalesTax;
//import com.nitya.accounter.web.client.core.ClientTaxAgency;
//import com.nitya.accounter.web.client.core.ClientTaxCode;
//import com.nitya.accounter.web.client.core.ClientTransaction;
//import com.nitya.accounter.web.client.core.ClientTransactionItem;
//import com.nitya.accounter.web.client.core.ClientTransactionPaySalesTax;
//import com.nitya.accounter.web.client.core.Utility;
//import com.nitya.accounter.web.client.ui.combo.CustomCombo;
//import com.nitya.accounter.web.client.ui.core.AmountField;
//import com.nitya.accounter.web.client.ui.forms.TextItem;
//import com.nitya.accounter.web.client.ui.grids.FinanceGrid;
//import com.nitya.accounter.web.client.ui.grids.ListGrid;
//
///**
// * 
// * @author venki.p
// * 
// */
//
//public class TaxAgencyListGrid extends FinanceGrid {
//	private ClientCompany company;
//	// private TaxCodeCombo taxCodeCombo;
//	private CustomCombo taxCodeCombo;
//	protected ClientTaxCode selectedTaxCode;
//
//	private double totallinetotal;
//	private ClientTaxAgency currentRecord;
//	protected List<ClientTaxCode> allTaxCodes;
//	FinanceGrid grid;
//	AmountField amtTextItem;
//	TextItem noInWordsText;
//
//	public TaxAgencyListGrid(AmountField amtTextItem, TextItem noInWordsText) {
//		super(false);
//		this.amtTextItem = amtTextItem;
//		this.noInWordsText = noInWordsText;
//		company = FinanceApplication.getCompany();
//		createControls();
//		// XXX NOT USED
//		// addTaxCodesList();
//	}
//
//	private void createControls() {
//
//		grid = new FinanceGrid(false);
//		grid.init();
//		setShowMenu(false);
//		grid.addColumn(ListGrid.COLUMN_TYPE_SELECT, "Tax Code");
//
//		// taxCodeCombo
//		// .addSelectionChangeHandler(new
//		// IAccounterComboSelectionChangeHandler() {
//		//
//		// public void selectedComboBoxItem(IsSerializable selectItem) {
//		// selectedTaxCode = (ClientTaxCode) selectItem;
//		// }
//		//
//		// });
//		// XXX NOT IN USE
//		// taxCodeField.setRequired(true);
//
//		grid.addColumn(ListGrid.COLUMN_TYPE_SELECT, "Tax Agency");
//		// XXX NOT USED
//		// taxAgencyField.setRequired(true);
//		// taxAgencyField.setCanEdit(false);
//		grid.addColumn(ListGrid.COLUMN_TYPE_TEXTBOX, "Amount To Pay");
//		// amountToPayField.setRequired(true);
//
//		// addEditCompleteHandler(new EditCompleteHandler() {
//		//
//		// public void onEditComplete(EditCompleteEvent event) {
//		// calcTotal(grid.getRecord(event.getRowNum()));
//		// }
//		// });
//		// addRecordDeleteHandler(new RecordDeleteHandler() {
//		// public boolean onRecordDelete(ListGridRecord record) {
//		// calcTotal(null);
//		// return true;
//		// }
//		// });
//
//		grid.addEditCompleteHandler(new EditCompleteHandler() {
//
//			@Override
//			public void OnEditComplete(IsSerializable core, Object value,
//					int col) {
//				ClientTransactionItem rec = (ClientTransactionItem) grid
//						.getSelection();
//				switch (col) {
//				case 0:
//					selectedTaxCode = (ClientTaxCode) core;
//					break;
//				case 1:
//
//					break;
//				case 2:
//					calcTotal(rec);
//					break;
//				}
//
//			}
//		});
//		grid.addRecordDeleteHandler(new RecordDeleteHandler() {
//
//			@Override
//			public boolean onRecordDelete(IsSerializable core, int row) {
//				calcTotal(null);
//				return true;
//			}
//		});
//	}
//
//	public void addTaxCodesList() {
//		allTaxCodes = FinanceApplication.getCompany().getTaxcodes();
//		taxCodeCombo.initCombo(allTaxCodes);
//		// taxCodeField.setEditorType(taxCodeCombo);
//
//	}
//
//	protected void calcTotal(ClientTransactionItem record) {
//		if (record != null) {
//			if (selectedTaxCode.getTaxAgency() != 0)
//				// record.setAttribute("taxAgency", FinanceApplication
//				// .getCompany().getTaxAgency(
//				// selectedTaxCode.getTaxAgency()).getName());
//				record.setTaxCode(selectedTaxCode.getID());
//		}
//		List<IsSerializable> allrecords = getRecords();
//		totallinetotal = 0;
//		for (IsSerializable re : allrecords) {
//			ClientTransactionItem rec = (ClientTransactionItem) re;
//			try {
//				// totallinetotal += Double.parseDouble(rec.getAttribute(
//				// "amount_to_pay").replace("$", ""));
//				totallinetotal += rec.getLineTotal();
//
//			} catch (Exception e) {
//			}
//		}
//		grid.addFooterValue("Total:" + "$" + totallinetotal, 3);
//		// setBottomLabelTitle("Total:" + "$" + totallinetotal, 3);
//		amtTextItem.setValue(DataUtils.getAmountAsString(totallinetotal));
//		noInWordsText.setValue(Utility.getNumberInWords(getTotal().toString()));
//	}
//
//	public Double getTotal() {
//		System.out.println("Total is  " + new Double(totallinetotal));
//		return new Double(totallinetotal);
//	}
//
//	public List<ClientTransactionItem> getallTransactions(
//			ClientTransaction object) {
//
//		List<IsSerializable> records = getRecords();
//		final List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
//		// // XXX NOT USED
//		// if (records != null) {
//		// for (IsSerializable re : records) {
//		// ClientWriteCheck rec = (ClientWriteCheck) re;
//		// final ClientTransactionItem item = new ClientTransactionItem();
//		// item.setTransaction(object);
//		// item.setType(ClientTransactionItem.TYPE_SALESTAX);
//		// // System.out.println("TaxCode name is   "
//		// // + rec.getAttribute("taxCode"));
//		// for (ClientTaxCode temp : allTaxCodes) {
//		// if (temp.getName().equalsIgnoreCase(
//		// rec.getAttribute("taxCode"))) {
//		// item.setTaxCode(temp.getID());
//		// break;
//		// }
//		// }
//		// item.setLineTotal(DataUtils.getBalance(rec
//		// .getAttribute("amount_to_pay")));
//		// transactionItems.add(item);
//		// }
//		// }
//		// return transactionItems;
//		return (ArrayList) records;
//	}
//
//	public void setAllTransactions(List<ClientTransactionItem> transactionItems) {
//
//		ClientTransactionItem allRecords[] = new ClientTransactionItem[transactionItems
//				.size()];
//		int i = 0;
//		for (ClientTransactionItem item : transactionItems) {
//			allRecords[i] = new ClientTransactionItem();
//			ClientTaxCode clientTaxCode = FinanceApplication.getCompany()
//					.getTaxCode(item.getTaxCode());
//			allRecords[i].setTaxCode(clientTaxCode.getID());
//			// allRecords[i].setAttribute("taxAgency", FinanceApplication
//			// .getCompany().getTaxAgency(clientTaxCode.getTaxAgency())
//			// .getName());
//			allRecords[i].setLineTotal(item.getLineTotal());
//			i++;
//		}
//		addRecords((ArrayList) Arrays.asList(allRecords));
//		calcTotal(null);
//	}
//
//	public void setGridDisabled() {
//
//		// isEdit = true;
//		setShowMenu(false);
//		canDeleteRecord(false);
//		// setEditEvent(ListGrid.NONE);
//		setEditDisableCells(0, 1, 2);
//
//	}
//
//	public void setTransactionPaySalesTax(
//			List<ClientTransactionPaySalesTax> transactionPaySalesTax) {
//
//		ClientTransactionPaySalesTax allRecords[] = new ClientTransactionPaySalesTax[transactionPaySalesTax
//				.size()];
//		int i = 0;
//		for (ClientTransactionPaySalesTax entry : transactionPaySalesTax) {
//			allRecords[i] = new ClientTransactionPaySalesTax();
//			allRecords[i].setTaxCode(entry.getTaxCode());
//			allRecords[i].setTaxAgency(entry.getTaxAgency());
//			allRecords[i].setTaxDue(entry.getTaxDue());
//			allRecords[i++].setAmountToPay(entry.getAmountToPay());
//		}
//		addRecords((ArrayList) Arrays.asList(allRecords));
//
//	}
//
//	public List<ClientTransactionPaySalesTax> getTransactionPaySalesTaxList(
//			ClientPaySalesTax paySalesTax) {
//
//		//  need to change later.
//		// List<TransactionPaySalesTax> tpsList = new
//		// ArrayList<TransactionPaySalesTax>();
//		// TransactionPaySalesTax entry;
//		//
//		// for (ListGridRecord record : getRecords()) {
//		// entry = new TransactionPaySalesTax();
//		//
//		// // Setting TaxAgency and TaxCode.
//		// for (TaxCode taxCode : allTaxCodes ) {
//		// if (taxCode.getName()
//		// .equals(record.getAttribute("taxCode"))) {
//		// entry.setTaxCode(taxCode);
//		// entry.setTaxAgency(taxCode.getTaxAgency());
//		// break;
//		// }
//		//
//		// }
//		//
//		// // Setting Tax Due.
//		// try {
//		// entry.setTaxDue(DataUtils.getAmountStringAsDouble(record
//		// .getAttribute("taxDue")));
//		// } catch (Exception e) {
//		// e.printStackTrace();
//		// }
//		//
//		// // Setting Amount to pay.
//		// try {
//		// entry.setAmountToPay(DataUtils.getAmountStringAsDouble(record
//		// .getAttribute("amount_to_pay")));
//		// } catch (Exception e) {
//		// e.printStackTrace();
//		// }
//		//
//		// // Setting Transaction.
//		// entry.setTransaction(paySalesTax);
//		//
//		// // adding this entry to the list.
//		// tpsList.add(entry);
//		//
//		// }
//		//
//		// return tpsList;
//		return paySalesTax.getTransactionPaySalesTax();
//	}
// }
