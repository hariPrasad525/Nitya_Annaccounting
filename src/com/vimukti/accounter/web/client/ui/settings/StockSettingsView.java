package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class StockSettingsView extends BaseView {

	private DateItem adjustDate;
	protected TextItem entryNo, adjustmentQuantity;
	private WarehouseCombo warehouseComboSelect;
	private SelectCombo itemComboSelect;
	private ItemCombo itemGroupComboSelect;
	private AmountField amountText;
	private LabelItem itemIncomeAmoutText, itemExpenseAmountText,
			itemIncomeAmoutTextlabel, itemExpenseAmountTextlabel;
	private TextAreaItem memo;
	private DynamicForm vatform, dateForm, lowerform, middleform;

	@Override
	protected String getViewTitle() {
		return messages.stockSettings();
	}

	public void init() {
		super.init();
		this.getElement().setId("StockSettingsView");
		createControls();
		//setSize("100%", "100%");

	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public List getForms() {
		return null;
	}

	private void createControls() {

		Label infoLabel;
		infoLabel = new Label(messages.stockSettings());
		infoLabel.removeStyleName("gwt-Label");

		infoLabel.setStyleName("label-title");
		adjustDate = new DateItem(null, "adjustDate");
		adjustDate.setDatethanFireEvent(new ClientFinanceDate());

		entryNo = new IntegerField(this, messages.no());
//		entryNo.setWidth(100);

		warehouseComboSelect = new WarehouseCombo(messages.wareHouse());

		itemComboSelect = new SelectCombo(messages.itemType());
		List<String> combolist = new ArrayList<String>();
		combolist.add(messages.product());
		combolist.add(messages.service());
		itemComboSelect.initCombo(combolist);
		itemGroupComboSelect = new ItemCombo(messages.stockItemName(), 0);
		adjustmentQuantity = new IntegerField(this,
				messages.stockAdjustmentQuantity());
//		entryNo.setWidth(100);
		itemIncomeAmoutText = new LabelItem("", "itemIncomeAmoutText");
		itemExpenseAmountText = new LabelItem("", "itemExpenseAmountText");
		itemIncomeAmoutTextlabel = new LabelItem("", "itemIncomeAmoutTextlabel");
		itemExpenseAmountTextlabel = new LabelItem("",
				"itemExpenseAmountTextlabel");
		itemIncomeAmoutTextlabel.setValue(messages.stockItemIncomeAccount());
		itemExpenseAmountTextlabel.setValue(messages.stockItemExpenseAccount());
		amountText = new AmountField(messages.stockItemAmount(), this,
				getBaseCurrency(), "amount_Text");
		memo = new TextAreaItem(messages.memo(), "memoArea");
		memo.setTitle(messages.writeCommentsForThis(getAction().getViewName()));

		vatform = new DynamicForm("vatform");
		lowerform = new DynamicForm("lowerform");
		middleform = new DynamicForm("middleform");
		itemIncomeAmoutText.setValue("6+6646546546");
		itemExpenseAmountText.setValue("25465765416");

		vatform.add(warehouseComboSelect, itemComboSelect,
				itemGroupComboSelect, adjustmentQuantity);
		lowerform.add(amountText, memo);
		middleform.add(itemIncomeAmoutTextlabel, itemIncomeAmoutText,
				itemExpenseAmountTextlabel, itemExpenseAmountText);

		dateForm = new DynamicForm("dateForm");
		dateForm.setStyleName("datenumber-panel");
		dateForm.add(adjustDate, entryNo);
		// dateForm.getCellFormatter().setWidth(0, 0, "189");
		StyledPanel datepanel = new StyledPanel("datepanel");
//		datepanel.setWidth("100%");
		datepanel.add(dateForm);
		// datepanel.setCellHorizontalAlignment(dateForm,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		StyledPanel mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(infoLabel);
		mainPanel.add(datepanel);
		// mainPanel.add(topform);
		mainPanel.add(vatform);
		mainPanel.add(middleform);
		mainPanel.add(lowerform);
		// mainPanel.add(memoForm);
		add(mainPanel);

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
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
