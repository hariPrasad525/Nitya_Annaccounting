package com.nitya.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.json.JSONException;

import com.nitya.accounter.core.change.ChangeTracker;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCommand;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.server.FinanceTool;

public class AccounterClass extends CreatableObject implements
		IAccounterServerCore, INamedObject {

	private String className;

	private AccounterClass parent;

	private int parentCount;

	private String path;

	private AccounterClass oldParent;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		this.parentCount = getparents();
		updatePath();
		return super.onUpdate(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return false;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		this.parentCount = getparents();
		updatePath();
		return super.onSave(session);
	}

	public String getclassName() {
		return className;
	}

	public void setclassName(String trackingClassName) {
		this.className = trackingClassName;
	}

	@Override
	public String getName() {
		return className;
	}

	@Override
	public void setName(String name) {
		this.className = name;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.ACCOUNTER_CLASS;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		w.put(messages.name(), this.className);
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.ACCOUNTER_CLASS);
		ChangeTracker.put(accounterCore);
		return super.onDelete(arg0);
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (className == null || className.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_NAME_NULL,
					Global.get().messages().accounterClass());
		}
	}

	public AccounterClass getParent() {
		return parent;
	}

	public void setParent(AccounterClass parent) {
		this.parent = parent;
	}

	public int getParentCount() {
		return parentCount;
	}

	public void setParentCount(int parentCount) {
		this.parentCount = parentCount;
	}

	/**
	 * get the parent count of this object
	 * 
	 * @return parent count
	 */
	private int getparents() {
		int count = 0;
		AccounterClass parentClass = this;
		while (parentClass.getParent() != null
				&& parentClass.getParent().getID() != 0) {
			count += 1;
			parentClass = (AccounterClass) new FinanceTool().getManager()
					.getServerObjectForid(AccounterCoreType.ACCOUNTER_CLASS,
							parentClass.getParent().getID());
		}
		return count;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		this.oldParent = this.parent;
		super.onLoad(arg0, arg1);
	}

	private void updatePath() {

		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		session.setFlushMode(FlushMode.COMMIT);
		try {
			if (getID() == 0
					|| ((parent == null && oldParent != null)
							|| (parent != null & oldParent == null) || (parent
							.getID() != oldParent.getID()))) {
				if (parent == null) {
					Integer path = (Integer) session
							.getNamedQuery("get.max.Path.Of.Classes")
							.setParameter("classId", getID()).uniqueResult();
					path++;
					setPath(path + "");
				} else {
					Long chldCount = (Long) session
							.getNamedQuery("get.child.count.of.Class")
							.setParameter("classId", parent.getID())
							.uniqueResult();
					chldCount++;
					setPath(parent.getPath() + '.' + chldCount);
				}
			}
		} finally {
			session.setFlushMode(flushMode);
		}
	}
}
