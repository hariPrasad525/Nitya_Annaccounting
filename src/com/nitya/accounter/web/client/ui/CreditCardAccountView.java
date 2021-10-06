package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.nitya.accounter.web.client.core.IAccounterCore;
//import com.nitya.accounter.web.client.exception.AccounterException;
//import com.nitya.accounter.web.client.ui.core.AmountField;
//import com.nitya.accounter.web.client.ui.core.IntegerField;
//import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
//import com.nitya.accounter.web.client.ui.forms.DateItem;
//import com.nitya.accounter.web.client.ui.forms.DynamicForm;
//import com.nitya.accounter.web.client.ui.forms.SelectItem;
//import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
//import com.nitya.accounter.web.client.ui.forms.TextItem;
//
//public class CreditCardAccountView extends AbstractBaseView {
//
//	TextItem accTypeText;
//
//	public CreditCardAccountView() {
//		createControls();
//	}
//
//	private void createControls() {
//
//		accTypeText = new TextItem(messages.accountType());
//		// accTypeText.setWidth("*");
//		accTypeText.setDisabled(true);
//		accTypeText.setValue(messages.creditCardOrLineOfCredit());
//
//		TextItem numText = new TextItem(messages.accountNumber());
//		// numText.setWidth("*");
//		TextItem accNameText = new TextItem(messages.accountName());
//		// accNameText.setWidth("*");
//		accNameText.setRequired(true);
//		CheckboxItem activeCheck = new CheckboxItem(messages
//				.active());
//		SelectItem cashFlowSelect = new SelectItem(messages
//				.cashFlowCategory());
//
//		// cashFlowSelect.setWidth("*");
//		cashFlowSelect.setValue(messages.operating());
//		AmountField opBalText = new AmountField(messages
//				.openingBalance(), this, getBaseCurrency());
//		// opBalText.setWidth("*");
//		DateItem asofDate = UIUtils.date(messages.asOf(), this);
//		// asofDate.setWidth("*");
//
//		DynamicForm chartForm = UIUtils.form(messages
//				.chartOfAccountsInformation());
//
//		chartForm.setFields(accTypeText, numText, accNameText, activeCheck,
//				cashFlowSelect, opBalText, asofDate);
//
//		CheckboxItem basisCheck = new CheckboxItem(messages
//				.thisIsConsideredACashAccount());
//
//		DynamicForm basisForm = UIUtils.form(messages
//				.cashBasisAccounting());
//		basisForm.setFields(basisCheck);
//
//		TextAreaItem commentsArea = new TextAreaItem();
//		// commentsArea.setWidth("*");
//		commentsArea.setShowTitle(false);
//		commentsArea.setToolTip(messages.writeCommentsForThis(
//				this.getAction().getViewName()));
//		DynamicForm commentsForm = UIUtils
//				.form(messages.comments());
//		commentsForm.setFields(commentsArea);
//
//		StyledPanel leftVLay = new StyledPanel();
//		leftVLay.setWidth("50%");
//		leftVLay.add(chartForm);
//		leftVLay.add(basisForm);
//		leftVLay.add(commentsForm);
//
//		TextItem bankName = new TextItem(messages.bankName());
//		// bankName.setWidth("*");
//		AmountField limitText = new AmountField(messages.amount(),
//				this, getBaseCurrency());
//		// limitText.setWidth("*");
//		IntegerField cardNumText = new IntegerField(this, messages
//				.cardOrLoadNumber());
//		// cardNumText.setWidth("*");
//
//		DynamicForm creditForm = UIUtils.form(messages
//				.creditCardAccountInformation());
//		// creditForm.setWidth("*");
//		// creditForm.setAutoHeight();
//		creditForm.setFields(bankName, limitText, cardNumText);
//
//		StyledPanel topHLay = new StyledPanel();
//		topHLay.setWidth("100%");
//		topHLay.add(leftVLay);
//		topHLay.add(creditForm);
//
//		Button saveCloseButt = new Button(messages.saveAndClose());
//		// saveCloseButt.setAutoFit(true);
//		// saveCloseButt.setLayoutAlign(Alignment.LEFT);
//
//		Button saveNewButt = new Button(messages.saveAndNew());
//		// saveNewButt.setAutoFit(true);
//		// saveNewButt.setLayoutAlign(Alignment.RIGHT);
//
//		StyledPanel buttHLay = new StyledPanel();
//		// buttHLay.setAlign(Alignment.RIGHT);
//		// buttHLay.setBackgroundColor("#ff00aa");
//		buttHLay.setWidth("100%");
//		// buttHLay.setAutoHeight();
//		// buttHLay.setMembersMargin(20);
//		buttHLay.add(saveCloseButt);
//		buttHLay.add(saveNewButt);
//		StyledPanel mainVLay = new StyledPanel();
//		// mainVLay.setMargin(20);
//		mainVLay.setSize("100%", "100%");
//		mainVLay.add(topHLay);
//		mainVLay.add(buttHLay);
//		// setAutoSize(true);
//		add(mainVLay);
//		setSize("750px", "500px");
//
//		// show();
//	}
//
//	@Override
//	public void init() {
//
//	}
//
//	@Override
//	public void initData() {
//
//	}
//
//	/**
//	 * call this method to set focus in View
//	 */
//	@Override
//	public void setFocus() {
//
//		this.accTypeText.setFocus();
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
//	public void fitToSize(int height, int width) {
//
//	}
//
//	@Override
//	public void onEdit() {
//
//	}
//
//	@Override
//	public void print() {
//
//	}
//
//	@Override
//	public void printPreview() {
//
//	}
//
//	@Override
//	protected String getViewTitle() {
//		return messages.titleToGoHere();
//	}
//
// }
