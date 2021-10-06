package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CreateVendorGroupCommand extends AbstractCommand {
	private static final String VENDORGROUP_NAME = "VendorGroup Name";

	private ClientVendorGroup vendorGroup;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(VENDORGROUP_NAME, getMessages()
				.pleaseEnter(getMessages().payeeGroup(Global.get().Vendor())),
				getMessages().payeeGroup(Global.get().vendor()), false, true));

	}

	@Override
	protected String getDeleteCommand(Context context) {
		long id = vendorGroup.getID();
		return id != 0 ? "deleteVendorGroup " + id : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages()
								.selectATransactionToUpdate(
										getMessages().payeeGroup(
												Global.get().Vendor())));
				return "vendorGroups";
			}
			ClientVendorGroup customerGroupByName = CommandUtils
					.getVendorGroupByName(context.getCompany(), string);
			if (customerGroupByName == null) {
				addFirstMessage(
						context,
						getMessages()
								.selectATransactionToUpdate(
										getMessages().payeeGroup(
												Global.get().Vendor())));
				return "vendorGroups " + string.trim();
			}
			vendorGroup = customerGroupByName;
			get(VENDORGROUP_NAME).setValue(vendorGroup.getName());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(VENDORGROUP_NAME).setValue(string);
			}
			vendorGroup = new ClientVendorGroup();
		}
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		vendorGroup.setName((String) get(VENDORGROUP_NAME).getValue());
		create(vendorGroup, context);
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		if (vendorGroup.getID() == 0) {
			return "Creating new Vendor Group... ";
		} else {
			return "Updating Vendor Group " + vendorGroup.getDisplayName();
		}
	}

	@Override
	protected String getDetailsMessage() {
		if (vendorGroup.getID() == 0) {
			return getMessages().readyToCreate(
					getMessages().payeeGroup(Global.get().vendor()));
		} else {
			return getMessages().readyToUpdate(
					getMessages().payeeGroup(Global.get().vendor()));
		}
	}

	@Override
	protected void setDefaultValues(Context context) {

	}

	@Override
	public String getSuccessMessage() {
		if (vendorGroup.getID() == 0) {
			return getMessages().createSuccessfully(
					getMessages().payeeGroup(Global.get().vendor()));
		} else {
			return getMessages().updateSuccessfully(
					getMessages().payeeGroup(Global.get().vendor()));
		}
	}
}
