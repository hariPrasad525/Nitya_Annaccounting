package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;

public class QuickAddListenerImpl<T extends IAccounterCore> implements
		QuickAddListener<T> {
	DropDownCombo<T> combo;

	public QuickAddListenerImpl(DropDownCombo<T> combo) {
		this.combo = combo;
	}

	@Override
	public void onAddAllInfo(String text) {
		combo.onAddAllInfo(text);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		Accounter.showError(exception.getMessage());
	}

	@Override
	public T getData(String text) {
		return combo.getQuickAddData(text);
	}

	@Override
	public void onCancel() {
		combo.changeValue(-1);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		combo.addItemThenfireEvent((T) object);
	}

}