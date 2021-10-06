package com.nitya.accounter.web.client.ui.win8;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.CompanyDetails;
import com.nitya.accounter.web.client.core.SubscriptionDetails;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.AccounterInitialiser;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.nitya.accounter.web.client.ui.core.IButtonBar;
import com.nitya.accounter.web.client.ui.core.URLLauncher;

/**
 * 
 * @author Devaraju.K
 * 
 * @param <T>
 */
public class CompaniesPanel extends FlowPanel {

	List<CompanyDetails> comapniesList = new ArrayList<CompanyDetails>();

	WebsocketAccounterInitialiser accounterInitialiser;

	String selected_companyName = "";

	HTML title;

	protected SubscriptionDetails subscriptionDetails;

	public CompaniesPanel(List<CompanyDetails> companiesList,
			WebsocketAccounterInitialiser accounterInitialiser) {
		init(companiesList, accounterInitialiser);
	}

	public CompaniesPanel(
			final WebsocketAccounterInitialiser accounterInitialiser) {
		Accounter.createWindowsRPCService().getCompanies(
				new AsyncCallback<ArrayList<CompanyDetails>>() {

					@Override
					public void onSuccess(ArrayList<CompanyDetails> result) {
						init(result, accounterInitialiser);
					}

					@Override
					public void onFailure(Throwable caught) {
						unableToShowTheView();
					}
				});
	}

	protected void unableToShowTheView() {
		Accounter.showError(Global.get().messages().unableToshowtheview());
	}

	protected void init(List<CompanyDetails> companiesList,
			WebsocketAccounterInitialiser accounterInitialiser) {
		getElement().setId("companiesPanel");
		this.comapniesList = companiesList;
		this.accounterInitialiser = accounterInitialiser;
		createControls();
		IButtonBar appBar = GWT.create(IButtonBar.class);
		appBar.remove();
	}

	/**
	 * 
	 */
	private void createControls() {

		String[] credentials = accounterInitialiser
				.autoLogin(WebsocketAccounterInitialiser.PASSWORD_CRED_RESOURCE);
		final String email = credentials[0];

		title = new HTML("<h2>" + Accounter.getMessages().companieslist()
				+ "</h2>");
		title.setStyleName("label-title");
		Button createNewCompanyButton = new Button(Accounter.getMessages()
				.createNewCompany());
		createNewCompanyButton.setStyleName("success");
		createNewCompanyButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadCompany(0);
			}
		});

		StyledPanel companiesListStyledPanel = new StyledPanel("companies-list");

		for (final CompanyDetails com : comapniesList) {
			Anchor anchor = new Anchor(com.getCompanyName());
			anchor.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					selected_companyName = com.getCompanyName();
					loadCompany(com.getCompanyId());
				}

			});
			StyledPanel anchorPanel = new StyledPanel("companyNameAnchor");
			anchorPanel.add(anchor);
			Button deleteComapny = new Button(Accounter.getMessages()
					.deletecompany());
			deleteComapny.setStyleName("warning");
			deleteComapny.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					accounterInitialiser.showView(new DeleteCompanyPanel(com,
							accounterInitialiser));
				}
			});
			anchorPanel.add(deleteComapny);
			companiesListStyledPanel.add(anchorPanel);
		}
		Button logOut = new Button(Accounter.getMessages().logout());
		logOut.setStyleName("cancel");
		final AccounterAsyncCallback<Boolean> logoutCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				discardCredentials(AccounterInitialiser.PASSWORD_CRED_RESOURCE);
				CompaniesPanel.this.removeFromParent();
				accounterInitialiser.showView(new LoginPanel(
						accounterInitialiser));

			}

		};
		logOut.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Accounter.createWindowsRPCService().logout(logoutCallback);
			}
		});

//		final URLLauncher lancher = GWT.create(URLLauncher.class);

//		final Button goPremium = new Button("Go Premium");
//		goPremium.setStyleName("go-premium");
//		goPremium.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				lancher.launch("http://app.annaccounting.com/main/gopremium?emailId="
//						+ email);
//			}
//		});

//		final Button goPremiumTrail = new Button(
//				"Go Premium (30 days free trail)");
//		goPremiumTrail.setStyleName("go-premium-trail");
//		goPremiumTrail.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				lancher.launch("http://app.annaccounting.com/main/gopremium?emailId="
//						+ email);
//			}
//		});

//		final Button manageSubscription = new Button("Manage Subscription");
//		manageSubscription.setStyleName("go-premium");
//		manageSubscription.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				lancher.launch("http://www.annaccounting.com/main/subscriptionmanagement");
//			}
//		});

		StyledPanel mainPanel = new StyledPanel("main-panel");
		mainPanel.add(title);
		mainPanel.add(companiesListStyledPanel);

		final StyledPanel buttonsPanel = new StyledPanel("app-bar");
		buttonsPanel.add(logOut);
		buttonsPanel.add(createNewCompanyButton);
	
		// buttonsPanel.add(deleteAccount);

		mainPanel.add(buttonsPanel);
		add(mainPanel);

//		Accounter.createWindowsRPCService().getSubscriptionDetails(email,
//				new AccounterAsyncCallback<SubscriptionDetails>() {
//
//					@Override
//					public void onException(AccounterException exception) {
//						Accounter.showError(exception.getMessage());
//					}
//
//					@Override
//					public void onResultSuccess(SubscriptionDetails result) {
//						subscriptionDetails = result;
//						if (subscriptionDetails.isPaidUser()) {
//							buttonsPanel.insert(manageSubscription, 0);
//						} else {
//							if (!subscriptionDetails.isFreeTrailDone()) {
//								buttonsPanel.insert(goPremiumTrail, 0);
//							} else {
//								buttonsPanel.insert(goPremium, 0);
//							}
//						}
//
//					}
//				});

	}

	protected native void discardCredentials(String resource) /*-{
		try {
			var passwordVault = new Windows.Security.Credentials.PasswordVault;
			var pcs = passwordVault.findAllByResource(resource);
			if (pcs != null) {
				for (i = 0; i < pcs.size; i++) {
					passwordVault.remove(pcs.getAt(i));
				}
			}
		} catch (e) {
		}
	}-*/;

	private void loadCompany(final long comId) {
		CompaniesPanel.this.removeFromParent();
		accounterInitialiser.loadCompany(comId);
	}

}
