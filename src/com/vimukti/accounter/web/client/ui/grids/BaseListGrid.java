package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.IDeleteCallback;
import com.vimukti.accounter.web.client.ui.ISaveCallback;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.IAccounterWidget;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public abstract class BaseListGrid<T> extends ListGrid<T> implements
		IAccounterWidget, ISaveCallback, IDeleteCallback {

	private final List<Integer> cellsWidth = new ArrayList<Integer>();
	protected IAccounterCRUDServiceAsync rpcDoSerivce;
	private int[] columnType;
	protected double total;
	protected String viewType;

	// long id;

	BaseListView<T> view;
	private String[] headerStyles;
	private String[] rowElementStyles;

	public BaseListView<T> getView() {
		return view;

	}

	public void setView(BaseListView<T> view) {
		this.view = view;

	}

	public BaseListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	public void init() {
		initRPCService();
		this.columnType = setColTypes();
		this.headerStyles = setHeaderStyle();
		this.rowElementStyles = setRowElementsStyle();
		super.init();
	}

	int type;

	public BaseListGrid(boolean isMultiSelectionEnable, int type) {
		super(isMultiSelectionEnable);
		this.type = type;
	}

	public BaseListGrid(boolean isMultiSelectionEnable, boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
	}

	@Override
	protected int getCellWidth(int index) {
		if (cellsWidth.size() > index)
			return cellsWidth.get(index);
		return -1;
	}

	@Override
	protected int getColumnType(int col) {

		return columnType[col];
	}

	@Override
	protected String getHeaderStyle(int index) {
		return headerStyles[index];
	}

	@Override
	protected String getRowElementsStyle(int index) {
		return rowElementStyles[index];
	}

	@Override
	protected String[] getSelectValues(T obj, int col) {
		return null;
	}

	@Override
	protected boolean isEditable(T obj, int row, int col) {
		return false;
	}

	@Override
	protected int sort(T obj1, T obj2, int index) {
		return 0;
	}

	@Override
	protected void onClick(T obj, int row, int col) {
		if ((this instanceof ReconcilListGrid) == false) {
			if (col == 6 && !this.isVoided(obj)) {
				showWarningDialog(obj, this.getAccounterCoreType(obj),
						this.getTransactionID(obj), col);
			}
		}
		// else if (col == 7) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	private long getTransactionID(T obj) {
		return 0;
	}

	public boolean isVoided(T obj) {
		return false;
	}

	public AccounterCoreType getAccounterCoreType(T obj) {
		return null;
	}

	protected void showWarningDialog(T obj, final AccounterCoreType coreType,
			final long transactionsID, final int col) {
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
						if (col == 6)
							voidTransaction(coreType, transactionsID);
						// else if (col == 7)
						// deleteTransaction(obj);
						return true;

					}

				});
	}

	protected void initRPCService() {
		this.rpcDoSerivce = Accounter.createCRUDService();

	}

	// public void setColumnType(int[] columnType) {
	// this.columnType = setColTypes();
	// }

	abstract protected int[] setColTypes();

	abstract protected String[] setHeaderStyle();

	abstract protected String[] setRowElementsStyle();

	protected void showWarnDialog(final T object) {
		Accounter.showWarning(messages
				.doyouwanttoDeleteObj(((IAccounterCore) object).getName()),
				AccounterType.WARNING, new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() {
						executeDelete(object);
						return true;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onCancelClick() {
						return false;
					}
				});
	}

	protected abstract void executeDelete(T object);

	@Override
	protected void onValueChange(T obj, int col, Object value) {

	}

	public Double getTotal() {
		return 0.0;
	}

	public AccounterCoreType getType() {
		return null;
	}

	@Override
	protected void onDoubleClick(T obj, int row, int index) {
		onDoubleClick(obj);
	};

	public void setViewType(String type) {
		viewType = type;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
		caught.fillInStackTrace();
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// Accounter.showInformation("Deleted Successfully");
		deleteRecord(this.getSelection());
		if (view != null) {
			view.deleteSuccess(result);
		}
	}

	@Override
	public void saveFailed(AccounterException exception) {
		// if (exception instanceof InvalidOperationException) {
		// Accounter.showError(exception.getMessage());
		// } else
		// Accounter.showError(messages.updationFailed());
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions
				.getErrorString(accounterException);
		Accounter.showError(errorString);
	}

	// public long getID() {
	// return this.id;
	// }
	//
	// public void setID(long id) {
	// this.id = id;
	// }

	public void setTotal() {
	}

	@Override
	public void addRecords(List<T> list) {
		super.addRecords(list);
	}

	@Override
	public void saveSuccess(IAccounterCore core) {
		if (core != null) {
			view.initListCallback();
		}
	}

	protected <D extends IAccounterCore> void deleteUserObject(D data) {
		Accounter.deleteUser(this, data);
	}

	protected <D extends IAccounterCore> void deleteObject(D data) {
		Accounter.deleteObject(this, data);
	}

	protected void voidTransaction(AccounterCoreType coreType,
			long transactionsID) {
		Accounter.voidTransaction(this, coreType, transactionsID);
	}

	public <D extends IAccounterCore> void createOrUpdate(D core) {
		Accounter.createOrUpdate(this, core);
	}

	public boolean isCanOpenTransactionView(int saveStatus, int transactionType) {
		ClientUser user = Accounter.getUser();
		if (user.isAdmin()) {
			return true;
		}
		ClientUserPermissions permissions = user.getPermissions();
		if (saveStatus == ClientTransaction.STATUS_DRAFT
				&& permissions.getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
			return true;
		}

		return Utility.isUserHavePermissions(transactionType);
	}
}
