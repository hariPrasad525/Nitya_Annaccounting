package com.vimukti.accounter.mobile;

import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;

public abstract class MobileChannelContext {
	private String networkId;
	private String message;
	private AdaptorType adaptorType;
	private int networkType;

	public MobileChannelContext(String networkId, String message,
			AdaptorType adaptorType, int networkType) {
		this.networkId = networkId;
		this.message = message;
		this.adaptorType = adaptorType;
		this.networkType = networkType;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AdaptorType getAdaptorType() {
		return adaptorType;
	}

	public void setAdaptorType(AdaptorType adaptorType) {
		this.adaptorType = adaptorType;
	}

	public int getNetworkType() {
		return networkType;
	}

	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	public abstract void send(String string);

	public abstract void changeNetworkId(String networkId);

}
