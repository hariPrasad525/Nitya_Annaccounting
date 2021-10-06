/**
 * 
 */
package com.vimukti.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientReminder;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterCRUDService extends RemoteService {

	long create(IAccounterCore coreObject) throws AccounterException;

	long update(IAccounterCore coreObject) throws AccounterException;

	boolean delete(AccounterCoreType type, long stringID)
			throws AccounterException;

	long inviteUser(IAccounterCore coreObject) throws AccounterException;

	long updateUser(IAccounterCore coreObject) throws AccounterException;

	boolean deleteUser(IAccounterCore deletableUser, String senderEmail)
			throws AccounterException;

	boolean updateCompanyPreferences(ClientCompanyPreferences preferences)
			throws AccounterException;

	Boolean voidTransaction(AccounterCoreType accounterCoreType, long id)
			throws AccounterException;

	boolean deleteTransaction(AccounterCoreType accounterCoreType, long stringID)
			throws AccounterException;

	boolean canEdit(AccounterCoreType accounterCoreType, long stringID)
			throws AccounterException;

	Long updateCompany(ClientCompany clientCompany) throws AccounterException;

	ArrayList<Client1099Form> get1099Vendors(int selected)
			throws AccounterException;

	Client1099Form get1099InformationByVendor(long vendorId);

	long createNote(long transactionId, String details)
			throws AccounterException;

	boolean deleteTransactionFromDb(IAccounterCore obj)
			throws AccounterException;

	boolean createOrSkipTransactions(ArrayList<ClientReminder> records,
			boolean isCreate) throws AccounterException;

	boolean doCreateIssuePaymentEffect(ClientIssuePayment obj)
			throws AccounterException;
}
