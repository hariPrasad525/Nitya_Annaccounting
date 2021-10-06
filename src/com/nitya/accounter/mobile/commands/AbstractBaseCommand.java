package com.nitya.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.mobile.ActionNames;
import com.nitya.accounter.mobile.Command;
import com.nitya.accounter.mobile.Context;
import com.nitya.accounter.mobile.Record;
import com.nitya.accounter.mobile.Requirement;
import com.nitya.accounter.mobile.Result;
import com.nitya.accounter.mobile.ResultList;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;

public abstract class AbstractBaseCommand extends Command {
	private long companyId;
	private Company company;
	private ClientCompanyPreferences preferences;
	private Stack<Integer> requirementSequence = new Stack<Integer>();
	protected int lastRequirement = -2;
	private Context context;

	@SuppressWarnings("unchecked")
	@Override
	public Result run(Context context) {
		setCompany(context.getCompany());
		setPreferences(context.getPreferences());
		companyId = getCompany() == null ? 0 : getCompany().getID();
		Result result = process(context);
		List<String> first = (List<String>) context
				.getAttribute("firstMessage");
		for (String f : first) {
			result.add(0, f);
		}
		result.setShowBack(!isDone());
		return result;
	}

	public Result process(Context context) {
		this.context = context;
		context.setAttribute("firstMessage", new ArrayList<String>());
		Result makeResult = context.makeResult();
		if (getAttribute("input") == null) {
			setDefaultValues(context);
			String lowerCase = context.getCommandString().toLowerCase();
			String nextCommandString = initObject(
					context,
					lowerCase.startsWith("update")
							|| lowerCase.startsWith("edit"));
			if (nextCommandString != null) {
				Result result = new Result();
				result.setNextCommand(nextCommandString);
				markDone();
				return result;
			}
			setAttribute("input", "");
		}

		ResultList actions = new ResultList("actions");

		List<Requirement> allRequirements = getRequirements();
		String backString = context.getString();
		if (backString != null && backString.equalsIgnoreCase("back")) {
			context.setString("");
			boolean canCancel = false;
			if (requirementSequence.size() > 1) {
				requirementSequence.pop();
				// Current req
				lastRequirement = requirementSequence.pop();// Last Req
				context.setAttribute("processAttr", "");
				context.setAttribute("multiprocessAttr", "");
				if (lastRequirement != -1) {
					Requirement requirement = allRequirements
							.get(lastRequirement);
					context.putSelection("values", requirement.getName());
					ResultList list2 = new ResultList("");
					Result process = requirement.process(context,
							context.makeResult(), list2, actions);
					if (process != null) {
						requirementSequence.push(lastRequirement);
						return process;
					}
				} else {
					canCancel = requirementSequence.isEmpty();
				}
			} else {
				canCancel = true;
			}
			if (canCancel) {
				Result result = new Result();
				result.setNextCommand("cancel");
				return result;
			}
		}
		ResultList list = new ResultList("values");
		makeResult.add(list);
		actions = new ResultList("actions");
		// if (lastRequirement == -1) {
		// requirementSequence.pop();
		// }
		for (int i = 0; i < allRequirements.size(); i++) {
			Result result = allRequirements.get(i).process(context, makeResult,
					list, actions);
			if (result != null) {
				if (lastRequirement != i && isCanTrackRequirements()) {
					requirementSequence.push(i);
				}
				setCanTrackRequirements(true);
				lastRequirement = i;
				return result;
			}
		}
		requirementSequence.push(-1);
		lastRequirement = -1;
		list.setTitle(getDetailsMessage());
		makeResult.add(actions);
		String finish = getFinishCommandString();
		if (finish != null) {
			Record record = new Record(ActionNames.FINISH_COMMAND);
			record.add(finish);
			actions.add(record);
		}
		Object selection = context.getSelection("actions");
		String thirdCommand = getThirdCommand(context);

		if (selection == ActionNames.THIRD_COMMAND) {
			if (thirdCommand != null) {
				Result result = new Result();
				result.setNextCommand(thirdCommand);
				markDone();
				return result;
			}
		}

		if (thirdCommand != null) {
			Record record = new Record(ActionNames.THIRD_COMMAND);
			record.add(getThirdCommandString());
			actions.add(record);
		}

		String deleteCommand = getDeleteCommand(context);

		if (selection == ActionNames.DELETE_COMMAND) {
			if (deleteCommand != null) {
				Result result = new Result();
				result.setNextCommand(deleteCommand);
				markDone();
				return result;
			}
		}

		if (deleteCommand != null) {
			Record record = new Record(ActionNames.DELETE_COMMAND);
			record.add("Delete");
			actions.add(record);
		}

		beforeFinishing(context, makeResult);
		if (!isDone() && selection != ActionNames.FINISH_COMMAND) {
			return makeResult;
		}

		Result result = onCompleteProcess(context);
		if (result != null) {
			List<Object> resultParts = makeResult.getResultParts();
			for (Object object : result.getResultParts()) {
				resultParts.add(0, object);
			}
			makeResult.setNextCommand(result.getNextCommand());
			return makeResult;
		}

		Result finishResult = context.makeResult();
		String success = getSuccessMessage();
		if (success != null) {
			finishResult.add(success);
		}
		markDone();
		return finishResult;
	}

	protected String getThirdCommandString() {
		return null;
	}

	protected String getThirdCommand(Context context) {
		return null;
	}

	protected String getDeleteCommand(Context context) {
		return null;
	}

	protected abstract String initObject(Context context, boolean isUpdate);

	protected abstract String getWelcomeMessage();

	protected Result onCompleteProcess(Context context) {
		return null;
	}

	public void beforeFinishing(Context context, Result makeResult) {
	}

	public String getFinishCommandString() {
		return "Finish";
	}

	protected abstract String getDetailsMessage();

	protected abstract void setDefaultValues(Context context);

	public abstract String getSuccessMessage();

	public long getCompanyId() {
		return companyId;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

	public Context getContext() {
		return context;
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId.toLowerCase());
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}
}