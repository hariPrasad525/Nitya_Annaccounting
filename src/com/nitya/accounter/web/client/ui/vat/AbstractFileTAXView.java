package com.nitya.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.nitya.accounter.web.client.ui.core.AccounterDOM;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.ButtonBar;
import com.nitya.accounter.web.client.ui.core.CancelButton;
import com.nitya.accounter.web.client.ui.core.SaveAndCloseButton;
import com.nitya.accounter.web.client.ui.forms.DateItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.widgets.DateUtil;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

public abstract class AbstractFileTAXView extends BaseView<ClientTAXReturn> {

	protected TAXAgencyCombo taxAgencyCombo;
	protected DateItem fromDate;
	protected DateItem toDate;
	private StyledPanel topLayout;
	private StyledPanel mainLayout;

	private Button adjustButton;
	private Button printButton;
	protected ClientTAXAgency selectedTaxAgency;
	private ArrayList<DynamicForm> listforms;
	private double amt;
	private Button updateButton;

	boolean canSaveFileVat = false;

	public AbstractFileTAXView() {
		super();
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("AbstractFileTAXView");
		createControls();
	}

	private void createControls() {
		taxAgencyCombo = new TAXAgencyCombo(messages.taxAgency());
		taxAgencyCombo.setRequired(true);
		taxAgencyCombo.setEnabled(!isInViewMode());
		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					public void selectedComboBoxItem(ClientTAXAgency selectItem) {

						taxAgencySelected(selectItem);

					}
				});
		fromDate = new DateItem(messages.from(), "fromDate");
		toDate = new DateItem(messages.to(), "toDate");

		toDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				canSaveFileVat = false;
			}
		});

		fromDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate financeDate) {
				canSaveFileVat = false;
				Date date = financeDate.getDateAsObject();
				int frequency = ClientTAXAgency.TAX_RETURN_FREQUENCY_MONTHLY;
				if (selectedTaxAgency != null) {
					frequency = selectedTaxAgency.getTAXFilingFrequency();
				}
				date = DateUtil.getMonthFirstDay(date);
				switch (frequency) {
				case ClientTAXAgency.TAX_RETURN_FREQUENCY_MONTHLY:
					date.setMonth(date.getMonth() + 1);
					break;
				case ClientTAXAgency.TAX_RETURN_FREQUENCY_QUARTERLY:
					date.setMonth(date.getMonth() + 3);
					break;
				case ClientTAXAgency.TAX_RETURN_FREQUENCY_HALF_YEARLY:
					date.setMonth(date.getMonth() + 6);
					break;
				case ClientTAXAgency.TAX_RETURN_FREQUENCY_YEARLY:
					date.setMonth(date.getMonth() + 12);
					break;
				}
				date.setDate(date.getDate() - 1);
				toDate.setValue(new ClientFinanceDate(date));
			}
		});

		Date startDate = DateUtil.getCurrentMonthFirstDate();
		fromDate.setDatethanFireEvent(new ClientFinanceDate(startDate));
		toDate.setValue(new Date());
		listforms = new ArrayList<DynamicForm>();

		updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				reloadGrid();
			}
		});

		DynamicForm topForm = new DynamicForm("topForm");
		// topForm.setIsGroup(true);
		// topForm.setGroupTitle(messages.top());
		// topForm.setNumCols(6);
		topForm.add(taxAgencyCombo, fromDate, toDate);
		// topForm.setWidth("100%");

		ListGrid grid = getGrid();

		HTML beforeLabel = new HTML("<string>" + messages.beforeYouFile());

		HTML adjustLabel = new HTML("<strong>"
				+ messages.doYouNeedToMakeAnAdjustment() + " </strong><br>"
				+ messages.useAdjustButton());

		adjustButton = new Button(messages.adjustTAXReturn());
		adjustButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedTaxAgency != null) {
					AdjustTAXAction vatAdjustmentAction = new AdjustTAXAction(1);
					vatAdjustmentAction.setVatAgency(selectedTaxAgency);
					vatAdjustmentAction.run(null, true);
				}

			}
		});

		StyledPanel adjustForm = new StyledPanel("adjustForm");
		// adjustForm.add(adjustLabel);
		adjustForm.add(adjustButton);
		// if (adjustButton.isEnabled()) {
		// adjustButton.getElement().getParentElement()
		// .setClassName("ibutton");
		// ThemesUtil.addDivToButton(adjustButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// }
		HTML printLabel1 = new HTML(messages.printTAXReturnLabel1());
		HTML printLabel2 = new HTML(messages.printTAXReturnLabel2());

		printLabel1.addStyleName("bold_HTML");

		printButton = new Button(messages.printTAXReturn());
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedTaxAgency != null
						&& fromDate.getEnteredDate() != null
						&& toDate.getEnteredDate() != null)
					printTaxReturn();
				else
					Accounter.showError(messages.pleaseselectvaliddateranges());

			}
		});

		disableButtons();

		StyledPanel printForm = new StyledPanel("printForm");
		printForm.add(printLabel1);
		printForm.add(printLabel2);
		printForm.add(printButton);
		// if (printButton.isEnabled()) {
		// printButton.getElement().getParentElement().setClassName("ibutton");
		// ThemesUtil.addDivToButton(printButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// }
		topLayout = new StyledPanel("topLayout");
		topLayout.add(topForm);
		topLayout.add(updateButton);

		// if (updateButton.isEnabled()) {
		// updateButton.getElement().getParentElement()
		// .setClassName("ibutton");
		// ThemesUtil.addDivToButton(updateButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// }
		mainLayout = new StyledPanel("mainLayout");
		mainLayout.add(topLayout);
		mainLayout.add(grid);
		// mainLayout.add(beforeLabel);
		mainLayout.add(adjustForm);
		mainLayout.add(printForm);

		AccounterDOM.setParentElementHeight(topLayout.getElement(), 10);
		AccounterDOM.setParentElementHeight(grid.getElement(), 60);

		this.add(mainLayout);

		/* Adding dynamic forms in list */
		listforms.add(topForm);

	}

	protected void taxAgencySelected(final ClientTAXAgency selectItem) {

		this.selectedTaxAgency = selectItem;
		canSaveFileVat = true;

		if (selectItem == null) {
			disableButtons();
			getGrid().clear();
		} else {

			enableButtons();
			fromDate.setEnabled(false);
			getLastTaxReturnEndDate(selectItem);
			// getVATReturnEndDate(selectItem);

		}

		fromDate.setEnabled(false);
	}

	private void getVATReturnEndDate(ClientTAXAgency selectItem) {
		List<ClientTAXReturn> vatReturns = getCompany().getTAXReturns();
		ClientTAXReturn lastVATReturn = null;
		for (ClientTAXReturn vatReturn : vatReturns) {
			if (selectItem.getID() == vatReturn.getTAXAgency()) {
				lastVATReturn = vatReturn;
			}
		}
		if (lastVATReturn != null) {
			if (lastVATReturn.getPeriodEndDate() != 0) {
				ClientFinanceDate date = new ClientFinanceDate(
						lastVATReturn.getPeriodEndDate());
				int day = date.getDay();
				date.setDay(day + 1);
				fromDate.setDatethanFireEvent(date);
				// date.setDate(day + 1);
				toDate.setDatethanFireEvent(date);
			}
		} else {
			// List<ClientFiscalYear> fiscalYears = Accounter.getCompany()
			// .getFiscalYears();
			// for (ClientFiscalYear fiscalYear : fiscalYears) {
			// if (fiscalYear.getIsCurrentFiscalYear()) {
			fromDate.setDatethanFireEvent(Accounter.getCompany()
					.getCurrentFiscalYearStartDate());
			// break;
			// }
			// }
		}

	}

	public void disableButtons() {
		this.adjustButton.setEnabled(false);
		this.printButton.setEnabled(false);

	}

	public void enableButtons() {
		this.adjustButton.setEnabled(true);
		this.printButton.setEnabled(true);
	}

	public void enableprintButton() {

		this.printButton.setEnabled(true);

	}

	public void disableprintButton() {

		this.printButton.setEnabled(false);

	}

	private void getLastTaxReturnEndDate(final ClientTAXAgency taxAgency) {
		rpcGetService.getLastTAXReturnEndDate(taxAgency.getID(),
				new AccounterAsyncCallback<Long>() {

					@Override
					public void onException(AccounterException exception) {
						String errorString = AccounterExceptions
								.getErrorString(exception.getErrorCode());
						Accounter.showError(errorString);
					}

					@Override
					public void onResultSuccess(Long result) {
						if (result != null && result > 0) {
							Date date = new ClientFinanceDate(result)
									.getDateAsObject();
							date.setDate(date.getDate() + 1);

							fromDate.setDatethanFireEvent(new ClientFinanceDate(
									date));
							fromDate.setEnabled(false);
						} else {
							fromDate.setDatethanFireEvent(new ClientFinanceDate(
									DateUtil.getCurrentMonthFirstDate()));
							fromDate.setEnabled(true);
						}
						reloadGrid();
					}
				});
	}

	@Override
	protected void createButtons() {
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.cancelButton = new CancelButton(this);
		if (!isInViewMode()) {
			addButton(saveAndCloseButton);
		}
		addButton(cancelButton);
		if (this instanceof FileVATView) {
			this.saveAndCloseButton.setText(messages.fileVATReturn());
		} else {
			this.saveAndCloseButton.setText(messages.fileTAXReturn());
		}
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		// check whether vet agency is selected or not
		// can save file vat or not

		if (this.selectedTaxAgency == null && this.isInViewMode()) {
			taxAgencyCombo.addStyleName("highlightedFormItem");
			result.addError(selectedTaxAgency,
					messages.pleaseSelectValidVATAgency());
		} else {
			result.addWarning(selectedTaxAgency, messages.sureToSaveFileVAT());
			// AccounterValidator.validate_FileVat(this);
		}
		if (!canSaveFileVat) {
			// taxAgencyCombo.addStyleName("highlightedFormItem");
			// fromDate.addStyleName("highlightedFormItem");
			// toDate.addStyleName("highlightedFormItem");
			result.addError(this, messages.updateGridBeforeSaving());
		}
		if (getGrid().getRecords().isEmpty()) {
			result.addError(this, messages.thereIsNoTrasationsToFile());
		}
		return result;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String errorString = AccounterExceptions.getErrorString(exception);
		Accounter.showError(errorString);
	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return listforms;
	}

	@Override
	public void setFocus() {
		this.taxAgencyCombo.setFocus();
	}

	protected abstract void reloadGrid();

	protected abstract void printTaxReturn();

	protected abstract ListGrid getGrid();

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
