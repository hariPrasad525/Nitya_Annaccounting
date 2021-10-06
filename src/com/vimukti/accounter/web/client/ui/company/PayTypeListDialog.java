//package com.vimukti.accounter.web.client.ui.company;
//
//import java.util.List;
//
//import com.vimukti.accounter.web.client.core.ClientCustomer;
//import com.vimukti.accounter.web.client.ui.Accounter;
//import com.vimukti.accounter.web.client.ui.core.GroupDialog;
//import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
//import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
//import com.vimukti.accounter.web.client.ui.grids.ListGrid;
//
//public class PayTypeListDialog extends GroupDialog<ClientCustomer> {
//
//	protected GroupDialogButtonsHandler groupDialogButtonHandler;
//
//	public PayTypeListDialog(String title, String descript) {
//		super(title, descript);
//
//		initialise();
//		center();
//	}
//
//	private void initialise() {
//
//		setWidth("30%");
//
//		final String title;
//
//		final String description;
//
//		title = messages.addOrEditPayType();
//		description = messages.toAddPayType();
//		DialogGrid grid = getGrid();
//		grid.addColumn(ListGrid.COLUMN_TYPE_TEXT, messages
//				.active());
//		grid.addColumn(ListGrid.COLUMN_TYPE_TEXT, messages
//				.description());
//
//		groupDialogButtonHandler = new GroupDialogButtonsHandler() {
//
//			public void onCloseButtonClick() {
//
//			}
//
//			public void onFirstButtonClick() {
//
//				// new AddOrEditPayTypeDialog(title, description).show();
//			}
//
//			public void onSecondButtonClick() {
//
//				// new AddOrEditPayTypeDialog(title, description).show();
//			}
//
//			public void onThirdButtonClick() {
//
//			}
//
//		};
//		addGroupButtonsHandler(groupDialogButtonHandler);
//	}
//
//	@Override
//	public Object getGridColumnValue(ClientCustomer obj, int index) {
//
//		return null;
//	}
//
//	@Override
//	public String[] setColumns() {
//
//		return null;
//	}
//
//	@Override
//	protected List getRecords() {
//
//		return null;
//	}
//
//	@Override
//	protected boolean onOK() {
//		return true;
//	}
//
//	@Override
//	public void setFocus() {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
