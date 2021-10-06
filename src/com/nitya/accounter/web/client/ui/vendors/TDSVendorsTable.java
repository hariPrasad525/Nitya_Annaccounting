package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Lists.ClientTDSInfo;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.IDeleteCallback;

public class TDSVendorsTable extends CellTable<ClientTDSInfo> implements
		IDeleteCallback {

	private ListDataProvider<ClientTDSInfo> dataprovider = new ListDataProvider<ClientTDSInfo>();

	TDSVendorsTable() {
		initColumns();
	}

	private void initColumns() {

		dataprovider.addDataDisplay(this);

		TextColumn<ClientTDSInfo> vendorName = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return object.getVendor().getName();
			}
		};

		TextColumn<ClientTDSInfo> date = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return object.getDate().toString();
			}
		};
		TextColumn<ClientTDSInfo> originalBalance = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return DataUtils.getAmountAsStringInPrimaryCurrency(object.getOrginalBalance());
			}
		};
		TextColumn<ClientTDSInfo> payment = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return DataUtils.getAmountAsStringInPrimaryCurrency(object.getPayment());
			}
		};

		TextColumn<ClientTDSInfo> tdsPercentage = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return String.valueOf(Accounter.getCompany()
						.getTAXItem(object.getVendor().getTaxItemCode())
						.getTaxRate())
						+ "%";
			}
		};
		TextColumn<ClientTDSInfo> tdsAmount = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {
				return DataUtils.getAmountAsStringInPrimaryCurrency(object.getTdsAmount());
			}
		};

		
		AccounterMessages messages=Global.get().messages();
		this.addColumn(vendorName, messages.Vendor());
		this.addColumn(date, messages.date());
		this.addColumn(originalBalance, messages.originalAmount());
		this.addColumn(payment, messages.payment());
		this.addColumn(tdsPercentage, messages.percentage());
		this.addColumn(tdsAmount, messages.tdsAmount());

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	public void setData(List<ClientTDSInfo> data) {
		if (data == null) {
			data = new ArrayList<ClientTDSInfo>();
		}

		this.dataprovider.setList(data);
	}

}
