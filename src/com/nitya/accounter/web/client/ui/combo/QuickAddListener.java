package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.ISaveCallback;

public interface QuickAddListener<T extends IAccounterCore> extends
		ISaveCallback {
	T getData(String text);

	void onAddAllInfo(String text);

	void onCancel();
}
