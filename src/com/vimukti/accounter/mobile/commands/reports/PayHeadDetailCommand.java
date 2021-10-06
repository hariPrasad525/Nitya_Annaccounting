package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.core.PayStructureDestination;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.EmployeeAndEmployeeGroupRequirement;
import com.vimukti.accounter.mobile.requirements.PayHeadRequirement;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.reports.PayHeadDetails;
import com.vimukti.accounter.web.server.FinanceTool;

public class PayHeadDetailCommand extends
		NewAbstractReportCommand<PayHeadDetails> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new PayHeadRequirement(PAY_HEAD) {

			@Override
			protected List<PayHead> getLists(Context context) {
				return super.getLists(context);
			}
		});
		list.add(new EmployeeAndEmployeeGroupRequirement(EMPLOYEE,
				getMessages().pleaseSelect(getMessages().employee()),
				getMessages().employee(), null) {

			@Override
			protected List<PayStructureDestination> getLists(Context context) {
				List<PayStructureDestination> list = new ArrayList<PayStructureDestination>();
				Session session = HibernateUtil.getCurrentSession();
				Query query = session.getNamedQuery("list.All.Employees")
						.setParameter("isActive", true)
						.setEntity("company", getCompany());
				List<Employee> employees = query.list();
				if (employees != null) {
					list.addAll(employees);
				}
				return list;

			}
		});

		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<PayHeadDetails>() {

			@Override
			protected String onSelection(PayHeadDetails selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<PayHeadDetails> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<PayHeadDetails>> recordGroups = new HashMap<String, List<PayHeadDetails>>();
				for (PayHeadDetails PayHeadDetails : records) {
					String payheadName = PayHeadDetails.getPayHead();
					List<PayHeadDetails> group = recordGroups.get(payheadName);
					if (group == null) {
						group = new ArrayList<PayHeadDetails>();
						recordGroups.put(payheadName, group);
					}
					group.add(PayHeadDetails);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> payHeadItems = new ArrayList<String>(keySet);
				Collections.sort(payHeadItems);
				for (String accountName : payHeadItems) {
					List<PayHeadDetails> group = recordGroups.get(accountName);
					double totalAmount = 0.0;
					addSelection(accountName);
					ResultList resultList = new ResultList(accountName);
					resultList.setTitle(accountName);
					for (PayHeadDetails rec : group) {
						totalAmount += rec.getAmount();
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
					makeResult.add("Total: "
							+ getAmountWithCurrency(totalAmount));
				}
			}
		});
	}

	protected Record createReportRecord(PayHeadDetails record) {
		Record payHeadRecord = new Record(record);
		payHeadRecord.add(getMessages().payhead(), record.getPayHead());
		payHeadRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getAmount()));
		return payHeadRecord;
	}

	protected List<PayHeadDetails> getRecords() {
		ArrayList<PayHeadDetails> PayHeadDetailss = new ArrayList<PayHeadDetails>();
		PayHead value = get(PAY_HEAD).getValue();
		Employee empValue = get(EMPLOYEE).getValue();
		try {
			PayHeadDetailss = new FinanceTool().getPayrollManager()
					.getpayHeadDetailsList(empValue.getID(), value.getID(),
							getStartDate(), getEndDate(), getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return PayHeadDetailss;
	}

	protected String addCommandOnRecordClick(PayHeadDetails selection) {
		return "payHeadDetailReport ," + selection.getPayHead();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {
			long numberFromString = getNumberFromString(string);
			get(PAY_HEAD).setValue(
					(getServerObject(PayHead.class, numberFromString)));
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportDetails(getMessages().payHeadDetailReport());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().payHeadDetailReport());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().payHeadDetailReport());
	}
}
