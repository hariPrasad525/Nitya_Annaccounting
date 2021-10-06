package com.nitya.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientFixedAsset;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;

/**
 * 
 * This Class Serves as the Base Class for all List Views, i.e CustomerList,
 * VendorList, InvoiceList
 * 
 * @author Fernandez
 * 
 * @param <T>
 */

public abstract class BaseListView<T> extends AbstractBaseView<T> implements
		IAccounterList<T>, AsyncCallback<PaginationList<T>>,
		ISavableView<HashMap<String, Object>> {
	protected BaseListGrid grid;
	protected static AccounterMessages messages = Global.get().messages();
	protected int start;
	protected boolean isActive = true;
	public static final int DEFAULT_PAGE_SIZE = Accounter.isWin8App() ? 14 : 50;

	public BaseListView() {
		this.getElement().addClassName("listView");
	}

	protected List<String> getViewSelectTypes() {
		return null;
	}

	public BaseListGrid getGrid() {
		return grid;
	}

	protected Label totalLabel;

	protected List<T> records;

	protected double total;
	protected boolean isDeleteDisable;

	protected List<T> initialRecords = new ArrayList<T>();

	protected StyledPanel gridLayout;

	protected boolean isViewSelectRequired = true;

	protected int cmd;

	private String dateRange;

	protected SelectCombo viewSelect;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		initListCallback();
		super.initData();
	}

	@Override
	public void deleteFromGrid(T objectToBeRemoved) {

		// this.grid.deleteRecord((IsSerializable) objectToBeRemoved);
		if (totalLabel != null) {
			updateTotal(objectToBeRemoved, false);
		}

	}

	@Override
	public final boolean isAListView() {
		return true;
	}

	protected void createControls() {
		StyledPanel hlay = new StyledPanel("hlay");
		// hlay.setWidth("100%");

		DynamicForm form = new DynamicForm("form");

		createListForm(form);
		hlay.add(form);
		StyledPanel vlayTop = new StyledPanel("vlayTop");
		StyledPanel hlayTop = new StyledPanel("hlayTop");
		vlayTop.add(hlayTop);
		if (isViewSelectRequired)
			hlayTop.add(hlay);

		Label lab1 = new Label(getListViewHeading());
		lab1.addStyleName("label-title-list");
		gridLayout = new StyledPanel("gridLayout");
		initGrid();

		StyledPanel totalLayout = getTotalLayout(grid);
		grid.setView(this);
		gridLayout.add(grid);

		StyledPanel mainVLay = new StyledPanel("mainVLay");

		if (totalLayout != null) {
			if (isTransactionListView()) {
				mainVLay.add(vlayTop);
				AccounterDOM.setParentElementHeight(vlayTop.getElement(), 10);
			} else {
				mainVLay.add(hlayTop);
				AccounterDOM.setParentElementHeight(hlayTop.getElement(), 5);
			}
			mainVLay.add(lab1);
			mainVLay.add(gridLayout);
			mainVLay.add(totalLayout);

		} else {
			if (isTransactionListView()) {
				mainVLay.add(vlayTop);
				AccounterDOM.setParentElementHeight(vlayTop.getElement(), 10);
			} else {
				mainVLay.add(hlayTop);
				AccounterDOM.setParentElementHeight(hlayTop.getElement(), 5);
			}
			mainVLay.add(lab1);
			AccounterDOM.setParentElementHeight(lab1.getElement(), 5);
			mainVLay.add(gridLayout);
		}
		int pageSize = getPageSize();
		if (pageSize != -1) {
			grid.addRangeChangeHandler2(new Handler() {

				@Override
				public void onRangeChange(RangeChangeEvent event) {
					start = event.getNewRange().getStart();
					showLoading();
					onPageChange(start, event.getNewRange().getLength());
				}
			});
			SimplePager pager = new SimplePager(TextLocation.CENTER,
					(Resources) GWT.create(Resources.class), false,
					pageSize * 2, true);
			pager.setDisplay(grid);
			updateRecordsCount(0, 0, 0);
			mainVLay.add(pager);
		}
		add(mainVLay);

	}

	/**
	 * Called to create the default form used to update/filter the content on
	 * the list
	 * 
	 * @param form
	 */
	protected void createListForm(DynamicForm form) {
		viewSelect = getSelectItem();
		if (viewSelect != null) {
			form.add(viewSelect);
		}
	}

	protected void setDateRange(String selectedValue) {
		this.dateRange = selectedValue;
	}

	protected String getDateRange() {
		return dateRange;
	}

	protected int getPageSize() {
		return -1;
	}

	public void updateRecordsCount(int start, int length, int total) {
		grid.updateRange(new Range(start, getPageSize()));
		grid.setRowCount(total, (start + length) == total);

	}

	protected void onPageChange(int start, int length) {
		// TODO Auto-generated method stub

	}

	protected boolean isTransactionListView() {
		return false;
	}

	public void customManage() {

	}

	/**
	 * Active/In-Active filter used by most of the lists
	 * 
	 * @return
	 */
	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(messages.currentView());
		viewSelect.setComboItem(messages.active());
		List<String> typeList = new ArrayList<String>();
		typeList.add(messages.active());
		typeList.add(messages.inActive());
		viewSelect.initCombo(typeList);
		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							if (viewSelect.getSelectedValue().toString()
									.equalsIgnoreCase(messages.active()))
								filterList(true);
							else
								filterList(false);
						}
					}
				});
		return viewSelect;
	}

	protected SelectCombo getDateRangeSelectItem() {
		return null;
	}

	protected void filterList(boolean isActive) {
	}

	protected void changeBudgetGrid(int numberSelected) {
	}

	protected abstract void initGrid();

	protected StyledPanel getTotalLayout(BaseListGrid grid) {

		return null;
	}

	protected abstract String getListViewHeading();

	@Override
	public void addToGrid(T objectToBeAdded) {
		grid.addData(objectToBeAdded);
		if (totalLabel != null) {
			updateTotal(objectToBeAdded, true);
		}

	}

	protected void updateTotal(T t, boolean add) {

	}

	protected abstract Action getAddNewAction();

	protected abstract String getAddNewLabelString();

	public void initListCallback() {
		showLoading();
		if (getPageSize() != -1) {
			start = 0;
			grid.setVisibleRange(start, getPageSize());
		}
	}

	protected AccounterAsyncCallback<Boolean> getGeneralizedDeleteCallBack(
			final T object) {

		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				deleteFailed(caught);
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result != null && result) {
					// Accounter.showInformation("Deleted Successfully");
					deleteFromGrid(object);
				} else
					deleteFailed(null);

			}
		};

		return callback;

	}

	@Override
	public void onFailure(Throwable exception) {
		if (exception instanceof AccounterException) {
			Accounter.showError(messages.failedRequest());
			return;
		}
		Accounter.showMessage(messages.sessionExpired());
	}

	@Override
	public void onSuccess(PaginationList<T> result) {
		grid.removeLoadingImage();
		if (result.isEmpty()) {
			grid.removeAllRecords();
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		} else {
			grid.removeLoadingImage();
			initialRecords = result;
			this.records = result;

			if (filterBeforeShow()) {
				filterList(true);
			} else {
				grid.setRecords(result);
			}
		}
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	protected boolean filterBeforeShow() {
		return false;
	}

	public List<ClientFixedAsset> getAssetsByType(int type,
			List<ClientFixedAsset> records) {
		List<ClientFixedAsset> pendingItems = new ArrayList<ClientFixedAsset>();
		List<ClientFixedAsset> registeredItems = new ArrayList<ClientFixedAsset>();
		List<ClientFixedAsset> soldDispzdItems = new ArrayList<ClientFixedAsset>();
		for (ClientFixedAsset asset : records) {
			if (asset.getStatus() == ClientFixedAsset.STATUS_REGISTERED) {
				registeredItems.add(asset);
			} else if (asset.getStatus() == ClientFixedAsset.STATUS_SOLD_OR_DISPOSED) {
				soldDispzdItems.add(asset);
			} else
				pendingItems.add(asset);
		}
		switch (type) {
		case 1:
			return pendingItems;
		case 2:
			return registeredItems;

		case 3:
			return soldDispzdItems;
		default:
			return pendingItems;
		}
	}

	protected <A extends IAccounterCore> void deleteObject(final A core,
			AccounterCoreType coreType) {
		Accounter.deleteObject(this, core);
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		if (getCallback() != null)
			getCallback().onResultSuccess(result);
	}

	// public void disableFilter() {
	// if (this.viewSelect != null) {
	// this.viewSelect.setEnabled(false);
	// }
	// }

	@Override
	public void restoreView(HashMap<String, Object> viewDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, Object> saveView() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Called by subclasses while downloading the csv file
	 * 
	 * @param filename
	 * @return
	 */
	public AsyncCallback<String> getExportCSVCallback(final String filename) {
		return new AsyncCallback<String>() {

			@Override
			public void onSuccess(String id) {
				UIUtils.downloadFileFromTemp(filename + ".csv", id);

			}

			@Override
			public void onFailure(Throwable arg0) {
				arg0.printStackTrace();

			}
		};
	}

	protected void restoreView(String currentView, String dateRange) {

	}

	public void showLoading() {
		grid.removeAllRecords();
		grid.addLoadingImagePanel();
	}
}
