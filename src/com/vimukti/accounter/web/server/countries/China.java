package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class China extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Anhui", "Aomen", "Beijing",
				"Chongqing", "Fujian", "Gansu", "Guangdong", "Guangxi",
				"Guizhou", "Hainan", "Hebei", "Heilongjiang", "Henan",
				"Hongkong", "Hubei", "Hunan", "Jiangsu", "Jiangxi", "Jilin",
				"Liaoning", "Neimenggu", "Ningxia", "Qinghai", "Shaanxi",
				"Shandong", "Shanghai", "Shanxi", "Sichuan", "Tianjin",
				"Xinjiang", "Xizang", "Yunnan", "Zhejiang" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CNY";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+8:00 Asia/Chongqing";
		//TODO
		// Asia/Shanghai east China - Beijing, Guangdong, Shanghai, etc.
		// Asia/Harbin Heilongjiang (except Mohe), Jilin
		// Asia/Chongqing central China - Sichuan, Yunnan, Guangxi, Shaanxi,
		// Guizhou, etc.
		// Asia/Urumqi most of Tibet & Xinjiang
		// Asia/Kashgar west Tibet & Xinjiang

	}

}
