package com.nitya.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.combo.AccountCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;

public class AccountReportToolBar extends DateRangeReportToolbar {

	private AccountCombo accountCombo;
	private ClientAccount selectAccount = null;

	public AccountReportToolBar(AbstractReportView reportView) {
		super(reportView);
	}

	@Override
	protected void createControls() {
		accountCombo = new AccountCombo(messages.Account(), false) {

			@Override
			protected List<ClientAccount> getAccounts() {
				List<ClientAccount> list = new ArrayList<ClientAccount>();
				list.addAll(getCompany().getAccounts(ClientAccount.TYPE_BANK));
				list.addAll(getCompany().getAccounts(
						ClientAccount.TYPE_OTHER_CURRENT_ASSET));
				if (reportview instanceof ReconciliationDiscrepancyReport) {
					list.addAll(getCompany().getAccounts(
							ClientAccount.TYPE_CREDIT_CARD));
				}
				return list;
			}
		};
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						setAccId(selectItem.getID());
					}
				});

		super.createControls();
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		if (selectAccount != null) {
			reportview.makeReportRequest(selectAccount.getID(),
					fromItem.getDate(), toItem.getDate());
		} else {
			reportview.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	protected void accountData() {
		if (getAccId() != 0) {
			accData(Accounter.getCompany().getAccount(getAccId()));
		}
	}

	protected com.nitya.accounter.web.client.ui.forms.FormItem<?> getItem() {
		return accountCombo;
	}

	protected void accData(ClientAccount selectItem) {
		if (selectItem != null) {
			selectAccount = selectItem;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectAccount.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
			accountCombo.setSelected(selectAccount.getName());
		}
	}
}
