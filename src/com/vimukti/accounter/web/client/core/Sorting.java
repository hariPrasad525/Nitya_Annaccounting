package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Sorting<T> {

	ISorting<T> viewTosort;

	public boolean isDecending;

	public Sorting(ISorting<T> viewtoSort) {
		this.viewTosort = viewtoSort;
	}

	public List<T> sort(List<T> unOrderList, final int col) {

		Collections.sort(unOrderList, new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				int ret = viewTosort.sort(o1, o2, col);
				return isDecending ? (-1 * ret) : (ret);
			}
		});
		return unOrderList;

	}

	public List<T> sort(List<T> unOrderList, int col, boolean isHirachy) {
		List<T> orderList = new ArrayList<T>();
		sort(unOrderList, orderList, null, col);
		return orderList;
	}

	private void sort(List<T> unOrderList, List<T> orderList, T parent, int col) {
		List<T> childs = getChildsByParent(unOrderList, parent, col);

		if (childs.isEmpty())
			return;
		if (parent == null) {
			orderList.addAll(0, childs);
		} else {
			orderList.addAll(orderList.indexOf(parent), childs);
		}

		for (T child : childs) {
			sort(unOrderList, orderList, child, col);
		}

	}

	private List<T> getChildsByParent(List<T> unOrderList, T parent,
			final int col) {

		List<T> childs = new ArrayList<T>();

		Iterator<T> iterator = unOrderList.iterator();

		while (iterator.hasNext()) {
			T obj = viewTosort.getObject(parent, iterator.next());
			if (obj != null) {
				childs.add(obj);
				iterator.remove();
			}
		}

		return childs;
	}

}
