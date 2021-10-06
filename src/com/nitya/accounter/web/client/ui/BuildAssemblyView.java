package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientBuildAssembly;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientInventoryAssembly;
import com.nitya.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientUnit;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.InventoryAssemblyItemCombo;
import com.nitya.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class BuildAssemblyView extends
		AbstractTransactionBaseView<ClientBuildAssembly> {

	public BuildAssemblyView() {
		super(ClientTransaction.TYPE_BUILD_ASSEMBLY);
	}

	private InventoryAssemblyItemCombo itemCombo;
	private AmountLabel quantityOnHand, buildPoint, maximumBuildsLabel;
	private InventoryAssemblyItemTable itemTable;
	private AmountField quantityToBuild;
	private DateField dateField;
	private TextAreaItem memoItem;

	@Override
	public void init() {
		this.getElement().setId("BuildAssemblyView");
		super.init();
	}

	@Override
	protected void createControls() {

		Label lab1 = new Label(messages.buildAssembly());
		lab1.setStyleName("label-title");

		itemCombo = new InventoryAssemblyItemCombo(messages.assemblyItem());
		itemCombo.setRequired(true);

		quantityOnHand = new AmountLabel(messages.quantityOnHand());
		buildPoint = new AmountLabel(messages.buildPoint());
		itemTable = new InventoryAssemblyItemTable(
				InventoryAssemblyItemTable.BUILD_ASSEMBLY,
				new ICurrencyProvider() {

					@Override
					public ClientCurrency getTransactionCurrency() {
						return getCompany().getPrimaryCurrency();
					}

					@Override
					public Double getCurrencyFactor() {
						return 1.0;
					}

					@Override
					public Double getAmountInBaseCurrency(Double amount) {
						return amount;
					}
				}) {

			@Override
			protected void updateNonEditableItems() {

			}

			@Override
			protected boolean isInViewMode() {
				return true;
			}

			@Override
			protected long getAssembly() {
				if (getData() == null) {
					return 0;
				}
				return getData().getInventoryAssembly();
			}

		};

		itemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientInventoryAssembly>() {

					@Override
					public void selectedComboBoxItem(
							ClientInventoryAssembly selectItem) {
						inventoryAssemblySelected(selectItem);
					}
				});
		itemTable.setEnabled(false);
		quantityToBuild = new AmountField(messages.quantityToBuild(), this);
		quantityToBuild.setRequired(true);
		dateField = new DateField(messages.date(), "dateField");
		dateField.setEnteredDate(new ClientFinanceDate());
		dateField.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				setTransactionDate(dateField.getDate());
			}
		});
		setTransactionDate(dateField.getDate());
		;
		memoItem = createMemoTextAreaItem();
		maximumBuildsLabel = new AmountLabel(
				messages.maximumNumberYouCanBuildFrom());
		quantityToBuild.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (maximumBuildsLabel.getAmount() != null
						&& DecimalUtil.isLessThan(
								maximumBuildsLabel.getAmount(),
								quantityToBuild.getAmount())) {
					Accounter.showError(messages
							.dntHaveEnoughComponents(maximumBuildsLabel
									.getValue()));
					quantityToBuild.setAmount(0.0);
				}
			}
		});

		transactionNumber = new TextItem(messages.refNo(), "transactionNumber");
		DynamicForm headerForm = new DynamicForm("headerForm");
		headerForm.add(dateField, transactionNumber);

		DynamicForm comboForm = new DynamicForm("comboForm");
		comboForm.add(itemCombo, quantityOnHand, buildPoint);

		StyledPanel topPanel = new StyledPanel("topPanel");
		topPanel.add(headerForm);
		topPanel.add(comboForm);

		DynamicForm buildToForm = new DynamicForm("buildToForm");
		buildToForm.add(quantityToBuild);

		DynamicForm memoForm = new DynamicForm("memoForm");
		memoForm.add(memoItem);

		DynamicForm maximumBuildsForm = new DynamicForm("maximumBuildsForm");
		maximumBuildsForm.add(maximumBuildsLabel);

		DynamicForm quantityToBuildForm = new DynamicForm("quantityToBuildForm");
		quantityToBuildForm.add(quantityToBuild);

		StyledPanel panel = new StyledPanel("panel");
		panel.add(maximumBuildsForm);
		panel.add(quantityToBuildForm);

		StyledPanel bottomPanel = new StyledPanel("bottomPanel");
		bottomPanel.add(memoForm);
		bottomPanel.add(panel);

		StyledPanel mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(lab1);
		mainPanel.add(topPanel);
		Label assemblyTableTitle = new Label(messages2.table(messages
				.assemblyItem()));
		assemblyTableTitle.addStyleName("editTableTitle");
		StyledPanel itemPanel = new StyledPanel("assemblyTableContainer");
		itemPanel.add(assemblyTableTitle);
		itemPanel.add(itemTable);

		mainPanel.add(itemPanel);
		mainPanel.add(bottomPanel);

		this.add(mainPanel);
		enableFormItems();

	}

	private void enableFormItems() {
		itemCombo.setEnabled(!isInViewMode());
		dateField.setEnabled(!isInViewMode());
		transactionNumber.setRequired(isInViewMode());
		quantityToBuild.setEnabled(!isInViewMode());
		memoItem.setEnabled(!isInViewMode());
	}

	@Override
	public void onEdit() {
		super.onEdit();
		enableFormItems();
	}

	protected void inventoryAssemblySelected(ClientInventoryAssembly selectItem) {
		if (selectItem != null) {
			if (selectItem.getComponents().isEmpty()) {
				Accounter.showInformation(messages
						.youCannotBuildWithoutComponents());
				return;
			}
			if (selectItem.getReorderPoint() != null) {
				buildPoint.setAmount(selectItem.getReorderPoint().getValue());
			}
			quantityOnHand.setAmount(selectItem.getOnhandQty().getValue());
			Set<ClientInventoryAssemblyItem> components = selectItem
					.getComponents();
			List<ClientInventoryAssemblyItem> assemblyItems = new ArrayList<ClientInventoryAssemblyItem>();
			for (ClientInventoryAssemblyItem clientInventoryAssemblyItem : components) {
				assemblyItems.add(clientInventoryAssemblyItem);
			}
			itemTable.setRecords(assemblyItems);
			Double maxBuilds = calculateNumberOfBuilds(assemblyItems);
			maximumBuildsLabel.setAmount(maxBuilds);

		}

	}

	@Override
	protected void createButtons() {
		super.createButtons();
		saveAndCloseButton.setText(messages.buildAndClose());
		saveAndNewButton.setText(messages.buildAndNew());
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public String getViewTitle() {
		return "Build Assembly View";
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	private Double calculateNumberOfBuilds(
			List<ClientInventoryAssemblyItem> assemblyItems) {
		Double buildNumber = null;
		for (ClientInventoryAssemblyItem inventoryAssemblyItem : assemblyItems) {
			ClientItem item = getCompany().getItem(
					inventoryAssemblyItem.getInventoryItem());
			if (item.isInventory()) {
				double onhandQuantity = item.getOnhandQty().getValue();
				ClientUnit unitById = getCompany().getUnitById(
						inventoryAssemblyItem.getQuantity().getUnit());
				double value = inventoryAssemblyItem.getQuantity()
						.convertToDefaultUnit(unitById).getValue();
				double temp = (int) (onhandQuantity / value);
				if (buildNumber == null) {
					buildNumber = temp;
				}
				if (buildNumber > temp) {
					buildNumber = temp;
				}
			}
		}
		return buildNumber;
	}

	@Override
	protected void updateTransaction() {

		if (itemCombo.getSelectedValue() != null) {
			transaction.setInventoryAssembly(itemCombo.getSelectedValue()
					.getID());
		}
		if (dateField.getTime() <= 0) {
			transaction.setDate(dateField.getTime());
			transactionDate = dateField.getDate();
		}
		if (transactionNumber.getValue() != null) {
			transaction.setNumber(transactionNumber.getValue());
		}
		if (quantityToBuild.getAmount() != null) {
			transaction.setQuantityToBuild(quantityToBuild.getAmount());
		}
		super.updateTransaction();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (maximumBuildsLabel.getAmount() != null
				&& maximumBuildsLabel.getAmount() <= 0) {
			result.addError(quantityToBuild,
					messages.youCannotBuildThisAssembly());
		}

		if (quantityToBuild.getAmount() <= 0) {
			result.addError(quantityToBuild,
					messages.pleaseEnter(quantityToBuild.getTitle()));
		}
		if (itemCombo.getSelectedValue() == null) {
			result.addError(itemCombo,
					messages.pleaseSelect(itemCombo.getTitle()));
		} else {
			if (itemCombo.getSelectedValue().getComponents().isEmpty()) {
				result.addError(itemCombo,
						messages.youCannotBuildWithoutComponents());
			}
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(transaction);
	}

	@Override
	protected void initTransactionViewData() {
		if (data == null) {
			setData(new ClientBuildAssembly());
		} else {
			setData(data);
			if (data.getInventoryAssembly() != 0) {
				ClientInventoryAssembly assembly = (ClientInventoryAssembly) getCompany()
						.getItem(data.getInventoryAssembly());
				if (assembly.getReorderPoint() != null)
					buildPoint.setAmount(assembly.getReorderPoint().getValue());
				quantityOnHand.setAmount(assembly.getOnhandQty().getValue());
				Set<ClientInventoryAssemblyItem> components = assembly
						.getComponents();
				List<ClientInventoryAssemblyItem> assemblyItems = new ArrayList<ClientInventoryAssemblyItem>();
				for (ClientInventoryAssemblyItem clientInventoryAssemblyItem : components) {
					assemblyItems.add(clientInventoryAssemblyItem);
				}
				itemTable.setRecords(assemblyItems);
				Double calculateNumberOfBuilds = calculateNumberOfBuilds(assemblyItems);
				maximumBuildsLabel.setAmount(calculateNumberOfBuilds);
			}

			if (data.getInventoryAssembly() != 0) {
				ClientInventoryAssembly item = (ClientInventoryAssembly) getCompany()
						.getItem(data.getInventoryAssembly());
				itemCombo.setComboItem(item);
			}
			if (data.getDate() != null) {
				dateField.setDefaultValue(data.getDate());
			}
			if (data.getNumber() != null) {
				transactionNumber.setValue(data.getNumber());
			}
			if (data.getQuantityToBuild() != null) {
				quantityToBuild.setAmount(data.getQuantityToBuild());
			}

		}

	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAmountsFromGUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientBuildAssembly saveView() {
		ClientBuildAssembly saveView = super.saveView();
		if (saveView != null) {
			ClientInventoryAssembly selectedValue = itemCombo
					.getSelectedValue();
			if (selectedValue != null) {
				saveView.setInventoryAssembly(selectedValue.getID());
			}
		}
		return saveView;
	}

	@Override
	public void restoreView(ClientBuildAssembly assembly) {
		super.restoreView(assembly);
		ClientInventoryAssembly item = (ClientInventoryAssembly) getCompany()
				.getItem(assembly.getInventoryAssembly());
		itemCombo.setComboItem(item);
		inventoryAssemblySelected(item);
	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	@Override
	public boolean canEdit() {
		if (transaction == null || transaction.getID() == 0) {
			return super.canEdit();
		}
		return false;
	}

	@Override
	protected boolean canDelete() {
		return true;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return false;
	}
}
