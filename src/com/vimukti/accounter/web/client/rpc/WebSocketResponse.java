package com.vimukti.accounter.web.client.rpc;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.Response;

public class WebSocketResponse extends Response {

	private List<Header> headers = new ArrayList<Header>();
	private int statusCode;
	private String allHeaders;
	private String data;
	private String statusText;

	public WebSocketResponse(String message) {
		String[] split = message.split("\r\n");
		String reply=split[0];
		String statusparts[] = reply.split(" ");
		this.statusCode = Integer.parseInt(statusparts[1]);
		String codeString=statusCode+" ";
		this.statusText = reply.substring(reply.indexOf(codeString)+codeString.length());
		this.allHeaders = "";
		String header = split[1];
		int index = 1;
		while (!header.equals("")) {
			allHeaders += header;
			headers.add(new WsHeader(header));
			allHeaders += "\n";
			header=split[++index];
		}
		data="";
		for (; index < split.length; index++) {
			if (data.length() != 0) {
				data+="\n";
			}
			data+=split[index];
		}
	}

	@Override
	public String getHeader(String header) {
		for(Header h:headers){
			if(h.getName().equals(header)){
				return h.getValue();
			}
		}
		return null;
	}

	@Override
	public Header[] getHeaders() {
		return headers.toArray(new Header[headers.size()]);
	}

	@Override
	public String getHeadersAsString() {
		return allHeaders;
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String getStatusText() {
		return statusText;
	}

	@Override
	public String getText() {
		return data;
	}

	class WsHeader extends Header {

		private String name;
		private String value;

		public WsHeader(String msg) {
			String[] split = msg.split(": +");
			this.name = split[0].trim();
			this.value = split[1].trim();
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getValue() {
			return value;
		}

	}

}
