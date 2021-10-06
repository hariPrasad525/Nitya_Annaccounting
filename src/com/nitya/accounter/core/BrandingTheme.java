package com.nitya.accounter.core;

import java.util.Iterator;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class BrandingTheme extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
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

	// selected invoice , credit note , Estimate & Cash sale template names
	private String invoiceTempleteName;
	private String creditNoteTempleteName;
	private String quoteTemplateName;
	private String cashSaleTemplateName;
	private String purchaseOrderTemplateName;
	private String salesOrderTemplateName;

	private String themeName;
	private int pageSizeType;
	private double topMargin, bottomMargin;
	private int marginsMeasurementType;
	private double addressPadding;
	private String font;
	private String fontSize;
	// String openInvoiceTitle;
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
	// boolean isShowPaymentAdviceCut_Away;
	private boolean isShowTaxColumn;
	private boolean isShowRegisteredAddress;
	private boolean isShowLogo;
	private boolean isLogoAdded;

	private boolean isCustomFile;

	String payPalEmailID;
	private int logoAlignmentType;
	// int showTaxesAsType;
	String contactDetails;
	String Terms_And_Payment_Advice;

	String fileName;

	boolean isDefault;

	/**
	 * @return the themeName
	 */
	public String getThemeName() {
		return themeName;
	}

	/**
	 * @param themeName
	 *            the themeName to set
	 */
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	/**
	 * @return the pageSizeType
	 */
	public int getPageSizeType() {
		return pageSizeType;
	}

	/**
	 * @param pageSizeType
	 *            the pageSizeType to set
	 */
	public void setPageSizeType(int pageSizeType) {
		this.pageSizeType = pageSizeType;
	}

	/**
	 * @return the topMargin
	 */
	public double getTopMargin() {
		return topMargin;
	}

	/**
	 * @param topMargin
	 *            the topMargin to set
	 */
	public void setTopMargin(double topMargin) {
		this.topMargin = topMargin;
	}

	/**
	 * @return the bottomMargin
	 */
	public double getBottomMargin() {
		return bottomMargin;
	}

	/**
	 * @param bottomMargin
	 *            the bottomMargin to set
	 */
	public void setBottomMargin(double bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	/**
	 * @return the marginsMeasurementType
	 */
	public int getMarginsMeasurementType() {
		return marginsMeasurementType;
	}

	/**
	 * @param marginsMeasurementType
	 *            the marginsMeasurementType to set
	 */
	public void setMarginsMeasurementType(int marginsMeasurementType) {
		this.marginsMeasurementType = marginsMeasurementType;
	}

	/**
	 * @return the addressPadding
	 */
	public double getAddressPadding() {
		return addressPadding;
	}

	/**
	 * @param addressPadding
	 *            the addressPadding to set
	 */
	public void setAddressPadding(double addressPadding) {
		this.addressPadding = addressPadding;
	}

	/**
	 * @return the font
	 */
	public String getFont() {
		return font;
	}

	/**
	 * @param font
	 *            the font to set
	 */
	public void setFont(String font) {
		this.font = font;
	}

	/**
	 * @return the fontSize
	 */
	public String getFontSize() {
		return fontSize;
	}

	/**
	 * @param fontSize
	 *            the fontSize to set
	 */
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	// /**
	// * @return the openInvoiceTitle
	// */
	// public String getOpenInvoiceTitle() {
	// return openInvoiceTitle;
	// }
	//
	// /**
	// * @param openInvoiceTitle
	// * the openInvoiceTitle to set
	// */
	// public void setOpenInvoiceTitle(String openInvoiceTitle) {
	// this.openInvoiceTitle = openInvoiceTitle;
	// }

	/**
	 * @return the overDueInvoiceTitle
	 */
	public String getOverDueInvoiceTitle() {
		return overDueInvoiceTitle;
	}

	/**
	 * @param overDueInvoiceTitle
	 *            the overDueInvoiceTitle to set
	 */
	public void setOverDueInvoiceTitle(String overDueInvoiceTitle) {
		this.overDueInvoiceTitle = overDueInvoiceTitle;
	}

	/**
	 * @return the creditMemoTitle
	 */
	public String getCreditMemoTitle() {
		return creditMemoTitle;
	}

	/**
	 * @param creditMemoTitle
	 *            the creditMemoTitle to set
	 */
	public void setCreditMemoTitle(String creditMemoTitle) {
		this.creditMemoTitle = creditMemoTitle;
	}

	/**
	 * @return the statementTitle
	 */
	public String getStatementTitle() {
		return statementTitle;
	}

	/**
	 * @param statementTitle
	 *            the statementTitle to set
	 */
	public void setStatementTitle(String statementTitle) {
		this.statementTitle = statementTitle;
	}

	/**
	 * @return the isShowTaxNumber
	 */
	public boolean isShowTaxNumber() {
		return isShowTaxNumber;
	}

	/**
	 * @param isShowTaxNumber
	 *            the isShowTaxNumber to set
	 */
	public void setShowTaxNumber(boolean isShowTaxNumber) {
		this.isShowTaxNumber = isShowTaxNumber;
	}

	/**
	 * @return the isShowColumnHeadings
	 */
	public boolean isShowColumnHeadings() {
		return isShowColumnHeadings;
	}

	/**
	 * @param isShowColumnHeadings
	 *            the isShowColumnHeadings to set
	 */
	public void setShowColumnHeadings(boolean isShowColumnHeadings) {
		this.isShowColumnHeadings = isShowColumnHeadings;
	}

	/**
	 * @return the isShowUnitPrice_And_Quantity
	 */
	public boolean isShowUnitPrice_And_Quantity() {
		return isShowUnitPrice_And_Quantity;
	}

	/**
	 * @param isShowUnitPriceAndQuantity
	 *            the isShowUnitPrice_And_Quantity to set
	 */
	public void setShowUnitPrice_And_Quantity(boolean isShowUnitPriceAndQuantity) {
		isShowUnitPrice_And_Quantity = isShowUnitPriceAndQuantity;
	}

	// /**
	// * @return the isShowPaymentAdviceCut_Away
	// */
	// public boolean isShowPaymentAdviceCut_Away() {
	// return isShowPaymentAdviceCut_Away;
	// }
	//
	// /**
	// * @param isShowPaymentAdviceCutAway
	// * the isShowPaymentAdviceCut_Away to set
	// */
	// public void setShowPaymentAdviceCut_Away(boolean
	// isShowPaymentAdviceCutAway) {
	// isShowPaymentAdviceCut_Away = isShowPaymentAdviceCutAway;
	// }

	/**
	 * @return the isShowTaxColumn
	 */
	public boolean isShowTaxColumn() {
		return isShowTaxColumn;
	}

	/**
	 * @param isShowTaxColumn
	 *            the isShowTaxColumn to set
	 */
	public void setShowTaxColumn(boolean isShowTaxColumn) {
		this.isShowTaxColumn = isShowTaxColumn;
	}

	/**
	 * @return the isShowRegisteredAddress
	 */
	public boolean isShowRegisteredAddress() {
		return isShowRegisteredAddress;
	}

	/**
	 * @param isShowRegisteredAddress
	 *            the isShowRegisteredAddress to set
	 */
	public void setShowRegisteredAddress(boolean isShowRegisteredAddress) {
		this.isShowRegisteredAddress = isShowRegisteredAddress;
	}

	/**
	 * @return the isShowLogo
	 */
	public boolean isShowLogo() {
		return isShowLogo;
	}

	/**
	 * @param isShowLogo
	 *            the isShowLogo to set
	 */
	public void setShowLogo(boolean isShowLogo) {
		this.isShowLogo = isShowLogo;
	}

	/**
	 * @return the payPalEmailID
	 */
	public String getPayPalEmailID() {
		return payPalEmailID;
	}

	/**
	 * @param payPalEmailID
	 *            the payPalEmailID to set
	 */
	public void setPayPalEmailID(String payPalEmailID) {
		this.payPalEmailID = payPalEmailID;
	}

	/**
	 * @return the logoAlignmentType
	 */
	public int getLogoAlignmentType() {
		return logoAlignmentType;
	}

	/**
	 * @param logoAlignmentType
	 *            the logoAlignmentType to set
	 */
	public void setLogoAlignmentType(int logoAlignmentType) {
		this.logoAlignmentType = logoAlignmentType;
	}

	// /**
	// * @return the showTaxesAsType
	// */
	// public int getShowTaxesAsType() {
	// return showTaxesAsType;
	// }
	//
	// /**
	// * @param showTaxesAsType
	// * the showTaxesAsType to set
	// */
	// public void setShowTaxesAsType(int showTaxesAsType) {
	// this.showTaxesAsType = showTaxesAsType;
	// }

	/**
	 * @return the contactDetails
	 */
	public String getContactDetails() {
		return contactDetails;
	}

	/**
	 * @param contactDetails
	 *            the contactDetails to set
	 */
	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	/**
	 * @return the terms_And_Payment_Advice
	 */
	public String getTerms_And_Payment_Advice() {
		return Terms_And_Payment_Advice;
	}

	/**
	 * @param termsAndPaymentAdvice
	 *            the terms_And_Payment_Advice to set
	 */
	public void setTerms_And_Payment_Advice(String termsAndPaymentAdvice) {
		Terms_And_Payment_Advice = termsAndPaymentAdvice;
	}

	public BrandingTheme() {

	}

	public BrandingTheme(
			String themeName,
			String id,
			double topMargin,
			double bottomMargin,
			double addressPadding,
			String font,
			String fontSize,
			// String openInvoiceTitle,
			String overDueInvoiceTitle, String creditMemoTitle,
			String statementTitle, String quoteTitle,
			String purchaseOrderTitle, String salesOrderTitle,
			String payPalEmailID, boolean isDefault, String contactDetails,
			String Terms_And_Payment_Advice, String invoiceTemplete,
			String creditNoteTemplete, String quoteTemplateName,
			String cashSaleTempleteName, String cashSaleTitle,
			String purchaseOrderTempleteName, String salesOrderTempleteName) {

		this.themeName = themeName;
		this.pageSizeType = PAGE_SIZE_US_LETTER;
		this.topMargin = topMargin;
		this.bottomMargin = bottomMargin;
		this.marginsMeasurementType = MARGIN_MEASURES_IN_CM;
		this.addressPadding = addressPadding;
		this.font = font;
		this.fontSize = fontSize;
		// this.openInvoiceTitle = openInvoiceTitle;
		this.overDueInvoiceTitle = overDueInvoiceTitle;
		this.creditMemoTitle = creditMemoTitle;
		this.statementTitle = statementTitle;
		this.quoteTitle = quoteTitle;
		this.purchaseOrderTitle = purchaseOrderTitle;
		this.salesOrderTitle = salesOrderTitle;
		this.isShowTaxNumber = true;
		this.isShowColumnHeadings = true;
		this.isShowUnitPrice_And_Quantity = true;
		// this.isShowPaymentAdviceCut_Away = true;
		this.isShowTaxColumn = true;
		this.isShowRegisteredAddress = true;
		this.isShowLogo = true;
		this.payPalEmailID = payPalEmailID;
		this.logoAlignmentType = LOGO_ALIGNMENT_RIGHT;
		// this.showTaxesAsType = SHOW_TAXES_AS_EXCLUSIVE;
		this.isDefault = isDefault;
		this.contactDetails = contactDetails;
		this.Terms_And_Payment_Advice = Terms_And_Payment_Advice;
		this.invoiceTempleteName = invoiceTemplete;
		this.creditNoteTempleteName = creditNoteTemplete;
		this.quoteTemplateName = quoteTemplateName;
		this.cashSaleTemplateName = cashSaleTempleteName;
		this.cashSaleTitle = cashSaleTitle;
		this.purchaseOrderTemplateName = purchaseOrderTempleteName;
		this.salesOrderTemplateName = salesOrderTempleteName;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		if (!UserUtils.canDoThis(BrandingTheme.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		Query query = session
				.getNamedQuery("getBrandingTheme")
				.setParameter("companyId",
						((BrandingTheme) clientObject).getCompany().getID())
				.setParameter("themeName", this.themeName,
						EncryptedStringType.INSTANCE)
				.setLong("id", this.getID());
		List list = query.list();

		if (list != null || list.size() > 0 || list.get(0) != null) {
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {

				String object = (String) iterator.next();
				if (this.getThemeName().equals(object)) {
					throw new AccounterException(
							AccounterException.ERROR_NAME_CONFLICT);
					// "Branding Theme already exist with this Name");
				}
			}
		}

		return true;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.BRANDINGTHEME);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {

		if (isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		isOnSaveProccessed = true;
		cashSaleTemplateName = "Classic Template";
		cashSaleTitle = "CASH SALE";

		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		super.onUpdate(arg0);
		if (isLogoAdded() == false)
			this.setFileName(null);
		return false;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
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

	public String getInvoiceTempleteName() {
		return invoiceTempleteName;
	}

	public void setInvoiceTempleteName(String invoiceTempleteName) {
		this.invoiceTempleteName = invoiceTempleteName;
	}

	public String getCreditNoteTempleteName() {
		return creditNoteTempleteName;
	}

	public void setCreditNoteTempleteName(String creditNoteTempleteId) {
		this.creditNoteTempleteName = creditNoteTempleteId;
	}

	@Override
	public String getName() {
		return themeName;
	}

	@Override
	public void setName(String name) {
		this.themeName = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.BRANDING_THEME;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.brandingTheme()).gap();

		w.put(messages.name(), this.themeName);

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

	@Override
	public void selfValidate() throws AccounterException {
		if (themeName == null || themeName.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().brandingTheme());
		}
	}

}
