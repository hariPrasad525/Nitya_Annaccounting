/**
 * 
 */
package com.vimukti.accounter.mobile;

import com.google.gson.Gson;
import com.vimukti.accounter.mobile.xtream.JResult;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileApplicationAdaptor implements MobileAdaptor {

	public static MobileAdaptor INSTANCE = new MobileApplicationAdaptor();

	@Override
	public String postProcess(Result result) {
		JResult jResult = new JResult();
		jResult.cookie = result.getCookie();
		jResult.title = result.getTitle();
		jResult.hideCancel = result.isHideCancel();
		jResult.showBack = result.isShowBack();
		jResult.addAll(result.resultParts);
		String json = new Gson().toJson(jResult);
		return json + "\n";
	}
}