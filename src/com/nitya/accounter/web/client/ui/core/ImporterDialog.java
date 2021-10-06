package com.nitya.accounter.web.client.ui.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.forms.CustomDialog;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LabelItem;

public class ImporterDialog extends CustomDialog {
	public String message;
	private Button okButton;
	AccounterMessages messges = Global.get().messages();
	private Map<Integer, Object> result = new HashMap<Integer, Object>();
	private StyledPanel scrollPanel;

	public ImporterDialog(String mesg, Map<Integer, Object> result) {
		this.getElement().setId("ImporterDialog");
		this.message = mesg;
		this.result = result;
		setText(mesg);
		createControls();

		// if (type == AccounterType.INFORMATION) {
		// Timer timer = new Timer() {
		//
		// @Override
		// public void run() {
		// okClicked();
		// Accounter.stopExecution();
		// }
		// };
		// timer.schedule(4000);
		// }
		sinkEvents(Event.ONKEYPRESS);
		sinkEvents(Event.ONMOUSEOVER);
	}

	private void createControls() {
		StyledPanel errorPanel = new StyledPanel("errorPanel");
		scrollPanel = new StyledPanel("scrollPanel");
		long successCount = ((Long) result.get(0)).intValue();
		int failureCount = result.size() - 1;
		result.remove(new Integer((int) successCount));

		DynamicForm importInfoForm = new DynamicForm("importInfoForm");
		LabelItem importedLabel = new LabelItem(messages.recordsImported()
				+ successCount, "importedLabel");
		LabelItem importeFailLabel = new LabelItem(messages.recordsFailed()
				+ failureCount, "importeFailLabel");
		importInfoForm.add(importedLabel, importeFailLabel);
		errorPanel.add(importInfoForm);

		for (Entry<Integer, Object> entry : result.entrySet()) {
			Integer key = entry.getKey();
			Object exception = entry.getValue();
			if (exception instanceof List<?>) {
				List<?> exceptionsList = (List<?>) exception;

				// buffer.append("<li>" + messages.exceptionDetails() + " "
				// + messages.recordIn() + "(" + key.intValue() + ")");

				for (int i = 0; i < exceptionsList.size(); i++) {
					AccounterException exception1 = (AccounterException) exceptionsList
							.get(i);
					LabelItem exceptionLabel = new LabelItem(messages.hash()
							+ key.intValue() + "    " + messages.column() + "("
							+ i + ")" + "   " + exception1.getMessage() + "  "
							+ AccounterExceptions.getErrorString(exception1),
							"exceptionLabel");
					scrollPanel.add(exceptionLabel);

				}
			} else if (exception instanceof AccounterException) {

				LabelItem exceptionLabel = new LabelItem(
						messages.hash()
								+ key.intValue()
								+ "   "
								+ messages.column()
								+ "("
								+ key.intValue()
								+ ")"
								+ "   "
								+ AccounterExceptions
										.getErrorString((AccounterException) exception),
						"exceptionLabel");
				scrollPanel.add(exceptionLabel);
			}
		}

		okButton = new Button(messages.ok());
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeFromParent();
				closeView();
			}
		});

		if (scrollPanel.getWidgetCount() > 0) {
			LabelItem errorDetailsLabel = new LabelItem(
					messages.errorDetails(), "errorDetailsLabel");
			errorPanel.add(errorDetailsLabel);
			errorPanel.add(scrollPanel);
		}
		errorPanel.add(okButton);

		add(errorPanel);
	}

	protected void closeView() {

	}
}
