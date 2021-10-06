package com.nitya.accounter.web.client.ui.customers;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientEmailTemplate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class EmailTemplateDialog extends BaseDialog<ClientEmailTemplate> {
	private ClientTransaction transaction;
	private TextAreaItem emailBody;
	private TextItem emailTemplateNameText;
	private ClientEmailTemplate template;

	public EmailTemplateDialog(String title, String desc,
			ClientEmailTemplate emailTemplate) {
		super(title, desc);
		this.template = emailTemplate;
		createContrls();
	}

	public EmailTemplateDialog(String title, String desc,
			ClientTransaction transaction) {
		super(title, desc);
		this.transaction = transaction;
		createContrls();
	}

	private void createContrls() {
		StyledPanel emailPanel = new StyledPanel("emailPanel");
		emailTemplateNameText = new TextItem(messages.name(),
				"emailTemplateNameText");
		emailTemplateNameText.setValue(template == null ? "" : template
				.getEmailTemplateName());
		emailTemplateNameText.setRequired(true);
		emailBody = new TextAreaItem(messages.email(), "emailBody");
		emailBody.setTitle(messages.writeCommentsForThis(messages.invoice()));
		emailBody.setValue(template == null ? Global
				.get()
				.messages()
				.invoiceMailMessage(Global.get().Customer(),
						this.transaction.getNumber(), transaction.getDate())
				: template.getEmailBody());
		emailPanel.add(emailTemplateNameText);
		emailPanel.add(emailBody);
		setBodyLayout(emailPanel);

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult validate = super.validate();
		if (emailTemplateNameText.getValue() == null
				|| emailTemplateNameText.getValue().isEmpty()) {
			validate.addError(emailTemplateNameText,
					messages.pleaseEnter(messages.Template() + messages.name()));
		}
		if (emailBody.getValue() == null || emailBody.getValue().isEmpty()) {
			validate.addError(emailBody,
					messages.pleaseEnter(messages.emailBodyShouldNotBeEmpty()));
		}
		return validate;
	}

	@Override
	protected boolean onOK() {
		createTemplate();
		return true;
	}

	private void createTemplate() {
		if (template == null) {
			template = new ClientEmailTemplate();
			template.setEmailTemplateName(emailTemplateNameText.getValue());
			template.setEmailBody(emailBody.getValue());
		} else {
			template.setEmailTemplateName(emailTemplateNameText.getValue());
			template.setEmailBody(emailBody.getValue());
		}
		saveOrUpdate(template);

	}

	@Override
	public void setFocus() {
		emailTemplateNameText.setFocus();
		emailBody.setFocus();
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (getCallback() != null) {
			getCallback().actionResult((ClientEmailTemplate) object);
		}
		super.saveSuccess(object);
	}

}
