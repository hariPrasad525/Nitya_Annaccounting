package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientDepreciation;
import com.nitya.accounter.web.client.core.ClientDepreciationDummyEntry;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientFiscalYear;
import com.nitya.accounter.web.client.core.ClientFixedAsset;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.Lists.DepreciableFixedAssetsEntry;
import com.nitya.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.nitya.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.nitya.accounter.web.client.core.Lists.LinkAccount;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.Calendar;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.fixedassets.RollBackDepreciationDialog;
import com.nitya.accounter.web.client.ui.fixedassets.StartDateDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.DepreciationTreeGrid;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

/**
 * 
 * @author Raj Vimal
 * 
 */
public class DepreciationView extends BaseView<ClientDepreciation> {

	private DepreciationTreeGrid grid;
	private ClientFinanceDate depreciationStartDate;
	protected ListBox depreciatedToCombo;
	private ArrayList<DynamicForm> listforms;
	protected ClientFinanceDate depreciationEndDate;
	private Button startDateButton, rollBackDepreciation;
	protected ClientAccount account;
	private final List<Long> assetIDList;
	private List<ClientFiscalYear> openFiscalYears;
	private Label fromLabel;
	private DateTimeFormat format;
	private Map<Long, List<DepreciableFixedAssetsEntry>> assets;
	FixedAssetLinkedAccountMap map;
	private Button updateButton;

	public DepreciationView() {
		super();
		assetIDList = new ArrayList<Long>();
		account = new ClientAccount();
		getDepriciationLastDate();
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("DepreciationView");
		createControls();

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();
		StyledPanel mainPanel = new StyledPanel("mainPanel");

		Label titleLabel = new Label(messages.depreciation());
		titleLabel.setStyleName("label-title");
		mainPanel.add(titleLabel);

		startDateButton = new Button(messages.startDate());
		startDateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new StartDateDialog();

			}
		});

		rollBackDepreciation = new Button(messages.rollBackDepreciation());
		rollBackDepreciation.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getLastDepreciationDate();
			}
		});

		StyledPanel buttonPanel = new StyledPanel("buttonPanel");
		// buttonPanel.setSpacing(10);
		buttonPanel.add(startDateButton);
		buttonPanel.add(rollBackDepreciation);
		rollBackDepreciation.setVisible(false);
		mainPanel.add(buttonPanel);
		fromLabel = new Label(messages.depricatiedFrom());

		format = DateTimeFormat.getFormat(getPreferences().getDateFormat());
		depreciatedToCombo = new ListBox();

		depreciatedToCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getDepreciableFixedAssets();

			}
		});
		// depreciatedToCombo.setWidth("100px");

		Button updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				getDepreciableFixedAssets();
			}
		});
		StyledPanel panel = new StyledPanel("panel");
		// panel.setSpacing(10);
		panel.add(fromLabel);
		panel.add(new HTML(messages.depreciateTo()));
		panel.add(depreciatedToCombo);
		// panel.add(updateButton);
		mainPanel.add(panel);

		StyledPanel gridPanel = new StyledPanel("gridPanel");

		grid = new DepreciationTreeGrid("");
		grid.isEnable = false;
		grid.init();
		grid.initParentAndChildIcons(Accounter.getFinanceMenuImages()
				.newAccount(), Accounter.getFinanceMenuImages().newFixedAsset());
		gridPanel.add(grid);

		mainPanel.add(gridPanel);

		StyledPanel actionButtonPanel = new StyledPanel("actionButtonPanel");
		mainPanel.add(actionButtonPanel);

		this.add(mainPanel);

	}

	private void getLastDepreciationDate() {
		AccounterAsyncCallback<ClientFinanceDate> callBack = new AccounterAsyncCallback<ClientFinanceDate>() {

			private ClientFinanceDate lastDepreciationDate;

			@Override
			public void onException(AccounterException caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onResultSuccess(ClientFinanceDate result) {
				lastDepreciationDate = result;
				RollBackDepreciationDialog dialog = new RollBackDepreciationDialog(
						lastDepreciationDate);
				ViewManager.getInstance().showDialog(dialog);
			}

		};
		Accounter.createHomeService().getDepreciationLastDate(callBack);

	}

	private void getDepriciationLastDate() {
		AccounterAsyncCallback<ClientFinanceDate> callBack = new AccounterAsyncCallback<ClientFinanceDate>() {
			@Override
			public void onException(AccounterException caught) {
				saveFailed(caught);
				return;

			}

			@Override
			public void onResultSuccess(ClientFinanceDate date) {
				if (date == null) {
					ClientFinanceDate date2 = new ClientFinanceDate(
							getCompany().getPreferences()
									.getDepreciationStartDate());
					depreciationStartDate = new ClientFinanceDate(
							date2.getYear(), date2.getMonth(), 1);
				} else {
					depreciationStartDate = new ClientFinanceDate(
							date.getYear(), date.getMonth(), date.getDay() + 1);
				}
				fromLabel
						.setText(messages.depreciatefrom()
								+ format.format(depreciationStartDate
										.getDateAsObject()));
				for (String dateArray : getDatesArray()) {
					depreciatedToCombo.addItem(dateArray);
				}
				if (depreciationStartDate.getDate() > getPreferences()
						.getStartOfFiscalYear()) {
					rollBackDepreciation.setVisible(true);
				}
				getDepreciableFixedAssets();
			}
		};
		Accounter.createHomeService().getDepreciationLastDate(callBack);
	}

	@Override
	public void initData() {
		super.initData();
		initGrid();

	}

	private void initGrid() {

	}

	private List<String> getDatesArray() {

		DateTimeFormat format = DateTimeFormat.getFormat(getPreferences()
				.getDateFormat());
		List<String> dates = new ArrayList<String>();
		Calendar fromDateCal = Calendar.getInstance();
		fromDateCal.setTime(depreciationStartDate.getDateAsObject());

		Calendar startDateCal = Calendar.getInstance();
		startDateCal
				.setTime(new ClientFinanceDate(Accounter.getCompany()
						.getPreferences().getDepreciationStartDate())
						.getDateAsObject());

		Calendar toDateCal = Calendar.getInstance();
		int year = 0;

		if (fromDateCal.get(Calendar.MONTH) >= startDateCal.get(Calendar.MONTH)) {
			year = fromDateCal.get(Calendar.YEAR) + 1;
		} else {
			year = fromDateCal.get(Calendar.YEAR);
		}

		openFiscalYears = AccounterValidator.getOpenFiscalYears();

		toDateCal.set(Calendar.YEAR, year);
		int month = 0;
		if (startDateCal.get(Calendar.MONTH) - 1 >= 0) {
			month = startDateCal.get(Calendar.MONTH) - 1;
		}
		toDateCal.set(Calendar.DAY_OF_MONTH, 1);
		toDateCal.set(Calendar.MONTH, month);

		while (fromDateCal.getTime().compareTo(toDateCal.getTime()) < 0) {
			Calendar tempCal = Calendar.getInstance();
			tempCal.set(Calendar.YEAR, fromDateCal.get(Calendar.YEAR));
			tempCal.set(Calendar.MONTH, fromDateCal.get(Calendar.MONTH));
			tempCal.set(Calendar.DAY_OF_MONTH,
					fromDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));

			if (validateDate(new ClientFinanceDate(tempCal.date)))
				dates.add(format.format(tempCal.getTime()));
			fromDateCal
					.set(Calendar.MONTH, fromDateCal.get(Calendar.MONTH) + 1);

		}

		return dates;
	}

	private boolean validateDate(ClientFinanceDate date) {

		boolean validDate = true;
		for (ClientFiscalYear openFiscalYear : openFiscalYears) {
			ClientFinanceDate endDate = openFiscalYear.getEndDate();
			/**
			 * The below code will increment the fiscal year end date by 1, this
			 * will be helpful when the date and fiscal year end date are same.
			 **/
			endDate.setDay(endDate.getDay() + 1);
			int before = date.compareTo(openFiscalYear.getStartDate());
			int after = date.compareTo(endDate);

			validDate = (before < 0 || after > 0) ? false : true;
			if (validDate)
				break;

		}
		return validDate;
	}

	@Override
	public ClientDepreciation saveView() {
		ClientDepreciation saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	private void updateTransaction() {
		if (data == null) {
			setData(new ClientDepreciation());
		}
		data.setDepreciateFrom(depreciationStartDate.getDate());
		data.setDepreciateTo(depreciationEndDate.getDate());
		data.setDepreciationFor(ClientDepreciation.DEPRECIATION_FOR_ALL_FIXEDASSET);
		// data.setFixedAssets(getAssetsList());
		// // depreciation.setFixedAssets(getAssetsList());
		// depreciation.setStatus(ClientDepreciation.APPROVE);
		//
		List<LinkAccount> linkedAccounts = new ArrayList<LinkAccount>();
		for (ClientDepreciationDummyEntry entry : grid.getNodes()) {

			for (Long key : assets.keySet()) {
				ClientAccount account = getCompany().getAccount(key);
				if (account.getName().equals(entry.getFixedAssetName())) {
					LinkAccount link = new LinkAccount();
					link.setAssetAccount(key);
					link.setLinkedAccount(entry.getAssetAccount());
					linkedAccounts.add(link);
				}
			}
		}
		map = new FixedAssetLinkedAccountMap();
		map.setFixedAssetLinkedAccounts(linkedAccounts);
		// depreciation.setLinkedAccounts(map);
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(data);
		Accounter.createHomeService().runDepreciation(
				depreciationStartDate.getDate(), depreciationEndDate.getDate(),
				map, new AccounterAsyncCallback<Object>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages.depreciationfailed());

					}

					@Override
					public void onResultSuccess(Object result) {

					}

				});
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (!(assets.size() > 0)) {
			result.addError(this, messages.pleaseselectaFixedAsset());
		}
		result.add(super.validate());
		return result;
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			super.saveSuccess(result);
			new DepreciationAction().run(null, true);

		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	protected void getDepreciableFixedAssets() {

		int index = depreciatedToCombo.getSelectedIndex();

		if (index >= 0) {

			String dateString = depreciatedToCombo.getValue(index).toString();
			// ClientFinanceDate clientFinanceDate = new ClientFinanceDate(
			// dateString);

			depreciationEndDate = DateUtills.getDateFromString(dateString);

			AccounterAsyncCallback<DepreciableFixedAssetsList> callBack = new AccounterAsyncCallback<DepreciableFixedAssetsList>() {

				@Override
				public void onException(AccounterException caught) {
					saveFailed(caught);
					return;

				}

				@Override
				public void onResultSuccess(DepreciableFixedAssetsList result) {
					setValuesInGrid(result);

				}

			};
			if (depreciationStartDate != null && depreciationEndDate != null)
				grid.removeAllRows();
			grid.addLoadingImagePanel();
			Accounter.createHomeService().getDepreciableFixedAssets(
					depreciationStartDate.getDate(),
					depreciationEndDate.getDate(), callBack);
		}
	}

	protected void setValuesInGrid(DepreciableFixedAssetsList assestsList) {
		if (assestsList != null) {
			// if (grid.getRowCount() > 0) {
			grid.removeAllRows();
			// }
			assets = assestsList.getAccountViceFixedAssets();
			if (assets.size() > 0) {
				assetIDList.addAll(assestsList.getFixedAssetIDs());
				for (Long key : assets.keySet()) {
					ClientAccount account = getCompany().getAccount(key);
					List<ClientDepreciationDummyEntry> dummyEntriesList = new ArrayList<ClientDepreciationDummyEntry>();
					for (DepreciableFixedAssetsEntry entry : assets.get(key)) {
						ClientDepreciationDummyEntry dummyEntry = new ClientDepreciationDummyEntry();
						dummyEntry.setFixedAssetName(entry.getFixedAssetName());
						dummyEntry.setAmountToBeDepreciated(entry
								.getAmountToBeDepreciated());
						dummyEntry.setAssetAccount(account != null ? account
								.getLinkedAccumulatedDepreciationAccount()
								: null);
						dummyEntriesList.add(dummyEntry);
					}
					// grid.addParent(account != null ? account.getName() : "");
					grid.addParentWithChilds(
							account != null ? account.getName() : "",
							dummyEntriesList);
				}
			} else
				grid.addEmptyMessage(messages.noDepreciableFixedAssetstoshow());
		} else
			grid.addEmptyMessage(messages.noDepreciableFixedAssetstoshow());

	}

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.startDateButton.setFocus(true);
	}

	private List<ClientFixedAsset> getAssetsList() {
		List<ClientFixedAsset> fixedAssetList = new ArrayList<ClientFixedAsset>();
		for (Long id : assetIDList) {
			fixedAssetList.add(getCompany().getFixedAsset(id));
		}
		return fixedAssetList;
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
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
	}

	@Override
	protected String getViewTitle() {
		return messages.depreciation();
	}

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean isSaveButtonAllowed() {
		return false;
	}

	@Override
	protected void createButtons() {
		updateButton = new Button();
		updateButton.setText(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onSave(false);
			}
		});
		updateButton.getElement().setAttribute("data-icon", "accept");
		addButton(updateButton);
		super.createButtons();
	}

}
