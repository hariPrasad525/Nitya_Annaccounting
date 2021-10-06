package com.nitya.accounter.web.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SimplePanel;

public class CustomMenuItem extends MenuItem {

	public CustomMenuItem(String text, Command cmd) {
		super(text, false, cmd);
		this.getElement().addClassName("menu-item-image");
	}

	public void setIcon(ImageResource image) {
		if (image != null) {

			// this.getElement().getStyle().setProperty(
			// FinanceApplication.constants().background(),
			// "url(" + image.getURL() + ")   no-repeat ");
			// this.getElement().addClassName(
			// FinanceApplication.constants().menuItemImage());
			// this.getElement().getStyle().setBackgroundColor("");
			//
			//
			// img.getElement().setAttribute("align", "left");
			// String title = this.getElement().getInnerText();
			// this.getElement().setInnerText("");
			// this.getElement().appendChild(img.getElement());
			// this.getElement().setInnerHTML(
			// this.getElement().getInnerHTML() + new HTML(title));

			Image img = new Image(image);
			StyledPanel hPanel = new StyledPanel("hPanel");
			HTML title = new HTML(this.getElement().getInnerText());
			title.getElement().getStyle().setMarginLeft(4, Unit.PX);
			// hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			hPanel.add(img);
			hPanel.add(title);
			this.getElement().setInnerText("");
			SimplePanel simplePanel = new SimplePanel();
			simplePanel.add(hPanel);
			this.setHTML(simplePanel.getElement().getInnerHTML());

			// Element div = DOM.createDiv();
			// div.setAttribute("align", "right");
			// div.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
			// div.getStyle().setProperty("margin", "3px 0px 0px 0px");
			// div.setInnerText(title);
			// this.getElement().appendChild(div);
			// this.getElement().addClassName(
			// FinanceApplication.constants().menuItemImage());
		}
	}

}
