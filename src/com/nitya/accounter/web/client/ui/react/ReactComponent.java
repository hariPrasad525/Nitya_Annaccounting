package com.nitya.accounter.web.client.ui.react;

import com.google.gwt.dom.client.Element;

import jsinterop.annotations.JsType;

/**
 * @author kumar kasimala
 *
 */

@JsType
public class ReactComponent {
	
	protected ReactComponent() {
	}
	
	public native Element getElement();
	
	public native Element getData();
	
	public native void setData(Object data);
	
	

}
