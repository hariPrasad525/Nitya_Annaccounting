package com.nitya.accounter.web.client.ui.translation;

import java.util.Set;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.nitya.accounter.web.client.ClientLocalMessage;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.translate.ClientMessage;
import com.nitya.accounter.web.client.translate.TranslateServiceAsync;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;

public class MessagePanel extends FlowPanel {
	private ClientMessage clientMessage;
	private String language;
	private TranslationView view;
	private TextBox addNewMessageBox;
	private TranslateServiceAsync async = Accounter.createTranslateService();
	private StyledPanel mainPanel, localMessagesPanel;

	protected AccounterMessages messages = Global.get().messages();
	private boolean canApprove;

	public MessagePanel(TranslationView view, String language,
			ClientMessage clientMessage, boolean canApprove) {
		this.view = view;
		this.clientMessage = clientMessage;
		this.language = language;
		this.canApprove = canApprove;
		createControls();
	}

	private void createControls() {
		mainPanel = new StyledPanel("mainPanel");
		localMessagesPanel = new StyledPanel("localMessagesPanel");

		Label valueLabel = new Label(clientMessage.getValue());

		for (int i = 0; i < clientMessage.getLocalMessages().size(); i++) {
			localMessagesPanel.add(getLocalMessgePanel(clientMessage
					.getLocalMessages().get(i)));
		}

		addNewMessageBox = new TextBox();
		addNewMessageBox.removeStyleName("gwt-TextBox");
		addNewMessageBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
						&& addNewMessageBox.getText().trim().length() != 0) {
					final String data = addNewMessageBox.getText();
					validateUserValue(data);
				}
			}

		});

		mainPanel.add(valueLabel);
		mainPanel.add(localMessagesPanel);
		mainPanel.add(addNewMessageBox);

		valueLabel.addStyleName("value_lable");
		localMessagesPanel.addStyleName("local_message_panel");
		addNewMessageBox.addStyleName("add_new_message_box");

		mainPanel.addStyleName("message_panel");

		this.add(mainPanel);

		this.addStyleName("message-panel-parent");
	}

	protected void validateUserValue(final String data) {
		async.validateUserValue(clientMessage, data,
				new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						addNewMessageBox.setValue("");
						Accounter.showError("Data Mismatch");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							addLocalMessage(data);
						} else {
							showValidationError();
						}
						addNewMessageBox.setValue("");
					}

				});
	}

	private void showValidationError() {
		async.getServerMatchList(new AsyncCallback<Set<String>>() {

			@Override
			public void onSuccess(Set<String> result) {

				String errorMsg = messages.pleaseEnterTheWordsAsItis();
				String fieldsNames = "";
				for (String string : result) {
					fieldsNames = fieldsNames + string + " ";
				}
				errorMsg = errorMsg + fieldsNames;
				Accounter.showError(errorMsg);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated
				// method stub

			}
		});
	}

	protected void addLocalMessage(final String data) {
		async.addTranslation(clientMessage.getId(), language, data,
				new AsyncCallback<ClientLocalMessage>() {

					@Override
					public void onSuccess(ClientLocalMessage result) {
						LocalMessagePanel localMessagePanel = new LocalMessagePanel(
								result, view, canApprove, clientMessage);
						localMessagesPanel.add(localMessagePanel);
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
	}

	private LocalMessagePanel getLocalMessgePanel(
			final ClientLocalMessage clientLocalMessage) {
		LocalMessagePanel localMessagePanel = new LocalMessagePanel(
				clientLocalMessage, view, canApprove, clientMessage);
		return localMessagePanel;
	}

}
