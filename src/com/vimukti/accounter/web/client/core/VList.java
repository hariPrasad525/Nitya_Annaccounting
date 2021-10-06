package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VList<E> extends ArrayList<E> {
//	Logger logger;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private List<ListFilter<E>> filters;
	private ArrayList<ListListener<E>> listeners;
	private Map<ListFilter<E>, ListListener<E>> filterListeners;

	public VList() {
		super();
//		logger = Logger.getLogger("ArrayList");
		// filters = new ArrayList<ListFilter<E>>();
		listeners = new ArrayList<ListListener<E>>();
		filterListeners = new HashMap<ListFilter<E>, ListListener<E>>();
	}

	public VList(Collection<E> collection) {
		this();
		addAll(collection);
	}

	// public void addFilter(ListFilter<E> listFilter) {
	// filters.add(listFilter);
	//
	// }

	public void addListener(ListListener<E> listListener) {
		listeners.add(listListener);
	}

	public void removeListener(ListListener<E> listListener) {
		listeners.remove(listListener);
	}

	// public void removeFilter(ListFilter<E> listFilter) {
	// filters.remove(listFilter);
	// }

	public void addListener(ListFilter<E> listFilter,
			ListListener<E> listListener) {
		filterListeners.put(listFilter, listListener);
	}

	public ArrayList<E> filter(final ListFilter<E> listFilter) {
		final ArrayList<E> filteredList = new ArrayList<E>();
		for (E e : this) {
			if (listFilter.filter(e)) {
				filteredList.add(e);
			}
		}

		ListListener<E> thisListListener = new ListListener<E>() {

			@Override
			public void onAdd(E e) {
				filteredList.add(e);
			}

			@Override
			public void onRemove(final Object e) {
				filteredList.remove(e);
			}
		};
		this.addListener(listFilter, thisListListener);
		return filteredList;
	}

	public ArrayList<E> and(final VList<E> list) {
		final ArrayList<E> filteredList = new ArrayList<E>();
		for (E e : this) {
			if (list.contains(e)) {
				filteredList.add(e);
			}
		}
		list.addListener(new ListListener<E>() {

			@Override
			public void onAdd(E e) {
				if (contains(e)) {
					filteredList.add(e);
				}
			}

			@Override
			public void onRemove(Object e) {
				filteredList.remove(e);
			}
		});

		this.addListener(new ListListener<E>() {

			@Override
			public void onAdd(E e) {
				if (list.contains(e)) {
					filteredList.add(e);
				}
			}

			@Override
			public void onRemove(Object e) {
				filteredList.remove(e);
			}
		});
		return filteredList;
	}

	public ArrayList<E> or(final VList<E> list) {
		final ArrayList<E> filteredList = new ArrayList<E>();
		for (E e : this) {
			filteredList.add(e);
		}

		for (E e : filteredList) {
			if (!list.contains(e)) {
				add(e);
			}
		}
		list.addListener(new ListListener<E>() {

			@Override
			public void onAdd(E e) {
				filteredList.add(e);
			}

			@Override
			public void onRemove(Object e) {
				if (!contains(e)) {
					filteredList.remove(e);
				}
			}
		});
		this.addListener(new ListListener<E>() {

			@Override
			public void onAdd(E e) {
				filteredList.add(e);
			}

			@Override
			public void onRemove(Object e) {
				if (!list.contains(e)) {
					filteredList.remove(e);
				}
			}
		});
		return filteredList;
	}

	public boolean add(E e) {
		// for (ListFilter<E> filter : filters) {
		// if (!filter.filter(e)) {
		// return false;
		// }
		// }
		super.add(e);

		for (ListListener<E> listener : listeners) {
			listener.onAdd(e);
		}

		Set<ListFilter<E>> keySet = filterListeners.keySet();
		for (ListFilter<E> key : keySet) {
			if (key.filter(e)) {
				ListListener<E> listener = filterListeners.get(key);
				listener.onAdd(e);
			}
		}
		return true;
	}

	public void add(int index, E e) {
		addInternal(index, e, null);
	}

	private void addInternal(int index, E e, ListListener<E> except) {
		// for (ListFilter<E> filter : filters) {
		// if (!filter.filter(e)) {
		// return;
		// }
		// }
		super.add(index, e);

		for (ListListener<E> listener : listeners) {
			if (listener == except) {
				continue;
			}
			listener.onAdd(e);
		}

		Set<ListFilter<E>> keySet = filterListeners.keySet();
		for (ListFilter<E> key : keySet) {
			if (key.filter(e)) {
				ListListener<E> listener = filterListeners.get(key);
				if (listener == except) {
					continue;
				}
				listener.onAdd(e);
			}
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		Iterator<? extends E> iterator = collection.iterator();
		while (iterator.hasNext()) {
			E e = iterator.next();
			this.add(index++, e);
		}
		return collection.size() > 0 ? true : false;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		Iterator<? extends E> iterator = collection.iterator();
		while (iterator.hasNext()) {
			E e = iterator.next();
			this.add(e);
		}
		return collection.size() > 0 ? true : false;
	}

	@Override
	public E remove(int index) {
		E e = get(index);
		super.remove(e);
//		logger.log(Level.SEVERE, "removing Object" + e);
		boolean result = super.remove(e);
		for (ListListener<E> listener : listeners) {

			listener.onRemove(e);
		}
		Set<ListFilter<E>> keySet = filterListeners.keySet();
		for (ListFilter<E> key : keySet) {
			if (key.filter(e)) {
				ListListener<E> listener = filterListeners.get(key);
				listener.onRemove(e);
			}
		}
		return e;
	}

	public boolean remove(Object e) {
		int index=super.indexOf(e);
		if(index==-1){
			return false;
		}
		super.remove(index);
		return true;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		for (int i = fromIndex; i < toIndex; i++) {
			remove(i);
		}
	}

	@Override
	public E set(int index, E e) {
		E remove = remove(index);
		add(index, e);
		return remove;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		Iterator<?> iterator = collection.iterator();
		boolean result = false;
		while (iterator.hasNext()) {
			Object e = iterator.next();
			result = result || remove(e);
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		Iterator<?> iterator = this.iterator();
		boolean result = false;
		while (iterator.hasNext()) {
			Object e = iterator.next();
			if (!collection.contains(e)) {
				result = result || remove(e);
			}
		}
		return result;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		for (E e : this) {
			remove(e);
		}
	}

}
