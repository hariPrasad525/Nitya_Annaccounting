package com.nitya.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientPayTAX;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.ClientTransactionPayTAX;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TaxHistoryView extends BaseView<ClientTAXReturn> {

	SelectCombo optionsCombo;
	TAXHistoryGrid grid;
	ClientTAXReturn clientVATReturn;
	StyledPanel gridLayout;

	@Override
	public void init() {
		setMode(EditMode.EDIT);
		super.init();
		initListGrid();
		this.getElement().setId("TaxHistoryView");
		createControls();
	}

	private void createControls() {

		Label label = new Label();
		label.removeStyleName("gwt-style");
		// label.setWidth("100%");
		label.addStyleName("label-title");
		label.setText(messages.taxHistory());
		this.optionsCombo = new SelectCombo(messages.taxFillings());
		optionsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						filterList(selectItem);
						hideButtons();
					}

				});
		initComboItems();
		// this.grid.setWidth("100%");
		DynamicForm form2 = new DynamicForm("form2");

		form2.add(optionsCombo);
		StyledPanel mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(label);
		mainPanel.add(form2);
		mainPanel.add(gridLayout);
		int pageSize = getPageSize();
		if (pageSize != -1) {
			grid.addRangeChangeHandler2(new Handler() {

				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onPageChange(event.getNewRange().getStart(), event
							.getNewRange().getLength());
				}
			});
			SimplePager pager = new SimplePager(TextLocation.CENTER,
					(Resources) GWT.create(Resources.class), false,
					pageSize * 2, true);
			pager.setDisplay(grid);
			updateRecordsCount(0, 0, 0);
			mainPanel.add(pager);
		}

		// grid.getElement().getParentElement()
		// .addClassName("recounciliation_grid");
		this.add(mainPanel);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setVisible(false);
		if (saveAndNewButton != null) {
			saveAndNewButton.setVisible(!grid.getSelectedRecords().isEmpty());
			saveAndNewButton.setText(messages.payTax());
			this.addButton(saveAndNewButton);
		}

		this.addButton(deleteButton);
		deleteButton.setVisible(false);

		deleteButton.addDeleteHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				deleteButton.setVisible(false);
				final ClientTAXReturn selection = grid.getSelection();
				if (selection == null || !canDelete(selection)) {
					return;
				}
				String warning = messages.taxReturnDeleteWarning(
						DateUtills.getDateAsString(selection
								.getPeriodStartDate()),
						DateUtills.getDateAsString(selection.getPeriodEndDate()));
				Accounter.showWarning(warning, AccounterType.WARNING,
						new ErrorDialogHandler() {

							@Override
							public boolean onYesClick() {
								ClientTAXReturn selection2 = selection;
								grid.deleteRecord(selection2);
								Accounter.deleteObject(TaxHistoryView.this,
										selection2);
								return true;
							}

							@Override
							public boolean onNoClick() {
								return true;
							}

							@Override
							public boolean onCancelClick() {
								return false;
							}
						});
			}
		});
	}

	@Override
	public void initData() {
		setData(0, getPageSize());
	}

	private void updateRecordsCount(int start, int length, int total) {
		grid.updateRange(new Range(start, getPageSize()));
		grid.setRowCount(total, (start + length) == total);
	}

	protected void onPageChange(int start, int length) {
		setData(start, getPageSize());
	}

	private int getPageSize() {
		return 25;
	}

	protected void hideButtons() {
		if (deleteButton != null) {
			deleteButton.setVisible(false);
		}
		if (saveAndNewButton != null) {
			saveAndNewButton.setVisible(false);
		}

	}

	@Override
	public void onSave(boolean reopen) {

		ClientTAXReturn taxReturn = grid.getSelection();

		List<ClientTransactionPayTAX> payTaxEntriesList = new ArrayList<ClientTransactionPayTAX>();
		ClientTransactionPayTAX payTAXEntry = null;

		payTAXEntry = new ClientTransactionPayTAX();
		if (taxReturn != null) {
			if (taxReturn.getBalance() >= 0) {
				payTAXEntry.setTaxDue(taxReturn.getBalance());
			} else {
				payTAXEntry.setAmountToPay(taxReturn.getBalance());
			}
			payTAXEntry.setTaxAgency(taxReturn.getTAXAgency());
			payTAXEntry.setTAXReturn(taxReturn.getID());
			payTAXEntry.setFiledDate(taxReturn.getDate());
			payTaxEntriesList.add(payTAXEntry);

			ClientPayTAX clientPayTAX = new ClientPayTAX();
			clientPayTAX.setTransactionPayTax(payTaxEntriesList);

			new PayTAXAction().run(clientPayTAX, false);
		}
	}

	private void initComboItems() {
		List<String> options = new ArrayList<String>();
		options.add(new String(messages.all()));
		options.add(new String(messages.paid()));
		options.add(new String(messages.unPaid()));
		optionsCombo.initCombo(options);
		optionsCombo.setSelectedItem(0);

	}

	private void initListGrid() {

		gridLayout = new StyledPanel("gridLayout");
		grid = new TAXHistoryGrid(this, false);
		grid.setCanEdit(!isInViewMode());
		grid.init();
		grid.setEnabled(!isInViewMode());

		gridLayout.add(grid);

	}

	private void setData(int start, int lenght) {
		grid.clear();
		grid.addLoadingImagePanel();
		rpcGetService.getAllTAXReturns(start, lenght, getViewType(),
				new AccounterAsyncCallback<PaginationList<ClientTAXReturn>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							PaginationList<ClientTAXReturn> result) {
						grid.removeLoadingImage();
						grid.removeAllRecords();
						if (result.isEmpty()) {
							updateRecordsCount(result.getStart(),
									grid.getTableRowCount(),
									result.getTotalCount());
							grid.addEmptyMessage(messages.noRecordsToShow());
							return;
						}
						grid.setRecords(result);
						Window.scrollTo(0, 0);
						updateRecordsCount(result.getStart(),
								grid.getTableRowCount(), result.getTotalCount());
						saveAndNewButton.setVisible(!grid.getSelectedRecords()
								.isEmpty());
						grid.sort(10, false);

					}
				});

	}

	private int getViewType() {
		String selectedValue = optionsCombo.getSelectedValue();
		if (selectedValue == null || selectedValue.isEmpty()) {
			return 0;
		}
		if (selectedValue.equals(messages.paid())) {
			return 1;
		} else if (selectedValue.equals(messages.unPaid())) {
			return 2;
		} else {
			return 0;
		}
	}

	private void filterList(String selectItem) {
		onPageChange(0, getPageSize());
	}

	@Override
	protected String getViewTitle() {
		return messages.taxHistory();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(messages.filedTAXReturnHasBeenPaid());
		} else {
			Accounter.showError(AccounterExceptions.getErrorString(errorCode));
		}
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		grid.deleteRecord(grid.getSelectedRecordIndex());
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
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
		return true;
	}

	public void taxReturnSelected(ClientTAXReturn obj) {
		if (obj == null) {
			deleteButton.setVisible(false);
			return;
		}
		if (obj.getBalance() > 0) {
			saveAndNewButton.setVisible(true);
		} else {
			saveAndNewButton.setVisible(false);
		}
		deleteButton.setVisible(canDelete(obj));
	}

	private boolean canDelete(ClientTAXReturn taxReturn) {
		ClientTAXAgency taxAgency = getCompany().getTaxAgency(
				taxReturn.getTAXAgency());
		return taxAgency.getLastTAXReturnDate() == null
				|| taxReturn.getPeriodEndDate() == taxAgency
						.getLastTAXReturnDate().getDate();
	}

	@Override
	public ClientTAXReturn saveView() {
		ClientTAXReturn taxReturn = new ClientTAXReturn();
		taxReturn.setNumber(optionsCombo.getSelectedValue());
		return taxReturn;

	}

	@Override
	public void restoreView(ClientTAXReturn viewDate) {
		optionsCombo.setSelected(viewDate.getNumber());
		filterList(viewDate.getNumber());
		hideButtons();
	}
}
