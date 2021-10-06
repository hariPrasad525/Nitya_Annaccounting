package org.hibernate.dialect;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;

import com.nitya.accounter.utils.AccounterEncryptor;

public class EncryptedStringType extends
		AbstractSingleColumnStandardBasicType<String> implements
		DiscriminatorType<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static EncryptedStringType INSTANCE = new EncryptedStringType();

	private EncryptedStringType() {
		super(BinaryTypeDescriptor.INSTANCE, new EncryptedStringTypeDescriptor(
				AccounterEncryptor.getInstance()));
	}

	public String getName() {
		return "encryptedstring";
	}

	@Override
	protected boolean registerUnderJavaType() {
		return false;
	}

	public String objectToSQLString(String value, Dialect dialect)
			throws Exception {
		return '\'' + value + '\'';
	}

	public String stringToObject(String xml) throws Exception {
		return xml;
	}

	public String toString(String value) {
		return value;
	}
}
