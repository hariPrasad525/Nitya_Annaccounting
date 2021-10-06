package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.CreditRatingListDialog;

public class CreditRatingCombo extends CustomCombo<ClientCreditRating> {

	public CreditRatingCombo(String title) {
		super(title, "creditRatingCombo");
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.creditRating();
	}

	@Override
	protected String getDisplayName(ClientCreditRating object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		CreditRatingListDialog creditRatingDialog = new CreditRatingListDialog(
				"", "");
		creditRatingDialog.hide();
		creditRatingDialog
				.addCallBack(new AccounterAsyncCallback<ClientCreditRating>() {

					@Override
					public void onResultSuccess(ClientCreditRating result) {
						createAddNewCallBack().onResultSuccess(result);
					}

					@Override
					public void onException(AccounterException exception) {
						createAddNewCallBack().onException(exception);
					}
				});
		creditRatingDialog.showAddEditGroupDialog(null);
	}

	@Override
	protected String getColumnData(ClientCreditRating object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
