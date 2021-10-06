package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.User;
import com.nitya.accounter.mobile.Command;
import com.nitya.accounter.mobile.CommandList;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.MobileSession;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.mobile.UserCommand;
import com.nitya.accounter.utils.HibernateUtil;

/**
 * Selects the Company
 * 
 * @author vimutki35
 * 
 */
public class SelectCompanyCommand extends Command {

	private static final String COMPANY = "company";

	@Override
	public String getId() {
		return "selectCompany";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(COMPANY, false, true));

	}

	@SuppressWarnings("unchecked")
	@Override
	public Result run(Context context) {
		Result makeResult = context.makeResult();
		Object choice = context.getSelection("Choice");
		if (choice != null) {
			if (choice.equals("No")) {
				markDone();
				makeResult = context.makeResult();
				makeResult
						.add("Selecting Company has been canceled. Continue with your Company.");
				return makeResult;
			} else {
				makeResult.add("Your commands are canceled.");
				MobileSession ioSession = context.getIOSession();
				Command currentCommand = ioSession.getCurrentCommand();
				while (currentCommand != null) {
					currentCommand.markDone();
					ioSession.refreshCurrentCommand();
					currentCommand = ioSession.getCurrentCommand();
				}
			}
		}
		if (context.getString() == null || context.getString().isEmpty()) {
			Company company = context.getCompany();
			if (company != null && choice == null) {
				makeResult = context.makeResult();
				makeResult
						.add("If you want to change company, your previuos commands are getting canceled. proccede?");
				ResultList list = new ResultList("Choice");
				Record e = new Record("Yes");
				e.add("Yes (Cancel all my previous commands)");
				list.add(e);

				e = new Record("No");
				e.add("No (Continue with '" + company.getDisplayName() + "')");
				list.add(e);
				makeResult.add(list);
				return makeResult;
			}
		}
		Requirement companyReq = get(COMPANY);

		Company selection = context.getSelection(COMPANY);
		if (selection != null) {
			makeResult.add(selection.getDisplayName() + " has been selected.");
			companyReq.setValue(selection);
		}
		Client client = context.getIOSession().getClient();
		if (!companyReq.isDone()) {
			if (client != null) {
				Set<User> users = client.getUsers();
				List<Company> companies = new ArrayList<Company>();
				Session session = HibernateUtil.getCurrentSession();
				try {
					if (!users.isEmpty()) {
						List<Long> userIds = new ArrayList<Long>();
						for (User user : users) {
							if (!user.isDeleted()) {
								userIds.add(user.getID());
							}
						}
						List<Object[]> objects = session
								.getNamedQuery(
										"get.CompanyId.Tradingname.and.Country.of.user")
								.setParameterList("userIds", userIds).list();
						addCompanies(companies, objects);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// for (User user : users) {
				// if (!user.isDeleted()) {
				// companies.add(user.getCompany());
				// }
				// }
				if (companies.size() == 1) {
					Company company = companies.get(0);
					companyReq.setValue(company);
					makeResult.add("You have only '" + company.getDisplayName()
							+ "' company.");
					makeResult.add("It has been selected.");
					CommandList commandList = new CommandList();
					commandList.add(new UserCommand("createCompany",
							"Create Another Company", ""));
					makeResult.add(commandList);
				} else {
					ResultList companyList = new ResultList(COMPANY);
					if (companies.isEmpty()) {
						makeResult.add("You don't have any companies.");
						makeResult.setNextCommand("createCompany");
						return makeResult;
					}
					makeResult.add("Select a company");
					// Collections.sort(companies, new Comparator<Company>() {
					//
					// @Override
					// public int compare(Company company1, Company company2) {
					// return company1.getTradingName().compareTo(
					// company2.getTradingName());
					// }
					//
					// });
					for (Company company : companies) {
						Record record = new Record(company);
						record.add("Name", company.getDisplayName());
						record.add("Country", company.getCountry());
						companyList.add(record);
					}
					makeResult.add(companyList);
					CommandList commandList = new CommandList();
					commandList.add("createCompany");
					commandList.add("deletecompany");
					commandList.add("logout");
					makeResult.add(commandList);
					return makeResult;
				}
			}
		}
		Company value = companyReq.getValue();
		context.selectCompany(value, client);
		makeResult.setNextCommand("menu");
		markDone();
		return makeResult;
	}

	private void addCompanies(List<Company> list, List<Object[]> objects) {
		for (Object[] obj : objects) {
			Company com = new Company();
			com.setId((Long) obj[0]);
			com.getPreferences().setTradingName((String) obj[1]);
			com.getRegisteredAddress().setCountryOrRegion((String) obj[2]);

			list.add(com);
		}
	}
}
