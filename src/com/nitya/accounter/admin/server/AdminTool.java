package com.nitya.accounter.admin.server;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.admin.client.ClientAdminUser;
import com.nitya.accounter.admin.core.AdminUser;
import com.nitya.accounter.core.IAccounterServerCore;
import com.nitya.accounter.services.DAOException;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

public class AdminTool {

	public AdminTool() {
	}

	public long addnewAdminUser(ClientAdminUser clientuser)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			AdminUser user = new AdminUser(clientuser);
			session.saveOrUpdate(user);
			transaction.commit();
			return user.getID();
		} catch (Exception e) {
			transaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL,
						e.getMessage());
			}
		}
	}

	public ArrayList<AdminUser> getAdminUsers() throws DAOException {
		try {
			Session session = HibernateUtil.getCurrentSession();

			Query query = session.getNamedQuery("getAdminUsers");
			List<AdminUser> list = query.list();

			if (list != null) {
				return new ArrayList<AdminUser>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public boolean delete(ClientAdminUser clientuser) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction hibernateTransaction = session.beginTransaction();

		IAccounterServerCore serverObject = (IAccounterServerCore) session.get(
				AdminUser.class, clientuser.getID());

		if (serverObject == null) {
			try {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		try {
			session.delete(serverObject);

			hibernateTransaction.commit();
		} catch (Exception e) {
			hibernateTransaction.rollback();
			if (e instanceof AccounterException) {
				try {
					throw (AccounterException) e;
				} catch (AccounterException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					throw new AccounterException(
							AccounterException.ERROR_INTERNAL);
				} catch (AccounterException e1) {
					e1.printStackTrace();
				}
			}
		}
		return true;
	}
}
