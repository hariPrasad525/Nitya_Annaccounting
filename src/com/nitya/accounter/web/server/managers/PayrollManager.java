package com.nitya.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nitya.accounter.core.AttendanceOrProductionType;
import com.nitya.accounter.core.AttendancePayHead;
import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.ComputionPayHead;
import com.nitya.accounter.core.Employee;
import com.nitya.accounter.core.EmployeeGroup;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.FlatRatePayHead;
import com.nitya.accounter.core.LiveTaxRate;
import com.nitya.accounter.core.PayHead;
import com.nitya.accounter.core.PayStructure;
import com.nitya.accounter.core.PayStructureItem;
import com.nitya.accounter.core.PayrollUnit;
import com.nitya.accounter.core.TaxComputationPayHead;
import com.nitya.accounter.core.Transaction;
import com.nitya.accounter.core.UserDefinedPayHead;
import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.core.ClientAttendanceManagementItem;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionItem;
import com.nitya.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.nitya.accounter.web.client.core.ClientAttendancePayHead;
import com.nitya.accounter.web.client.core.ClientComputaionFormulaFunction;
import com.nitya.accounter.web.client.core.ClientComputionPayHead;
import com.nitya.accounter.web.client.core.ClientEmployee;
import com.nitya.accounter.web.client.core.ClientEmployeeGroup;
import com.nitya.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientFlatRatePayHead;
import com.nitya.accounter.web.client.core.ClientPayHead;
import com.nitya.accounter.web.client.core.ClientPayStructure;
import com.nitya.accounter.web.client.core.ClientPayStructureDestination;
import com.nitya.accounter.web.client.core.ClientPayStructureList;
import com.nitya.accounter.web.client.core.ClientPayrollTransactionPayTax;
import com.nitya.accounter.web.client.core.ClientPayrollUnit;
import com.nitya.accounter.web.client.core.ClientTaxComputationPayHead;
import com.nitya.accounter.web.client.core.ClientTransactionPayEmployee;
import com.nitya.accounter.web.client.core.ClientUserDefinedPayHead;
import com.nitya.accounter.web.client.core.ClientUserDefinedPayheadItem;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.PaymentsList;
import com.nitya.accounter.web.client.core.reports.PayHeadDetails;
import com.nitya.accounter.web.client.core.reports.PayHeadSummary;
import com.nitya.accounter.web.client.core.reports.PaySheet;
import com.nitya.accounter.web.client.core.reports.PaySlipDetail;
import com.nitya.accounter.web.client.core.reports.PaySlipSummary;
import com.nitya.accounter.web.client.exception.AccounterException;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PayrollManager extends Manager {

	public PaginationList<ClientEmployee> getEmployees(boolean isActive,
			int start, int lenght, Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.All.Employees")
				.setParameter("isActive", isActive)
				.setEntity("company", company);
		List<Employee> employees = query.list();
		if (employees == null) {
			return null;
		}
		int total = query.list().size();
		if (lenght != -1) {
			employees = query.setFirstResult(start).setMaxResults(lenght)
					.list();
		}
		PaginationList<ClientEmployee> clientEmployees = new PaginationList<ClientEmployee>();
		for (Employee employee : employees) {
			ClientEmployee clientEmployee;
			clientEmployee = new ClientConvertUtil().toClientObject(employee,
					ClientEmployee.class);
			clientEmployees.add(clientEmployee);
		}
		clientEmployees.setStart(start);
		clientEmployees.setTotalCount(total);
		return clientEmployees;
	}

	public PaginationList<ClientPayHead> getPayheadsList(int start, int length,
			Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.Payhead").setEntity(
				"company", company);
		List<PayHead> employees = query.list();
		int total = query.list().size();
		if (length != -1 && length != 0) {
			employees = query.setFirstResult(start).setMaxResults(length)
					.list();
		}
		PaginationList<ClientPayHead> clientPayHeads = new PaginationList<ClientPayHead>();
		if (employees != null) {
			for (PayHead payHead : employees) {
				ClientPayHead clientPayHead = null;
				if (payHead instanceof AttendancePayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientAttendancePayHead.class);
				} else if (payHead instanceof ComputionPayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientComputionPayHead.class);
				} else if (payHead instanceof FlatRatePayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientFlatRatePayHead.class);
				} else if (payHead instanceof UserDefinedPayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientUserDefinedPayHead.class);
				} else if (payHead instanceof FlatRatePayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientFlatRatePayHead.class);
				} else if (payHead instanceof TaxComputationPayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientTaxComputationPayHead.class);
				}

				if (clientPayHead instanceof ClientComputionPayHead) {
					ClientComputionPayHead ph = (ClientComputionPayHead) clientPayHead;
					for (ClientComputaionFormulaFunction formula : ph
							.getFormulaFunctions()) {
						PayHead object = (PayHead) session.get(PayHead.class,
								formula.getPayHead());
						ClientUserDefinedPayHead payhead = new ClientConvertUtil()
								.toClientObject(object,
										ClientUserDefinedPayHead.class);
						formula.setClientPayHead(payhead);
					}
					clientPayHead = ph;
				}

				if (clientPayHead != null) {
					clientPayHeads.add(clientPayHead);
				}
			}
		}
		clientPayHeads.setTotalCount(total);
		clientPayHeads.setStart(start);
		return clientPayHeads;
	}

	public PaginationList<ClientPayrollUnit> getPayrollUnitsList(int start,
			int length, Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.PayrollUnit").setEntity(
				"company", company);
		List<PayrollUnit> units = query.list();
		if (units == null) {
			return null;
		}

		int total = query.list().size();

		if (length != -1) {
			units = query.setFirstResult(start).setMaxResults(length).list();
		}

		PaginationList<ClientPayrollUnit> clientPayrollUnits = new PaginationList<ClientPayrollUnit>();
		for (PayrollUnit unit : units) {
			ClientPayrollUnit clientPayrollUnit;
			clientPayrollUnit = new ClientConvertUtil().toClientObject(unit,
					ClientPayrollUnit.class);
			clientPayrollUnits.add(clientPayrollUnit);
		}
		clientPayrollUnits.setTotalCount(total);
		clientPayrollUnits.setStart(start);
		return clientPayrollUnits;
	}

	public ArrayList<ClientEmployeePayHeadComponent> getEmployeeGroupPayHeadComponents(
			FinanceDate startDate, FinanceDate endDate,
			ClientPayStructureDestination selectItem, Long companyId,
			List<ClientAttendanceManagementItem> attendanceItems,
			Long noOfWorkingDays) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<Employee> employees = session.getNamedQuery("getEmployeesByGroup")
				.setParameter("groupId", selectItem.getID()).list();
		ArrayList<ClientEmployeePayHeadComponent> groupComponents = new ArrayList<ClientEmployeePayHeadComponent>();
		for (Employee employee : employees) {
			ArrayList<ClientEmployeePayHeadComponent> employeePayHeadComponents = getEmployeePayHeadComponents(
					startDate, endDate, employee, companyId,
					getEmployeeAttendanceItems(employee, attendanceItems),
					noOfWorkingDays);
			groupComponents.addAll(employeePayHeadComponents);
		}
		return new ArrayList<ClientEmployeePayHeadComponent>(groupComponents);
	}

	private List<ClientAttendanceManagementItem> getEmployeeAttendanceItems(
			Employee employee,
			List<ClientAttendanceManagementItem> attendanceItems) {
		List<ClientAttendanceManagementItem> empItems = new ArrayList<ClientAttendanceManagementItem>();
		for (ClientAttendanceManagementItem item : attendanceItems) {
			if (item.getEmployee() == employee.getID()) {
				empItems.add(item);
			}
		}
		return empItems;
	}

	public ArrayList<ClientEmployeePayHeadComponent> getEmployeePayHeadComponents(
			FinanceDate startDate, FinanceDate endDate, Employee selectItem,
			Long companyId,
			List<ClientAttendanceManagementItem> attendanceItems,
			Long noOfWorkingDays) throws AccounterException {
		long employeeId = selectItem.getID();
		EmployeeGroup group = selectItem.getGroup();
		long employeeGroup = 0l;
		if (group != null) {
			//employeeGroup = group.getID();
		}
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getPayStructureItem.by.employee")
				.setParameter("employee", employeeId)
				.setParameter("group", employeeGroup)
				.setParameter("companyId", companyId)
				.setParameter("start", startDate.getDate());
		List<PayStructureItem> list = query.list();

		if (list == null) {
			return null;
		}

		// Remove Duplicate PayHeads that are selected for Employee and Group
		removeDuplicatePayHeads(list);

		double earnings = 0.0;
		double deductions = 0.0;

		double[] attendance = { 0, 0, 0, 0 };

		HashMap<Long, Double> userDefinedValues = new HashMap<Long, Double>();

		if (attendanceItems != null && attendanceItems.size() > 0) {
			for (ClientAttendanceManagementItem attendanceManagementItem : attendanceItems) {
				attendance[1] += attendanceManagementItem.getAbscentDays();
				for (ClientAttendanceOrProductionItem item : attendanceManagementItem
						.getAttendanceOrProductionItems()) {
					Object object = session.get(
							AttendanceOrProductionType.class,
							item.getAttendanceOrProductionType());
					ClientAttendanceOrProductionType attType = new ClientConvertUtil()
							.toClientObject(object,
									ClientAttendanceOrProductionType.class);
					if (attType.getType() == AttendanceOrProductionType.TYPE_PRODUCTION) {
						attendance[2] += item.getValue();
					} else {
						attendance[2] += item.getValue();
					}
				}

				for (ClientUserDefinedPayheadItem item : attendanceManagementItem
						.getUserDefinedPayheads()) {
					userDefinedValues.put(item.getPayHeadID(), item.getValue());
				}

			}
		}

		attendance[0] = noOfWorkingDays - attendance[1];

		ArrayList<ClientEmployeePayHeadComponent> clientEmployeePayHeadComponents = new ArrayList<ClientEmployeePayHeadComponent>();

		for (PayStructureItem payStructureItem : list) {
			int calcType = payStructureItem.getPayHead().getCalculationType();
			PayHead payHead = payStructureItem.getPayHead();

			ClientPayHead clientPayHead = null;
			if (calcType == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE
					|| calcType == ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientAttendancePayHead.class);
			} else if (calcType == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientComputionPayHead.class);
			} else if (payHead.getCalculationType() == ClientPayHead.CALCULATION_TYPE_AS_TAX) {
				clientPayHead = new ClientConvertUtil().toClientObject(
						payHead, ClientTaxComputationPayHead.class);
			} else if (calcType == ClientPayHead.CALCULATION_TYPE_FLAT_RATE) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientFlatRatePayHead.class);
			} else if (calcType == ClientPayHead.CALCULATION_TYPE_AS_USER_DEFINED) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientUserDefinedPayHead.class);
			}

			if (clientPayHead instanceof ClientComputionPayHead) {
				ClientComputionPayHead ph = (ClientComputionPayHead) clientPayHead;
				for (ClientComputaionFormulaFunction formula : ph
						.getFormulaFunctions()) {
					PayHead object = (PayHead) session.get(PayHead.class,
							formula.getPayHead());
					ClientPayHead payhead = new ClientConvertUtil()
							.toClientObject(object, ClientPayHead.class);
					formula.setClientPayHead(payhead);
				}
				clientPayHead = ph;
			}

			ClientEmployeePayHeadComponent component = new ClientEmployeePayHeadComponent();
			component.setPayHead(clientPayHead.getID());
			component.setClientPayHead(clientPayHead);
			payStructureItem.setStartDate(startDate);
			payStructureItem.setEndDate(endDate);
			Double userDefValue = userDefinedValues.get(payHead.getID());

			double[] newAttendance = Arrays.copyOf(attendance, 4);
			if (userDefValue != null) {
				newAttendance[3] = userDefValue;
			}
			payStructureItem.setAttendance(newAttendance);
			double calculatedAmount = payHead.calculatePayment(component, 
					payStructureItem, deductions, earnings);

			if (payHead.isEarning()) {
				earnings += calculatedAmount;
			} else {
				deductions += calculatedAmount;
			}
			component.setRate(calculatedAmount);

			component.setEmployee(selectItem.getName());
			clientEmployeePayHeadComponents.add(component);
		}
		clientEmployeePayHeadComponents.sort(new Comparator<ClientEmployeePayHeadComponent>() {
			@Override
			public int compare(ClientEmployeePayHeadComponent o1, ClientEmployeePayHeadComponent o2) {
				return o1.getClientPayHead() != null && o1.getClientPayHead().isEarning() ? 1 : 0;
			}
		});
		return clientEmployeePayHeadComponents;
	}

	/**
	 * Remove Duplicate PayHeads that are selected for Employee and his/her
	 * Group
	 * 
	 * @param list
	 * @return
	 */
	private void removeDuplicatePayHeads(List<PayStructureItem> list) {
		ArrayList<PayStructureItem> result = new ArrayList<PayStructureItem>(
				list);

		// Separate Employee and Groups PayStructure Items First
		ArrayList<PayStructureItem> employeeItems = new ArrayList<PayStructureItem>();
		ArrayList<PayStructureItem> groupsItems = new ArrayList<PayStructureItem>();
		for (PayStructureItem psItem : result) {
			PayStructure payStructure = psItem.getPayStructure();
			if (payStructure.getEmployee() != null) {
				employeeItems.add(psItem);
			} else {
				groupsItems.add(psItem);
			}
		}

		// Check and Remove duplicate PayHeads selected for Employee's Group
		for (PayStructureItem psItem : groupsItems) {
			PayHead payHead = psItem.getPayHead();
			for (PayStructureItem ePsItem : employeeItems) {
				// If Found this Group's PayHead in Empoyee Listm then Remove
				// this
				if (ePsItem.getPayHead().getID() == payHead.getID()) {
					list.remove(psItem);
				}
			}
		}
	}

	public PaginationList<ClientPayStructureList> getPayrollStructuresList(
			int start, int length, Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.PayStructure").setEntity(
				"company", company);
		List<PayStructure> paystructures = query.list();
		if (paystructures == null) {
			return null;
		}
		int total = query.list().size();
		paystructures = query.setFirstResult(start).setMaxResults(length)
				.list();
		PaginationList<ClientPayStructureList> clientPayStructures = new PaginationList<ClientPayStructureList>();
		for (PayStructure payStructure : paystructures) {
			ClientPayStructureList payStructureList = new ClientPayStructureList();
			ClientEmployee employee = null;
			if (payStructure.getEmployee() != null) {
				employee = new ClientConvertUtil().toClientObject(
						payStructure.getEmployee(), ClientEmployee.class);
			}
			ClientEmployeeGroup employeeGroup = null;
			if (payStructure.getEmployeeGroup() != null) {
				employeeGroup = new ClientConvertUtil().toClientObject(
						payStructure.getEmployeeGroup(),
						ClientEmployeeGroup.class);
			}

			ClientPayStructure clientPayStructure = new ClientConvertUtil()
					.toClientObject(payStructure, ClientPayStructure.class);

			payStructureList.setId(payStructure.getID());
			payStructureList.setEmployee(employee);
			payStructureList.setEmployeeGroup(employeeGroup);
			payStructureList.setItems(clientPayStructure.getItems());
			clientPayStructures.add(payStructureList);
		}
		clientPayStructures.setStart(start);
		clientPayStructures.setTotalCount(total);
		return clientPayStructures;
	}

	public PaginationList<ClientAttendanceOrProductionType> getAttendanceProductionTypes(
			int start, int length, Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		int total = 0;
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.AttendanceProductionType")
				.setEntity("company", company);
		List<AttendanceOrProductionType> types = query.list();
		if (types == null) {
			return null;
		}
		if (length == -1) {
			types = query.list();
		} else {
			total = query.list().size();
			types = query.setFirstResult(start).setMaxResults(length).list();
		}
		PaginationList<ClientAttendanceOrProductionType> list = new PaginationList<ClientAttendanceOrProductionType>();
		ClientConvertUtil cUtils = new ClientConvertUtil();
		for (AttendanceOrProductionType type : types) {
			ClientAttendanceOrProductionType clientType = cUtils
					.toClientObject(type,
							ClientAttendanceOrProductionType.class);
			PayrollUnit serverUnit = type.getUnit();
			if (serverUnit != null) {
				ClientPayrollUnit clientUnit = cUtils.toClientObject(
						serverUnit, ClientPayrollUnit.class);
				clientType.setClientPayrollUnit(clientUnit);
			}
			list.add(clientType);
		}
		list.setTotalCount(total);
		list.setStart(start);
		return list;
	}

	public ArrayList<ClientEmployeeGroup> getEmployeeGroups(Long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.EmployeeGroups").setEntity(
				"company", company);
		List<EmployeeGroup> groups = query.list();
		if (groups == null) {
			return null;
		}
		ArrayList<ClientEmployeeGroup> clientGroups = new ArrayList<ClientEmployeeGroup>();
		for (EmployeeGroup group : groups) {
			List<Employee> employees = session
					.getNamedQuery("getEmployeesByGroup")
					.setParameter("groupId", group.getID()).list();
			group.setEmployees(new HashSet<Employee>(employees));
			ClientEmployeeGroup clientGroup = new ClientConvertUtil()
					.toClientObject(group, ClientEmployeeGroup.class);
			clientGroups.add(clientGroup);
		}
		return clientGroups;
	}

	public List<ClientPayStructureDestination> getEmployeesAndGroups(
			Long companyId) throws AccounterException {
		ArrayList<ClientPayStructureDestination> arrayList = new ArrayList<ClientPayStructureDestination>();
		PaginationList<ClientEmployee> employees = getEmployees(true, 0, -1,
				companyId);
		arrayList.addAll(employees);
		ArrayList<ClientEmployeeGroup> employeeGroups = getEmployeeGroups(companyId);
		arrayList.addAll(employeeGroups);

		return arrayList;
	}

	public long getEmployeeAttendanceCurrentBal(long employee,
			long attendanceType, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("EmployeeAttendanceCurrentBal")
				.setParameter("company", companyId)
				.setParameter("employee", employee)
				.setParameter("attendanceType", attendanceType);
		Object result = query.uniqueResult();
		if (result == null) {
			return 0;
		}
		return (Long) result;
	}

	public ArrayList<ClientEmployee> getEmployeesByGroup(
			ClientPayStructureDestination group, Long companyId)
			throws AccounterException {
		ArrayList<ClientEmployee> list = new ArrayList<ClientEmployee>();
		Session session = HibernateUtil.getCurrentSession();
		List<Employee> employees = session.getNamedQuery("getEmployeesByGroup")
				.setParameter("groupId", group.getID()).list();
		for (Employee employee : employees) {
			ClientEmployee clientEmployee = new ClientConvertUtil()
					.toClientObject(employee, ClientEmployee.class);
			list.add(clientEmployee);
		}
		return list;
	}

	public ArrayList<PayHeadSummary> getPayHeadSummaryReport(long payHeadId,
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws AccounterException {
		ArrayList<PayHeadSummary> payHeadSummaryList = new ArrayList<PayHeadSummary>();
		Session currentSession = HibernateUtil.getCurrentSession();
		Query query = currentSession
				.getNamedQuery("getPayHeadSummaryReportList")
				.setParameter("payHeadId", payHeadId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("companyId", companyId);
		List list = query.list();

		Iterator iterator = list.iterator();
		while ((iterator).hasNext()) {
			Object[] object = (Object[]) iterator.next();
			PayHeadSummary headSummary = new PayHeadSummary();
			headSummary.setEmployeeId(object[0] == null ? 0 : (Long) object[0]);
			headSummary.setEmployeeName(object[1] == null ? ""
					: (String) object[1]);
			headSummary.setPayHead(object[2] == null ? 0 : (Long) object[2]);
			headSummary.setPayHeadName(object[3] == null ? ""
					: (String) object[3]);
			headSummary.setPayHeadAmount(object[4] == null ? 0
					: (Double) object[4]);
			headSummary.setDefault((Boolean) object[5]);
			headSummary.setEmployeerDeductions((Double) object[6]);
			payHeadSummaryList.add(headSummary);
		}
		return payHeadSummaryList;
	}
	
	public ArrayList<PayHeadSummary> getPayTaxSummaryReport(String taxType, long employeeId,  FinanceDate startDate, FinanceDate endDate, long companyId)
			throws AccounterException {
		ArrayList<PayHeadSummary> payHeadSummaryList = getPayHeadSummaryReport(0, startDate, endDate, companyId);
		Company company = getCompany(companyId);
		String state = company.getPreferences().getTradingAddress().getStateOrProvinence();
		Iterator<PayHeadSummary> iterator = payHeadSummaryList.iterator();
		while ((iterator).hasNext()) {
			PayHeadSummary payHead= iterator.next();
			boolean isAllow = false;
			if (taxType.equals(LiveTaxRate.TAX_TYPE_FEDERAL)
					&& Arrays.asList(PayHead.DEFAULT_SSN_TAX, PayHead.DEFAULT_MEDICARE_TAX, PayHead.DEFAULT_FEDERAL)
							.contains(payHead.getPayHeadName())) {
				isAllow = true;
			} else if (taxType.equals(LiveTaxRate.TAX_TYPE_STATE) && payHead.getPayHeadName().startsWith(state)
					&& payHead.getPayHeadName().endsWith("Tax")) {
				isAllow = true;
			}
			if (!(payHead.isDefault() && isAllow && (employeeId == 0 || employeeId == payHead.getEmployeeId()))) {
				iterator.remove();
			}
			payHead.setPayHeadAmount(Math.abs(payHead.getPayHeadAmount()));
			payHead.setEmployeerDeductions(Math.abs(payHead.getEmployeerDeductions()));
		}
		return payHeadSummaryList;
	}

	public ArrayList<PaySlipSummary> getPaySlipSummary(FinanceDate dates,
			FinanceDate dates2, Long companyId) {
		ArrayList<PaySlipSummary> result = new ArrayList<PaySlipSummary>();

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getPaySlipSummary")
				.setParameter("companyId", companyId)
				.setParameter("start", dates.getDate())
				.setParameter("end", dates2.getDate());

		List<Object[]> list = query.list();
		Iterator<Object[]> iterator = list.iterator();

		while (iterator.hasNext()) {
			Object[] type = iterator.next();
			PaySlipSummary summary = new PaySlipSummary();

			summary.setName((String) type[0]);
			summary.setNumber((String) type[1]);
			summary.setAccountNo((String) type[2]);
			summary.setBankName((String) type[3]);
			summary.setBranch((String) type[4]);
			Object object = type[5];
			if (object != null) {
				summary.setAmount((Double) type[5]);
			}
			summary.setEmail((String) type[6]);
			summary.setEmployeeId((Long) type[7]);

			result.add(summary);
		}
		return result;
	}

	public ArrayList<PaySheet> getPaySheet(FinanceDate dates,
			FinanceDate dates2, Long companyId) {
		Session session = HibernateUtil.getCurrentSession();

		Map<Long, Long> inneequeryMap = new HashMap<Long, Long>();
		List l;
		List inerlist = null;

		l = session.getNamedQuery("getPaySheet")
				.setParameter("companyId", companyId)
				.setParameter("start", dates.getDate())
				.setParameter("end", dates2.getDate()).list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<PaySheet> queryResult = new ArrayList<PaySheet>();
		long previousEmployeeID = 0;
		while (iterator.hasNext()) {
			object = (Object[]) iterator.next();
			long employeeId = ((Long) object[0]).longValue();
			if (previousEmployeeID == 0 || previousEmployeeID != employeeId) {
				previousEmployeeID = employeeId;
				PaySheet record = new PaySheet();
				record.setEmployeeId(employeeId == 0 ? 0 : employeeId);
				record.setEmployee(object[1] == null ? null
						: (String) object[1]);
				record.setPayheadId((Long) object[5]);
				long transactionID = (object[4] == null ? 0
						: ((Long) object[4]).longValue());
				long location;
				if (object[2] == null) {
					location = inneequeryMap.get(transactionID);
				} else {
					location = object[2] == null ? 0 : ((Long) object[2])
							.longValue();
				}

				double amount = object[3] == null ? 0 : (Double) object[3];
				record.getMap().put(location, amount);

				queryResult.add(record);
			} else {
				PaySheet record = queryResult.get(queryResult.size() - 1);
				long transactionID = (object[4] == null ? 0
						: ((Long) object[4]).longValue());
				long location;

				if (object[4] == null) {
					location = inneequeryMap.get(transactionID);
				} else {
					location = object[2] == null ? 0 : ((Long) object[2])
							.longValue();
				}
				double amount = object[3] == null ? 0 : (Double) object[3];

				Double double1 = record.getMap().get(location);
				record.getMap().put(location, amount);
				record.setEmployeeId(location);
				record.setPayheadId((Long) object[5]);
			}
		}
		return new ArrayList<PaySheet>(queryResult);
	}

	public ArrayList<PayHeadDetails> getpayHeadDetailsList(long employeeId,
			long payHeadId, FinanceDate startDate, FinanceDate endDate,
			Long companyId) throws AccounterException {
		ArrayList<PayHeadDetails> payHeadDetails = new ArrayList<PayHeadDetails>();
		Session currentSession = HibernateUtil.getCurrentSession();
		Query query = currentSession.getNamedQuery("getpayHeadDetailsList")
				.setParameter("payHeadId", payHeadId)
				.setParameter("employee", employeeId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())
				.setParameter("companyId", companyId);
		List list = query.list();

		Iterator iterator = list.iterator();
		while ((iterator).hasNext()) {
			Object[] object = (Object[]) iterator.next();
			PayHeadDetails payHeadDetail = new PayHeadDetails();
			payHeadDetail.setAmount(object[0] == null ? 0.0
					: (Double) object[0]);
			payHeadDetail.setEmployee(object[1] == null ? ""
					: (String) object[1]);
			payHeadDetail.setPayHead(object[2] == null ? ""
					: (String) object[2]);
			payHeadDetail.setPayHeadType(object[3] == null ? 0
					: (Integer) object[3]);
			payHeadDetail.setTransactionNumber(object[4] == null ? ""
					: (String) object[4]);
			payHeadDetail.setPeriodEndDate(object[5] == null ? 0
					: (Long) object[5]);
			payHeadDetail.setTransactionId(object[6] == null ? 0
					: (Long) object[6]);
			payHeadDetails.add(payHeadDetail);
		}
		return payHeadDetails;
	}

	public PaginationList<PaymentsList> getPayRunsList(Long companyId,
			long startDate, long endDate, int type, int start, int length,
			int transactionType) {
		PaginationList<PaymentsList> payrunsList = new PaginationList<PaymentsList>();
		Session currentSession = HibernateUtil.getCurrentSession();
		Query query = currentSession
				.getNamedQuery(
						transactionType == Transaction.TYPE_PAY_RUN ? "getPayrunsList"
								: "getPayEmployeeList")
				.setParameter("companyId", companyId)
				.setParameter("type", type)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
		List list = query.list();
		int total = 0;
		if (length == -1) {
			list = query.list();
		} else {
			total = query.list().size();
			list = query.setFirstResult(start).setMaxResults(length).list();
		}
		if (list != null && !list.isEmpty()) {
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Object[] object = (Object[]) iterator.next();
				PaymentsList paymentsList = new PaymentsList();
				paymentsList.setName((String) object[0]);
				paymentsList.setTransactionId((Long) object[1]);
				paymentsList.setPaymentNumber((String) object[2]);
				paymentsList.setPaymentDate(new ClientFinanceDate(
						(Long) object[3]));
				paymentsList.setAmountPaid((Double) object[4]);
				paymentsList.setVoided((Boolean) object[5]);
				paymentsList.setType(transactionType);
				if (transactionType == Transaction.TYPE_PAY_EMPLOYEE) {
					paymentsList.setInFavourOf((String) object[6]);
				}
				payrunsList.add(paymentsList);
			}
		}
		payrunsList.setTotalCount(total);
		payrunsList.setStart(start);
		return payrunsList;
	}

	public ArrayList<PaySlipDetail> getPaySlipDetail(long employeeId,
			FinanceDate start, FinanceDate end, Long companyId) {
		Session session = HibernateUtil.getCurrentSession();

		Query attQuery = session.getNamedQuery("getAttendanceDetials")
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.setParameter("start", start.getDate())
				.setParameter("end", end.getDate());

		List list = attQuery.list();

		Iterator iterator = list.iterator();
		ArrayList<PaySlipDetail> queryResult = new ArrayList<PaySlipDetail>();

		while (iterator.hasNext()) {
			PaySlipDetail paySlipDetail = new PaySlipDetail();
			Object[] object = (Object[]) iterator.next();

			paySlipDetail.setId((Long) object[0]);
			paySlipDetail.setName((String) object[1]);
			paySlipDetail.setAmount((Double) object[2]);
			paySlipDetail.setPeriodType(object[3] == null ? 0
					: (Integer) object[3]);
			paySlipDetail.setType((Integer) object[4]);
			paySlipDetail.setEmployeeId((Long) object[5]);
			paySlipDetail.setAttendanceOrProductionType(object[6] == null ? 0
					: (Integer) object[6]);
			paySlipDetail.setUnitName((String) object[7]);
			ClientFinanceDate financeDate = new ClientFinanceDate(
					(Long) object[8]);
			paySlipDetail.setPayDate(financeDate);
			queryResult.add(paySlipDetail);
		}

		Query payheadQuery = session.getNamedQuery("getPayHeadDetials")
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.setParameter("start", start.getDate())
				.setParameter("end", end.getDate());

		list = payheadQuery.list();

		iterator = list.iterator();

		while (iterator.hasNext()) {
			PaySlipDetail paySlipDetail = new PaySlipDetail();
			Object[] object = (Object[]) iterator.next();

			paySlipDetail.setId((Long) object[0]);
			paySlipDetail.setName((String) object[1]);
			paySlipDetail.setAmount((Double) object[2]);
			paySlipDetail.setType((Integer) object[3]);
			ClientFinanceDate financeDate = new ClientFinanceDate(
					(Long) object[4]);
			paySlipDetail.setPayDate(financeDate);
			paySlipDetail.setEmployeerDeduction((Double) object[5]);
			queryResult.add(paySlipDetail);
		}
		
		queryResult.sort(new Comparator<PaySlipDetail>() {@Override
			public int compare(PaySlipDetail o1, PaySlipDetail o2) {
			    boolean earning1 = (o1.isEarning() || o1.getType() == 1);
			    boolean earning2 = (o2.isEarning() || o2.getType() == 1);
				return earning1 ? 1 : earning1== earning2 ?-1:0;
			}
		});

		return queryResult;
	}

	public ClientPayStructure getPayStructure(
			ClientPayStructureDestination selectItem, Long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		long employeeId = 0;
		long groupId = 0;

		if (selectItem instanceof ClientEmployee) {
			employeeId = selectItem.getID();
		} else {
			groupId = selectItem.getID();
		}

		Query query = session.getNamedQuery("getPayStructure")
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.setParameter("groupId", groupId);

		PayStructure result = (PayStructure) query.uniqueResult();

		if (result == null) {
			return null;
		}

		ClientPayStructure clientObject = new ClientConvertUtil()
				.toClientObject(result, ClientPayStructure.class);

		return clientObject;
	}

	public List<ClientTransactionPayEmployee> getTransactionPayEmployeeList(
			ClientPayStructureDestination structureDestination, long companyId)
			throws AccounterException {
		List<ClientTransactionPayEmployee> transactionPayEmployeeList = new ArrayList<ClientTransactionPayEmployee>();
		Session currentSession = HibernateUtil.getCurrentSession();
		List list = currentSession
				.getNamedQuery("getTransactionPayEmployeeList")
				.setParameter(
						"employeeId",
						structureDestination instanceof ClientEmployee ? structureDestination
								.getID() : 0)
				.setParameter(
						"employeeGroupId",
						structureDestination instanceof ClientEmployeeGroup ? structureDestination
								.getID() : 0)
				.setParameter("companyId", companyId).list();

		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			ClientTransactionPayEmployee payEmpTransaction = new ClientTransactionPayEmployee();
			Object[] object = (Object[]) iterator.next();
			payEmpTransaction.setPayRun((Long) object[0]);
			payEmpTransaction.setPayRunNumber((String) object[3]);
			payEmpTransaction.setOriginalAmount((Double) object[4]);
			payEmpTransaction.setAmountDue((Double) object[5]);
			payEmpTransaction.setPayment((Double) object[6]);
			payEmpTransaction.setDate((Long) object[7]);
			transactionPayEmployeeList.add(payEmpTransaction);
		}

		return transactionPayEmployeeList;
	}

	/**
	 * Get User Defineds
	 * 
	 * @param start
	 * @param length
	 * @param companyId
	 * @return
	 * @throws AccounterException
	 */
	public PaginationList<ClientUserDefinedPayHead> getClientUserDefinedPayHead(
			int start, int length, Long companyId) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();
		int total = 0;
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.PayHeads").setEntity(
				"company", company);
		List<UserDefinedPayHead> types = query.list();
		if (types == null) {
			return null;
		}
		if (length == -1) {
			types = query.list();
		} else {
			total = query.list().size();
			types = query.setFirstResult(start).setMaxResults(length).list();
		}
		PaginationList<ClientUserDefinedPayHead> list = new PaginationList<ClientUserDefinedPayHead>();
		for (UserDefinedPayHead type : types) {
			ClientUserDefinedPayHead clientType;
			clientType = new ClientConvertUtil().toClientObject(type,
					ClientUserDefinedPayHead.class);
			list.add(clientType);
		}
		list.setTotalCount(total);
		list.setStart(start);
		return list;

	}

	public List<ClientPayrollTransactionPayTax> getTransactionPayTaxList(
			ClientPayStructureDestination structureDestination, long companyId)
			throws AccounterException {
		List<ClientPayrollTransactionPayTax> transactionPayTaxList = new ArrayList<ClientPayrollTransactionPayTax>();
		Session currentSession = HibernateUtil.getCurrentSession();
		List list = currentSession
				.getNamedQuery("getTransactionPayTaxList")
				.setParameter(
						"employeeId",
						structureDestination instanceof ClientEmployee ? structureDestination
								.getID() : 0)
				.setParameter(
						"employeeGroupId",
						structureDestination instanceof ClientEmployeeGroup ? structureDestination
								.getID() : 0)
				.setParameter("companyId", companyId).list();

		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			ClientPayrollTransactionPayTax payTaxTransaction = new ClientPayrollTransactionPayTax();
			Object[] object = (Object[]) iterator.next();
			payTaxTransaction.setPayRun((Long) object[0]);
			payTaxTransaction.setPayRunNumber((String) object[3]);
			payTaxTransaction.setOriginalAmount((Double) object[4]);
			payTaxTransaction.setAmountDue((Double) object[5]);
			payTaxTransaction.setPayment((Double) object[6]);
			payTaxTransaction.setDate((Long) object[7]);
			transactionPayTaxList.add(payTaxTransaction);
		}

		return transactionPayTaxList;
	}

	}

