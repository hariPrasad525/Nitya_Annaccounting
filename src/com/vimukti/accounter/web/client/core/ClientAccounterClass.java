package com.vimukti.accounter.web.client.core;

public class ClientAccounterClass implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private String className;

	private int version;

	private long parent;

	private int depth;

	private int parentCount;

	private String path;

	private String modifiedName;

	@Override
	public String getName() {
		return getClassName();
	}

	@Override
	public String getDisplayName() {
		return getClassName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ACCOUNTER_CLASS;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public String getClassName() {
		if (className == null) {
			return "";
		}
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ClientAccounterClass)) {
			return false;
		}

		ClientAccounterClass accounterClass = (ClientAccounterClass) obj;
		return accounterClass.getID() == this.getID()
				&& accounterClass.getClassName().equals(this.className);
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getParentCount() {
		return parentCount;
	}

	public void setParentCount(int parentCount) {
		this.parentCount = parentCount;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getModifiedName() {
		return modifiedName;
	}

	public void setModifiedName(String modifiedName) {
		this.modifiedName = modifiedName;
	}
}
