package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.core.ClientEmailAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class EmailAccountDialog extends BaseDialog<ClientEmailAccount> {

	private EmailField emailField;
	private PasswordItem passwordField;
	private TextItem mailServerField;
	private IntegerField portNumField;
	private CheckboxItem sslField;

	DynamicForm form;

	Button testButton;

	ClientEmailAccount data;

	public EmailAccountDialog(ClientEmailAccount data) {
		super(messages.emailAccount());
		this.getElement().setId("EmailAccountDialog");

		createControls();
		this.data = data;
		initData();
	}

	private void initData() {
		if (data == null) {
			data = new ClientEmailAccount();
		} else {
			emailField.setEmail(data.getEmailId());
			passwordField.setValue(data.getPassword());
			mailServerField.setValue(data.getSmtpMailServer());
			portNumField.setValue(data.getPortNumber() != 0 ? String
					.valueOf(data.getPortNumber()) : "");
			sslField.setValue(data.isSSL());
		}
	}

	private void createControls() {

		StyledPanel bodyLayout = new StyledPanel("bodyLayout");
		form = new DynamicForm("form");

		emailField = new EmailField(messages.email());
		emailField.setRequired(true);

		passwordField = new PasswordItem(messages.password());
		passwordField.setRequired(true);

		mailServerField = new TextItem(messages.smtpMailServer(),
				"mailServerField");
		mailServerField.setRequired(true);

		portNumField = new IntegerField(this, messages.portNumber());
		portNumField.setRequired(true);

		sslField = new CheckboxItem(messages.isSsl(), "sslField");

		form.add(emailField, passwordField, mailServerField, portNumField,
				sslField);

		bodyLayout.add(form);
		setBodyLayout(bodyLayout);
	}

	@Override
	protected void createButtons(StyledPanel footer) {
		super.createButtons(footer);
		testButton = new Button(messages.test());
		testButton.getElement().setAttribute("data-icon", "mail2");
		testButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (validate().haveErrors()) {
					processOK();
				} else {
					processTest();
				}

			}
		});
		addButton(footer, testButton);
	}

	protected void processTest() {
		updateData();
		EmailTestDialog testDialog = new EmailTestDialog(data);
		ViewManager.getInstance().showDialog(testDialog);
	}

	private void updateData() {
		data.setEmailId(emailField.getValue());
		data.setPassword(passwordField.getValue());
		data.setSmtpMailServer(mailServerField.getValue());
		data.setPortNumber(Integer.valueOf(portNumField.getValue()));
		data.setSSL(sslField.getValue());
	}

	@Override
	protected boolean onOK() {
		updateData();
		saveOrUpdate(data);
		return true;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		this.removeFromParent();
		if (getCallback() != null) {
			getCallback().actionResult((ClientEmailAccount) object);
		}
	}

	@Override
	public void setFocus() {
		emailField.setFocus();
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(form.validate());
		String email = emailField.getValue();
		ClientEmailAccount emailAccount = getCompany().getEmailAccount(email);
		if (emailAccount != null && data.getID() != emailAccount.getID()) {
			result.addError(emailField,
					messages.anEmailAccountAlreadyExistWithTheGivenEmailID());
		}
		return result;
	}
}
