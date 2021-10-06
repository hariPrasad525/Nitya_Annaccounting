package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SalesByLocationSummary extends BaseReport implements
		IsSerializable, Serializable {

	private static final long serialVersionUID = 1L;

	private double total;

	private String locationName;

	private long transactionId;

	private int type;

	private long parentId;

	private long classId;

	private List<String> parents;

	private int depth;

	private Map<String, Integer> depthsByName;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public List<String> getParents() {
		return parents;
	}

	public void setParents(List<String> parents) {
		this.parents = parents;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Map<String, Integer> getDepthsByName() {
		return depthsByName;
	}

	public void setDepthsByName(Map<String, Integer> depthsByName) {
		this.depthsByName = depthsByName;
	}

}
