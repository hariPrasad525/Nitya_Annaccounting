package com.nitya.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.grids.DialogGrid;

/**
 * GroupDialog is Dialog which contains common controls to display and manage
 * all groups and group Lists .like tax groups,Customer Group List.
 * 
 * @author kumar kasimala
 * 
 * 
 */

public abstract class GroupDialog<T extends IAccounterCore> extends
		BaseDialog<T> {

	private StyledPanel buttonsLayout;
	private StyledPanel bodyLayout1;
	protected Button button1;
	private Button button2;
	private Button button3;
	protected DialogGrid listGridView;
	private GroupDialogButtonsHandler dialogButtonsHandler;
	private InputDialogHandler dialogHandler;
	public final static int FIRST_BUTTON = 1;
	public final static int SECOND_BUTTON = 2;
	public final static int THIRD_BUTTON = 3;

	private List<IsSerializable> recordsList = new ArrayList<IsSerializable>();

	private RecordAddhandler recordAddhandler;

	private boolean isEdit;
	private AccounterAsyncCallback<T> callBack;

	public GroupDialog(String title, String descript) {
		super(title, descript);
		this.getElement().setId("GroupDialog");
		initialise();
		getWidget().getParent().addStyleName("group_dialogue_list");
	}

	/*
	 */
	private void initialise() {

		bodyLayout1 = new StyledPanel("bodyLayout1");
		// mainPanel.setCellVerticalAlignment(bodyLayout1,
		// HasVerticalAlignment.ALIGN_TOP);
		// bodyLayout1.setWidth("100%");
		// bodyLayout1.setSpacing(5);

		listGridView = new DialogGrid(false) {
			@Override
			protected String getHeaderStyle(int index) {
				return GroupDialog.this.getHeaderStyle(index);
			}

			@Override
			protected String getRowElementsStyle(int index) {
				return GroupDialog.this.getRowElementsStyle(index);
			}
		};
		listGridView.setView(this);
		listGridView.addColumns(setColumns());
		listGridView.setColumnTypes(getColunmTypes());
		DialogGrid.BODY_WIDTH = 1;
		listGridView.isEnable = false;
		listGridView.init();
		// listGridView.setWidth("100%");
		initGrid(getRecords());

		/**
		 * buttons Layout
		 */
		buttonsLayout = new StyledPanel("buttonsLayout");

		button1 = new Button(messages.add());
		button1.setTitle(messages.clickThisTo(messages.addNewLine() + " "
				+ this.getText().replace(messages.manage(), ""), ""));
		// button1.setWidth("80px");

		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					firstButtonClick();

			}
		});

		button2 = new Button(messages.edit());
		button2.setEnabled(false);
		// button2.setWidth("80px");
		button2.setTitle(messages.clickThisTo(messages.edit(), this.getText()
				.replace(messages.manage(), "")));
		button2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					secondButtonClick();
			}
		});

		button3 = new Button(this.messages.remove());
		button3.setEnabled(false);
		// button3.setWidth("80px");
		button2.setTitle(messages.clickThisTo(messages.delete(), this.getText()
				.replace(messages.manage(), "")));
		button3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					thirdButtonClick();
			}
		});

		enableEditRemoveButtons(false);

		buttonsLayout.add(button1);
		buttonsLayout.add(button2);
		buttonsLayout.add(button3);
		cancelBtn.setText(messages.close());
		// button2.enabledButton();
		// button3.enabledButton();
		button1.setFocus(true);
		bodyLayout1.add(listGridView);
		if (Accounter.getUser().canDoInvoiceTransactions())
			bodyLayout1.add(buttonsLayout);
		setBodyLayout(bodyLayout1);
		cancelBtn.setTitle(this.messages.close());
		dialogHandler = new InputDialogHandler() {

			public void onCancel() {
				closeWindow();
			}

			public boolean onOK() {
				return true;
			}
		};
		addInputDialogHandler(dialogHandler);

	}

	public abstract String getHeaderStyle(int index);

	public abstract String getRowElementsStyle(int index);

	public Integer[] getColunmTypes() {
		return null;
	}

	public void firstButtonClick() {
		dialogButtonsHandler.onFirstButtonClick();
	}

	public void secondButtonClick() {
		dialogButtonsHandler.onSecondButtonClick();
		enableEditRemoveButtons(false);
	}

	public void thirdButtonClick() {
		dialogButtonsHandler.onThirdButtonClick();
	}

	protected void closeWindow() {
		if (dialogButtonsHandler != null)
			dialogButtonsHandler.onCloseButtonClick();

	}

	/**
	 * delete currently Selected Record
	 */
	public void deleteSelectedRecord() {
		deleteRecord();
		enableEditRemoveButtons(false);
	}

	/**
	 * Get total Records As Array
	 * 
	 * @return
	 */
	public IAccounterCore[] getRecordsAsArray() {
		return (IAccounterCore[]) this.listGridView.getRecords().toArray();
	}

	/**
	 * Get Total Records As List
	 * 
	 * @return
	 */

	public List<IsSerializable> getRecordsAsList() {
		return this.recordsList;
	}

	/**
	 * add Handler On Record add.default Implementation does nothing
	 * 
	 * @param addhandler
	 */
	public void addRecordAddHandler(RecordAddhandler addhandler) {
		this.recordAddhandler = addhandler;
	}

	/**
	 * Group Dialog Buttons handler
	 * 
	 * @param dialogButtonsHandler
	 */
	public void addGroupButtonsHandler(
			GroupDialogButtonsHandler dialogButtonsHandler) {
		this.dialogButtonsHandler = dialogButtonsHandler;
	}

	/**
	 * Change Title of Buttons
	 * 
	 * @param buttonType
	 * @param title
	 */
	public void setButtonTitle(int buttonType, String title) {
		switch (buttonType) {
		case FIRST_BUTTON:
			button1.setTitle(title);
			break;
		case SECOND_BUTTON:
			button2.setTitle(title);
			break;
		case THIRD_BUTTON:
			button3.setTitle(title);
			break;

		default:
			break;
		}
	}

	public IAccounterCore getSelectedRecord() {
		return (IAccounterCore) listGridView.getSelection();
	}

	public void enableEditRemoveButtons(boolean enable) {
		button2.setEnabled(enable);
		button3.setEnabled(enable);
	}

	public void refreshGrid() {

	}

	public void addButton(Button button) {
		buttonsLayout.add(button);
	}

	public DialogGrid getGrid() {
		return this.listGridView;
	}

	public void initGrid(List<T> resultrecords) {
		if (resultrecords != null) {

			List<T> records = resultrecords;

			if (records != null) {
				for (T t : records) {
					addToGrid(t);
				}

			}
		}
	}

	public void addToGrid(T objectToBeAdded) {
		listGridView.addData((IsSerializable) objectToBeAdded);
	}

	public abstract String[] setColumns();

	protected abstract List<T> getRecords();

	public void deleteCallBack() {
	}

	public void addCallBack() {
	}

	public void editCallBack() {
	}

	private void updateOrAddRecord(T obj) {
		listGridView.removeAllRecords();
		initGrid(getRecords());
		if (callBack != null) {
			callBack.onResultSuccess(obj);
		}
	}

	private void deleteRecord() {
		listGridView.deleteRecord(listGridView.getSelection());
	}

	public void deleteObject(IAccounterCore core) {
		Accounter.deleteObject(this, core);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
	}

	@Override
	protected void onLoad() {
		okbtn.setFocus(true);
		cancelBtn.setFocus(true);
		super.onLoad();
	}

	@Override
	public void deleteFailed(AccounterException accounterException) {
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		deleteRecord();
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		updateOrAddRecord((T) object);
		// if (callBack != null) {
		// callBack.onSuccess(object);
		// }
	}

	@Override
	public void saveFailed(AccounterException exception) {
		String errorString = AccounterExceptions.getErrorString(exception);
		Accounter.showError(errorString);
	}

	@Override
	public String toString() {
		return messages.classNameis() + this.getText();
	}

	public void addCallBack(AccounterAsyncCallback<T> callback) {
		this.callBack = callback;
	}

	protected void saveOrUpdate(final T core) {

		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				saveFailed(caught);
				caught.printStackTrace();
				// TODO handle other kind of errors
			}

			public void onResultSuccess(Long result) {
				if (core.getID() != 0) {
					core.setVersion(core.getVersion() + 1);
				}
				core.setID(result);
				Accounter.getCompany().processUpdateOrCreateObject(core);
				saveSuccess(core);
			}

		};
		if (core.getID() == 0) {
			Accounter.createCRUDService().create((IAccounterCore) core,
					transactionCallBack);
		} else {
			Accounter.createCRUDService().update((IAccounterCore) core,
					transactionCallBack);
		}

	}
}
