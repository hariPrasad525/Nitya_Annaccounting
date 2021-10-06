package com.nitya.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.Client1099Form;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientIssuePayment;
import com.nitya.accounter.web.client.core.ClientReminder;
import com.nitya.accounter.web.client.core.IAccounterCore;

/**
 * 
 * @author Fernandez
 * 
 */
public interface IAccounterCRUDServiceAsync {

	void create(IAccounterCore coreObject, AsyncCallback<Long> callback);

	void update(IAccounterCore coreObject, AsyncCallback<Long> callback);

	void delete(AccounterCoreType type, long id, AsyncCallback<Boolean> callback);

	void updateCompanyPreferences(ClientCompanyPreferences preferences,
			AsyncCallback<Boolean> callback);

	void voidTransaction(AccounterCoreType accounterCoreType, long id,
			AsyncCallback<Boolean> callback);

	void deleteTransaction(AccounterCoreType accounterCoreType, long id,
			AsyncCallback<Boolean> callback);

	void canEdit(AccounterCoreType accounterCoreType, long id,
			AsyncCallback<Boolean> callback);

	void updateCompany(ClientCompany clientCompany, AsyncCallback<Long> callback);

	void inviteUser(IAccounterCore coreObject, AsyncCallback<Long> callback);

	void updateUser(IAccounterCore coreObject, AsyncCallback<Long> callback);

	void deleteUser(IAccounterCore deletableUser, String senderEmail,
			AsyncCallback<Boolean> callback);

	void get1099Vendors(int selected,
			AsyncCallback<ArrayList<Client1099Form>> callback);

	void get1099InformationByVendor(long vendorId,
			AsyncCallback<Client1099Form> callback);

	void createNote(long transactionId, String details,
			AsyncCallback<Long> callback);

	void deleteTransactionFromDb(IAccounterCore obj,
			AsyncCallback<Boolean> transactionCallBack);

	void createOrSkipTransactions(ArrayList<ClientReminder> records,
			boolean isCreate, AsyncCallback<Boolean> transactionCallBack);

	void doCreateIssuePaymentEffect(ClientIssuePayment obj,
			AsyncCallback<Boolean> transactionCallBack);
}
