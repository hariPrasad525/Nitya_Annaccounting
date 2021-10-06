package com.nitya.accounter.web.client.ui.combo;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

public abstract class DropDownTable<T> extends CellTable<T> {

	public FlexTable body;
	private RowFormatter rowFormatter;
	private CellFormatter cellFormatter;
	protected DropDownCombo<T> combo;
	protected SingleSelectionModel<T> selectionModel;
	boolean isAddNewRequire;
	private Element bodyrowElem;
	protected ClickableTextCell clickableTextCell;
	private EscapEventHandler escapEventHandler;

	public DropDownTable(DropDownCombo<T> combo) {
		this.combo = combo;
		sinkEvents(Event.ONMOUSEOVER);
		initGrid();
	}

	/**
	 * Initialise the table
	 */
	private void initGrid() {
		addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE
						|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {
					if (escapEventHandler != null) {
						escapEventHandler.onEscap(event);
					}
				}
			}
		}, KeyDownEvent.getType());
		this.body = new FlexTable();
		this.setStyleName("dropdownFlex");

		// this.setWidth("100%");

		clickableTextCell = new ClickableTextCell();
		Column[] columns = getColumns();
		for (int i = 0; i < columns.length; i++)
			this.addColumn(columns[i]);
		selectionModel = new SingleSelectionModel<T>();
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		// this.setSize("100%", "");

	}

	public void addEscapEventHandler(EscapEventHandler handler) {
		escapEventHandler = handler;
	}

	@Override
	public void setVisibleRange(Range range) {
		super.setVisibleRange(range);
	}

	@Override
	protected void setKeyboardSelected(int index, boolean selected,
			boolean stealFocus) {
		super.setKeyboardSelected(index, selected, stealFocus);
	}

	@Override
	public TableRowElement getRowElement(int row) {
		// TODO Auto-generated method stub
		return super.getRowElement(row);
	}

	@Override
	protected void setSelected(com.google.gwt.dom.client.Element elem,
			boolean selected) {
		super.setSelected(elem, selected);
	}

	public Column[] getColumns() {
		Column[] columns = new Column[combo.getNoOfCols()];
		int i;
		for (i = 0; i < combo.getNoOfCols(); i++) {
			columns[i] = createColumn(i);
		}
		setColumnActions(columns);
		return columns;
	}

	private void setColumnActions(Column<T, String>[] columns) {
		for (int i = 0; i < combo.getNoOfCols(); i++) {
			columns[i].setFieldUpdater(new FieldUpdater<T, String>() {

				@Override
				public void update(int index, T object, String value) {
					eventFired(index);
				}
			});
		}
	}

	private Column createColumn(final int col) {
		Column<T, String> column = new Column<T, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(T object) {
				return getColumnValue(object, col);
			}

		};
		return column;
	}

	protected abstract String getColumnValue(T obj, int col);

	protected void eventFired(int rowIndex) {
		combo.changeValue(rowIndex);
	}

	public void setText(int row, int column, String text) {
		this.body.setText(row, column, text);
		this.cellFormatter.getElement(row, column).setTitle(text);
	}

	public int insertRow(int beforeRow) {
		this.body.insertRow(beforeRow);
		return beforeRow;
	}

	public void addLabel(int row, int column, String text) {
		final Label label = new Label();
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				widgetClicked((Label) event.getSource());
			}
		});

		if (text != null)
			label.setText(text.toString());
		else
			label.setText(" ");

		setWidget(row, column, label);
	}

	public void setWidget(int row, int column, final Widget widget) {
		this.body.setWidget(row, column, widget);
	}

	private void widgetClicked(Widget widget) {

		RowCell cell = getCellByWidget(widget);
		onRowSelect(cell.getRowIndex());

		// /**
		// * preventing Cell click event when widget clicked in Cell
		// */
		// Timer timer = new Timer() {
		//
		// @Override
		// public void run() {
		// disable = false;
		// }
		// };
		// timer.schedule(100);
	}

	/**
	 * Called this method when cell clicked in Row on This Grid
	 * 
	 * @param cell
	 */
	protected abstract void onRowSelect(int row);

	protected RowCell getCellByWidget(Widget widget) {
		Element td = DOM.getParent(widget.getElement());
		Element tr = DOM.getParent(td);
		Element parent = DOM.getParent(tr);
		int row = DOM.getChildIndex(parent, tr);
		int col = DOM.getChildIndex(tr, td);
		return new RowCell(row, col);
	}

	@Override
	public int getKeyboardSelectedRow() {
		return super.getKeyboardSelectedRow();
	}

	@Override
	protected com.google.gwt.dom.client.Element getKeyboardSelectedElement() {
		return super.getKeyboardSelectedElement();
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

	@Override
	public void onBrowserEvent2(Event event) {

		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:
			if (bodyrowElem != null)
				bodyrowElem.getStyle().clearBackgroundColor();
			bodyrowElem = (Element) DOM.eventGetTarget(event)
					.getParentElement();

			String tagName = bodyrowElem.getTagName();
			if ("td".equalsIgnoreCase(tagName)
					|| "th".equalsIgnoreCase(tagName)) {
				bodyrowElem = (Element) bodyrowElem.getParentElement();
			}
			if (DOM.isOrHasChild(this.getElement(), bodyrowElem)) {
				bodyrowElem.getStyle().setBackgroundColor("#D3E8A3");

			}
			eatEvent(event);
			break;
		case Event.ONCLICK:
		case Event.ONMOUSEOUT:
			if (bodyrowElem != null)
				bodyrowElem.getStyle().clearBackgroundColor();

			eatEvent(event);
			break;
		}
		super.onBrowserEvent2(event);
	}

	public void removeRow(int rowindex) {
		((TableSectionElement) this.getChildContainer()).deleteRow(rowindex);
	}

	private void eatEvent(Event event) {
		DOM.eventCancelBubble(event, true);
		DOM.eventPreventDefault(event);
	}
}
