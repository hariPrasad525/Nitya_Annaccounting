package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.Element;

public class AccounterDOM {

	public static void addStyleToparent(Element ele, String className) {
		Element parent = ele.getParentElement().cast();
		parent.addClassName(className);
	}

	public static void setParentElementWidth(Element ele, int width) {
		Element parent = ele.getParentElement().cast();
		parent.setAttribute("width", width + "%");
	}

	public static void setParentElementHeight(Element ele, int height) {
		Element parent = ele.getParentElement().cast();
		parent.setAttribute("height", height + "%");
	}

	public static void setAttribute(Element e, String name, String value) {
		Element parent = e.getParentElement().cast();
		parent.setAttribute(name, value);
	}

	public static void setAttrib(Element e, String name, String value) {
		e.setAttribute(name, value);
	}

	public static void setAttribToParent(Element e, String name, String value) {
		e.getParentElement().setAttribute(name, value);
	}
}
