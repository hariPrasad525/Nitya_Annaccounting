package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.Range;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class UsersActivityListView extends BaseView implements IPrintableView {

	private Label titleItem;
	private DateField fromdate, toDate;
	private StyledPanel mainPanel;
	private UsersActivityList activityList;
	private Button updateButton, customizeButton;
	private long value;
	private ActivityCustomizationDialog customizationDialog;

	public UsersActivityListView() {
		this.getElement().setId("UsersActivityListView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		mainPanel = new StyledPanel("mainPanel");

		titleItem = new Label(messages.usersActivityLogTitle());
		titleItem.setStyleName("label-title");

		fromdate = new DateField(messages.fromDate(), "fromdate");
		toDate = new DateField(messages.endDate(), "toDate");
		fromdate.setEnteredDate(new ClientFinanceDate());
		toDate.setEnteredDate(new ClientFinanceDate());

		customizeButton = new Button(messages.customize());
		customizeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				customiseActivityLog();
			}
		});
		updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshActivityList();
			}
		});

		activityList = new UsersActivityList(fromdate.getValue(),
				toDate.getValue(), value) {
			@Override
			public void refreshData() {
				refreshActivityList();
			}
		};
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				pagerResources, false, 0, true);
		pager.setDisplay(activityList);
		pager.setStyleName("pager-alignment");
		activityList.addColumnSortHandler(new Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				Column<?, ?> column = event.getColumn();
				int columnIndex = activityList
						.getColumnIndex((Column<ClientActivity, ?>) column);
				boolean isAscending = event.isSortAscending();
				activityList.sortRowData(columnIndex, isAscending);

			}
		});

		StyledPanel activityPanel = new StyledPanel("useractivitylog_parent");
		activityPanel.add(activityList);

		StyledPanel panel = new StyledPanel("panel");
		panel.add(fromdate);
		panel.add(toDate);
		panel.add(customizeButton);
		panel.add(updateButton);
		panel.addStyleName("user_activity_dateform");

		mainPanel.add(titleItem);
		mainPanel.add(panel);
		mainPanel.add(activityPanel);
		activityList.setStyleName("user_activity_log");
		mainPanel.add(pager);
		add(mainPanel);
	}

	protected void customiseActivityLog() {
		customizationDialog = new ActivityCustomizationDialog(
				messages.customize());
		ViewManager.getInstance().showDialog(customizationDialog);
		customizationDialog.showRelativeTo(customizeButton);
		customizationDialog.setCallback(new ActionCallback<Long>() {

			@Override
			public void actionResult(Long result) {
				value = result;
				refreshActivityByCustomiseValues(value);
				customizationDialog.setCustomiseValue(value);
			}
		});

	}

	private void refreshActivityByCustomiseValues(long value) {
		ClientFinanceDate startDate = fromdate.getEnteredDate();
		ClientFinanceDate endDate = toDate.getEnteredDate();
		activityList.setFromDate(startDate);
		activityList.setEndDate(endDate);
		activityList.setCustomiseValue(value);
		activityList.setVisibleRangeAndClearData(new Range(0, 50), true);
	}

	private void refreshActivityList() {
		ClientFinanceDate startDate = fromdate.getEnteredDate();
		ClientFinanceDate endDate = toDate.getEnteredDate();
		activityList.setFromDate(startDate);
		activityList.setEndDate(endDate);
		activityList.setVisibleRangeAndClearData(new Range(0, 50), true);
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	protected void createButtons() {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return messages.usersActivity();
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		Accounter.createExportCSVService().getUsersActivityLogExportCsv(
				activityList.getFromDate(), activityList.getEndDate(),
				activityList.getCustomiseValue(), new AsyncCallback<String>() {

					@Override
					public void onSuccess(String id) {
						UIUtils.downloadFileFromTemp(
								messages.usersActivityLogTitle() + ".csv", id);
					}

					@Override
					public void onFailure(Throwable arg0) {
						arg0.printStackTrace();
					}
				});
	}
}
