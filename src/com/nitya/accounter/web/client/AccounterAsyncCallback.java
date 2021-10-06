package com.nitya.accounter.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.nitya.accounter.web.client.core.StartupException;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;

/**
 * 
 * TODO Show Processing Dialog if Rasponse is taking time
 * 
 * @author Prasanna Kumar G
 * 
 */
public abstract class AccounterAsyncCallback<T> implements AsyncCallback<T> {

	// protected PopupPanel processDialog;

	public AccounterAsyncCallback() {
		// processDialog = UIUtils.getLoadingMessageDialog(Accounter.constants()
		// .processingRequest());
		//
		// processDialog.center();
	}

	@Override
	public void onFailure(Throwable exception) {
		if (Accounter.isShutdown()) {
			return;
		}
		// processDialog.removeFromParent();
		if (exception instanceof AccounterException) {
			AccounterException accounterException = (AccounterException) exception;
			onException(accounterException);
			return;
		} else if (exception instanceof StatusCodeException) {
			if (((StatusCodeException) exception).getStatusCode() == 403) {
				Accounter.showMessage(Global.get().messages().sessionExpired());
				Accounter.getMainFinanceWindow().onSessionExpired();
			} else {

				Accounter.showInformation(Global.get().messages()
						.unableToPerformTryAfterSomeTime());
			}
		} else if (exception instanceof StartupException) {
			onException(new AccounterException(exception.getMessage()));
		}
		exception.printStackTrace();

	}

	public abstract void onException(AccounterException exception);

	public abstract void onResultSuccess(T result);

	public void onSuccess(T result) {
		// processDialog.removeFromParent();
		onResultSuccess(result);
	};
}
