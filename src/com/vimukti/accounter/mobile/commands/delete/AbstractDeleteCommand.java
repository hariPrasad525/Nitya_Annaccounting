package com.vimukti.accounter.mobile.commands.delete;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.commands.AbstractCommand;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

/**
 * 
 * @author Lingarao.R
 * 
 */
public abstract class AbstractDeleteCommand extends AbstractCommand {

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	/**
	 * delete
	 * 
	 * @param type
	 * @param id
	 * @param context
	 * @return {@link Boolean}
	 * @throws AccounterException
	 */
	public boolean delete(AccounterCoreType type, long id, Context context)
			throws AccounterException {
		FinanceTool tool = new FinanceTool();
		OperationContext opContext = new OperationContext(getCompanyId(), type,
				context.getEmailId());
		opContext.setArg1(String.valueOf(id));
		opContext.setArg2(type.getClientClassSimpleName());
		return tool.delete(opContext);

	}

	/**
	 * delete transaction.
	 * 
	 * @param accounterCoreType
	 * @param id
	 * @param context
	 * @return
	 * @throws AccounterException
	 */
	public boolean deleteTransaction(AccounterCoreType accounterCoreType,
			long id, Context context) throws AccounterException {
		IAccounterServerCore serverCore = CommandUtils.getServerObjectById(id,
				accounterCoreType);
		FinanceTool tool = new FinanceTool();
		if (serverCore instanceof Transaction) {
			IAccounterCore clientObject = (IAccounterCore) new ClientConvertUtil()
					.toClientObject(serverCore, Util.getClientClass(serverCore));
			tool.deleteTransactionFromDb(context.getCompany().getID(),
					clientObject);
			return true;
		}
		return false;
	}

	/**
	 * load object by id
	 * 
	 * @param className
	 * @param id
	 * @param context
	 * @return
	 * @throws AccounterException
	 */
	private Object loadObjectById(String className, long id, Context context)
			throws AccounterException {
		Session hibernateSession = context.getHibernateSession();
		try {
			Class.forName(className);
			Object object = hibernateSession.get(className, id);
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(AccounterException.ERROR_INTERNAL,
					e.getMessage());
		}

	}

	/**
	 * update
	 * 
	 * @param coreObject
	 * @param context
	 * @return {@link Long}
	 * @throws AccounterException
	 */
	private long update(IAccounterCore coreObject, Context context)
			throws AccounterException {
		FinanceTool tool = new FinanceTool();
		String serverClassFullyQualifiedName = coreObject.getObjectType()
				.getServerClassFullyQualifiedName();
		OperationContext operationContext = new OperationContext(
				getCompanyId(), coreObject, context.getEmailId(),
				String.valueOf(coreObject.getID()),
				serverClassFullyQualifiedName);
		return tool.update(operationContext);
	}

	/**
	 * void the transaction.
	 * 
	 * @param accounterCoreType
	 * @param id
	 * @param context
	 * @return
	 * @throws AccounterException
	 */

	public Boolean voidTransaction(AccounterCoreType accounterCoreType,
			long id, Context context) throws AccounterException {
		IAccounterServerCore serverCore = CommandUtils.getServerObjectById(id,
				accounterCoreType);
		if (serverCore instanceof Transaction) {
			IAccounterCore clientObject = (IAccounterCore) new ClientConvertUtil()
					.toClientObject(serverCore, Util.getClientClass(serverCore));
			((ClientTransaction) clientObject)
					.setSaveStatus(ClientTransaction.STATUS_VOID);
			update(clientObject, context);

			return true;
		}
		return false;
	}
}
