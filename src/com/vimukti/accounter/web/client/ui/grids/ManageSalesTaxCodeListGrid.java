/**
 * 
 */
//package com.vimukti.accounter.web.client.ui.grids;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.vimukti.accounter.web.client.core.AccounterCoreType;
//import com.vimukti.accounter.web.client.core.ClientTaxCode;
//import com.vimukti.accounter.web.client.core.IAccounterCore;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.core.ActionFactory;
//import com.vimukti.accounter.web.client.ui.core.ViewManager;
//
///**
// * @author Murali.A
// * 
// */
//public class ManageSalesTaxCodeListGrid extends BaseListGrid<ClientTaxCode> {
//	Map<Integer, Integer> colsMap = new HashMap<Integer, Integer>();
//	public ManageSalesTaxCodeListGrid() {
//		super(false, true);
//	}
//	/**
//	 * @param isMultiSelectionEnable
//	 */
//	public ManageSalesTaxCodeListGrid(boolean isMultiSelectionEnable) {
//		super(isMultiSelectionEnable);
//	}
//
//	/*
//	 * @param taxcode--The taxcode object
//	 * 
//	 * @param col--the index of the column
//	 */
//	@Override
//	protected Object getColumnValue(ClientTaxCode taxCode, int col) {
//		switch (col) {
//		case 0:
//			return taxCode.getName() != null ? taxCode.getName() : "";
//		case 1:
//			return taxCode.getDescription() != null ? taxCode.getDescription()
//					: "";
//		case 2:
//			return FinanceApplication.getFinanceMenuImages().delete();
//			// return "/images/delete.png";
//		}
//		return "";
//	}
//
//	/*
//	 * 
//	 * @see com.vimukti.accounter.web.client.ui.grids.ListGrid#getColumns()
//	 * 
//	 * @return an array of columnNames which'll displayed in header part of the
//	 * grid
//	 */
//	@Override
//	protected String[] getColumns() {
//		return new String[] { FinanceApplication.constants().taxCode(),
//				FinanceApplication.constants().description(), "" };
//	}
//
//	@Override
//	public void onDoubleClick(ClientTaxCode obj) {
//		ActionFactory.getAddEditSalesTaxCodeAction().run(obj, true);
//
//	}
//
//	@Override
//	protected int[] setColTypes() {
//		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
//				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
//	}
//
//	@Override
//	public boolean validateGrid() {
//		return true;
//	}
//
//	@Override
//	protected int getCellWidth(int index) {
//	
//		if (index == 2) {
//			if (UIUtils.isMSIEBrowser())
//				return 25;
//			else
//				return 15;
//		}
//		return -1;
//	}
//
//	@Override
//	protected void onClick(ClientTaxCode obj, int row, int col) {
//		switch (col) {
//		case 2:
//			executeDelete(obj);
//			break;
//		default:
//			break;
//		}
//	}
//
//	protected void executeDelete(final ClientTaxCode recordToBeDeleted) {
//		ViewManager.getInstance().deleteObject(recordToBeDeleted,
//				AccounterCoreType.TAX_CODE, this);
//
//	}
//
//	@Override
//	public void processupdateView(IAccounterCore core, int command) {
//
//	}
//
//	public AccounterCoreType getType() {
//		return AccounterCoreType.TAX_CODE;
//	}
//
// }
