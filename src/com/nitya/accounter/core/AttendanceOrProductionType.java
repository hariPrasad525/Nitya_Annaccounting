package com.nitya.accounter.core;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;
import org.json.JSONException;

import com.nitya.accounter.utils.HibernateUtil;
import com.nitya.accounter.web.client.exception.AccounterException;

public class AttendanceOrProductionType extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_LEAVE_WITH_PAY = 4;
	public static final int TYPE_LEAVE_WITHOUT_PAY = 3;
	public static final int TYPE_PRODUCTION = 1;
	public static final int TYPE_USER_DEFINED_CALENDAR = 2;

	private int type;

	/**
	 * Name of the AttendanceOrProductionType
	 */
	private String name;

	/** Will use for Type Production */
	private PayrollUnit unit;

	/** Use for all AttendanceOrProductionType except Production */
	private int periodType;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the unit
	 */
	public PayrollUnit getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(PayrollUnit unit) {
		this.unit = unit;
	}

	/**
	 * @return the periodType
	 */
	public int getPeriodType() {
		return periodType;
	}

	/**
	 * @param periodType
	 *            the periodType to set
	 */
	public void setPeriodType(int periodType) {
		this.periodType = periodType;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!goingToBeEdit) {
			Session session = HibernateUtil.getCurrentSession();

			AttendanceOrProductionType attendanceType = (AttendanceOrProductionType) clientObject;
			Query query = session
					.getNamedQuery("getAttendanceType.by.Name")
					.setParameter("name", attendanceType.name,
							EncryptedStringType.INSTANCE)
					.setParameter("id", attendanceType.getID())
					.setEntity("company", attendanceType.getCompany());
			List list = query.list();
			if (list != null && list.size() > 0) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
			}
		}
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

}
