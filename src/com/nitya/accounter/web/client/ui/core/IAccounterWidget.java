package com.nitya.accounter.web.client.ui.core;

import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;

public interface IAccounterWidget {

	public void saveFailed(AccounterException exception);

	public void saveSuccess(IAccounterCore object);

	public void deleteFailed(AccounterException caught);

	public void deleteSuccess(IAccounterCore result);

}
