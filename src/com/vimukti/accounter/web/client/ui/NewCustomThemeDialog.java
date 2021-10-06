package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.settings.InvoiceBrandingAction;

public class NewCustomThemeDialog extends BaseDialog<ClientBrandingTheme> {

	private DynamicForm form;
	private TextItem themeName, overdueBox, creditNoteBox, statementBox,
			quoteBox, cashSaleBox, purchaseOrderBox, salesOrderBox,
			payPalEmail;
	private ClientBrandingTheme brandingTheme;
	private boolean isEdit;
	private boolean isUsersActivityList;

	public NewCustomThemeDialog(String title,
			ClientBrandingTheme brandingTheme, boolean isNew) {
		super(title, "");
		this.getElement().setId("NewCustomThemeDialog");
		this.brandingTheme = brandingTheme;
		this.isEdit = isNew;
		createControls();
	}

	private void createControls() {
		form = new DynamicForm("formItems");
		themeName = new TextItem("Custom Theme", "themeName");
		themeName.setRequired(true);

		if (isEdit == true) {

			themeName.setValue(brandingTheme.getThemeName().trim());

			overdueBox = new TextItem(messages.overdueInvoiceTitle(),
					"overdueBox");
			overdueBox.setValue(brandingTheme.getOverDueInvoiceTitle());

			creditNoteBox = new TextItem(messages.creditNoteTitle(),
					"creditNoteBox");
			creditNoteBox.setValue(brandingTheme.getCreditMemoTitle());

			statementBox = new TextItem(messages.statementTitle(),
					"statementBox");
			statementBox.setValue(brandingTheme.getStatementTitle());

			quoteBox = new TextItem(messages.quoteTitle(), "quoteBox");
			quoteBox.setValue(brandingTheme.getQuoteTitle());

			cashSaleBox = new TextItem(messages.cashSaleTitle(), "cashSaleBox");
			cashSaleBox.setValue(brandingTheme.getCashSaleTitle());

			purchaseOrderBox = new TextItem(messages.purchaseOrderTitle(),
					"purchaseOrderBox");
			purchaseOrderBox.setValue(brandingTheme.getPurchaseOrderTitle());

			salesOrderBox = new TextItem(messages.salesOrderTitle(),
					"salesOrderBox");
			salesOrderBox.setValue(brandingTheme.getSalesOrderTitle());

			payPalEmail = new TextItem("PayPal Email", "payPalEmail");
			String emailId = brandingTheme.getPayPalEmailID() != null ? brandingTheme
					.getPayPalEmailID().trim() : "";
			payPalEmail.setValue(emailId);

			form.add(themeName, overdueBox, creditNoteBox, statementBox,
					quoteBox, cashSaleBox, purchaseOrderBox, salesOrderBox,
					payPalEmail);
		} else {
			form.add(themeName);
		}

		StyledPanel layout = new StyledPanel("layout");
		layout.add(form);
		setBodyLayout(layout);
	}

	@Override
	protected boolean onOK() {
		if (brandingTheme == null) {
			brandingTheme = new ClientBrandingTheme();
			brandingTheme.setInvoiceTempleteName(messages.classicTemplate());
			brandingTheme.setCreditNoteTempleteName(messages.classicTemplate());
			brandingTheme.setQuoteTemplateName(messages.classicTemplate());
			brandingTheme.setCashSaleTemplateName(messages.classicTemplate());
			brandingTheme.setPurchaseOrderTemplateName(messages
					.classicTemplate());
			brandingTheme.setSalesOrderTemplateName(messages.classicTemplate());

			brandingTheme.setOverDueInvoiceTitle(messages.overdueValue());
			brandingTheme.setCreditMemoTitle(messages.creditNoteValue());
			brandingTheme.setQuoteTitle(messages.QuoteOverDueTitle());
			brandingTheme.setCashSaleTitle(messages.cashSaleValue());
			brandingTheme.setPurchaseOrderTitle(messages.purchaseOrderValue());
			brandingTheme.setSalesOrderTitle(messages.salesOrderValue());
			brandingTheme.setStatementTitle(messages.statement());

			brandingTheme.setPayPalEmailID("");

		}

		if (isEdit == true) {
			brandingTheme.setOverDueInvoiceTitle(this.overdueBox.getValue()
					.toString());
			brandingTheme.setCreditMemoTitle(this.creditNoteBox.getValue()
					.toString());
			brandingTheme.setStatementTitle(this.statementBox.getValue()
					.toString());
			brandingTheme.setQuoteTitle(this.quoteBox.getValue().toString());
			brandingTheme.setCashSaleTitle(this.cashSaleBox.getValue()
					.toString());
			brandingTheme.setPurchaseOrderTitle(this.purchaseOrderBox
					.getValue().toString());
			brandingTheme.setSalesOrderTitle(this.salesOrderBox.getValue()
					.toString());
			brandingTheme.setPayPalEmailID(this.payPalEmail.getValue()
					.toString());
		}

		brandingTheme.setThemeName(this.themeName.getValue().toString());
		brandingTheme.setCustomFile(true);
		saveOrUpdate(brandingTheme);

		return true;

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
		super.saveSuccess(object);
		if (!isUsersActivityList()) {
			new InvoiceBrandingAction().run(null, true);
		} else {
			if (getCallback() != null) {
				getCallback().actionResult((ClientBrandingTheme) object);
			}
		}
	}

	@Override
	protected ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		String name = themeName.getValue().toString();
		if (name.trim().length() == 0)
			result.addError(this, messages.pleaseEnterValidLocationName(""));

		return result;
	}

	public String getLocationGroupName() {
		return this.themeName.getValue().toString();
	}

	@Override
	public void setFocus() {
		themeName.setFocus();

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	public boolean isUsersActivityList() {
		return isUsersActivityList;
	}

	public void setUsersActivityList(boolean isUsersActivityList) {
		this.isUsersActivityList = isUsersActivityList;
	}
}
