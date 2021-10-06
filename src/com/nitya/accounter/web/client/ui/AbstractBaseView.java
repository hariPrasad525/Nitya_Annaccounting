/**
 * 
 */
package com.nitya.accounter.web.client.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.IAccounterCRUDServiceAsync;
import com.nitya.accounter.web.client.IAccounterGETServiceAsync;
import com.nitya.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.ValidationResult.Error;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.core.AbstractView;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.core.IAccounterWidget;

/**
 * This Class serves as the Base Root Class for all the views, in Accounter GUI,
 * providing common Functionality (This includes CustomerView (Customer Creation
 * and Editing), VendorView , Account View, ListViews, etc., and all Transaction
 * Related View Classes
 * 
 * 
 * @param <T>
 */
public abstract class AbstractBaseView<T> extends AbstractView<T> implements
		IAccounterWidget, WidgetWithErrors, ISaveCallback, IDeleteCallback {

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	public AbstractBaseView() {
		sinkEvents(Event.ONCHANGE | Event.KEYEVENTS);
		initRPCService();
		this.addStyleName("abstract_base_view");
		this.errorPanel = new StyledPanel("errorPanel");
		this.errorPanel.addStyleName("errors");
	}

	protected abstract String getViewTitle();

	/**
	 * To Maintain a list of all Accounts, for Every View, (Optional)
	 */
	// protected List<Account> accounts;
	/**
	 * Optional to Deleted in Future
	 */
	// XXX if required
	// protected FinanceGrid<T> view;
	/**
	 * Base reference for all RPC GET Services
	 */
	protected IAccounterGETServiceAsync rpcGetService;

	@Override
	public void init() {
		clearAllErrors();
		errorPanel.addStyleName("error-panel");
		add(errorPanel);
	}

	/**
	 * Base reference for all RPC DO Services
	 */
	protected IAccounterCRUDServiceAsync rpcDoSerivce;

	/**
	 * Base reference for all RPC Util Service
	 */
	protected IAccounterHomeViewServiceAsync rpcUtilService;

	/**
	 * Member Flag Variable to determine, whether to Save And Close or Save and
	 * New
	 */
	protected boolean saveAndClose;

	// protected List<DynamicForm> forms = new ArrayList<DynamicForm>();

	// protected List<FormItem> formItems = new ArrayList<FormItem>();

	// public boolean yesClicked;

	/**
	 * CallBack Functionality to Support the Add New Feature of Custom Combo
	 * Boxes Like say, Post, creating a Customer, the Customer object should be
	 * added back to the Combo list.
	 */
	private AccounterAsyncCallback<Object> callback;

	// protected IAccounterReportServiceAsync rpcReportService;

	// public boolean isRegister;

	// private DialogBox dialog;

	// private boolean isViewModfied;
	private StyledPanel errorPanel;
	private Map<Object, Widget> errorsMap = new HashMap<Object, Widget>();
	protected boolean isDirty;

	protected Set<Object> lastErrorSourcesFromValidation = new HashSet<Object>();

	// /**
	// * Convenience Method to Set CallBack
	// *
	// * @param callBack
	// */
	// public final void setCallBack(AccounterAsyncCallback<Object> callBack) {
	//
	// this.setCallback(callBack);
	//
	// }

	protected void initRPCService() {
		this.rpcGetService = Accounter.createGETService();
		this.rpcDoSerivce = Accounter.createCRUDService();
		this.rpcUtilService = Accounter.createHomeService();
		// this.rpcReportService = Accounter.createReportService();

	}

	/**
	 * Called when any Saving any Transactional / Non-Transactional View has
	 * Failed to save.
	 */
	public void saveFailed(AccounterException exception) {
		changeButtonBarMode(false);
		// if (dialog != null) {
		// dialog.removeFromParent();
		// }

		// Accounter.showError(exception.getMessage());

		// SC.logWarn(exception.getMessage());
		/*
		 * if (saveAndCloseButton != null) {
		 * saveAndCloseButton.setDisabled(false); }
		 * 
		 * if (saveAndNewButton != null) { saveAndNewButton.setDisabled(false);
		 * }
		 */

		// saveAndCloseButton.getParentElement().enable();
	}

	/**
	 * Called when Any Non-Transaction View is Success
	 */
	@Override
	public void saveSuccess(IAccounterCore object) {
		if (this.getCallback() != null) {
			this.getCallback().onResultSuccess(object);
		}
		if (saveAndClose) {
			getManager().closeCurrentView();
		} else {
			// if (!History.getToken().equals(getAction().getHistoryToken())) {
			//
			// }
			if (getManager() != null) {
				getManager().closeCurrentView(false);
			}
			if (getAction() != null) {
				getAction().run(null, getAction().isDependent());
			}
		}
	}

	public ValidationResult validate() {
		// TO BE OVERRIDEN
		return new ValidationResult();
	}

	public void saveAndUpdateView() {
		// TO BE OVERRIDDEN
	}

	protected void refreshData() {
		// TODDO Refresh the View Data
	}

	public void setData(T data) {
		super.setData(data);
	}

	public void disableSaveButtons() {
		//
		// if (this.saveAndCloseButton != null) {
		//
		// this.saveAndCloseButton.setDisabled(true);
		// }
		//
		// if (this.saveAndNewButton != null) {
		//
		// this.saveAndNewButton.setDisabled(true);
		// }

	}

	public void enableSaveButtons() {

		// if (this.saveAndCloseButton != null) {
		//
		// this.saveAndCloseButton.setDisabled(false);
		// }
		//
		// if (this.saveAndNewButton != null) {
		//
		// this.saveAndNewButton.setDisabled(false);
		// }

	}

	protected <P extends IAccounterCore> void saveOrUpdateUser(final P core) {
		Accounter.inviteUser(this, core);
	}

	protected <P extends IAccounterCore> void saveOrUpdate(final P core) {
		Accounter.createOrUpdate(this, core);
	}

	protected <P extends IAccounterCore> void deleteObject(final P core) {
		Accounter.deleteObject(this, core);
	}

	@Override
	public abstract void setFocus();

	//
	// @Override
	// public void saveSuccess(IAccounterCore object) {
	// saveSuccess((T) object);
	// }
	@Override
	public String toString() {
		return messages.actionClassNameis(this.getAction().getText());
	}

	// @Override
	// public void onBrowserEvent(Event event) {
	// Element element = DOM.eventGetTarget(event);
	//
	// switch (DOM.eventGetType(event)) {
	// case Event.ONKEYPRESS:
	// if (Arrays.asList("INPUT", "TEXTAREA").contains(
	// element.getTagName()))
	// isViewModfied = true;
	//
	// break;
	// case Event.ONCHANGE:
	// if (Arrays.asList("SELECT", "RADIO", "CHECKBOX", "INPUT",
	// "TEXTAREA").contains(element.getTagName())) {
	// isViewModfied = true;
	// }
	// if (Arrays.asList("SELECT", "RADIO", "CHECKBOX").contains(
	// element.getTagName()))
	// isViewModfied = true;
	//
	// break;
	// default:
	// break;
	// }
	// super.onBrowserEvent(event);
	// }

	// public boolean isViewModfied() {
	// return isViewModfied;
	// }

	// public void setViewModfied(boolean isViewModified) {
	// this.isViewModfied = isViewModified;
	// }

	public ClientCompany getCompany() {
		return Accounter.getCompany();

	}

	public String getBaseCurrencyAsString() {
		return "";

	}

	public String getCurrencyAsString(long id) {
		return "";

	}

	/**
	 * Adds Error
	 * 
	 * @param item
	 * @param erroMsg
	 */
	public void addError(Object item, String erroMsg) {
		Widget widget = errorsMap.get(item);
		if (widget != null) {
			errorPanel.remove(widget);
		}
		HTML error = new HTML("<li>" + messages.errorMsg(erroMsg) + "</li>");
		error.addStyleName("error");
		this.errorPanel.add(error);
		this.errorPanel.setVisible(true);
		this.errorsMap.put(item, error);
		// Now make sure that error is visible to user
		errorPanel.getElement().scrollIntoView();
	}

	/**
	 * Clears All Errors
	 */
	public void clearAllErrors() {
		this.errorsMap.clear();
		this.errorPanel.clear();
		this.errorPanel.setVisible(false);
	}

	private boolean isSaveCliecked = false;

	/**
	 * Clears the given Error
	 * 
	 * @param obj
	 */
	public void clearError(Object obj) {
		Widget remove = this.errorsMap.remove(obj);
		if (remove != null) {
			this.errorPanel.remove(remove);
			if (this.errorsMap.isEmpty()) {
				errorPanel.setVisible(false);
			}
		}
	}

	public void setCloseOnSave(boolean closeOnSave) {
		this.saveAndClose = true;
	}

	/**
	 * 
	 * 
	 * @param b
	 */
	public void onSave(boolean reopen) {
		validationAndSave(reopen,false);
		
	}
	public void validationAndSave(boolean reopen,boolean isMail) {
		changeButtonBarMode(true);
		this.saveAndClose = !reopen;
		for (Object errorSource : lastErrorSourcesFromValidation) {
			clearError(errorSource);
		}
		lastErrorSourcesFromValidation.clear();

		ValidationResult validationResult = this.validate();
		if (validationResult.haveErrors()) {
			for (Error error : validationResult.getErrors()) {
				addError(error.getSource(), error.getMessage());
				lastErrorSourcesFromValidation.add(error.getSource());
			}
		}
		if (!errorsMap.isEmpty()) {
			changeButtonBarMode(false);
			return;
		}
		if (validationResult.haveWarnings()) {

			new WarningsDialog(validationResult.getWarningsAsList(),
					new ErrorDialogHandler() {

						@Override
						public boolean onYesClick() {
							saveAndUpdateView();
							saveActivity();
							setSaveCliecked(true);
							return true;
						}

						@Override
						public boolean onNoClick() {
							changeButtonBarMode(false);
							return true;
						}

						@Override
						public boolean onCancelClick() {
							
							changeButtonBarMode(false);
							return true;
						}
					});
		}
		else if(!isMail){
		
			saveAndUpdateView();
			saveActivity();
			setSaveCliecked(true);
		}
		else {
			saveAndEmailView();
//			saveActivity();
//			setSaveCliecked(true);
		}
	}
	
	private void saveActivity() {
	}

	/**
	 * This method disable or enable the button bar according to the arg. True
	 * for disable.
	 * 
	 * @param disable
	 */
	protected void changeButtonBarMode(boolean disable) {
		// implement in Sub classes if necessary.
	}

	/**
	 * Closes the View
	 */
	public void onClose() {
		if (isDirty) {
			Accounter.showWarning(messages.W_106(),
					AccounterType.WARNINGWITHCANCEL, new ErrorDialogHandler() {

						@Override
						public boolean onCancelClick() {
							return true;
						}

						@Override
						public boolean onNoClick() {
							cancel();
							return true;
						}

						@Override
						public boolean onYesClick() {
							onSave(false);
							return true;
						}
					});
		} else {
			cancel();
		}
	}

	public void onAddNew() {
		// TODO Auto-generated method stub

	}

	public boolean isMenuRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	public void showMenu(Event arg0) {
		// TODO Auto-generated method stub

	}

	public void showMenu(Widget nativeEvent) {
		// TODO Auto-generated method stub

	}

	public AccounterAsyncCallback<Object> getCallback() {
		return callback;
	}

	public void setCallback(AccounterAsyncCallback<Object> callback) {
		this.callback = callback;
	}

	public boolean isMultiCurrencyEnabled() {
		return getCompany().getPreferences().isEnableMultiCurrency();
	}

	protected ClientCurrency getBaseCurrency() {
		return getCompany().getPrimaryCurrency();
	}

	protected ClientCurrency getCurrency(long currency) {
		return getCompany().getCurrency(currency);
	}

	public boolean isSaveCliecked() {
		return isSaveCliecked;
	}

	public void setSaveCliecked(boolean isSaveCliecked) {
		this.isSaveCliecked = isSaveCliecked;
	}

	public void saveAsDrafts() {

	}

	public void saveAndEmailView() {
		// TO be overridden
		
	}

}
