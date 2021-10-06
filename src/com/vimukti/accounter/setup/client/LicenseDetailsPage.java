package com.vimukti.accounter.setup.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class LicenseDetailsPage extends AbstractPage {

	private HTML serverIDLabel;
	private TextArea licenseKey;

	public LicenseDetailsPage() {
		super();
		SetupHome.getSetupService().getServerID(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String result) {
				serverIDLabel.setHTML("<b>" + result + "</b>");
			}
		});
	}

	@Override
	protected Widget createControls() {
		FlexTable table = new FlexTable();

		this.serverIDLabel = new HTML();
		this.licenseKey = new TextArea();
		licenseKey.setSize("350px", "130px");

		table.setWidget(0, 0, createTitle("Server ID"));
		table.setWidget(0, 1, serverIDLabel);
		table.setWidget(2, 0, createTitle("License Key", true));
		table.setWidget(
				2,
				1,
				addTag(licenseKey,
						"If you are a new user, you can <a target='_blank' href='http://app.annaccounting.com/main/managelicense?gen=true'>generate an license key.</a> <br/> Or, if you have already one, <a target='_blank' href='http://www.annaccounting.com/main/managelicense'>you can retrieve it.</a>"));

		table.getCellFormatter().setVerticalAlignment(1, 0,
				HasVerticalAlignment.ALIGN_TOP);
		return table;
	}

	@Override
	public int getPageNo() {
		return 2;
	}

	@Override
	public String getPageTitle() {
		return "License Details";
	}

	@Override
	public void validate(ValidationResult result) {
		String text = licenseKey.getText();
		if (text == null || text.trim().isEmpty()) {
			result.addError(licenseKey, "Please enter license key");
		}

	}

	@Override
	protected void savePage(final Callback<Boolean, Throwable> callback) {
		SetupHome.getSetupService().verifyLicense(serverIDLabel.getText(),
				licenseKey.getText().trim(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(Boolean result) {
						callback.onSuccess(result);
					}
				});
	}

}
