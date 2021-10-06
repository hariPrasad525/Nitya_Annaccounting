package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class Budget extends CreatableObject implements IAccounterServerCore,
		INamedObject {

	private static final long serialVersionUID = 1L;

	private String budgetName;

	private String financialYear;

	List<BudgetItem> budgetItems = new ArrayList<BudgetItem>();

	private int version;

	public String getBudgetName() {
		return budgetName;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		for (BudgetItem item : budgetItems) {
			item.setCompany(getCompany());
		}
		return super.onSave(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		for (BudgetItem item : budgetItems) {
			item.setCompany(getCompany());
		}
		return super.onUpdate(session);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public void setBudgetName(String budgetname) {
		this.budgetName = budgetname;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!UserUtils.canDoThis(Budget.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		return true;
	}

	public List<BudgetItem> getBudgetItems() {
		return budgetItems;
	}

	public void setBudgetItems(List<BudgetItem> budgetItems) {
		this.budgetItems = budgetItems;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.budget()).gap().gap();

		w.put(messages.name(), this.budgetName);

		w.put(messages.details(), this.budgetItems);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getObjType() {
		return IAccounterCore.BUDGET;
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (financialYear == null || financialYear.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().budgetFinancialYear());
		}
		if (budgetName == null || budgetName.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().budget());
		}
		if (budgetItems.size() == 0) {
			throw new AccounterException(AccounterException.ERROR_AMOUNT_ZERO,
					Global.get().messages().amount());
		}
	}

}
