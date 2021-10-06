package com.vimukti.accounter.utils;

public interface ICSVExportRunner<T> {

	String[] getColumns();

	String getColumnValue(T obj, int index);
}
