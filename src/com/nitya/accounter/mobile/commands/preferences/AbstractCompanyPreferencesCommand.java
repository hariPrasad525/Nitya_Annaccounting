package com.nitya.accounter.mobile.commands.preferences;

import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.commands.AbstractCommand;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.server.FinanceTool;
import com.nitya.accounter.web.server.OperationContext;
import com.nitya.accounter.web.server.managers.CompanyManager;

public abstract class AbstractCompanyPreferencesCommand extends AbstractCommand {

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void savePreferences(Context context,
			ClientCompanyPreferences preferences) {
		try {
			CompanyManager companyManager = new FinanceTool()
					.getCompanyManager();
			ClientCompany clientCompany = companyManager.getClientCompany(
					context.getEmailId(), getCompanyId());
			clientCompany.setRegisteredAddress(getRegisteredAddress());
			OperationContext opContext = new OperationContext(getCompanyId(),
					clientCompany, context.getEmailId());

			clientCompany.setPreferences(preferences);
			companyManager.updateCompany(opContext);

		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	protected ClientAddress getRegisteredAddress() {
		return toClientAddress(getCompany().getRegisteredAddress());
	}

	@Override
	protected String getWelcomeMessage() {
		return "Updating Company Preferences";
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToUpdate(getMessages().companyPreferences());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().updateSuccessfully(
				getMessages().companyPreferences());
	}
}
