package com.nitya.accounter.license;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class LicensePair {

	byte[] licenseText;

	byte[] hash;

	String originalLicenseString;

	public LicensePair(byte[] license, byte[] hash) {
		this.licenseText = license;
		this.hash = hash;
		originalLicenseString = LicenseManager.packLicense(license, hash);
	}

	public LicensePair(byte[] licenseText, byte[] hash, String originalLicense) {
		this.licenseText = licenseText;
		this.hash = hash;
		this.originalLicenseString = originalLicense;
	}

	public LicensePair(String contactLicenseText) throws LicenseException {
		this.originalLicenseString = contactLicenseText;
		if (!new LicenseManager().canDecode(contactLicenseText)) {
			return;
		}

		try {
			byte[] decodedBytes = Base64.decodeBase64(contactLicenseText
					.getBytes());

			ByteArrayInputStream in = new ByteArrayInputStream(decodedBytes);
			DataInputStream dIn = new DataInputStream(in);

			int textLength = dIn.readInt();
			this.licenseText = new byte[textLength];
			dIn.read(licenseText);

			this.hash = new byte[dIn.available()];
			dIn.read(hash);
		} catch (IOException e) {
			throw new LicenseException(e);
		}

	}

}
