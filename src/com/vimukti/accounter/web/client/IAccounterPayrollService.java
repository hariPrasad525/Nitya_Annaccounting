package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
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
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.reports.PaySheet;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.core.reports.PaySlipSummary;
import com.vimukti.accounter.web.client.exception.AccounterException;

public interface IAccounterPayrollService extends RemoteService {

	public PaginationList<ClientEmployee> getEmployees(boolean isActive,
			int start, int lenght) throws AccounterException;

	public PaginationList<ClientPayHead> getPayheads(int start, int length)
			throws AccounterException;

	public PaginationList<ClientPayrollUnit> getPayrollUnitsList(int start,
			int length) throws AccounterException;

	ArrayList<ClientEmployeePayHeadComponent> getEmployeePayHeadComponents(
			List<ClientAttendanceManagementItem> list,
			ClientPayStructureDestination selectItem,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			Long noOfWorkingDays) throws AccounterException;

	PaginationList<ClientPayStructureList> getPayStructures(int start,
			int length) throws AccounterException;

	PaginationList<ClientAttendanceOrProductionType> getAttendanceProductionTypes(int start,
			int length) throws AccounterException;
	
	PaginationList<IAccounterCore> getAttendanceProductionOrUserDefined(int start,
			int length) throws AccounterException;
	
	ArrayList<ClientEmployeeGroup> getEmployeeGroups()
			throws AccounterException;

	List<ClientPayStructureDestination> getEmployeesAndGroups()
			throws AccounterException;

	long getEmployeeAttendanceCurrentBal(long employee, long attendanceType)
			throws AccounterException;

	ArrayList<ClientEmployee> getEmployeesByGroup(
			ClientPayStructureDestination employeeGroup)
			throws AccounterException;

	ArrayList<PaySlipSummary> getPaySlipSummary(ClientFinanceDate start,
			ClientFinanceDate clientFinanceDate) throws AccounterException;

	ArrayList<PaySheet> getPaySheet(ClientFinanceDate start,
			ClientFinanceDate end) throws AccounterException;

	ArrayList<PaySlipDetail> getPaySlipDetail(long employeeId,
			ClientFinanceDate start, ClientFinanceDate end)
			throws AccounterException;

	ClientPayStructure getPayStructure(ClientPayStructureDestination selectItem)
			throws AccounterException;

	List<ClientTransactionPayEmployee> getTransactionPayEmployeeList(
			ClientPayStructureDestination structureDestination)
			throws AccounterException;
	
	List<ClientPayrollTransactionPayTax> getTransactionPayTaxList(
			ClientPayStructureDestination structureDestination)
			throws AccounterException;
}
