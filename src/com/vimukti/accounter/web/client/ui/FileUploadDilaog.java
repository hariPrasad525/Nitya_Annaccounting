package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

@SuppressWarnings({ "deprecation" })
public class FileUploadDilaog extends CustomDialog {

	private String parentID;
	private FormPanel uploadForm;
	private ValueCallBack<ClientBrandingTheme> callback;
	private StyledPanel loadingLayout;
	private StyledPanel mainLayout;
	private StyledPanel panel;
	private ArrayList<FileUpload> uploadItems = new ArrayList<FileUpload>();
	private int count = 1;
	private StyledPanel uploadFormLayout;
	private String[] fileTypes;
	private StyledPanel buttonHlay;
	private boolean closeAfterUploaded;
	private static ClientBrandingTheme brandingTheme;
	private boolean isCustomTemplateUpload;

	private String title;
	private HTML detailsHtml1, helpHtml, chooseHtml, chooseHtml1, detailsHtml2,
			detailsHtml3, detailsHtml4, detailsHtml5;

	public FileUploadDilaog(String title, String parentID,
			ValueCallBack<ClientBrandingTheme> callback, String[] fileTypes,
			ClientBrandingTheme theme, boolean forCusotomTemplateUpload) {
		super(false, true);
		this.getElement().setId("FileUploadDilaog");
		this.title = title;
		setText(title);
		this.parentID = parentID;
		this.callback = callback;
		this.fileTypes = fileTypes;
		this.brandingTheme = theme;
		closeAfterUploaded = true;
		this.isCustomTemplateUpload = forCusotomTemplateUpload;
		doCreateContents();
	}

	protected void doCreateContents() {
		messages = messages;
		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");
		final String fileID = createID();

		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		StyledPanel vpaPanel = new StyledPanel("vpaPanel");

		panel = new StyledPanel("panel");

		if (isCustomTemplateUpload) {

			// for uploading the custom template files
			detailsHtml1 = new HTML(messages.uploadOneOrMoreTemplates());
			detailsHtml2 = new HTML(messages.eachFileCanBeNoLongerThan());
			detailsHtml3 = new HTML(messages.invoice());
			detailsHtml3.addStyleName("bold_HTML");
			detailsHtml4 = new HTML(messages.creditNote());
			detailsHtml4.addStyleName("bold_HTML");

			final FileUpload invoice_upload = new FileUpload();
			/* Default height of upload text box 26 */
			invoice_upload.getElement().setAttribute("size", "33");
			invoice_upload.setName(fileID);

			final FileUpload creditNote_upload = new FileUpload();
			final String fileID_1 = createID();
			/* Default height of upload text box 26 */
			creditNote_upload.getElement().setAttribute("size", "33");
			creditNote_upload.setName(fileID_1);

			uploadItems.add(invoice_upload);
			uploadItems.add(creditNote_upload);

			// panel.add(detailsHtml1);
			// panel.setSpacing(5);
			// panel.add(detailsHtml2);
			// panel.setSpacing(5);
			// panel.add(detailsHtml3);
			// panel.setSpacing(5);
			panel.add(invoice_upload);
			// panel.setSpacing(5);
			// panel.add(detailsHtml4);
			// panel.setSpacing(5);
			// panel.add(creditNote_upload);

			vpaPanel.add(panel);
		} else {
			// for uploading the CompanyLogo image
			detailsHtml1 = new HTML(messages.logoComment1());
			detailsHtml2 = new HTML(messages.logoComment2());
			detailsHtml3 = new HTML(messages.logoComment3());
			detailsHtml4 = new HTML(messages.logoComment4());
			detailsHtml5 = new HTML(messages.logoComment5());
			detailsHtml5.addStyleName("bold_HTML");
			helpHtml = new HTML(messages.helpContent());
			helpHtml.addStyleName("help_content");
			helpHtml.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					helpHtml.getElement().getStyle().setCursor(Cursor.POINTER);
					helpHtml.getElement().getStyle()
							.setTextDecoration(TextDecoration.UNDERLINE);
				}
			});
			helpHtml.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					helpHtml.getElement().getStyle()
							.setTextDecoration(TextDecoration.NONE);
				}
			});
			helpHtml.setVisible(false);
			chooseHtml = new HTML(messages.chooseLogo());
			final FileUpload upload = new FileUpload();
			/* Default height of upload text box 26 */
			upload.getElement().setAttribute("size", "33");
			upload.setName(fileID);
			uploadItems.add(upload);
			panel.add(detailsHtml1);
			panel.add(detailsHtml2);
			panel.add(detailsHtml3);
			panel.add(detailsHtml4);
			panel.add(detailsHtml5);
			panel.add(helpHtml);
			panel.add(chooseHtml);
			panel.add(upload);
			vpaPanel.add(panel);
		}
		// Add a 'submit' button.
		Button uploadSubmitButton = new Button(messages.upload());
		// uploadSubmitButton.setWidth("80px");
		// vpaPanel.add(uploadSubmitButton);

		Button closeButton = new Button(messages.close());
		buttonHlay = new StyledPanel("buttonHlay");
		buttonHlay.add(uploadSubmitButton);
		buttonHlay.add(closeButton);
		buttonHlay.setStyleName("panel-right-align");
		vpaPanel.add(buttonHlay);
		/* Make align three Element on there position */
		// buttonHlay.setCellWidth(uploadSubmitButton);

		// buttonHlay.setCellHorizontalAlignment(closeButton,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// buttonHlay.setCellHorizontalAlignment(uploadSubmitButton,
		// HasHorizontalAlignment.ALIGN_RIGHT);

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
			public void onSubmit(FormSubmitEvent event) {

			}

			public void onSubmitComplete(FormSubmitCompleteEvent event) {

				String value = event.getResults();

				if (brandingTheme == null) {
					brandingTheme = new ClientBrandingTheme();
				}

				if (isCustomTemplateUpload) {
					// for custom files upload for Brandingtheme
					// String[] split = value.split(".docx");
					//
					// if (split.length == 1) {
					// // if only one file is uploaded
					// brandingTheme.setInvoiceTempleteName(split[0]
					// .toString());
					// } else {
					// // if two files are uploaded
					// brandingTheme.setInvoiceTempleteName(split[0]
					// .toString());
					// brandingTheme.setCreditNoteTempleteName(split[1]
					// .toString());
					// }

					brandingTheme.setCustomFile(true);
					if (title.toLowerCase().contains("invoice")) {
						brandingTheme.setInvoiceTempleteName(value);
					} else if (title.toLowerCase().contains("credit")) {
						brandingTheme.setCreditNoteTempleteName(value);
					} else if (title.toLowerCase().contains("quote")) {
						brandingTheme.setQuoteTemplateName(value);
					}
				} else {
					// for CompanyLogo upload for BrandingTheme
					brandingTheme.setFileName(value);
					brandingTheme.setLogoAdded(true);
					brandingTheme.setCustomFile(false);
				}

				if (value == null) {
					getFileInfo();
				} else {
					if (value.trim().length() != 0) {

						processUploadAttachments(brandingTheme, callback);
					} else {
						loadingLayout.setVisible(false);
					}

				}
			}

		});
		uploadFormLayout = new StyledPanel("uploadFormLayout");
		uploadFormLayout.add(uploadForm);
		mainLayout = new StyledPanel("mainLayout");
		mainLayout.add(uploadFormLayout);
		add(mainLayout);
		show();
	}

	protected void getFileInfo() {
		getFileInfo(parentID,
				new AccounterAsyncCallback<ClientBrandingTheme>() {
					@Override
					public void onException(AccounterException caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onResultSuccess(ClientBrandingTheme result) {
						processUploadAttachments(result, callback);
					}
				});

	}

	public StyledPanel getFileItem() {
		final StyledPanel panel = new StyledPanel("panel");
		// panel.setHeight("30");
		panel.setStyleName("fileuploaddialog-fileitem");

		final FileUpload upload = new FileUpload();
		String fileID = createID();
		upload.setName(fileID);
		// upload.getElement().setAttribute("size", "50");

		HTML label = new HTML(messages.remove());
		label.addStyleName("remove_html");
		// label.setWidth("60px");
		// label.setHeight("25px");
		label.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				panel.removeFromParent();
				uploadItems.remove(upload);
				count--;
				// uploadFormLayout.setHeight(new StringBuilder(uploadFormLayout
				// .getOffsetHeight() - 30).toString());
			}
		});

		uploadItems.add(upload);
		panel.add(upload);
		panel.add(label);

		return panel;

	}

	private boolean checkFileType(String name, String[] types) {

		String type = name.substring(name.lastIndexOf('.') + 1);
		for (String fileType : types) {
			if (type.equalsIgnoreCase(fileType))
				return true;

		}
		Accounter.showInformation("Please select image file.");
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

	protected void processOnUpload() {

		if (!validateFileItems()) {

			if (isCustomTemplateUpload) {
				// for custom file error message
				Accounter.showInformation(messages.noFileSelected());
			} else {
				// for image error message
				Accounter.showInformation(messages.noImageisselected());
			}
			return;
		}

		if (!isCustomTemplateUpload) {
			// we need to validate for images types only when uploading company
			// logo for the BrandingTheme
			if (fileTypes != null) {
				for (FileUpload fileUpload : uploadItems) {
					if (!checkFileType(fileUpload.getFilename(), fileTypes)) {
						return;
					}
				}

			}
		}
		uploadForm.submit();

	}

	private boolean validateFileItems() {
		// String ids="";
		boolean fileSelected = false;
		for (FileUpload upload : uploadItems) {
			String filename = upload.getFilename();

			if (filename != null && filename.length() > 0) {
				fileSelected = true;
			}
		}

		if (fileSelected) {
			if (isCustomTemplateUpload) {
				uploadForm.setAction("/do/uploadtemplatefile?themeId="
						+ brandingTheme.getID());
			} else {
				uploadForm.setAction("/do/uploadfile");
			}

			return fileSelected;
		}

		return fileSelected;

	}

	public void close() {
		this.removeFromParent();
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

	public void getFileInfo(String parentID,
			final AccounterAsyncCallback<ClientBrandingTheme> callback) {

		String url = "/do/uploadfile?parentId=" + parentID;
		if (isCustomTemplateUpload) {
			url = "/do/uploadtemplatefile?parentId=" + parentID;
		}
		post(url, new AccounterAsyncCallback<ClientBrandingTheme>() {

			public void onResultSuccess(ClientBrandingTheme value) {
				callback.onResultSuccess(value);
			}

			@Override
			public void onException(AccounterException caught) {
				// TODO Auto-generated method stub
			}
		}, parentID);
	}

	public static <E, T> Object post(String url,
			final AccounterAsyncCallback<ClientBrandingTheme> callback,
			String parentId) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Accept", "text/html");
		// Create a callback object to handle the result
		RequestCallback requestCallback = new RequestCallback() {
			public void onError(Request request, Throwable exception) {
				exception.printStackTrace();
			}

			@Override
			public void onResponseReceived(Request request, Response response) {
				// ClientBrandingTheme attachment =
				// parseStringAttachment(response
				// .getText());
				callback.onResultSuccess(brandingTheme);
			}
		};
		try {
			Request request = builder.sendRequest("", requestCallback);
			return request;
		} catch (RequestException e) {
			e.printStackTrace();
		}
		return null;
	}
}
