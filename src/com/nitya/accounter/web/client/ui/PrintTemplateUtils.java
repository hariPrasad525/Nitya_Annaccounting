/**
 * 
 */
package com.nitya.accounter.web.client.ui;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.FlexTable;

/**
 * @author Murali.A T This class contains methods to return the components for
 *         print template in different styles
 */
public class PrintTemplateUtils {

	public FlexTable getWidget(int numRows, int noCols,
			Map<String, String> data, boolean isHeadersRequired) {
		FlexTable tb = new FlexTable();
		tb.setStyleName("gridHeader");
		Set<String> headerData = data.keySet();
		Object[] headArray = headerData.toArray();
		if (isHeadersRequired) {
			for (int c = 0; c < noCols; c++) {
				tb.setHTML(0, c,
						"<center><font  size=\"1\" style=\"font-weight:600;\"> &nbsp;"
								+ headArray[c].toString() + "</font></center>");
				tb.getCellFormatter()
						.setStyleName(0, c, "gridHeaderBackGround");
			}
		}
		for (int r = 1; r < numRows; r++) {
			int c = 0;
			for (String key : data.keySet()) {
				tb.setHTML(r, c,
						"<center>" + data.get(key) != null ? data.get(key)
								: "&nbsp;" + "</center>");
				c++;
			}
		}
		return tb;
	}

	public FlexTable getGridWidget(
			Map<List<String>, Map<Integer, List<String>>> gridData,
			boolean isBorderRequire) {
		FlexTable tb = new FlexTable();
		if (isBorderRequire)
			tb.setStyleName("gridHeader");
		Set<List<String>> headers = gridData.keySet();
		Object[] hds = headers.toArray();
		List<String> header = (List<String>) hds[0];
		int c = 0;
		for (String colName : header) {

			tb.setHTML(0, c,
					"<center><font  size=\"1\" style=\"font-weight:600;\"> "
							+ colName + "</font></center>");
			// tb.getCellFormatter().setHeight(0, c, "10px");
			tb.getCellFormatter().setStyleName(0, c, "gridHeaderBackGround");

			c++;
			// tb.getCellFormatter().setWidth(0, 0, "33%");
		}
		Map<Integer, List<String>> rowData = new LinkedHashMap<Integer, List<String>>();
		for (List<String> key : headers) {
			rowData = gridData.get(key);
		}

		for (Integer rowNum : rowData.keySet()) {
			int col = 0;
			for (String colValue : rowData.get(rowNum)) {
				tb.setHTML(rowNum.intValue() + 1, col,
						"<center>" + colValue != null ? colValue : "&nbsp;"
								+ "</center>");
				// tb.getCellFormatter().setHeight(rowNum.intValue() + 1, col,
				// "10px");
				col++;
				// tb.getCellFormatter().setWidth(rowNum.intValue() + 1, 0,
				// "33%");
			}
		}
		return tb;
	}

	public FlexTable getGridNBy1(int row, int numCols, Map<String, String> data) {
		FlexTable tb = new FlexTable();
		tb.setStyleName("gridHeader");
		int r = 0;
		int col = 0;
		for (String key : data.keySet()) {
			tb.setHTML(r++, col++, data.get(key) != null ? data.get(key)
					: "&nbsp;");
			if (col == numCols) {
				col = 0;
			}
		}
		return tb;
	}

	public FlexTable getFooterWidget(
			Map<Integer, Map<String, String>> gridData, List<String> headres) {
		FlexTable tb = new FlexTable();
		int c = 0;
		for (String headerName : headres) {
			tb.setHTML(0, c++, "<strong>" + headerName + "</strong>");
		}

		for (Integer rowNum : gridData.keySet()) {
			int col = 0;
			Map<String, String> colsMap = gridData.get(rowNum);
			for (String colValue : colsMap.keySet()) {
				tb.setText(rowNum + 1, col++, colValue);
				tb.setText(rowNum + 1, col++, colsMap.get(colValue));
			}
		}
		return tb;
	}

	public FlexTable getWidget1(int numRows, int noCols,
			Map<String, String> data, boolean isHeadersRequired,
			List<String> removeHeaderBackground) {
		FlexTable tb = new FlexTable();
		tb.setStyleName("gridHeader");
		Set<String> headerData = data.keySet();
		Object[] headArray = headerData.toArray();
		if (isHeadersRequired) {
			for (int r = 0; r < numRows; r++) {
				tb.setHTML(r, 0,
						"<font  size=\"1\" style=\"font-weight:600;\"> &nbsp;"
								+ headArray[r].toString() + "</font>");
				if (!removeHeaderBackground.contains(headArray[r].toString()))
					tb.getCellFormatter().setStyleName(r, 0,
							"gridHeaderBackGround");

			}
		}
		for (int c = 1; c < noCols; c++) {
			int r = 0;
			for (String key : data.keySet()) {
				tb.setHTML(r, c,
						"<center>" + data.get(key) != null ? data.get(key)
								: "&nbsp;" + "</center>");
				r++;
			}
		}
		return tb;
	}

	public FlexTable getThinBorderWidget(int numRows, int noCols,
			Map<String, String> data, boolean isHeadersRequired) {
		FlexTable tb = new FlexTable();
		tb.setStyleName("thinBorder");
		tb.setBorderWidth(1);
		Set<String> headerData = data.keySet();
		Object[] headArray = headerData.toArray();

		if (isHeadersRequired) {
			for (int c = 0; c < noCols; c++) {
				tb.setHTML(0, c,
						"<center><font  size=\"1\">" + headArray[c].toString()
								+ "</font></center>");
			}
		}

		for (int r = 1; r < numRows; r++) {
			int c = 0;
			for (String key : data.keySet()) {
				tb.setHTML(r, c,
						"<center>" + data.get(key) != null ? data.get(key)
								: "&nbsp;" + "</center>");
				c++;
			}
		}

		if (numRows == 1 && noCols == 0) {
			tb.setHTML(0, 0,
					"<center><font  size=\"1\">" + headArray[0].toString()
							+ "</font></center>");
		}
		return tb;
	}
}
