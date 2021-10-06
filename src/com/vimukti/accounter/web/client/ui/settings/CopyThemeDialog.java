package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class CopyThemeDialog extends BaseDialog {

	private ClientBrandingTheme theme;
	TextBox nameBox;

	public CopyThemeDialog(String title, String desc,
			ClientBrandingTheme brandingTheme) {
		super(title, desc);
		this.getElement().setId("CopyThemeDialog");
		this.theme = brandingTheme;
		createControls();
	}

	private void createControls() {

		StyledPanel copyPanel = new StyledPanel("copyPanel");
		Label yourLabel = new Label(messages.yourTitle());
		nameBox = new TextBox();
		copyPanel.add(yourLabel);
		copyPanel.add(nameBox);

		setBodyLayout(copyPanel);

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String name = nameBox.getValue();
		ClientBrandingTheme brandingThemeByName = company
				.getBrandingThemeByName(nameBox.getText());
		if (name == null || name.isEmpty()) {
			result.addError(this, messages.pleaseenterThemename());
		}

		if (brandingThemeByName != null) {
			result.addError(this, messages.themenamealreadyexist());
		}
		// if (Utility.isObjectExist(Accounter.getCompany().getBrandingTheme(),
		// nameBox.getText())) {
		//
		// }

		return result;
	}

	protected ClientBrandingTheme setValues() {

		ClientBrandingTheme clientBrandingTheme = new ClientBrandingTheme();
		if (theme.isCustomFile()) {// for uploaded custom BrandingTheme
			clientBrandingTheme.setCustomFile(true);
			clientBrandingTheme.setInvoiceTempleteName(theme
					.getInvoiceTempleteName());
			clientBrandingTheme.setCreditNoteTempleteName(theme
					.getCreditNoteTempleteName());
			clientBrandingTheme.setQuoteTemplateName(theme
					.getQuoteTemplateName());
			clientBrandingTheme.setOverDueInvoiceTitle(theme
					.getOverDueInvoiceTitle());
			clientBrandingTheme.setCreditMemoTitle(theme.getCreditMemoTitle());
			clientBrandingTheme.setStatementTitle(theme.getStatementTitle());
			clientBrandingTheme.setQuoteTitle(theme.getQuoteTitle());
			clientBrandingTheme.setPayPalEmailID(theme.getPayPalEmailID());
			clientBrandingTheme.setPurchaseOrderTitle(theme
					.getPurchaseOrderTitle());
			clientBrandingTheme.setPurchaseOrderTemplateName(theme
					.getPurchaseOrderTemplateName());
			clientBrandingTheme.setSalesOrderTitle(theme.getSalesOrderTitle());
			clientBrandingTheme.setSalesOrderTemplateName(theme
					.getSalesOrderTemplateName());
		} else {
			// for regular BrandingTheme
			clientBrandingTheme.setPageSizeType(theme.getPageSizeType());
			clientBrandingTheme.setAddressPadding(theme.getAddressPadding());
			clientBrandingTheme.setBottomMargin(theme.getBottomMargin());
			clientBrandingTheme.setTopMargin(theme.getTopMargin());
			clientBrandingTheme.setMarginsMeasurementType(theme
					.getMarginsMeasurementType());
			clientBrandingTheme.setFont(theme.getFont());
			clientBrandingTheme.setFontSize(theme.getFontSize());
			clientBrandingTheme.setOverDueInvoiceTitle(theme
					.getOverDueInvoiceTitle());
			clientBrandingTheme.setCreditMemoTitle(theme.getCreditMemoTitle());
			clientBrandingTheme.setStatementTitle(theme.getStatementTitle());
			clientBrandingTheme.setQuoteTitle(theme.getQuoteTitle());
			clientBrandingTheme.setContactDetails(theme.getContactDetails());
			clientBrandingTheme.setTerms_And_Payment_Advice(theme
					.getTerms_And_Payment_Advice());
			clientBrandingTheme.setPayPalEmailID(theme.getPayPalEmailID());
			clientBrandingTheme.setShowLogo(theme.isShowLogo());
			clientBrandingTheme.setShowColumnHeadings(theme
					.isShowColumnHeadings());
			clientBrandingTheme.setShowRegisteredAddress(theme
					.isShowRegisteredAddress());
			clientBrandingTheme.setShowTaxColumn(theme.isShowTaxColumn());
			clientBrandingTheme.setShowTaxNumber(theme.isShowTaxNumber());
			clientBrandingTheme.setShowUnitPrice_And_Quantity(theme
					.isShowUnitPrice_And_Quantity());
			clientBrandingTheme.setInvoiceTempleteName(theme
					.getInvoiceTempleteName());
			clientBrandingTheme.setCreditNoteTempleteName(theme
					.getCreditNoteTempleteName());
			clientBrandingTheme.setQuoteTemplateName(theme
					.getQuoteTemplateName());
			clientBrandingTheme.setPurchaseOrderTemplateName(theme
					.getPurchaseOrderTemplateName());
			clientBrandingTheme.setSalesOrderTemplateName(theme
					.getSalesOrderTemplateName());
			clientBrandingTheme.setCustomFile(false);
		}
		return clientBrandingTheme;
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
		super.saveSuccess(object);
		new InvoiceBrandingAction().run(null, true);
	}

	@Override
	protected boolean onOK() {
		ClientBrandingTheme brandingTheme = new ClientBrandingTheme();
		brandingTheme = setValues();
		brandingTheme.setThemeName(nameBox.getText());
		saveOrUpdate(brandingTheme);
		return true;
	}

	@Override
	public void setFocus() {
		nameBox.setFocus(true);

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	public boolean isViewDialog() {
		// TODO Auto-generated method stub
		return false;
	}
}
