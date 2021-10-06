package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.Global;

public class JNSI {
	public static native boolean getIsAdmin(String jsVar)/*-{

		var isAdmin = eval('$wnd.' + jsVar);

		if (isAdmin != null) {
			return isAdmin;
		} else {
			return false;
		}
	}-*/;

	public static native void log(String message)/*-{
		if (window.console != undefined) {
			console.log(message);
		}
	}-*/;

	public static String getCalcultedAmount(String str) {
		String decimalCharacter = Global.get().preferences()
				.getDecimalCharacter();
		boolean hasDecimalSymbol = decimalCharacter != null
				&& !decimalCharacter.isEmpty();
		if (hasDecimalSymbol) {
			String replaceChar = String.valueOf(decimalCharacter.charAt(0));
			str = str.replace(replaceChar, ".");
		}
		String result = getCalcultedDecimalAmount(str);

		return result;
	}

	private static native String getCalcultedDecimalAmount(String str)/*-{
		str = str.replace(/[^0-9\+\-\.x\*\/]/gi, '');
		str = str.replace(/0*([0-9\.]+)/gi, '$1');
		str = str.replace(/[^0-9\+\-\.x\*\/]/gi, '');
		str = str.replace(/^[^0-9\.\-]+/gi, '');
		str = str.replace(/[^0-9\.]+$/gi, '');
		str = str.replace(/x/gi, '*');
		str = str.replace(/([\-\+\*\/])+/gi, '$1');
		return String(eval(str));
	}-*/;

	public static native Object getFeatures()/*-{
		return eval('$wnd.features');
	}-*/;

	public static native String readNumber(String value)/*-{
			var number = value.match(/\d+/g);
			return String(number);
		}-*/;
}
