package com.vimukti.accounter.setup.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.setup.client.ValidationResult.Error;
import com.vimukti.accounter.setup.client.core.DatabaseConnection;

public class SetupHome implements EntryPoint {

	private static final String SETUP_ENTRY_POINT = "/do/setup/service";

	private static ISetupServiceAsync setupService = null;

	SimplePanel mainPanel = null;

	private Button testConnection;

	private AbstractPage curreenctPage;

	private VerticalPanel errorsPane;

	private VerticalPanel informationPane;

	private Button nextBtn;

	@Override
	public void onModuleLoad() {
		mainPanel = new SimplePanel();
		mainPanel.setStyleName("mainPage");
		HorizontalPanel bottomPane = new HorizontalPanel();
		bottomPane.setStyleName("footer");
		createNavigation(bottomPane);
		VerticalPanel pane = new VerticalPanel();
		pane.setStyleName("mainBody");
		this.errorsPane = new VerticalPanel();
		errorsPane.setStyleName("errors");
		this.informationPane = new VerticalPanel();
		informationPane.setStyleName("infos");
		pane.add(errorsPane);
		pane.add(informationPane);
		pane.add(mainPanel);
		pane.add(bottomPane);
		RootPanel.get("mainBody").add(pane);
		changePage(getPageNo() + 1);
	}

	private void createNavigation(HorizontalPanel pane) {
		pane.setWidth("100%");

		this.testConnection = new Button("Test Connection");
		testConnection.setStyleName("testConnBtn");
		pane.add(testConnection);

		testConnection.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clearErrors();
				ValidationResult result = new ValidationResult();
				curreenctPage.validate(result);
				if (!result.haveErrors() && !result.haveWarnings()) {
					DatabaseConnection databaseDetails = ((DatabaseDetailsPage) curreenctPage)
							.getDatabaseDetails();
					testDatabaseConnection(databaseDetails);
				} else {
					showErrors(result);
				}
			}
		});

		this.nextBtn = new Button("Next");
		nextBtn.setStyleName("nextBtn");
		pane.add(nextBtn);

		nextBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clearErrors();
				nextBtn.setEnabled(false);
				if (curreenctPage != null) {
					curreenctPage
							.saveData(new Callback<Boolean, ValidationResult>() {

								@Override
								public void onSuccess(Boolean result) {
									nextBtn.setEnabled(true);
									if (!result) {
										// TODO
									}
									int pageNo = curreenctPage.getPageNo();
									pageNo++;
									if (curreenctPage.isLastPage()) {
										redirect("/main/companies");
									} else {
										changePage(pageNo);
									}
								}

								@Override
								public void onFailure(ValidationResult reason) {
									nextBtn.setEnabled(true);
									showErrors(reason);
								}

							});

				}

			}
		});

		pane.setCellHorizontalAlignment(testConnection,
				HasHorizontalAlignment.ALIGN_RIGHT);

		pane.setCellHorizontalAlignment(nextBtn,
				HasHorizontalAlignment.ALIGN_RIGHT);
	}

	protected void testDatabaseConnection(DatabaseConnection detabaseDetails) {
		testConnection.setEnabled(false);
		nextBtn.setEnabled(false);
		getSetupService().testDBConnection(detabaseDetails,
				new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						testConnection.setEnabled(true);
						nextBtn.setEnabled(true);
						ValidationResult vResult = new ValidationResult();
						vResult.addInformation(result);
						showErrors(vResult);
					}

					@Override
					public void onFailure(Throwable caught) {
						testConnection.setEnabled(true);
						nextBtn.setEnabled(true);
						ValidationResult result = new ValidationResult();
						result.addError(caught, caught.getMessage());
						showErrors(result);
					}
				});
	}

	private void showErrors(ValidationResult reason) {
		clearErrors();
		for (Error err : reason.getErrors()) {
			errorsPane.setVisible(true);
			errorsPane.add(new HTML("Error : " + err.getMessage()));
		}
		for (String info : reason.getInformations()) {
			informationPane.setVisible(true);
			informationPane.add(new HTML("Info : " + info));
		}

	}

	private void clearErrors() {
		errorsPane.clear();
		informationPane.clear();
		errorsPane.setVisible(false);
		informationPane.setVisible(false);
	}

	protected void changePage(int pageNo) {
		clearErrors();
		this.curreenctPage = AbstractPage.pageByNo(this, pageNo);
		if (curreenctPage.isLastPage()) {
			nextBtn.setText("Finish");
		}
		mainPanel.clear();
		mainPanel.setWidget(curreenctPage);
	}

	public static ISetupServiceAsync getSetupService() {
		if (setupService == null) {
			setupService = GWT.create(ISetupService.class);
			((ServiceDefTarget) setupService)
					.setServiceEntryPoint(SETUP_ENTRY_POINT);
		}
		return setupService;
	}

	public void showOrHideTestConnection(boolean isShow) {
		testConnection.setVisible(isShow);
	}

	native void redirect(String url)
	/*-{
		$wnd.location.replace(url);
	}-*/;

	private native int getPageNo() /*-{
		return $wnd.pageNo;
	}-*/;
}
