package com.nitya.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientBudget;
import com.nitya.accounter.web.client.core.ClientETDSFillingItem;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTDSDeductorMasters;
import com.nitya.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.reports.ETDsFilingData;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.SaveAndCloseButton;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.ETdsCellTable;

public class ETdsFillingView extends BaseView<ClientETDSFillingItem> {

	private SelectCombo formType;
	private Label lab1;

	StyledPanel mainVLay;

	ClientBudget budgetForEditing = new ClientBudget();

	private ETdsCellTable tdsCellTable;
	private SelectCombo slectAssecementYear;
	private SelectCombo financialYearCombo;
	private SelectCombo quaterSelectionCombo;
	private DateField fromDateField;
	private DateField toDateField;
	private DynamicForm dynamicFormLeft;
	private DynamicForm dynamicFormRight;
	private int formNoSelected;
	private int quaterSelected;
	private int startYear;
	private int endYear;
	protected ArrayList<ClientETDSFillingItem> eTDSList;
	private ClientTDSDeductorMasters deductor;
	private ClientTDSResponsiblePerson responsiblePerson;
	private Button acknowledgementFormButton;

	public ETdsFillingView() {

	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("ETdsFillingView");
		createControls();
	}

	public void initData() {
		if (data == null) {
			setData(new ClientETDSFillingItem());
			initCallBack();
		}
		getDeductorAndResponsiblePerson();
	}

	private void getDeductorAndResponsiblePerson() {
		Accounter.createHomeService().getDeductorMasterDetails(
				new AccounterAsyncCallback<ClientTDSDeductorMasters>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(ClientTDSDeductorMasters result) {
						deductor = result;
					}
				});

		Accounter.createHomeService().getResponsiblePersonDetails(
				new AccounterAsyncCallback<ClientTDSResponsiblePerson>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ClientTDSResponsiblePerson result) {
						responsiblePerson = result;
					}
				});
	}

	private void createControls() {

		lab1 = new Label();
		lab1.removeStyleName("gwt-Label");
		lab1.setText(messages.eTDSFilling());
		lab1.addStyleName("label-title");

		formType = new SelectCombo(messages.formNo());
		formType.initCombo(getFormTypes());
		formType.setSelectedItem(0);
		formNoSelected = 1;
		formType.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

			@Override
			public void selectedComboBoxItem(String selectItem) {
				if (selectItem.equals(getFormTypes().get(0))) {
					if (formNoSelected == 2) {
						tdsCellTable.reDraw();
					}
					formNoSelected = 1;
				} else if (selectItem.equals(getFormTypes().get(1))) {
					if (formNoSelected != 2) {
						tdsCellTable.reDraw();
					}
					formNoSelected = 2;
				} else if (selectItem.equals(getFormTypes().get(2))) {
					if (formNoSelected == 2) {
						tdsCellTable.reDraw();
					}
					formNoSelected = 3;
				}
				initCallBack();
			}
		});

		slectAssecementYear = new SelectCombo(messages.assessmentYear());
		slectAssecementYear.initCombo(getAssessmentYearList());
		slectAssecementYear.setEnabled(false);

		financialYearCombo = new SelectCombo(messages.financialYear());
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setSelectedItem(0);
		String[] tokens = financialYearCombo.getSelectedValue().split("-");
		startYear = Integer.parseInt(tokens[0]);
		endYear = Integer.parseInt(tokens[1]);
		financialYearCombo.setEnabled(!isInViewMode());
		financialYearCombo.setRequired(true);
		financialYearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						slectAssecementYear.setSelected(getAssessmentYearList()
								.get(financialYearCombo.getSelectedIndex() + 1));

						String delims = "-";
						String[] tokens = selectItem.split(delims);

						startYear = Integer.parseInt(tokens[0]);
						endYear = Integer.parseInt(tokens[1]);
						initCallBack();
					}

				});

		quaterSelectionCombo = new SelectCombo(messages.forQuarter());
		quaterSelectionCombo.initCombo(getFinancialQuatersList());
		quaterSelectionCombo.setSelectedItem(0);
		quaterSelected = 1;
		quaterSelectionCombo.setEnabled(!isInViewMode());
		quaterSelectionCombo.setRequired(true);
		quaterSelectionCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(getFinancialQuatersList().get(0))) {
							quaterSelected = 1;
						} else if (selectItem.equals(getFinancialQuatersList()
								.get(1))) {
							quaterSelected = 2;
						} else if (selectItem.equals(getFinancialQuatersList()
								.get(2))) {
							quaterSelected = 3;
						} else if (selectItem.equals(getFinancialQuatersList()
								.get(3))) {
							quaterSelected = 4;
						}

						quarterChanged(selectItem);
						initCallBack();
					}
				});

		ClientFinanceDate[] dates = Utility.getFinancialQuarter(1);

		fromDateField = new DateField(messages.fromDate(), "dateItem2");
		fromDateField.setEnteredDate(dates[0]);
		fromDateField.setEnabled(false);

		toDateField = new DateField(messages.toDate(), "dateItem2");
		toDateField.setEnteredDate(dates[1]);
		toDateField.setEnabled(false);

		dynamicFormLeft = UIUtils.form(messages.chartOfAccountsInformation());
		// dynamicFormLeft.setWidth("100%");
		dynamicFormLeft.add(formType, financialYearCombo);

		dynamicFormRight = UIUtils.form(messages.chartOfAccountsInformation());
		// dynamicFormRight.setWidth("100%");
		dynamicFormRight.add(quaterSelectionCombo, fromDateField, toDateField,
				slectAssecementYear);

		StyledPanel topHLay = new StyledPanel("topHLay");
		// topHLay.setWidth("100%");
		topHLay.add(dynamicFormLeft);
		topHLay.add(dynamicFormRight);

		tdsCellTable = new ETdsCellTable() {

			@Override
			protected boolean is27Q() {
				if (formType.getSelectedValue() != null
						&& formType.getSelectedValue().equals("27Q")) {
					return true;
				}
				return false;
			}
		};

		mainVLay = new StyledPanel("mainVLay");
		// mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);
		topHLay.getElement().getStyle().setPaddingTop(10, Unit.PX);
		Label etdsTableTitle = new Label(
				messages2.table(messages.eTDSFilling()));
		etdsTableTitle.addStyleName("editTableTitle");
		StyledPanel tablePanel = new StyledPanel("etdsTable");
		tablePanel.add(etdsTableTitle);
		ScrollPanel scroll = new ScrollPanel();
		scroll.add(tdsCellTable);
		scroll.getElement().removeAttribute("style");
		// scroll.setWidth("910px");
		scroll.setTouchScrollingDisabled(false);
		scroll.addStyleName("tds-scroll-panel");
		tablePanel.add(scroll);
		mainVLay.add(tablePanel);

		this.add(mainVLay);

	}

	protected void quarterChanged(String selectdQuarter) {
		if (selectdQuarter.equalsIgnoreCase(messages.custom())) {
			fromDateField.setEnabled(true);
			toDateField.setEnabled(true);
		} else {
			int quarter = getFinancialQuatersList().indexOf(selectdQuarter);
			ClientFinanceDate[] dates = Utility
					.getFinancialQuarter(quarter + 1);
			fromDateField.setEnteredDate(dates[0]);
			toDateField.setEnteredDate(dates[1]);
			fromDateField.setEnabled(false);
			toDateField.setEnabled(false);
		}
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
	protected String getViewTitle() {
		return messages.eTDSFilling();

	}

	@Override
	public void saveAndUpdateView() {
		List<ClientETDSFillingItem> dataList = tdsCellTable.getAllRows();

		String panList = "";
		String remarkList = "";
		String codeList = "";
		String grossingUpList = "";
		for (ClientETDSFillingItem record : dataList) {
			if (record.getRemark() != null) {
				remarkList = remarkList + record.getRemark().trim();
			}
			remarkList = remarkList + "-";
			codeList = codeList + record.getCompanyCode() + "-";
			if (record.getPanOfDeductee() != null) {
				panList = panList + record.getPanOfDeductee().trim();
			}
			panList = panList + "-";
			if (record.getGrossingUpIndicator() != null) {
				grossingUpList = grossingUpList
						+ record.getGrossingUpIndicator().substring(0, 1);
			}
			grossingUpList = grossingUpList + "-";
		}
		long fromDate = fromDateField.getEnteredDate().getDate();
		long toDate = toDateField.getEnteredDate().getDate();
		ETDsFilingData etDsFilingData = new ETDsFilingData(formNoSelected,
				quaterSelected, fromDate, toDate, startYear, endYear, panList,
				codeList, remarkList, grossingUpList);
		UIUtils.generateETDSFillingtext(etDsFilingData);
		changeButtonBarMode(false);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (getCompany().getTdsDeductor() == null) {
			result.addError("deductor", messages
					.tdsDeductorDetailsNotEnteredYetPleaseFillTheDetailsFirst());
		}

		if (getCompany().getTdsResposiblePerson() == null) {
			result.addError(
					"responsible",
					messages.tDSResponsiblePersonDetailsNotEnteredYetPleaseFillTheDetailsFirst());
		}

		List<ClientETDSFillingItem> records = tdsCellTable.getAllRows();

		for (ClientETDSFillingItem row : records) {
			if (row.getCompanyCode() == null || row.getCompanyCode().isEmpty()) {
				result.addError(tdsCellTable,
						messages.pleaseSelectDeducteeCodeForAllRecords());
			}
		}

		return result;

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void setFocus() {

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("26Q");
		list.add("27Q");
		// list.add("27EQ");

		return list;
	}

	private void initCallBack() {
		Accounter.createHomeService().getEtdsDetails(formNoSelected,
				quaterSelected, fromDateField.getEnteredDate(),
				toDateField.getEnteredDate(), startYear, endYear,
				new AccounterAsyncCallback<ArrayList<ClientETDSFillingItem>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientETDSFillingItem> result) {
						eTDSList = result;
						tdsCellTable.setAllRows(eTDSList);
					}
				});
	}

	@Override
	protected void createButtons() {
		// super.createButtons(buttonBar);
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.saveAndCloseButton.setText(messages.downloadTxtFile());

		acknowledgementFormButton = new Button(messages.enterAckNumber());
		acknowledgementFormButton.setFocus(true);

		acknowledgementFormButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				openAcknowledgementForm();
			}
		});

		// addButton(acknowledgementFormButton);
		addButton(saveAndCloseButton);
	}

	protected void openAcknowledgementForm() {

		TDSAcknowlegmentForm ackFormDialogue = new TDSAcknowlegmentForm();
		ViewManager.getInstance().showDialog(ackFormDialogue);

	}

	@Override
	protected boolean isSaveButtonAllowed() {
		if (data == null) {
			return false;
		}
		return Utility.isUserHavePermissions(AccounterCoreType.TDSCHALANDETAIL);
	}

	@Override
	protected boolean canDelete() {
		return false;
	}
}
