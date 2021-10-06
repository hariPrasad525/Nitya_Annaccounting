package com.nitya.accounter.web.client.ui.company;
//package com.nitya.accounter.web.client.ui.company;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gwt.event.dom.client.BlurEvent;
//import com.google.gwt.event.dom.client.BlurHandler;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.nitya.accounter.web.client.AccounterAsyncCallback;
//import com.nitya.accounter.web.client.Global;
//import com.nitya.accounter.web.client.core.AccounterCoreType;
//import com.nitya.accounter.web.client.core.ClientAccount;
//import com.nitya.accounter.web.client.core.ClientBudget;
//import com.nitya.accounter.web.client.core.ClientBudgetItem;
//import com.nitya.accounter.web.client.core.IAccounterCore;
//import com.nitya.accounter.web.client.core.ValidationResult;
//import com.nitya.accounter.web.client.exception.AccounterException;
//import com.nitya.accounter.web.client.exception.AccounterExceptions;
//import com.nitya.accounter.web.client.ui.Accounter;
//import com.nitya.accounter.web.client.ui.UIUtils;
//import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
//import com.nitya.accounter.web.client.ui.combo.SelectCombo;
//import com.nitya.accounter.web.client.ui.core.ActionCallback;
//import com.nitya.accounter.web.client.ui.core.BaseView;
//import com.nitya.accounter.web.client.ui.core.EditMode;
//import com.nitya.accounter.web.client.ui.forms.DynamicForm;
//import com.nitya.accounter.web.client.ui.forms.TextItem;
//import com.nitya.accounter.web.client.ui.grids.BudgetAccountGrid;
//import com.nitya.accounter.web.client.ui.grids.ListGrid;
//
//public class NewBudgetView extends BaseView<ClientBudget> {
//
//	public static final String AUCTUAL_AMOUNT_LAST_FISCAL_YEAR = messages
//			.ActualAmountfromlastfiscalyear();
//	public static final String AUCTUAL_AMOUNT_THIS_FISCAL_YEAR = messages
//			.ActualAmountfromthisfiscalyear();
//	public static final String NO_AMOUNT = messages.StartfromScratch();
//	public static final String COPY_FROM_EXISTING = messages
//			.CopyfromExistingBudget();
//
//	public static final String SUBDIVIDE_DONT = messages.DontSubdevide();
//	public static final String SUBDIVIDE_BUSINESS = messages.businessPhone();
//	public static final String SUBDIVIDE_CLASS = messages.accounterClass();
//	public static final String SUBDIVIDE_CUSTOMER = messages.customer();
//
//	public static final String FISCAL_YEAR_1 = "FY2010 (Jan2010 - Dec2010)";
//	public static final String FISCAL_YEAR_2 = "FY2011 (Jan2011 - Dec2011)";
//	public static final String FISCAL_YEAR_3 = "FY2012 (Jan2012 - Dec2012)";
//	public static final String FISCAL_YEAR_4 = "FY2013 (Jan2013 - Dec2013)";
//	public static final String FISCAL_YEAR_5 = "FY2014 (Jan2014 - Dec2014)";
//	public static final String FISCAL_YEAR_6 = "FY2015 (Jan2015 - Dec2015)";
//	public static final String FISCAL_YEAR_7 = "FY2016 (Jan2016 - Dec2016)";
//	public static final String FISCAL_YEAR_8 = "FY2017 (Jan2017 - Dec2017)";
//	public static final String FISCAL_YEAR_9 = "FY2018 (Jan2018 - Dec2018)";
//
//	private SelectCombo budgetStartWithSelect, budgetSubdevideBy;
//	private SelectCombo selectFinancialYear;
//	private TextItem budgetNameText;
//	private DynamicForm budgetInfoForm;
//	private StyledPanel topHLay;
//	private StyledPanel leftLayout;
//	private Label lab1;
//	private List<ClientAccount> listOfAccounts;
//	// boolean isEditing;
//
//	StyledPanel mainVLay;
//	BudgetAccountGrid gridView;
//
//	private ArrayList<DynamicForm> listforms;
//
//	List<ClientBudget> budgetList;
//
//	ClientBudget budgetForEditing = new ClientBudget();
//
//	public NewBudgetView(List<ClientBudget> listData) {
//		budgetList = listData;
//	}
//
//	// public NewBudgetView(boolean isEdit, Object data1) {
//	// isEditing = isEdit;
//	// data = (ClientBudget) data1;
//	// }
//
//	public NewBudgetView() {
//
//	}
//
//	@Override
//	public void init() {
//		super.init();
//		createControls();
//		setSize("100%", "100%");
//
//	}
//
//	public void initData() {
//		super.initData();
//		if (data == null) {
//			ClientBudget account = new ClientBudget();
//			setData(account);
//		}
//		// if (data != null) {
//		// onEdit();
//		// }
//	}
//
//	private void createControls() {
//
//		listforms = new ArrayList<DynamicForm>();
//
//		lab1 = new Label();
//		lab1.removeStyleName("gwt-Label");
//		lab1.addStyleName("label-title");
//		lab1.setText(messages.budget());
//
//		// hierarchy = new String("");
//
//		budgetStartWithSelect = new SelectCombo(Global.get().messages()
//				.budgetStartWith());
//		budgetStartWithSelect.initCombo(getStartWithList());
//		budgetStartWithSelect.setSelectedItem(0);
//		budgetStartWithSelect
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
//
//					@SuppressWarnings("unchecked")
//					@Override
//					public void selectedComboBoxItem(String selectItem) {
//						if (selectItem == COPY_FROM_EXISTING) {
//							String budgetTitle = messages
//									.CopyvaluesfromExistingBudget();
//							CopyBudgetDialogue copybudgetDialogue = new CopyBudgetDialogue(
//									budgetTitle, "", budgetList);
//							copybudgetDialogue
//									.setCallback(new ActionCallback<String>() {
//
//										@Override
//										public void actionResult(String result) {
//											refreshView(result);
//
//										}
//
//									});
//							copybudgetDialogue.show();
//						}
//					}
//				});
//
//		budgetSubdevideBy = new SelectCombo(Global.get().messages()
//				.budgetSubdivide());
//		budgetSubdevideBy.setHelpInformation(true);
//		budgetSubdevideBy.initCombo(getSubdevideList());
//		budgetSubdevideBy
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
//
//					@Override
//					public void selectedComboBoxItem(String selectItem) {
//
//					}
//				});
//
//		selectFinancialYear = new SelectCombo(Global.get().messages()
//				.budgetFinancialYear());
//		selectFinancialYear.setHelpInformation(true);
//		selectFinancialYear.initCombo(getFiscalYearList());
//		selectFinancialYear.setRequired(true);
//		selectFinancialYear
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
//
//					@Override
//					public void selectedComboBoxItem(String selectItem) {
//
//					}
//				});
//
//		budgetNameText = new TextItem(messages.payeeName(messages.budget()));
//		budgetNameText.setToolTip(messages.giveTheNameAccordingToYourID(this
//				.getAction().getViewName()));
//		budgetNameText.setHelpInformation(true);
//		budgetNameText.setRequired(true);
//		budgetNameText.setWidth(100);
//		budgetNameText.addBlurHandler(new BlurHandler() {
//
//			public void onBlur(BlurEvent event) {
//
//				// Converts the first letter of Account Name to Upper case
//				String name = budgetNameText.getValue().toString();
//				if (name.isEmpty()) {
//					return;
//				}
//				String lower = name.substring(0, 1);
//				String upper = lower.toUpperCase();
//				budgetNameText.setValue(name.replaceFirst(lower, upper));
//
//			}
//		});
//
//		if (data != null) {
//			onEditChangeControls();
//		}
//
//		budgetInfoForm = UIUtils.form(messages.chartOfAccountsInformation());
//		budgetInfoForm.setWidth("100%");
//
//		topHLay = new StyledPanel();
//		topHLay.setWidth("50%");
//		leftLayout = new StyledPanel();
//		leftLayout.setWidth("100%");
//
//		// budgetInfoForm.setFields(budgetStartWithSelect, budgetSubdevideBy,
//		// selectFinancialYear, budgetNameText);
//
//		budgetInfoForm.setFields(budgetStartWithSelect, selectFinancialYear,
//				budgetNameText);
//
//		leftLayout.add(budgetInfoForm);
//		topHLay.add(leftLayout);
//
//		// budgetInfoForm.getCellFormatter().setWidth(0, 0, "200");
//
//		gridView = new BudgetAccountGrid();
//		gridView.setCanEdit(true);
//		gridView.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
//		gridView.isEnable = false;
//		if (data != null) {
//			gridView.setDisabled(true);
//		}
//
//		gridView.init();
//
//		if (data != null) {
//			List<ClientBudgetItem> itemsList = new ArrayList<ClientBudgetItem>();
//			itemsList = data.getBudgetItem();
//			for (ClientBudgetItem item : itemsList) {
//				ClientBudgetItem obj = new ClientBudgetItem();
//				obj.setAccountsName(item.getAccount().getName());
//				obj = item;
//				gridView.addData(obj);
//			}
//		} else {
//
//			listOfAccounts = getCompany().getAccounts();
//			for (ClientAccount account : listOfAccounts) {
//				ClientBudgetItem obj = new ClientBudgetItem();
//				gridView.addData(obj, account);
//			}
//		}
//
//		mainVLay = new StyledPanel();
//		mainVLay.setSize("100%", "300px");
//		mainVLay.add(lab1);
//		mainVLay.add(topHLay);
//		mainVLay.add(gridView);
//
//		// setHeightForCanvas("450");
//		this.add(mainVLay);
//
//		/* Adding dynamic forms in list */
//		listforms.add(budgetInfoForm);
//
//	}
//
//	private void onEditChangeControls() {
//
//		budgetStartWithSelect.setDisabled(true);
//		budgetNameText.setDisabled(true);
//		selectFinancialYear.setDisabled(true);
//
//		String budgetname = data.getBudgetName();
//
//		String[] temp;
//		String delimiter = " ";
//		temp = budgetname.split(delimiter);
//
//		budgetNameText.setValue(temp[0]);
//
//		if (temp[2].equals("FY2010")) {
//			selectFinancialYear.setSelected(FISCAL_YEAR_1);
//		} else if (temp[2].equals("FY2011")) {
//			selectFinancialYear.setSelected(FISCAL_YEAR_2);
//		} else if (temp[2].equals("FY2012")) {
//			selectFinancialYear.setSelected(FISCAL_YEAR_3);
//		} else if (temp[2].equals("FY2013")) {
//			selectFinancialYear.setSelected(FISCAL_YEAR_4);
//		} else if (temp[2].equals("FY2014")) {
//			selectFinancialYear.setSelected(FISCAL_YEAR_5);
//		} else if (temp[2].equals("FY2015")) {
//			selectFinancialYear.setSelected(FISCAL_YEAR_6);
//		} else if (temp[2].equals("FY2016")) {
//			selectFinancialYear.setSelected(FISCAL_YEAR_7);
//		} else if (temp[2].equals("FY2017")) {
//			selectFinancialYear.setSelected(FISCAL_YEAR_8);
//		} else if (temp[2].equals("FY2018")) {
//			selectFinancialYear.setSelected(FISCAL_YEAR_9);
//		}
//
//	}
//
//	private List<String> getFiscalYearList() {
//		List<String> list = new ArrayList<String>();
//
//		list.add(FISCAL_YEAR_1);
//		list.add(FISCAL_YEAR_2);
//		list.add(FISCAL_YEAR_3);
//		list.add(FISCAL_YEAR_4);
//		list.add(FISCAL_YEAR_5);
//		list.add(FISCAL_YEAR_6);
//		list.add(FISCAL_YEAR_7);
//		list.add(FISCAL_YEAR_8);
//		list.add(FISCAL_YEAR_9);
//
//		return list;
//	}
//
//	private List<String> getSubdevideList() {
//		List<String> list = new ArrayList<String>();
//
//		list.add(SUBDIVIDE_DONT);
//		list.add(SUBDIVIDE_BUSINESS);
//		list.add(SUBDIVIDE_CLASS);
//		list.add(SUBDIVIDE_CUSTOMER);
//
//		return list;
//	}
//
//	private List<String> getStartWithList() {
//		List<String> list = new ArrayList<String>();
//
//		list.add(NO_AMOUNT);
//		list.add(AUCTUAL_AMOUNT_LAST_FISCAL_YEAR);
//		list.add(AUCTUAL_AMOUNT_THIS_FISCAL_YEAR);
//		list.add(COPY_FROM_EXISTING);
//
//		return list;
//	}
//
//	@Override
//	public void deleteFailed(AccounterException caught) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void deleteSuccess(IAccounterCore result) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected String getViewTitle() {
//		return Global.get().messages().budget();
//
//	}
//
//	public List<DynamicForm> getForms() {
//		return listforms;
//	}
//
//	@Override
//	public ClientBudget saveView() {
//		ClientBudget saveView = super.saveView();
//		if (data != null) {
//			updateBudgetObject();
//		}
//		return saveView;
//	}
//
//	@Override
//	public void saveAndUpdateView() {
//		updateBudgetObject();
//
//		saveOrUpdate(getData());
//
//	}
//
//	private void updateBudgetObject() {
//
//		String budgetname = budgetNameText.getValue() != null ? budgetNameText
//				.getValue() : " ";
//		String financialYear = selectFinancialYear.getSelectedValue();
//
//		String[] temp;
//		String delimiter = " ";
//		temp = financialYear.split(delimiter);
//
//		data.setBudgetName(budgetname + " - " + temp[0]);
//
//		List<ClientBudgetItem> allGivenRecords = gridView.getRecords();
//
//		data.setBudgetItem(allGivenRecords);
//
//	}
//
//	@Override
//	public void saveFailed(AccounterException exception) {
//		super.saveFailed(exception);
//		// BaseView.errordata.setHTML(exception.getMessage());
//		// BaseView.commentPanel.setVisible(true);
//		// this.errorOccured = true;
//		// String exceptionMessage = exception.getMessage();
//		// addError(this, exceptionMessage);
//		AccounterException accounterException = exception;
//		int errorCode = accounterException.getErrorCode();
//		String errorString = AccounterExceptions.getErrorString(errorCode);
//		Accounter.showError(errorString);
//
//		updateBudgetObject();
//
//	}
//
//	@Override
//	public void saveSuccess(IAccounterCore result) {
//		if (result != null) {
//			super.saveSuccess(result);
//
//		} else {
//			saveFailed(new AccounterException());
//		}
//	}
//
//	@Override
//	public ValidationResult validate() {
//		ValidationResult result = new ValidationResult();
//
//		result.add(budgetInfoForm.validate());
//		if (data == null) {
//			String name = budgetNameText.getValue().toString() != null ? budgetNameText
//					.getValue().toString() : "";
//
//			String financialYear = selectFinancialYear.getSelectedValue();
//
//			String[] temp;
//			String delimiter = " ";
//			temp = financialYear.split(delimiter);
//
//			String budgetName = name + " - " + temp[0];
//
//			if (name != null && !name.isEmpty()) {
//
//				for (ClientBudget budget : budgetList) {
//					if (budgetName.equals(budget.getBudgetName())) {
//						result.addError(name, messages.alreadyExist());
//						break;
//					}
//				}
//			}
//		}
//		return result;
//
//	}
//
//	private void refreshView(String result) {
//
//		gridView.removeAllRecords();
//
//		for (ClientBudget budget : budgetList) {
//			List<ClientBudgetItem> budgetItemList = budget.getBudgetItem();
//			if (result.equals(budget.getBudgetName())) {
//				for (ClientBudgetItem items : budgetItemList) {
//					items.setAccountsName(items.getAccount().getName());
//					gridView.addData(items);
//				}
//				break;
//			}
//
//		}
//	}
//
//	@Override
//	public void onEdit() {
//		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {
//
//			@Override
//			public void onException(AccounterException caught) {
//				Accounter.showError(caught.getMessage());
//			}
//
//			@Override
//			public void onResultSuccess(Boolean result) {
//				if (result)
//					enableFormItems();
//			}
//
//		};
//
//		this.rpcDoSerivce.canEdit(AccounterCoreType.BUDGET, data.getID(),
//				editCallBack);
//	}
//
//	protected void enableFormItems() {
//		setMode(EditMode.EDIT);
//		budgetStartWithSelect.setDisabled(false);
//		budgetNameText.setDisabled(false);
//		selectFinancialYear.setDisabled(false);
//		gridView.setDisabled(false);
//
//		super.onEdit();
//
//	}
//
//	@Override
//	public void setFocus() {
//		this.budgetStartWithSelect.setFocus();
//
//	}
//}
