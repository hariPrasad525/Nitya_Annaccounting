package com.vimukti.accounter.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.IDeleteCallback;
import com.vimukti.accounter.web.client.ui.ISaveCallback;
import com.vimukti.accounter.web.client.ui.StyledPanel;

public class AdminHomePage extends FlowPanel implements EntryPoint {
	static AdminViewManger viewmanger;
	Button mybutton;
	StyledPanel vpPanel;
	private static AdminConstants adminconstants;
	private static AdminCRUDServiceAsync crudService;
	private static AdminHomeViewServiceAsync homeViewService;
	public final static String CRUD_SERVICE_ENTRY_POINT = "/do/admin/crud/rpc/service";
	public final static String HOME_SERVICE_ENTRY_POINT = "/do/admin/home/rpc/service";
	

	@Override
	public void onModuleLoad() {
		Global.get().messages();
		vpPanel = new StyledPanel("vpPanel");
		createControls();
		RootPanel.get().add(vpPanel);

	}

	private void createControls() {
		// vpPanel.setWidth("100%");
		// vpPanel.setHeight("100%");
		AdminHorizantalMenuBar adminHMenubar = new AdminHorizantalMenuBar();
		vpPanel.add(adminHMenubar);
		viewmanger = new AdminViewManger();
		vpPanel.add(viewmanger);

	}

	public static AdminViewManger getViewManager() {
		return viewmanger;
	}

	public static AdminConstants getAdminConstants() {
		if (adminconstants == null) {
			adminconstants = (AdminConstants) GWT.create(AdminConstants.class);
		}
		return adminconstants;
	}

	public static AdminUserListAction getAdminUserListAction() {
		return new AdminUserListAction();
	}

	public static AddNewAdminuserAction getNewAdminUserAction() {
		return new AddNewAdminuserAction();
	}

	public static AdminCRUDServiceAsync createCRUDService() {
		if (crudService == null) {
			crudService = (AdminCRUDServiceAsync) GWT
					.create(AdminCRUDService.class);
			((ServiceDefTarget) crudService)
					.setServiceEntryPoint(AdminHomePage.CRUD_SERVICE_ENTRY_POINT);
		}

		return crudService;

	}

	public static AdminHomeViewServiceAsync createHomeService() {
		if (homeViewService == null) {
			homeViewService = (AdminHomeViewServiceAsync) GWT
					.create(AdminHomeViewService.class);
			((ServiceDefTarget) homeViewService)
					.setServiceEntryPoint(AdminHomePage.HOME_SERVICE_ENTRY_POINT);
		}
		return homeViewService;
	}

	public static void createORUpdate(final ISaveCallback source,
			final IAccounterCore coreObj) {

		final AccounterAsyncCallback<Long> addNewadminUserCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
				// TODO handle other kind of errors
			}

			public void onResultSuccess(Long result) {
				coreObj.setID(result);
				source.saveSuccess(coreObj);
			}
		};
		if (coreObj.getID() == 0) {
			AdminHomePage.createCRUDService().inviteNewAdminUser(
					(IAccounterCore) coreObj, addNewadminUserCallBack);
		} else {
			AdminHomePage.createCRUDService().updateAdminUser(
					(IAccounterCore) coreObj, addNewadminUserCallBack);
		}
	}

	public static void deleteAdminuser(final IDeleteCallback source,
			final IAccounterCore coreObj) {

		final AccounterAsyncCallback<Boolean> deleteadminUserCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException caught) {
				source.deleteFailed(caught);
				caught.printStackTrace();
			}

			@Override
			public void onResultSuccess(Boolean result) {
				source.deleteSuccess(coreObj);
				Accounter.showInformation(coreObj.getName() + coreObj.getID());
			}
		};
		AdminHomePage.createCRUDService().deleteAdminUser(coreObj,
				"adminEmailId", deleteadminUserCallBack);

	}
}
