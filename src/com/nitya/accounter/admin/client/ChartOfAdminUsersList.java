package com.nitya.accounter.admin.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.IDeleteCallback;
import com.nitya.accounter.web.client.ui.grids.columns.ImageActionColumn;

public class ChartOfAdminUsersList extends CellTable<ClientAdminUser> implements
		IDeleteCallback {
	private AccounterMessages messages = Accounter.getMessages();
	List<ClientAdminUser> userlistList;
	ListDataProvider<ClientAdminUser> dataProvider;
	TextColumn<ClientAdminUser> serialNo, name, email, type, password;
	ImageActionColumn<ClientAdminUser> deleteImage;

	ChartOfAdminUsersList() {
		createControls();
	}

	private void createControls() {
		dataProvider = new ListDataProvider<ClientAdminUser>();
		dataProvider.addDataDisplay(this);
		initTableColumns();
		initList();

	}

	private void initList() {
		AdminHomePage.createHomeService().getAdminUsersList(
				new AccounterAsyncCallback<ArrayList<ClientAdminUser>>() {

					@Override
					public void onException(AccounterException exception) {
					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientAdminUser> result) {
						dataProvider.setList(result);
					}
				});
	}

	private void initTableColumns() {

		serialNo = new TextColumn<ClientAdminUser>() {

			@Override
			public String getValue(ClientAdminUser object) {
				return String.valueOf(object.getID());
			}
		};

		name = new TextColumn<ClientAdminUser>() {

			@Override
			public String getValue(ClientAdminUser object) {
				return String.valueOf(object.getName());
			}
		};

		email = new TextColumn<ClientAdminUser>() {

			@Override
			public String getValue(ClientAdminUser object) {
				return String.valueOf(object.getEmailId());
			}
		};

		type = new TextColumn<ClientAdminUser>() {

			@Override
			public String getValue(ClientAdminUser object) {
				return String.valueOf(object.getTypeOfUser());
			}
		};

		password = new TextColumn<ClientAdminUser>() {

			@Override
			public String getValue(ClientAdminUser object) {

				return String.valueOf(object.getPassword());
			}
		};
		deleteImage = new ImageActionColumn<ClientAdminUser>() {

			@Override
			protected void onSelect(int index, ClientAdminUser object) {
				deleteAdminUser(object);
			}

			@Override
			public ImageResource getValue(ClientAdminUser object) {

				return Accounter.getFinanceImages().delete();
			}

		};

		this.addColumn(serialNo, messages.no());
		this.addColumn(name, messages.name());
		this.addColumn(email, messages.email());
		this.addColumn(type, messages.type());
		this.addColumn(password, messages.password());
		this.addColumn(deleteImage, "X");

	}

	protected void deleteAdminUser(ClientAdminUser object) {
		AdminHomePage.deleteAdminuser(this, object);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

}
