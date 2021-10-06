package com.nitya.accounter.web.client.ui.reports;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.PayheadCombo;

public class PayHeadReportToolBar extends DateRangeReportToolbar {

	protected PayheadCombo payheadCombo;
	private ClientPayHead selectedPayHead;
	
	ClientPayHead allPayhead = null;

	public PayHeadReportToolBar(AbstractReportView reportView) {
		super(reportView);
	}

	@Override
	protected void createControls() {
		payheadCombo = new PayheadCombo(messages.payhead()) {

			@Override
			public void initCombo(List<ClientPayHead> list) {
				if (allPayhead == null) {
					allPayhead = new ClientPayHead();
					allPayhead.setName(messages.all());
				}
				if (!list.contains(allPayhead)) {
					list.add(allPayhead);
				}
				super.initCombo(list);
			}
		};
		payheadCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayHead>() {

					@Override
					public void selectedComboBoxItem(ClientPayHead selectItem) {
						setSelectedPayHead(selectItem);
					}
				});

		super.createControls();
	}

	protected void setSelectedPayHead(ClientPayHead selectItem) {
		this.selectedPayHead = selectItem;
		changeDates(getStartDate(), getEndDate());
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		reportview.makeReportRequest(fromItem.getDate(), toItem.getDate());
	}

	@Override
	protected void accountData() {
		if (selectedPayHead != null) {
			accData(selectedPayHead);
		}
	}

	protected com.nitya.accounter.web.client.ui.forms.FormItem<?> getItem() {
		return payheadCombo;
	}

	protected void accData(ClientPayHead payHead) {
		if (payHead != null) {
			selectedPayHead = payHead;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectedPayHead.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
		}
	}

	public ClientPayHead getSelectedPayHead() {
		return payheadCombo.getSelectedValue();
	}

	public void setPayHead(long payHead) {
		payheadCombo.setSelectedId(payHead);
	}
}
