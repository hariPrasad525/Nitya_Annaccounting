package org.hibernate.dialect;

public class AccounterPgSQLDialect extends PostgreSQLDialect {

	@Override
	public String getAddForeignKeyConstraintString(String constraintName,
			String[] foreignKey, String referencedTable, String[] primaryKey,
			boolean referencesPrimaryKey) {
		return super.getAddForeignKeyConstraintString(constraintName,
				foreignKey, referencedTable, primaryKey, referencesPrimaryKey)
				+ " deferrable initially deferred";
	}

}
