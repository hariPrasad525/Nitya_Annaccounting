package com.vimukti.accounter.main;

import java.util.Locale;

import com.vimukti.accounter.web.server.languages.Arabic;
import com.vimukti.accounter.web.server.languages.Chinees;
import com.vimukti.accounter.web.server.languages.English;
import com.vimukti.accounter.web.server.languages.French;
import com.vimukti.accounter.web.server.languages.German;
import com.vimukti.accounter.web.server.languages.Ilanguage;
import com.vimukti.accounter.web.server.languages.Indonesian;
import com.vimukti.accounter.web.server.languages.Italian;
import com.vimukti.accounter.web.server.languages.Japanees;
import com.vimukti.accounter.web.server.languages.Korean;
import com.vimukti.accounter.web.server.languages.Nederlands;
import com.vimukti.accounter.web.server.languages.Polish;
import com.vimukti.accounter.web.server.languages.Portuguese;
import com.vimukti.accounter.web.server.languages.Russian;
import com.vimukti.accounter.web.server.languages.Spanish;
import com.vimukti.accounter.web.server.languages.Thai;
import com.vimukti.accounter.web.server.languages.Turkish;
import com.vimukti.accounter.web.server.languages.Ukrainian;

public class ServerLocal {
	private static ThreadLocal<Locale> local = new ThreadLocal<Locale>();

	public static Locale get() {
		return local.get();
	}

	public static void set(Locale locale) {
		local.set(locale);
	}

	public static Ilanguage getLocaleLanguage() {
		Ilanguage languageobj = null;
		String displayLanguage = get().getLanguage();
		if (displayLanguage.equalsIgnoreCase("ar")) {
			languageobj = new Arabic();
		} else if (displayLanguage.equalsIgnoreCase("zh")) {
			languageobj = new Chinees();
		} else if (displayLanguage.equalsIgnoreCase("fr")) {
			languageobj = new French();
		} else if (displayLanguage.equalsIgnoreCase("de")) {
			languageobj = new German();
		} else if (displayLanguage.equalsIgnoreCase("id")) {
			languageobj = new Indonesian();
		} else if (displayLanguage.equalsIgnoreCase("it")) {
			languageobj = new Italian();
		} else if (displayLanguage.equalsIgnoreCase("ja")) {
			languageobj = new Japanees();
		} else if (displayLanguage.equalsIgnoreCase("ko")) {
			languageobj = new Korean();
		} else if (displayLanguage.equalsIgnoreCase("nl")) {
			languageobj = new Nederlands();
		} else if (displayLanguage.equalsIgnoreCase("pl")) {
			languageobj = new Polish();
		} else if (displayLanguage.equalsIgnoreCase("pt")) {
			languageobj = new Portuguese();
		} else if (displayLanguage.equalsIgnoreCase("ru")) {
			languageobj = new Russian();
		} else if (displayLanguage.equalsIgnoreCase("es")) {
			languageobj = new Spanish();
		} else if (displayLanguage.equalsIgnoreCase("th")) {
			languageobj = new Thai();
		} else if (displayLanguage.equalsIgnoreCase("tr")) {
			languageobj = new Turkish();
		} else if (displayLanguage.equalsIgnoreCase("uk")) {
			languageobj = new Ukrainian();
		} else {
			languageobj = new English();
		}
		return languageobj;
	}
}
