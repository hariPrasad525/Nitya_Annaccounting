package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;

public class CreateJobIdToolBar extends CreateStatementToolBar {

	private ClientJob selectedJob;

	public CreateJobIdToolBar(boolean isVendor, AbstractReportView reportView) {
		super(isVendor, reportView);
		getViewSelect().setVisible(false);

	}

	@Override
	protected void createDisplayJobCombo(ClientCustomer selectedCustomer) {

		jobCombo.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientJob>() {
			@Override
			public void selectedComboBoxItem(ClientJob selectItem) {
				selectedJob = selectItem;
				setJobId(selectItem.getID());
				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						fromItem.getDate(), toItem.getDate());
			}
		});
		if (getPayeeId() != 0) {
			jobCombo.setCustomer(Accounter.getCompany().getCustomer(
					getPayeeId()));
		}
		if (getJobId() != 0) {
			jobData();
		}
	}

	@Override
	protected void payeeData() {
		super.payeeData();
		if (getPayeeId() != 0) {
			jobCombo.setCustomer(Accounter.getCompany().getCustomer(
					getPayeeId()));
		}
	}

	@Override
	protected void jobData() {
		if (getJobId() > 0) {
			ClientJob selectItem = Accounter.getCompany().getjob(getJobId());
			if (selectItem != null) {
				selectedJob = selectItem;
				ClientFinanceDate startDate = fromItem.getDate();
				ClientFinanceDate endDate = toItem.getDate();
				reportview.removeEmptyStyle();
				jobCombo.setSelected(selectedJob.getName());
				reportview.makeReportRequest(getPayeeId(), startDate, endDate);
			}
		}
	}
}
