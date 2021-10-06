/**
 * 
 */
package com.nitya.accounter.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.web.client.core.AccountsTemplate;
import com.nitya.accounter.web.client.core.TemplateAccount;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccountsTemplateManager {

	public ArrayList<AccountsTemplate> loadAccounts(Locale locale)
			throws IOException {

		XStream xStream = new XStream(new DomDriver());

		xStream.alias("industries", List.class);

		xStream.alias("industry", AccountsTemplate.class);
		xStream.useAttributeFor(AccountsTemplate.class, "type");
		xStream.useAttributeFor(AccountsTemplate.class, "name");

		xStream.alias("account", TemplateAccount.class);
		xStream.useAttributeFor(TemplateAccount.class, "type");
		xStream.useAttributeFor(TemplateAccount.class, "name");
		xStream.useAttributeFor(TemplateAccount.class, "defaultValue");
		xStream.useAttributeFor(TemplateAccount.class, "isSystemOnly");
		xStream.useAttributeFor(TemplateAccount.class, "cashFlowType");
		xStream.useAttributeFor(TemplateAccount.class, "countries");

		xStream.aliasField("default", TemplateAccount.class, "defaultValue");
		xStream.aliasField("systemOnly", TemplateAccount.class, "isSystemOnly");

		File file = getFile(locale);

		Object object = xStream.fromXML(new FileInputStream(file));

		return (ArrayList<AccountsTemplate>) object;

	}

	/**
	 * @param language
	 * @return
	 */
	private File getFile(Locale locale) {
		File file = new File(ServerConfiguration.getAccountsDir(),
				getResourceName(locale));
		if (file.exists()) {
			return file;
		}
		return new File(ServerConfiguration.getAccountsDir(), "/accounts.xml");
	}

	private String getResourceName(Locale locale) {
		return toResourceName(toBundleName("accounts", locale), "xml");
	}

	public String toBundleName(String baseName, Locale locale) {
		if (locale == Locale.ROOT) {
			return baseName;
		}

		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		if (language == "" && country == "" && variant == "") {
			return baseName;
		}

		StringBuilder sb = new StringBuilder(baseName);
		sb.append('_');
		if (variant != "") {
			sb.append(language).append('_').append(country).append('_')
					.append(variant);
		} else if (country != "") {
			sb.append(language).append('_').append(country);
		} else {
			sb.append(language);
		}
		return sb.toString();
	}

	public final String toResourceName(String bundleName, String suffix) {
		StringBuilder sb = new StringBuilder(bundleName.length() + 1
				+ suffix.length());
		sb.append(bundleName.replace('.', '/')).append('.').append(suffix);
		return sb.toString();
	}
}
