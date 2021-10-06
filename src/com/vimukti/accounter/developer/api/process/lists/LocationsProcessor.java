package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.Features;

public class LocationsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.LOCATION);
		ClientConvertUtil util = new ClientConvertUtil();
		List<ClientLocation> list = new ArrayList<ClientLocation>();
		Set<Location> groups = getCompany().getLocations();
		for (Location customerGroup : groups) {
			ClientLocation object = util.toClientObject(customerGroup,
					ClientLocation.class);
			list.add(object);
		}
		sendResult(list);
	}

}