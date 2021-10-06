package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.ui.PortalLayout;

public interface IPortletPage {

	ArrayList<String> getAddablePortletList();

	PortalLayout getPortalLayout();

}
