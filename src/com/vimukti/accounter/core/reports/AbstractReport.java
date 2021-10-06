package com.vimukti.accounter.core.reports;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public abstract class AbstractReport<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Session session;

	public AbstractReport(Session session) {
		super();
		this.session = session;
	}

	protected void run() {
		Query q = execute();
		List list = q.list();
		for (Object obj : list) {
			T row = process(obj);
			if (row != null) {
				add(row);
			}
		}
	}

	protected abstract T process(Object obj);

	protected abstract Query execute();

}
