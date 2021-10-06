package com.nitya.accounter.developer.api.process.lists;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.web.client.core.ClientFixedAsset;
import com.nitya.accounter.web.client.core.Features;

public class FixedAssetItemsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.FIXED_ASSET);
		int type = readInt(req, "item_type");
		ArrayList<ClientFixedAsset> list = service.getFixedAssetList(type);
		sendResult(list);
	}

}
