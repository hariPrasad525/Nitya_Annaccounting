package com.nitya.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.ClientBrandingTheme;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.images.FinanceImages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.FileUploadDilaog;
import com.nitya.accounter.web.client.ui.GwtTabPanel;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.combo.TemplateCombo;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Uday Kumar
 * 
 */
@SuppressWarnings({ "deprecation" })
public class NewBrandingThemeView extends BaseView<ClientBrandingTheme> {

	private Label pageSizeLabel, logoLabel, termsLabel;
	private GwtTabPanel tabSet;
	private RadioButton a4Button, usLetterButton, leftRadioButton,
			rightRadioButton, cmButton, inchButton;
	private StyledPanel checkBoxPanel, radioButtonPanel,
			check_radio_textAreaPanel, button_textBoxPanel;
	private StyledPanel mainLayoutPanel, check_radioPanel, hPanel;
	private CheckboxItem taxNumItem, headingItem, unitPriceItem,// paymentItem,
			columnItem, addressItem, logoItem;
	private TextItem overdueBox, creditNoteBox, statementBox, paypalTextBox,
			logoNameBox, quoteBox, cashSaleBox, purchaseOrderBox,
			salesOrderBox;
	private AmountField topMarginBox, bottomMarginBox, addressPadBox;
	private TextItem nameItem;
	private String[] fontNameArray, fontSizeArray;
	private SelectCombo fontNameBox, fontSizeBox;
	private HTML paypalEmailHtml, contactDetailHtml;
	private TextArea contactDetailsArea, termsPaymentArea;
	private Label measureLabel;
	private FlexTable textBoxTable;
	private List<String> listOfFontNames, listOfFontSizes;
	private DynamicForm nameForm;
	private ArrayList<String> templatesList;

	private TemplateCombo invoiceCombo, creditMemoCombo, quoteCombo;
	private final FinanceImages financeImages = Accounter.getFinanceImages();
	private Image invoiceTempImage, creditTempImage, quoteTempImage;
	private String invVal, creditVal, quoteVal;

	public NewBrandingThemeView(String title, String desc) {
	}

	public NewBrandingThemeView(String title, String desc,
			ClientBrandingTheme brandingTheme) {
		super();
		setData(brandingTheme);
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("NewBrandingThemeView");
		createControls();
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientBrandingTheme());
		} else {
			initThemeData(getData());
		}
		/*
		 * else { brandingTheme = new ClientBrandingTheme();
		 * setBrandingTheme(brandingTheme); }
		 */
		super.initData();

	}

	private void initThemeData(ClientBrandingTheme brandingTheme) {

		nameItem.setValue(brandingTheme.getThemeName());
		topMarginBox.setAmount(brandingTheme.getTopMargin());
		bottomMarginBox.setAmount(brandingTheme.getBottomMargin());
		addressPadBox.setAmount(brandingTheme.getAddressPadding());
		setPazeSize(brandingTheme.getPageSizeType());
		fontNameBox.setComboItem(brandingTheme.getFont());
		fontSizeBox.setComboItem(brandingTheme.getFontSize());
		setLogoType(brandingTheme.getLogoAlignmentType());
		setMeasurementType(brandingTheme.getMarginsMeasurementType());
		creditNoteBox.setValue(brandingTheme.getCreditMemoTitle());
		overdueBox.setValue(brandingTheme.getOverDueInvoiceTitle());
		statementBox.setValue(brandingTheme.getStatementTitle());
		quoteBox.setValue(brandingTheme.getQuoteTitle());
		cashSaleBox.setValue(brandingTheme.getCashSaleTitle());
		purchaseOrderBox.setValue(brandingTheme.getPurchaseOrderTitle());
		salesOrderBox.setValue(brandingTheme.getSalesOrderTitle());
		taxNumItem.setValue(brandingTheme.isShowTaxNumber());
		headingItem.setValue(brandingTheme.isShowColumnHeadings());
		unitPriceItem.setValue(brandingTheme.isShowUnitPrice_And_Quantity());
		columnItem.setValue(brandingTheme.isShowTaxColumn());
		addressItem.setValue(brandingTheme.isShowRegisteredAddress());
		logoItem.setValue(brandingTheme.isShowLogo());
		paypalTextBox.setValue(brandingTheme.getPayPalEmailID());
		termsPaymentArea.setValue(brandingTheme.getTerms_And_Payment_Advice());
		contactDetailsArea.setValue(brandingTheme.getContactDetails());
		logoNameBox.setValue(brandingTheme.getFileName());
		invoiceCombo.setValue(brandingTheme.getInvoiceTempleteName());
		creditMemoCombo.setValue(brandingTheme.getCreditNoteTempleteName());
		quoteCombo.setValue(brandingTheme.getQuoteTemplateName());

	}

	private void createControls() {
		tabSet = (GwtTabPanel) GWT.create(GwtTabPanel.class);
		tabSet.add(getGeneralLayout(), messages.general());
		tabSet.add(getTemplateLayout(), messages.templates());
		tabSet.selectTab(0);
		// tabSet.setSize("100%", "100%");

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(tabSet.getPanel());
		this.add(mainVLay);
	}

	private StyledPanel getGeneralLayout() {

		StyledPanel panel = new StyledPanel("panel");
		HTML titleHtml = new HTML(messages.brandingTheme());
		titleHtml.setStyleName("label-title");

		check_radioPanel = new StyledPanel("check_radioPanel");
		check_radio_textAreaPanel = new StyledPanel("check_radio_textAreaPanel");
		button_textBoxPanel = new StyledPanel("button_textBoxPanel");

		check_radioPanel.add(addCheckBoxTableControls());

		check_radioPanel.add(addRadioBoxTableControls());

		check_radio_textAreaPanel.add(check_radioPanel);
		termsLabel = new Label(messages.termsLabel());
		termsPaymentArea = new TextArea();
		termsPaymentArea.setStyleName("terms-payment-area");
		check_radio_textAreaPanel.add(termsLabel);
		check_radio_textAreaPanel.add(termsPaymentArea);

		// addInputDialogHandler(new InputDialogHandler() {
		//
		// @Override
		// public boolean onOkClick() {
		// try {
		//
		// } catch (Exception e) {
		// System.err.println(e.toString());
		// }
		// return false;
		//
		// }
		//
		// @Override
		// public void onCancelClick() {
		// removeFromParent();
		// HistoryTokenUtils.setPresentToken(MainFinanceWindow
		// .getViewManager().getCurrentView().getAction(),
		// MainFinanceWindow.getViewManager().getCurrentView()
		// .getData());
		// }
		//
		// });
		mainLayoutPanel = new StyledPanel("mainLayoutPanel");

		mainLayoutPanel.add(addTextBoxTableControl());
		mainLayoutPanel.add(check_radio_textAreaPanel);
		panel.add(titleHtml);

		panel.add(mainLayoutPanel);
		button_textBoxPanel.add(panel);

		// this.add(button_textBoxPanel);

		topMarginBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!UIUtils.isDouble(topMarginBox.getValue().toString())) {
					// ccounter.showError(messages.numberForTopMarginField());
					addError(topMarginBox, messages.errorForTopMarginField());
					topMarginBox.setValue("");
				} else {
					clearError(topMarginBox);
				}
			}
		});
		bottomMarginBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!UIUtils.isDouble(bottomMarginBox.getValue().toString())) {
					// Accounter.showError(messages.numberForbottomMarginField());
					addError(bottomMarginBox,
							messages.errorForbottomMarginField());
					bottomMarginBox.setValue("");
				} else {
					clearError(bottomMarginBox);
				}
			}
		});
		addressPadBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!UIUtils.isDouble(addressPadBox.getValue().toString())) {
					// Accounter.showError(messages.numberForAddresspadField());
					addError(addressPadBox, messages.errorForaddresspadField());
					addressPadBox.setValue("");
				} else {
					clearError(addressPadBox);
				}
			}
		});

		return button_textBoxPanel;
	}

	private int getPageSize() {
		if (a4Button.isChecked()) {
			return 1;
		} else {
			return 2;
		}
	}

	private void setMeasurementType(int i) {
		if (i == 1) {
			cmButton.setChecked(true);
		} else {
			inchButton.setChecked(true);
		}
	}

	private int getMeasurementType() {
		if (cmButton.getValue())
			return 1;
		else
			return 2;
	}

	private void setPazeSize(int i) {
		if (i == 1) {
			a4Button.setChecked(true);
		} else {
			usLetterButton.setChecked(true);
		}
	}

	private ClientBrandingTheme getBrandingThemeObject() {

		data.setThemeName(String.valueOf(nameItem.getValue()));
		data.setPageSizeType(getPageSize());

		data.setTopMargin(topMarginBox.getAmount());
		data.setBottomMargin(bottomMarginBox.getAmount());
		data.setAddressPadding(addressPadBox.getAmount());
		data.setFont(fontNameBox.getSelectedValue());
		data.setFontSize(fontSizeBox.getSelectedValue());

		data.setCreditMemoTitle(String.valueOf(creditNoteBox.getValue()));
		data.setOverDueInvoiceTitle(String.valueOf(overdueBox.getValue()));
		data.setStatementTitle(String.valueOf(statementBox.getValue()));
		data.setQuoteTitle(String.valueOf(quoteBox.getValue()));
		data.setCashSaleTitle(String.valueOf(cashSaleBox.getValue()));
		data.setPurchaseOrderTitle(String.valueOf(purchaseOrderBox.getValue()));
		data.setSalesOrderTitle(salesOrderBox.getValue());
		data.setShowTaxNumber(taxNumItem.isChecked());
		data.setShowColumnHeadings(headingItem.isChecked());
		data.setShowUnitPrice_And_Quantity(unitPriceItem.isChecked());
		data.setShowTaxColumn(columnItem.isChecked());
		data.setShowRegisteredAddress(addressItem.isChecked());
		data.setShowLogo(logoItem.isChecked());
		data.setMarginsMeasurementType(getMeasurementType());
		data.setPayPalEmailID(String.valueOf(paypalTextBox.getValue()));
		data.setTerms_And_Payment_Advice(String.valueOf(termsPaymentArea
				.getValue()));
		data.setContactDetails(String.valueOf(contactDetailsArea.getValue()));
		data.setLogoAlignmentType(getLogoType());

		// for setting the selected templetes
		String invoice = invoiceCombo.getValue().toString().isEmpty() ? messages
				.classicTemplate() : invoiceCombo.getValue().toString();
		String creditNote = creditMemoCombo.getValue().toString().isEmpty() ? messages
				.classicTemplate() : creditMemoCombo.getValue().toString();
		String quote = quoteCombo.getValue().toString().isEmpty() ? messages
				.classicTemplate() : quoteCombo.getValue().toString();

		data.setInvoiceTempleteName(invoice);
		data.setCreditNoteTempleteName(creditNote);
		data.setQuoteTemplateName(quote);

		if (logoNameBox.getValue().toString().isEmpty()) {
			data.setFileName(null);
			data.setLogoAdded(false);
		} else {
			data.setFileName(String.valueOf(logoNameBox.getValue().toString()));
			data.setLogoAdded(true);
		}

		data.setPurchaseOrderTemplateName("Classic Template");
		data.setSalesOrderTemplateName("Classic Template");
		return data;
	}

	private int getLogoType() {
		if (leftRadioButton.isChecked()) {
			return 1;
		} else {
			return 2;
		}
	}

	private void setLogoType(int i) {
		if (i == 1) {
			leftRadioButton.setChecked(true);
		} else {
			rightRadioButton.setChecked(true);
		}
	}

	private StyledPanel addRadioBoxTableControls() {
		radioButtonPanel = new StyledPanel("radioButtonPanel");

		measureLabel = new Label(messages.measure());
		logoLabel = new Label(messages.logoAlignment());
		leftRadioButton = new RadioButton(messages.logoType(), messages.left());
		rightRadioButton = new RadioButton(messages.logoType(),
				messages.right());
		leftRadioButton.setChecked(true);
		// taxesLabel = new Label(messages.showTaxesAs());
		// exclusiveButton = new RadioButton(messages.taxType(), messages
		// .exclusive());
		// inclusiveButton = new RadioButton(messages.taxType(), messages
		// .inclusive());
		// inclusiveButton.setChecked(true);

		contactDetailHtml = new HTML(messages.contactDetailsHtml());
		contactDetailsArea = new TextArea();
		contactDetailsArea.setStyleName("contact-deatils-area");

		radioButtonPanel.add(logoLabel);
		radioButtonPanel.add(leftRadioButton);
		radioButtonPanel.add(rightRadioButton);
		// radioButtonPanel.add(taxesLabel);
		// radioButtonPanel.add(exclusiveButton);
		// radioButtonPanel.add(inclusiveButton);
		radioButtonPanel.add(contactDetailHtml);
		radioButtonPanel.add(contactDetailsArea);

		return radioButtonPanel;
	}

	private StyledPanel getTemplateLayout() {

		hPanel = new StyledPanel("hPanel");

		StyledPanel vPanel1 = new StyledPanel("vPanel1");
		StyledPanel vPanel2 = new StyledPanel("vPanel2");
		StyledPanel vPanel3 = new StyledPanel("vPanel3");

		DynamicForm invForm = UIUtils.form(messages.type());
		invoiceCombo = new TemplateCombo(messages.invoiceTemplete(),
				"Invoice.html");
		templatesList = invoiceCombo.getTempletes();
		invForm.add(invoiceCombo);

		// setting the tempalte invoice image
		invoiceTempImage = new Image();
		changeTemplateImage(templatesList.get(0), "invoice");

		vPanel1.add(invForm);
		vPanel1.add(invoiceTempImage);

		invVal = templatesList.get(0);
		invoiceCombo.setComboItem(templatesList.get(0));
		invoiceCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						invoiceCombo.setComboItem(selectItem);
						invVal = selectItem;
						changeTemplateImage(selectItem, "invoice");

					}

				});

		// for displaying the credit templetes combo boxes
		DynamicForm crediteForm = UIUtils.form(messages.type());
		creditMemoCombo = new TemplateCombo(messages.creditNoteTemplete(),
				"Credit.html");
		templatesList = creditMemoCombo.getTempletes();
		creditMemoCombo.setComboItem(templatesList.get(0));
		creditVal = templatesList.get(0);
		crediteForm.add(creditMemoCombo);

		// setting the credit note template image
		creditTempImage = new Image();
		changeTemplateImage(templatesList.get(0), "credit");

		vPanel2.add(crediteForm);
		vPanel2.add(creditTempImage);

		creditMemoCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						creditMemoCombo.setComboItem(selectItem);
						creditVal = selectItem;
						changeTemplateImage(selectItem, "credit");
					}

				});

		// for Estimate template selection
		DynamicForm estimateForm = UIUtils.form(messages.type());
		quoteCombo = new TemplateCombo(messages.quoteTemplate(), "quote.html");
		templatesList = quoteCombo.getTempletes();
		estimateForm.add(quoteCombo);

		// setting the template invoice image
		quoteTempImage = new Image();
		changeTemplateImage(templatesList.get(0), "quote");

		vPanel3.add(estimateForm);
		vPanel3.add(quoteTempImage);

		quoteVal = templatesList.get(0);
		quoteCombo.setComboItem(templatesList.get(0));
		quoteCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						quoteCombo.setComboItem(selectItem);
						quoteVal = selectItem;
						changeTemplateImage(selectItem, "quote");

					}

				});

		hPanel.add(vPanel1);
		hPanel.add(vPanel2);
		hPanel.add(vPanel3);
		return hPanel;
	}

	private StyledPanel addCheckBoxTableControls() {

		checkBoxPanel = new StyledPanel("checkBoxPanel");

		taxNumItem = new CheckboxItem(messages.showTaxNumber(), "taxNumItem");
		taxNumItem.setValue(true);

		headingItem = new CheckboxItem(messages.showColumnHeadings(),
				"headingItem");
		headingItem.setValue(true);
		unitPriceItem = new CheckboxItem(messages.showUnitPrice(),
				"unitPriceItem");
		unitPriceItem.setValue(true);
		// paymentItem = new CheckBox(messages
		// .showPaymentAdvice());
		// paymentItem.setChecked(true);
		columnItem = new CheckboxItem(messages.showTaxColumn(), "columnItem");
		columnItem.setValue(true);

		addressItem = new CheckboxItem(messages.showRegisteredAddress(),
				"addressItem");
		addressItem.setValue(true);
		logoItem = new CheckboxItem(messages.showLogo(), "logoItem");
		logoItem.setValue(true);
		paypalEmailHtml = new HTML(messages.paypalEmailHtml());
		paypalTextBox = new TextItem(messages.paypalEmail(), "paypalTextBox");

		checkBoxPanel.add(taxNumItem);
		checkBoxPanel.add(headingItem);
		checkBoxPanel.add(unitPriceItem);
		// checkBoxPanel.add(paymentItem);
		checkBoxPanel.add(columnItem);
		checkBoxPanel.add(addressItem);
		checkBoxPanel.add(logoItem);
		checkBoxPanel.add(paypalEmailHtml);

		DynamicForm paypalForm = new DynamicForm("paypalForm");
		checkBoxPanel.add(paypalForm);

		checkBoxPanel.setStyleName("rightBorder");
		return checkBoxPanel;

	}

	private StyledPanel addTextBoxTableControl() {

		nameItem = new TextItem(messages.name(), "name-item");
		pageSizeLabel = new Label(messages.pageSize());
		topMarginBox = new AmountField(messages.topMargin(), this);
		topMarginBox.setAmount(1.35);
		bottomMarginBox = new AmountField(messages.bottomMargin(), this);
		bottomMarginBox.setAmount(1.00);
		addressPadBox = new AmountField(messages.addressPadding(), this);
		addressPadBox.setAmount(1.00);
		// draftBox = new TextBox();
		// draftBox.setText(messages
		// .draftBoxValue());
		// approvedBox = new TextBox();
		// approvedBox.setText(messages
		// .approvedValue());
		overdueBox = new TextItem(messages.overdueInvoiceTitle(), "overdueBox");
		overdueBox.setValue(messages.overdueValue());
		creditNoteBox = new TextItem(messages.creditNoteTitle(),
				"creditNoteBox");
		creditNoteBox.setValue(messages.creditNoteValue());
		statementBox = new TextItem(messages.statementTitle(), "statementBox");
		statementBox.setValue(messages.statement());
		quoteBox = new TextItem(messages.quoteTitle(), "quoteBox");
		quoteBox.setValue(messages.QuoteOverDueTitle());
		cashSaleBox = new TextItem(messages.cashSaleTitle(), "cashSaleBox");
		cashSaleBox.setValue(messages.cashSaleValue());
		purchaseOrderBox = new TextItem(messages.purchaseOrderTitle(),
				"purchaseOrderBox");
		purchaseOrderBox.setValue(messages.purchaseOrderValue());
		salesOrderBox = new TextItem(messages.salesOrderTitle(),
				"salesOrderBox");
		salesOrderBox.setValue(messages.salesOrderValue());
		a4Button = new RadioButton(messages.pageType(), "A4");
		usLetterButton = new RadioButton(messages.pageType(),
				messages.usLetter());
		a4Button.setChecked(true);

		cmButton = new RadioButton(messages.measureType(), messages.cm());
		inchButton = new RadioButton(messages.measureType(), messages.inch());
		cmButton.setChecked(true);

		fontNameArray = new String[] { "Arial", "Calibri", "Cambria",
				"Georgia", "Myriad", "Tahoma", "Times New Roman", "Trebuchet",
				"Code2000" };
		fontSizeArray = new String[] { messages.pointnumber(8),
				messages.pointnumber(9), messages.pointnumber(10),
				messages.pointnumber(11),
		// messages.point13(),
		// messages.point14(),
		// messages.point15()
		};

		fontNameBox = new SelectCombo(messages.font());
		// fontNameBox.setWidth(100);
		listOfFontNames = new ArrayList<String>();
		for (int i = 0; i < fontNameArray.length; i++) {
			listOfFontNames.add(fontNameArray[i]);
		}
		fontNameBox.initCombo(listOfFontNames);
		fontNameBox.setComboItem(fontNameArray[0]);
		fontNameBox
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem != null)
							fontNameBox.setComboItem(selectItem);

					}
				});

		fontSizeBox = new SelectCombo(messages.fontSize());
		// fontSizeBox.setWidth(100);
		listOfFontSizes = new ArrayList<String>();
		for (int i = 0; i < fontSizeArray.length; i++) {
			listOfFontSizes.add(fontSizeArray[i]);
		}
		fontSizeBox.initCombo(listOfFontSizes);
		fontSizeBox.setComboItem(fontSizeArray[0]);
		fontSizeBox
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem != null)
							fontSizeBox.setComboItem(selectItem);
					}
				});

		// DynamicForm fontNameForm = new DynamicForm();
		// fontNameForm.setCellSpacing(0);
		// fontNameForm.setNumCols(1);
		// fontNameForm.setFields(fontNameBox);
		// DynamicForm fontSizeForm = new DynamicForm();
		// fontSizeForm.setCellSpacing(0);
		// fontSizeForm.setNumCols(1);
		// fontSizeForm.setFields(fontSizeBox);

		StyledPanel unitsPanel = new StyledPanel("unitsPanel");
		unitsPanel.add(cmButton);
		unitsPanel.add(inchButton);

		StyledPanel measurePanel = new StyledPanel("measurePanel");
		measurePanel.add(measureLabel);
		// measurePanel.setCellHorizontalAlignment(measureLabel,
		// HasAlignment.ALIGN_CENTER);
		measurePanel.add(unitsPanel);
		measurePanel.setStyleName("measurePanel");

		nameForm = new DynamicForm("nameForm");
		// nameForm.setCellSpacing(0);
		nameItem.setRequired(true);
		nameForm.add(nameItem);

		logoNameBox = new TextItem(messages.addLogo(), "logoNameBox");
		logoNameBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ValueCallBack<ClientBrandingTheme> callback = new ValueCallBack<ClientBrandingTheme>() {
					@Override
					public void execute(ClientBrandingTheme value) {
						logoNameBox.setValue(value.getFileName());
					}
				};
				String[] filetypes = { "png", "jpg", "jpeg", "gif" };

				FileUploadDilaog dilaog = new FileUploadDilaog("Upload Logo",
						"parent", callback, filetypes, getData(), false);
				ViewManager.getInstance().showDialog(dilaog);
			}
		});

		textBoxTable = new FlexTable();
		DynamicForm dynamicForm = new DynamicForm("allItems");

		textBoxTable.setWidget(0, 0, pageSizeLabel);
		textBoxTable.setWidget(0, 1, a4Button);
		textBoxTable.setWidget(1, 1, usLetterButton);
		dynamicForm.add(topMarginBox, bottomMarginBox, addressPadBox,
				fontNameBox, fontSizeBox, overdueBox, creditNoteBox,
				statementBox, quoteBox, cashSaleBox, purchaseOrderBox,
				salesOrderBox, logoNameBox);

		// textBoxTable.setWidget(2, 1, );
		// textBoxTable.setWidget(3, 0, bottomMarginLabel);
		// textBoxTable.setWidget(3, 1, );
		// textBoxTable.setWidget(4, 0, addressPadLabel);
		// textBoxTable.setWidget(4, 1, );
		// textBoxTable.setWidget(5, 0, fontLabel);
		// textBoxTable.setWidget(5, 1, fontNameForm);
		// textBoxTable.setWidget(6, 0, fontSizeLabel);
		// textBoxTable.setWidget(6, 1, fontSizeForm);
		// // textBoxTable.setWidget(8, 0, draftLabel);
		// // textBoxTable.setWidget(8, 1, draftBox);
		// // textBoxTable.setWidget(9, 0, approvedLabel);
		// // textBoxTable.setWidget(9, 1, approvedBox);
		// textBoxTable.setWidget(7, 0, overdueLabel);
		// textBoxTable.setWidget(7, 1, );
		// textBoxTable.setWidget(8, 0, creditNoteLabel);
		// textBoxTable.setWidget(8, 1, );
		// textBoxTable.setWidget(9, 0, statementLabel);
		// textBoxTable.setWidget(9, 1, );
		// textBoxTable.setWidget(10, 0, addLogoLabel);
		// textBoxTable.setWidget(10, 1, );

		StyledPanel textBoxStyledPanel = new StyledPanel("textBoxStyledPanel");

		StyledPanel textBoxPanel = new StyledPanel("textBoxPanel");
		textBoxPanel.add(nameForm);
		textBoxPanel.add(textBoxTable);
		// textBoxTable.setWidth("86%");
		textBoxPanel.add(dynamicForm);
		textBoxStyledPanel.add(textBoxPanel);
		textBoxStyledPanel.add(measurePanel);
		textBoxStyledPanel.setStyleName("rightBorder");

		return textBoxStyledPanel;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		ClientBrandingTheme brandingThemeByName = Accounter.getCompany()
				.getBrandingThemeByName(data.getThemeName());
		if (getData() == null && brandingThemeByName != null) {
			result.addError("TakenTheme", "Theme Name already exists");
			return result;
		}
		result.add(nameForm.validate());
		return result;
	}

	@Override
	public void saveFailed(AccounterException exception) {
		changeButtonBarMode(false);
		String errorString = AccounterExceptions.getErrorString(exception);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		super.saveSuccess(object);
	}

	@Override
	protected String getViewTitle() {
		return messages.editBrandThemeLabel();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientBrandingTheme saveView() {
		ClientBrandingTheme saveView = super.saveView();
		if (saveView != null) {
			getBrandingThemeObject();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		ClientBrandingTheme brandingTheme = getBrandingThemeObject();
		// if (!Utility.isObjectExist(Accounter.getCompany().getBrandingTheme(),
		// brandingTheme.getThemeName())) {
		// // TODO Do this checking in validation method
		// }
		saveOrUpdate(brandingTheme);

	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected void createButtons() {
		super.createButtons();
		remove(this.saveAndNewButton);
	}

	@Override
	public boolean canEdit() {
		return false;
	}

	/**
	 * Used to change the image, when template is selected from the
	 * corresponding template combo box
	 * 
	 * @param selectItem
	 */
	private void changeTemplateImage(String selectItem, String compareText) {
		if (compareText.equalsIgnoreCase("invoice")) {
			// for invoice reports
			if (selectItem.contains("Classic")) {
				invoiceTempImage.setResource(financeImages.vimuktiInvoice());

			} else if (selectItem.contains("Professional")) {
				invoiceTempImage.setResource(financeImages.xeroInvoice());
			} else if (selectItem.contains("Modern")) {
				invoiceTempImage.setResource(financeImages.zohoInvoice());
			} else if (selectItem.contains("Plain")) {
				invoiceTempImage.setResource(financeImages.quickbooksInvoice());
			}
		} else if (compareText.equalsIgnoreCase("credit")) {
			// for credit reports
			if (selectItem.contains("Classic")) {
				creditTempImage.setResource(financeImages.vimuktiCredit());
			} else if (selectItem.contains("Professional")) {
				creditTempImage.setResource(financeImages.xeroCredit());
			} else if (selectItem.contains("Modern")) {
				creditTempImage.setResource(financeImages.zohoCredit());
			} else if (selectItem.contains("Plain")) {
				creditTempImage.setResource(financeImages.quickbooksCredit());
			}
		} else if (compareText.equalsIgnoreCase("quote")) {
			// for quote reports
			if (selectItem.contains("Classic")) {
				quoteTempImage.setResource(financeImages.classicQuote());
			} else if (selectItem.contains("Professional")) {
				quoteTempImage.setResource(financeImages.professionalQuote());
			} else if (selectItem.contains("Modern")) {
				quoteTempImage.setResource(financeImages.modernQuote());
			} else if (selectItem.contains("Plain")) {
				quoteTempImage.setResource(financeImages.plainQuote());
			}
		}

	}

	@Override
	public void setFocus() {
		this.nameItem.setFocus();

	}

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
