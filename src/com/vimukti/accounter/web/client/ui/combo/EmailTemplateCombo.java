package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientEmailTemplate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.EmailTemplateDialog;

public class EmailTemplateCombo extends CustomCombo<ClientEmailTemplate> {
	EmailTemplateDialog emailTemplateDialog;
	ClientTransaction transaction;

	public EmailTemplateCombo(String title, boolean isAddNewRequire,
			ClientTransaction transaction) {
		super(title, isAddNewRequire, 1, "emailTemplateCombo");
		this.transaction = transaction;
		initCombo(getCompany().getEmailTemplates());
	}

	@Override
	protected String getDisplayName(ClientEmailTemplate object) {
		if (object != null)
			return object.getEmailTemplateName() != null ? object
					.getEmailTemplateName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientEmailTemplate object, int col) {
		switch (col) {
		case 0:
			return object.getEmailTemplateName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.Template();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAddNew() {
		emailTemplateDialog = new EmailTemplateDialog(messages.emaiTemplate(),
				" ", transaction);
		ViewManager.getInstance().showDialog(emailTemplateDialog);
		emailTemplateDialog
				.setCallback(new ActionCallback<ClientEmailTemplate>() {

					@Override
					public void actionResult(ClientEmailTemplate result) {
						if (result != null)
							addItemThenfireEvent(result);
					}
				});
	}
}
