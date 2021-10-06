/**
 * 
 */
package com.nitya.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientReconciliation;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.IDeleteCallback;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.grids.columns.ImageActionColumn;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationsTable extends CellTable<ClientReconciliation>
		implements IDeleteCallback {

	private ListDataProvider<ClientReconciliation> dataprovider = new ListDataProvider<ClientReconciliation>();
	protected static AccounterMessages messages = Global.get().messages();

	/**
	 * Creates new Instance
	 */
	public ReconciliationsTable() {
		initColumns();
		dataprovider.addDataDisplay(this);
		HTML emptyMessage = new HTML(messages.reconciliationsEmpty());
		emptyMessage.setHeight("150px");
		setEmptyTableWidget(emptyMessage);
	}

	/**
	 * 
	 */
	private void initColumns() {

		TextColumn<ClientReconciliation> reconciliationDate = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return object.getReconcilationDate().toString();
			}
		};

		TextColumn<ClientReconciliation> reconciliationPeriod = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return messages.oneToOther(object.getStartDate().toString(),
						object.getEndDate().toString());
			}
		};

		TextColumn<ClientReconciliation> openingBalance = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {

				return DataUtils.getAmountAsStringInCurrency(
						object.getOpeningBalance(), Accounter.getCompany()
								.getCurrency(object.getAccount().getCurrency())
								.getSymbol());
			}
		};

		TextColumn<ClientReconciliation> closingBalance = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return DataUtils.getAmountAsStringInCurrency(
						object.getClosingBalance(), Accounter.getCompany()
								.getCurrency(object.getAccount().getCurrency())
								.getSymbol());
			}
		};

		ImageActionColumn<ClientReconciliation> show = new ImageActionColumn<ClientReconciliation>() {

			@Override
			protected void onSelect(int index, ClientReconciliation object) {
				openReconciliation(object);
			}

			@Override
			public ImageResource getValue(ClientReconciliation object) {
				return Accounter.getFinanceMenuImages().accounterRegisterIcon();
			}
		};

		ImageActionColumn<ClientReconciliation> delete = new ImageActionColumn<ClientReconciliation>() {

			@Override
			protected void onSelect(int index, final ClientReconciliation object) {
				// Accounter.getFinanceMenuImages().accounterRegisterIcon()

				// Show the cofirmation dialog box before delete
				if (!Utility
						.isUserHavePermissions(AccounterCoreType.RECONCILIATION)) {
					return;
				}
				Accounter.showWarning(
						messages.areYouwantToDeleteReconcilationHistory(),
						AccounterType.WARNING, new ErrorDialogHandler() {

							@Override
							public boolean onYesClick() {
								deleteReconciliation(object);
								return true;
							}

							@Override
							public boolean onNoClick() {
								return true;
							}

							@Override
							public boolean onCancelClick() {
								return true;
							}
						});

			}

			@Override
			public ImageResource getValue(ClientReconciliation object) {
				return Accounter.getFinanceImages().delete();
			}
		};

		this.addColumn(reconciliationDate, messages.ReconciliationDate());
		this.addColumn(reconciliationPeriod, messages.ReconciliationPeriod());
		this.addColumn(openingBalance, messages.openBalance());
		this.addColumn(closingBalance, messages.ClosingBalance());
		this.addColumn(show, messages.show());
		this.addColumn(delete);
	}

	private void deleteReconciliation(ClientReconciliation object) {
		Accounter.deleteObject(this, object);
	}

	public void setData(List<ClientReconciliation> data) {
		if (data == null) {
			data = new ArrayList<ClientReconciliation>();
		}
		this.dataprovider.setList(data);
	}

	public void openReconciliation(ClientReconciliation reconciliation) {
		new NewReconcileAccountAction().run(reconciliation, false);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		Accounter.showError(AccounterExceptions.getErrorString(caught
				.getErrorCode()));
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		dataprovider.getList().remove((ClientReconciliation) result);
	}

}
