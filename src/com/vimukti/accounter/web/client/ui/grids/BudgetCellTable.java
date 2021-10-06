package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.AddBudgetAmountDialogue;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.ClickableSafeHtmlCell;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class BudgetCellTable extends CellTable<ClientBudgetItem> {

	ListDataProvider<ClientBudgetItem> listDataProvider = new ListDataProvider<ClientBudgetItem>();

	protected AccounterMessages messages = Global.get().messages();

	int rowSelected;

	int dialogueType = 0;

	public BudgetCellTable() {
		createControls();
		this.addStyleName("budget-table");
	}

	private void createControls() {

		setPageSize(50);
		listDataProvider.addDataDisplay(this);
		List<ClientBudgetItem> list = listDataProvider.getList();

		List<ClientAccount> listOfExpenseAccounts = Accounter.getCompany()
				.getAccounts(ClientAccount.TYPE_EXPENSE);
		List<ClientAccount> listOfOtherExpenseAccounts = Accounter.getCompany()
				.getAccounts(ClientAccount.TYPE_OTHER_EXPENSE);
		List<ClientAccount> listOfIncomeAccounts = Accounter.getCompany()
				.getAccounts(ClientAccount.TYPE_INCOME);
		List<ClientAccount> listOfOtherIncomeAccounts = Accounter.getCompany()
				.getAccounts(ClientAccount.TYPE_OTHER_INCOME);
		List<ClientAccount> listOfCostOFGoodAccounts = Accounter.getCompany()
				.getAccounts(ClientAccount.TYPE_COST_OF_GOODS_SOLD);

		List<ClientAccount> listOfAccounts = new ArrayList<ClientAccount>(
				listOfExpenseAccounts);
		listOfAccounts.addAll(listOfOtherExpenseAccounts);
		listOfAccounts.addAll(listOfIncomeAccounts);
		listOfAccounts.addAll(listOfOtherIncomeAccounts);
		listOfAccounts.addAll(listOfCostOFGoodAccounts);

		for (ClientAccount account : listOfAccounts) {

			ClientBudgetItem obj = new ClientBudgetItem();
			obj.setAccountsName(account.getName());
			obj.setAccount(account);

			list.add(obj);

		}

		// this.setWidth("100%", true);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		initTableColumns();
	}

	private void initTableColumns() {

		Column<ClientBudgetItem, SafeHtml> accountLinkColumn = new Column<ClientBudgetItem, SafeHtml>(
				new ClickableSafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ClientBudgetItem object) {
				SafeHtmlBuilder shb = new SafeHtmlBuilder();

				shb.appendHtmlConstant("<u>");
				shb.appendEscaped(object.getAccountsName());
				shb.appendHtmlConstant("</u>");
				return shb.toSafeHtml();
			}
		};
		accountLinkColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, SafeHtml>() {

					@Override
					public void update(int index,
							final ClientBudgetItem object, SafeHtml safeHtml) {
						rowSelected = index;

						HashMap<String, String> map = new HashMap<String, String>();

						ArrayList<Object> mapList = new ArrayList<Object>();
						mapList.add(map);
						mapList.add(dialogueType);

						String budgetTitle = messages.AddBudgetfor(object
								.getAccountsName());
						AddBudgetAmountDialogue assignAccountsTo1099Dialog = new AddBudgetAmountDialogue(
								budgetTitle, "", mapList, object);
						assignAccountsTo1099Dialog
								.setCallback(new ActionCallback<ArrayList<Object>>() {

									@Override
									public void actionResult(
											ArrayList<Object> result) {
										// TODO Auto-generated method stub
										HashMap<String, String> newMap = (HashMap<String, String>) result
												.get(0);
										dialogueType = (Integer) result.get(1);

										refreshView(newMap, object);
									}

								});
						ViewManager.getInstance().showDialog(
								assignAccountsTo1099Dialog);

					}
				});

		Column<ClientBudgetItem, String> janMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		janMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> febMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getFebruaryAmount());

			}
		};
		febMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> marMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getMarchAmount());

			}
		};
		marMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> aprMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getAprilAmount());

			}
		};
		aprMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> mayMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getMayAmount());

			}
		};
		mayMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> juneMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJuneAmount());

			}
		};
		juneMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> julMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJulyAmount());

			}
		};
		julMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> augMonthCoulmn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getAugustAmount());

			}
		};
		augMonthCoulmn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> septMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getSpetemberAmount());

			}
		};
		septMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> octMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getOctoberAmount());

			}
		};
		octMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> novMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getNovemberAmount());

			}
		};
		novMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> decMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getDecemberAmount());

			}
		};
		decMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		this.setColumnWidth(accountLinkColumn, "200px");
		this.setColumnWidth(janMonthColumn, "53px");
		this.setColumnWidth(febMonthColumn, "53px");
		this.setColumnWidth(marMonthColumn, "53px");
		this.setColumnWidth(aprMonthColumn, "53px");
		this.setColumnWidth(mayMonthColumn, "53px");
		this.setColumnWidth(juneMonthColumn, "53px");
		this.setColumnWidth(julMonthColumn, "53px");
		this.setColumnWidth(augMonthCoulmn, "53px");
		this.setColumnWidth(septMonthColumn, "53px");
		this.setColumnWidth(octMonthColumn, "53px");
		this.setColumnWidth(novMonthColumn, "53px");
		this.setColumnWidth(decMonthColumn, "53px");

		this.addColumn(accountLinkColumn, messages.Account());
		this.addColumn(janMonthColumn, DayAndMonthUtil.jan());
		this.addColumn(febMonthColumn, DayAndMonthUtil.feb());
		this.addColumn(marMonthColumn, DayAndMonthUtil.mar());
		this.addColumn(aprMonthColumn, DayAndMonthUtil.apr());
		this.addColumn(mayMonthColumn, DayAndMonthUtil.mayS());
		this.addColumn(juneMonthColumn, DayAndMonthUtil.jun());
		this.addColumn(julMonthColumn, DayAndMonthUtil.jul());
		this.addColumn(augMonthCoulmn, DayAndMonthUtil.aug());
		this.addColumn(septMonthColumn, DayAndMonthUtil.sep());
		this.addColumn(octMonthColumn, DayAndMonthUtil.oct());
		this.addColumn(novMonthColumn, DayAndMonthUtil.nov());
		this.addColumn(decMonthColumn, DayAndMonthUtil.dec());

	}

	public void sortRowData(int columnIndex, boolean isAscending) {
		redraw();
	}

	/*
	 * private void openLinkAction(final ClientBudgetItem object) {
	 * HashMap<String, String> map = new HashMap<String, String>(); String
	 * budgetTitle = "Add Budget for " + object.getAccountsName();
	 * AddBudgetAmountDialogue assignAccountsTo1099Dialog = new
	 * AddBudgetAmountDialogue( budgetTitle, "", map, object);
	 * assignAccountsTo1099Dialog .setCallback(new
	 * ActionCallback<HashMap<String, String>>() {
	 * 
	 * @Override public void actionResult(HashMap<String, String> result) {
	 * refreshView(result, object);
	 * 
	 * } }); assignAccountsTo1099Dialog.show(); }
	 */

	private void refreshView(HashMap<String, String> result,
			ClientBudgetItem obj) {

		obj.setJanuaryAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.jan())));
		obj.setFebruaryAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.feb())));
		obj.setMarchAmount(Double.parseDouble(result.get(DayAndMonthUtil.mar())));
		obj.setAprilAmount(Double.parseDouble(result.get(DayAndMonthUtil.apr())));
		obj.setMayAmount(Double.parseDouble(result.get(DayAndMonthUtil.mayS())));
		obj.setJuneAmount(Double.parseDouble(result.get(DayAndMonthUtil.jun())));
		obj.setJulyAmount(Double.parseDouble(result.get(DayAndMonthUtil.jul())));
		obj.setAugustAmount(Double.parseDouble(result.get(DayAndMonthUtil.aug())));
		obj.setOctoberAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.oct())));

		obj.setNovemberAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.nov())));
		obj.setSeptemberAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.sep())));
		obj.setDecemberAmount(Double.parseDouble(result.get(DayAndMonthUtil
				.dec())));
		obj.setTotalAmount(Double.parseDouble(result.get(messages.total())));

		// Double total;
		// total = Double.parseDouble(result.get(DayAndMonthUtil.jan()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.feb()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.mar()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.apr()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.mayS()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.jun()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.jul()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.aug()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.oct()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.nov()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.sep()))
		// + Double.parseDouble(result.get(DayAndMonthUtil.dec()));
		//
		// obj.setTotalAmount(total);

		refreshAllRecords(obj);

	}

	private void refreshAllRecords(ClientBudgetItem obj) {

		List<ClientBudgetItem> list = listDataProvider.getList();
		list.remove(rowSelected);
		list.add(rowSelected, obj);
	}

	public List<ClientBudgetItem> getDataList() {
		List<ClientBudgetItem> list = listDataProvider.getList();
		return list;
	}

	public void setDataProvided(ClientBudget data) {

		List<ClientBudgetItem> list = listDataProvider.getList();

		for (ClientBudgetItem budgetItem : data.getBudgetItem()) {
			int i = 0;
			budgetItem.setAccountsName(budgetItem.getAccount().getName());
			for (ClientBudgetItem item : list) {
				if (item.getAccount().getID() == budgetItem.getAccount()
						.getID()) {
					list.set(i, budgetItem);
				}
				i++;
			}
			// list.add(budgetItem);
		}

	}
}
