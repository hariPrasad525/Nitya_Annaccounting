//package com.vimukti.accounter.web.client.ui;
//
//import java.util.List;
//
//import com.vimukti.accounter.web.client.core.ClientTaxCode;
//import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
//import com.vimukti.accounter.web.client.ui.core.Action;
//import com.vimukti.accounter.web.client.ui.core.BaseListView;
//import com.vimukti.accounter.web.client.ui.core.ActionFactory;
//import com.vimukti.accounter.web.client.ui.grids.ManageSalesTaxCodeListGrid;
//
///**
// * 
// * @author Raj vimal
// * 
// */
//
//public class SalesTaxCodesView extends BaseListView<ClientTaxCode> {
//
//	protected List<ClientTaxCode> taxCodesList;
//
//	public SalesTaxCodesView(String trt) {
//
//	}
//
//	@Override
//	protected Action getAddNewAction() {
//
//		return ActionFactory.getAddEditSalesTaxCodeAction();
//	}
//
//	@Override
//	protected String getAddNewLabelString() {
//
//		return FinanceApplication.constants().addaNewTaxCode();
//	}
//
//	@Override
//	protected String getListViewHeading() {
//
//		return FinanceApplication.constants().taxCodesList();
//	}
//
//	@Override
//	public void initListCallback() {
//		// FinanceApplication.createGETService().getTaxCodes(this);
//
//	}
//
//	@Override
//	public void updateInGrid(ClientTaxCode objectTobeModified) {
//
//	}
//
//	/*
//	 * This method invoked when "CurrentView" combo value changed.And it filters
//	 * the records based on the type slected(Active/InActive)
//	 */
//	
//	@Override
//	protected void filterList(boolean isActive) {
//		grid.removeAllRecords();
//		List<ClientTaxCode> records = FinanceApplication.getCompany()
//				.getTaxcodes();
//			for (ClientTaxCode code : records) {
//				if (isActive) {
//					if (code.getIsActive() == true)
//						grid.addData(code);
//				} else if (code.getIsActive() == false) {
//					grid.addData(code);
//				}
//			}
//			if(grid.getRecords().isEmpty())
//				grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
//
//	}
//	
//	@Override
//	protected void initGrid() {
//		grid = new ManageSalesTaxCodeListGrid(false);
//		grid.init();
//		grid.setRecords(FinanceApplication.getCompany().getActiveTaxCodes());
//		grid.setHeight("320px");
//	}
//
//	@Override
//	public void fitToSize(int height, int width) {
//		
//
//	}
//
//	@Override
//	public void onEdit() {
//		
//
//	}
//
//	@Override
//	public void print() {
//		
//
//	}
//
//	@Override
//	public void printPreview() {
//		
//
//	}
//}=======
////package com.vimukti.accounter.web.client.ui;
////
////import java.util.List;
////
////import com.google.gwt.core.client.GWT;
////import com.vimukti.accounter.web.client.core.ClientTaxCode;
////import com.vimukti.accounter.web.client.core.Lists.PayeeList;
////import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
////import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
////import com.vimukti.accounter.web.client.ui.core.Action;
////import com.vimukti.accounter.web.client.ui.core.BaseListView;
////import com.vimukti.accounter.web.client.ui.core.ActionFactory;
////import com.vimukti.accounter.web.client.ui.customers.CustomersMessages;
////import com.vimukti.accounter.web.client.ui.grids.ManageSalesTaxCodeListGrid;
////
/////**
//// * 
//// * @author Raj vimal
//// * 
//// */
////
////public class SalesTaxCodesView extends BaseListView<ClientTaxCode> {
////
////	protected List<ClientTaxCode> taxCodesList;
////
////	public SalesTaxCodesView(String trt) {
////
////	}
////
////	@Override
////	protected Action getAddNewAction() {
////
////		return ActionFactory.getAddEditSalesTaxCodeAction();
////	}
////
////	@Override
////	protected String getAddNewLabelString() {
////
////		return FinanceApplication.constants().addaNewTaxCode();
////	}
////
////	@Override
////	protected String getListViewHeading() {
////
////		return FinanceApplication.constants().taxCodesList();
////	}
////
////	@Override
////	public void initListCallback() {
////		// FinanceApplication.createGETService().getTaxCodes(this);
////
////	}
////
////	@Override
////	public void updateInGrid(ClientTaxCode objectTobeModified) {
////
////	}
////
////	/*
////	 * This method invoked when "CurrentView" combo value changed.And it filters
////	 * the records based on the type slected(Active/InActive)
////	 */
////	
////	@Override
////	protected void filterList(boolean isActive) {
////		grid.removeAllRecords();
////		List<ClientTaxCode> records = FinanceApplication.getCompany()
////				.getTaxcodes();
////			for (ClientTaxCode code : records) {
////				if (isActive) {
////					if (code.getIsActive() == true)
////						grid.addData(code);
////				} else if (code.getIsActive() == false) {
////					grid.addData(code);
////				}
////			}
////			if(grid.getRecords().isEmpty())
////				grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
////
////	}
////	
////	@Override
////	protected void initGrid() {
////		grid = new ManageSalesTaxCodeListGrid(false);
////		grid.init();
////		grid.setRecords(FinanceApplication.getCompany().getActiveTaxCodes());
////		grid.setHeight("320px");
////	}
////
////	@Override
////	public void fitToSize(int height, int width) {
////		
////
////	}
////
////	@Override
////	public void onEdit() {
////		
////
////	}
////
////	@Override
////	public void print() {
////		
////
////	}
////
////	@Override
////	public void printPreview() {
////		
////
////	}
// // }>>>>>>> .merge-right.r20318
