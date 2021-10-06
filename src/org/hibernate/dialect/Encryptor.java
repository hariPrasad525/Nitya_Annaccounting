package org.hibernate.dialect;

public interface Encryptor {
	public byte[] encrypt(String value);

	public String decrypt(byte[] value);

}
