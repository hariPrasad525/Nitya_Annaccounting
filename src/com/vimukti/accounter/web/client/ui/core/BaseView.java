package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientAttachment;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.TransactionAttachmentPanel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public abstract class BaseView<T extends IAccounterCore> extends
		AbstractBaseView<T> implements IEditableView, ISavableView<T> {

	protected EditMode mode;
	// private boolean isInViewMode;

	protected SaveAndCloseButton saveAndCloseButton;

	protected SaveAndNewButtom saveAndNewButton;

	protected SaveAndEmailButton saveAndEmailButton;

	protected CancelButton cancelButton;

	protected ApproveButton approveButton;

	protected DeleteButton deleteButton;

	protected VoidButton voidButton;

	protected String quickAddText;

	protected TransactionAttachmentPanel transactionAttachmentPanel;

	protected boolean emailButtonAllowed;

	public BaseView() {
		super();
	}

	@Override
	protected abstract String getViewTitle();

	@Override
	public void init() {
		super.init();
		createView();
	}

	@Override
	public void initData() {
		super.initData();
		// showSaveButtons();
		updateButtons();
	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	private void createView() {

		// setWidth("100%");
		// setHeight("100%");

		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.saveAndNewButton = new SaveAndNewButtom(this);
		this.saveAndEmailButton = new SaveAndEmailButton(this);
		this.cancelButton = new CancelButton(this);
		this.deleteButton = new DeleteButton(this, getData());
		this.voidButton = new VoidButton(this, getData());

		if (data != null && mode != EditMode.CREATE) {
			if (Accounter.hasPermission(Features.HISTORY)) {
				super.add(createHistoryView());
			}
		}
		if (canAddAttachmentPanel()
				&& Accounter.hasPermission(Features.ATTACHMENTS)) {
			transactionAttachmentPanel = new TransactionAttachmentPanel() {

				@Override
				public boolean isInViewMode() {
					return BaseView.this.isInViewMode();
				}

				@Override
				protected void saveAttachment(ClientAttachment attachment) {
					BaseView.this.saveAttachment(attachment);
				}
			};
			super.add(transactionAttachmentPanel);
		}

	}

	public void addAttachments(ArrayList<ClientAttachment> attachments) {
		if (transactionAttachmentPanel != null) {
			transactionAttachmentPanel.addAttachments(attachments);
		}
	}

	public List<ClientAttachment> getAttachments() {
		if (transactionAttachmentPanel != null) {
			return transactionAttachmentPanel.getAttachments();
		}
		return new ArrayList<ClientAttachment>();
	}

	protected void saveAttachment(ClientAttachment attachment) {
		// TODO Auto-generated method stub
		System.out.println();
	}

	protected boolean canAddAttachmentPanel() {
		return false;
	}

	protected StyledPanel createHistoryView() {
		return new StyledPanel("history");
	}

	/**
	 * Return list of all DynamicForm items in this view
	 * 
	 * @return
	 */
	public abstract List<DynamicForm> getForms();

	@Override
	public void fitToSize(int height, int width) {
		// canvas.setHeight(height - 125 + "px");
		// canvas.setWidth(width - 15 + "px");
	}

	protected void enableAttachmentPanel(boolean b) {
		if (transactionAttachmentPanel != null) {
			transactionAttachmentPanel.setEnable(b);
		}
	}

	/**
	 * This method will be called my all sub classes to add items to this view.
	 */
	@Override
	public void add(Widget child) {
		Widget widget = getButtonBar().asWidget();
		if (widget == null || this.getWidgetIndex(widget) < 0) {
			super.add(child);
			return;
		}
		int index = this.getWidgetIndex(widget);
		// Insert widgets above button bar
		super.insert(child, index);
	}

	@Override
	public void setData(T data) {
		super.setData(data);
		if (data == null || data.getID() == 0) {
			this.setMode(EditMode.CREATE);
		} else {
			this.setMode(EditMode.VIEW);
		}
	}

	public void updateButtons() {
		clearButtons();
		createButtons();
		super.updateButtons();
	}

	protected void clearButtons() {
		getButtonBar().clearDirectButtons();
	}

	protected void createButtons() {

		if (!isInViewMode()) {
			if (isSaveButtonAllowed()) {
				saveAndCloseButton = new SaveAndCloseButton(this);
				addButton(saveAndCloseButton);
			}
			if (isSaveButtonAllowed()) {
				saveAndNewButton = new SaveAndNewButtom(this);
				addButton(saveAndNewButton);
			}
			if (isEmailButtonAllowed()) {
				saveAndEmailButton = new SaveAndEmailButton(this);
				addButton(saveAndEmailButton);
			}
		}

		if (getMode() != null && getMode() != EditMode.CREATE) {

			if (isSaveButtonAllowed() && canVoid()) {
				voidButton = new VoidButton(this, getData());
				addButton(voidButton);
				// this.buttonBar.setCellHorizontalAlignment(voidButton,
				// ALIGN_LEFT);
			}

			if (canDelete()) {
				deleteButton = new DeleteButton(this, getData());
				addButton(deleteButton);
				// this.buttonBar.setCellHorizontalAlignment(deleteButton,
				// ALIGN_LEFT);
			} else if (!canDelete() && deleteButton != null) {
				remove(deleteButton);
			}

		}
		cancelButton = new CancelButton(this);
		addButton(cancelButton);
	}

	protected boolean isSaveButtonAllowed() {
		if (data == null) {
			return false;
		}
		return Utility.isUserHavePermissions(data.getObjectType());
	}
	protected boolean isEmailButtonAllowed() {
			return this.emailButtonAllowed ;
	}

	protected boolean canVoid() {
		if (getMode() == null || getMode() == EditMode.CREATE) {
			return false;
		}
		ClientTransaction data = null;
		if (getData() instanceof ClientTransaction) {
			data = ((ClientTransaction) getData());
			if (data == null || data.isVoid() || data.isTemplate()
					|| data.getSaveStatus() == ClientTransaction.STATUS_DRAFT) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	protected boolean canDelete() {
		if (getMode() == null || getMode() == EditMode.CREATE) {
			return false;
		}
		return isSaveButtonAllowed();
	}

	@Override
	public boolean canEdit() {
		return getMode() == EditMode.VIEW;
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	public EditMode getMode() {
		return mode;
	}

	public void setMode(EditMode mode) {
		if (this.mode == mode) {
			return;
		}
		this.mode = mode;
		if (getManager() != null) {
			getManager().updateButtons();
		}
		if (!isInViewMode()) {
			updateButtons();
		}
	}

	public boolean isInViewMode() {
		return this.mode == EditMode.VIEW;
	}

	public void prepareForQuickAdd(String text) {
		quickAddText = text;
	}

	@Override
	protected void changeButtonBarMode(boolean disable) {
		getButtonBar().setDisabled(disable);
	}

	@Override
	protected ClientCurrency getBaseCurrency() {
		return getCompany().getPrimaryCurrency();
	}

	@Override
	protected ClientCurrency getCurrency(long currency) {
		return getCompany().getCurrency(currency);
	}

	@Override
	public T saveView() {
		T saveView = getData();
		if (isSaveCliecked()) {
			saveView = null;
		}
		setSaveCliecked(false);
		if (saveView == null || saveView.getID() != 0) {
			return null;
		}
		return saveView;
	}

	@Override
	public void restoreView(T viewDate) {
		setData(viewDate);
	}

	public List<String> getYESNOList() {
		List<String> names = new ArrayList<String>();
		names.add(messages.YES());
		names.add(messages.NO());
		return names;

	}

	public List<String> getStatesList() {

		List<String> statesName = new ArrayList<String>();
		statesName = Utility.getStatesList();
		return statesName;
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

	public List<String> getAssessmentYearList() {
		ArrayList<String> list = new ArrayList<String>();

		ClientFinanceDate date = new ClientFinanceDate();
		int year = date.getYear();
		for (int i = year - 10; i < year + 2; i++) {
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

	public void addButton(Button widget) {
		getButtonBar().add(widget);
	}

	public void addButton(Button widget, HorizontalAlignmentConstant alignment) {
		getButtonBar().add(widget, alignment);
	}

	public void setEmailButtonAllowed(boolean emailButtonAllowed) {
		this.emailButtonAllowed = emailButtonAllowed;
	}
}
