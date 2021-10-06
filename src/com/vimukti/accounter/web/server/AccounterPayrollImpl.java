package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.IAccounterPayrollService;
import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayStructureList;
import com.vimukti.accounter.web.client.core.ClientPayrollTransactionPayTax;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.ClientTransactionPayEmployee;
import com.vimukti.accounter.web.client.core.ClientUserDefinedPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.PaySheet;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.core.reports.PaySlipSummary;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class AccounterPayrollImpl extends AccounterRPCBaseServiceImpl implements
		IAccounterPayrollService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public PaginationList<ClientEmployee> getEmployees(boolean isActive,
			int start, int lenght) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		return tool.getPayrollManager().getEmployees(isActive, start, lenght,
				getCompanyId());
	}

	@Override
	public PaginationList<ClientPayHead> getPayheads(int start, int length)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		return tool.getPayrollManager().getPayheadsList(start, length,
				getCompanyId());
	}

	@Override
	public PaginationList<ClientPayrollUnit> getPayrollUnitsList(int start,
			int length) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		return tool.getPayrollManager().getPayrollUnitsList(start, length,
				getCompanyId());
	}

	@Override
	public ArrayList<ClientEmployeePayHeadComponent> getEmployeePayHeadComponents(
			List<ClientAttendanceManagementItem> attendanceItems,
			ClientPayStructureDestination selectItem,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			Long noOfWorkingDays) throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(startDate, endDate,
				getCompanyId());
		Session currentSession = HibernateUtil.getCurrentSession();
		Employee employee = null;
		if (selectItem instanceof ClientEmployee) {
			employee = (Employee) currentSession.get(Employee.class,
					selectItem.getID());
		}
		return selectItem instanceof ClientEmployee ? getFinanceTool()
				.getPayrollManager().getEmployeePayHeadComponents(dates[0],
						dates[1], employee, getCompanyId(), attendanceItems,
						noOfWorkingDays) : getFinanceTool().getPayrollManager()
				.getEmployeeGroupPayHeadComponents(dates[0], dates[1],
						selectItem, getCompanyId(), attendanceItems,
						noOfWorkingDays);
	}

	public ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate(
			long companyId) {
		List<ClientFinanceDate> transactionDates = new ArrayList<ClientFinanceDate>();
		try {

			ClientFinanceDate[] dates = getFinanceTool().getManager()
					.getMinimumAndMaximumTransactionDate(getCompanyId());
			transactionDates.add(dates[0]);
			transactionDates.add(dates[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ClientFinanceDate>(transactionDates);
	}

	private FinanceDate[] getMinimumAndMaximumDates(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		// if ((startDate.equals("") || startDate == null)
		// || (endDate.equals("") || endDate == null)) {

		List<ClientFinanceDate> dates = getMinimumAndMaximumTransactionDate(companyId);
		ClientFinanceDate startDate1 = dates.get(0) == null ? new ClientFinanceDate()
				: dates.get(0);
		ClientFinanceDate endDate2 = dates.get(1) == null ? new ClientFinanceDate()
				: dates.get(1);

		FinanceDate transtartDate;
		if (startDate == null || startDate.isEmpty())
			transtartDate = new FinanceDate(startDate1);
		else
			transtartDate = new FinanceDate(startDate.getDate());
		FinanceDate tranendDate;
		if (endDate == null || endDate.isEmpty())
			tranendDate = new FinanceDate(endDate2);
		else
			tranendDate = new FinanceDate(endDate.getDate());

		return new FinanceDate[] { transtartDate, tranendDate };
	}

	@Override
	public PaginationList<ClientPayStructureList> getPayStructures(int start,
			int length) throws AccounterException {
		return getFinanceTool().getPayrollManager().getPayrollStructuresList(
				start, length, getCompanyId());
	}

	@Override
	public PaginationList<ClientAttendanceOrProductionType> getAttendanceProductionTypes(
			int start, int length) throws AccounterException {
		return getFinanceTool().getPayrollManager()
				.getAttendanceProductionTypes(start, length, getCompanyId());
	}

	@Override
	public PaginationList<IAccounterCore> getAttendanceProductionOrUserDefined(
			int start, int length) throws AccounterException {
		PaginationList<IAccounterCore> accounterCores = new PaginationList<IAccounterCore>();
		// GET ATTENDANCE OR PRODUCTION TYPE
		PaginationList<ClientAttendanceOrProductionType> attTypes = getFinanceTool()
				.getPayrollManager().getAttendanceProductionTypes(start,
						length, getCompanyId());
		accounterCores.addAll(attTypes);

		// GET USER DEFINED PAYHEADS
		PaginationList<ClientUserDefinedPayHead> payHeads = getFinanceTool()
				.getPayrollManager().getClientUserDefinedPayHead(start,
						length, getCompanyId());
		accounterCores.addAll(payHeads);

		return accounterCores;
	}

	@Override
	public ArrayList<ClientEmployeeGroup> getEmployeeGroups()
			throws AccounterException {
		return getFinanceTool().getPayrollManager().getEmployeeGroups(
				getCompanyId());
	}

	@Override
	public List<ClientPayStructureDestination> getEmployeesAndGroups()
			throws AccounterException {
		return getFinanceTool().getPayrollManager().getEmployeesAndGroups(
				getCompanyId());
	}

	@Override
	public long getEmployeeAttendanceCurrentBal(long employee,
			long attendanceType) throws AccounterException {
		return getFinanceTool().getPayrollManager()
				.getEmployeeAttendanceCurrentBal(employee, attendanceType,
						getCompanyId());
	}

	@Override
	public ArrayList<ClientEmployee> getEmployeesByGroup(
			ClientPayStructureDestination employeeGroup)
			throws AccounterException {
		return getFinanceTool().getPayrollManager().getEmployeesByGroup(
				employeeGroup, getCompanyId());
	}

	@Override
	public ArrayList<PaySlipSummary> getPaySlipSummary(ClientFinanceDate start,
			ClientFinanceDate end) throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(start, end,
				getCompanyId());

		ArrayList<PaySlipSummary> paySlipSummary = getFinanceTool()
				.getPayrollManager().getPaySlipSummary(dates[0], dates[1],
						getCompanyId());

		PaySlipSummary obj = new PaySlipSummary();
		if (paySlipSummary != null)
			paySlipSummary.add((PaySlipSummary) setStartEndDates(obj, dates));

		return paySlipSummary;
	}

	@Override
	public ArrayList<PaySheet> getPaySheet(ClientFinanceDate start,
			ClientFinanceDate end) throws AccounterException {
		FinanceDate[] dates = getMinimumAndMaximumDates(start, end,
				getCompanyId());

		ArrayList<PaySheet> paySheet = getFinanceTool().getPayrollManager()
				.getPaySheet(dates[0], dates[1], getCompanyId());

		PaySheet obj = new PaySheet();
		if (paySheet != null)
			paySheet.add((PaySheet) setStartEndDates(obj, dates));

		return paySheet;
	}

	@Override
	public ArrayList<PaySlipDetail> getPaySlipDetail(long employeeId,
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException {

		FinanceDate[] dates = getMinimumAndMaximumDates(start, end,
				getCompanyId());

		ArrayList<PaySlipDetail> paySlipDetail = getFinanceTool()
				.getPayrollManager().getPaySlipDetail(employeeId, dates[0],
						dates[1], getCompanyId());

		PaySlipDetail obj = new PaySlipDetail();
		if (paySlipDetail != null)
			paySlipDetail.add((PaySlipDetail) setStartEndDates(obj, dates));

		return paySlipDetail;
	}

	private BaseReport setStartEndDates(BaseReport obj,
			FinanceDate[] financeDates) {
		obj.setStartDate(financeDates[0].toClientFinanceDate());
		obj.setEndDate(financeDates[1].toClientFinanceDate());
		return obj;
	}

	@Override
	public ClientPayStructure getPayStructure(
			ClientPayStructureDestination selectItem) throws AccounterException {
		return getFinanceTool().getPayrollManager().getPayStructure(selectItem,
				getCompanyId());
	}

	public List<ClientTransactionPayEmployee> getTransactionPayEmployeeList(
			ClientPayStructureDestination structureDestination)
			throws AccounterException {
		return getFinanceTool().getPayrollManager()
				.getTransactionPayEmployeeList(structureDestination,
						getCompanyId());
	}
											
	@Override
	public List<ClientPayrollTransactionPayTax> getTransactionPayTaxList(ClientPayStructureDestination structureDestination)
			throws AccounterException {
		return getFinanceTool().getPayrollManager()
				.getTransactionPayTaxList(structureDestination,
						getCompanyId());
	}
}
