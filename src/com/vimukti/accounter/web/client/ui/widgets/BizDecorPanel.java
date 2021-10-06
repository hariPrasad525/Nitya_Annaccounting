package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.impl.CldrImpl;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;

/**
 * 
 * @author P.Praneeth
 * 
 */

public abstract class BizDecorPanel extends FlowPanel {
	private Label TC;
	private AutoFillWidget MC;
	private Image closeImage;
	private Image configImage;
	private Label gotoLabel;

	public BizDecorPanel(String title, String gotoString) {
		setDecorator(title, gotoString);
		this.setStyleName("biz-decor-panel");
		if (title != null) {
			setPanelTitle(title);
		} else {
			this.addStyleName("no-title");
		}

		if (gotoString != null) {
			setGoToAction(title, gotoString);
		}
		setTitleActions();
	}

	public BizDecorPanel(String title, String gotoString, String width) {
		this(title, gotoString);
	}

	private void setTitleActions() {
		closeImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onClose();
			}
		});

		configImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onConfigure();
			}
		});

	}

	/**
	 * Initiates the layout
	 */
	private void setDecorator(String title, String gotoString) {
		StyledPanel decorator = new StyledPanel("decorator");
		decorator.add(setFirstRow());
		decorator.add(setSecondRow());
		decorator.add(setThirdRow());
		this.add(decorator);
		// setStyles(title);
	}

	protected void setPortletTitle(String title) {
		TC.setText(title);
	}

	/**
	 * Creates the first row of Panel
	 * 
	 * @return
	 */
	private StyledPanel setFirstRow() {
		Label TL = new Label();
		TC = new Label();
		closeImage = new Image(Accounter.getFinanceImages().portletClose());
		configImage = new Image(Accounter.getFinanceImages().portletSettings());
		gotoLabel = new HTML();

		FlowPanel titleTable = new FlowPanel();
		titleTable.add(gotoLabel);

		if (canConfigure()) {
			titleTable.add(configImage);
			configImage.getElement().getParentElement()
					.addClassName("portlet_config_button");
		}
		gotoLabel.getElement().getParentElement().addClassName("go-to-link");
		if (canClose()) {
			titleTable.add(closeImage);
			closeImage.getElement().getParentElement()
					.addClassName("portlet_close_button");

		}
		Label TR = new Label();
		TR.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");
		TL.getElement().setAttribute("lang",
				((CldrImpl) GWT.create(CldrImpl.class)).isRTL() ? "ar" : "en");

		StyledPanel firstRow = new StyledPanel("firstRow");
		firstRow.add(TL);
		firstRow.add(TC);
		firstRow.add(titleTable);
		firstRow.add(TR);
		return firstRow;
		// this.setWidget(0, 0, TL);
		// this.setWidget(0, 1, titleTable);
		// this.setWidget(0, 2, TR);
	}

	private void setGoToAction(String title, String gotoString) {
		gotoLabel.setText(gotoString);
		gotoLabel.addStyleName("goToLink");
		AccounterMessages messages = Global.get().messages();
		gotoLabel.setTitle(messages.clickThisObjToOpen(messages.link(),
				messages.allTransactionDetails(title)));
		gotoLabel.getElement().getStyle()
				.setTextDecoration(getTitleDecoration());
		gotoLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				titleClicked();
			}
		});
	}

	/**
	 * Creates the second row of Panel
	 */
	private StyledPanel setSecondRow() {
		MC = new AutoFillWidget();
		AutoFillWidget c = new AutoFillWidget();
		c.add(MC);

		MC.setStyleName("decor-left");

		StyledPanel secondRow = new StyledPanel("secondRow");
		secondRow.add(c);
		return secondRow;

		// this.setWidget(1, 0, c);
		// this.getFlexCellFormatter().setColSpan(1, 0, 3);
	}

	/**
	 * Creates the third row of Panel
	 */
	private StyledPanel setThirdRow() {
		Label BL = new Label();
		Label BC = new Label();
		Label BR = new Label();

		StyledPanel setThirdRow = new StyledPanel("setThirdRow");
		setThirdRow.add(BL);
		setThirdRow.add(BC);
		setThirdRow.add(BR);
		return setThirdRow;

		// this.setWidget(2, 0, BL);
		// this.setWidget(2, 1, BC);
		// this.setWidget(2, 2, BR);
	}

	// /**
	// * Sets the required styles to cells
	// */
	// private void setStyles(String title) {
	// for (int i = 0; i < 3; i++) {
	// for (int j = 0; j < 3; j++) {
	// if (i == 1 && j != 0)
	// break;
	// this.getCellFormatter().setStyleName(i, j, "td" + i + j);
	// this.getWidget(i, j).addStyleName("decor" + i + j);
	// }
	// }
	// }

	/**
	 * Sets the panel title.
	 * 
	 * @param title
	 */
	private void setPanelTitle(String title) {
		TC.setText(title);
		TC.addStyleName("panel-title");
	}

	public Cursor getTitleCursor() {
		return Cursor.AUTO;
	}

	public TextDecoration getTitleDecoration() {
		return TextDecoration.UNDERLINE;
	}

	public void titleClicked() {

	}

	/**
	 * Adds the widget to the centre cell
	 * 
	 * @param widget
	 */
	public void setWidget(Widget widget) {
		MC.add(widget);
		widget.addStyleName("main-w");
	}

	// /**
	// * Returns the added widget
	// *
	// * @return
	// */
	// public Widget getWidget() {
	// return super.getWidget(1, 1);
	// }

	@Override
	public void setHeight(String height) {
		if (height.contains("%")) {
			super.setHeight(height);
			MC.setHeight(height);
		} else {
			super.setHeight("");
			int h = Integer.parseInt(height.replace("px", ""));
			if (h > 0) {
				MC.setHeight(h + "px");
			}

		}
	}

	@Override
	public void setWidth(String width) {
		if (width.contains("%")) {
			super.setWidth(width);
		} else {
			super.setWidth("");
			int w = Integer.parseInt(width.replace("px", ""));
			if (w > 0) {
				MC.setWidth(w + "px");
			}

		}
	}

	/**
	 * Behaves same as setWidget()
	 */
	// @Override
	// public void add(Widget widget) {
	// MC.add(widget);
	// widget.addStyleName("main-w-finance");
	// widget.setWidth("");
	// }

	public void doAnimate(final Boolean isMinimizing) {
		if (isMinimizing) {
			MC.setVisible(false);
		} else {
			MC.setVisible(true);
		}
	}

	protected abstract boolean canClose();

	protected abstract boolean canConfigure();

	protected abstract void onClose();

	protected abstract void onConfigure();

	public Label getHeader() {
		return TC;
	}

	public void setHeader(Label tC) {
		TC = tC;
	}

}
