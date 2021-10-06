package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.nitya.accounter.web.client.ValueCallBack;
import com.nitya.accounter.web.client.core.ClientBrandingTheme;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.settings.InvoiceBrandingAction;

@SuppressWarnings({ "deprecation" })
public class UploadTemplateFileDialog extends BaseDialog<ClientBrandingTheme> {
	private static final String INVOICE = "INVOICE";
	private static final String QUOTE = "QUOTE";
	private static final String CREDITNOTE = "CREDITNOTE";
	private static final String CASHSALE = "CASHSALE";
	private static final String PURCHASEORDER = "PURCHASEORDER";
	private static final String SALESORDER = "SALESORDER";
	private ValueCallBack<ClientBrandingTheme> callback;
	private FormPanel uploadForm;
	private StyledPanel mainLayout;
	private StyledPanel panel;
	private StyledPanel uploadFormLayout;
	private StyledPanel buttonHlay;
	private ClientBrandingTheme brandingTheme;
	private boolean closeAfterUploaded = true;
	private String title;
	private HTML detailsHtml1, detailsHtml2, detailsHtml3, detailsHtml4,
			detailsHtml5, detailsHtml6;
	private FileUpload invoiceBtn, creditNoteBtn, quoteBtn, cashSaleBtn;
	private String[] fileTypes;
	private ArrayList<FileUpload> uploadItems = new ArrayList<FileUpload>();
	private HTML detailsHtml7;
	private HTML detailsHtml8;
	private FileUpload purchaseOrderBtn;
	private FileUpload salesOrderBtn;

	public UploadTemplateFileDialog(String title, String parentID,
			ValueCallBack<ClientBrandingTheme> callback,
			ClientBrandingTheme theme) {
		// super(false, true);
		this.title = title;
		setText(title);
		this.getElement().setId("UploadTemplateFileDialog");
		this.callback = callback;
		this.brandingTheme = theme;
		this.fileTypes = new String[] { "odt", "docx" };
		doCreateContents();
	}

	protected void doCreateContents() {
		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		StyledPanel vpaPanel = new StyledPanel("vpaPanel");

		panel = new StyledPanel("panel");
		/* make space small */

		// for uploading the custom template files
		detailsHtml1 = new HTML(messages.uploadOneOrMoreTemplates());
		detailsHtml2 = new HTML(messages.eachFileCanBeNoLongerThan());
		detailsHtml3 = new HTML(messages.invoice());
		detailsHtml3.addStyleName("bold_HTML");
		detailsHtml4 = new HTML(messages.creditNote());
		detailsHtml4.addStyleName("bold_HTML");
		detailsHtml5 = new HTML(messages.quote());
		detailsHtml5.addStyleName("bold_HTML");
		detailsHtml6 = new HTML(messages.cashSale());
		detailsHtml6.addStyleName("bold_HTML");
		detailsHtml7 = new HTML(messages.purchaseOrder());
		detailsHtml7.addStyleName("bold_HTML");
		detailsHtml8 = new HTML(messages.salesOrder());
		detailsHtml8.addStyleName("bold_HTML");

		invoiceBtn = new FileUpload();
		// final String fileID_1 = createID();
		invoiceBtn.setName(INVOICE);

		creditNoteBtn = new FileUpload();
		// final String fileID_2 = createID();
		creditNoteBtn.setName(CREDITNOTE);

		quoteBtn = new FileUpload();
		// final String fileID_3 = createID();
		quoteBtn.setName(QUOTE);

		cashSaleBtn = new FileUpload();
		// final String fileID_3 = createID();
		cashSaleBtn.setName(CASHSALE);

		purchaseOrderBtn = new FileUpload();
		purchaseOrderBtn.setName(PURCHASEORDER);

		salesOrderBtn = new FileUpload();
		salesOrderBtn.setName(SALESORDER);

		uploadItems.add(invoiceBtn);
		uploadItems.add(creditNoteBtn);
		uploadItems.add(quoteBtn);
		uploadItems.add(cashSaleBtn);
		uploadItems.add(purchaseOrderBtn);
		uploadItems.add(salesOrderBtn);

		panel.add(detailsHtml1);
		panel.add(detailsHtml2);
		panel.add(detailsHtml3);

		StyledPanel invPanel = new StyledPanel("invPanel");
		invPanel.add(invoiceBtn);
		panel.add(invPanel);

		panel.add(detailsHtml4);

		StyledPanel creditPanel = new StyledPanel("creditPanel");
		creditPanel.add(creditNoteBtn);
		panel.add(creditPanel);

		panel.add(detailsHtml5);

		StyledPanel quotePanel = new StyledPanel("quotePanel");
		quotePanel.add(quoteBtn);
		panel.add(quotePanel);

		panel.add(detailsHtml6);

		StyledPanel cashPanel = new StyledPanel("cashPanel");
		cashPanel.add(cashSaleBtn);
		panel.add(cashPanel);

		panel.add(detailsHtml7);

		StyledPanel purchaseOrderPanel = new StyledPanel("purchaseOrderPanel");
		purchaseOrderPanel.add(purchaseOrderBtn);
		panel.add(purchaseOrderPanel);

		panel.add(detailsHtml8);

		StyledPanel salesOrderPanel = new StyledPanel("salesOrderPanel");
		salesOrderPanel.add(salesOrderBtn);
		panel.add(salesOrderPanel);

		vpaPanel.add(panel);

		// Add a 'submit' button.
		Button uploadSubmitButton = new Button(messages.save());
		uploadSubmitButton.getElement().setAttribute("data-icon", "save");
		uploadSubmitButton.setWidth("80px");
		// vpaPanel.add(uploadSubmitButton);

		Button closeButton = new Button(messages.close());
		closeButton.getElement().setAttribute("data-icon", "cancel");
		buttonHlay = new StyledPanel("buttonHlay");
		addButton(buttonHlay, uploadSubmitButton);
		addButton(buttonHlay, closeButton);
		buttonHlay.setStyleName("panel-right-align");
		vpaPanel.add(buttonHlay);
		/* Make align three Element on there position */
		// buttonHlay.setCellWidth(uploadSubmitButton);

		uploadSubmitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				processOnUpload();
			}

		});
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				close();
			}
		});

		uploadForm.setWidget(vpaPanel);
		uploadForm.addFormHandler(new FormHandler() {

			@Override
			public void onSubmit(FormSubmitEvent event) {
			}

			@Override
			public void onSubmitComplete(FormSubmitCompleteEvent event) {

				// processUploadAttachments(brandingTheme, callback);

				String value = event.getResults();

				if (brandingTheme == null) {
					brandingTheme = new ClientBrandingTheme();
				}
				processResult(brandingTheme, value);
			}
		});

		uploadFormLayout = new StyledPanel("uploadFormLayout");
		uploadFormLayout.add(uploadForm);
		mainLayout = new StyledPanel("mainLayout");
		mainLayout.add(uploadFormLayout);
		add(mainLayout);
		ViewManager.getInstance().showDialog(this);
	}

	/**
	 * this method is used to process the final result
	 * 
	 * @param brandingTheme
	 * @param value
	 *            (a.docx;b.docx;c.docx)or(a.odt;b.odt;c.odt)
	 */
	private void processResult(ClientBrandingTheme brandingTheme, String value) {

		if (value.trim().length() != 0) {
			String[] split = value.split(";");
			if (split[0].trim().length() > 0)
				brandingTheme.setInvoiceTempleteName(split[0]);
			if (split[1].trim().length() > 0)
				brandingTheme.setCreditNoteTempleteName(split[1]);
			if (split[2].trim().length() > 0)
				brandingTheme.setQuoteTemplateName(split[2]);
			if (split[3].trim().length() > 0)
				brandingTheme.setCashSaleTemplateName(split[3]);
			if (split[4].trim().length() > 0)
				brandingTheme.setPurchaseOrderTemplateName(split[4]);
			if (split[5].trim().length() > 0)
				brandingTheme.setSalesOrderTemplateName(split[5]);

			processUploadAttachments(brandingTheme, callback);
		}

	}

	protected void processOnUpload() {
		String file_1 = uploadItems.get(0).getFilename() == null ? ""
				: uploadItems.get(0).getFilename();
		String file_2 = uploadItems.get(1).getFilename() == null ? ""
				: uploadItems.get(1).getFilename();
		String file_3 = uploadItems.get(2).getFilename() == null ? ""
				: uploadItems.get(2).getFilename();
		String file_4 = uploadItems.get(3).getFilename() == null ? ""
				: uploadItems.get(3).getFilename();
		String file_5 = uploadItems.get(4).getFilename() == null ? ""
				: uploadItems.get(4).getFilename();
		String file_6 = uploadItems.get(5).getFilename() == null ? ""
				: uploadItems.get(5).getFilename();
		if (file_1.equals("") && file_2.equals("") && file_3.equals("")
				&& file_4.equals("") && file_5.equals("") && file_6.equals("")) {
			Accounter.showInformation(messages.noFileSelected());
			return;
		}
		for (FileUpload upload : uploadItems) {
			String filename = upload.getFilename();

			if (filename != null && filename.length() > 0) {
				if (!(filename.endsWith("odt") || filename.endsWith("docx"))) {

					Accounter.showInformation(messages
							.selectFileOfTypeOdtOrDocx());
					return;
				}
			}
		}

		uploadForm.setAction("/do/uploadtemplatefile?themeId="
				+ brandingTheme.getID());
		uploadForm.submit();
	}

	private boolean checkFileType(String name) {
		String type = name.substring(name.lastIndexOf('.') + 1);
		for (String fileType : fileTypes) {
			if (type.equalsIgnoreCase(fileType))
				return true;
		}
		return false;
	}

	private void processUploadAttachments(ClientBrandingTheme result,
			final ValueCallBack<ClientBrandingTheme> callback2) {

		if (callback2 != null) {
			callback2.execute(result);
		}
		if (closeAfterUploaded) {
			close();
		}

	}

	public void close() {
		this.removeFromParent();
	}

	@Override
	protected boolean onOK() {
		return true;
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
		super.saveSuccess(object);
		new InvoiceBrandingAction().run(null, true);
	}

	public native static String createID()/*-{
		var MES_UNIQUE_IDS = {};
		var mes_generateUniqueId = function(charset, len, isNotInDOM) {
			var i = 0;
			if (!charset) {
				charset = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
			}
			if (!len) {
				len = 40;
			}
			var id = '', charsetlen = charset.length, charIndex;

			// iterate on the length and get a random character for each position
			for (i = 0; len > i; i += 1) {
				charIndex = Math.random() * charsetlen;
				id += charset.charAt(charIndex);
			}

			if (MES_UNIQUE_IDS[id] || (isNotInDOM)) {
				MES_UNIQUE_IDS[id] = true; // add DOM ids to the map
				return mes_generateUniqueId(charset, len, isNotInDOM);
			}

			MES_UNIQUE_IDS[id] = true;

			return id;
		};
		return mes_generateUniqueId();
	}-*/;
}
