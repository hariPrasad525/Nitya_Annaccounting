package com.nitya.accounter.web.client.ui.grids;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Custom Table is super class for all table widgets like listGrid,TreeGrid,Its
 * provide ground for normal table,
 * 
 * @author kumar kasimala
 * 
 */
public abstract class CustomTable extends FlowPanel {

	protected GwtFlexTable header;
	protected GwtFlexTable body;
	private ScrollPanel panel;

	protected int nofCols;
	protected int currentRow;
	protected int currentCol = 0;

	protected boolean isDecending;
	public static int BODY_WIDTH = 0;
	public RowFormatter rowFormatter;
	public CellFormatter cellFormatter;

	protected boolean isMultiSelectionEnable;
	protected boolean isSingleClick;

	protected GwtFlexTable footer;
	protected boolean isShowFooter;
	public boolean isEnable = true;

	protected Event event;
	Element bodyrowElem;

	protected boolean disable;
	/**
	 * on cell Doubled clicked , cell& row index coming wrong, so that I used
	 * this variable hold to cell& row index when cell single clicked
	 */
	protected RowCell cell;
	private int scrollPanelHeight;
	private boolean isFirstTime = true;
	private int headerCellCount;
	private int bodyCellCount;
	private StyledPanel imagePanel;
	private final static short scrollBarWidth = UIUtils.getScroolBarWidth();
	// private final FinanceImages images = GWT.create(FinanceImages.class);
	private boolean hasLoadingImage;
	protected int width;

	protected static AccounterMessages messages = Global.get().messages();
	private ClientCompanyPreferences preferences = Global.get().preferences();

	public CustomTable() {
		sinkEvents(Event.ONDBLCLICK);
	}

	public CustomTable(boolean isMultiSelection) {
		this.isMultiSelectionEnable = isMultiSelection;
		sinkEvents(Event.ONDBLCLICK);
	}

	public CustomTable(boolean isMultiSelectionEnable, boolean showFooter) {
		this.isShowFooter = showFooter;
		this.isMultiSelectionEnable = isMultiSelectionEnable;
		sinkEvents(Event.ONDBLCLICK);
	}

	public void init() {
		initGrid();
	}

	/**
	 * Initialise the table
	 */
	private void initGrid() {

		this.body = (GwtFlexTable) GWT.create(GwtFlexTable.class);
		this.body.setCustomTable(this);
		this.body.setStyleName("gridBody");

		// this.body.setWidth("100%");

		if (getColumns() != null)
			this.nofCols = getColumns().length;
		else
			Window.alert(messages.columnShouldntbeEmptyInitColumns());

		this.nofCols = isMultiSelectionEnable ? nofCols + 1 : nofCols;

		rowFormatter = this.body.getRowFormatter();
		cellFormatter = this.body.getCellFormatter();

		// adding handler for Table
		if (this.body.getFlexTable() != null) {
			this.body.getFlexTable().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Cell cell = body.getCellForEvent(event);
					if (cell == null || disable) {
						return;
					} else {
						CustomTable.this.cell = new RowCell(cell.getRowIndex(),
								cell.getCellIndex());
						isSingleClick = true;
						eventFired(CustomTable.this.cell);
					}
				}
			});
		}
		this.header = (GwtFlexTable) GWT.create(GwtFlexTable.class);
		this.header.setCustomTable(this);
		// this.header.setWidth("100%");
		CellFormatter headerCellFormater = this.header.getCellFormatter();

		// add checkbox if the multiSection is True
		if (isMultiSelectionEnable) {
			CheckBox headerChkbox = new CheckBox();
			headerChkbox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					checkedUncheckedCheckBox(((CheckBox) event.getSource())
							.getValue());
				}
			});
			this.header.setWidget(0, 0, headerChkbox);
			if (headerCellFormater != null) {
				headerCellFormater.getElement(0, 0).setClassName(
						"header_check_box");
			}
		}
		if (isEnable) {
			if (this.header.getFlexTable() != null) {
				this.header.getFlexTable().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						if (disable)
							return;

						Cell cell = header.getCellForEvent(event);
						CustomTable.this.cell = null;
						if (cell == null) {
							return;
						} else {
							if (isDecending) {
								changeHeaderCellStyles("gridDecend",
										"gridAscend");
								header.getCellFormatter().addStyleName(0,
										cell.getCellIndex(), "gridDecend");

							} else {
								changeHeaderCellStyles("gridAscend",
										"gridDecend");
								header.getCellFormatter().addStyleName(0,
										cell.getCellIndex(), "gridAscend");
							}
							headerCellClicked(cell.getCellIndex());
						}
					}
				});

			}
		}
		initHeader();
		if (this.header.getRowFormatter() != null) {
			this.header.getRowFormatter().addStyleName(0, "gridHeaderRow");
		}

		this.header.setStyleName("gridHeader");
		if (rowFormatter != null) {
			this.add(this.header.getPanel());
		}
		if (this.header.getElement() != null) {
			Element par = this.header.getElement().getParentElement().cast();
			par.addClassName("gridHeaderParent");
		}
		// DONT ENABLE THIS
		// this.header.getElement().getParentElement().getStyle()
		// .setHeight(10, Unit.PX);

		panel = new ScrollPanel() {
			@Override
			protected Element getContainerElement() {
				Element containerElement = super.getContainerElement();
				containerElement.addClassName("bodyParentContainer");
				return containerElement;
			}

			@Override
			protected void onLoad() {
				super.onLoad();
				getContainerElement();
			}
		};
		panel.getElement().removeAttribute("style");
		panel.addStyleName("gridBodyContainer");
		panel.add(this.body.getPanel());
		// addLoadingImagePanel(body);
		this.add(panel);

		if (isShowFooter) {
			initFooter();
		}
		// this.setWidth("100%");
		// this.setSize("100%", "120px");
	}

	private void initFooter() {
		if (this.footer != null)
			return;
		this.footer = (GwtFlexTable) GWT.create(GwtFlexTable.class);
		this.footer.setCustomTable(this);
		for (int i = (isMultiSelectionEnable ? 1 : 0); i < nofCols; i++) {
			this.footer.setText(0, i, " ");
		}
		// this.add(footer);
		// this.footer.addStyleName("listgridfooter");
		// Element fparent = this.footer.getElement().getParentElement().cast();
		// fparent.addClassName("gridFooterParent");
	}

	protected void initHeader() {
		CellFormatter headerCellFormater = this.header.getCellFormatter();
		if (headerCellFormater != null) {
			for (int x = isMultiSelectionEnable ? 1 : 0; x < nofCols; x++) {

				this.header.setText(0, x,
						getColumns()[isMultiSelectionEnable ? (x - 1) : x]);
				headerCellFormater.addStyleName(0, x, "gridHeaderCell");
			}
		}

	}

	public void addLoadingImagePanel() {
		hasLoadingImage = true;
		imagePanel = new StyledPanel("imagePanel");
		imagePanel.setStyleName("loading-panel");
		imagePanel.add(new Image(Accounter.getFinanceImages().loadingImage()));
		Label label = new Label(messages.pleaseWaitDataIsLoading());
		imagePanel.add(label);
		// imagePanel.setCellVerticalAlignment(label,
		// HasVerticalAlignment.ALIGN_MIDDLE);
		body.setWidget(0, 0, imagePanel);
		if (this.body.getRowFormatter() != null) {
			this.body.getRowFormatter().addStyleName(0, "loading-panel");
		}
		if (this.cellFormatter != null) {
			this.cellFormatter.setHorizontalAlignment(0, 0,
					HasHorizontalAlignment.ALIGN_CENTER);
		}

	}

	public void removeLoadingImage() {
		if (hasLoadingImage) {
			removeRow(0);
			hasLoadingImage = false;
		}
		// imagePanel.setVisible(false);
		// showEmptyMessage();

	}

	public void showLoadingImage() {
		imagePanel.setVisible(true);
		// removeEmptyMessage();

	}

	protected boolean isEnableCheckBox = true;

	/**
	 * Enable or Disable check boxs when header checkbox clicked
	 * 
	 * @param isEnable
	 */

	public void enableOrDisableCheckBox(boolean isEnable) {
		if (disable)
			return;
		isEnableCheckBox = isEnable;
		for (int i = 0; i < this.getTableRowCount(); i++) {
			Widget wdget = this.getWidget(i, 0);
			if (wdget != null && wdget instanceof CheckBox) {
				CheckBox box = (CheckBox) this.getWidget(i, 0);
				box.setEnabled(isEnable);
			}
		}
	}

	public void checkedUncheckedCheckBox(boolean isChecked) {
		if (disable)
			return;
		for (int i = 0; i < this.getTableRowCount(); i++) {
			Widget wdget = this.getWidget(i, 0);
			if (wdget != null && wdget instanceof CheckBox) {
				CheckBox box = (CheckBox) this.getWidget(i, 0);
				box.setValue(isEnable);
			}
		}
	}

	/**
	 * called when body cell clicked
	 * 
	 * @param rowIndex
	 * @param cellIndex
	 */
	protected abstract void cellClicked(int rowIndex, int cellIndex);

	/**
	 * Called when any cell click On Header of This table
	 * 
	 * @param cell
	 */
	public abstract void headerCellClicked(int colIndex);

	/**
	 * This method called when setting Cell width, return width of cell, if dont
	 * want to mention cell width, return -1, if cell want use default width
	 * ,then return -2.
	 * 
	 * @return dont return 0 value for width of cell. return -1 0r -2
	 * @param index
	 * 
	 */
	protected abstract int getCellWidth(int index);

	protected abstract String[] getColumns();

	protected abstract void cellDoubleClicked(int row, int col);

	public int getTableRowCount() {
		return this.body.getRowCount();
	}

	public void removeRow(int row) {
		this.body.removeRow(row);
		fixHeader();
	}

	public void removeCell(int row, int cell) {
		this.body.removeCell(row, cell);
	}

	public void removeAllRows() {
		hasLoadingImage = false;
		this.body.removeStyleName("no_records");
		this.body.removeAllRows();
	}

	/**
	 *
	 */
	@Override
	public void clear() {
		this.removeAllRows();
	}

	@Override
	public void setHeight(String height) {

		if (height.contains("%")) {
			// this.panel.setHeight(height);
			// super.setHeight(height);
			return;
		}

		int heightIn = Integer.parseInt(height.replace("px", ""));
		// this.panel.setHeight(heightIn + "px");
		super.setHeight(heightIn + "px");
		fixHeader();
	}

	public int getHeight() {
		return this.panel.getOffsetHeight();
	}

	/**
	 * whenever scroll bar appears to gridBody this function add an extra 'td'
	 * to gridHeader and equates width of that 'td' to scroll bar width
	 * 
	 */

	public void fixHeader() {
		// int gridBodyContainerHeight = Integer.parseInt(panel.getElement()
		// .getStyle().getHeight().replace("px", ""));
		// int gridBodyContainerHeight = body.getOffsetHeight();
		// int contentHeight = this.scrollPanelHeight;
		// Label l = new Label();
		// if (gridBodyContainerHeight > contentHeight && this.isFirstTime) {
		// this.headerCellCount = this.header.getCellCount(0);
		// this.bodyCellCount = this.body.getCellCount(0);
		// this.isFirstTime = false;
		// this.header.setWidget(0, this.headerCellCount, l);
		// if (scrollBarWidth > 0)
		// this.header.getCellFormatter().setWidth(0,
		// this.headerCellCount, scrollBarWidth + "px");
		// this.header.getCellFormatter().setStyleName(0,
		// this.headerCellCount, "gridHeaderLastTd");
		// this.headerCellCount = this.header.getCellCount(0);
		// if (this.footer != null) {
		// this.footer.setWidget(0, this.headerCellCount - 1, l);
		// this.footer.getCellFormatter().setWidth(0,
		// this.headerCellCount - 1, 12 + "px");
		// this.footer.getCellFormatter().setStyleName(0,
		// this.headerCellCount - 1, "gridHeaderLastTd");
		// }
		//
		// } else if (this.headerCellCount > this.bodyCellCount
		// && gridBodyContainerHeight < contentHeight) {
		// this.header.removeCell(0, this.headerCellCount - 1);
		// if (this.footer != null)
		// this.footer.removeCell(0, this.headerCellCount - 2);
		// this.isFirstTime = true;
		// this.headerCellCount = this.header.getCellCount(0);
		// }
	}

	@Override
	public void setWidth(String width) {

		// this.header.setWidth(width);
		// if (width.contains("px"))
		// this.width = Integer.parseInt(width.replace("px", ""));
		// this.body.setWidth("100%");
		// this.panel.setWidth("100%");
		super.setWidth(width);
		// adjustCellsWidth(0, header);
		// // body.setWidth(header.getOffsetWidth() + "px");
		// for (int row = 0; row < this.body.getRowCount(); row++) {
		// adjustCellsWidth(row, body);
		// }
		//
		// if (isShowFooter) {
		// adjustCellsWidth(0, footer);
		// }
	}

	public void setText(int row, int column, String text) {
		this.body.setText(row, isMultiSelectionEnable ? column + 1 : column,
				text);
		if (cellFormatter != null) {
			this.cellFormatter.getElement(row,
					isMultiSelectionEnable ? column + 1 : column)
					.setTitle(text);
		}
	}

	public void setWidget(int row, int column, final Widget widget) {
		this.body.setWidget(row, isMultiSelectionEnable ? column + 1 : column,
				widget);
	}

	public void setWidget(int row, final int column, final FocusWidget widget) {

		this.body.setWidget(row, isMultiSelectionEnable ? column + 1 : column,
				widget);
		widget.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (isMultiSelectionEnable && column == -1)
					return;
				// removeWidget(widget);
			}
		});
		widget.setFocus(true);
	}

	protected void removeWidget(Widget widget) {
		if (widget instanceof CheckBox)
			return;
		currentCol = 0;
		currentRow = 0;
		remove(widget);
	}

	public Widget getWidget(int row, int col) {
		if (row < this.getTableRowCount())
			return this.body.getWidget(row, col);
		return null;
	}

	/**
	 * Remove widget from Table
	 */
	@Override
	public boolean remove(Widget widget) {
		return this.body.remove(widget);
	}

	public void setHTML(int row, int column, String text) {
		this.body.setHTML(row, isMultiSelectionEnable ? column + 1 : column,
				"<div>" + text + "</div>");
		this.cellFormatter.getElement(row,
				isMultiSelectionEnable ? column + 1 : column).setTitle(text);
	}

	private void changeHeaderCellStyles(String addStyle, String removeStyle) {
		for (int i = 0; i < this.header.getCellCount(0); i++) {
			if (header.getCellFormatter() != null)
				continue;

			header.getCellFormatter().removeStyleName(0, i, removeStyle);
		}
	}

	protected RowCell getCellByWidget(Widget widget) {
		int row = 0;
		int col = 0;
		if (body.getCellFormatter() == null) {
			Element td = DOM.getParent(widget.getElement());
			Element child = widget.getElement();
			Element tr = DOM.getParent(td);
			row = DOM.getChildIndex(tr, td);
			col = DOM.getChildIndex(td, child);
			col = (col / 2);
		} else {
			Element td = DOM.getParent(widget.getElement());
			Element tr = DOM.getParent(td);
			Element parent = DOM.getParent(tr);
			row = DOM.getChildIndex(parent, tr);
			col = DOM.getChildIndex(tr, td);
		}
		return new RowCell(row, col);
	}

	/**
	 * Adjust the Cell width in row, getCellsWidth method will call when setting
	 * width for cell. if that method returns -1 or -2 than width will adjusted
	 * according to row width. if that method returns width,then that will set
	 * to cell& others cells width will adjusted according to row width
	 * 
	 * @param row
	 * @param table
	 */
	protected void adjustCellsWidth(int row, GwtFlexTable table) {

		try {
			int parentWidth = width;

			int[] colsUpdate = new int[nofCols];

			int cellWidth = -1;
			int colCounts = 0;
			if (table.getCellFormatter() != null) {
				for (int i = isMultiSelectionEnable ? 1 : 0; i < nofCols; i++) {

					Element cell = table.getCellFormatter().getElement(row, i);
					cellWidth = getCellWidth(isMultiSelectionEnable ? i - 1 : i);
					// if (i == nofCols - 2)
					// continue;
					if (cellWidth == -2)
						continue;
					if (cellWidth == -1) {
						// cell.setAttribute("width", "100%");
						colsUpdate[colCounts++] = i;
					} else {
						try {
							int cellSize = table.getCellCount(row);
							if (cellSize <= i) {
								continue;
							}

							parentWidth = parentWidth - cellWidth;
							if (table.equals(body) && BODY_WIDTH == 1)
								cell.setAttribute("width", ""
										+ (cellWidth - (20 / nofCols)));
							else
								cell.setAttribute("width", "" + cellWidth);
						} catch (IndexOutOfBoundsException e) {
							e.printStackTrace();
						}
					}
				}
			}

			// cellWidth = parentWidth / colCounts;
			//
			// for (int col : colsUpdate) {
			// if (col != 0) {
			// try {
			// int cellSize = table.getCellCount(row);
			// if (cellSize <= col) {
			// continue;
			// }
			// Element cell = table.getCellFormatter().getElement(row,
			// col);
			// cell.setAttribute("width", "" + cellWidth);
			// } catch (IndexOutOfBoundsException e) {
			// e.printStackTrace();
			// }
			// }
			// }

			if (isMultiSelectionEnable) {
				// if (UIUtils.isMSIEBrowser())
				// table.getCellFormatter().getElement(row, 0)
				// .setAttribute("width", "" + 25);
				// else
				if (table.getCellFormatter() != null) {
					table.getCellFormatter().getElement(row, 0)
							.setAttribute("width", "" + 15);
				}
			}

		} catch (Exception e) {

		}

	}

	public void addEmptyMessage(String msg) {
		this.body.addEmptyMessage(msg);
	}

	@Override
	protected void onLoad() {
		width = header.getOffsetWidth() - 90;
		if (BODY_WIDTH == 1)
			bodyCellWidth();
		adjustCellsWidth(0, header);
		if (isShowFooter) {
			adjustCellsWidth(0, footer);
		}
	}

	private void bodyCellWidth() {
		bodyCellCount = 1;
		for (int row = 0; row < this.body.getRowCount(); row++) {
			adjustCellsWidth(row, body);
		}
		this.body.getParent().getElement().getParentElement()
				.addClassName("list-grid-body");
		BODY_WIDTH = 0;
	}

	public void eventFired(final RowCell cell) {
		try {
			if (disable)
				return;

			if (isSingleClick) {
				new Timer() {

					@Override
					public void run() {
						if (isSingleClick)
							cellClicked(cell.getRowIndex(), cell.getCellIndex());
					}
				}.schedule(300);
			} else {
				isSingleClick = false;
				if (cell == null)
					return;
				cellDoubleClicked(cell.getRowIndex(), cell.getCellIndex());
			}
		} catch (Exception e) {
			return;
		}
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONDBLCLICK:

			isSingleClick = false;
			eventFired(cell);
			break;
		case Event.ONMOUSEOVER:
			if (bodyrowElem != null)
				bodyrowElem.removeClassName("report-hover");
			bodyrowElem = (Element) DOM.eventGetTarget(event)
					.getParentElement();
			if (DOM.isOrHasChild(this.body.getPanel().getElement(), bodyrowElem)) {
				bodyrowElem.addClassName("report-hover");

			}
			break;
		case Event.ONMOUSEOUT:
			if (bodyrowElem != null)
				bodyrowElem.removeClassName("report-hover");
			break;
		case Event.ONCLICK:
			break;

		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	public void setAttribute(String name, String value, int rowIndex) {
		this.rowFormatter.getElement(rowIndex).setPropertyString(name, value);
	}

	public void setAttribute(String name, Object value, int rowIndex) {
		this.rowFormatter.getElement(rowIndex).setPropertyObject(name, value);
	}

	public String getAttribute(String name, int rowIndex) {
		return this.rowFormatter.getElement(rowIndex).getPropertyString(name);
	}

	public Object getAttributeAsObject(String name, int rowIndex) {
		return this.rowFormatter.getElement(rowIndex).getPropertyObject(name);
	}

	/**
	 * class is used to Hold Row&ColumnIndex
	 */
	class RowCell {

		private final int rowIndex;
		private final int cellIndex;

		/**
		 * Creates a cell.
		 * 
		 * @param rowIndex
		 *            the cell's row
		 * @param cellIndex
		 *            the cell's index
		 */
		public RowCell(int rowIndex, int cellIndex) {
			this.cellIndex = cellIndex;
			this.rowIndex = rowIndex;
		}

		/**
		 * Gets the cell index.
		 * 
		 * @return the cell index
		 */
		public int getCellIndex() {
			return cellIndex;
		}

		/**
		 * Gets the cell's element.
		 * 
		 * @return the cell's element.
		 */
		public Element getElement() {
			return cellFormatter.getElement(cellIndex, rowIndex);
		}

		/**
		 * Get row index.
		 * 
		 * @return the row index
		 */
		public int getRowIndex() {
			return rowIndex;
		}

	}

	public void setEnabled(boolean disable) {
		this.disable = !disable;
		this.addStyleName("disableGrid");
		if (isMultiSelectionEnable) {
			Widget widget = this.header.getWidget(0, 0);
			if (widget instanceof CheckBox) {
				((CheckBox) widget).setEnabled(disable);
			}
		}
		for (int i = 0; i < this.getTableRowCount(); i++) {
			Widget widget = this.getWidget(i, 0);
			if (widget instanceof CheckBox) {
				((CheckBox) widget).setEnabled(disable);
			}
		}
	}

	public boolean isShowFooter() {
		return isShowFooter;
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

	public String getDecimalChar() {
		return getPreferences().getDecimalCharacter();
	}

}
