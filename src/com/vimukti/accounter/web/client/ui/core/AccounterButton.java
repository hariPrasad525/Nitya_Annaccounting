//package com.vimukti.accounter.web.client.ui.core;
//
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.safehtml.shared.SafeHtml;
//import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.Element;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.Image;
//import com.vimukti.accounter.web.client.theme.ThemesUtil;
//import com.vimukti.accounter.web.client.ui.Accounter;
//import com.vimukti.accounter.web.client.ui.ImageButton;
//
//public class Button extends ImageButton {
//
//	private int type;
//
//	public Button() {
//	}
//
//	public Button(SafeHtml html) {
//		super(html);
//	}
//
//	public Button(String html) {
//		super(html);
//	}
//
//	public Button(Element element) {
//		super(element);
//	}
//
//	public Button(SafeHtml html, ClickHandler handler) {
//		super(html, handler);
//	}
//
//	public Button(String html, ClickHandler handler) {
//		super(html, handler);
//	}
//
//	public int getType() {
//		return type;
//	}
//
//	public void setType(int type) {
//		this.type = type;
//	}
//
//	@Override
//	public void setEnabled(boolean enabled) {
//		super.setEnabled(enabled);
//		if (enabled) {
//			if (this.type == ADD_NEW_BUTTON) {
//				disabledAddNewButton();
//				enabledAddNewButton();
//			} else if (this.type == ADD_BUTTON) {
//				disabledAddButton();
//				enabledAddButton();
//			} else {
//				disabledButton();
//				enabledButton();
//			}
//
//		} else {
//			if (this.type == ADD_BUTTON) {
//				disabledAddButton();
//			} else if (this.type == ADD_NEW_BUTTON) {
//				disabledAddNewButton();
//			} else {
//				disabledButton();
//			}
//		}
//	}
//
//	public void enabledAddNewButton() {
//		try {
//			this.getElement().getParentElement()
//					.setClassName("add-newitem-button");
//
//			Element addseparator = DOM.createSpan();
//			addseparator.setInnerText("|");
//			addseparator.addClassName("add-separator");
//			DOM.appendChild(this.getElement(), addseparator);
//
//			Element addimage = DOM.createSpan();
//			addimage.addClassName("add-image");
//			DOM.appendChild(this.getElement(), addimage);
//
//			ThemesUtil.addDivToButton(this, Accounter.getThemeImages()
//					.button_right_blue_image(), "add-right-image");
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//	}
//
//	public void disabledAddNewButton() {
//		try {
//			if (this.getElement().getParentElement().getClassName() != null) {
//				this.getElement().getParentElement()
//						.removeClassName("add-newitem-button");
//			}
//			if (this.getText() != null) {
//				ThemesUtil.removeDivToButton(this);
//			}
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//	}
//
//	@Override
//	public void setVisible(boolean visible) {
//		if (visible) {
//			if (this.type == ADD_NEW_BUTTON) {
//				disabledAddNewButton();
//				enabledAddNewButton();
//			} else if (this.type == ADD_BUTTON) {
//				disabledAddButton();
//				enabledAddButton();
//			} else {
//				disabledButton();
//				enabledButton();
//			}
//
//		} else {
//			if (this.type == ADD_BUTTON) {
//				disabledAddButton();
//			} else if (this.type == ADD_NEW_BUTTON) {
//				disabledAddNewButton();
//			} else {
//				disabledButton();
//			}
//		}
//		super.setVisible(visible);
//	}
//
//	public void enabledAddButton() {
//		try {
//			this.getElement().getParentElement().setClassName("add-button");
//
//			Element addseparator = DOM.createSpan();
//			addseparator.setInnerText("|");
//			addseparator.addClassName("add-separator");
//			DOM.appendChild(this.getElement(), addseparator);
//
//			Element addimage = DOM.createSpan();
//			addimage.addClassName("add-image");
//			DOM.appendChild(this.getElement(), addimage);
//
//			ThemesUtil.addDivToButton(this, Accounter.getThemeImages()
//					.button_right_blue_image(), "add-right-image");
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//
//	}
//
//	public void enabledButton() {
//		try {
//			this.getElement().getParentElement().setClassName("ibutton");
//			ThemesUtil.addDivToButton(this, Accounter.getThemeImages()
//					.button_right_blue_image(), "ibutton-right-image");
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//	}
//
//	public void disabledAddButton() {
//		try {
//			if (this.getElement().getParentElement().getClassName() != null) {
//				this.getElement().getParentElement()
//						.removeClassName("add-button");
//			}
//			if (this.getText() != null) {
//				ThemesUtil.removeDivToButton(this);
//			}
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//	}
//
//	public void disabledButton() {
//		try {
//			if (this.getElement().getParentElement().getClassName() != null) {
//				this.getElement().getParentElement().removeClassName("ibutton");
//				if (this.getText() != null) {
//					ThemesUtil.removeDivToButton(this);
//				}
//			}
//		} catch (Exception e) {
//			System.err.println();
//		}
//	}
//
//	public void enabledButton(String string) {
//		try {
//			this.getElement().getParentElement().setClassName(string);
//			ThemesUtil.addDivToButton(this, Accounter.getThemeImages()
//					.button_right_blue_image(), "ibutton-right-image");
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//	}
//
//	public void enabledButton(int type, String image, String string) {
//		try {
//			this.getElement().getParentElement().setClassName(string);
//
//			Element accounterSeperator = DOM.createSpan();
//			accounterSeperator.addClassName("accounter-button-seperater");
//			DOM.appendChild(this.getElement(), accounterSeperator);
//
//			Element accounterImage = DOM.createSpan();
//			accounterImage.addClassName(image);
//			DOM.appendChild(this.getElement(), accounterImage);
//
//			ThemesUtil.addDivToButton(this, Accounter.getThemeImages()
//					.button_right_blue_image(), "ibutton-right-image");
//
//		} catch (Exception e) {
//			System.err.println(e);
//		}
//	}
//
//	public void setWidth(String width) {
//		super.setWidth(width);
//	}
//
//}
