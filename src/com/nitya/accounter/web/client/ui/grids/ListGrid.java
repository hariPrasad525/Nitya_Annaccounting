package com.nitya.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.externalization.AccounterMessages2;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.grids.AbstractTransactionGrid.RecordClickHandler;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

/**
 * ListGrid is Table widget that supports editing & updating Cells.Sort Columns
 * 
 * @author kumar kasimala
 * 
 * @param <T>
 *            is Object that consider as Row in the table
 */
public abstract class ListGrid<T> extends CustomTable implements HasRows {

	public static final int COLUMN_TYPE_IMAGE = 1;
	public static final int COLUMN_TYPE_TEXT = 2;
	public static final int COLUMN_TYPE_DATE = 3;
	public static final int COLUMN_TYPE_CHECK = 4;
	public static final int COLUMN_TYPE_TEXTBOX = 5;
	public static final int COLUMN_TYPE_SELECT = 7;
	public static final int COLUMN_TYPE_LABEL = 8;
	public static final int COLUMN_TYPE_HTML = 9;
	public static final int COLUMN_TYPE_LINK = 10;
	public static final int COLUMN_TYPE_DECIMAL_TEXTBOX = 11;
	public static final int COLUMN_TYPE_DECIMAL_TEXT = 12;
	public static final int COLUMN_TYPE_QUANTITY_POPUP = 13;
	public static final int COLUMN_TYPE_COMBO = 14;

	public static final int EDIT_EVENT_CLICK = 1;
	public static final int EDIT_EVENT_DBCLICK = 2;

	protected boolean isAddRequired;

	protected Map<Integer, Widget> widgetsMap = new HashMap<Integer, Widget>();
	List<T> objects = new ArrayList<T>();
	T selectedObject = null;

	private boolean isEditEnable;

	ClientCurrency currency = getCompany().getPrimaryCurrency();
	protected static AccounterMessages messages = Global.get().messages();
	protected static AccounterMessages2 messages2 = Global.get().messages2();

	private int editEventType = 1;
	protected RecordClickHandler<T> recordClickHandler;

	public ListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.isMultiSelectionEnable = isMultiSelectionEnable;
		this.addStyleName("listgrid");
	}

	public ListGrid(boolean isMultiSelectionEnable, boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
		this.addStyleName("listgrid");
	}

	@Override
	public void init() {
		super.init();
	}

	/**
	 * set Event when you want edit the Cell
	 * 
	 * @waring this is feture not yet implemented
	 * @param eventType
	 */
	public void setEditEventType(int eventType) {
		this.editEventType = eventType;
	}

	protected Event getCellEvent() {
		return this.event;
	}

	/**
	 * when row clicked , CheckBox value is becomes true on that row if
	 * multiselection is true
	 * 
	 * @param row
	 */
	public void selectRow(int row) {
		if (isMultiSelectionEnable) {
			/* Changing the state of check box opposite to its previus state */
			((CheckBox) this.body.getWidget(row, 0))
					.setValue(!((CheckBox) this.body.getWidget(row, 0))
							.getValue());
		}
		selectedObject = objects.get(row);
		onSelectionChanged(selectedObject, row, true);
	}

	/**
	 * add Row with Data
	 * 
	 * @param obj
	 */
	public void addData(T obj) {
		addData(obj, false);
	}

	private void widgetClicked(Widget widget) {
		disable = true;
		RowCell cell = getCellByWidget(widget);
		cellClicked(cell.getRowIndex(), cell.getCellIndex());

		/**
		 * preventing Cell click event when widget clicked in Cell
		 */
		Timer timer = new Timer() {

			@Override
			public void run() {
				disable = false;
			}
		};
		timer.schedule(100);
	}

	/**
	 * Called this method when cell clicked in Row on This Grid
	 * 
	 * @param cell
	 */
	@Override
	protected final void cellClicked(int row, int col) {
		if (isContinueToexecuteEvent(row, col)) {
			currentCol = isMultiSelectionEnable ? col - 1 : col;
			currentRow = row;
			if (!objects.isEmpty()) {
				selectedObject = objects.get(row);

				onClick(selectedObject, currentRow, currentCol);

				if (selectedObject == null)
					return;

				if (editEventType == EDIT_EVENT_CLICK) {
					if (isEditEnable) {

						if (!isEditable(selectedObject, currentRow, currentCol)) {
							selectRow(currentRow);
							return;
						}
						// get text of clicked Cell
						editCell(DOM
								.getChild(rowFormatter.getElement(row), col)
								.getInnerText());
					}
				}

			}
		}
	}

	@Override
	protected final void cellDoubleClicked(int row, int col) {
		if (isContinueToexecuteEvent(row, col)) {
			currentCol = isMultiSelectionEnable ? col - 1 : col;
			currentRow = row;
			selectedObject = objects.get(row);

			ListGrid.this.onDoubleClick(selectedObject, currentRow, currentCol);

			if (editEventType == EDIT_EVENT_DBCLICK) {

				if (isEditEnable) {
					if (!isEditable(selectedObject, currentRow, currentCol))
						return;
					// get text of clicked Cell
					editCell(DOM.getChild(rowFormatter.getElement(row), col)
							.getInnerText());
				}
			}

		}
	}

	public boolean isContinueToexecuteEvent(int row, int col) {
		if (!isMultiSelectionEnable) {
			if (rowFormatter != null) {
				rowFormatter.removeStyleName(currentRow, "selected");
				rowFormatter.addStyleName(row, "selected");
			}
		}

		if (isEditEnable) {
			/**
			 * skip if the cell is all ready editmode
			 */
			if (isCellEditMode(row, col))
				return false;
			stopEditing(currentCol);
		}

		/**
		 * ship cell is 0th cell in Row
		 */
		if (isMultiSelectionEnable && col == 0)
			return false;
		if (objects.size() > 0)
			if (objects.size() < row || objects.get(row) == null)
				return false;
		return true;
	}

	/**
	 * setRecords in Table
	 * 
	 * @param list
	 */
	public void setRecords(List<T> list) {
		removeAllRecords();
		if (list.size() == 0) {
			addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		for (T t : list) {
			addData(t);
		}
	}

	/**
	 * setRecords in Table
	 * 
	 * @param list
	 */
	public void addRecords(List<T> list) {
		for (T t : list) {
			addData(t);
		}
	}

	public void addRecordClickHandler(RecordClickHandler<T> recordClickHandler) {
		this.recordClickHandler = recordClickHandler;
	}

	private void editCell(String val) {
		int type = getColumnType(currentCol);
		switch (type) {
		case COLUMN_TYPE_DATE:
			ClientFinanceDate date = null;
			if (!val.equals(""))
				date = new ClientFinanceDate(val);
			else
				date = new ClientFinanceDate();
			addOrEditDateWidget(selectedObject, date);
			break;
		case COLUMN_TYPE_TEXTBOX:
			addOrEditTextBox(selectedObject, val);
			break;
		case COLUMN_TYPE_DECIMAL_TEXTBOX:
			addOrEditTextBox(selectedObject, val);
			break;
		case COLUMN_TYPE_SELECT:
			addOrEditSelectBox(selectedObject, val);
			break;
		case COLUMN_TYPE_QUANTITY_POPUP:
			addQuantityPopup(selectedObject, val);
			break;
		case COLUMN_TYPE_COMBO:
			addOrEditCombo(selectedObject, val);
			break;
		default:
			break;
		}

	}

	private void addOrEditCombo(T obj, final String value) {
		if (widgetsMap.get(currentCol) == null) {
			final SelectCombo selectbox = new SelectCombo("");
			final String[] values = getSelectValues(obj, currentCol);
			final DynamicForm form = new DynamicForm("form");
			form.add(selectbox);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					selectbox.addItem(values[i]);
				}
			}

			selectbox
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {

							selectbox.setComboItem(selectItem);

							onValueChange(selectedObject, currentCol,
									selectbox.getSelectedValue());

							onWidgetValueChanged(form,
									selectbox.getSelectedValue());

						}

					});

			if (value != null)
				setSelectedValue(obj, selectbox, value);
			widgetsMap.put(currentCol, form);
			setWidget(currentRow, currentCol, form);
			// selectbox.setFocus(true);
		} else {
			DynamicForm form = (DynamicForm) widgetsMap.get(currentCol);
			if (value != null) {
				SelectCombo field = (SelectCombo) form.getWidget(0);
				setSelectedValue(obj, field, value);
				DynamicForm form1 = new DynamicForm("form1");
				form1.add(field);
				setWidget(currentRow, currentCol, form1);
				// field.setFocus(true);
			}

		}
	}

	private void addQuantityPopup(T selectedObject2, String val) {

		PopupPanel popupPanel = new PopupPanel();

		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		TextItem textField = new TextItem(messages.quantity(), "textField");
		SelectCombo selectCombo = new SelectCombo(messages.units());
		// selectCombo.setWidth("50%");
		DynamicForm dynamicForm = new DynamicForm("dynamicForm");
		// dynamicForm.setNumCols(4);
		dynamicForm.add(textField, selectCombo);

		horizontalPanel.add(dynamicForm);
		popupPanel.add(horizontalPanel);
		setWidget(currentRow, currentCol, popupPanel);
		popupPanel.show();
		popupPanel.center();
		popupPanel.setAutoHideEnabled(true);
		// popupPanel.setPopupPosition(left, top)

	}

	private void addData(final T obj, boolean showInEditMode) {

		int rowCount = this.body.getRowCount();
		objects.add(obj);
		// add checkbox if the isMultiSelectionEnable is true
		if (isMultiSelectionEnable) {
			final CheckBox box = new CheckBox();
			box.setEnabled(!disable);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					onCheckBoxValueChange((CheckBox) event.getSource());
				}
			});
			setWidget(rowCount, -1, box);
		}
		// if the multiSecltion is true, than x is 1, else 0
		for (int x = (isMultiSelectionEnable ? 1 : 0); x < nofCols; x++) {
			addColumnData(obj, rowCount, isMultiSelectionEnable ? x - 1 : x);
		}
		if (rowFormatter != null) {
			if (rowCount % 2 == 0) {

				rowFormatter.addStyleName(rowCount, "gridEvenRow");
			} else
				rowFormatter.addStyleName(rowCount, "gridOddRow");
			rowFormatter.addStyleName(rowCount, "gridRow");
		}
		// to fix columns aligment problem of ,
		// if (rowCount == 0)
		this.adjustCellsWidth(rowCount, body);

		super.fixHeader();

		NativeEvent ne = Document.get().createChangeEvent();
		DomEvent.fireNativeEvent(ne, this);
	}

	protected void onCheckBoxValueChange(CheckBox box) {
		int row = getCellByWidget(box).getRowIndex();
		selectedObject = objects.get(row);
		if (box.getValue())
			rowFormatter.addStyleName(row, "selected");
		else
			rowFormatter.removeStyleName(row, "selected");

		onSelectionChanged(selectedObject, row, box.getValue());
	}

	/**
	 * Add Cell with specified widget
	 * 
	 * @param obj
	 * @param row
	 * @param col
	 */
	protected void addColumnData(T obj, int row, int col) {

		selectedObject = obj;
		int type = 0;
		currentRow = row;
		currentCol = col;
		type = getColumnType(col);

		if (col == getColumnsCount() - 1) {
			addCellStyles("removeRightBorder");
		}

		Object data = getColumnValue(obj, col);
		switch (type) {

		case COLUMN_TYPE_DATE:
			// setText(currentRow, currentCol, data != null ? UIUtils
			// .stringToDate(new ClientFinanceDate(data.toString()))
			// : UIUtils.stringToDate(new ClientFinanceDate()));
			setText(currentRow, currentCol, data != null ? data.toString()
					: new ClientFinanceDate().toString());
			addCellStyles("gridDateBoxCell");
			break;
		case COLUMN_TYPE_CHECK:
			addCheckBox(obj, (Boolean) data);
			addCellStyles("gridCheckBoxCell table-checkbox-cell");
			break;
		case COLUMN_TYPE_IMAGE:
			addImage(obj, data);
			addCellStyles("gridImageCell delete-table-image");
			break;
		case COLUMN_TYPE_SELECT:
			setText(currentRow, currentCol, (String) data);
			addCellStyles("gridSelectBoxCell");
			break;
		case COLUMN_TYPE_TEXT:
			// if (data instanceof Double) {
			// data = DataUtils.amountAsStringWithCurrency((Double) data,
			// currency);
			// }
			if (data != null) {
				setText(currentRow, currentCol, data.toString());
			}
			addCellStyles("gridTextCell");
			break;
		case COLUMN_TYPE_TEXTBOX:
			if (data instanceof Double) {
				data = DataUtils.amountAsStringWithCurrency((Double) data,
						currency);
			}
			setText(currentRow, currentCol, data != null ? data.toString() : "");
			addCellStyles("gridTextBoxCell");
			break;
		case COLUMN_TYPE_LABEL:
			if (data instanceof Double) {
				data = DataUtils.amountAsStringWithCurrency((Double) data,
						currency);
			}
			addLabel(obj, data);
			addCellStyles("gridLabelCell");
			break;
		case COLUMN_TYPE_HTML:
			this.body.setHTML(currentRow, currentCol, (String) data);
			addCellStyles("gridLabelCell");
			break;
		case COLUMN_TYPE_LINK:
			addLink(obj, data);
			addCellStyles("gridLabelCell");
			break;
		case COLUMN_TYPE_DECIMAL_TEXTBOX:
			if (data instanceof Double) {
				data = DataUtils.amountAsStringWithCurrency((Double) data,
						currency);
			}
			setText(currentRow, currentCol, data != null ? data.toString() : "");
			addCellStyles("gridTextBoxCell");
			addCellStyles("gridDecimalCell");
			break;
		case COLUMN_TYPE_DECIMAL_TEXT:
			if (data instanceof Double) {
				data = DataUtils.amountAsStringWithCurrency((Double) data,
						currency);
			}
			setText(currentRow, currentCol, data != null ? data.toString() : "");
			addCellStyles("gridDecimalCell");
			break;
		case COLUMN_TYPE_QUANTITY_POPUP:
			data = data;
			setText(currentRow, currentCol, data != null ? data.toString() : "");
			break;
		case COLUMN_TYPE_COMBO:
			setText(currentRow, currentCol, (String) data);
			break;
		}

		addCellStyles(getRowElementsStyle(currentCol));
	}

	private void addLink(final T obj, final Object value) {
		final Anchor ar = new Anchor();
		ar.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				widgetClicked((Anchor) event.getSource());
				onDoubleClick(obj, currentRow, currentCol);
			}
		});
		if (value != null) {
			ar.setText(value.toString());
		}
		this.setWidget(currentRow, currentCol, ar);
	}

	private void addLabel(final T obj, final Object value) {
		final Label label = new Label();
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				widgetClicked((Label) event.getSource());
			}
		});

		if (value != null)
			label.setText(value.toString());
		else
			label.setText(" ");

		setWidget(currentRow, currentCol, label);
	}

	/**
	 * Stop editing in current Row
	 */
	public void endEditing() {
		for (int x = isMultiSelectionEnable ? 1 : 0; x < nofCols; x++) {
			stopEditing(x);
		}
	}

	@Override
	protected void initHeader() {
		CellFormatter headerCellFormater = this.header.getCellFormatter();
		if (headerCellFormater != null) {
			for (int x = isMultiSelectionEnable ? 1 : 0; x < nofCols; x++) {
				this.header.setText(0, x,
						getColumns()[isMultiSelectionEnable ? (x - 1) : x]);

				headerCellFormater.addStyleName(0, x, "gridHeaderCell");
				headerCellFormater.addStyleName(0, x, getHeaderStyle(x));

				if (getColumnType(isMultiSelectionEnable ? (x - 1) : x) == COLUMN_TYPE_DECIMAL_TEXT)
					this.header.getCellFormatter().addStyleName(0, x,
							"gridDecimalCell");
				if (x == nofCols - 1) {
					this.header.getCellFormatter().addStyleName(0, x,
							"removeRightBorder");
				}
			}
		}

	}

	protected void addOrEditTextBox(final T obj, final Object value) {
		final TextBox textBox;
		if (widgetsMap.get(currentCol) == null) {
			textBox = new TextBox();
			textBox.removeStyleName("gwt-TextBox");
			// textBox.setWidth("100%");
			if (getColumnType(currentCol) == COLUMN_TYPE_DECIMAL_TEXTBOX)
				textBox.setTextAlignment(TextBoxBase.ALIGN_RIGHT);
			textBox.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					onWidgetValueChanged(textBox, textBox.getValue().toString());
				}
			});
			if (value != null)
				textBox.setValue(value.toString());

			widgetsMap.put(currentCol, textBox);
			setWidget(currentRow, currentCol, textBox);
		} else {
			textBox = (TextBox) widgetsMap.get(currentCol);
			if (value != null)
				textBox.setValue(value.toString());
			setWidget(currentRow, currentCol, textBox);
		}
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				textBox.setFocus(true);
				if (getColumnType(currentCol) == COLUMN_TYPE_DECIMAL_TEXTBOX)
					textBox.setSelectionRange(0, 0);
				else {
					textBox.setSelectionRange(textBox.getValue().length(), 0);
				}
			}
		});
	}

	/**
	 * called when value of a widget changed
	 */
	public void onWidgetValueChanged(Widget widget, Object value) {

		this.remove(widget);

		setText(currentRow, currentCol, value.toString());

		editComplete(selectedObject, value, currentCol);

		currentCol = 0;
		currentRow = 0;
	}

	protected void addImage(final T obj, Object value) {
		ImageResource imgrs = null;
		if (value instanceof ImageResource) {
			imgrs = (ImageResource) value;
		}
		final Image image = imgrs == null ? new Image() : new Image(imgrs);
		image.setSize("16px", "16px");
		image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				widgetClicked((Image) event.getSource());
			}
		});
		if (value instanceof String)
			image.setUrl(value.toString());
		setWidget(currentRow, currentCol, image);
	}

	protected void addCheckBox(final T obj, Boolean value) {
		final CheckBox checkBox = new CheckBox();
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				CheckBox box = ((CheckBox) event.getSource());
				Object col = null;
				if (cellFormatter != null) {
					col = UIUtils.getKey(widgetsMap, box);
				} else {
					RowCell cellByWidget = getCellByWidget(checkBox);
					col = cellByWidget.getCellIndex();
				}
				if (col != null)
					editComplete(obj, box.getValue(), (Integer) col);
			}
		});
		if (value != null)
			checkBox.setValue(value);
		if (disable || !isEnableCheckBox) {
			checkBox.setEnabled(false);
		}
		setWidget(currentRow, currentCol, checkBox);
		// cellFormatter.setStyleName(currentRow, currentCol, "gridCheckCell");
	}

	/**
	 * add SelectBox
	 * 
	 * @param obj
	 * @param row
	 * @param col
	 */
	protected void addOrEditSelectBox(final T obj, Object value) {
		if (widgetsMap.get(currentCol) == null) {
			final ListBox selectbox = new ListBox();
			final String[] values = getSelectValues(obj, currentCol);
			if (values != null)
				for (int i = 0; i < values.length; i++) {
					selectbox.addItem(values[i]);
				}
			selectbox.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					onValueChange(selectedObject, currentCol,
							values[selectbox.getSelectedIndex()]);
					onWidgetValueChanged(selectbox,
							values[selectbox.getSelectedIndex()]);
				}
			});

			if (value != null)
				setshowValue(obj, selectbox, value);
			widgetsMap.put(currentCol, selectbox);
			setWidget(currentRow, currentCol, selectbox);
			selectbox.setFocus(true);
		} else {
			ListBox box = (ListBox) widgetsMap.get(currentCol);
			if (value != null)
				setshowValue(obj, box, value);
			setWidget(currentRow, currentCol, box);
			box.setFocus(true);
		}
	}

	private void setshowValue(T obj, ListBox box, Object value) {
		final String[] values = getSelectValues(obj, currentCol);
		int index = Arrays.asList(values).indexOf(value);
		if (index != -1) {
			box.setSelectedIndex(index);
		}
	}

	private void setSelectedValue(T obj, SelectCombo box, Object value) {
		final String[] values = getSelectValues(obj, currentCol);
		int index = Arrays.asList(values).indexOf(value);
		if (index != -1) {
			box.setSelectedItem(index);
		}
	}

	protected void addOrEditDateWidget(final T obj, Object value) {
		if (widgetsMap.get(currentCol) == null) {
			final DateBox datePicker = new DateBox();
			ClientFinanceDate val = (ClientFinanceDate) value;
			datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {

				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {

					onWidgetValueChanged(datePicker,
							DateUtills.getDateAsString(event.getValue()));

				}
			});
			datePicker.setFormat(new DateBox.DefaultFormat(DateTimeFormat
					.getFormat("yyyy-MM-dd")));
			datePicker.setValue(val.getDateAsObject());

			widgetsMap.put(currentCol, datePicker);

			setWidget(currentRow, currentCol, datePicker);
			datePicker.setFocus(true);
		} else {
			DateBox box = (DateBox) widgetsMap.get(currentCol);
			box.setFormat(new DateBox.DefaultFormat(DateTimeFormat
					.getFormat("yyyy-MM-dd")));
			box.setValue(((ClientFinanceDate) value).getDateAsObject());
			setWidget(currentRow, currentCol, box);
			box.setFocus(true);
		}
	}

	public int getSelectedRecordIndex() {
		return this.objects.indexOf(selectedObject);
	}

	public int getCurrentRow() {
		return this.currentRow;
	}

	public int getCurrentColumn() {
		return this.currentCol;
	}

	// public void addFooterValues(String... values) {
	// for (int i = isMultiSelectionEnable ? 1 : 0; i < nofCols; i++) {
	// if (i < values.length)
	// this.footer.setText(0, i, values[i]);
	// else
	// this.footer.setText(0, i, "");
	//
	// this.footer.getCellFormatter().addStyleName(0, i, "gridDecimalCell");
	//
	// this.footer.getCellFormatter()
	// .addStyleName(0, i, "gridDecimalCell");
	//
	// }
	// }

	/**
	 * add Value at footer
	 * 
	 * @param value
	 *            is value in Column
	 * @param col
	 *            is column index
	 */
	// public void addFooterValue(String value, int col) {
	// this.footer.setText(0, isMultiSelectionEnable ? col + 1 : col, value);
	// }

	// public void updateFooterValues(String value, int index) {
	// this.footer.setText(0, isMultiSelectionEnable ? index + 1 : index,
	// value);
	// }

	private void addCellStyles(String... styles) {
		for (String style : styles) {
			if (cellFormatter != null) {
				cellFormatter.addStyleName(currentRow,
						isMultiSelectionEnable ? currentCol + 1 : currentCol,
						style);
			}
		}
	}

	public void startEditing(int row) {
		currentRow = row;
		for (int x = isMultiSelectionEnable ? 1 : 0; x < nofCols; x++) {
			currentCol = x;
			editCell(cellFormatter.getElement(row, x).getInnerText());
		}
	}

	public void stopEditing(int col) {
		if (widgetsMap.size() > 0 && widgetsMap.get(col) != null
				&& !(widgetsMap.get(col) instanceof CheckBox)) {
			addColumnData(this.objects.get(currentRow), currentRow, col);
			this.remove(widgetsMap.get(col));
		}
	}

	/**
	 * Called when any cell click On Header of This table
	 * 
	 * @param cell
	 */
	@Override
	public void headerCellClicked(int colIndex) {
		if ((colIndex == 0 && isMultiSelectionEnable)
				|| getColumnType(colIndex) == ListGrid.COLUMN_TYPE_IMAGE) {
			return;
		}
		sort(colIndex, isDecending);
	}

	@Override
	public void checkedUncheckedCheckBox(boolean isEnable) {
		super.checkedUncheckedCheckBox(isEnable);
		onHeaderCheckBoxClick(isEnable);

	}

	public void deleteRecord(T obj) {
		int index = this.objects.indexOf(obj);
		this.deleteRecord(index);
	}

	public void deleteSelectionRecords() {
		for (int i = 0; i < this.getTableRowCount(); i++) {
			CheckBox box = (CheckBox) this.getWidget(i, 0);
			if (box.getValue()) {
				deleteRecord(i);
			}
		}
	}

	public void removeAllRecords() {
		this.removeAllRows();
		this.objects.clear();
		this.selectedObject = null;
	}

	public void editComplete(T item, Object value, int col) {

	}

	/**
	 * Update a Record when Cell value changed
	 * 
	 * @param obj
	 * @param row
	 * @param col
	 */
	public void updateRecord(T obj, int row, int col) {
		addColumnData(obj, row, col);

		currentCol = 0;
		currentRow = 0;
	}

	public void updateData(T obj) {
		int rowIndex = this.objects.indexOf(obj);
		if (rowIndex > -1) {
			for (int i = isMultiSelectionEnable ? 1 : 0; i < nofCols; i++) {
				addColumnData(obj, rowIndex, isMultiSelectionEnable ? i - 1 : i);
			}
		}
		currentCol = 0;
		currentRow = 0;
	}

	public T getSelection() {
		return selectedObject;
	}

	public void setSelection(T selectedObject) {
		this.selectedObject = selectedObject;
	}

	public List<T> getSelectedRecords() {
		List<T> list = new ArrayList<T>();
		if (isMultiSelectionEnable)
			for (int i = 0; i < this.getTableRowCount(); i++) {
				Widget widget = this.getWidget(i, 0);
				if (widget instanceof CheckBox) {
					CheckBox box = (CheckBox) widget;
					if (box != null)
						if (box.getValue()) {
							list.add(objects.get(i));
						}
				}
			}
		return list;
	}

	public List<T> getRecords() {
		List<T> list = new ArrayList<T>();
		list.addAll(objects);
		return list;
	}

	public T getRecordByIndex(int index) {
		return this.objects.get(index);
	}

	/**
	 * getIndex of the T
	 * 
	 * @param obj
	 * @return
	 */
	public int indexOf(T obj) {
		return this.objects.indexOf(obj);
	}

	protected abstract int getColumnType(int index);

	protected abstract Object getColumnValue(T obj, int index);

	protected abstract String[] getSelectValues(T obj, int index);

	protected abstract void onValueChange(T obj, int index, Object value);

	protected abstract boolean isEditable(T obj, int row, int index);

	protected abstract void onClick(T obj, int row, int index);

	/**
	 * to get the header style names required mainly in windows8
	 * 
	 * @param index
	 * @return
	 */
	protected abstract String getHeaderStyle(int index);

	/**
	 * to get the row style name
	 * 
	 * @param index
	 * @return
	 */
	protected abstract String getRowElementsStyle(int index);

	protected void onSelectionChanged(T obj, int row, boolean isChecked) {
	}

	protected void onDoubleClick(T obj, int row, int index) {
	}

	public void onHeaderCheckBoxClick(boolean isChecked) {
	}

	public abstract void onDoubleClick(T obj);

	// public abstract void comboSelected();

	/**
	 * Sort the two objects based on the column index
	 * 
	 * @param obj1
	 * @param obj2
	 * @param index
	 *            Column Index
	 * @return
	 */
	protected abstract int sort(T obj1, T obj2, int index);

	public ValidationResult validateGrid() {
		return null;
	}

	public void setCanEdit(boolean enabled) {
		this.isEditEnable = enabled;
	}

	/**
	 * Sort Colunm depends on Cell
	 * 
	 * @param index
	 * @param isDecending
	 */
	public void sort(final int index, final boolean isDecending) {
		if (cellFormatter != null) {
			Collections.sort(this.objects, new Comparator<T>() {

				@Override
				public int compare(T o1, T o2) {
					int ret = sort(o1, o2, index);
					return isDecending ? (-1 * ret) : (ret);
				}
			});
			recreateData();
		}
	}

	private void recreateData() {
		int rowCount = 0;
		if (this instanceof AccountRegisterOtherListGrid) {
			((AccountRegisterOtherListGrid) this).balance = 0;
			((AccountRegisterOtherListGrid) this).totalBalance = 0;
		}
		for (T obj : this.objects) {
			for (int x = isMultiSelectionEnable ? 1 : 0; x < nofCols; x++) {
				addColumnData(obj, rowCount, isMultiSelectionEnable ? x - 1 : x);
			}
			rowCount++;
		}
		this.isDecending = !isDecending;
	}

	public void setMultiSelection(boolean multi) {
		this.isMultiSelectionEnable = multi;
	}

	public void deleteRecord(int row) {
		currentCol = 0;
		currentRow = 0;
		selectedObject = null;
		this.objects.remove(row);
		this.removeRow(row);
		if (this.objects.size() > 0) {
			this.adjustCellsWidth(0, this.body);
		}
		deleteRowFromPager();
	}

	/**
	 * 
	 * @param row
	 * @param cellIndex
	 * @return
	 */
	public boolean isCellEditMode(int row, int cell) {
		Widget widget = this.body.getWidget(row, cell);
		if (widget == null)
			return false;
		return widget instanceof TextBox ? true
				: widget instanceof Label ? true
						: widget instanceof ListBox ? true
								: widget instanceof DateBox ? true : false;
	}

	public int getColumnsCount() {
		return nofCols;
	}

	public ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	public void refreshAllRecords() {
		List<T> records = this.getRecords();
		this.removeAllRecords();
		this.setRecords(records);
	}

	@Override
	public void setEnabled(boolean disable) {
		super.setEnabled(disable);
		refreshAllRecords();
	}

	// HasRows
	private Range visibleRange = new Range(0, 0);
	private boolean isRowCountExact;
	private int rowCount;
	private Handler handler;
	private Handler handler2;

	@Override
	public void fireEvent(GwtEvent<?> event) {
	}

	@Override
	public HandlerRegistration addRangeChangeHandler(Handler handler) {
		this.handler = handler;
		return null;
	}

	public HandlerRegistration addRangeChangeHandler2(Handler handler) {
		this.handler2 = handler;
		return null;
	}

	@Override
	public HandlerRegistration addRowCountChangeHandler(
			com.google.gwt.view.client.RowCountChangeEvent.Handler handler) {
		return null;
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public Range getVisibleRange() {
		return visibleRange;
	}

	@Override
	public boolean isRowCountExact() {
		return isRowCountExact;
	}

	@Override
	public void setRowCount(int count) {
		rowCount = count;
	}

	@Override
	public void setRowCount(int count, boolean isExact) {
		rowCount = count;
		isRowCountExact = isExact;
		if (handler != null) {
			if (count == 0) {
				visibleRange = new Range(-1, visibleRange.getLength());
			}
			RangeChangeEvent rangeChangeEvent = new RangeChangeEvent(
					visibleRange) {
			};
			handler.onRangeChange(rangeChangeEvent);
		}
	}

	@Override
	public void setVisibleRange(int start, int length) {
		setVisibleRange(new Range(start, length));
	}

	@Override
	public void setVisibleRange(Range range) {
		visibleRange = range;
		RangeChangeEvent rangeChangeEvent = new RangeChangeEvent(range) {
		};
		if (handler2 != null) {
			handler2.onRangeChange(rangeChangeEvent);
		}
	}

	public void updateRange(Range range) {
		visibleRange = range;
	}

	private void deleteRowFromPager() {
		int length = visibleRange.getLength();// page size
		int start = visibleRange.getStart();
		int curLen = rowCount - start - 1;// -1 for delete record
		if (curLen == 0) {// page become empty
			start -= length;// Prev page
		}
		if (start < 0) {// If current page is first page
			start = 0;
		}
		setVisibleRange(start, length);// Reload page
	}

}
