package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.event.dom.client.MouseOutEvent;
//import com.google.gwt.event.dom.client.MouseOutHandler;
//import com.google.gwt.event.dom.client.MouseOverEvent;
//import com.google.gwt.event.dom.client.MouseOverHandler;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//
//import com.nitya.accounter.web.client.ui.core.Accounter;
//import com.nitya.accounter.web.client.ui.core.Action;
//
///**
// * Class for adding Customised Stack's inner Elements
// * 
// * @author Raj vimal
// * 
// */
//public class StackInnerElement extends Canvas {
//
//	private StyledPanel mainLayout;
//
//	public StackInnerElement() {
//		createView();
//	}
//
//	private void createView() {
//		mainLayout = new StyledPanel();
//		mainLayout.setSize("*", "100%");
//		// mainLayout.setMembersMargin(5);
//	//	mainLayout.setLeft(15);
////		mainLayout.setStyleName("stackpan_items");
//		addChild(mainLayout);
//
//	}
//
//	public void addInnerMember(final Action action) {
//		final Label iconLabel = new Label();
//		iconLabel.setSize("100%", "10");
//		iconLabel.addMouseOverHandler(new MouseOverHandler() {
//
//			public void onMouseOver(MouseOverEvent event) {
//				iconLabel.setStyleName("actionOver");
//			}
//		});
//		iconLabel.addMouseOutHandler(new MouseOutHandler() {
//
//			public void onMouseOut(MouseOutEvent event) {
//				iconLabel.setStyleName("actionOut");
//			}
//		});
//		iconLabel.setIcon((action.getIconString() == null || action
//				.getIconString() == "") ? "/images/icons/noImageAvailable.jpg"
//				: action.getIconString());
//		iconLabel.setIconSize(15);
//		iconLabel.setContents("<font size=\" 2 \">"
//				+ UIUtils.getStringWithSpaces(action.getText()) + "</font>");
////		iconLabel.setCursor(Cursor.HAND);
//		iconLabel.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				try {
//					action.run(null, false);
//				} catch (Throwable e) {
//					Accounter.showError("Sorry, Could Not Load... ");
//					GWT.log("", e);
//					SC.logWarn(e.getMessage());
//				}
//			}
//
//		});
//		mainLayout.add(iconLabel);
//	}
//
//}
