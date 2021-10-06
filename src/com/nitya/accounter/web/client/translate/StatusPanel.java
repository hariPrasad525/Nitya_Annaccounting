package com.nitya.accounter.web.client.translate;

import com.google.gwt.user.client.ui.FlowPanel;

public class StatusPanel extends FlowPanel {
	// public FlexTable table;
	// public TranslationWizard translationWizard;
	// private ArrayList<Status> list = new ArrayList<Status>();
	//
	public StatusPanel(TranslationWizard translationWizard) {
		// this.setWidth("100%");
		// this.translationWizard = translationWizard;
		// createControls();
	}
	//
	// private void createControls() {
	// table = new FlexTable();
	//
	// // TranslationEntryPoint.translateService
	// // .getStatus(new AsyncCallback<ArrayList<Status>>() {
	// //
	// // @Override
	// // public void onSuccess(ArrayList<Status> result) {
	// // list = result;
	// // initTable();
	// // }
	// //
	// // @Override
	// // public void onFailure(Throwable caught) {
	// // // TODO Auto-generated method stub
	// //
	// // }
	// // });
	// this.add(table);
	// }
	//
	// protected void initTable() {
	// for (int i = 0; i < list.size(); i++) {
	// final Status status = list.get(i);
	// Anchor anchor = new Anchor(status.getLang());
	// anchor.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent event) {
	// TranslationDetailPanel translationDetailPanel = new
	// TranslationDetailPanel(
	// translationWizard, status.getLang());
	// translationDetailPanel.setWidth("100%");
	// translationWizard.remove(StatusPanel.this);
	// translationWizard.add(translationDetailPanel);
	// }
	// });
	// StyledPanel translateProgressPanel = new StyledPanel();
	// Label translatedProgress = new Label("Translated Messages : "
	// + status.getTranslated());
	// // translateProgressPanel.setWidth("500px");
	// String translateWidth = (500 / status.getTotal())
	// * status.getTranslated() + "px";
	// translatedProgress.setWidth(translateWidth);
	//
	// StyledPanel approveProgressPanel = new StyledPanel();
	// Label approvedProgress = new Label("Approved Messages : "
	// + status.getApproved());
	// approveProgressPanel.setWidth("500px");
	// String approvedWidth = (500 / status.getTotal())
	// * status.getApproved() + "px";
	// approvedProgress.setWidth(approvedWidth);
	//
	// table.setWidget(i, 0, anchor);
	// table.setWidget(i, 1, translateProgressPanel);
	// table.setWidget(i, 2, approveProgressPanel);
	// }
	//
	// }
}
