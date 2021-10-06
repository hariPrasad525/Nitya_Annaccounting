package com.nitya.accounter.web.client.ui.react;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.nitya.accounter.web.client.core.IAccounterCore;

import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class ReactGwt {

	@JsFunction
	interface ReactToGwtHandler {
		boolean execute(String action, Element element, Object data, GwtToReactHandler gwtToReactHandler);
	}

	@JsFunction
	interface GwtToReactHandler {
		boolean execute(String action, Element element, Object data);
	}

	/**
	 * This is a static class.
	 */
	private ReactGwt() {
	}

	public static native Element renderElement(Element parentElement, String type, IAccounterCore object,
			List<IAccounterCore> objects, ReactToGwtHandler handler);

	public static native void unRenderElement(Element parentElement, Element element, String type);

}
