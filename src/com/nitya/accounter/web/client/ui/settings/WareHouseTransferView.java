package com.nitya.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientStockTransfer;
import com.nitya.accounter.web.client.core.ClientStockTransferItem;
import com.nitya.accounter.web.client.core.ClientWarehouse;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.WarehouseCombo;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.edittable.tables.WareHouseTransferTable;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;

public class WareHouseTransferView extends BaseView<ClientStockTransfer> {

	private StyledPanel mainPanel;
	private WareHouseTransferTable table;
	private WarehouseCombo fromCombo, toCombo;
	private TextAreaItem commentArea;
	private DynamicForm form;
	private List<DynamicForm> listForms;
	private ClientWarehouse selectedFrom;

	@Override
	protected String getViewTitle() {
		return messages.wareHouseTransfer();
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("WareHouseTransferView");
		createControls();
	}

	private void createControls() {
		Label lab1 = new Label(messages.wareHouseTransfer());
		lab1.setStyleName("label-title");
		listForms = new ArrayList<DynamicForm>();
		mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(lab1);
		fromCombo = new WarehouseCombo(messages.from()) {
			@Override
			public void addItemThenfireEvent(ClientWarehouse obj) {
				super.addItemThenfireEvent(obj);
				toCombo.initCombo(getCompany().getWarehouses());
			}
		};
		fromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientWarehouse>() {

					@Override
					public void selectedComboBoxItem(ClientWarehouse selectItem) {
						if (selectedFrom != null
								&& selectedFrom.getID() == selectItem.getID()) {
							return;
						}
						fromWareHouseSelected(selectItem);
					}
				});
		fromCombo.setRequired(true);
		fromCombo.setEnabled(!isInViewMode());
		toCombo = new WarehouseCombo(messages.to()) {
			@Override
			public void addItemThenfireEvent(ClientWarehouse obj) {
				super.addItemThenfireEvent(obj);
				fromCombo.initCombo(getCompany().getWarehouses());
			}
		};
		toCombo.setEnabled(!isInViewMode());
		toCombo.setRequired(true);
		commentArea = new TextAreaItem(messages.comment(), "commentArea");
		commentArea.setTitle(messages.comment());
		commentArea.setDisabled(isInViewMode());
		form = new DynamicForm("fields-panel");
		form.add(fromCombo, toCombo, commentArea);
		listForms.add(form);
		mainPanel.add(form);
		table = new WareHouseTransferTable() {

			@Override
			protected boolean isInViewMode() {
				return WareHouseTransferView.this.isInViewMode();
			}
		};
		table.setEnabled(!isInViewMode());
		Label tableTitle = new Label(messages2.table(messages.units()));
		tableTitle.addStyleName("editTableTitle");
		StyledPanel tablePanel = new StyledPanel("transferTablePanel");
		tablePanel.add(tableTitle);
		tablePanel.add(table);
		mainPanel.add(tablePanel);
		// mainPanel.setSize("100%", "100%");

		this.add(mainPanel);
	}

	protected void fromWareHouseSelected(ClientWarehouse selectItem) {
		selectedFrom = selectItem;
		Accounter
				.createHomeService()
				.getStockTransferItems(
						selectItem.getID(),
						new AccounterAsyncCallback<ArrayList<ClientStockTransferItem>>() {

							@Override
							public void onException(AccounterException exception) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onResultSuccess(
									ArrayList<ClientStockTransferItem> result) {
								table.removeAllRecords();
								if (result != null && !result.isEmpty()) {
									table.setAllRows(result);
								} else {
									table.addEmptyMessage(messages
											.noRecordsToShow());
								}
							}
						});
	}

	@Override
	public void onEdit() {
		Accounter.showWarning(messages.W_117(), AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {
						return true;
					}

					@Override
					public boolean onNoClick() {
						return true;

					}

					@Override
					public boolean onYesClick() {
						deleteTransfer();
						return true;
					}
				});
	}

	protected void deleteTransfer() {
		AccounterAsyncCallback<Boolean> callBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				Accounter.showError("");
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					enableFormItems();
				} else {
					onException(new AccounterException());
				}
			}
		};
		Accounter.createCRUDService().delete(data.getObjectType(),
				data.getID(), callBack);
	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		setData(new ClientStockTransfer());
		fromCombo.setEnabled(!isInViewMode());
		toCombo.setEnabled(!isInViewMode());
		commentArea.setDisabled(isInViewMode());
		table.setEnabled(!isInViewMode());
		fromWareHouseSelected(fromCombo.getSelectedValue());
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// currently not using

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// currently not using

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// currently not using

	}

	@Override
	public List<DynamicForm> getForms() {
		return listForms;
	}

	@Override
	public void setFocus() {
		this.fromCombo.setFocus();
	}

	@Override
	public ClientStockTransfer saveView() {
		ClientStockTransfer saveview = super.saveView();
		if (saveview != null) {
			updateData();
		}
		return saveview;
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void updateData() {
		if (fromCombo.getSelectedValue() != null) {
			data.setFromWarehouse(fromCombo.getSelectedValue().getID());
		}
		if (toCombo.getSelectedValue() != null) {
			data.setToWarehouse(toCombo.getSelectedValue().getID());
		}
		data.setMemo(commentArea.getValue());
		data.setStockTransferItems(table.getSelectedRecords());
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientStockTransfer());
		} else {
			fromCombo.setComboItem(getCompany().getWarehouse(
					data.getFromWarehouse()));
			toCombo.setComboItem(getCompany().getWarehouse(
					data.getToWarehouse()));
			commentArea.setValue(data.getMemo());
			for (ClientStockTransferItem item : data.getStockTransferItems()) {
				table.add(item);
			}
		}

		super.initData();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(form.validate());
		result.add(table.validate());
		if (fromCombo.getSelectedValue() != null
				&& toCombo.getSelectedValue() != null) {
			if (fromCombo.getSelectedValue().getID() == toCombo
					.getSelectedValue().getID()) {
				result.addError(form,
						messages.pleaseSelectDiffWarehousesToTransfer());
			}
		}
		return result;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		if (isInViewMode()) {
			return super.canDelete();
		}
		return false;
	}
}
