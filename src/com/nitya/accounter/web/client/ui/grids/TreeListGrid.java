package com.nitya.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.nitya.accounter.web.client.ui.edittable.EditColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.ExpandImageColumn;
import com.nitya.accounter.web.client.ui.edittable.RenderContext;
import com.nitya.accounter.web.client.ui.edittable.TreeColumn;

public abstract class TreeListGrid<R> extends EditTable<R> {

	Map<R, List<R>> parentRows = new HashMap<R, List<R>>();

	EditColumn<R> firstColumn;

	public void init() {
		this.createColumns();
	}

	public void addColumn(TreeColumn<R> column) {
		if (this.firstColumn == null && column.getForParent() != null) {
			this.firstColumn = column;
			super.addColumn(new TreeColumn<>(new ExpandImageColumn<R>(column.getForParent()), column.getForChild()));
		} else {
			super.addColumn(column);
		}
	}

	protected abstract void initColumns();

	public EditColumn<R> getConcretColumn(EditColumn<R> column, R row, RenderContext<R> context) {
		if (column instanceof TreeColumn) {
			TreeColumn<R> treeCol = (TreeColumn<R>) column;
			return treeCol.getColumn(this.parentRows.get(row)!= null);
		}
		return column;
	}
	
	@Override
	public void update(R row) {
		if(parentRows.get(row) != null) {
			super.updateRows(parentRows.get(row));
		} else {
			R parent = getParent(row);
			super.updateRows(parentRows.get(parent));
			super.update(parent);
		}
		super.update(row);
	}
		
	private R getParent(R child) {
		for (R parent : parentRows.keySet()) {
			 if(parentRows.get(parent).contains(child)) {
				 return parent;
			 }
		}
		return null;
	}
	
	public List<R> getAllChilds() {
		List<R> allChilds = new ArrayList<R>();
		for (R parent : parentRows.keySet()) {
			allChilds.addAll(parentRows.get(parent));
		}
		return allChilds;
	}
	
	public List<R> getAllParents() {
		List<R> allParents = new ArrayList<R>();
		for (R parent : parentRows.keySet()) {
			allParents.add(parent);
		}
		return allParents;
	}

	protected abstract boolean isInViewMode();

	public void addParentRow(R parentRow, List<R> childs) {
		parentRows.put(parentRow, childs);
		add(parentRow);
		addRows(childs);
		this.updateClassNames("parentRow", "childRow", false, parentRow);
	}

	private void updateClassNames(String className, String classChildName, boolean isExpand, R parentRow) {
		List<R> rows = this.getAllRows();
		Element parentEle = this.getRowElement(rows.indexOf(parentRow) + 1);
		if(className != "")
			parentEle.addClassName(className);
		parentEle.toggleClassName("parentRowCollapsed");
		for (R childRow : parentRows.get(parentRow)) {
			Element child = this.getRowElement(rows.indexOf(childRow) + 1);
			if(classChildName != "")
				child.addClassName(classChildName);
			child.toggleClassName("childRowCollapsed");
		}
	}

	@Override
	public void onExpand(R row, boolean isExpand) {
		this.updateClassNames("", "", isExpand, row);
		this.onParentExpand(row, parentRows.get(row), isExpand);
	}

	public void onParentExpand(R parent, List<R> childs, boolean isExpand) {

	}
	
	@Override
	public void clear() {
		parentRows.clear();
		super.clear();
	}

}
