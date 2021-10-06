package com.vimukti.accounter.web.client.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;

public class WebSocketRequest extends Request{

	private String request;
	private RequestCallback callback;

	public WebSocketRequest(String request, RequestCallback callback) {
		this.callback=callback;
		this.request=request;
	}

	@Override
	public void cancel() {
		super.cancel();
	}

	@Override
	public boolean isPending() {
		return super.isPending();
	}

	public RequestCallback getCallback() {
		return callback;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}


}
