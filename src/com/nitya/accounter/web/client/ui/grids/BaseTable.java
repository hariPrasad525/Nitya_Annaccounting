/**
 * 
 */
package com.nitya.accounter.web.client.ui.grids;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.IDeleteCallback;
import com.nitya.accounter.web.client.ui.ISaveCallback;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.grids.columns.CustomCellTable;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class BaseTable<T> extends CustomCellTable<T> implements
		ISaveCallback, IDeleteCallback {

	AccounterMessages messages=Global.get().messages();
	public void init() {
		super.init();
		// Initiates the Columns
		initColumns();
	}

	/**
	 * 
	 */
	protected abstract void initColumns();

	protected void showWarningDialog(T obj, final AccounterCoreType coreType,
			final long transactionsID) {
		String msg = null;
		msg = messages.doyouwanttoVoidtheTransaction();
		// else if (col == 7) {
		// if (!viewType.equalsIgnoreCase("Deleted"))
		// msg = "Do you want to Delete the Transaction";
		//
		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						voidTransaction(transactionsID, coreType);
						return true;

					}

				});
	}

	private void voidTransaction(long transactionID, AccounterCoreType coreType) {
		Accounter.voidTransaction(this, coreType, transactionID);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
		caught.fillInStackTrace();
	}

	@Override
	public void deleteSuccess(IAccounterCore result){
		// Accounter.showInformation("Deleted Successfully");
		// TODO
		// deleteRecord(this.getSelection());
	}

	@Override
	public void saveFailed(AccounterException exception) {
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {

	}

}
