package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.IButtonBar;

public class CustomDialog extends DialogBox {

	private StyledPanel captionPanel;
	private HTML caption;
	private Image cross, help;

	private boolean isShowHelpBtn;
	private boolean isShowCloseBtn;
	private StyledPanel imageStyledPanel;
	protected static AccounterMessages messages = Global.get().messages();

	public CustomDialog() {
		showHeader();
		this.getElement().setId("CustomDialog");
	}

	public CustomDialog(boolean isShowCloseandHelpBtns) {

		this.isShowCloseBtn = isShowCloseandHelpBtns;
		this.isShowHelpBtn = isShowCloseandHelpBtns;

		sinkEvents(Event.ONCLICK);

		showHeader();
		this.getElement().setId("CustomDialog");
	}

	public CustomDialog(boolean isShowHelp, boolean isShowClose) {
		this.isShowCloseBtn = isShowClose;
		this.isShowHelpBtn = isShowHelp;

		sinkEvents(Event.ONCLICK);

		showHeader();
		this.getElement().setId("CustomDialog");
	}

	public void showHeader() {
		cross = new Image(Accounter.getFinanceImages().dialougueCloseicon());
		cross.setTitle(messages.clickThisTo(messages.close().toLowerCase(), ""));
		// help = new Image("/images/help-icon.png");
		help = new Image(Accounter.getFinanceImages().helpIcon());
		help.setStyleName("dialog_help_icon");
		help.setTitle(messages.clickThisTo(messages.help(), "")
				.replace(messages.to().toLowerCase() + " ", messages.For())
				.replace(messages.This(), ""));
		// help.setStyleName("helpAlign");
		caption = new HTML();

		captionPanel = new StyledPanel("captionPanel");
		// captionPanel.setWidth("100%");
		captionPanel.add(caption);

		imageStyledPanel = new StyledPanel("imageStyledPanel");
		// imageStyledPanel.setSpacing(3);

		if (isShowHelpBtn)
			imageStyledPanel.add(help);
		if (isShowCloseBtn)
			imageStyledPanel.add(cross);

		// imageStyledPanel.setCellHorizontalAlignment(help,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// imageStyledPanel.setCellHorizontalAlignment(cross,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		captionPanel.add(imageStyledPanel);
		captionPanel.setStyleName("caption");
		// captionPanel.setCellHorizontalAlignment(imageStyledPanel,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		Element td = getCellElement(0, 1);
		td.setInnerHTML("");
		td.appendChild(captionPanel.getElement());
		td.getParentElement().getParentElement().getParentElement()
				.getParentElement().getParentElement()
				.setClassName("view-dialog-child");
	}

	@Override
	public void setText(String text) {
		caption.setText(text);
	}

	@Override
	public String getText() {
		return caption.getText();
	}

	public void setHtml(String html) {
		caption.setHTML(html);
	}

	public String getHtml() {
		return caption.getHTML();
	}

	@Override
	public void onBrowserEvent(Event event) {

		switch (DOM.eventGetType(event)) {
		case Event.ONCLICK:
			Element element = event.getTarget();
			if (cross.getElement().equals(element)) {
				if (onCancel())
					hide();
			} else if (help.getElement().equals(element)) {
				Window.open("http://help.annaccounting.com/", "_blank", "");
				onHelp();
			}
			break;
		// case Event.ONMOUSEOVER:
		// Element element1 = event.getTarget();
		// if (cross.getElement().equals(element1)) {
		// // cross.setUrl("/images/X-1.png");
		//
		// } else if (help.getElement().equals(element1)) {
		// help.setUrl("/images/Help-1.png");
		// }
		// break;
		// case Event.ONMOUSEOUT:
		// Element element2 = event.getTarget();
		// if (cross.getElement().equals(element2)) {
		// cross.setUrl("/images/dialog-close.png");
		//
		// } else if (help.getElement().equals(element2)) {
		// help.setUrl("/images/help-icon.png");
		// }
		// break;
		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	protected boolean onCancel() {
		com.google.gwt.user.client.History.back();
		return true;
	}

	protected void onHelp() {

	}

	private IButtonBar buttonBar;

	public IButtonBar getButtonBar() {
		if (this.buttonBar == null) {
			buttonBar = GWT.create(IButtonBar.class);
		}
		return buttonBar;
	}

	public void removeButton(Panel parent, Button child) {
		getButtonBar().removeButton(parent, child);
	}

	public void addButton(Panel parent, Button child) {
		if (isViewDialog()) {
			getButtonBar().addButton(parent, child);
		} else {
			parent.add(child);
		}
	}

	public void updateButtons() {

	}

	public boolean isViewDialog() {
		return false;
	}

	public void cancel() {
		onCancel();
		hide();
	}

}