package com.nitya.accounter.web.client.ui.translation;

public interface PagerListener {
	boolean hasMoreRecords();

	void setMoreRecords(boolean b);

	void updateListData();

	int getDataSize();
}
