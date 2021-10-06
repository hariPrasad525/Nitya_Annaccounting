package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * TreeGrid is table widget that supports to display rows in hierarchical tree.
 * the tree contains a hierarchy of row that the user can open, close, and
 * select.
 * 
 * @author kumar kasimala
 * 
 * @param <T>
 *            is Object that consider as Row in the table
 */
public abstract class TreeGrid<T> extends CustomTable {

	List<Object> objects = new ArrayList<Object>();

	String currentParent;

	T selectedObject = null;

	public static final int COLUMN_TYPE_TEXT = 1;
	public static final int COLUMN_TYPE_DECIMAL_TEXT = 2;

	public Boolean isCollapse = false;

	private ImageResource nodeIcon;
	private ImageResource parentIcon;

	public TreeGrid(String emptyMessage) {
		super(false);
		this.addStyleName("treeGrid");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected void initHeader() {
		CellFormatter headerCellFormater = this.header.getCellFormatter();

		if (headerCellFormater != null) {
			for (int x = isMultiSelectionEnable ? 1 : 0; x < nofCols; x++) {
				this.header.setText(0, x,
						getColumns()[isMultiSelectionEnable ? (x - 1) : x]);
				headerCellFormater.addStyleName(0, x, "gridHeaderCell");
				if (getColumnType(isMultiSelectionEnable ? (x - 1) : x, 0) == COLUMN_TYPE_DECIMAL_TEXT)
					this.header.getCellFormatter().addStyleName(0, x,
							"gridDecimalCell");
			}
		}
	}

	/**
	 * Hide or show Childs of parent
	 * 
	 * @param parentName
	 * @param row
	 * @param isCollapse
	 */
	private void HideOrShowNodes(String parentName, int row, Boolean isCollapse) {
		String display = isCollapse ? "none" : "";
		parentName = parentName.replace(" ", "");

		for (int i = row + 1; i < this.getTableRowCount(); i++) {
			Element Ele = rowFormatter.getElement(i);
			String clName = Ele.getClassName();
			if ((Arrays.asList(clName.split(" ")).contains(parentName))) {
				Ele.getStyle().setProperty("display", display);
			} else {
				break;
			}
		}
		super.fixHeader();
	}

	/**
	 * Hide or show Child by parent
	 * 
	 * @param parentName
	 * @param isShow
	 */
	public void HideOrShowNodesByParent(String parentName, boolean isShow) {
		int row = this.objects.indexOf(parentName);
		HideOrShowNodes(parentName, row, isShow);
		super.fixHeader();
	}

	public void setParentIcon(ImageResource icon) {
		this.parentIcon = icon;
	}

	public void setNodeIcon(ImageResource icon) {
		this.nodeIcon = icon;
	}

	public void addParentWithChilds(String name, List<T> childNodes) {

		currentRow = this.getTableRowCount();
		currentParent = name;

		if (!this.objects.contains(name)) {
			addObjectToList(name);

			TreeCell treeCell = new TreeCell(parentIcon, name, true, currentRow);
			this.setWidget(currentRow, 0, treeCell);

			for (int x = 1; x < nofCols; x++) {
				this.setText(currentRow, x, this.getParentColumnValue(name, childNodes, x));
			}
		}
		adjustCellsWidth(currentRow, this.body);

		this.addStyleToRow("parentRow");
		if (childNodes != null) {
			addNodes(childNodes);
		}
	}

	public void addObjectToList(Object obj) {
		if (!this.objects.isEmpty()) {
			this.objects.add(currentRow, obj);
		} else {
			this.objects.add(obj);
		}
	}

	public void addParent(String name) {
		// calling recursivly,so there is a possiblity of getting into infinte
		// loop
		this.addParentWithChilds(name, null);// ..........
	}

	/**
	 * addNode to Current parent
	 * 
	 * @param node
	 */
	private void addOrUpdateNode(T node) {
		for (int x = 0; x < nofCols; x++) {
			addCell((String) getColumnValue(node, x), currentRow, x);
		}
		if (currentRow % 2 == 0) {
			addStyleToRow("gridEvenRow");
		} else
			addStyleToRow("gridOddRow");
		// addStyleToRow("gridRow");
		addStyleToRow(currentParent.replaceAll(" ", ""));

		addObjectToList(node);
		adjustCellsWidth(currentRow, this.body);
		super.fixHeader();
	}

	public void addNode(String parent, ImageResource parentIcon, T node,
			ImageResource nodeIcon) {
		ImageResource piconBk = null;
		ImageResource ciconBk = null;
		if (this.parentIcon != null) {
			piconBk = this.parentIcon;
		}

		if (this.nodeIcon != null) {
			ciconBk = this.nodeIcon;
		}

		this.parentIcon = parentIcon;
		this.nodeIcon = nodeIcon;

		addNode(parent, node);
		if (piconBk != null) {
			this.parentIcon = piconBk;
		}

		if (ciconBk != null) {
			this.nodeIcon = ciconBk;
		}
	}

	/**
	 * add Node to specific parent
	 * 
	 * @param parent
	 * @param node
	 */

	public void addNode(String parent, T node) {
		int rowIndex = getParentIndex(parent);
		if (rowIndex > -1) {
			this.currentParent = parent;
			currentRow = insertAfter(DOM.createTR(), rowIndex);
			addOrUpdateNode(node);
		} else {
			addParentWithChilds(parent, Arrays.asList(node));
		}

		if (isCollapse) {
			HideOrShowNodes(currentParent, getParentIndex(currentParent),
					isCollapse);
		}

		addStyleToRow("gridRow");
	}

	public void addNodes(T... nodes) {
		addNodes(Arrays.asList(nodes));
	}

	public void addNodes(List<T> nodes) {
		for (T t : nodes) {
			currentRow = this.getTableRowCount();
			addOrUpdateNode(t);
		}
		if (nodes.size() == 0) {
			// showEmptyMessage();
		}
	}

	/**
	 * Inserting an Element after Targeted Element, I used it for inserting Row
	 * after another Row
	 * 
	 * @param newEle
	 * @param row
	 * @return
	 */
	public int insertAfter(Element newEle, int row) {
		Element targetEle = this.rowFormatter.getElement(row);
		com.google.gwt.dom.client.Element parent = targetEle.getParentElement();
		parent.insertAfter(newEle, targetEle);
		return DOM.getChildIndex(DOM.getParent(newEle), newEle);
	}

	private int getParentIndex(String parent) {
		for (Object obj : this.objects) {
			if (obj instanceof String) {
				String name = (String) obj;
				if (name.equals(parent))
					return this.objects.indexOf(obj);
			}
		}
		return -1;
	}

	/**
	 * add Style to Row, row will the currentRow Only
	 * 
	 * @param clasName
	 */

	private void addStyleToRow(String clasName) {
		rowFormatter.addStyleName(currentRow, clasName);
	}

	public void addCell(String value, int row, int col) {
		if (col == 0) {
			// if (this.getWidget(row, col) == null) {
			TreeCell treeCell = new TreeCell(nodeIcon, value, false, row);
			this.setWidget(row, col, treeCell);
			// } else {
			// TreeCell cell = (TreeCell) this.getWidget(row, col);
			// cell.label.setText(value);
			// }
		} else {
			if (getColumnType(col, row) == COLUMN_TYPE_DECIMAL_TEXT) {
				this.cellFormatter.addStyleName(row, col, "gridDecimalCell");
				this.setText(row, col, value);
			} else
				this.setHTML(row, col, value);
		}
	}

	@Override
	protected abstract String[] getColumns();

	protected abstract Object getColumnValue(T obj, int index);

	protected abstract void onClick(T obj, int row, int index);

	protected int getColumnType(int col, int row) {
		return 0;
	}
	
	protected String getParentColumnValue(String parent, List<T> childNodes, int index) {
		return "";
	}

	protected void onDoubleClick(T obj, int row, int index) {
	}

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

	@Override
	protected abstract int getCellWidth(int index);

	/**
	 * Called this method when cell clicked in Row on This Grid
	 * 
	 * @param cell
	 */

	@Override
	public void cellClicked(int row, int col) {
		rowFormatter.removeStyleName(currentRow, "selected");
		rowFormatter.addStyleName(row, "selected");

		Object parent = this.objects.get(row);

		currentRow = row;
		if (parent instanceof String) {
			currentParent = (String) parent;
			parentNodeSelected(currentParent);
		} else {
			selectedObject = (T) objects.get(row);
			onClick(selectedObject, row, col);
		}
	}

	public void parentNodeSelected(String parent) {
		// subclass need to implement, if require
	}

	/**
	 * Called when any cell click On Header of This table
	 * 
	 * @param cell
	 */
	@Override
	public void headerCellClicked(int colIndex) {
		sortAllRows(colIndex, isDecending);
	}

	@Override
	public void removeAllRows() {
		this.objects.clear();
		super.removeAllRows();
	}

	public void deleteNode(T obj) {
		int index = this.objects.indexOf(obj);
		this.deleteNode(index);
	}

	public void deleteNode(int row) {
		if (row >= this.objects.size())
			return;
		currentRow = 0;
		selectedObject = null;
		this.objects.remove(row);
		this.removeRow(row);
		if (this.objects.size() == 0) {
			// showEmptyMessage();
		}
	}

	public void deleteNodesByParent(String parent) {
		for (T child : getChildNodesByParent(parent)) {
			this.deleteNode(child);
		}
	}

	public List<T> getChildNodesByParent(String name) {
		return getChildNodesByParent(name, this.objects);
	}

	public List<T> getChildNodesByParent(String name, List<?> objects) {
		List<T> childs = new ArrayList<T>();
		for (int i = objects.indexOf(name) + 1; i < objects.size(); i++) {
			Object obj = objects.get(i);
			if (!(obj instanceof String)) {
				childs.add((T) obj);
			} else {
				break;
			}
		}
		return childs;
	}

	/**
	 * delete parent Node, child nodes get deleted if parent node is deleted
	 * 
	 * @param name
	 */
	public void deleteParentNode(String name) {
		this.deleteNode(this.objects.indexOf(name));
		deleteNodesByParent(name);
	}

	public void removeAllNodes() {
		this.removeAllRows();
		this.objects.clear();
		// showEmptyMessage();
		selectedObject = null;
	}

	public T getSelectedNode() {
		return selectedObject;
	}

	public List<T> getSelectedNodes() {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < this.getTableRowCount(); i++) {

		}
		return list;
	}

	public List<T> getNodes() {
		List<T> list = new ArrayList<T>();
		for (Object obj : this.objects) {
			if (!(obj instanceof String)) {
				list.add((T) obj);
			}
		}
		return list;
	}

	public List<String> getParents() {
		return getParents(this.objects);
	}

	public List<String> getParents(List<?> objects) {
		List<String> list = new ArrayList<String>();
		for (Object obj : objects) {
			if (obj instanceof String) {
				list.add((String) obj);
			}
		}
		return list;
	}

	/**
	 * Sort Parents & its childs, recreated the row again
	 * 
	 * @param colIndex
	 * @param isDecending
	 */
	public void sortAllRows(int colIndex, final boolean isDecending) {
		// sort parents first
		List<String> parents = sort(getParents(), 0);

		List<Object> templist = new ArrayList<Object>();
		// Sorts childs by parent
		for (String pr : parents) {
			templist.add(pr);
			List<T> childs = sort(pr, colIndex, isDecending);
			if (!childs.isEmpty()) {
				templist.addAll(childs);
			}

		}
		recreateData(templist);
	}

	/**
	 * sort List
	 * 
	 * @param <S>
	 * @param list
	 * @param colIndex
	 * @return
	 */
	public <S extends Object> List<S> sort(List<S> list, final int colIndex) {
		Collections.sort(list, new Comparator<S>() {

			@Override
			public int compare(S o1, S o2) {
				int ret = 0;
				if (o1 instanceof String) {
					ret = ((String) o1).compareToIgnoreCase(((String) o2));
				} else {
					ret = sort((T) o1, (T) o2, colIndex);
					return isDecending ? (-1 * ret) : (ret);
				}
				return isDecending ? (-1 * ret) : (ret);
			}
		});
		return list;
	}

	/**
	 * Sort Colunm depends on Cell
	 * 
	 * @param index
	 * @param isDecending
	 */
	private List<T> sort(String parentName, final int colIndex,
			final boolean isDecending) {

		List<T> list = getChildNodesByParent(parentName);

		sort(list, colIndex);

		return list;
	}

	private void recreateData(List<Object> objs) {

		removeAllNodes();

		for (Object obj : getParents(objs)) {
			addParentWithChilds(obj.toString(),
					getChildNodesByParent(obj.toString(), objs));
		}
		this.isDecending = !isDecending;
	}

	public void updateNode(T obj) {
		if (this.objects.contains(obj) && !(obj instanceof String)) {
			this.addOrUpdateNode(obj);
		}
	}

	public void updateParentNode(String obj, int row) {
		if (this.objects.contains(obj) && (obj instanceof String)) {
			addOrUpdateParent(obj);
		}
	}

	private void addOrUpdateParent(String value) {

		if (this.getWidget(currentRow, 0) == null) {
			TreeCell treeCell = new TreeCell(nodeIcon, value, false, currentRow);
			this.setWidget(currentRow, 0, treeCell);
		} else {
			TreeCell cell = (TreeCell) this.getWidget(currentRow, 0);
			cell.label.setText(value);
		}
		for (int x = 1; x < nofCols; x++) {
			this.setText(currentRow, x, " ");
		}
	}

	/**
	 * show child nodes by default
	 * 
	 * @param show
	 */
	public void showChildNodesDefault(Boolean show) {
		this.isCollapse = !show;
	}

	/**
	 * TreeCell is class
	 * 
	 * @author kumar kasimala
	 * 
	 */
	public class TreeCell extends FlowPanel {
		Image image;
		Label label;

		public TreeCell(ImageResource url, final String text, Boolean isParent,
				final int row) {
			if (isParent) {
				final Image parentImg = new Image();
				// parentImg.setHeight("20px");
				// parentImg.setWidth("18px");
				changeImg(isCollapse, parentImg);

				parentImg.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						if (Arrays.asList(parentImg.getStyleName().split(" "))
								.contains("treeCollapse")) {
							HideOrShowNodes(text, getParentIndex(text), false);
							changeImg(false, parentImg);
						} else {
							HideOrShowNodes(text, getParentIndex(text), true);
							changeImg(true, parentImg);
						}

					}
				});
				this.add(parentImg);
				this.addStyleName("parentNode");
			} else {
				this.addStyleName("childNode");
			}
			image = new Image();
			// image.setHeight("15px");
			// image.setWidth("15px");
			image.setResource(url);
			image.addStyleName("treeCellIcon");

			label = new Label();
			label.setText(text);
			label.setTitle(text);
			label.addStyleName("treeLabel");

			this.add(image);
			this.add(label);
		}

		private void changeImg(Boolean isCollapse, Image img) {
			if (isCollapse) {
				img.setStyleName("treeCollapse");
				img.setResource(Accounter.getFinanceImages().treeCollapse());
			} else {
				img.setStyleName("treeExpend");
				img.setResource(Accounter.getFinanceImages().treeExpand());
			}
		}
	}

	@Override
	public void cellDoubleClicked(int row, int col) {
		Object obj = this.objects.get(row);
		if (!(obj instanceof String)) {
			onDoubleClick((T) obj, row, col);
		}

	}

	public void initParentAndChildIcons(ImageResource parentIconURL,
			ImageResource childIconURL) {
		parentIcon = parentIconURL;
		nodeIcon = childIconURL;

	}

	public void addParent(String name, ImageResource parentIcon) {
		ImageResource piconBk = this.parentIcon;
		this.parentIcon = parentIcon;
		addParent(name);
		this.parentIcon = piconBk;

	}
}
