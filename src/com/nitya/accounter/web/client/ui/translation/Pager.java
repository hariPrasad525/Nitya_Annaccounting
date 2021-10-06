package com.nitya.accounter.web.client.ui.translation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.ImageButton;
import com.nitya.accounter.web.client.ui.StyledPanel;

public abstract class Pager extends FlowPanel implements PagerListener {
	public static final int DEFAULT_START = 0;
	public static int DEFAULT_RANGE = 0;

	private ImageButton previousButton, nextButton;
	private Label rangeLabel;
	private int totalResultCount, presentResultCount, startRange, endRange,
			range;
	private boolean isNext;
	private boolean isBack;
	private boolean hasMoreRecords;

	public Pager(int range, AbstractPagerView<?> pagerView) {
		createControls();
		DEFAULT_RANGE = range;
		refreshPager();
	}

	public void refreshPager() {
		this.setRange(DEFAULT_RANGE);
		this.setStartRange(DEFAULT_START);
		endRange = getStartRange() + getRange();
		this.updateRange();
	}

	public void updateRange() {
		rangeLabel.setText(Global.get().messages()
				.range(getStartRange(), endRange));
	}

	private void createControls() {
		StyledPanel pagerPanel = new StyledPanel("pagerPanel");
		previousButton = new ImageButton(Accounter.getFinanceImages()
				.leftArrow(), "previous");
		nextButton = new ImageButton(Accounter.getFinanceImages().rightArrow(),
				"next");
		rangeLabel = new Label();
		rangeLabel.addStyleName("pager-range");
		previousButton.setVisible(false);
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onPrevious();
			}
		});
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onNext();
			}
		});
		pagerPanel.add(previousButton);
		pagerPanel.add(rangeLabel);
		pagerPanel.add(nextButton);
		this.add(pagerPanel);
		// this.setWidth("100%");
		// this.setCellHorizontalAlignment(pagerPanel,
		// HasAlignment.ALIGN_CENTER);
	}

	protected void onNext() {
		isNext = true;
		isBack = false;
		setStartRange(getStartRange() + getRange());
		updateListData();
	}

	protected void updateRangeData() {
		if (isNext) {
			nextRange();
		}
		if (isBack) {
			presentResultCount = getDataSize();
			setStartRange(endRange - presentResultCount);
		}
		if (!isNext && !isBack) {
			nextRange();
		}
		buttonVisability();
	}

	private void nextRange() {
		presentResultCount = getDataSize();
		endRange = getStartRange() + presentResultCount;
	}

	protected void initData() {
		onNext();
	}

	private void buttonVisability() {
		if (startRange >= range) {
			previousButton.setVisible(true);
		} else {
			previousButton.setVisible(false);
		}
		if ((totalResultCount != 0) && (totalResultCount > endRange)) {
			setMoreRecords(true);
		} else {
			setMoreRecords(false);
		}
		if (range != DEFAULT_RANGE || (!hasMoreRecords())) {
			nextButton.setVisible(false);
			// range = DEFAULT_RANGE;
		} else {
			nextButton.setVisible(true);
		}
		updateRange();
	}

	protected void onPrevious() {
		isNext = false;
		isBack = true;
		endRange = getStartRange();
		startRange = startRange - range;
		updateListData();

	}

	public int getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(int totalResultCount) {
		this.totalResultCount = totalResultCount;
	}

	public int getPresentResultCount() {
		return presentResultCount;
	}

	public void setPresentResultCount(int presentResultCount) {
		this.presentResultCount = presentResultCount;
	}

	public int getStartRange() {
		return startRange;
	}

	public void setStartRange(int startRange) {
		this.startRange = startRange;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	@Override
	public boolean hasMoreRecords() {
		return hasMoreRecords;
	}

	@Override
	public void setMoreRecords(boolean b) {
		this.hasMoreRecords = b;
	}

	@Override
	public abstract void updateListData();

	@Override
	public abstract int getDataSize();

}
