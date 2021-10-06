package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;

public class PayeeManager extends Manager {

	public ArrayList<CreditsAndPayments> getCreditsAndPayments(long payeeId,
			long transactionId, long companyId) throws DAOException {

		try {
			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			Query query = session
					.getNamedQuery(
							"getCreditsAndPayments.by.check.payeeidandbalanceid")
					.setParameter("id", payeeId)
					.setParameter("transactionId", transactionId)
					.setEntity("company", company);
			List<CreditsAndPayments> list = query.list();

			if (list != null) {
				ArrayList<CreditsAndPayments> result =new ArrayList<CreditsAndPayments>();
				for(CreditsAndPayments cap:list){
					if(!result.contains(cap)){
						result.add(cap);
					}
				}
				return result;
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}
}
