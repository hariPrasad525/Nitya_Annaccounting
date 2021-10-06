package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class VendorMergeView extends BaseView<ClientVendor> {

	private DynamicForm form;
	private DynamicForm form1;

	private VendorCombo vendorCombo;
	private VendorCombo vendorCombo1;
	private TextItem vendorIDTextItem;
	private TextItem vendorIDTextItem1;

	private CheckboxItem status;
	private CheckboxItem status1;
	private TextItem balanceTextItem1;
	private TextItem balanceTextItem;

	private ClientVendor fromclientVendor;
	private ClientVendor toClientVendor;

	public VendorMergeView() {
		this.getElement().setId("VendorMergeView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	protected void mergeVendor() {

		if (fromclientVendor != null && toClientVendor != null) {
			if (fromclientVendor.getID() == toClientVendor.getID()) {
			}
		}
		ClientCurrency currency1 = getCompany().getCurrency(
				fromclientVendor.getCurrency());
		ClientCurrency currency2 = getCompany().getCurrency(
				toClientVendor.getCurrency());
		if (!currency1.equals(currency2)) {
			Accounter.showError(messages
					.currenciesOfTheBothCustomersMustBeSame(Global.get()
							.vendors()));
		} else {
			Accounter.createHomeService().mergeVendor(fromclientVendor,
					toClientVendor, new AccounterAsyncCallback<Void>() {

						@Override
						public void onException(AccounterException exception) {
							Accounter.showError(exception.getMessage());
						}

						@Override
						public void onResultSuccess(Void result) {
							onClose();
						}
					});

		}

	}

	private void createControls() {
		Label lab1 = new Label(messages.mergeVendors(Global.get().vendors()));
		lab1.setStyleName("label-title");

		form = new DynamicForm("form");
		form1 = new DynamicForm("form1");
		StyledPanel layout = new StyledPanel("layout");
		StyledPanel layout1 = new StyledPanel("layout1");
		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		vendorCombo = createVendorCombo();
		vendorCombo1 = createVendorCombo1();

		vendorIDTextItem = new TextItem(
				messages.payeeID(Global.get().Vendor()), "vendorIDTextItem");

		vendorIDTextItem1 = new TextItem(
				messages.payeeID(Global.get().Vendor()), "vendorIDTextItem1");
		vendorIDTextItem.setEnabled(false);
		vendorIDTextItem1.setEnabled(false);

		status = new CheckboxItem(messages.active(), "status");
		status.setValue(false);

		status1 = new CheckboxItem(messages.active(), "status1");
		status1.setValue(false);

		balanceTextItem = new TextItem(messages.balance(), "balanceTextItem");

		balanceTextItem1 = new TextItem(messages.balance(), "balanceTextItem1");
		balanceTextItem.setEnabled(false);
		balanceTextItem1.setEnabled(false);

		form.add(vendorCombo, vendorIDTextItem, status, balanceTextItem);
		form1.add(vendorCombo1, vendorIDTextItem1, status1, balanceTextItem1);
		// form.setItems(getTextItems());
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(lab1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		this.add(horizontalPanel);

	}

	private VendorCombo createVendorCombo1() {
		vendorCombo1 = new VendorCombo(messages.payeeTo(Global.get().Vendor()),
				false);
		vendorCombo1.setRequired(true);
		vendorCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						toClientVendor = selectItem;
						customerSelected1(selectItem);

					}

				});

		return vendorCombo1;
	}

	private VendorCombo createVendorCombo() {
		vendorCombo = new VendorCombo(
				messages.payeeFrom(Global.get().Vendor()), false);
		vendorCombo.setRequired(true);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						fromclientVendor = selectItem;
						customerSelected(selectItem);

					}

				});

		return vendorCombo;
	}

	private void customerSelected(ClientVendor selectItem) {

		vendorIDTextItem.setValue(String.valueOf(selectItem.getID()));
		balanceTextItem.setValue(String.valueOf(selectItem.getBalance()));
		status.setValue(selectItem.isActive());

	}

	private void customerSelected1(ClientVendor selectItem) {
		vendorIDTextItem1.setValue(String.valueOf(selectItem.getID()));
		balanceTextItem1.setValue(String.valueOf(selectItem.getBalance()));
		status1.setValue(selectItem.isActive());
	}

	public ValidationResult validate() {
		ValidationResult result = form.validate();
		if (fromclientVendor != null && toClientVendor != null) {
			if (fromclientVendor.getID() == toClientVendor.getID()) {
				result.addError(fromclientVendor,
						messages.notMove(Global.get().vendors()));
				return result;
			}
		}

		result.add(form1.validate());

		return result;

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return messages.mergeVendors(Global.get().Vendor());
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void saveAndUpdateView() {
		mergeVendor();
	}

	@Override
	protected void createButtons() {
		saveAndCloseButton = new SaveAndCloseButton(this);
		saveAndCloseButton.setText(messages.merge());
		saveAndCloseButton.getElement().setAttribute("data-icon", "remote");
		addButton(saveAndCloseButton);

		cancelButton = new CancelButton(this);
		addButton(cancelButton);
	}
}
