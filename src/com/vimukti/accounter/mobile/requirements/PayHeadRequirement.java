package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;

public abstract class PayHeadRequirement extends ListRequirement<PayHead> {

	public PayHeadRequirement(String reqName) {
		super(reqName, Global.get().messages()
				.pleaseSelect(Global.get().messages().payhead()), Global.get()
				.messages().otherPayhead(), false, true, null);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().payheadList());
	}

	@Override
	protected List<PayHead> getLists(Context context) {
		List<PayHead> payheadsList = new ArrayList<PayHead>();
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("list.Payhead").setEntity(
				"company", getCompany());
		payheadsList = query.list();
		return payheadsList;
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().otherPayhead());
	}

	@Override
	protected Record createRecord(PayHead value) {
		Record record = new Record(value);
		record.add(getMessages().otherPayhead(),
				value == null ? "" : value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(PayHead value) {
		return value == null ? "" : value.getName();
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add("newPayHead");
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().otherPayhead());
	}

	@Override
	protected boolean filter(PayHead e, String name) {
		return e.getName().equals(name);
	}

}
