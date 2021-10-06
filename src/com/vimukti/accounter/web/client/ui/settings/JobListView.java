package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.customers.NewJobAction;
import com.vimukti.accounter.web.client.ui.grids.JobListGrid;

public class JobListView extends BaseListView<ClientJob> implements
		IPrintableView {

	public JobListView() {
		this.getElement().setId("JobListView");
	}

	@Override
	public void updateInGrid(ClientJob objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getJobs(this);
	}

	@Override
	public void onSuccess(PaginationList<ClientJob> result) {
		super.onSuccess(result);

	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return false;
	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new JobListGrid();
		grid.init();

	}

	@Override
	protected String getListViewHeading() {
		return messages.jobList();
	}

	@Override
	protected Action getAddNewAction() {
		return new NewJobAction(null);
	}

	@Override
	protected String getAddNewLabelString() {
		if (Utility.isUserHavePermissions(AccounterCoreType.JOB)) {
			return messages.addNew(messages.job());
		}
		return null;

	}

	@Override
	protected String getViewTitle() {
		return messages.jobList();
	}

}
