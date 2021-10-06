package com.vimukti.accounter.web.client.ui.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.InvitableUser;

public class InviteEmailCombo extends FormItem<String> {

	private ListBox emailsBox;
	private List<String> emails = new ArrayList<String>();

	public InviteEmailCombo(String title, String styleName) {
		super(title, styleName);
		emailsBox = new ListBox();
		this.add(emailsBox);
	}

	public void setValue(String email) {
		if (email == null) {
			return;
		}
		int index = setIndexByEmail(email);
		if (index == -1) {
			emailsBox.addItem(email);
			index = 0;
		}
		emailsBox.setSelectedIndex(index);
	}

	private int setIndexByEmail(String email) {
		for (int i = 0; i < emails.size(); i++) {
			if (emails.get(i).equals(email)) {
				return i;
			}
		}
		return -1;
	}

	public String getValue() {
		if (emailsBox.getSelectedIndex() < 0) {
			return null;
		}
		return emailsBox.getValue(emailsBox.getSelectedIndex());
	}

	public void setInvitableUser(Set<InvitableUser> usersList) {
		for (InvitableUser invitableUser : usersList) {
			String email = invitableUser.getEmail();
			emails.add(email);
			emailsBox.addItem(email);
		}
	}

	public void addChangeHandler(ChangeHandler changeHandler) {
		emailsBox.addChangeHandler(changeHandler);
	}

	@Override
	public Widget getMainWidget() {
		return emailsBox;
	}

}
