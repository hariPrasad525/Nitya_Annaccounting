package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import java.awt.Canvas;
//
//import com.google.gwt.dom.client.Style.Cursor;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.ui.Label;
///**
// * Customised stack panel for left accordion 
// * @author Raj Vimal
// *
// *
// */
//public class CustomizedStackPanel extends Canvas {
//
//	private VLayout mainLayout;
//	public boolean flag = false;
//	public static StackInnerElement previousElement;
//
//	public CustomizedStackPanel() {
//
//		createView();
//	}
//
//	private void createView() {
//
//		mainLayout = new VLayout();
//		mainLayout.setSize("100%", "100%");
//		setStyleName("stack");
//	}
//
//	/**
//	 * 
//	 * Add Outer Component with specific name & inner Element
//	 * @param name
//	 * @param icon
//	 * @param innerElement
//	 */
//	public void addOuterComponent(String name, String icon,
//			final StackInnerElement innerElement) {
//		final VLayout layout = new VLayout();
//		Label iconLabel = new Label();
//		iconLabel.setSize("245", "25");
//		iconLabel.setLeft(5);
//		iconLabel.setIcon(icon);
//		iconLabel.setIconSize(15);
//		iconLabel.setStyleName("stackpannel");
//		iconLabel.setCursor(Cursor.HAND);
//		iconLabel.setContents("<font size=\" 4 \">"
//				+ UIUtils.getStringWithSpaces(name) + "</font>");
//		layout.add(iconLabel);
//		layout.add(innerElement);
//		if (flag == false) {
//			innerElement.show();
//			previousElement = innerElement;
//			flag = true;
//		} else {
//			innerElement.hide();
//		}
//		layout.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//
//				previousElement.hide();
//				innerElement.show();
//				previousElement = innerElement;
//			}
//
//		});
//
//		mainLayout.setBorder("#C6E2FF");
//		mainLayout.add(layout);
//		addChild(mainLayout);
//	}
//}
