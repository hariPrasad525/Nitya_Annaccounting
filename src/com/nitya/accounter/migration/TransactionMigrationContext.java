package com.nitya.accounter.migration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class TransactionMigrationContext {
	private String identity;
	// OPT=ONE PER TRANSACTION;
	// OPDL=ONE PER DETAIL LINE;
	// W=WITH;
	// WO=WITH OUT;
	private JSONArray wDiscountOPTAndWTaxOPT = new JSONArray();
	private JSONArray wDiscountOPTAndWTaxOPDL = new JSONArray();
	private JSONArray wDiscountOPTAndWOTax = new JSONArray();

	private JSONArray wDiscountOPDLAndWTaxOPT = new JSONArray();
	private JSONArray wDiscountOPDLAndWTaxOPDL = new JSONArray();
	private JSONArray wDiscountOPDLAndWOTax = new JSONArray();

	private JSONArray wODiscountAndWTaxOPT = new JSONArray();
	private JSONArray wODiscountAndWTaxOPDL = new JSONArray();
	private JSONArray wODiscountAndWOTax = new JSONArray();
	// BasedOn CompanySettings we split Transactions into sub lists like
	// with tax,without discount etc. These childrens we are maintaining here
	private Map<String, Map<String, List<Long>>> childrensMap = new HashMap<String, Map<String, List<Long>>>();

	public TransactionMigrationContext(String identity) {
		this.identity = identity;
	}

	public String getIdentity() {
		return identity;
	}

	public JSONArray getwDiscountOPTAndWTaxOPT() {
		return wDiscountOPTAndWTaxOPT;
	}

	public JSONArray getwDiscountOPTAndWTaxOPDL() {
		return wDiscountOPTAndWTaxOPDL;
	}

	public JSONArray getwDiscountOPTAndWOTax() {
		return wDiscountOPTAndWOTax;
	}

	public JSONArray getwDiscountOPDLAndWTaxOPT() {
		return wDiscountOPDLAndWTaxOPT;
	}

	public JSONArray getwDiscountOPDLAndWTaxOPDL() {
		return wDiscountOPDLAndWTaxOPDL;
	}

	public JSONArray getwDiscountOPDLAndWOTax() {
		return wDiscountOPDLAndWOTax;
	}

	public JSONArray getwODiscountAndWTaxOPT() {
		return wODiscountAndWTaxOPT;
	}

	public JSONArray getwODiscountAndWTaxOPDL() {
		return wODiscountAndWTaxOPDL;
	}

	public JSONArray getwODiscountAndWOTax() {
		return wODiscountAndWOTax;
	}

	// ADD METHODS
	public void addwDiscountOPTAndWTaxOPT(JSONObject object) {
		wDiscountOPTAndWTaxOPT.put(object);
	}

	public void addwDiscountOPTAndWTaxOPDL(JSONObject object) {
		wDiscountOPTAndWTaxOPDL.put(object);
	}

	public void addwDiscountOPTAndWOTax(JSONObject object) {
		wDiscountOPTAndWOTax.put(object);
	}

	public void addwDiscountOPDLAndWTaxOPT(JSONObject object) {
		wDiscountOPDLAndWTaxOPT.put(object);
	}

	public void addwDiscountOPDLAndWTaxOPDL(JSONObject object) {
		wDiscountOPDLAndWTaxOPDL.put(object);
	}

	public void addwDiscountOPDLAndWOTax(JSONObject object) {
		wDiscountOPDLAndWOTax.put(object);
	}

	public void addwODiscountAndWTaxOPT(JSONObject object) {
		wODiscountAndWTaxOPT.put(object);
	}

	public void addwODiscountAndWTaxOPDL(JSONObject object) {
		wODiscountAndWTaxOPDL.put(object);
	}

	public void addwODiscountAndWOTax(JSONObject object) {
		wODiscountAndWOTax.put(object);
	}

	// HAS METHODS
	public boolean haswDiscountOPTAndWTaxOPT() {
		return getwDiscountOPTAndWTaxOPT().length() != 0;
	}

	public boolean haswDiscountOPTAndWTaxOPDL() {
		return getwDiscountOPTAndWTaxOPDL().length() != 0;
	}

	public boolean haswDiscountOPTAndWOTax() {
		return getwDiscountOPTAndWOTax().length() != 0;
	}

	public boolean haswDiscountOPDLAndWTaxOPT() {
		return getwDiscountOPDLAndWTaxOPT().length() != 0;
	}

	public boolean haswDiscountOPDLAndWTaxOPDL() {
		return getwDiscountOPDLAndWTaxOPDL().length() != 0;
	}

	public boolean haswDiscountOPDLAndWOTax() {
		return getwDiscountOPDLAndWOTax().length() != 0;
	}

	public boolean haswODiscountAndWTaxOPT() {
		return getwODiscountAndWTaxOPT().length() != 0;
	}

	public boolean haswODiscountAndWTaxOPDL() {
		return getwODiscountAndWTaxOPDL().length() != 0;
	}

	public boolean haswODiscountAndWOTax() {
		return getwODiscountAndWOTax().length() != 0;
	}

	public Map<String, Map<String, List<Long>>> getChildrensMap() {
		return childrensMap;
	}

	public void setChildrensMap(
			Map<String, Map<String, List<Long>>> childrensMap) {
		this.childrensMap = childrensMap;
	}
}
