package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreateClassDialog;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ClassTable extends AbstractDropDownTable<ClientAccounterClass> {

	public ClassTable() {
		super(getClasses(), true);
	}

	private static List<ClientAccounterClass> getClasses() {
		ArrayList<ClientAccounterClass> accounterClasses = Accounter
				.getCompany().getaccounterClassesWithChilds();
		return accounterClasses;
	}

	private String getDisplayValueForCombo(ClientAccounterClass obj) {
		if (obj == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		ClientAccounterClass parentClass = obj;
		while (parentClass.getParent() != 0) {
			buffer.append(parentClass.getClassName());
			parentClass = Accounter.getCompany().getAccounterClass(
					parentClass.getParent());
			buffer.append(":");
		}
		buffer.append(parentClass.getClassName());
		buffer = getReverseBuffer(buffer.toString());
		return buffer.toString();
	}

	private StringBuffer getReverseBuffer(String actvalString) {
		String[] split = actvalString.split(":");
		StringBuffer buffer = new StringBuffer();
		for (int i = split.length - 1; i > 0; --i) {
			buffer.append(split[i]);
			buffer.append(':');
		}
		buffer.append(split[0]);
		return buffer;
	}

	@Override
	public List<ClientAccounterClass> getTotalRowsData() {
		return getClasses();
	}

	@Override
	protected ClientAccounterClass getAddNewRow() {
		ClientAccounterClass clientAccounterClass = new ClientAccounterClass();
		clientAccounterClass.setClassName(messages.comboDefaultAddNew("Class"));
		return clientAccounterClass;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientAccounterClass> column = new TextColumn<ClientAccounterClass>() {

			@Override
			public String getValue(ClientAccounterClass object) {
				return getDisplayName(object);
			}
		};
		this.addColumn(column);
		this.setColumnWidth(column, "100px");
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	private String getDisplayName(ClientAccounterClass code) {
		String name = code.getName();
		if (name == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(name);
		if (name.equals(messages.comboDefaultAddNew("Class"))) {
			return result.toString();
		}
		return getspaces(code);
	}

	/**
	 * 
	 * @param object
	 *            ClientAccounterClass
	 * @return {@link String}
	 */
	private String getspaces(ClientAccounterClass object) {
		StringBuffer buffer = new StringBuffer();
		if (object.getDepth() != 0) {
			for (int i = 0; i < object.getDepth(); i++) {
				buffer.append("     ");
			}
		}
		buffer.append(object.getClassName());
		return buffer.toString();
	}

	@Override
	protected boolean filter(ClientAccounterClass t, String string) {
		String[] split = string.split(":");
		return getDisplayName(t).trim().toLowerCase()
				.startsWith(split[split.length - 1]);
	}

	@Override
	protected String getDisplayValue(ClientAccounterClass value) {
		return getDisplayValueForCombo(value);
	}

	@Override
	protected void addNewItem(String text) {
		addNewItem("");
	}

	@Override
	protected void addNewItem() {
		if (Accounter.getCompany().getPreferences().isClassTrackingEnabled()) {
			CreateClassDialog classDialog = new CreateClassDialog(null,
					messages.createClass(), "");
			classDialog
					.addSuccessCallback(new ValueCallBack<ClientAccounterClass>() {

						@Override
						public void execute(
								final ClientAccounterClass accounterClass) {
							Accounter.createCRUDService().create(
									accounterClass, new AsyncCallback<Long>() {

										@Override
										public void onSuccess(Long result) {
											accounterClass.setID(result);
											Accounter
													.getCompany()
													.processUpdateOrCreateObject(
															accounterClass);
											selectRow(accounterClass);
										}

										@Override
										public void onFailure(Throwable caught) {
											caught.printStackTrace();
										}
									});
						}
					});
		}
	}

	@Override
	protected Class<?> getType() {
		return ClientAccounterClass.class;
	}
}
