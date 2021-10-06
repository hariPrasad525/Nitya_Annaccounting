package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.ChalanDetailsListGrid;

public class ChalanDetailsListView extends
		TransactionsListView<ClientTDSChalanDetail> {

	private ArrayList<ClientTDSChalanDetail> listOfCHalans;
	private boolean fromTransactionCenter;

	public ChalanDetailsListView(boolean fromTransactionCenter) {
		this.fromTransactionCenter = fromTransactionCenter;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void updateInGrid(ClientTDSChalanDetail objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createListForm(DynamicForm form) {

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);
		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	protected void initGrid() {
		grid = new ChalanDetailsListGrid(fromTransactionCenter);
		grid.init();
	}

	@Override
	public void initListCallback() {
		onPageChange(0, getPageSize());
	}

	@Override
	protected String getListViewHeading() {
		return messages.challanListView();
	}

	@Override
	protected Action<ClientTDSChalanDetail> getAddNewAction() {
		TDSChalanDetailsAction tdsChalanDetailsAction = new TDSChalanDetailsAction();
		tdsChalanDetailsAction.isFromListView(true);
		return tdsChalanDetailsAction;
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addChallanDetails();
	}

	@Override
	protected String getViewTitle() {
		return messages.challanListView();
	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getTDSChalanDetailsList(this);
	}

	@Override
	public void onSuccess(PaginationList<ClientTDSChalanDetail> result) {
		// super.onSuccess(result);
		grid.removeLoadingImage();
		listOfCHalans = result;
		filterList(true);
		// grid.sort(10, false);
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();

		grid.setRecords(listOfCHalans);
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	protected void filterList(String selectedValue) {
		onPageChange(start, getPageSize());
	}

}
