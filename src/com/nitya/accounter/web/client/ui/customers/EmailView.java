package com.nitya.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAttachment;
import com.nitya.accounter.web.client.core.ClientContact;
import com.nitya.accounter.web.client.core.ClientEmailAccount;
import com.nitya.accounter.web.client.core.ClientEmailTemplate;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientInvoice;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.IDeleteCallback;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.TransactionAttachmentPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.EmailCombo;
import com.nitya.accounter.web.client.ui.combo.EmailTemplateCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.company.EmailAccountDialog;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.EmailField;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.FormItem;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class EmailView extends AbstractBaseView<ClientTransaction> {
	private ClientTransaction transaction;
	// private EmailField fromAddress;
	private TransactionAttachmentPanel transactionAttachmentPanel;
	private EmailField toAddress;
	private EmailField ccAddress;
	private TextItem subject;
	private TextAreaItem emailBody;
	private long brandingThemeId;
	private String ToAdd, ccAdd, sub, body, fileName;

	private EmailCombo fromAddcombo;

	private EmailTemplateCombo emailBodyTemplateCombo;

	private Button sendBtn;
	private Button cancelBtn;
	private Button smtpBtn;
	final DynamicForm form1 = new DynamicForm("firstForm");
	final DynamicForm form2 = new DynamicForm("secondForm");
	final DynamicForm form3 = new DynamicForm("thirdForm");
	private Button editButton;
	private Button deleteButton;
	private ClientEmailTemplate emailTemplate;

	public EmailView(ClientTransaction transaction) {
		this.transaction = transaction;
		this.getElement().setId("EmailView");
	}

	public EmailView() {
	}

	@Override
	public void init() {
		super.init();
		preparePdfFile();
		createControls();

	}

	private void preparePdfFile() {

		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			public void onSuccess(List<String> result) {
				fileName = result.get(0).toString();
			};
		};

		try {
			Accounter.createHomeService().createPdfFile(
					String.valueOf(transaction.getID()), transaction.getType(),
					brandingThemeId, new ClientFinanceDate(),
					new ClientFinanceDate(), callback);
		} catch (AccounterException e) {
			e.printStackTrace();
		}

	}

	public void createControls() {

		final AccounterMessages messages = Global.get().messages();
//		fromAddress = new EmailField(messages.from());
//		fromAddress.setText(emailDetails.EmailId());
//		fromAddress.setEnabled(false);
		
		fromAddcombo = new EmailCombo(messages.from(), true);
		fromAddcombo.setRequired(true);
		fromAddcombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientEmailAccount>() {

					@Override
					public void selectedComboBoxItem(
							ClientEmailAccount selectItem) {
						if (selectItem != null) {
							smtpBtn.setEnabled(true);
						} else {
							smtpBtn.setEnabled(false);
						}
					}
				});

		smtpBtn = new Button(messages.smtpSettings());
		smtpBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showEmailAccountDialog();
			}
		});
//		smtpBtn.setEnabled(fromAddcombo.getSelectedValue() != null ? true
//				: false);
		smtpBtn.setEnabled(false);
		ClientEmailAccount clientValue = new ClientEmailAccount();
		clientValue.setVersion(01);
		clientValue.setEmailId("hariprasad@annaccounting.com");
		clientValue.setID(1);
		clientValue.setPassword("Hariprasad@321");
		clientValue.setPortNumber(587);
		clientValue.setSmtpMailServer("mail.annaccounting.com");
		clientValue.setSSL(true);
//		fromAddress.setText(clientValue.getEmailId());
		fromAddcombo.setComboItem(clientValue);
		fromAddcombo.textBox.setText(clientValue.getEmailId());
		fromAddcombo.setEnabled(false);
		ClientContact contact = null;
		if (transaction instanceof ClientInvoice) {
			contact = ((ClientInvoice) transaction).getContact();
		}
		String toemail = contact != null ? contact.getEmail() : "";
		toAddress = new EmailField(messages.to());
		toAddress.setText(toemail.trim());
		toAddress.setRequired(true);
		ccAddress = new EmailField(messages.cc());
		ccAddress.setRequired(false);

		subject = new TextItem(messages.subject(), "subject");

		emailBodyTemplateCombo = new EmailTemplateCombo(
				messages.selectEmailBodyTemplate(), true, transaction);
		emailBodyTemplateCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientEmailTemplate>() {

					@Override
					public void selectedComboBoxItem(
							ClientEmailTemplate selectItem) {
						if (selectItem != null
								&& (selectItem.getEmailTemplateName()
										.equals(messages.defaultTemplate()))) {
							emailBody.setValue(selectItem.getEmailBody());
							editButton.setVisible(false);
							deleteButton.setVisible(false);
						} else if (selectItem != null) {
							emailBody.setValue(selectItem.getEmailBody());
							editButton.setVisible(true);
							deleteButton.setVisible(true);
						}
					}
				});
		emailTemplate = new ClientEmailTemplate();
		emailTemplate.setEmailTemplateName(messages.defaultTemplate());
		emailTemplate.setEmailBody(getMailMessage());
		emailBodyTemplateCombo.setComboItem(emailTemplate);

		emailBody = new TextAreaItem(messages.email(), "emailBody");
		emailBody.setTitle(messages.writeCommentsForThis(getAction()
				.getViewName()));
		emailBody.setValue(emailTemplate.getEmailBody());
		emailBody.setEnabled(false);

		editButton = new Button(messages.edit());
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EmailTemplateDialog dialog;

				if (emailBodyTemplateCombo.getValidator().size() == 1) {
					dialog = new EmailTemplateDialog(messages.email(), "",
							transaction);
				} else {
					ClientEmailTemplate selectedValue = emailBodyTemplateCombo
							.getSelectedValue();
					dialog = new EmailTemplateDialog(messages.email(), "",
							selectedValue);
					emailBody.setValue(selectedValue.getEmailBody());
				}
				dialog.setCallback(new ActionCallback<ClientEmailTemplate>() {

					@Override
					public void actionResult(ClientEmailTemplate result) {
						emailBody.setValue(result.getEmailBody());
					}
				});
				ViewManager.getInstance().showDialog(dialog);
			}
		});
		deleteButton = new Button(messages.delete());
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (emailBodyTemplateCombo.getSelectedValue() != null) {

					IDeleteCallback iDeleteCallback = new IDeleteCallback() {

						@Override
						public void deleteSuccess(IAccounterCore result) {
							reFreshView();
						}

						@Override
						public void deleteFailed(AccounterException caught) {
							int errorCode = caught.getErrorCode();
							String errorString = AccounterExceptions
									.getErrorString(errorCode);
							Accounter.showError(errorString);
							caught.fillInStackTrace();

						}
					};

					Accounter.deleteObject(iDeleteCallback,
							emailBodyTemplateCombo.getSelectedValue());

				}
			}
		});
		editButton.setVisible(false);
		deleteButton.setVisible(false);
		StyledPanel vPanel = new StyledPanel("vPanel");

		TextAreaItem attachmentItem = new TextAreaItem(messages.attachments(),
				"attachmentItem");
		attachmentItem.setValue(getAttachmentName());
		attachmentItem.setDisabled(true);
		// attachmentItem.setWidth(60);
		transactionAttachmentPanel = new TransactionAttachmentPanel() {
			
			@Override
			protected void saveAttachment(ClientAttachment attachment) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isInViewMode() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		transactionAttachmentPanel.setAttachments(data.getUploadFile());
		
		TextAreaItem attachmentFiles = new TextAreaItem("Attachment Files",
				"attachmentFiles");
//		attachmentFiles.setValue(data.getUploadFile());
		attachmentFiles.setDisabled(true);
		form2.add(attachmentItem);
		form2.add(transactionAttachmentPanel);
		vPanel.add(form2);

		form1.add(fromAddcombo, toAddress, ccAddress, subject);
		StyledPanel horPanel = new StyledPanel("horPanel");
		horPanel.add(form1);
		horPanel.add(smtpBtn);
		horPanel.add(vPanel);

		StyledPanel mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(horPanel);
		form3.add(emailBodyTemplateCombo);
		form3.add(emailBody);
		form3.addStyleName("email_textarea");
		mainPanel.add(form3);

		sendBtn = new Button(messages.send());
		sendBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// for checking the From and To email address

				onSave(false);

			}

		});

		cancelBtn = new Button(messages.cancel());
		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MainFinanceWindow.getViewManager().closeCurrentView();

			}
		});

		StyledPanel buttonsPanel = new StyledPanel("buttonsPanel");
		buttonsPanel.add(editButton);
		buttonsPanel.add(deleteButton);
		buttonsPanel.add(sendBtn);
		buttonsPanel.add(cancelBtn);

		mainPanel.add(buttonsPanel);

		this.add(mainPanel);

		toAddress.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (toAddress.getValue() != null)

					toAddress.setText(getValidMail(toAddress.getValue()));
			}
		});
		ccAddress.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (ccAddress.getValue() != null)

					ccAddress.setText(getValidMail(ccAddress.getValue()));
			}
		});
	}

	private void reFreshView() {
		emailBodyTemplateCombo.initCombo(getCompany().getEmailTemplates());
		emailBodyTemplateCombo.setComboItem(emailTemplate);
		emailBody.setValue(emailTemplate.getEmailBody());
		editButton.setVisible(false);
		deleteButton.setVisible(false);

	}

	protected void showEmailAccountDialog() {
		EmailAccountDialog dialog = new EmailAccountDialog(
				fromAddcombo.getSelectedValue());
		dialog.setCallback(new ActionCallback<ClientEmailAccount>() {

			@Override
			public void actionResult(ClientEmailAccount result) {
				fromAddcombo.setComboItem(result);
			}
		});
		ViewManager.getInstance().showDialog(dialog);
	}

	private String getValidMail(String email) {
		if (email.trim().length() != 0) {
			if (!UIUtils.isValidMultipleEmailIds(email)) {
				Accounter.showError(messages.invalidEmail());
				return "";
			} else
				return email;
		}
		return email;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		result.add(FormItem.validate(toAddress));
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		if (UIUtils.isValidMultipleEmailIds(toAddress.getValue().toString())) {
			ToAdd = toAddress.getValue().toString() != null ? toAddress
					.getValue().toString() : "";
			getValidMail(toAddress.getValue().toString());
			ccAdd = ccAddress.getValue().toString() != null ? ccAddress
					.getValue().toString() : "";
			sub = subject.getValue().toString() != null ? subject.getValue()
					.toString() : "";
			body = emailBody.getValue().toString() != null ? emailBody
					.getValue().toString() : "";
			body = body.replaceAll("\n", "<br/>");

			Accounter.createHomeService().sendPdfInMail(fileName, sub, body,
					fromAddcombo.getSelectedValue(), ToAdd, ccAdd,
					new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(Void result) {
							MainFinanceWindow.getViewManager()
									.closeCurrentView();
						}
					});
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return messages.email();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setThemeId(long themeId) {
		this.brandingThemeId = themeId;

	}

	private String getAttachmentName() {
		if (transaction != null) {
			int type = transaction.getType();
			if (type == ClientTransaction.TYPE_INVOICE) {
				return "Invoice_" + transaction.getNumber() + ".pdf";
			} else if (type == ClientTransaction.TYPE_CASH_SALES) {
				return "CashSale_" + transaction.getNumber() + ".pdf";
			} else if (type == ClientTransaction.TYPE_ESTIMATE) {
				if (((ClientEstimate) transaction).getEstimateType() == ClientEstimate.QUOTES) {
					return "Quote_" + transaction.getNumber() + ".pdf";
				} else if (((ClientEstimate) transaction).getEstimateType() == ClientEstimate.SALES_ORDER) {
					return "SalesOrder_" + transaction.getNumber() + ".pdf";
				}
			} else if (type == ClientTransaction.TYPE_PURCHASE_ORDER) {
				return "PurchaseOrder_" + transaction.getNumber() + ".pdf";
			}
		}
		return "";
	}

	private String getMailMessage() {
		String message = null;
		if (transaction.getType() == ClientTransaction.TYPE_INVOICE) {
			message = Global
					.get()
					.messages()
					.invoiceMailMessage(Global.get().Customer(),
							this.transaction.getNumber(), transaction.getDate());
		} else if (transaction.getType() == ClientTransaction.TYPE_CASH_SALES) {
			message = Global
					.get()
					.messages()
					.cashSaleMailMessage(Global.get().Customer(),
							this.transaction.getNumber(), transaction.getDate());
		} else if (transaction.getType() == ClientTransaction.TYPE_ESTIMATE) {
			if (((ClientEstimate) transaction).getEstimateType() == ClientEstimate.QUOTES) {
				message = Global
						.get()
						.messages()
						.quoteMailMessage(Global.get().Customer(),
								this.transaction.getNumber(),
								transaction.getDate());
			} else if (((ClientEstimate) transaction).getEstimateType() == ClientEstimate.SALES_ORDER) {
				message = Global
						.get()
						.messages()
						.salesOrderMailMessage(Global.get().Customer(),
								this.transaction.getNumber(),
								transaction.getDate());
			}
		} else if (transaction.getType() == ClientTransaction.TYPE_PURCHASE_ORDER) {
			message = Global
					.get()
					.messages()
					.purchaseOrderMailMessage(Global.get().Vendor(),
							this.transaction.getNumber(), transaction.getDate());
		}
		return message;

	}
}
