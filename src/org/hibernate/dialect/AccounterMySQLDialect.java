package org.hibernate.dialect;

import java.sql.Types;

public class AccounterMySQLDialect extends MySQLInnoDBDialect {

	@Override
	public String getTableTypeString() {
		StringBuilder sb = new StringBuilder(super.getTableTypeString());
		sb.append(" DEFAULT CHARSET=utf8");
		return sb.toString();
	}

	public AccounterMySQLDialect() {
		registerColumnType(Types.BIGINT, "bigint(20)");
		registerHibernateType(Types.LONGVARCHAR, "longvarchar");
	}

}
