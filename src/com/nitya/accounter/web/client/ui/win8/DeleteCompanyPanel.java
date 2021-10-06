package com.nitya.accounter.web.client.ui.win8;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.nitya.accounter.web.client.core.CompanyDetails;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.WebsocketAccounterInitialiser;

public class DeleteCompanyPanel extends FlowPanel {
	RadioButton deleteRadio;
	Label warningTextLabel;
	Button deleteButton;
	Button cancelbutton;
	CompanyDetails company;
	WebsocketAccounterInitialiser accounterInitialiser;

	public DeleteCompanyPanel(CompanyDetails company,
			WebsocketAccounterInitialiser accounterInitialiser) {
		getElement().setId("deleteCompanyPanel");
		this.accounterInitialiser = accounterInitialiser;
		this.company = company;
		createControls();
	}

	private void createControls() {
		deleteRadio = new RadioButton(Accounter.getMessages()
				.deletecompanyfromaccount());
		deleteRadio
				.setTitle(Accounter.getMessages().deletecompanyfromaccount());
		warningTextLabel = new Label(Accounter.getMessages()
				.deletecompanyWarningMsg());
		deleteButton = new Button(Accounter.getMessages().delete());
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (deleteRadio.getValue()) {
					Accounter.createWindowsRPCService().deleteCompany(
							company.getCompanyId(), true,
							new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
									Accounter
											.showError("Company deletion failed");
								}

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										onDeleteCompany();
									}

									else {
										Accounter
												.showError("Company deletion failed");
									}
								}
							});
				} else {
					Accounter.showError(Accounter.getMessages().pleaseSelect(
							"option"));
				}
			}
		});
		cancelbutton = new Button(Accounter.getMessages().cancel());
		cancelbutton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				Accounter.createWindowsRPCService().getCompanies(
						new AsyncCallback<ArrayList<CompanyDetails>>() {

							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(
									ArrayList<CompanyDetails> result) {
								accounterInitialiser
										.showView(new CompaniesPanel(result,
												accounterInitialiser));
							}

						});

			}
		});
		this.add(deleteRadio);
		this.add(warningTextLabel);
		this.add(deleteButton);
		this.add(cancelbutton);
	}

	private void onDeleteCompany() {
		Accounter.createWindowsRPCService().getCompanies(
				new AsyncCallback<ArrayList<CompanyDetails>>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ArrayList<CompanyDetails> result) {
						accounterInitialiser.showView(new CompaniesPanel(
								result, accounterInitialiser));
					}
				});

	}

}
