package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * Adjustment reason POJO.
 * 
 * @author Srikanth.J
 * 
 */
public class AdjustmentReason extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String section;
	private String name;

	public AdjustmentReason() {
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.adjustmentreason());

		if (this.section != null) {
			w.put(messages.sectionhashnumber(1), this.section);
		}

		if (this.name != null) {
			w.put(messages.name(), this.name);
		}
	}

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub

	}

}
