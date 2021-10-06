package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DesktopMenuFactory;
import com.vimukti.accounter.web.client.ui.FileUploadDilaog;
import com.vimukti.accounter.web.client.ui.IMenuFactory;
import com.vimukti.accounter.web.client.ui.IMenuFactory.IMenu;
import com.vimukti.accounter.web.client.ui.IMenuFactory.IMenuBar;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.UploadTemplateFileDialog;
import com.vimukti.accounter.web.client.ui.core.NewBrandCustomThemeAction;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class InvoiceBrandingView<T> extends
		AbstractBaseView<ClientBrandingTheme> {

	private ClientBrandingTheme brandingTheme;
	private HTML generalSettingsHTML, allLabelsHtml, ShowHtml, checkBoxHtml,
			headingsHtml, paypalEmailHtml, termsHtml, radioButtonHtml,
			contactDetailsHtml, titleHtml, invoiceFileName, creditNoteFileName,
			quoteFileName, purchaseOrderFileName, salesOrderFileName,
			downloadOdtHtml, downloadDocxHtml, uploadHtml, cashSaleFileName;
	private Label titleLabel;
	// helpHtml;
	private StyledPanel mainPanel, titlePanel, subLayPanel, uploadPanel,
			contactDetailsPanel, vPanel;
	private Button newBrandButton, automaticButton;
	private StyledPanel buttonPanel, showPanel, allPanel, nameAndMenuPanel,
			buttonPanel2, detailsPanel;
	private IMenuFactory menuFactory = null;
	private ClientCompany company;

	@Override
	public void setData(ClientBrandingTheme data) {
		super.setData(data);
		if (data != null) {
			brandingTheme = data;
		} else
			brandingTheme = null;
	}

	private void createControls() {
		company = Accounter.getCompany();
		mainPanel = new StyledPanel("mainPanel");
		titlePanel = new StyledPanel("titlePanel");
		generalSettingsHTML = new HTML(messages.generalSettings());
		generalSettingsHTML.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				generalSettingsHTML.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				generalSettingsHTML.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		generalSettingsHTML.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				generalSettingsHTML.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		titleLabel = new Label(messages.invoiceBranding());
		titleLabel.removeStyleName("gwt-Label");
		titleLabel.setStyleName("label-title");
		generalSettingsHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new GeneralSettingsAction().run(null, false);
			}
		});
		generalSettingsHTML.setVisible(false);
		titlePanel.add(generalSettingsHTML);
		titlePanel.add(titleLabel);

		buttonPanel = new StyledPanel("buttonPanel");
		// for displaying the Branding theme menu
		IMenuBar menuBar = getBrandingmenuBar();
		menuBar.addMenuItem(messages.newBrandTheme(), getBrandingMenu());

		newBrandButton = new Button(messages.newBrandTheme());
		newBrandButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				new NewBrandThemeAction().run(null, false);
			}
		});

		automaticButton = new Button(messages.automaticSequencing());
		automaticButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new AutomaticSequenceAction().run(null, false);

			}
		});
		automaticButton.setVisible(false);

		// buttonPanel.add(newBrandButton);
		buttonPanel.add(menuBar);

		buttonPanel.add(automaticButton);

		mainPanel.add(titlePanel);
		mainPanel.add(buttonPanel);

		List<ClientBrandingTheme> brandingThemes = Accounter.getCompany()
				.getBrandingTheme();

		// To display regular and uploaded themes separately, taken loop two
		// times

		for (int i = 0; i < brandingThemes.size(); i++) {
			// for regular BrandingTheme Objects
			brandingTheme = brandingThemes.get(i);
			if (brandingTheme.isCustomFile() == false) {

				mainPanel.add(addingThemeToView(brandingTheme));
			}
		}
		for (int i = 0; i < brandingThemes.size(); i++) {
			// for custom file uploaded themes
			brandingTheme = brandingThemes.get(i);
			if (brandingTheme.isCustomFile()) {
				mainPanel.add(addingCustomThemeToView(brandingTheme));
			}
		}

		// mainPanel.setWidth("100%");
		add(mainPanel);

	}

	// private native boolean isTouch() /*-{
	// return $wnd.isTouch;
	// }-*/;

	private IMenuBar getBrandingmenuBar() {
		// boolean isTouch = isTouch();

		// if (isTouch) {
		// menuFactory = new TouchMenuFactory();
		// } else {
		menuFactory = new DesktopMenuFactory();
		// }

		return menuFactory.createMenuBar();
	}

	/**
	 * This method is used to display the Branding Menu
	 */
	private IMenu getBrandingMenu() {
		IMenu themesMenuBar = createMenu();
		themesMenuBar.addMenuItem(new NewBrandThemeAction());
		if (Accounter.hasPermission(Features.BRANDING_THEME)) {
			themesMenuBar.addMenuItem(new NewBrandCustomThemeAction());
		}
		return themesMenuBar;
	}

	private IMenu createMenu() {
		return menuFactory.createMenu();
	}

	private String getPageType(int type) {
		String pageType = null;
		if (type == ClientBrandingTheme.PAGE_SIZE_A4) {
			pageType = "A4";
		} else {
			pageType = messages.usLetter();
		}

		return pageType;

	}

	private String getLogoType(int type) {
		String logoType = null;
		if (type == ClientBrandingTheme.LOGO_ALIGNMENT_LEFT) {
			logoType = messages.left();
		} else {
			logoType = messages.right();
		}
		return logoType;

	}

	private SimplePanel addingCustomThemeToView(final ClientBrandingTheme theme) {
		vPanel = new StyledPanel("vPanel");
		subLayPanel = new StyledPanel("subLayPanel");
		final Button editButton, copyThemeButton, deleteButton, downloadOdtFiles, downloadDocxFiles, uploadButton;
		editButton = new Button(messages.edit());
		editButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				new NewBrandCustomThemeAction().run(theme, true);
			}
		});

		copyThemeButton = new Button(messages.copy());
		copyThemeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new CopyThemeAction().run(theme, false);
			}
		});

		deleteButton = new Button(messages.delete());
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new DeleteThemeAction().run(theme, false);
			}
		});

		downloadOdtFiles = new Button(messages.downloadOdtFiles());
		downloadOdtFiles.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UIUtils.downloadCustomFile("CustomOdtFiles.zip");
			}
		});

		downloadDocxFiles = new Button(messages.downloadDocxFiles());
		downloadDocxFiles.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UIUtils.downloadCustomFile("CustomDocxFiles.zip");
			}
		});

		uploadButton = new Button(messages.upload());
		uploadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				ValueCallBack<ClientBrandingTheme> callback = new ValueCallBack<ClientBrandingTheme>() {
					@Override
					public void execute(ClientBrandingTheme value) {
						saveOrUpdate(value);
					}
				};
				UploadTemplateFileDialog uploadDialog = new UploadTemplateFileDialog(
						"Upload Custom Templates", "parent", callback, theme);
				ViewManager.getInstance().showDialog(uploadDialog);
			}
		});

		titleHtml = new HTML(theme.getThemeName());

		nameAndMenuPanel = new StyledPanel("nameAndMenuPanel");
		buttonPanel2 = new StyledPanel("buttonPanel2");

		nameAndMenuPanel.add(titleHtml);
		nameAndMenuPanel.setStyleName("standard-options");
		titleHtml.getElement().getAbsoluteLeft();

		buttonPanel2.add(editButton);
		buttonPanel2.add(copyThemeButton);
		buttonPanel2.add(deleteButton);

		titleHtml.getElement().getParentElement()
				.addClassName("standard_label");

		nameAndMenuPanel.add(buttonPanel2);

		// buttonPanel2.getElement().getParentElement()
		// .setAttribute("width", "18%");
		// buttonPanel2.setWidth("100%");

		vPanel.add(nameAndMenuPanel);
		// vPanel.setWidth("100%");

		StyledPanel invoicePanel = new StyledPanel("invoicePanel");
		invoiceFileName = new HTML(theme.getInvoiceTempleteName());
		invoicePanel.add(new LabelItem(messages.invoice(), "type"));
		invoicePanel.add(invoiceFileName);
		invoicePanel.setStyleName("rightBorder");

		StyledPanel creditPanel = new StyledPanel("creditPanel");
		creditNoteFileName = new HTML(theme.getCreditNoteTempleteName().trim());
		creditPanel.add(new LabelItem(messages.creditNoteTitle(), "type"));
		creditPanel.add(creditNoteFileName);
		creditPanel.setStyleName("rightBorder");

		StyledPanel quotePanel = new StyledPanel("quotePanel");
		quoteFileName = new HTML(theme.getQuoteTemplateName());
		quotePanel.add(new LabelItem(messages.quote(), "type"));
		quotePanel.add(quoteFileName);
		quotePanel.setStyleName("rightBorder");

		StyledPanel cashSalePanel = new StyledPanel("cashSalePanel");
		cashSaleFileName = new HTML(theme.getCashSaleTemplateName());
		cashSalePanel.add(new LabelItem(messages.cashSale(), "type"));
		cashSalePanel.add(cashSaleFileName);
		cashSalePanel.setStyleName("rightBorder");

		StyledPanel purchaseOrderPanel = new StyledPanel("purchaseOrderPanel");
		purchaseOrderFileName = new HTML(theme.getPurchaseOrderTemplateName());
		purchaseOrderPanel.add(new LabelItem(messages.purchaseOrder(), "type"));
		purchaseOrderPanel.add(purchaseOrderFileName);
		purchaseOrderPanel.setStyleName("rightBorder");

		StyledPanel salesOrderPanel = new StyledPanel("salesOrderPanel");
		salesOrderFileName = new HTML(theme.getSalesOrderTemplateName());
		salesOrderPanel.add(new LabelItem(messages.salesOrder(), "type"));
		salesOrderPanel.add(salesOrderFileName);
		salesOrderPanel.setStyleName("rightBorder");

		downloadOdtHtml = new HTML(messages.odtDownloadMessage());
		downloadDocxHtml = new HTML(messages.docxDownloadMessage());

		uploadHtml = new HTML(messages.themeUploadMessage());

		detailsPanel = new StyledPanel("detailsPanel");

		StyledPanel downloadPanel = new StyledPanel("downloadPanel");
		downloadPanel.add(downloadOdtFiles);
		downloadPanel.add(downloadOdtHtml);
		downloadPanel.add(downloadDocxFiles);
		downloadPanel.add(downloadDocxHtml);

		StyledPanel uploadPanel = new StyledPanel("uploadPanel");
		// uploadPanel.setWidth("30%");
		uploadPanel.add(uploadButton);
		uploadPanel.add(uploadHtml);

		StyledPanel transactionStyles = new StyledPanel("transactionStyles");

		transactionStyles.add(invoicePanel);
		transactionStyles.add(creditPanel);
		transactionStyles.add(quotePanel);
		transactionStyles.add(cashSalePanel);
		transactionStyles.add(purchaseOrderPanel);
		transactionStyles.add(salesOrderPanel);

		detailsPanel.add(transactionStyles);
		detailsPanel.add(downloadPanel);
		detailsPanel.add(uploadPanel);

		vPanel.add(detailsPanel);

		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setStyleName("setting-class-panel");
		simplePanel.add(vPanel);
		return simplePanel;

	}

	private SimplePanel addingThemeToView(final ClientBrandingTheme theme) {

		final HTML uploadPictureHtml, changeLogoHtml, removeLogoHtml;
		final Button editButton, copyThemeButton, deleteButton;
		titleHtml = new HTML("<strong>" + theme.getThemeName() + "</strong>");
		vPanel = new StyledPanel("vPanel");

		subLayPanel = new StyledPanel("subLayPanel");

		editButton = new Button(messages.edit());
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new NewBrandThemeAction().run(theme, false);
				MainFinanceWindow.getViewManager().existingView.onEdit();
				MainFinanceWindow.getViewManager().removeEditButton();
			}
		});

		copyThemeButton = new Button(messages.copy());
		copyThemeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new CopyThemeAction().run(theme, false);
			}
		});

		deleteButton = new Button(messages.delete());
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new DeleteThemeAction().run(theme, false);
			}
		});

		double topMargin;
		if (theme.getTopMargin() != 0) {
			topMargin = theme.getTopMargin();
		} else {
			topMargin = 0.0;
		}

		double bottomMargin;
		if (theme.getBottomMargin() != 0) {
			bottomMargin = theme.getBottomMargin();
		} else {
			bottomMargin = 0.0;
		}

		double addressPadding;
		if (theme.getAddressPadding() != 0) {
			addressPadding = theme.getAddressPadding();
		} else {
			addressPadding = 0.0;
		}
		allLabelsHtml = new HTML("<p>" + messages.pageLabel() + " : <b>"
				+ getPageType(theme.getPageSizeType()) + "</b> ; "
				+ messages.marginTop() + " : <b>" + topMargin + "</b> ; "
				+ messages.bottom() + " : <b>" + bottomMargin + "</b> ; "
				+ messages.addressPadding() + " : <b>" + addressPadding
				+ "</b> ; " + messages.font() + "   : <b>" + theme.getFont()
				+ "</b> , <b>" + theme.getFontSize() + " </b>.</p>");

		boolean[] showArray = new boolean[] { theme.isShowTaxNumber(),
				theme.isShowTaxColumn(), theme.isShowColumnHeadings(),
				theme.isShowUnitPrice_And_Quantity(),
				// theme.isShowPaymentAdviceCut_Away(),
				theme.isShowRegisteredAddress(), theme.isShowLogo() };

		String[] showDataArray = new String[] { messages.taxNumber(),
				messages.taxColumn(), messages.showVatColumn(),
				messages.columnHeadings(), messages.unitPriceAndQuantity(),
				// messages.paymentAdviceCutAway(),
				messages.registeredAddress(), messages.logo() };
		String showItem = new String();
		for (int i = 0; i < showArray.length; i++) {
			if (showArray[i]) {
				showItem += "<li>" + showDataArray[i];
			}
		}
		ShowHtml = new HTML("<p>" + messages.show() + " : </p>");
		checkBoxHtml = new HTML("<ui>" + showItem + "</ui>");
		radioButtonHtml = new HTML("<ui><li>"
		// + getTaxesType(theme.getShowTaxesAsType())
				+ getLogoType(theme.getLogoAlignmentType()) + "<ui>");

		// adding titles.....
		String overDueTitle = theme.getOverDueInvoiceTitle() == null ? messages
				.none() : theme.getOverDueInvoiceTitle();
		String creditMemoTitle = theme.getCreditMemoTitle() == null ? messages
				.none() : theme.getCreditMemoTitle();
		String statementTitle = theme.getStatementTitle() == null ? messages
				.none() : theme.getStatementTitle();
		String quoteTitle = theme.getQuoteTitle() == null ? messages.none()
				: theme.getQuoteTitle();
		String purcahseOrderTitle = theme.getPurchaseOrderTitle() == null ? messages
				.none() : theme.getPurchaseOrderTitle();
		String salesOrderTitle = theme.getSalesOrderTitle() == null ? messages
				.none() : theme.getSalesOrderTitle();

		headingsHtml = new HTML("<p>" + messages.headings()
				+ " : "
				// + theme.getOpenInvoiceTitle()
				+ overDueTitle + " , " + creditMemoTitle + " , "
				+ statementTitle + "," + quoteTitle + " , "
				+ purcahseOrderTitle + " , " + salesOrderTitle + "</p>");

		// adding paypal email......
		String paypalEmail = theme.getPayPalEmailID() == null ? messages.none()
				: theme.getPayPalEmailID();

		paypalEmailHtml = new HTML("<p>" + messages.paypalEmail() + " : "
				+ paypalEmail + "</p>");

		// adding terms.....
		String terms = theme.getTerms_And_Payment_Advice() == null ? messages
				.notAdded() : theme.getTerms_And_Payment_Advice();

		termsHtml = new HTML("<p> " + messages.terms() + " : " + terms + "</p>");

		// adding invoice templete name
		String invoiceTemp = theme.getInvoiceTempleteName() == null ? messages
				.classicTemplate() : theme.getInvoiceTempleteName();

		HTML invoiceHtml = new HTML("<p>" + messages.invoiceTemplete() + " : "
				+ invoiceTemp + "</p>");

		// adding credit note template name
		String creditTemp = theme.getCreditNoteTempleteName() == null ? messages
				.classicTemplate() : theme.getCreditNoteTempleteName();

		HTML creditNoteHtml = new HTML("<p>" + messages.creditNoteTemplete()
				+ " : " + creditTemp + "</p>");

		// adding quote template name
		String quote = theme.getQuoteTemplateName() == null ? messages
				.classicTemplate() : theme.getQuoteTemplateName();

		HTML quoteHtml = new HTML("<p>" + messages.quoteTemplate() + " : "
				+ quote + "</p>");

		// adding purchaseOrder template name
		String purchaseOrder = theme.getPurchaseOrderTemplateName() == null ? messages
				.classicTemplate() : theme.getPurchaseOrderTemplateName();
		HTML purchaseOrderHtml = new HTML("<p>"
				+ messages.purchaseOrderTemplate() + " : " + purchaseOrder
				+ "</p>");

		// adding salesOrder template name
		String salesOrder = theme.getSalesOrderTemplateName() == null ? messages
				.classicTemplate() : theme.getSalesOrderTemplateName();
		HTML salesOrderHtml = new HTML("<p>" + messages.salesOrderTemplate()
				+ " : " + salesOrder + "</p>");

		showPanel = new StyledPanel("showPanel");
		showPanel.add(checkBoxHtml);
		showPanel.add(radioButtonHtml);

		// adding contact details.....
		String contactDetails = theme.getContactDetails();

		contactDetailsHtml = new HTML("<p><b>" + messages.contactDetailsLabel()
				+ "</b><br>" + contactDetails + "</p>");

		if (theme.getContactDetails() == null) {
			contactDetailsPanel = new StyledPanel("contactDetailsPanel");
			contactDetailsPanel.add(contactDetailsHtml);
			contactDetailsPanel.setStyleName("contact-deatails-panel");
			contactDetailsPanel.setVisible(false);
		} else {
			contactDetailsPanel = new StyledPanel("contactDetailsPanel");
			contactDetailsPanel.add(contactDetailsHtml);
			contactDetailsPanel.setStyleName("contact-deatails-panel");
			contactDetailsPanel.setVisible(true);
		}

		if ((theme.getFileName() == null)) {
			uploadPictureHtml = new HTML(messages.uploadLogo());
			// uploadPictureHtml.setWidth("104px");
			uploadPictureHtml.setVisible(true);
			uploadPictureHtml.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					uploadPictureHtml.getElement().getStyle()
							.setCursor(Cursor.POINTER);
					uploadPictureHtml.getElement().getStyle()
							.setTextDecoration(TextDecoration.UNDERLINE);
				}
			});
			uploadPictureHtml.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					uploadPictureHtml.getElement().getStyle()
							.setTextDecoration(TextDecoration.NONE);
				}
			});
			uploadPictureHtml.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					uploadPictureHtml.getElement().getStyle()
							.setTextDecoration(TextDecoration.NONE);
					changeLogo(theme);

				}
			});
		} else {
			StringBuffer original = new StringBuffer();
			String imagesDomain = "/do/downloadFileFromFile?";
			original.append("<img src='");
			original.append(imagesDomain);
			original.append("fileName=");
			original.append(theme.getFileName());
			original.append("' ");
			original.append("/>");
			uploadPictureHtml = new HTML("" + original);
		}
		changeLogoHtml = new HTML(messages.changeLogo());
		changeLogoHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				changeLogoHtml.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				changeLogoHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		changeLogoHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				changeLogoHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		changeLogoHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeLogo(theme);
			}
		});
		removeLogoHtml = new HTML(messages.removeLogo());
		removeLogoHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				removeLogoHtml.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				removeLogoHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		removeLogoHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				removeLogoHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		removeLogoHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (theme.getFileName() == null) {
					Accounter.showInformation(messages.noLogoIsAdded());
				} else {
					removeLogo(theme);
				}
			}
		});

		changeLogoHtml.setStyleName("change-logo");
		removeLogoHtml.setStyleName("remove-logo");
		uploadPictureHtml.setStyleName("picture-link");
		StyledPanel panel = new StyledPanel("panel");
		panel.setStyleName("panel-logo");
		StyledPanel verticalPanel = new StyledPanel("verticalPanel");
		panel.add(changeLogoHtml);
		panel.add(removeLogoHtml);
		uploadPanel = new StyledPanel("uploadPanel");
		uploadPanel.setStyleName("upload-logo");
		uploadPanel.add(uploadPictureHtml);
		verticalPanel.add(uploadPanel);
		// verticalPanel.setWidth("100%");
		verticalPanel.add(panel);
		// panel.getElement().getParentElement().setAttribute("align",
		// "center");

		if (theme.getFileName() == null) {
			panel.setVisible(false);
		} else {
			panel.setVisible(true);
		}
		subLayPanel.add(allLabelsHtml);
		subLayPanel.add(ShowHtml);
		subLayPanel.add(showPanel);
		subLayPanel.add(headingsHtml);
		subLayPanel.add(paypalEmailHtml);
		subLayPanel.add(termsHtml);
		subLayPanel.add(invoiceHtml);
		subLayPanel.add(creditNoteHtml);
		subLayPanel.add(quoteHtml);
		subLayPanel.add(purchaseOrderHtml);
		subLayPanel.add(salesOrderHtml);

		subLayPanel.setStyleName("general-setting-invoice");
		allPanel = new StyledPanel("allPanel");
		allPanel.add(subLayPanel);
		allPanel.add(contactDetailsPanel);
		allPanel.add(verticalPanel);
		// allPanel.setCellWidth(subLayPanel, "60%");
		// allPanel.setCellWidth(contactDetailsPanel, "20%");
		// allPanel.setCellWidth(verticalPanel, "20%");
		// allPanel.setWidth("100%");

		nameAndMenuPanel = new StyledPanel("nameAndMenuPanel");
		buttonPanel2 = new StyledPanel("buttonPanel2");

		nameAndMenuPanel.add(titleHtml);
		nameAndMenuPanel.setStyleName("standard-options");
		titleHtml.getElement().getAbsoluteLeft();

		buttonPanel2.add(editButton);
		buttonPanel2.add(copyThemeButton);
		buttonPanel2.add(deleteButton);

		titleHtml.getElement().getParentElement()
				.addClassName("standard_label");

		nameAndMenuPanel.add(buttonPanel2);

		// buttonPanel2.getElement().getParentElement()
		// .setAttribute("width", "18%");
		// buttonPanel2.setWidth("100%");

		// optionsButton.setStyleName("ibutton-right-align") ;
		if (theme.getName().equalsIgnoreCase(messages.standard())) {
			deleteButton.setVisible(false);
			// buttonPanel2.getElement().getParentElement()
			// .setAttribute("width", "12%");
		} else {
			deleteButton.setVisible(true);
		}
		vPanel.add(nameAndMenuPanel);
		vPanel.add(allPanel);
		// allPanel.setWidth("100%");
		// vPanel.setWidth("100%");
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setStyleName("setting-class-panel");
		simplePanel.add(vPanel);
		return simplePanel;

	}

	protected void removeLogo(ClientBrandingTheme theme) {

		theme.setLogoAdded(false);
		theme.setFileName(null);
		saveOrUpdate(theme);

	}

	protected void changeLogo(ClientBrandingTheme theme) {

		ValueCallBack<ClientBrandingTheme> callback = new ValueCallBack<ClientBrandingTheme>() {
			@Override
			public void execute(ClientBrandingTheme value) {
				saveOrUpdate(value);
			}
		};
		String[] filetypes = { "png", "jpg", "gif", "jpeg" };
		FileUploadDilaog dilaog = new FileUploadDilaog("Upload file", "parent",
				callback, filetypes, theme, false);
		ViewManager.getInstance().showDialog(dilaog);

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
	public void init() {
		super.init();
		try {
			createControls();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.getElement().setId("invoicebrandingview");
	}

	@Override
	public void initData() {
		super.initData();
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
	public void saveSuccess(IAccounterCore object) {
		if (object != null) {
			super.saveSuccess(object);
		} else
			saveFailed(new AccounterException(messages.failed()));
	}

	@Override
	protected String getViewTitle() {
		return messages.invoiceBranding();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
