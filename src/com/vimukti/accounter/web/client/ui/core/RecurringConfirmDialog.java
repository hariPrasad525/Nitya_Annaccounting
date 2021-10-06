package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

public class RecurringConfirmDialog extends CustomDialog {

	protected StyledPanel headerLayout;
	protected StyledPanel bodyLayout;
	protected StyledPanel footerLayout;

	protected Button gotoRecurringBtn;
	protected Button keepWorkingBtn;
	protected StyledPanel mainPanel, mainVLayPanel;

	public RecurringConfirmDialog() {
		super(true);
		this.getElement().setId("RecurringConfirmDialog");
		setText(messages.pleaseConfirm());
		setModal(true);
		createControls();
		gotoRecurringBtn.setFocus(true);
		sinkEvents(Event.ONKEYPRESS);
		sinkEvents(Event.ONMOUSEOVER);
	}

	private void createControls() {

		/**
		 * Header Layout
		 */
		headerLayout = new StyledPanel("headerLayout");
		Label label = new Label();
		label.setText(messages.youSuccessfullyCreatedRecurringTemplate());
		headerLayout.add(label);

		/**
		 * Body LayOut
		 */
		bodyLayout = new StyledPanel("bodyLayout");

		/**
		 * Footer Layout
		 */
		footerLayout = new StyledPanel("footerLayout");
		// footerLayout.addStyleName("dialogfooter");

		this.gotoRecurringBtn = new Button(
				messages.gotoRecurringTransactionsList());

		gotoRecurringBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				removeFromParent();
				History.newItem(HistoryTokens.RECURRINGTRANSACTIONS, true);
			}
		});

		keepWorkingBtn = new Button(messages.keepWorkingOnThisTransaction());
		keepWorkingBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		});

		// footerLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		footerLayout.add(gotoRecurringBtn);
		footerLayout.add(keepWorkingBtn);

		// footerLayout.setCellHorizontalAlignment(gotoRecurringBtn,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// footerLayout.setCellHorizontalAlignment(keepWorkingBtn,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		/**
		 * adding all Layouts to Window
		 */

		mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(headerLayout);
		mainPanel.add(bodyLayout);
		mainPanel.add(footerLayout);

		add(mainPanel);

	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONKEYPRESS:
			int keycode = event.getKeyCode();
			if (KeyCodes.KEY_ESCAPE == keycode) {
				removeFromParent();
			}
			break;
		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
