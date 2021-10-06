package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.PayStructureDestination;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.EmployeeAndEmployeeGroupRequirement;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class PaySlipDetailReportCommand extends
		NewAbstractReportCommand<PaySlipDetail> {

	private static String EMPLOYEE = "employee";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {
			long numberFromString = getNumberFromString(string);
			get(EMPLOYEE).setValue(
					(getServerObject(Employee.class, numberFromString)));
		}
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new EmployeeAndEmployeeGroupRequirement(EMPLOYEE,
				getMessages().pleaseSelect(getMessages().employee()),
				getMessages().employee(), null) {
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
		addDateRangeFromDateRequirements(list);
		list.add(new ReportResultRequirement<PaySlipDetail>() {

			@Override
			protected String onSelection(PaySlipDetail selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<PaySlipDetail> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				Map<String, List<PaySlipDetail>> recordGroups = new HashMap<String, List<PaySlipDetail>>();
				for (PaySlipDetail transactionDetailByAccount : records) {
					String taxItemName = getItemTypeName(transactionDetailByAccount
							.getType());
					List<PaySlipDetail> group = recordGroups.get(taxItemName);
					if (group == null) {
						group = new ArrayList<PaySlipDetail>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByAccount);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> taxItems = new ArrayList<String>(keySet);
				double earnings = 0.0D;
				double deductions = 0.0D;
				for (String accountName : taxItems) {
					List<PaySlipDetail> group = recordGroups.get(accountName);
					addSelection(accountName);
					ResultList resultsList = new ResultList(accountName);
					for (PaySlipDetail rec : group) {
						if (rec.getType() == 2) {
							earnings += rec.getAmount();
						}
						if (rec.getType() == 3) {
							deductions += rec.getAmount();
						}
						resultsList.setTitle(accountName);
						Record createReportRecord = createReportRecord(rec);
						resultsList.add(createReportRecord);
					}
					makeResult.add(resultsList);
				}
				makeResult.add(getMessages().earnings()
						+ getAmountWithCurrency(earnings));
				makeResult.add(getMessages().deductions()
						+ getAmountWithCurrency(deductions));
			}
		});
	}

	private String getItemTypeName(int type) {
		if (type == 1) {
			return getMessages().attendance();
		} else if (type == 2) {
			return getMessages().earnings();
		} else {
			return getMessages().deductions();
		}
	}

	private void addRecords() {
		List<PaySlipDetail> records = getRecords();

	}

	protected Record createReportRecord(PaySlipDetail record) {
		Record payDetailRecord = new Record(record);
		payDetailRecord.add(getMessages().name(), record.getName());

		if (record.getType() == 1) {
			if (record.getAttendanceOrProductionType() == ClientAttendanceOrProductionType.TYPE_PRODUCTION) {
				payDetailRecord.add(getMessages().value(),
						record.getAmount() == null ? 0 : record.getAmount()
								+ " " + record.getUnitName());
			} else {

				payDetailRecord.add(
						getMessages().value(),
						record.getAmount() == null ? 0 : record.getAmount()
								+ " "
								+ ClientPayHead.getCalculationPeriod(record
										.getPeriodType()));
			}
		}

		if (record.getType() == 2) {
			payDetailRecord.add(getMessages().earnings(), record.getAmount());
		}
		if (record.getType() == 3) {
			payDetailRecord.add(getMessages().deductions(), record.getAmount());
		}
		return payDetailRecord;
	}

	protected List<PaySlipDetail> getRecords() {
		List<PaySlipDetail> paySlipDetails = new ArrayList<PaySlipDetail>();
		PayStructureDestination value = get(EMPLOYEE).getValue();
		paySlipDetails = new FinanceTool().getPayrollManager()
				.getPaySlipDetail(value.getID(), getStartDate(), getEndDate(),
						getCompanyId());
		return paySlipDetails;
	}

	protected String addCommandOnRecordClick(PaySlipDetail selection) {
		return null;
	}

}
