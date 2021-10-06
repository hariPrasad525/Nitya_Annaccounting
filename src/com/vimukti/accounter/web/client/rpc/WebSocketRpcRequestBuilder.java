package com.vimukti.accounter.web.client.rpc;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

public class WebSocketRpcRequestBuilder extends RpcRequestBuilder {

	@Override
	protected RequestBuilder doCreate(String serviceEntryPoint) {
		return new WebSocketRequestBuilder(RequestBuilder.POST,
				serviceEntryPoint);
	}

}
