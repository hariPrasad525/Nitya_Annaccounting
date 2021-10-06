package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;

//import java.awt.Canvas;

/**
 * Portlet Column for placing Widgets
 * 
 * @author Raj Vimal
 * 
 */
// FIXME Vstack need to be implemented rather than vertical panel
public class PortalColumn extends FlowPanel {
	private int colNum;

	public PortalColumn(int colNum) {

		// setMembersMargin(3);
//		setSize("100%", "100%");
		setColNum(colNum);
		// setAnimateMembers(true);

		// setAnimateMemberTime(2);
		// setCanAcceptDrop(true);
		// setDropLineThickness(10);

		StyledPanel dropLineProperties = new StyledPanel("dropLineProperties");
		// dropLineProperties.setBackgroundColor("pink");
		// setDropLineProperties(dropLineProperties);

		// setShowDragPlaceHolder(true);

		StyledPanel placeHolderProperties = new StyledPanel(
				"placeHolderProperties");
		// placeHolderProperties.setBorder("2px solid #8289A6");
		// setPlaceHolderProperties(placeHolderProperties);
		// animateShow(AnimationEffect.SLIDE);
	}

	/**
	 * Getter for Column Number
	 * 
	 * @return colNum
	 */
	public int getColNum() {
		return colNum;
	}

	/**
	 * Setter for Column Number
	 * 
	 * @param colNum
	 */
	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

}