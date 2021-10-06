package com.nitya.accounter.web.client.ui.search;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.SearchInput;
import com.nitya.accounter.web.client.core.SearchResultlist;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.forms.ClickableSafeHtmlCell;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class SearchTable extends CellTable<SearchResultlist> {

	private AsyncDataProvider<SearchResultlist> listDataProvider;
	private AccounterMessages messages = Global.get().messages();
	private SearchInput input;

	public SearchTable(SearchInput input) {
		this.input = input;
		createControls();
	}

	private void createControls() {

		listDataProvider = new AsyncDataProvider<SearchResultlist>() {

			@Override
			protected void onRangeChanged(HasData<SearchResultlist> display) {
				final int start = display.getVisibleRange().getStart();
				int length = display.getVisibleRange().getLength();
				Accounter.createHomeService().getSearchResultByInput(input,
						start, length,
						new AsyncCallback<PaginationList<SearchResultlist>>() {

							@Override
							public void onSuccess(
									PaginationList<SearchResultlist> result) {
								if (result != null && result.size() != 0
										&& !result.isEmpty()) {
									enableGrid();
									updateRowData(start, result);
									setRowCount(result.getTotalCount());
								} else {
									disableGrid();
									setVisible(false);
								}

							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});

			}
		};
		setPageSize(10);
		listDataProvider.addDataDisplay(this);

		// this.setWidth("100%", true);
		this.setStyleName("search_result_table");
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		initTableColumns();
	}

	public void disableGrid() {

	}

	public void enableGrid() {

	}

	private void initTableColumns() {

		TextColumn<SearchResultlist> transactionDateColumn = new TextColumn<SearchResultlist>() {

			@Override
			public String getValue(SearchResultlist object) {
				return String.valueOf(object.getDate() != null ? object
						.getDate() : "");
			}
		};

		Column<SearchResultlist, SafeHtml> typeColumn = new Column<SearchResultlist, SafeHtml>(
				new ClickableSafeHtmlCell()) {

			@Override
			public SafeHtml getValue(final SearchResultlist object) {
				return new SafeHtml() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 7929174412047187326L;

					@Override
					public String asString() {
						return getTransactionName(object.getTransactionType(),
								object.getTransactionSubType());
					}
				};
			}
		};

		typeColumn
				.setFieldUpdater(new FieldUpdater<SearchResultlist, SafeHtml>() {

					@Override
					public void update(int index, SearchResultlist object,
							SafeHtml value) {
						ReportsRPC.openTransactionView(getType(object),
								object.getId());
					}
				});

		TextColumn<SearchResultlist> amountColumn = new TextColumn<SearchResultlist>() {

			@Override
			public String getValue(SearchResultlist object) {
				return DataUtils.amountAsStringWithCurrency(object.getAmount(),
						object.getCurrency());
			}
		};

		this.addColumn(transactionDateColumn, messages.date());
		this.addColumn(typeColumn, messages.type());
		this.addColumn(amountColumn, messages.amount());
		this.setColumnWidth(transactionDateColumn, "15px");
		this.setColumnWidth(typeColumn, "40px");
		this.setColumnWidth(amountColumn, "20px");

	}

	int getType(SearchResultlist object) {

		return object.getTransactionType();
	}

	private String getTransactionName(int transactionType,
			int transactionSubType) {
		if (transactionType == ClientTransaction.TYPE_ESTIMATE) {
			if (transactionSubType == ClientEstimate.CHARGES) {
				return messages.charge();
			} else if (transactionSubType == ClientEstimate.CREDITS) {
				return messages.credit();
			} else if (transactionSubType == ClientEstimate.SALES_ORDER) {
				return messages.salesOrder();
			} else {
				return messages.estimate();
			}
		}  else {
			return Utility.getTransactionName(transactionType);
		}
	}
}
