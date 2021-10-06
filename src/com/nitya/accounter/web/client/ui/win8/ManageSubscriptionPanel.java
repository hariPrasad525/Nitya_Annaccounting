package com.nitya.accounter.web.client.ui.win8;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.nitya.accounter.web.client.core.SubscriptionDetails;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.WebsocketAccounterInitialiser;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.IButtonBar;
import com.nitya.accounter.web.client.ui.core.URLLauncher;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;

public class ManageSubscriptionPanel extends FlowPanel {

	private WebsocketAccounterInitialiser accounterInitialiser;
	private SubscriptionDetails subscripton;
	private HTML errors;

	public ManageSubscriptionPanel(
			WebsocketAccounterInitialiser accounterInitialiser,
			SubscriptionDetails subscripton) {
		this.accounterInitialiser = accounterInitialiser;
		this.subscripton = subscripton;
		init();
	}

	private void init() {
		getElement().setId("manageSubscriptionPanel");
		createControls();
		IButtonBar appBar = GWT.create(IButtonBar.class);
		appBar.remove();
	}

	private void createControls() {

		HTML title = new HTML("<h2>"
				+ Accounter.getMessages().subscriptionManagement() + "</h2>");
		title.setStyleName("label-title");

		FlowPanel errorPane = new FlowPanel();
		errorPane.addStyleName("subs-errorpane");
		this.add(errorPane);

		this.errors = new HTML();
		errors.addStyleName("subs-errors");
		errorPane.add(errors);

		FlowPanel body = new FlowPanel();
		this.add(body);
		body.addStyleName("subscription-body");

		LabelItem expiresOn = new LabelItem("Subscription Expire Date",
				"sub-expireson");
		body.add(expiresOn);
		expiresOn.setValue(subscripton.getExpiresOn());

		int subscriptionType = subscripton.getSubscriptionType();

		SelectCombo subType = new SelectCombo("Subscription Type");
		body.add(subType);
		ArrayList<String> types = new ArrayList<String>();
		types.add("One user monthly");
		types.add("One user yearly");
		types.add("2 users monthly");
		types.add("2 users yearly");
		types.add("5 users monthly");
		types.add("5 users yearly");
		types.add("Unlimited Users monthly");
		types.add("Unlimited Users yearly");
		subType.initCombo(types);

		subType.setSelectedItem((subscriptionType * 2) - 1);

		FlowPanel buttonPane = new FlowPanel();
		buttonPane.addStyleName("subscription-btnpanel");
		body.add(buttonPane);

		// IF Subscription is One User or Two User
		if (subscriptionType == 2 || subscriptionType == 3) {
			StringBuffer buff = new StringBuffer();
			for (String email : subscripton.getAllowedEmails()) {
				buff.append(email);
				buff.append(";");
			}

			final TextAreaItem members = new TextAreaItem("User Emails",
					"user-emails");
			this.add(members);
			members.setValue(buff.toString());

			Button update = new Button("Update Subscription");
			buttonPane.add(update);
			update.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					updateSubscription(members.getValue());
				}

			});
		}

		Button upgrade = new Button("Upgrade Premium");
		buttonPane.add(upgrade);
		upgrade.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				URLLauncher lancher = GWT.create(URLLauncher.class);
				lancher.launch("http://app.annaccounting.com/main/gopremium?emailId="
						+ Accounter.getUser().getEmail());
			}
		});

		Anchor comapanies = new Anchor("Comapanies");
		body.add(comapanies);
		comapanies.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				accounterInitialiser.showView(new CompaniesPanel(
						accounterInitialiser));
			}
		});

	}

	private void updateSubscription(String emails) {
		errors.setText("");
		HashSet<String> newEmails = new HashSet<String>();
		for (String email : emails.split("\n\r")) {
			if (email == null || email.isEmpty()) {
				continue;
			}
			if (!validateEmail(email)) {
				showError("Invalide EmailId : " + email);
				return;
			}
			newEmails.add(email);
		}
		if (subscripton.getAllowedEmails().equals(newEmails)) {
			return;
		}

		URLLauncher lancher = GWT.create(URLLauncher.class);
		lancher.launch(URL
				.encode("http://app.annaccounting.com/main/subsdeleteuserconform?userMailds="
						+ emails));

	}

	private void showError(String error) {
		errors.setText(error);
	}

	private native boolean validateEmail(String email) /*-{
		var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
		if (emailPattern.test(elementValue)) {
			return true;
		} else {
			return false;
		}
		return false;
	}-*/;

}
