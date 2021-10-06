package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientMessageOrTask;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.company.AddMessageOrTaskAction;
import com.vimukti.accounter.web.client.ui.grids.MessagesAndTasksGrid;

public class MessagesAndTasksPortlet extends Portlet {

	private MessagesAndTasksGrid grid;

	public MessagesAndTasksPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.messagesAndTasks(), messages
				.addNewmessageOrTask());
		this.getElement().setId("MessagesAndTasksPortlet");
	}

	@Override
	public void createBody() {
		grid = new MessagesAndTasksGrid();
		grid.init();
		body.add(grid);
		updateData();
	}

	@Override
	public void goToClicked() {
		AddMessageOrTaskAction action = new AddMessageOrTaskAction() {

			@Override
			public ImageResource getSmallImage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ImageResource getBigImage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void updateData() {
				refreshWidget();

			}
		};

		action.run();

	}

	private void updateData() {
		grid.removeAllRecords();
		Accounter.createHomeService().getMessagesAndTasks(
				new AsyncCallback<ArrayList<ClientMessageOrTask>>() {

					@Override
					public void onSuccess(ArrayList<ClientMessageOrTask> result) {
						if (result != null && !result.isEmpty()) {
							for (ClientMessageOrTask clientMessageOrTask : result) {
								long toUser = clientMessageOrTask.getToUser();
								if (toUser == Accounter.getUser().getID()
										|| toUser == ClientMessageOrTask.TO_USER_TYPE_ALL
										|| clientMessageOrTask.getCreatedBy() == Accounter
												.getUser().getID()) {
									grid.addData(clientMessageOrTask);
								}
							}
						} else {
							grid.addEmptyMessage(messages.noRecordsToShow());
						}
						completeInitialization();
					}

					@Override
					public void onFailure(Throwable caught) {
						completeInitialization();
					}
				});
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}

}
