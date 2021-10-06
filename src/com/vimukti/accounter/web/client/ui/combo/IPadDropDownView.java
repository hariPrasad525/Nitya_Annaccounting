package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ButtonGroup;
import com.vimukti.accounter.web.client.ui.core.IButtonContainer;

public class IPadDropDownView<T> extends DropDownView<T> implements
		IButtonContainer {

	private DropDownTable<T> dropDown;
	private boolean isAddNewRequire;

	@Override
	public void show(int x, int y) {
		MainFinanceWindow.getViewManager().showView(this, null, Boolean.TRUE,
				ActionFactory.getDropDOwn());
	}

	@Override
	public void init(DropDownCombo<T> combo, List<T> comboItems,
			boolean isAddNewRequire) {
		this.combo = combo;
		this.isAddNewRequire = isAddNewRequire;

		dropDown = new DropDownTable<T>(combo) {

			@Override
			protected void onRowSelect(int row) {
				if (this.combo.comboItems.size() >= row) {
					selectedObject = this.combo.comboItems.get(row);
				}
			}

			@Override
			protected String getColumnValue(T object, int col) {
				if (object.equals("addNewCaption")) {
					if (combo.getNoOfCols() > 1)
						return (col == 1) ? combo.messages
								.comboDefaultAddNew(combo
										.getDefaultAddNewCaption()) : "  ";
					else
						return combo.messages.comboDefaultAddNew(combo
								.getDefaultAddNewCaption());
				}
				return combo.getColumnData(object, col);
			}
		};

		ScrollPanel panel = new ScrollPanel();
		panel.getElement().removeAttribute("style");
		panel.addStyleName("dropdownTable");
		panel.add(dropDown);
		dropDown.addStyleName("dropDown");

		List<T> list = new ArrayList<T>(comboItems);
		dataProvider.setList(list);
		dataProvider.addDataDisplay(dropDown);

		this.add(panel);
		dropDown.getElement().getStyle().setCursor(Cursor.POINTER);

	}

	@Override
	public void addButtons(ButtonGroup group) {
		if (!isAddNewRequire) {
			return;
		}
		Button addNew = new Button("Add");
		addNew.getElement().setId("addNew");
		addNew.setTitle(messages.clickThisToOpen(messages.previousTransaction()));
		addNew.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				combo.changeValue(0);
			}
		});
		group.add(addNew);
	}

	@Override
	public void addEscapEventHandler(EscapEventHandler escapEventHandler) {
	}

	@Override
	public void setList(List<T> newList) {
		// if (this.isAddNewRequire) {
		// newList.add(newList.z, (T)messages.add());
		// }
		int size = newList.size();
		dataProvider.setList(newList);
		dataProvider.refresh();
		dropDown.setRowCount(size);
		dropDown.setPageSize(size);
	}

	@Override
	public boolean isShowing() {
		return true;
	}

	public void add(T obj) {
		dataProvider.getList().add(obj);
		dropDown.setRowCount(dataProvider.getList().size());
		dropDown.setPageSize(dropDown.getRowCount());
	}

	public void remove(T obj) {
		dataProvider.getList().remove(obj);
	}

	public void hide() {
		cancel();
	}

	@Override
	public int updateIndex(int rowIndex) {
		if (isAddNewRequire)
			return rowIndex+1;
		else
			return rowIndex;
	}
}
