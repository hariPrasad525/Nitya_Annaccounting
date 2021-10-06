package com.nitya.accounter.web.client.i18n;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.i18n.client.impl.cldr.DateTimeFormatInfoImpl;

public class AccounterDateTimeFormatInfoImpl extends DateTimeFormatInfoImpl {

	@Override
	public String[] ampms() {
		return toArray(_ampms());
	}

	private String[] toArray(JsArrayString array) {
		String[] str = new String[array.length()];
		for (int x = 0; x < str.length; x++) {
			str[x] = array.get(x);
		}
		return str;
	}

	public native JsArrayString _ampms() /*-{
		return $wnd['accounter_locale']['ampms'];
	}-*/;

	@Override
	public native String dateFormatFull() /*-{

		return $wnd['accounter_locale']['dateFormats'][0];
	}-*/;

	@Override
	public native String dateFormatLong() /*-{

		return $wnd['accounter_locale']['dateFormats'][1];
	}-*/;

	@Override
	public native String dateFormatMedium() /*-{

		return $wnd['accounter_locale']['dateFormats'][2];
	}-*/;

	@Override
	public native String dateFormatShort() /*-{

		return $wnd['accounter_locale']['dateFormats'][3];
	}-*/;

	@Override
	public String[] erasFull() {
		return toArray(_erasFull());
	}

	public native JsArrayString _erasFull() /*-{
		return $wnd['accounter_locale']['eraNames'];
	}-*/;

	@Override
	public String[] erasShort() {
		return toArray(_erasShort());
	}

	public native JsArrayString _erasShort() /*-{

		return $wnd['accounter_locale']['erasShort'];
	}-*/;

	@Override
	public native int firstDayOfTheWeek() /*-{

		return $wnd['accounter_locale']['firstDayOfTheWeek'];
	}-*/;

	@Override
	public String[] monthsFull() {
		return toArray(_monthsFull());
	}

	public native JsArrayString _monthsFull() /*-{

		return $wnd['accounter_locale']['months'];
	}-*/;

	@Override
	public String[] monthsFullStandalone() {
		return toArray(_monthsFullStandalone());
	}

	public native JsArrayString _monthsFullStandalone() /*-{

		return $wnd['accounter_locale']['standaloneMonths'];
	}-*/;

	@Override
	public String[] monthsNarrow() {
		return toArray(_monthsNarrow());
	}

	public native JsArrayString _monthsNarrow() /*-{

		return $wnd['accounter_locale']['narrowMonths'];
	}-*/;

	@Override
	public String[] monthsNarrowStandalone() {
		return toArray(_monthsNarrowStandalone());
	}

	public native JsArrayString _monthsNarrowStandalone() /*-{

		return $wnd['accounter_locale']['standaloneNarrowMonths'];
	}-*/;

	@Override
	public String[] monthsShort() {
		return toArray(_monthsShort());
	}

	public native JsArrayString _monthsShort() /*-{

		return $wnd['accounter_locale']['shortMonths'];
	}-*/;

	@Override
	public String[] monthsShortStandalone() {
		return toArray(_monthsShortStandalone());
	}

	public native JsArrayString _monthsShortStandalone() /*-{

		return $wnd['accounter_locale']['standaloneShortMonths'];
	}-*/;

	@Override
	public String[] quartersFull() {
		return toArray(_quartersFull());
	}

	public native JsArrayString _quartersFull() /*-{

		return $wnd['accounter_locale']['quarters'];
	}-*/;

	@Override
	public String[] quartersShort() {
		return toArray(_quartersShort());
	}

	public native JsArrayString _quartersShort() /*-{

		return $wnd['accounter_locale']['shortQuarters'];
	}-*/;

	@Override
	public native String timeFormatFull() /*-{

		return $wnd['accounter_locale']['timeFormats'][0];
	}-*/;

	@Override
	public native String timeFormatLong() /*-{

		return $wnd['accounter_locale']['timeFormats'][1];
	}-*/;

	@Override
	public native String timeFormatMedium() /*-{

		return $wnd['accounter_locale']['timeFormats'][2];
	}-*/;

	@Override
	public native String timeFormatShort() /*-{

		return $wnd['accounter_locale']['timeFormats'][3];
	}-*/;

	@Override
	public String[] weekdaysFull() {
		return toArray(_weekdaysFull());
	}

	public native JsArrayString _weekdaysFull() /*-{

		return $wnd['accounter_locale']['weekdays'];
	}-*/;

	@Override
	public String[] weekdaysFullStandalone() {
		return toArray(_weekdaysFullStandalone());
	}

	public native JsArrayString _weekdaysFullStandalone() /*-{

		return $wnd['accounter_locale']['standaloneWeekdays'];
	}-*/;

	@Override
	public String[] weekdaysNarrow() {
		return toArray(_weekdaysNarrow());
	}

	public native JsArrayString _weekdaysNarrow() /*-{

		return $wnd['accounter_locale']['narrowWeekdays'];
	}-*/;

	@Override
	public String[] weekdaysNarrowStandalone() {
		return toArray(_weekdaysNarrowStandalone());
	}

	public native JsArrayString _weekdaysNarrowStandalone() /*-{

		return $wnd['accounter_locale']['standaloneNarrowWeekdays'];
	}-*/;

	@Override
	public String[] weekdaysShort() {
		return toArray(_weekdaysShort());
	}

	public native JsArrayString _weekdaysShort() /*-{

		return $wnd['accounter_locale']['shortWeekdays'];
	}-*/;

	@Override
	public String[] weekdaysShortStandalone() {
		return toArray(_weekdaysShortStandalone());
	}

	public native JsArrayString _weekdaysShortStandalone() /*-{
		return $wnd['accounter_locale']['standaloneShortWeekdays'];
	}-*/;

	@Override
	public native int weekendEnd() /*-{
		return $wnd['accounter_locale']['weekendRange'][1];
	}-*/;

	@Override
	public native int weekendStart() /*-{
		return $wnd['accounter_locale']['weekendRange'][0];
	}-*/;

}
