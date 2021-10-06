package com.nitya.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientCompany;

public class CompanyAndFeatures implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientCompany clientCompany;

	private ArrayList<String> features = new ArrayList<String>();

	/**
	 * @return the clientCompany
	 */
	public ClientCompany getClientCompany() {
		return clientCompany;
	}

	/**
	 * @param clientCompany
	 *            the clientCompany to set
	 */
	public void setClientCompany(ClientCompany clientCompany) {
		this.clientCompany = clientCompany;
	}

	/**
	 * @return the features
	 */
	public ArrayList<String> getFeatures() {
		return features;
	}

	/**
	 * @param features
	 *            the features to set
	 */
	public void setFeatures(ArrayList<String> features) {
		this.features = features;
	}

}
