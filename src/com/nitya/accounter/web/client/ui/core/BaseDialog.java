package com.nitya.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.IAccounterCRUDServiceAsync;
import com.nitya.accounter.web.client.IAccounterGETServiceAsync;
import com.nitya.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.ValidationResult.Error;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.IDeleteCallback;
import com.nitya.accounter.web.client.ui.ISaveCallback;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.WarningsDialog;
import com.nitya.accounter.web.client.ui.WidgetWithErrors;
import com.nitya.accounter.web.client.ui.forms.CustomDialog;
import com.nitya.accounter.web.client.util.DayAndMonthUtil;

/**
 * Base Dialog is abstract class which provides common ground for all small
 * Windows or Dialogs.like header, body, footer with help, ok & cancel buttons
 * 
 * @author kumar kasimala
 * 
 */
public abstract class BaseDialog<T extends IAccounterCore> extends CustomDialog
		implements IAccounterWidget, WidgetWithErrors, ISaveCallback,
		IDeleteCallback {

	protected ClientCompanyPreferences preferences = Global.get().preferences();
	protected static AccounterMessages messages = Global.get().messages();
	protected static AccounterMessages2 messages2 = Global.get().messages2();
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
	protected ClientCompany company = Accounter.getCompany();
	protected StyledPanel mainPanel;
	StyledPanel mainVLayPanel;
	public StyledPanel errorPanel;
	private final Map<Object, Widget> errorsMap = new HashMap<Object, Widget>();

	private ActionCallback<T> callback;

	/**
	 * Creates new Instance
	 */
	public BaseDialog() {
		super(true);
		this.getElement().setId("BaseDialog");
		setModal(true);
		if (isViewDialog()) {
			addStyleName("view-gwt-dialog");
			if (Accounter.isWin8App()) {
				setModal(false);
			}
		}

		getButtonBar().clear();
	}

	public BaseDialog(String text) {
		this();
		setText(text);
		initRPCService();
		createControls();
		sinkEvents(Event.ONKEYPRESS);
		sinkEvents(Event.ONMOUSEOVER);

	}

	public BaseDialog(String text, String desc) {
		this(text);
		this.description = desc;
	}

	protected void initRPCService() {
		this.rpcGetService = Accounter.createGETService();
		this.rpcDoSerivce = Accounter.createCRUDService();
		this.rpcUtilService = Accounter.createHomeService();
	}

	private void createControls() {

		/**
		 * Header Layout
		 */
		headerLayout = new StyledPanel("headerLayout");
		// headerLayout.setWidth("100%");
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
		// mainPanel.setSize("100%", "100%");
		// mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		mainPanel.add(errorPanel);
		mainPanel.add(headerLayout);
		// mainPanel.setCellVerticalAlignment(headerLayout,
		// HasVerticalAlignment.ALIGN_TOP);

		//
		// mainPanel.setCellVerticalAlignment(bodyLayout,
		// HasVerticalAlignment.ALIGN_TOP);
		mainPanel.add(bodyLayout);
		mainPanel.add(footerLayout);
		// mainPanel.setCellHorizontalAlignment(footerLayout,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		add(mainPanel);
		createButtons(footerLayout);
	}

	protected void createButtons(StyledPanel footer) {
		this.okbtn = new Button(messages.ok());
		// okbtn.setWidth("80px");
		okbtn.getElement().setAttribute("data-icon", "accept");
		this.okbtn.setFocus(true);

		okbtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				processOK();
			}
		});
		okbtn.setFocus(true);

		cancelBtn = new Button(messages.cancel());
		cancelBtn.getElement().setAttribute("data-icon", "cancel");
		// cancelBtn.setWidth("80px");
		cancelBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				processCancel();
			}
		});

		okbtn.setEnabled(true);
		cancelBtn.setEnabled(true);

		addButton(footer, okbtn);
		addButton(footer, cancelBtn);
		okbtn.setFocus(true);
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
		if (onCancel()) {
			hide();
		}
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
			if (ok && dialogHandler != null) {
				ok |= dialogHandler.onOK();
			}
			if (ok) {
				hide();
			}
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

	public static BaseDialog newInstance() {
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

	public Object getGridColumnValue(T obj, int index) {
		return null;
	}

	protected ClientCompany getCompany() {
		return company;
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

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

	protected void saveOrUpdate(final T core) {
		Accounter.createOrUpdate(this, core);
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

	public String getDecimalChar() {
		return getPreferences().getDecimalCharacter();
	}

	public String amountAsString(Double amount) {
		return DataUtils.getAmountAsStringInPrimaryCurrency(amount);
	}

	public abstract void setFocus();

	@Override
	protected void onAttach() {

		super.onAttach();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				setFocus();
			}
		});

	}

	protected ClientCurrency getBaseCurrency() {
		return getCompany().getPrimaryCurrency();
	}

	protected ClientCurrency getCurrency(long currency) {
		return getCompany().getCurrency(currency);
	}

	public List<String> getFinancialYearList() {
		ArrayList<String> list = new ArrayList<String>();

		ClientFinanceDate date = new ClientFinanceDate();
		int year = date.getYear();
		for (int i = year - 10; i < year + 1; i++) {
			list.add(Integer.toString(i) + "-" + Integer.toString(i + 1));
		}
		return list;
	}

	public List<String> getFinancialQuatersList() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("Q1" + " " + DayAndMonthUtil.apr() + " - "
				+ DayAndMonthUtil.jun());
		list.add("Q2" + " " + DayAndMonthUtil.jul() + " - "
				+ DayAndMonthUtil.sep());
		list.add("Q3" + " " + DayAndMonthUtil.oct() + " - "
				+ DayAndMonthUtil.dec());
		list.add("Q4" + " " + DayAndMonthUtil.jan() + " - "
				+ DayAndMonthUtil.mar());
		list.add(messages.custom());
		return list;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}

	@Override
	public void updateButtons() {
		getButtonBar().clear(footerLayout);
		createButtons(footerLayout);
	}

	public boolean isViewDialog() {
		return true;
	}
}
