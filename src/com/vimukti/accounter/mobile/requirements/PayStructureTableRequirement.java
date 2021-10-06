package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AttendancePayHead;
import com.vimukti.accounter.core.ComputionPayHead;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.FlatRatePayHead;
import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.core.TaxComputationPayHead;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.ui.Accounter;

public class PayStructureTableRequirement extends
		AbstractTableRequirement<ClientPayStructureItem> {

	private static final String EFFECTIVE_FROM = "effectiveFrom";

	private static final String PAY_HEAD = "itemPayHead";

	private static final String RATE = "itemrate";

	public PayStructureTableRequirement(String requirementName,
			String enterString, String recordName, boolean isCreatable,
			boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isCreatable,
				isOptional, isAllowFromContext);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new DateRequirement(EFFECTIVE_FROM, getMessages()
				.pleaseSelect(getMessages().effectiveFrom()), getMessages()
				.effectiveFrom(), true, true));

		list.add(new PayHeadRequirement(PAY_HEAD) {

			@Override
			protected List<PayHead> getLists(Context context) {
				return PayStructureTableRequirement.this.getPayHeads();
			}

			@Override
			public String getRecordName() {
				return getMessages().payhead();
			}
		});

		list.add(new CurrencyAmountRequirement(RATE, getMessages().pleaseEnter(
				getMessages().rate()), getMessages().rate(), false, true) {

			@Override
			protected Currency getCurrency() {
				return getCompany().getPrimaryCurrency();
			}

			@Override
			public boolean isEditable() {
				return PayStructureTableRequirement.this.isRateRequired();
			}
		});
	}

	private boolean isRateRequired() {
		PayHead payhead = get(PAY_HEAD).getValue();
		if (payhead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE || payhead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_TAX) {
			return false;
		} else if (payhead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE
				|| payhead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION) {
			AttendancePayHead ph = (AttendancePayHead) payhead;
			if (ph.getAttendanceType() == AttendancePayHead.ATTENDANCE_ON_RATE) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	protected List<PayHead> getPayHeads() {
		List<PayHead> payheadsList = new ArrayList<PayHead>();
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("list.Payhead").setEntity(
				"company", getCompany());
		payheadsList = query.list();
		return payheadsList;
	}

	@Override
	protected String getEmptyString() {
		return null;
	}

	@Override
	protected void getRequirementsValues(ClientPayStructureItem obj) {
		PayHead payHead = get(PAY_HEAD).getValue();
		obj.setPayHead(payHead.getID());
		Double rate = get(RATE).getValue();
		ClientFinanceDate date = get(EFFECTIVE_FROM).getValue();
		obj.setEffectiveFrom(date.getDate());
		obj.setRate(rate);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientPayStructureItem obj) {
		long payHead = obj.getPayHead();
		get(PAY_HEAD).setValue(
				CommandUtils.getServerObjectById(payHead,
						AccounterCoreType.PAY_HEAD));
		get(RATE).setValue(obj.getRate());
		get(EFFECTIVE_FROM).setValue(
				new ClientFinanceDate(obj.getEffectiveFrom()));
	}

	@Override
	protected ClientPayStructureItem getNewObject() {
		ClientPayStructureItem item = new ClientPayStructureItem();
		item.setEffectiveFrom(new ClientFinanceDate().getDate());
		item.setRate(0.0D);
		return item;
	}

	@Override
	protected Record createFullRecord(ClientPayStructureItem t) {
		return createRecord(t);
	}

	@Override
	protected List<ClientPayStructureItem> getList() {
		return new ArrayList<ClientPayStructureItem>();
	}

	@Override
	protected Record createRecord(ClientPayStructureItem t) {
		Record record = new Record(t);
		record.add(getMessages().effectiveFrom(),
				new ClientFinanceDate(t.getEffectiveFrom()));
		PayHead payHead = (PayHead) CommandUtils.getServerObjectById(
				t.getPayHead(), AccounterCoreType.PAY_HEAD);
		record.add(getMessages().payhead(), payHead.getName());
		record.add(getMessages().rate(), t.getRate());
		Session currentSession = HibernateUtil.getCurrentSession();
		int type = 0;
		if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE) {

			AttendancePayHead payhead = (AttendancePayHead) currentSession.get(
					AttendancePayHead.class, payHead.getID());
			type = payhead.getCalculationPeriod();
		} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
			ComputionPayHead payhead = (ComputionPayHead) currentSession.get(
					ComputionPayHead.class, payHead.getID());
			type = payhead.getCalculationPeriod();
		} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_TAX) {
			type = ClientPayHead.CALCULATION_PERIOD_DAYS;
		} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_FLAT_RATE) {
			FlatRatePayHead payhead = (FlatRatePayHead) currentSession.get(
					FlatRatePayHead.class, payHead.getID());
			type = payhead.getCalculationPeriod();
		}
		record.add(getMessages().calculationPeriod(),
				ClientPayHead.getCalculationPeriod(type));
		record.add(getMessages().payHeadType(),
				ClientPayHead.getPayHeadType(payHead.getType()));
		record.add(getMessages().calculationType(),
				ClientPayHead.getCalculationType(payHead.getCalculationType()));
		return record;
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(
				getMessages().payStructure() + " " + getMessages().items());
	}

	@Override
	protected boolean contains(List<ClientPayStructureItem> oldValues,
			ClientPayStructureItem t) {
		for (ClientPayStructureItem clientTransactionItem : oldValues) {
			if (clientTransactionItem.getPayHead() != 0
					&& clientTransactionItem.getPayHead() == t.getPayHead()) {
				return true;
			}
		}
		return false;
	}

	public void setEmpGroup(long id) {

	}
}
