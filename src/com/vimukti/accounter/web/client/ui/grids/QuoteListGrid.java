package com.vimukti.accounter.web.client.ui.grids;

import java.util.Set;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPhone;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.customers.NewQuoteAction;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class QuoteListGrid extends BaseListGrid<ClientEstimate> {

	public QuoteListGrid(int type) {
		super(false, type);
		this.getElement().setId("QuoteListGrid");
	}

	boolean isDeleted;

	@Override
	protected int[] setColTypes() {
		if (type == ClientEstimate.QUOTES) {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE
			// ,ListGrid.COLUMN_TYPE_IMAGE
			};
		}
		if (type == ClientEstimate.SALES_ORDER) {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT
			// ,ListGrid.COLUMN_TYPE_IMAGE
			};
		} else {
			return new int[] { ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
		}
	}

	@Override
	protected Object getColumnValue(ClientEstimate estimate, int col) {
		if (type != ClientEstimate.QUOTES && type != ClientEstimate.SALES_ORDER) {
			return this.getchargedColumnValues(estimate, col);
		}
		if (estimate != null) {
			ClientCustomer customer = getCompany().getCustomer(
					estimate.getCustomer());
			// ClientSalesPerson clientSalesPerson = Accounter.getCompany()
			// .getSalesPerson(estimate.getSalesPerson());
			// String salesPerson = clientSalesPerson != null ?
			// clientSalesPerson
			// .getFirstName() : "";
			// if (salesPerson == null)
			// salesPerson = messages.unavailabel();

			switch (col) {
			case 0:
				return UIUtils.getDateByCompanyType(estimate.getDate());
			case 1:
				return estimate.getNumber() + "";
			case 2:
				if (customer != null)
					return String.valueOf(customer.getName());
				break;
			case 3:
				// if (customer != null) {
				// Set<ClientPhone> phones = customer.getPhoneNumbers();
				// if (phones != null)
				// for (ClientPhone p : phones) {
				// return String.valueOf(p.getNumber());
				// }
				// }
				return estimate.getPhone();

				// case 4:
				// return String.valueOf(salesPerson);
			case 4:
				if (type == ClientEstimate.SALES_ORDER) {
					return UIUtils.getDateByCompanyType(new ClientFinanceDate(
							estimate.getDeliveryDate()));
				}
				return UIUtils.getDateByCompanyType(new ClientFinanceDate(
						estimate.getExpirationDate()));
			case 5:
				if (type == ClientEstimate.SALES_ORDER) {
					return DataUtils.amountAsStringWithCurrency(
							estimate.getTotal(), estimate.getCurrency());
				}

				return UIUtils.getDateByCompanyType(new ClientFinanceDate(
						estimate.getDeliveryDate()));
			case 6:
				return DataUtils.amountAsStringWithCurrency(
						estimate.getTotal(), estimate.getCurrency());
			case 7:
				if (estimate.getStatus() == ClientEstimate.STATUS_OPEN)
					return Accounter.getFinanceImages().beforereject();
				// return "/images/before-reject.png";
				if (estimate.getStatus() == ClientEstimate.STATUS_ACCECPTED
						|| estimate.getStatus() == ClientTransaction.STATUS_COMPLETED)
					return Accounter.getFinanceImages().tickMark();
				// return "/images/Tick-mark.png";
				if (estimate.getStatus() == ClientEstimate.STATUS_REJECTED
						|| estimate.getStatus() == ClientEstimate.STATUS_DELETED
						|| estimate.getStatus() == ClientTransaction.STATUS_CANCELLED)
					return Accounter.getFinanceImages().rejected();
				// return "/images/cancel.png";
				break;
			case 9:
				// if (estimate.getStatus() ==
				// ClientTransaction.STATUS_DELETED)
				// return FinanceApplication.getFinanceImages().delSuccess()
				// .getURL();
				// else
				// return FinanceApplication.getFinanceImages().delete()
				// .getURL();

			default:
				break;
			}
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		if (type == ClientEstimate.QUOTES) {
			return new String[] { messages.date(), messages.no(),
					messages.payeeName(Global.get().Customer()),
					messages.phone(), messages.expirationDate(),
					messages.deliveryDate(), messages.totalPrice(),
					messages.reject()
			// , ""
			};
		} else if (type == ClientEstimate.SALES_ORDER) {
			return new String[] { messages.date(), messages.no(),
					messages.payeeName(Global.get().Customer()),
					messages.phone(), messages.deliveryDate(),
					messages.totalPrice() };
		} else {
			return new String[] { messages.date(), messages.no(),
					messages.payeeName(Global.get().Customer()),
					messages.netAmount(), messages.totalPrice() };
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 80;
		else if (index == 1)
			return 50;
		else if (index == 2)
			return 160;
		else if (index == 3)
			return 100;
		else if (index == 4)
			return 100;
		else if (index == 5)
			return 100;
		else if (index == 6)
			return 80;
		else if (index == 7)
			return 45;
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDoubleClick(ClientEstimate obj) {
		if (isCanOpenTransactionView(obj.getSaveStatus(), obj.getType())) {
			new NewQuoteAction(obj.getEstimateType()).run(obj, false);
		}
	}

	private boolean isUserHavePermissions(ClientEstimate obj) {
		ClientUser user = Accounter.getUser();
		if (user.canDoInvoiceTransactions()) {
			return true;
		}

		if (obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT
				&& user.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
			return true;
		}
		return false;
	}

	protected void onClick(ClientEstimate obj, int row, int col) {
		if (!isCanOpenTransactionView(obj.getSaveStatus(), obj.getType()))
			return;
		if (col == 7 && obj.getStatus() == ClientEstimate.STATUS_OPEN
				&& obj.getEstimateType() != ClientEstimate.SALES_ORDER) {
			showWarningDialog(obj, col);
		}
		// else if (col == 9 && obj.getStatus() !=
		// ClientEstimate.STATUS_DELETED) {
		// if (!isDeleted)
		// showWarningDialog(obj, col);
		// else
		// return;
		// }

	}

	protected String getViewTitle() {
		if (type == ClientEstimate.CREDITS) {
			return messages.credit();
		} else if (type == ClientEstimate.CHARGES) {
			return messages.charge();
		} else if (type == ClientEstimate.SALES_ORDER) {
			return messages.salesOrder();
		}
		return messages.quote();
	}

	private void showWarningDialog(final ClientEstimate obj, final int col) {
		String msg = null;
		if (col == 7 && obj.getStatus() == ClientEstimate.STATUS_OPEN
				&& obj.getSaveStatus() != ClientTransaction.STATUS_DRAFT) {
			msg = messages.doyouwanttorejecttheEstimate(getViewTitle());
		} else if (col == 7
				&& obj.getSaveStatus() == ClientTransaction.STATUS_DRAFT) {
			Accounter.showError(messages.youCannotrejecttheEstimate());
			return;
		}
		// else if (col == 9) {
		// msg = "Do you want to Delete the Transaction";

		// }
		Accounter.showWarning(msg, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onCancelClick() {

						return false;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onYesClick() {
						if (col == 7)
							updateEstimate(obj);
						else
							deleteTransaction(obj);
						return true;
					}

				});

	}

	protected void updateEstimate(final ClientEstimate obj) {
		obj.setStatus(ClientEstimate.STATUS_REJECTED);
		Accounter.createOrUpdate(this, obj);
	}

	protected void deleteTransaction(final ClientEstimate obj) {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					if (!viewType.equalsIgnoreCase("All"))
						deleteRecord(obj);
					obj.setStatus(ClientTransaction.STATUS_DELETED);
					isDeleted = true;
					updateData(obj);

				}

			}
		};
		AccounterCoreType type = UIUtils.getAccounterCoreType(obj.getType());
		rpcDoSerivce.deleteTransaction(type, obj.id, callback);
	}

	// this is not using any where
	@Override
	protected void executeDelete(ClientEstimate object) {

	}

	@Override
	protected int sort(ClientEstimate obj1, ClientEstimate obj2, int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date1 = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;
		case 1:
			Integer num1 = Integer.parseInt(obj1.getNumber());
			Integer num2 = Integer.parseInt(obj2.getNumber());
			return num1.compareTo(num2);

		case 2:
			String name1 = getCustomer(obj1).toLowerCase();
			String name2 = getCustomer(obj2).toLowerCase();
			return name1.compareTo(name2);
		case 3:
			String phone1 = getPhoneNumber(obj1);
			String phone2 = getPhoneNumber(obj2);
			return phone1.compareTo(phone2);

			// case 4:
			// String salesPerson1 = getSalesPerson(obj1).toLowerCase();
			// String salesPerson2 = getSalesPerson(obj2).toLowerCase();
			// return salesPerson1.compareTo(salesPerson2);

		case 4:
			ClientFinanceDate expiration1 = new ClientFinanceDate(
					obj1.getExpirationDate());
			ClientFinanceDate expiration2 = new ClientFinanceDate(
					obj2.getExpirationDate());
			return expiration1.compareTo(expiration2);

		case 5:
			ClientFinanceDate deliveryDate1 = new ClientFinanceDate(
					obj1.getDeliveryDate());
			ClientFinanceDate deliveryDate2 = new ClientFinanceDate(
					obj2.getDeliveryDate());
			return deliveryDate1.compareTo(deliveryDate2);

		case 6:
			Double price1 = obj1.getTotal();
			Double price2 = obj2.getTotal();
			return price1.compareTo(price2);

		default:
			break;
		}

		return 0;
	}

	private String getCustomer(ClientEstimate estimate) {
		ClientCustomer customer = getCompany().getCustomer(
				estimate.getCustomer());

		if (customer != null)
			return String.valueOf(customer.getName());
		return "";

	}

	private String getSalesPerson(ClientEstimate estimate) {
		ClientSalesPerson clientSalesPerson = getCompany().getSalesPerson(
				estimate.getSalesPerson());
		return clientSalesPerson != null ? clientSalesPerson.getFirstName()
				: "";

	}

	private String getPhoneNumber(ClientEstimate estimate) {
		String phoneNo = null;
		if (estimate != null) {
			ClientCustomer customer = getCompany().getCustomer(
					estimate.getCustomer());
			if (customer != null) {
				Set<ClientPhone> phones = customer.getPhoneNumbers();
				if (phones != null)
					for (ClientPhone p : phones) {
						if (p.getType() == ClientPhone.BUSINESS_PHONE_NUMBER) {
							phoneNo = String.valueOf(p.getNumber());
						}
					}
			}

		}
		return phoneNo != null ? phoneNo : "";
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (object instanceof ClientEstimate) {
			if (!viewType.equalsIgnoreCase(messages.all()))
				deleteRecord((ClientEstimate) object);
			updateData((ClientEstimate) object);
		}
		super.saveSuccess(object);
	}

	// this is not using any where

	// this is not using any where
	public AccounterCoreType getType() {
		return null;
	}

	/**
	 * 
	 * @param estimate
	 * @param col
	 * @return
	 */
	private Object getchargedColumnValues(ClientEstimate estimate, int col) {

		if (estimate != null) {
			ClientCustomer customer = getCompany().getCustomer(
					estimate.getCustomer());

			switch (col) {
			case 0:
				return UIUtils.getDateByCompanyType(estimate.getDate());
			case 1:
				return estimate.getNumber() + "";
			case 2:
				if (customer != null)
					return String.valueOf(customer.getName());
				break;

			case 3:
				return DataUtils.amountAsStringWithCurrency(
						estimate.getNetAmount(), estimate.getCurrency());

			case 4:
				return DataUtils.amountAsStringWithCurrency(
						estimate.getTotal(), estimate.getCurrency());
			default:
				break;
			}
		}
		return null;

	}

	@Override
	protected String[] setHeaderStyle() {
		if (type == ClientEstimate.QUOTES) {
			return new String[] { "date", "no", "payeename", "phone",
					"expirationdate", "deliverydate", "totalprice", "reject" };
		} else if (type == ClientEstimate.SALES_ORDER) {
			return new String[] { "date", "no", "payeename", "deliveryDate",
					"totalprice", "reject" };
		} else {
			return new String[] { "date", "no", "payeename", "totalprice",
					"reject" };
		}
	}

	@Override
	protected String[] setRowElementsStyle() {
		if (type == ClientEstimate.QUOTES) {
			return new String[] { "date-value", "no-value", "payeename-value",
					"phone-value", "expirationdate-value",
					"deliverydate-value", "totalprice-value", "reject-value" };
		} else if (type == ClientEstimate.SALES_ORDER) {
			return new String[] { "date-value", "no-value", "payeename-value",
					"deliveryDate-value", "totalprice-value", "reject-value" };
		} else {
			return new String[] { "date-value", "no-value", "payeename-value",
					"totalprice-value", "reject-value" };
		}
	}
}
