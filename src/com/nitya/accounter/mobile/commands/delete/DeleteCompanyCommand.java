package com.nitya.accounter.mobile.commands.delete;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.User;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.requirements.ListRequirement;
import com.nitya.accounter.mobile.requirements.StringListRequirement;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientUser;
import com.nitya.accounter.web.server.FinanceTool;
import com.nitya.accounter.web.server.OperationContext;

public class DeleteCompanyCommand extends AbstractDeleteCommand {

	private static final String SELECT_OPTION = "selectOption";
	private static final String COMPANY = "company";
	boolean canDeleteFromSingle = true, canDeleteFromAll = true;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ListRequirement<Company>(COMPANY, getMessages()
				.pleaseSelect(getMessages().company()),
				getMessages().company(), false, true, null) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				setOptions();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().companies());
			}

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected Record createRecord(Company value) {
				Record record = new Record(value);
				record.add(getMessages().companyName(), value.getTradingName());
				return record;
			}

			@Override
			protected String getDisplayValue(Company value) {
				return value.getTradingName();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().company());
			}

			@Override
			protected boolean filter(Company e, String name) {
				return e.getTradingName().equalsIgnoreCase(name);
			}

			@Override
			protected List<Company> getLists(Context context) {
				return DeleteCompanyCommand.this.getCompanies(context);
			}
		});

		list.add(new StringListRequirement(SELECT_OPTION, getMessages()
				.pleaseSelect("option"), "Option", false, true, null) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect("option");
			}

			@Override
			protected List<String> getLists(Context context) {
				List<String> lists = new ArrayList<String>();
				if (canDeleteFromAll) {
					lists.add(getMessages().deletecompanyfromallusers());
				}
				if (canDeleteFromSingle) {
					lists.add(getMessages().deletecompanyfromaccount());
				}
				return lists;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
	}

	protected List<Company> getCompanies(Context context) {
		Client client = context.getIOSession().getClient();
		if (client != null) {
			Set<User> users = client.getUsers();
			Session session = HibernateUtil.getCurrentSession();
			try {
				if (!users.isEmpty()) {
					List<Long> userIds = new ArrayList<Long>();
					for (User user : users) {
						if (!user.isDeleted()) {
							userIds.add(user.getID());
						}
					}
					@SuppressWarnings("unchecked")
					List<Object[]> objects = session
							.getNamedQuery(
									"get.CompanyId.Tradingname.and.Country.of.user")
							.setParameterList("userIds", userIds).list();
					return getCompanies(objects);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<Company>();
	}

	private List<Company> getCompanies(List<Object[]> objects) {
		List<Company> list = new ArrayList<Company>();
		for (Object[] obj : objects) {
			Company com = new Company();
			com.setId((Long) obj[0]);
			com.getPreferences().setTradingName((String) obj[1]);
			com.getRegisteredAddress().setCountryOrRegion((String) obj[2]);

			list.add(com);
		}
		return list;
	}

	protected String getFalseString() {
		if (canDeleteFromAll) {
			return "No,I do not want to delete from all users";
		}

		if (canDeleteFromSingle) {
			return "No,I do not want to delete from single account";
		}
		return null;
	}

	protected String getTrueString() {
		if (canDeleteFromAll) {
			return getMessages().deletecompanyfromallusers();
		}

		if (canDeleteFromSingle) {
			return getMessages().deletecompanyfromaccount();
		}
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	private void setOptions() {
		String emailId = getContext().getIOSession().getClient().getEmailId();
		Company company = get(COMPANY).getValue();
		Session hibernateSession = HibernateUtil.getCurrentSession();
		try {
			User user = (User) hibernateSession
					.getNamedQuery("user.by.emailid")
					.setParameter("emailID", emailId)
					.setParameter("company", company).uniqueResult();

			Query query = hibernateSession.getNamedQuery("get.Admin.Users")
					.setParameter("company", company);
			List<User> adminUsers = query.list();
			if (adminUsers.size() < 2) {
				for (User u : adminUsers) {
					if (u.getID() == user.getID()) {
						canDeleteFromSingle = false;
						break;
					}
				}
			}

			if (user != null && !user.isAdmin()) {
				canDeleteFromAll = false;
			}
		} finally {
		}
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		if (!canDeleteFromSingle && !canDeleteFromAll) {
			addFirstMessage(context,
					"You Don't have Permissions to Delete this Company");
			makeResult.setNextCommand("cancel");
		}
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Company company = get(COMPANY).getValue();
		boolean deletFromAll = false;

		String selectedOpt = get(SELECT_OPTION).getValue();
		if (selectedOpt.equals(getMessages().deletecompanyfromallusers())) {
			deletFromAll = true;
		}

		if (selectedOpt.equals(getMessages().deletecompanyfromaccount())) {
			deletFromAll = false;
		}

		if (canDeleteFromAll
				&& (deletFromAll || company.getNonDeletedUsers().size() == 1)) {
			Session session = HibernateUtil.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			try {
				CallableStatement call = session.connection().prepareCall(
						"{ ? = call delete_company(?) }");
				call.registerOutParameter(1, Types.BOOLEAN);
				call.setLong(2, company.getId());
				call.execute();
				boolean isDeleted = false;
				isDeleted = call.getBoolean(1);
				if (!isDeleted) {
					addFirstMessage(context, "Company not deleted");
					return new Result();
				}
				transaction.commit();
			} catch (HibernateException e1) {
				transaction.rollback();
				addFirstMessage(context,
						"Company Deletion failed.Internal Error.");
				return new Result();
			} catch (SQLException e1) {
				transaction.rollback();
				addFirstMessage(context,
						"Company Deletion failed.Internal Error.");
				return new Result();
			} finally {

			}
		} else if (canDeleteFromSingle) {
			Session session = HibernateUtil.getCurrentSession();
			try {
				User serverUser = (User) session
						.getNamedQuery("user.by.emailid")
						.setParameter("emailID",
								context.getIOSession().getClient().getEmailId())
						.setParameter("company", company).uniqueResult();
				String clientClassSimpleName = AccounterCoreType.USER
						.getClientClassSimpleName();
				FinanceTool financeTool = new FinanceTool();
				ClientUser user = new ClientConvertUtil().toClientObject(
						serverUser, ClientUser.class);
				OperationContext opcon = new OperationContext(company.getID(),
						user, context.getIOSession().getClient().getEmailId(),
						String.valueOf(user.getID()), clientClassSimpleName);
				financeTool.delete(opcon);
			} catch (Exception e) {
				addFirstMessage(context,
						"Company Deletion failed.Internal Error.");
				return new Result();
			} 
		}
		return super.onCompleteProcess(context);
	}
}
