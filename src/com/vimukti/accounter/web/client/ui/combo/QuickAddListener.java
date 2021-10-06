package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.ISaveCallback;

public interface QuickAddListener<T extends IAccounterCore> extends
		ISaveCallback {
	T getData(String text);

	void onAddAllInfo(String text);

	void onCancel();
}
