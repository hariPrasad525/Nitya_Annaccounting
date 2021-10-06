package com.vimukti.accounter.utils;

import java.io.UnsupportedEncodingException;

import org.hibernate.dialect.Encryptor;

import com.vimukti.accounter.core.EU;

public class AccounterEncryptor implements Encryptor {

	private static AccounterEncryptor instance;

	@Override
	public byte[] encrypt(String value) {
		if (value == null) {
			return null;
		}
		if (isActive()) {
			return EU.e(value);
		}
		return fromUTF8String(value);
	}

	private boolean isActive() {
		return true;
	}

	@Override
	public String decrypt(byte[] value) {
		if (value == null) {
			return null;
		}
		if (isActive()) {
			return EU.d(value);
		}
		return toUTF8String(value);
	}

	private String toUTF8String(byte[] value) {
		try {
			String string = new String(value, "UTF-8");
			return string;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] fromUTF8String(String value) {
		try {
			byte[] bytes = value.getBytes("UTF-8");
			return bytes;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static AccounterEncryptor getInstance() {
		if (instance == null) {
			instance = new AccounterEncryptor();
		}
		return instance;
	}

}
