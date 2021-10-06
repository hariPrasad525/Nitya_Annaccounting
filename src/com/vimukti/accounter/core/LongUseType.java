package com.vimukti.accounter.core;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class LongUseType implements UserType {

	private static final int[] SQL_TYPES = { Types.BIGINT };

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public Class returnedClass() {
		return FinanceDate.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
			return true;
		} else if (x == null || y == null) {
			return false;
		} else {
			return x.equals(y);
		}
	}

	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
			throws HibernateException, SQLException {
		FinanceDate result = null;
		long dateAsLong = resultSet.getLong(names[0]);
		if (!resultSet.wasNull()) {
			result = dateAsLong == 0 ? null : new FinanceDate(dateAsLong);
		}
		return result;
	}

	public void nullSafeSet(PreparedStatement statement, Object value, int index)
			throws HibernateException, SQLException {
		if (value == null) {
			statement.setInt(index, 0);
		} else {
			Long dateAsLong = ((FinanceDate) value).getDate();
			statement.setLong(index, dateAsLong);
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	@Override
	public Object assemble(Serializable arg0, Object arg1)
			throws HibernateException {
		return null;
	}

	@Override
	public Serializable disassemble(Object arg0) throws HibernateException {
		return null;
	}

	@Override
	public int hashCode(Object arg0) throws HibernateException {
		return super.hashCode();
	}

	@Override
	public Object replace(Object arg0, Object arg1, Object arg2)
			throws HibernateException {
		return arg1;
	}

}
