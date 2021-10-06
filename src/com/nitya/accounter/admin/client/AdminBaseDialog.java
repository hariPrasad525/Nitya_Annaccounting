package com.nitya.accounter.admin.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.IAccounterCRUDServiceAsync;
import com.nitya.accounter.web.client.IAccounterGETServiceAsync;
import com.nitya.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.ValidationResult.Error;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.IDeleteCallback;
import com.nitya.accounter.web.client.ui.ISaveCallback;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.WarningsDialog;
import com.nitya.accounter.web.client.ui.WidgetWithErrors;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.core.IAccounterWidget;
import com.nitya.accounter.web.client.ui.core.InputDialogHandler;
import com.nitya.accounter.web.client.ui.forms.CustomDialog;

/**
 * Base Dialog is abstract class which provides common ground for all small
 * Windows or Dialogs.like header, body, footer with help, ok & cancel buttons
 * 
 * @author kumar kasimala
 * 
 */
public abstract class AdminBaseDialog<T extends IAccounterCore> extends
		CustomDialog implements IAccounterWidget, WidgetWithErrors,
		ISaveCallback, IDeleteCallback {

	// private String title;
	protected StyledPanel headerLayout;
	private String description;
	protected StyledPanel bodyLayout;
	protected StyledPanel footerLayout;

	protected Button cancelBtn;
	protected Button okbtn;
	private InputDialogHandler dialogHandler;
	protected IAccounterGETServiceAsync rpcGetService;
	protected IAccounterCRUDServiceAsync rpcDoSerivce;
	protected IAccounterHomeViewServiceAsync rpcUtilService;
	protected ClientCompany company;
	protected StyledPanel mainPanel, mainVLayPanel;
	public StyledPanel errorPanel;
	private Map<Object, Widget> errorsMap = new HashMap<Object, Widget>();

	private ActionCallback<T> callback;

	/**
	 * Creates new Instance
	 */
	public AdminBaseDialog() {
		super(true);
		this.getElement().setId("AdminBaseDialog");
	}

	public AdminBaseDialog(String text) {
		super(true);
		this.getElement().setId("AdminBaseDialog");
		setText(text);
	}

	public AdminBaseDialog(String text, String desc) {
		super(true);
		this.getElement().setId("AdminBaseDialog");
		// setText(getViewTitle());
		setText(text);
		setModal(true);
		this.description = desc;
		// initCompany();
		// initConstants();
		initRPCService();
		createControls();
		okbtn.setFocus(true);

		sinkEvents(Event.ONKEYPRESS);
		sinkEvents(Event.ONMOUSEOVER);
	}

	protected void initConstants() {

		Accounter.showError("failedToInitializeCompanyConstants()");

	}

	protected void initRPCService() {
		this.rpcGetService = Accounter.createGETService();
		this.rpcDoSerivce = Accounter.createCRUDService();
		this.rpcUtilService = Accounter.createHomeService();
	}

	protected void initCompany() {
		// this.company = Accounter.getCompany();
	}

	private void createControls() {

		/**
		 * Header Layout
		 */
		headerLayout = new StyledPanel("headerLayout");
		if (description != null) {
			Label label = new Label();
			label.setText(description);
			headerLayout.add(label);
		}

		/**
		 * Body LayOut
		 */
		bodyLayout = new StyledPanel("bodyLayout");
		// bodyLayout.setWidth("100%");

		/**
		 * Footer Layout
		 */
		footerLayout = new StyledPanel("footerLayout");
		// footerLayout.setSpacing(3);
		// footerLayout.addStyleName("dialogfooter");

		this.okbtn = new Button("ok");
		okbtn.getElement().setId("OkButton");
		this.okbtn.setFocus(true);

		okbtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				processOK();
			}
		});
		okbtn.setFocus(true);

		cancelBtn = new Button("cancel");
		cancelBtn.getElement().setId("cancelBtn");
		cancelBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				processCancel();
			}
		});

		// footerLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		footerLayout.add(okbtn);
		footerLayout.add(cancelBtn);

		okbtn.setEnabled(true);

		cancelBtn.setEnabled(true);

		// footerLayout.setCellHorizontalAlignment(okbtn,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// footerLayout.setCellHorizontalAlignment(cancelBtn,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		/**
		 * adding all Layouts to Window
		 */

		mainPanel = new StyledPanel("mainPanel");
		errorPanel = new StyledPanel("errorPanel");
		errorPanel.setVisible(false);
		errorPanel.addStyleName("errors error-panel");
		// mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		mainPanel.add(errorPanel);
		mainPanel.add(headerLayout);

		//
		// mainPanel.setCellVerticalAlignment(bodyLayout,
		// HasVerticalAlignment.ALIGN_TOP);
		mainPanel.add(bodyLayout);
		mainPanel.add(footerLayout);

		add(mainPanel);

	}

	/**
	 * add body to this Dialog
	 * 
	 * @param layout
	 */
	public void setBodyLayout(Panel layout) {
		this.bodyLayout.add(layout);
	}

	/**
	 * called when cancelButton clicks
	 */
	protected void processCancel() {

		if (dialogHandler != null) {
			dialogHandler.onCancel();

		}
		if (onCancel())
			hide();
	}

	protected void updateCompany() {

	}

	/**
	 * Called when Ok button clicked
	 */
	protected void processOK() {
		clearAllErrors();
		okbtn.setFocus(true);
		ValidationResult validationResult = validate();
		if (validationResult.haveErrors()) {
			for (Error error : validationResult.getErrors()) {
				HTML err = new HTML("<li>" + error.getMessage() + "</li>");
				errorPanel.add(err);
				errorPanel.setVisible(true);
				errorsMap.put(error.getSource(), err);
			}
		} else if (validationResult.haveWarnings()) {

			new WarningsDialog(validationResult.getWarningsAsList(),
					new ErrorDialogHandler() {

						@Override
						public boolean onYesClick() {
							onOK();
							return true;
						}

						@Override
						public boolean onNoClick() {
							return true;
						}

						@Override
						public boolean onCancelClick() {
							return true;
						}
					});
		} else {
			boolean ok = onOK();
			if (dialogHandler != null) {
				ok |= dialogHandler.onOK();
			}
			if (ok)
				this.removeFromParent();
		}

	}

	/**
	 * add InputDialog handler, methods in handler will call when particular
	 * event happen on this Dialog.default implementation does nothing
	 * 
	 * @param handler
	 */
	public void addInputDialogHandler(InputDialogHandler handler) {
		this.dialogHandler = handler;
	}

	public static AdminBaseDialog newInstance() {
		return null;
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONKEYPRESS:
			int keycode = event.getKeyCode();
			if (KeyCodes.KEY_ESCAPE == keycode) {
				processCancel();
			}
			break;
		case Event.ONMOUSEOVER:
			// cancelBtn.setFocus(true);

		case Event.ONKEYDOWN:
			// cancelBtn.setFocus(true);
			break;
		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	/**
	 * Adds Error
	 * 
	 * @param item
	 * @param erroMsg
	 */
	public void addError(Object item, String erroMsg) {
		HTML error = new HTML("<li>" + erroMsg + "</li>");
		this.errorPanel.add(error);
		this.errorPanel.setVisible(true);
		this.errorsMap.put(item, error);
	}

	/**
	 * Clears All Errors
	 */
	public void clearAllErrors() {
		this.errorsMap.clear();
		this.errorPanel.clear();
		this.errorPanel.setVisible(false);
	}

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

	public Object getGridColumnValue(IsSerializable obj, int index) {
		return null;
	}

	public void deleteFailed(AccounterException caught) {

	}

	public void deleteSuccess(IAccounterCore result) {

	}

	public void saveFailed(AccounterException exception) {

	}

	public void saveSuccess(IAccounterCore object) {
	}

	// }

	protected ValidationResult validate() {
		return new ValidationResult();
	}

	protected void saveOrUpdate(final T core) {
		AdminHomePage.createORUpdate(this, core);
	}

	protected abstract boolean onOK();

	public ActionCallback<T> getCallback() {
		return callback;
	}

	public void setCallback(ActionCallback<T> callback) {
		this.callback = callback;
	}

	/**
	 * Used to tell the call backs about the result of showing this dialog
	 * 
	 * @param result
	 */
	public void setResult(T result) {
		if (this.callback != null) {
			this.callback.actionResult(result);
		}
	}

	public String amountAsString(Double amount) {
		return DataUtils.getAmountAsStringInPrimaryCurrency(amount);
	}
}
