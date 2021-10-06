package com.nitya.accounter.web.client.portlet;

import java.util.ArrayList;

import com.nitya.accounter.web.client.ui.PortalLayout;

public interface IPortletPage {

	ArrayList<String> getAddablePortletList();

	PortalLayout getPortalLayout();

}
