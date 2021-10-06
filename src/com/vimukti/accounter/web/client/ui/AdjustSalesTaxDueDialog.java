//package com.vimukti.accounter.web.client.ui;
//
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.vimukti.accounter.web.client.core.ClientAccount;
//import com.vimukti.accounter.web.client.core.IAccounterCore;
//import com.vimukti.accounter.web.client.exception.AccounterException;
//import com.vimukti.accounter.web.client.ui.core.AmountField;
//import com.vimukti.accounter.web.client.ui.core.BaseDialog;
//import com.vimukti.accounter.web.client.ui.core.IntegerField;
//import com.vimukti.accounter.web.client.ui.forms.DateItem;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
//import com.vimukti.accounter.web.client.ui.forms.SelectItem;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//
///**
// * 
// * @author Mandeep Singh
// * 
// */
//
//public class AdjustSalesTaxDueDialog extends BaseDialog<ClientAccount> {
//	public AdjustSalesTaxDueDialog(String title, String description) {
//		super(title, description);
//		createControls();
//		center();
//	}
//
//	private void createControls() {
//		Label lab1 = new Label(messages.adjustSalesTax());
//		// lab1.setWrap(false);
//		// lab1.setAutoFit(true);
//
//		Label lab2 = new Label(messages.selectDate());
//		lab2.setHeight("1px");
//		// lab2.setOverflow(Overflow.VISIBLE);
//		// lab2.setWrap(false);
//		lab2.setWidth("100%");
//
//		DateItem effectDate = UIUtils.date(messages
//				.dateEffective(), null);
//		effectDate.setRequired(true);
//
//		IntegerField entryText = new IntegerField(this, messages
//				.journalEntryNo());
//
//		SelectItem incSelect = new SelectItem(messages.taxIncome());
//		incSelect.setRequired(true);
//
//		SelectItem codeSelect = new SelectItem(messages
//				.taxCodeAdjust());
//		codeSelect.setRequired(true);
//
//		RadioGroupItem incDecRadio = new RadioGroupItem(messages
//				.adjust());
//
//		incDecRadio.setValueMap(messages.increase(), Accounter
//				.messages().decrease());
//
//		AmountField amtText = new AmountField(messages.amount(),
//				this,getBaseCurrency());
//		TextItem memoText = new TextItem();
//		memoText.setTitle(messages.memo());
//
//		final DynamicForm taxForm = new DynamicForm();
//		taxForm.setFields(effectDate, entryText, incSelect, codeSelect,
//				incDecRadio, amtText, memoText);
//
//		StyledPanel mainVLay = new StyledPanel();
//		mainVLay.setSize("100%", "100%");
//		// mainVLay.setTop(30);
//		mainVLay.add(lab1);
//		mainVLay.add(lab2);
//		mainVLay.add(taxForm);
//		// setOverflow(Overflow.VISIBLE);
//		setBodyLayout(mainVLay);
//
//		setWidth("450px");
//		// show();
//	}
//
//	@Override
//	public Object getGridColumnValue(ClientAccount obj, int index) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void deleteFailed(AccounterException caught) {
//
//	}
//
//	@Override
//	public void deleteSuccess(IAccounterCore result) {
//
//	}
//
//	@Override
//	public void saveSuccess(IAccounterCore object) {
//	}
//
//	@Override
//	public void saveFailed(AccounterException exception) {
//
//	}
//
//	@Override
//	protected boolean onOK() {
//		return true;
//	}
//
//	@Override
//	public void setFocus() {
//		// TODO Auto-generated method stub
//
//	}
//
// }
