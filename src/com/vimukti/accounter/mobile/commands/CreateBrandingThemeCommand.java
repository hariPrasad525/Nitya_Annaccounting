package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;

public class CreateBrandingThemeCommand extends AbstractCommand {

	private List<String> fontList;
	private ArrayList<String> fontSizeArrayList;
	ClientBrandingTheme brandingTheme;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(getMessages().name()).setDefaultValue(getMessages().standard());
		get(getMessages().pageSize()).setDefaultValue(getMessages().usLetter());
		get(getMessages().measure()).setDefaultValue(getMessages().cm());
		get(getMessages().topMargin()).setDefaultValue(1.35);
		get(getMessages().bottomMargin()).setDefaultValue(1.0);
		get(getMessages().addressPadding()).setDefaultValue(1.0);
		get(getMessages().font()).setDefaultValue(fontList.get(0));
		get(getMessages().fontSize()).setDefaultValue(fontSizeArrayList.get(0));
		get(getMessages().overdueInvoiceTitle()).setDefaultValue(
				getMessages().invoice());
		get(getMessages().creditNoteTitle()).setDefaultValue(
				getMessages().credit());
		get(getMessages().statementTitle()).setDefaultValue(
				getMessages().statement());
		get(getMessages().showTaxNumber()).setDefaultValue(
				getMessages().taxNumber() + getMessages().selected());
		get(getMessages().showColumnHeadings()).setDefaultValue(
				getMessages().showColumnHeadings()
						+ getMessages().notSpecified());
		get(getMessages().showUnitPrice()).setDefaultValue(
				getMessages().showUnitPrice() + getMessages().selected());
		get(getMessages().showTaxColumn()).setDefaultValue(
				getMessages().showTaxColumn() + getMessages().selected());
		get(getMessages().showRegisteredAddress()).setDefaultValue(
				getMessages().showRegisteredAddress()
						+ getMessages().selected());
		get(getMessages().invoiceTemplete()).setDefaultValue(
				getTempletes().get(0));
		get(getMessages().creditNoteTemplete()).setDefaultValue(
				getTempletes().get(0));
		get(getMessages().paypalEmailHtml()).setDefaultValue("");
		get(getMessages().termsLabel()).setDefaultValue("");
		get(getMessages().contactDetailsHtml()).setDefaultValue("");
	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(getMessages().name(), getMessages()
				.pleaseEnter(getMessages().name()), getMessages().name(),
				false, true));
		list.add(new BooleanRequirement(getMessages().pageSize(), true) {

			@Override
			protected String getTrueString() {
				return "A4";
			}

			@Override
			protected String getFalseString() {
				return getMessages().usLetter();
			}
		});
		list.add(new BooleanRequirement(getMessages().measure(), true) {

			@Override
			protected String getTrueString() {
				return getMessages().cm();
			}

			@Override
			protected String getFalseString() {
				return getMessages().inch();
			}
		});
		list.add(new NumberRequirement(getMessages().topMargin(), getMessages()
				.pleaseEnter(getMessages().topMargin()), getMessages()
				.topMargin(), true, true));
		list.add(new NumberRequirement(getMessages().bottomMargin(),
				getMessages().pleaseEnter(getMessages().bottomMargin()),
				getMessages().bottomMargin(), true, true));
		list.add(new NumberRequirement(getMessages().addressPadding(),
				getMessages().pleaseEnter(getMessages().addressPadding()),
				getMessages().addressPadding(), true, true));

		list.add(new StringListRequirement(getMessages().font(), getMessages()
				.pleaseSelect(getMessages().font()), getMessages().font(),
				true, true, new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {

					}
				}) {

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSelectString() {
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				String fontNameArray[] = new String[] { "Arial", "Calibri",
						"Cambria", "Georgia", "Myriad", "Tahoma",
						"Times New Roman", "Trebuchet" };
				fontList = new ArrayList<String>();
				Collections.addAll(fontList, fontNameArray);
				return fontList;
			}

			@Override
			protected String getEmptyString() {
				return "";
			}
		});
		list.add(new StringListRequirement(getMessages().fontSize(),
				getMessages().pleaseSelect(getMessages().fontSize()),
				getMessages().fontSize(), true, true,
				new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSelectString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				String[] fontSizeArray = new String[] {
						getMessages().pointnumber(8),
						getMessages().pointnumber(9),
						getMessages().pointnumber(10),
						getMessages().pointnumber(11) };
				fontSizeArrayList = new ArrayList<String>();
				Collections.addAll(fontSizeArrayList, fontSizeArray);
				return fontSizeArrayList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
		list.add(new StringRequirement(getMessages().overdueInvoiceTitle(),
				getMessages().pleaseEnter(getMessages().overdueInvoiceTitle()),
				getMessages().overdueInvoiceTitle(), true, true));
		list.add(new StringRequirement(getMessages().creditNoteTitle(),
				getMessages().pleaseEnter(getMessages().creditNoteTitle()),
				getMessages().creditNoteTitle(), true, true));
		list.add(new StringRequirement(getMessages().statementTitle(),
				getMessages().pleaseEnter(getMessages().statementTitle()),
				getMessages().statementTitle(), true, true));
		list.add(new StringRequirement(getMessages().paypalEmailHtml(),
				getMessages().pleaseEnter(getMessages().paypalEmailHtml()),
				getMessages().paypalEmailHtml(), true, true));
		list.add(new StringRequirement(getMessages().termsLabel(),
				getMessages().pleaseEnter(getMessages().termsLabel()),
				getMessages().termsLabel(), true, true));
		list.add(new StringRequirement(getMessages().contactDetailsHtml(),
				getMessages().pleaseEnter(getMessages().contactDetailsHtml()),
				getMessages().contactDetailsHtml(), true, true));
		list.add(new BooleanRequirement(getMessages().showTaxNumber(), true) {

			@Override
			protected String getTrueString() {
				return getMessages().taxNumber() + getMessages().selected();
			}

			@Override
			protected String getFalseString() {
				return getMessages().taxNumber() + getMessages().notSpecified();
			}
		});
		list.add(new BooleanRequirement(getMessages().showColumnHeadings(),
				true) {

			@Override
			protected String getTrueString() {
				return getMessages().showColumnHeadings()
						+ getMessages().selected();
			}

			@Override
			protected String getFalseString() {
				return getMessages().showColumnHeadings()
						+ getMessages().notSpecified();
			}
		});
		list.add(new BooleanRequirement(getMessages().showUnitPrice(), true) {

			@Override
			protected String getTrueString() {
				return getMessages().showUnitPrice() + getMessages().selected();
			}

			@Override
			protected String getFalseString() {
				return getMessages().showUnitPrice()
						+ getMessages().notSpecified();
			}
		});
		list.add(new BooleanRequirement(getMessages().showTaxColumn(), true) {

			@Override
			protected String getTrueString() {
				return getMessages().showTaxColumn() + getMessages().selected();
			}

			@Override
			protected String getFalseString() {
				return getMessages().showTaxColumn()
						+ getMessages().notSpecified();
			}
		});
		list.add(new BooleanRequirement(getMessages().showRegisteredAddress(),
				true) {

			@Override
			protected String getTrueString() {
				return getMessages().showRegisteredAddress()
						+ getMessages().selected();
			}

			@Override
			protected String getFalseString() {
				return getMessages().showRegisteredAddress()
						+ getMessages().notSpecified();
			}
		});

		list.add(new StringListRequirement(getMessages().invoiceTemplete(),
				getMessages().pleaseSelect(getMessages().invoiceTemplete()),
				getMessages().invoiceTemplete(), true, true,
				new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {

					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				return getTempletes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(getMessages().creditNoteTemplete(),
				getMessages().pleaseSelect(getMessages().creditNoteTemplete()),
				getMessages().creditNoteTemplete(), true, true,
				new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {

					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				return getTempletes();
			}

			@Override
			protected String getEmptyString() {
				return "";
			}
		});
	}

	public ArrayList<String> getTempletes() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("Classic Template");
		list.add("Modern Template");
		list.add("Plain Template");
		list.add("Professional Template");
		return list;
	}

	private int getPageSize() {
		String value = get(getMessages().pageSize()).getValue();
		if (value.equals("A4")) {
			return 1;
		} else {
			return 2;
		}
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		brandingTheme = new ClientBrandingTheme();
		brandingTheme.setThemeName(get(getMessages().name()).getValue()
				.toString());
		brandingTheme.setPageSizeType(getPageSize());
		brandingTheme.setTopMargin((Double) get(getMessages().topMargin())
				.getValue());
		brandingTheme
				.setBottomMargin((Double) get(getMessages().bottomMargin())
						.getValue());
		brandingTheme.setBottomMargin((Double) get(
				getMessages().addressPadding()).getValue());
		return null;
	}

}
