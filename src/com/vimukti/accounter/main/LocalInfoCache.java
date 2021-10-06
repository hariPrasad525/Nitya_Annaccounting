package com.vimukti.accounter.main;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.vimukti.accounter.utils.UTF8Control;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class LocalInfoCache {
	private static Map<Locale, DayAndMonthUtil> map = new HashMap<Locale, DayAndMonthUtil>();

	public static DayAndMonthUtil get(Locale locale) {
		DayAndMonthUtil dayAndMonthUtil = map.get(locale);
		if (dayAndMonthUtil == null) {
			dayAndMonthUtil = createDayAndMonthUtil(locale);
			map.put(locale, dayAndMonthUtil);
		}
		return dayAndMonthUtil;
	}

	/**
	 * 
	 * @param locale
	 * @return
	 */
	private static DayAndMonthUtil createDayAndMonthUtil(Locale locale) {
		ResourceBundle bundle = ResourceBundle
				.getBundle(
						"com.vimukti.accounter.web.server.i18n.constants.DateTimeConstantsImpl",
						locale, new UTF8Control());
		DummyDateTimeFormatInfo dateTimeFormatInfo = new DummyDateTimeFormatInfo();
		Set<String> keySet = bundle.keySet();
		for (String key : keySet) {
			String[] values = null;
			Object object = bundle.getObject(key);
			if (object instanceof String) {
				String[] split = object.toString().split(",");
				values = new String[split.length];
				for (int i = 0; i < split.length; i++) {
					values[i] = split[i].trim();
				}
			} else {
				values = (String[]) object;
			}
			setValueToObject(dateTimeFormatInfo, key, values);
		}
		return new DayAndMonthUtil(dateTimeFormatInfo);
	}

	/**
	 * Creating DateTimeFormatInfo Object depending on Keys
	 * 
	 * @param dateTimeFormatInfo
	 * @param key
	 * @param values
	 * @return
	 */
	private static void setValueToObject(
			DummyDateTimeFormatInfo dateTimeFormatInfo, String key,
			String[] values) {
		if (key.equalsIgnoreCase("eras")) {
			dateTimeFormatInfo.seterasShort(values);
		} else if (key.equalsIgnoreCase("eraNames")) {
			dateTimeFormatInfo.setErasFull(values);
		} else if (key.equalsIgnoreCase("narrowMonths")) {
		} else if (key.equalsIgnoreCase("months")) {
			dateTimeFormatInfo.setMonthsFull(values);
		} else if (key.equalsIgnoreCase("shortMonths")) {
			dateTimeFormatInfo.setMonthsShort(values);
		} else if (key.equalsIgnoreCase("standaloneNarrowMonths")) {
			dateTimeFormatInfo.setMonthsNarrowStandalone(values);
		} else if (key.equalsIgnoreCase("standaloneMonths")) {
			dateTimeFormatInfo.setMonthsFullStandalone(values);
		} else if (key.equalsIgnoreCase("standaloneShortMonths")) {
			dateTimeFormatInfo.setMonthsShortStandalone(values);
		} else if (key.equalsIgnoreCase("weekdays")) {
			dateTimeFormatInfo.setWeekdaysFull(values);
		} else if (key.equalsIgnoreCase("shortWeekdays")) {
			dateTimeFormatInfo.setWeekdaysShort(values);
		} else if (key.equalsIgnoreCase("narrowWeekdays")) {
			dateTimeFormatInfo.setWeekdaysNarrow(values);
		} else if (key.equalsIgnoreCase("standaloneWeekdays")) {
			dateTimeFormatInfo.setWeekdaysFullStandalone(values);
		} else if (key.equalsIgnoreCase("standaloneShortWeekdays")) {
			dateTimeFormatInfo.setWeekdaysShortStandalone(values);
		} else if (key.equalsIgnoreCase("standaloneNarrowWeekdays")) {
			dateTimeFormatInfo.setWeekdaysNarrowStandalone(values);
		} else if (key.equalsIgnoreCase("shortQuarters")) {
			dateTimeFormatInfo.setQuartersShort(values);
		} else if (key.equalsIgnoreCase("quarters")) {
			dateTimeFormatInfo.setQuartersFull(values);
		} else if (key.equalsIgnoreCase("ampms")) {
			dateTimeFormatInfo.setAmpms(values);
		} else if (key.equalsIgnoreCase("dateFormats")) {
			dateTimeFormatInfo.setDateFormat(values[0]);
		} else if (key.equalsIgnoreCase("timeFormats")) {
			dateTimeFormatInfo.setTimeFormatFull(values[0]);
		} else if (key.equalsIgnoreCase("firstDayOfTheWeek")) {
			dateTimeFormatInfo
					.setFirstDayOfTheWeek(Integer.parseInt(values[0]));
		} else if (key.equalsIgnoreCase("weekendRange")) {
			dateTimeFormatInfo.setWeekendEnd(Integer.parseInt(values[0]));
		}
	}
}
