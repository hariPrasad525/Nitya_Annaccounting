package com.nitya.accounter.web.client.translate;

import com.google.gwt.user.client.ui.FlowPanel;

public class TranslationDetailPanel extends FlowPanel {
	//
	// private TranslationWizard translationWizard;
	// private StyledPanel verticalPanel;
	// private ClientMessage clientMessage;
	// private String language;
	//
	// public TranslationDetailPanel(TranslationWizard translationWizard,
	// String string) {
	// this.setWidth("100%");
	// this.translationWizard = translationWizard;
	// this.language = string;
	// initView(0);
	// }
	//
	// private void initView(long lastMessageId) {
	// // if (lastMessageId < 0) {
	// // TranslationEntryPoint.translateService.getMessage(language,
	// // clientMessage.getId(), new AsyncCallback<ClientMessage>() {
	// //
	// // @Override
	// // public void onSuccess(ClientMessage result) {
	// // clientMessage = result;
	// // createControls();
	// // }
	// //
	// // @Override
	// // public void onFailure(Throwable caught) {
	// // // TODO Auto-generated method stub
	// //
	// // }
	// // });
	// // } else {
	// // TranslationEntryPoint.translateService.getNext(language,
	// // lastMessageId, new AsyncCallback<ClientMessage>() {
	// //
	// // @Override
	// // public void onSuccess(ClientMessage result) {
	// // clientMessage = result;
	// // createControls();
	// // }
	// //
	// // @Override
	// // public void onFailure(Throwable caught) {
	// // // TODO Auto-generated method stub
	// //
	// // }
	// // });
	// // }
	// }
	//
	// private void createControls() {
	//
	// if (clientMessage == null) {
	// return;
	// }
	//
	// verticalPanel = new StyledPanel();
	// verticalPanel.setHorizontalAlignment(ALIGN_LEFT);
	//
	// Label label = new Label(language);
	//
	// StyledPanel buttonPanel = new StyledPanel();
	// Button backButton = new Button("Back");
	// backButton.addStyleName("backnextbutton");
	// backButton.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent event) {
	// StatusPanel statusPanel = new StatusPanel(translationWizard);
	// statusPanel.setWidth("100%");
	// translationWizard.remove(TranslationDetailPanel.this);
	// translationWizard.add(statusPanel);
	// }
	// });
	//
	// Button nextButton = new Button("Next");
	// nextButton.addStyleName("backnextbutton");
	// nextButton.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent event) {
	// long lastMessageId = 0;
	// if (clientMessage != null) {
	// lastMessageId = clientMessage.getId();
	// }
	// reload(lastMessageId);
	// }
	// });
	//
	// buttonPanel.add(label);
	// buttonPanel.add(backButton);
	// buttonPanel.add(nextButton);
	// buttonPanel.setWidth("100%");
	// buttonPanel.setCellHorizontalAlignment(nextButton, ALIGN_RIGHT);
	// verticalPanel.add(buttonPanel);
	//
	// Label valueLabel = new Label(clientMessage.getValue());
	// valueLabel.addStyleName("message-default-value");
	// verticalPanel.add(valueLabel);
	//
	// ArrayList<ClientLocalMessage> localMessages = clientMessage
	// .getLocalMessages();
	//
	// for (int i = 0; i < localMessages.size(); i++) {
	// final ClientLocalMessage message = localMessages.get(i);
	// Label value = new Label(message.getValue());
	// Label up = new Label("" + message.getUps());
	// Label down = new Label("" + message.getDowns());
	//
	// Label upButton = new Label();
	// upButton.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent event) {
	// vote(message.getId(), true);
	// }
	// });
	//
	// Label downButton = new Label();
	// downButton.addStyleName("downArrowicon");
	// downButton.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent event) {
	// vote(message.getId(), false);
	// }
	// });
	//
	// FlowPanel flowPanel = new FlowPanel();
	// FlowPanel upbuttonPanel = new FlowPanel();
	// upbuttonPanel.addStyleName("upButton");
	// FlowPanel downbuttonPanel = new FlowPanel();
	// downbuttonPanel.addStyleName("downButton");
	//
	// upbuttonPanel.add(upButton);
	// upbuttonPanel.add(up);
	//
	// downbuttonPanel.add(down);
	// downbuttonPanel.add(downButton);
	//
	// Label approveButton = new Label();
	// approveButton.addStyleName("approve-button");
	//
	// Label notApproveButton = new Label();
	// notApproveButton.addStyleName("not-approve-button");
	//
	// flowPanel.add(approveButton);
	// flowPanel.add(upbuttonPanel);
	// flowPanel.add(value);
	// value.addStyleName("message-value");
	// flowPanel.add(downbuttonPanel);
	// flowPanel.setSize("100%", "100%");
	//
	// verticalPanel.add(flowPanel);
	// }
	//
	// final StyledPanel valuePanel = new StyledPanel();
	//
	// final TextArea userValue = new TextArea();
	// userValue.setWidth("100%");
	// final Anchor anchor = new Anchor("Add Another");
	// Button addButton = new Button("Add");
	// addButton.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent event) {
	// String value = userValue.getValue();
	// if (value == null || value.equals("")) {
	//
	// } else {
	// TranslationEntryPoint.translateService.addTranslation(
	// clientMessage.getId(), language, value,
	// new AsyncCallback<Boolean>() {
	//
	// @Override
	// public void onSuccess(Boolean result) {
	// reload(-1);
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	// verticalPanel.remove(valuePanel);
	// verticalPanel.add(anchor);
	//
	// }
	// });
	// valuePanel.add(userValue);
	// valuePanel.add(addButton);
	// valuePanel.addStyleName("translation-add-data");
	//
	// anchor.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent event) {
	// verticalPanel.remove(anchor);
	// verticalPanel.add(valuePanel);
	// }
	// });
	// verticalPanel.add(anchor);
	// verticalPanel.setWidth("100%");
	// verticalPanel.addStyleName("translation-detail");
	// verticalPanel.setCellHorizontalAlignment(anchor, ALIGN_RIGHT);
	// this.add(verticalPanel);
	// this.setSize("100%", "");
	// }
	//
	// protected void vote(long id, boolean b) {
	// TranslationEntryPoint.translateService.vote(id, b,
	// new AsyncCallback<Boolean>() {
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// System.out.println("");
	//
	// }
	//
	// @Override
	// public void onSuccess(Boolean result) {
	// reload(-1);
	// }
	// });
	// }
	//
	// private void reload(long lastMessageId) {
	// this.remove(verticalPanel);
	// initView(lastMessageId);
	// }
}
