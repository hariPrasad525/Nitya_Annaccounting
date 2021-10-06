package com.nitya.accounter.web.client.ui.translation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.nitya.accounter.web.client.ClientLocalMessage;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.translate.ClientMessage;
import com.nitya.accounter.web.client.translate.TranslateServiceAsync;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.StyledPanel;

public class LocalMessagePanel extends FlowPanel {

	private ClientLocalMessage clientLocalMessage;
	private TranslationView view;
	private TranslateServiceAsync async = Accounter.createTranslateService();
	private boolean canApprove;
	private ImageButton upImage;
	private StyledPanel upVotesPanel;
	private StyledPanel votesPanel;
	private Label messageLabel, upVotesLengthLabel;
	private FlowPanel approvePanel;
	private RadioButton approveButton;
	private ClientMessage clientMessage;

	public LocalMessagePanel(ClientLocalMessage clientLocalMessage,
			TranslationView view, boolean canApprove,
			ClientMessage clientMessage) {
		this.clientLocalMessage = clientLocalMessage;
		this.clientMessage = clientMessage;
		this.view = view;
		this.canApprove = canApprove;
		createControls();
	}

	private void createControls() {
		if (canApprove) {
			createApprovePanel();
		}
		createVotePanel();
		createMessagePanel();

		this.add(votesPanel);
		// this.setSpacing(4);
		// this.setCellWidth(votesPanel, "6%");
		this.addStyleName("votes-message-panel");
		this.add(messageLabel);
		// this.setCellVerticalAlignment(messageLabel,
		// HasAlignment.ALIGN_MIDDLE);
	}

	private void createMessagePanel() {
		messageLabel = new Label(clientLocalMessage.getValue());
		messageLabel.addStyleName("message_label");
	}

	private void createVotePanel() {
		votesPanel = new StyledPanel("votesPanel");
		upVotesPanel = new StyledPanel("upVotesPanel");
		upVotesLengthLabel = new Label(String.valueOf(clientLocalMessage
				.getVotes()));
		upImage = new ImageButton(Accounter.getFinanceImages().upArrow(),
				"upload");
		upImage.addStyleName("image_button");

		upImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				voteLocalMessage();
			}
		});

		upVotesPanel.add(upImage);
		upVotesPanel.add(upVotesLengthLabel);

		votesPanel.add(upVotesPanel);

		upVotesPanel.addStyleName("up_image_panel");
		upVotesLengthLabel.addStyleName("up_label");

		// votesPanel.setSpacing(4);
		votesPanel.addStyleName("votes_panel");
	}

	private void createApprovePanel() {
		approveButton = new RadioButton(String.valueOf(clientMessage.getId()));
		approvePanel = new FlowPanel();
		approveButton.setValue(clientLocalMessage.isApproved());

		approveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				approveMessage();
			}
		});

		approvePanel.add(approveButton);
		this.add(approvePanel);
		approvePanel.getElement().getParentElement()
				.addClassName("approve-img");
	}

	protected void approveMessage() {
		async.setApprove(clientLocalMessage.getId(), true,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							approveButton.setValue(result);
							view.refreshPager();
							view.updateListData();
						}
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
	}

	protected void voteLocalMessage() {
		async.vote(clientLocalMessage.getId(), new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showError(Global.get().messages()
						.unableToVoteThisTranslation(caught.toString()));
			}

			@Override
			public void onSuccess(Boolean result) {
				view.refreshPager();
				view.updateListData();
			}
		});
	}

}
