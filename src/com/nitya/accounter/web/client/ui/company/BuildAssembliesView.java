package com.nitya.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.nitya.accounter.web.client.core.ClientBuildAssembly;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.BuildAssemblyAction;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TransactionsListView;

public class BuildAssembliesView extends
		TransactionsListView<ClientBuildAssembly> implements IPrintableView {

	private PaginationList<ClientBuildAssembly> listOfBuildAssemblies;
	private int viewId;

	public BuildAssembliesView() {
		super(messages.all());
		isDeleteDisable = true;
	}

	@Override
	public void updateInGrid(ClientBuildAssembly objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new BuildAssembliesGrid();
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.buildAssemblies();
	}

	@Override
	protected Action getAddNewAction() {
		return new BuildAssemblyAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.buildAssembly();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		onPageChange(0, getPageSize());
	}

	@Override
	protected void filterList(String selectedValue) {
		onPageChange(start, getPageSize());
	}

	@Override
	protected String getViewTitle() {
		return messages.buildAssemblies();
	}

	@Override
	protected void onPageChange(int start, int length) {
		setViewId(checkViewType(getViewType()));
		Accounter.createHomeService().getBuildAssembliesList(
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, getViewId(), this);
	}

	@Override
	public void onSuccess(PaginationList<ClientBuildAssembly> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), result.size(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.removeLoadingImage();
		listOfBuildAssemblies = result;
		viewSelect.setComboItem(getViewType());
		grid.setRecords(listOfBuildAssemblies);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), result.size(),
				result.getTotalCount());
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		// listOfTypes.add(messages.open());
		// listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		if (getViewType() != null && !getViewType().equals("")) {
			viewSelect.setComboItem(getViewType());
		}
		return listOfTypes;
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return false;
	}

	private int checkViewType(String view) {
		// if (getViewType().equalsIgnoreCase(messages.open())) {
		// setViewId(VIEW_OPEN);
		// } else if (getViewType().equalsIgnoreCase(messages.voided())) {
		// setViewId(VIEW_VOIDED);
		// } else
		if (getViewType().equalsIgnoreCase(messages.all())) {
			setViewId(VIEW_ALL);
		}

		return getViewId();
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}

	@Override
	public void exportToCsv() {
		setViewId(checkViewType(getViewType()));
		Accounter.createExportCSVService().getBuildAssembliesExportCsv(
				getStartDate().getDate(), getEndDate().getDate(), viewId,
				getExportCSVCallback(messages.buildAssemblies()));
	}
}
