package org.hibernate.dialect;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

public class EncryptedStringTypeDescriptor extends
		AbstractTypeDescriptor<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Encryptor encryptor;


	public EncryptedStringTypeDescriptor(Encryptor encryptor) {
		super(String.class);
		this.encryptor=encryptor;
	}

	public String toString(String value) {
		return value;
	}

	public String fromString(String string) {
		return string;
	}

	@SuppressWarnings({ "unchecked" })
	public <X> X unwrap(String value, Class<X> type, WrapperOptions options) {
		if (value == null) {
			return null;
		}
		if (byte[].class.isAssignableFrom(type)) {
			if (encryptor == null) {
				return (X) value.getBytes();
			} else {
				return (X) encryptor.encrypt(value);
			}

		}
		throw unknownUnwrap(type);
	}

	public <X> String wrap(X value, WrapperOptions options) {
		if (value == null) {
			return null;
		}
		if (byte[].class.isInstance(value)) {
			if (encryptor == null) {
				return new String((byte[]) value);
			} else {
				return encryptor.decrypt((byte[]) value);
			}
		}

		throw unknownWrap(value.getClass());
	}
}
