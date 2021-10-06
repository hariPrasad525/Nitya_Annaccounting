package com.nitya.accounter.core;

import java.util.List;

import org.json.JSONException;

import com.nitya.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar g
 * 
 */
public class ComputaionFormulaFunction implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int FUNCTION_ADD_PAY_HEAD = 1;

	public static final int FUNCTION_SUBSTRACT_PAY_HEAD = 2;

	public static final int FUNCTION_DIVIDE_ATTENDANCE = 3;

	public static final int FUNCTION_MULTIPLY_ATTENDANCE = 4;

	private int functionType;

	private PayHead payHead;

	private AttendanceOrProductionType attendanceType;

	/**
	 * @return the functionType
	 */
	public int getFunctionType() {
		return functionType;
	}

	/**
	 * @param functionType
	 *            the functionType to set
	 */
	public void setFunctionType(int functionType) {
		this.functionType = functionType;
	}

	/**
	 * @return the payHead
	 */
	public PayHead getPayHead() {
		return payHead;
	}

	/**
	 * @param payHead
	 *            the payHead to set
	 */
	public void setPayHead(PayHead payHead) {
		this.payHead = payHead;
	}

	public AttendanceOrProductionType getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(AttendanceOrProductionType attendanceType) {
		this.attendanceType = attendanceType;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public double calculatePayment(ClientEmployeePayHeadComponent component, PayStructureItem payStructureItem,
			double deductions, double earnings) {
		List<PayStructureItem> items = payStructureItem.getPayStructure()
				.getItems();
		PayStructureItem payHeadStructureItem = null;
		for (PayStructureItem structureItem : items) {
			if (structureItem.getPayHead().getID() == this.getPayHead().getID()) {
				payHeadStructureItem = structureItem;
				break;
			}
		}
		if (payHeadStructureItem != null) {
			payHeadStructureItem.setStartDate(payStructureItem.getStartDate());
			payHeadStructureItem.setEndDate(payStructureItem.getEndDate());
			payHeadStructureItem
					.setAttendance(payStructureItem.getAttendance());
			return payHead.calculatePayment(component, payHeadStructureItem, deductions,
					earnings);
		}
		return 0.0;
	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

}
