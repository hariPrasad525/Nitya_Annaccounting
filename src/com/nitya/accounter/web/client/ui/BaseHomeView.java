package com.nitya.accounter.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.core.AbstractView;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;

public class BaseHomeView extends AbstractView<Object> implements
		IDeleteCallback {
	private StyledPanel widgetLayout;
	protected BaseListGrid payeeGrid;
	protected final int start = 0;

	public BaseHomeView() {
		createView();
	}

	private void createView() {
		StyledPanel mainLayout = new StyledPanel("mainLayout");
		// mainLayout.setSize("100%", "100%");
		StyledPanel imagePanel = new StyledPanel("imagePanel");
		imagePanel.setStyleName("Image action container");

		widgetLayout = new StyledPanel("widgetLayout");
		widgetLayout.setStyleName("finance-portlet");
		mainLayout.add(widgetLayout);

		add(mainLayout);
		StyledPanel leftLayout = new StyledPanel("leftLayout");
		int pageSize = getPageSize();
		if (pageSize != -1 && payeeGrid != null) {
			payeeGrid.addRangeChangeHandler2(new Handler() {
				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onPageChange(event.getNewRange().getStart(), event
							.getNewRange().getLength());
				}
			});
			payeeGrid.setVisibleRange(start, pageSize);
			SimplePager pager = new SimplePager(TextLocation.CENTER,
					(Resources) GWT.create(Resources.class), false,
					pageSize * 2, true);
			pager.setDisplay(payeeGrid);
			pager.addStyleName("pager-images");
			leftLayout.add(payeeGrid);
			leftLayout.add(pager);
		}
		widgetLayout.add(leftLayout);
	}

	public StyledPanel getLeftLayout() {
		return widgetLayout;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void fitToSize(int height, int width) {
		// this.setHeight(height + "px");

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void init() {
		createView();
	}

	protected int getPageSize() {
		return 10;
	}

	protected void onPageChange(int start, int length) {

	}

	public void updateRecordsCount(int start, int length, int total) {
		payeeGrid.updateRange(new Range(start, getPageSize()));
		payeeGrid.setRowCount(total, (start + length) == total);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

}
