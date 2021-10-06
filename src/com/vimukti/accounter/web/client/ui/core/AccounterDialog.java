package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

public class AccounterDialog extends CustomDialog {
	public ErrorDialogHandler dialogHandler;
	public AccounterType type;
	public String message;
	private boolean isError;
	private Button okButton;
	private Button cancelButton;
	private HTML goPremiumLink;

	public AccounterDialog(String mesg, AccounterType type) {

		super(true);
		this.getElement().setId("AccounterDialog");
		this.message = mesg;
		this.type = type;
		createControls();
		show();
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

	public AccounterDialog(String mesg, AccounterType type,
			ErrorDialogHandler handler) {

		super(true);
		this.getElement().setId("AccounterDialog");
		this.message = mesg;
		this.type = type;
		this.dialogHandler = handler;
		createControls();
		show();
	}

	public void createControls() {

		StyledPanel bodyLayout = new StyledPanel("bodyLayout");

		StyledPanel headerLayout = new StyledPanel("headerLayout");

		StyledPanel buttonLayout = new StyledPanel("buttonLayout");
		// buttonLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		// buttonLayout.setSpacing(5);
		// buttonLayout.setAutoHeight();
		// buttonLayout.setHeight("20%");

		Button yesButton;
		Button noButton;

		ImageResource imageUrl;

		if ((isError = this.type.equals(AccounterType.ERROR))
				|| this.type.equals(AccounterType.INFORMATION)) {
			// set imagePath for error type

			if (isError) {

				imageUrl = Accounter.getFinanceImages().errorIcon();
				setText(messages.ERROR());

			} else {

				imageUrl = Accounter.getFinanceImages().infoIcon();
				setText(messages.INFORMATION());
			}

			okButton = new Button(messages.ok());
//			okButton.setWidth("80px");

			// buttonLayout.setAlign(Alignment.RIGHT);
			buttonLayout.add(okButton);
			okButton.setEnabled(true);
			okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					okClicked();
					// Accounter.stopExecution();
				}

			});

		} else if (isError = this.type.equals(AccounterType.SUBSCRIPTION)) {

			// set imagePath for error type

			if (isError) {
				imageUrl = Accounter.getFinanceImages().errorIcon();
				setText(messages.ERROR());

			} else {

				imageUrl = Accounter.getFinanceImages().infoIcon();
				setText(messages.INFORMATION());
			}

			okButton = new Button(Accounter.getMessages().cancel());
//			okButton.setWidth("80px");
			okButton.setEnabled(true);
			okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					okClicked();
				}

			});
			String emailId = Accounter.getUser().getEmail();
			goPremiumLink = new HTML("<a href='/main/gopremium?emailId="
					+ emailId + "' target='_blank'>go premium </a>");

			buttonLayout.add(goPremiumLink);
			buttonLayout.add(okButton);

		} else {

			imageUrl = Accounter.getFinanceImages().warnIcon();
			setText(messages.WARNING());
			yesButton = new Button(messages.yes());
//			yesButton.setWidth("60px");
			noButton = new Button(messages.no());
//			noButton.setWidth("60px");
			yesButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					try {
						yesClicked();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});
			noButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					try {
						noClicked();
					} catch (InvalidEntryException e) {
					}
				}

			});
			// buttonLayout.setAlign(Alignment.RIGHT);
			if (this.type.equals(AccounterType.WARNINGWITHCANCEL)) {
				cancelButton = new Button(messages.cancel());
				buttonLayout.add(yesButton);
				buttonLayout.add(noButton);
				buttonLayout.add(cancelButton);
				cancelButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						removeFromParent();
					}

				});
			} else {
				buttonLayout.add(yesButton);
				buttonLayout.add(noButton);
				yesButton.setEnabled(true);
				noButton.setEnabled(true);
			}

		}

		Image image = new Image(imageUrl);
		StyledPanel imageLayout = new StyledPanel("imageLayout");
		imageLayout.add(image);
		HTML msgLabel = new HTML(this.message);

		headerLayout.add(imageLayout);
		headerLayout.add(msgLabel);
		// headerLayout.setCellVerticalAlignment(msgLabel,
		// HasVerticalAlignment.ALIGN_MIDDLE);
		// headerLayout.setSpacing(5);
//		headerLayout.setHeight("10%");
//		buttonLayout.setHeight("10%");

		bodyLayout.add(headerLayout);
		bodyLayout.add(buttonLayout);

		setModal(true);
		add(bodyLayout);
	}

	protected void noClicked() throws InvalidEntryException {
		if (dialogHandler != null)
			if (dialogHandler.onNoClick()) {
				removeFromParent();
			}
	}

	protected void yesClicked() throws Exception {
		if (dialogHandler != null) {
			if (dialogHandler.onYesClick())
				removeFromParent();
		}

	}

	protected void okClicked() {
		removeFromParent();
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONKEYPRESS:
			int keycode = event.getKeyCode();
			if (KeyCodes.KEY_ESCAPE == keycode) {
				this.okClicked();
			}
			break;
		case Event.ONMOUSEOVER:
			if (cancelButton != null)
				cancelButton.setFocus(true);
			else if (okButton != null) {
				okButton.setFocus(true);
			}

		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	@Override
	protected void onLoad() {
		if (cancelButton != null)
			cancelButton.setFocus(true);
		else if (okButton != null) {
			okButton.setFocus(true);
		}

		super.onLoad();
	}

	protected void setDialogHandler(ErrorDialogHandler dialogHandler) {
		this.dialogHandler = dialogHandler;
	}

	@Override
	public void show() {
		super.show();
		center();
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
