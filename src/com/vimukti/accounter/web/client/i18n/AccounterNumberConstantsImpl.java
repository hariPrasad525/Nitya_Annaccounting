package com.vimukti.accounter.web.client.i18n;

import com.google.gwt.i18n.client.constants.NumberConstantsImpl;

public class AccounterNumberConstantsImpl implements NumberConstantsImpl {

	@Override
	public native String notANumber() /*-{
		return $wnd['accounter_locale']['notANumber'];
	}-*/;

	@Override
	public native String currencyPattern() /*-{
		return $wnd['accounter_locale']['currencyPattern'];
	}-*/;

	@Override
	public native String decimalPattern() /*-{
		return $wnd['accounter_locale']['decimalPattern'];
	}-*/;

	@Override
	public native String decimalSeparator() /*-{
		return $wnd['accounter_locale']['decimalSeparator'];
	}-*/;

	@Override
	public native String defCurrencyCode() /*-{
		return $wnd['accounter_locale']['defCurrencyCode'];
	}-*/;

	@Override
	public native String exponentialSymbol() /*-{
		return $wnd['accounter_locale']['exponentialSymbol'];
	}-*/;

	@Override
	public native String groupingSeparator() /*-{
		return $wnd['accounter_locale']['groupingSeparator'];
	}-*/;

	@Override
	public native String infinity() /*-{
		return $wnd['accounter_locale']['infinity'];
	}-*/;

	@Override
	public native String minusSign() /*-{
		return $wnd['accounter_locale']['minusSign'];
	}-*/;

	@Override
	public native String monetaryGroupingSeparator() /*-{
		return $wnd['accounter_locale']['monetaryGroupingSeparator'];
	}-*/;

	@Override
	public native String monetarySeparator() /*-{
		return $wnd['accounter_locale']['monetarySeparator'];
	}-*/;

	@Override
	public native String percent() /*-{
		return $wnd['accounter_locale']['percent'];
	}-*/;

	@Override
	public native String percentPattern() /*-{
		return $wnd['accounter_locale']['percentPattern'];
	}-*/;

	@Override
	public native String perMill() /*-{
		return $wnd['accounter_locale']['perMill'];
	}-*/;

	@Override
	public native String plusSign() /*-{
		return $wnd['accounter_locale']['plusSign'];
	}-*/;

	@Override
	public native String scientificPattern() /*-{
		return $wnd['accounter_locale']['scientificPattern'];
	}-*/;

	@Override
	public native String zeroDigit() /*-{
		return $wnd['accounter_locale']['zeroDigit'];
	}-*/;

	@Override
	public native String globalCurrencyPattern() /*-{
		return $wnd['accounter_locale']['currencyPattern'];
	}-*/;

	@Override
	public native String simpleCurrencyPattern() /*-{
		return $wnd['accounter_locale']['currencyPattern'];
	}-*/;

}
