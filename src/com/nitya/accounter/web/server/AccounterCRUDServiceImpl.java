package com.nitya.accounter.web.server;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.IAccounterServerCore;
import com.nitya.accounter.core.ObjectConvertUtil;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.Util;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.IAccounterCRUDService;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.Client1099Form;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientIssuePayment;
import com.nitya.accounter.web.client.core.ClientReminder;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.client.core.ClientUserInfo;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Fernandez This Service is for All CREATE UPDATE DELETE Operations
 * 
 */

public class AccounterCRUDServiceImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterCRUDService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccounterCRUDServiceImpl() {
		super();
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {

		super.service(arg0, arg1);

	}

	@Override
	public long create(IAccounterCore coreObject) throws AccounterException {

		String clientClassSimpleName = coreObject.getObjectType()
				.getClientClassSimpleName();

		OperationContext context = new OperationContext(getCompanyId(),
				coreObject, getUserEmail());
		context.setArg2(clientClassSimpleName);

		return new FinanceTool().create(context);
	}

	@Override
	public long update(IAccounterCore coreObject) throws AccounterException {
		FinanceTool tool = getFinanceTool();

		String serverClassFullyQualifiedName = coreObject.getObjectType()
				.getServerClassFullyQualifiedName();

		OperationContext context = new OperationContext(getCompanyId(),
				coreObject, getUserEmail(), String.valueOf(coreObject.getID()),
				serverClassFullyQualifiedName);
		return tool.update(context);
	}

	@Override
	public boolean delete(AccounterCoreType type, long id)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		OperationContext opContext = new OperationContext(getCompanyId(), type,
				getUserEmail());
		opContext.setArg1(String.valueOf(id));
		opContext.setArg2(type.getClientClassSimpleName());
		return tool.delete(opContext);

	}

	@Override
	public boolean updateCompanyPreferences(ClientCompanyPreferences preferences)
			throws AccounterException {

		FinanceTool tool = getFinanceTool();
		OperationContext updateComPref = new OperationContext(getCompanyId(),
				preferences, getUserEmail());
		tool.getCompanyManager().updateCompanyPreferences(updateComPref);
		return true;
	}

	@Override
	public Long updateCompany(ClientCompany clientCompany)
			throws AccounterException {

		FinanceTool tool = getFinanceTool();
		OperationContext opContext = new OperationContext(getCompanyId(),
				clientCompany, getUserEmail());
		return tool.getCompanyManager().updateCompany(opContext);

	}

	@Override
	public Boolean voidTransaction(AccounterCoreType accounterCoreType, long id)
			throws AccounterException {

		FinanceTool tool = getFinanceTool();
		Class<?> clientClass = ObjectConvertUtil
				.getEqivalentClientClass(accounterCoreType
						.getClientClassSimpleName());

		Class<?> serverClass = ObjectConvertUtil
				.getServerEqivalentClass(clientClass);

		if (tool.canDelete(serverClass.getSimpleName(), id, getCompanyId())) {
			IAccounterServerCore serverCore = (IAccounterServerCore) loadObjectById(
					accounterCoreType.getServerClassFullyQualifiedName(), id);

			if (serverCore instanceof Transaction) {
				IAccounterCore clientObject = (IAccounterCore) new ClientConvertUtil()
						.toClientObject(serverCore,
								Util.getClientClass(serverCore));
				((ClientTransaction) clientObject)
						.setSaveStatus(ClientTransaction.STATUS_VOID);
				update(clientObject);

				return true;
			}
			return false;
		} else {
			throw new AccounterException(AccounterException.ERROR_OBJECT_IN_USE);
		}
	}

	@Override
	public boolean deleteTransaction(AccounterCoreType accounterCoreType,
			long id) throws AccounterException {
		IAccounterServerCore serverCore = (IAccounterServerCore) loadObjectById(
				accounterCoreType.getServerClassFullyQualifiedName(), id);
		if (serverCore instanceof Transaction) {
			Transaction trans = (Transaction) serverCore;
			trans.setSaveStatus(Transaction.STATUS_VOID);
			update((IAccounterCore) new ClientConvertUtil().toClientObject(
					serverCore, Util.getClientClass(serverCore)));

			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(AccounterCoreType accounterCoreType, long id)
			throws AccounterException {
		IAccounterServerCore serverCore = (IAccounterServerCore) loadObjectById(
				accounterCoreType.getServerClassFullyQualifiedName(), id);
		if (serverCore == null) {
			throw new AccounterException(
					AccounterException.ERROR_ALREADY_DELETED);
		}
		return serverCore.canEdit(serverCore, true);
	}

	@Override
	public long inviteUser(IAccounterCore coreObject) throws AccounterException {

		ClientUserInfo invitedser = (ClientUserInfo) coreObject;

		ClientUser coreUser = convertUserInfoToUser(invitedser);

		String clientClassSimpleName = coreUser.getObjectType()
				.getClientClassSimpleName();
		FinanceTool financeTool = new FinanceTool();
		OperationContext context = new OperationContext(getCompanyId(),
				coreUser, getUserEmail(), String.valueOf(coreObject.getID()),
				clientClassSimpleName);
		return financeTool.getUserManager().inviteUser(context);
	}
	
//	AutoActive
	
	private void autoActivateUser(ClientUserInfo clientUserInf) {
		Session hbSession = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = null;
		try {
			transaction = hbSession.beginTransaction();
			Client client = (Client) hbSession
					.getNamedQuery("getClient.by.mailId")
					.setParameter(EMAIL_ID, clientUserInf.getEmail())
					.uniqueResult();
			client.setActive(true);
			saveSessionEntry(client);

			// delete activation object

			Query query = hbSession
					.getNamedQuery("delete.activation.by.emailId");
			query.setParameter("emailId", clientUserInf.getEmail()
					.trim());
			int updatedRows = query.executeUpdate();
			log("No of updated rows = " + updatedRows);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}
	
	private void saveSessionEntry(Object object) {
		Session currentSession = HibernateUtil.getCurrentSession();
		currentSession.saveOrUpdate(object);
	}
	

	private ClientUser convertUserInfoToUser(ClientUserInfo clientUserInfo) {
		ClientUser clientUser = new ClientUser();
		clientUser.setCanDoUserManagement(clientUserInfo
				.isCanDoUserManagement());
		clientUser.setFirstName(clientUserInfo.getFirstName());
		clientUser.setLastName(clientUserInfo.getLastName());
		clientUser.setFullName(clientUserInfo.getFullName());
		clientUser.setEmail(clientUserInfo.getEmail());
		clientUser.setPassword(clientUserInfo.getPassword());
		clientUser.setUserRole(clientUserInfo.getUserRole());
		clientUser.setPermissions(clientUserInfo.getPermissions());
		// Setting the user permissions
		// new ServerConvertUtil().toServerObject(
		// (IAccounterServerCore) serverUser.getPermissions(),
		// (IAccounterCore) clientUserInfo.getPermissions(), session);
		clientUser.setDisplayName(clientUserInfo.getDisplayName());
		clientUser.setAdmin(clientUserInfo.isAdmin());
		clientUser.setID(clientUserInfo.getID());
		if(clientUserInfo.getPassword().isEmpty()) {
			clientUser.setActive(clientUserInfo.isActive());
		} else {
			autoActivateUser(clientUserInfo);
			clientUser.setActive(true);
		}
		return clientUser;
	}

	@Override
	public long updateUser(IAccounterCore coreObject) throws AccounterException {
		ClientUser coreUser = convertUserInfoToUser((ClientUserInfo) coreObject);
		String serverClassSimpleName = coreUser.getObjectType()
				.getServerClassFullyQualifiedName();
		FinanceTool financeTool = new FinanceTool();
		OperationContext context = new OperationContext(getCompanyId(),
				coreUser, getUserEmail(), String.valueOf(coreObject.getID()),
				serverClassSimpleName);
		return financeTool.getUserManager().updateUser(context);
	}

	@Override
	public boolean deleteUser(IAccounterCore deletableUser, String senderEmail)
			throws AccounterException {
		// ClientUserInfo deletingUser = (ClientUserInfo) deletableUser;

		// try {
		//
		// IS2SService s2sSyncProxy = getS2sSyncProxy(ServerConfiguration
		// .getMainServerDomain());
		//
		// String serverCompanyId = getCookie(BaseServlet.COMPANY_COOKIE);
		//
		// s2sSyncProxy.deleteClientFromCompany(
		// Long.parseLong(serverCompanyId), deletingUser.getEmail());
		//
		// } catch (Exception e) {
		// if (e instanceof AccounterException) {
		// throw (AccounterException) e;
		// }
		// throw new AccounterException(AccounterException.ERROR_INTERNAL);
		// }

		ClientUser coreUser = convertUserInfoToUser((ClientUserInfo) deletableUser);
		String clientClassSimpleName = coreUser.getObjectType()
				.getClientClassSimpleName();
		FinanceTool financeTool = new FinanceTool();
		OperationContext context = new OperationContext(getCompanyId(),
				coreUser, getUserEmail(),
				String.valueOf(deletableUser.getID()), clientClassSimpleName);
		return financeTool.delete(context);
	}

	@Override
	public ArrayList<Client1099Form> get1099Vendors(int selected)
			throws AccounterException {
		FinanceTool financeTool = new FinanceTool();
		return financeTool.getVendorManager().get1099Vendors(selected,
				getCompanyId());
	}

	@Override
	public Client1099Form get1099InformationByVendor(long vendorId) {
		FinanceTool financeTool = new FinanceTool();
		return financeTool.getVendorManager().get1099InformationByVendor(
				vendorId, getCompanyId());
	}

	@Override
	public long createNote(long transactionId, String noteDetails)
			throws AccounterException {
		return getFinanceTool().createNote(getCompanyId(), transactionId,
				noteDetails);
	}

	@Override
	public boolean deleteTransactionFromDb(IAccounterCore obj)
			throws AccounterException {
		IAccounterServerCore serverCore = (IAccounterServerCore) loadObjectById(
				obj.getObjectType().getServerClassFullyQualifiedName(),
				obj.getID());
		if (serverCore instanceof Transaction) {
			return getFinanceTool()
					.deleteTransactionFromDb(getCompanyId(), obj);
		}
		return false;
	}

	@Override
	public boolean createOrSkipTransactions(ArrayList<ClientReminder> records,
			boolean isCreate) throws AccounterException {
		FinanceTool tool = new FinanceTool();
		return tool.createOrSkipTransactions(records, isCreate, getCompanyId());
	}

	@Override
	public boolean doCreateIssuePaymentEffect(ClientIssuePayment obj)
			throws AccounterException {
		getFinanceTool().doCreateIssuePaymentEffect(getCompanyId(), obj);
		return false;
	}

}