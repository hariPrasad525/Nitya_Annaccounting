package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class TransactionAttachmentPanel extends SimplePanel {
	protected static AccounterMessages messages = Global.get().messages();

	private static final String ATTACHMENT_URL = "/do/uploadattachment";

	StyledPanel attachmentTable;
	private boolean isFileSelected;
	private final String[] fileTypes = { "*" };
	private List<ClientAttachment> attachments = new ArrayList<ClientAttachment>();
	FormPanel uploadForm;
	private Anchor browseFileAnchor;
	private Button uploadButton;
	private FileUpload uploadFile;

	public TransactionAttachmentPanel() {
		createControls();
		this.addStyleName("attachment_panel");
		// setSize("100%", "100%");
	}

	private void createControls() {
		StyledPanel mainPanel = new StyledPanel("mainPanel");
		Label label = new Label(messages.attachments());
		label.addStyleName("history_notes_label");
		mainPanel.add(label);
		attachmentTable = new StyledPanel("attachmentTable");
		mainPanel.add(attachmentTable);

		uploadForm = new FormPanel();
		uploadForm.setVisible(!isInViewMode());
		uploadForm.setAction(ATTACHMENT_URL);
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);
		StyledPanel hPanel = new StyledPanel("hPanel");
		uploadFile = new FileUpload();
		uploadFile.setEnabled(!isInViewMode());
		browseFileAnchor = new Anchor(messages.attachaFile());// Choose
																// File
		browseFileAnchor.setEnabled(!isInViewMode());
		uploadFile.setVisible(false);
		uploadFile.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String filename = getFileName(uploadFile.getFilename());
				for (ClientAttachment attachment : attachments) {
					if (attachment.getName().equals(filename)) {
						Accounter.showError(messages
								.attachmentNameShouldbeUniqueInTransaction());
						return;
					}
				}
				isFileSelected = true;
				browseFileAnchor.setText(filename);
				uploadFile.setName(filename);
				// Show button
				uploadButton.setVisible(true);
			}
		});
		hPanel.add(uploadFile);
		uploadForm.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				if (uploadFile.getFilename() == null
						|| uploadFile.getFilename().length() <= 0) {
					Accounter.showInformation(messages.noFileSelected());
					return;
				}

				// if (!checkFileType(upload.getFilename(), fileTypes)) {
				// return;
				// }
			}
		});

		uploadForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String text = browseFileAnchor.getText();
				String results = event.getResults();
				String[] split = results.split("#");
				if (split.length != 2) {
					return;// Failed
				}
				ClientAttachment attachment = new ClientAttachment();
				attachment.setCreatedBy(Accounter.getUser().getID());
				attachment.setAttachmentId(split[0]);
				attachment.setName(text);
				attachment.setSize(Long.parseLong(split[1]));

				browseFileAnchor.setText(messages.attachaFile());// Choose
																	// File
				// Hide that button
				uploadButton.setVisible(false);
				attachmentTable.add(getAttachmentField(attachment));
				attachments.add(attachment);
				saveAttachment(attachment);
			}
		});

		browseFileAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				uploadFile.setVisible(true);
				uploadFile.getElement().focus();
				clickOnInputFile(uploadFile.getElement());
				uploadFile.setVisible(false);
			}
		});
		hPanel.add(browseFileAnchor);

		uploadForm.add(hPanel);

		uploadButton = new Button(messages.upload());
		uploadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (isFileSelected) {
					isFileSelected = false;
					uploadForm.submit();
				}
			}
		});
		hPanel.add(uploadButton);
		uploadButton.setVisible(false);
		mainPanel.add(uploadForm);

		this.add(mainPanel);
	}

	public Anchor getBrowseFileAnchor() {
		return browseFileAnchor;
	}

	public void setBrowseFileAnchor(Anchor browseFileAnchor) {
		this.browseFileAnchor = browseFileAnchor;
	}

	private String getFileName(String fileName) {
		int backslashIndex = fileName.lastIndexOf("\\");
		if (backslashIndex > -1) {
			return fileName.substring(backslashIndex + 1);
		}
		return fileName;
	}

	protected StyledPanel getAttachmentField(final ClientAttachment attachment) {
		StyledPanel hPanel = new StyledPanel("hPanel");
		hPanel.addStyleName("transaction_attachment_hpanel");
		Anchor anchor = new Anchor(attachment.getName());
		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (attachment.getID() != 0) {
					UIUtils.downloadTransactionAttachment(
							attachment.getAttachmentId(), attachment.getName());
				} else {
					UIUtils.downloadFileFromTemp(attachment.getName(),
							attachment.getName());
				}
			}
		});

		Label attachmentSizeLabel = new Label(getFileSize(attachment.getSize()));

		Label creatorLabel = new Label(Accounter.getCompany()
				.getUserById(attachment.getCreatedBy()).getName());

		ImageButton button = new ImageButton(Accounter.getFinanceImages()
				.delete(), "delete");
		button.setVisible(!isInViewMode());
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deeleteAttachment(attachment);
			}
		});
		hPanel.add(anchor);
		hPanel.add(attachmentSizeLabel);
		hPanel.add(creatorLabel);
		hPanel.add(button);
		return hPanel;
	}

	private String getFileSize(long size) {
		final long K = 1024;
		final long M = K * K;
		final long G = M * K;
		final long T = G * K;
		final long[] dividers = new long[] { T, G, M, K, 1 };
		final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
		if (size < 1)
			throw new IllegalArgumentException("Invalid file size: " + size);
		String result = null;
		for (int i = 0; i < dividers.length; i++) {
			final long divider = dividers[i];
			if (size >= divider) {
				result = format(size, divider, units[i]);
				break;
			}
		}
		return result;
	}

	private String format(final long value, final long divider,
			final String unit) {
		final double result = divider > 1 ? (double) value / (double) divider
				: (double) value;
		return DataUtils.getAmountAsStrings(result) + " " + unit;
	}

	protected void deeleteAttachment(ClientAttachment attachment) {
		this.attachments.remove(attachment);
		addAttachments(this.attachments);
	}

	public abstract boolean isInViewMode();

	private static native void clickOnInputFile(Element elem) /*-{
		elem.click();
	}-*/;

	private boolean checkFileType(String name, String[] types) {
		String type = name.substring(name.lastIndexOf('.') + 1);
		for (String fileType : types) {
			if (type.equalsIgnoreCase(fileType))
				return true;

		}
		Accounter.showInformation(messages.pleaseSelect("File"));
		return false;

	}

	protected abstract void saveAttachment(ClientAttachment attachment);

	public void setAttachments(List<ClientAttachment> attachments) {
		this.attachments = attachments;
		for (ClientAttachment clientAttachment : attachments) {
			attachmentTable.add(getAttachmentField(clientAttachment));
		}
	}

	public List<ClientAttachment> getAttachments() {
		return attachments;
	}

	public void addAttachments(List<ClientAttachment> attachments) {
		this.attachments = attachments;
		attachmentTable.clear();
		for (ClientAttachment clientAttachment : attachments) {
			attachmentTable.add(getAttachmentField(clientAttachment));
		}
	}

	public void setEnable(boolean isEnable) {
		uploadForm.setVisible(isEnable);
		browseFileAnchor.setEnabled(isEnable);
		uploadFile.setEnabled(isEnable);
		for (int i = 0; i < attachmentTable.getWidgetCount(); i++) {
			Widget widget = attachmentTable.getWidget(i);
			if (widget instanceof StyledPanel) {
				StyledPanel panel = (StyledPanel) widget;
				ImageButton childWidget = (ImageButton) panel.getWidget(3);
				childWidget.setVisible(isEnable);
			}
		}
	}

}
