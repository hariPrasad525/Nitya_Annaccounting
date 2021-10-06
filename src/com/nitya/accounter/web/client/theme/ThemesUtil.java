package com.nitya.accounter.web.client.theme;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.nitya.accounter.web.client.ui.Accounter;

public class ThemesUtil {

	public static void insertImageChildToMenuItem(MenuBar menubar,
			MenuItem menuitem) {
		Image child = new Image();
		child.addStyleName("menu_arrow");
		child.setUrl(Accounter.getThemeImages().drop_down_indicator().getURL());
		DOM.insertChild(menuitem.getElement(), child.getElement(), 0);
		insertEmptyChildToMenuBar(menubar);
	}

	public static void insertEmptyChildToMenuBar(MenuBar menubar) {
		MenuItem menuitem = menubar.addItem(new MenuItem("", new Command() {

			@Override
			public void execute() {

			}
		}));
		Image child = new Image();
		child.setUrl(Accounter.getThemeImages().menu_bar_devider().getURL());
		menuitem.setStyleName("menubar-devider");
		DOM.insertChild(menuitem.getElement(), child.getElement(), 0);
	}

	public static void addDivToButton(Button button, ImageResource imgResource,
			String styleName) {
		Element btnEle = button.getElement();

		Element divEle = DOM.createDiv();
		divEle.setId(button.getText());
		divEle.addClassName(styleName);
		divEle.getStyle().setBackgroundImage(
				"url(" + imgResource.getURL() + ")");

		btnEle.getParentElement().insertAfter(divEle, btnEle);

	}

	public static void removeDivToButton(Button button) {
		try {
			Element parent = (Element) DOM.getElementById(button.getText())
					.getParentElement();
			parent.removeChild(DOM.getElementById(button.getText()));
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
