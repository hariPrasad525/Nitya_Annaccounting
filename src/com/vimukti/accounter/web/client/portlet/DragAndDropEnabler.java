package com.vimukti.accounter.web.client.portlet;

import java.util.Map.Entry;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class DragAndDropEnabler implements DragHandler {

	private PickupDragController dragController;
	private DragAndDropInput input;

	public DragAndDropEnabler(DragAndDropInput input) {
		this.input = input;
		dragController = new PickupDragController(input.page, false);
		dragController.setBehaviorMultipleSelection(false);
		dragController.addDragHandler(this);
	}

	public void enable() {
		for (FlowPanel column : input.columns) {
			FlowPanelDropController dropController = new FlowPanelDropController(
					column);
			dragController.registerDropController(dropController);
		}

		for (Entry<Widget, Widget> portlets : input.portlets.entrySet()) {
			dragController
					.makeDraggable(portlets.getKey(), portlets.getValue());
		}
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		input.callback.onSuccess(Boolean.TRUE);
	}

	@Override
	public void onDragStart(DragStartEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
		// TODO Auto-generated method stub

	}

}
