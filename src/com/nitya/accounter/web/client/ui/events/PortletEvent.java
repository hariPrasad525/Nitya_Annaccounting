package com.nitya.accounter.web.client.ui.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PortletEvent extends GwtEvent<PortletEventHandler> {

	public static final GwtEvent.Type<PortletEventHandler> TYPE = new GwtEvent.Type<PortletEventHandler>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared
	 * .EventHandler)
	 */
	@Override
	protected void dispatch(PortletEventHandler arg0) {
		// NOTHING TO DO.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public GwtEvent.Type<PortletEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return null;
	}

}
