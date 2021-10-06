package com.vimukti.accounter.web.client.ui.customers;

import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.BrandingThemeCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.EmailViewAction;

public class EmailThemeComboDialog extends BrandingThemeComboDialog {
	private BrandingThemeCombo brandingThemeTypeCombo;
	private ClientTransaction clientTransaction;
	private ClientBrandingTheme brandingTheme;

	public EmailThemeComboDialog(String selectThemes, String desc,
			ClientTransaction data) {
		super(selectThemes, desc);
		this.getElement().setId("EmailThemeComboDialog");
		this.clientTransaction = data;
		createControls();
	}

	private void createControls() {
		brandingThemeTypeCombo = new BrandingThemeCombo(messages.selectTheme());
		brandingTheme = new ClientBrandingTheme();
		brandingThemeTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientBrandingTheme>() {

					@Override
					public void selectedComboBoxItem(
							ClientBrandingTheme selectItem) {
						brandingTheme = selectItem;
					}
				});
		brandingThemeTypeCombo.setThemeDialog(this);
		brandingTheme = Accounter.getCompany().getBrandingTheme().get(1);
		brandingThemeTypeCombo.setComboItem(brandingTheme);

		setBodyLayout(brandingThemeTypeCombo);
	}

	@Override
	protected boolean onOK() {
		if (brandingThemeTypeCombo.getSelectedValue().equals(null)) {
			brandingThemeTypeCombo.setSelected(messages.standardTheme());
		}
		showEmailView();
		return true;
	}

	private void showEmailView() {

		new EmailViewAction().run(clientTransaction, brandingTheme.getID(),
				false);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
