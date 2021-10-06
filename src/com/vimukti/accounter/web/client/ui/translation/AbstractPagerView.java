package com.vimukti.accounter.web.client.ui.translation;

import java.util.List;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;

public abstract class AbstractPagerView<T> extends AbstractBaseView<T>
		implements PagerListener {
	protected Pager pager;
	public List<T> t;

	public AbstractPagerView() {
		super();
		createControls();
	}

	protected abstract void createControls();

	protected void updateData(List<T> t) {
		this.t = t;
		pager.updateRangeData();
	}

	public void refreshPager() {
		if (pager != null) {
			pager.refreshPager();
		}
	}

	public int getDataSize() {
		if (t != null) {
			return t.size();
		} else {
			return 0;
		}
	}

	@Override
	public void updateListData() {

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasMoreRecords() {
		return pager.hasMoreRecords();
	}

	@Override
	public void setMoreRecords(boolean b) {
		pager.setMoreRecords(b);
	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
