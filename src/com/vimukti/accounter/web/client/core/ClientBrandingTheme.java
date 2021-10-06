package com.vimukti.accounter.web.client.core;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class ClientBrandingTheme implements IAccounterCore {

	private static final long serialVersionUID = 1L;

	public static final String FONT_ARIAL = "Arial";
	public static final String FONT_CALIBIRI = "Calibiri";
	public static final String FONT_CAMBRIA = "Cambria";
	public static final String FONT_GEORGIA = "Georgia";
	public static final String FONT_MY_RIAD = "Myriad";
	public static final String FONT_TAHOMA = "Tahoma";
	public static final String FONT_TIMES_NEW_ROMAN = "Times New Roman";
	public static final String FONT_TREBUCHET = "Trebuchet";

	public static final int PAGE_SIZE_A4 = 1;
	public static final int PAGE_SIZE_US_LETTER = 2;

	public static final int MARGIN_MEASURES_IN_CM = 1;
	public static final int MARGIN_MEASURES_IN_INCHES = 2;

	public static final int LOGO_ALIGNMENT_LEFT = 1;
	public static final int LOGO_ALIGNMENT_RIGHT = 2;

	// public static final int SHOW_TAXES_AS_EXCLUSIVE = 1;
	// public static final int SHOW_TAXES_AS_INCLUSIVE = 2;

	// selected invoice , credit note & Estimate template names
	private String invoiceTempleteName;
	private String creditNoteTempleteName;
	private String quoteTemplateName;
	private String cashSaleTemplateName;
	private String purchaseOrderTemplateName;
	private String salesOrderTemplateName;

	private long id;
	private String themeName;
	private int pageSizeType;
	private double bottomMargin;
	private double topMargin;
	private int marginsMeasurementType;
	private double addressPadding;
	private String font;
	private String fontSize;
	// private String openInvoiceTitle;
	private String overDueInvoiceTitle;
	private String creditMemoTitle;
	private String statementTitle;
	private String quoteTitle;
	private String cashSaleTitle;
	private String purchaseOrderTitle;
	private String salesOrderTitle;

	private boolean isShowTaxNumber;
	private boolean isShowColumnHeadings;
	private boolean isShowUnitPrice_And_Quantity;
	// private boolean isShowPaymentAdviceCut_Away;
	private boolean isShowTaxColumn;
	private boolean isShowRegisteredAddress;
	private boolean isShowLogo;
	private boolean isDefault;
	private boolean isLogoAdded;

	private boolean isCustomFile;

	private String payPalEmailID;
	private int logoAlignmentType;
	// private int showTaxesAsType;
	private String contactDetails;
	private String Terms_And_Payment_Advice;
	private String fileName;

	private int version;

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return themeName;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.BRANDINGTHEME;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setPageSizeType(int pageSizeType) {
		this.pageSizeType = pageSizeType;
	}

	public int getPageSizeType() {
		return pageSizeType;
	}

	public void setTopMargin(double topMargin) {
		this.topMargin = topMargin;
	}

	public double getTopMargin() {
		return topMargin;
	}

	public void setBottomMargin(double bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public double getBottomMargin() {
		return bottomMargin;
	}

	public void setMarginsMeasurementType(int marginsMeasurementType) {
		this.marginsMeasurementType = marginsMeasurementType;
	}

	public int getMarginsMeasurementType() {
		return marginsMeasurementType;
	}

	public void setAddressPadding(double addressPadding) {
		this.addressPadding = addressPadding;
	}

	public double getAddressPadding() {
		return addressPadding;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getFont() {
		return font;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontSize() {
		return fontSize;
	}

	// public void setOpenInvoiceTitle(String openInvoiceTitle) {
	// this.openInvoiceTitle = openInvoiceTitle;
	// }
	//
	// public String getOpenInvoiceTitle() {
	// return openInvoiceTitle;
	// }

	public void setOverDueInvoiceTitle(String overDueInvoiceTitle) {
		this.overDueInvoiceTitle = overDueInvoiceTitle;
	}

	public String getOverDueInvoiceTitle() {
		return overDueInvoiceTitle;
	}

	public void setCreditMemoTitle(String creditMemoTitle) {
		this.creditMemoTitle = creditMemoTitle;
	}

	public String getCreditMemoTitle() {
		return creditMemoTitle;
	}

	public void setStatementTitle(String statementTitle) {
		this.statementTitle = statementTitle;
	}

	public String getStatementTitle() {
		return statementTitle;
	}

	public void setShowTaxNumber(boolean isShowTaxNumber) {
		this.isShowTaxNumber = isShowTaxNumber;
	}

	public boolean isShowTaxNumber() {
		return isShowTaxNumber;
	}

	public void setShowColumnHeadings(boolean isShowColumnHeadings) {
		this.isShowColumnHeadings = isShowColumnHeadings;
	}

	public boolean isShowColumnHeadings() {
		return isShowColumnHeadings;
	}

	public void setShowUnitPrice_And_Quantity(
			boolean isShowUnitPrice_And_Quantity) {
		this.isShowUnitPrice_And_Quantity = isShowUnitPrice_And_Quantity;
	}

	public boolean isShowUnitPrice_And_Quantity() {
		return isShowUnitPrice_And_Quantity;
	}

	// public void setShowPaymentAdviceCut_Away(boolean
	// isShowPaymentAdviceCut_Away) {
	// this.isShowPaymentAdviceCut_Away = isShowPaymentAdviceCut_Away;
	// }
	//
	// public boolean isShowPaymentAdviceCut_Away() {
	// return isShowPaymentAdviceCut_Away;
	// }

	public void setShowTaxColumn(boolean isShowTaxColumn) {
		this.isShowTaxColumn = isShowTaxColumn;
	}

	public boolean isShowTaxColumn() {
		return isShowTaxColumn;
	}

	public void setShowRegisteredAddress(boolean isShowRegisteredAddress) {
		this.isShowRegisteredAddress = isShowRegisteredAddress;
	}

	public boolean isShowRegisteredAddress() {
		return isShowRegisteredAddress;
	}

	public void setShowLogo(boolean isShowLogo) {
		this.isShowLogo = isShowLogo;
	}

	public boolean isShowLogo() {
		return isShowLogo;
	}

	public void setPayPalEmailID(String payPalEmailID) {
		this.payPalEmailID = payPalEmailID;
	}

	public String getPayPalEmailID() {
		return payPalEmailID;
	}

	public void setLogoAlignmentType(int logoAlignmentType) {
		this.logoAlignmentType = logoAlignmentType;
	}

	public int getLogoAlignmentType() {
		return logoAlignmentType;
	}

	// public void setShowTaxesAsType(int showTaxesAsType) {
	// this.showTaxesAsType = showTaxesAsType;
	// }
	//
	// public int getShowTaxesAsType() {
	// return showTaxesAsType;
	// }

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setTerms_And_Payment_Advice(String terms_And_Payment_Advice) {
		Terms_And_Payment_Advice = terms_And_Payment_Advice;
	}

	public String getTerms_And_Payment_Advice() {
		return Terms_And_Payment_Advice;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setLogoAdded(boolean isLogoAdded) {
		this.isLogoAdded = isLogoAdded;
	}

	public boolean isLogoAdded() {
		return isLogoAdded;
	}

	public ClientBrandingTheme clone() {
		ClientBrandingTheme clientBrandingTheme = (ClientBrandingTheme) this
				.clone();
		return clientBrandingTheme;

	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ClientBrandingTheme) {
			ClientBrandingTheme brandingTheme = (ClientBrandingTheme) obj;
			return this.getID() == brandingTheme.getID() ? true : false;
		}
		return false;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public String getInvoiceTempleteName() {
		return invoiceTempleteName;
	}

	public void setInvoiceTempleteName(String invoiceTempleteName) {
		this.invoiceTempleteName = invoiceTempleteName;
	}

	public String getCreditNoteTempleteName() {
		return creditNoteTempleteName;
	}

	public void setCreditNoteTempleteName(String creditNoteTempleteName) {
		this.creditNoteTempleteName = creditNoteTempleteName;
	}

	public boolean isCustomFile() {
		return isCustomFile;
	}

	public void setCustomFile(boolean isCustomFile) {
		this.isCustomFile = isCustomFile;
	}

	public String getQuoteTitle() {
		return quoteTitle;
	}

	public void setQuoteTitle(String quoteTitle) {
		this.quoteTitle = quoteTitle;
	}

	public String getQuoteTemplateName() {
		return quoteTemplateName;
	}

	public void setQuoteTemplateName(String quoteTemplateName) {
		this.quoteTemplateName = quoteTemplateName;
	}

	public String getCashSaleTemplateName() {
		return cashSaleTemplateName;
	}

	public void setCashSaleTemplateName(String cashSaleTemplateName) {
		this.cashSaleTemplateName = cashSaleTemplateName;
	}

	public String getCashSaleTitle() {
		return cashSaleTitle;
	}

	public void setCashSaleTitle(String cashSaleTitle) {
		this.cashSaleTitle = cashSaleTitle;
	}

	public String getPurchaseOrderTemplateName() {
		return purchaseOrderTemplateName;
	}

	public void setPurchaseOrderTemplateName(String purchaseOrderTemplateName) {
		this.purchaseOrderTemplateName = purchaseOrderTemplateName;
	}

	public String getPurchaseOrderTitle() {
		return purchaseOrderTitle;
	}

	public void setPurchaseOrderTitle(String purchaseOrderTitle) {
		this.purchaseOrderTitle = purchaseOrderTitle;
	}

	public String getSalesOrderTemplateName() {
		return salesOrderTemplateName;
	}

	public void setSalesOrderTemplateName(String salesOrderTemplateName) {
		this.salesOrderTemplateName = salesOrderTemplateName;
	}

	public String getSalesOrderTitle() {
		return salesOrderTitle;
	}

	public void setSalesOrderTitle(String salesOrderTitle) {
		this.salesOrderTitle = salesOrderTitle;
	}

}
