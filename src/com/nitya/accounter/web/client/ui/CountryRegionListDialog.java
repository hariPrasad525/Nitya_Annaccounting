package com.nitya.accounter.web.client.ui;
//package com.nitya.accounter.web.client.ui;
//
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.DialogBox;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.nitya.accounter.web.client.ui.forms.DynamicForm;
//import com.nitya.accounter.web.client.ui.forms.TextItem;
//import com.nitya.accounter.web.client.ui.grids.DialogGrid;
//
///**
// * 
// * @author Mandeep Singh
// * 
// */
//
//public class CountryRegionListDialog extends DialogBox {
//	private DialogGrid grid;
//	private Button addGroupButt, editGroupButt, remGroupButt;
//	private Button helpButt, closeButt;
//	private String[] typeRecords = { messages.us(),
//			messages.india() };
//
//	public CountryRegionListDialog() {
//		setModal(true);
//		// setShowModalMask(true);
//		// setModalMaskOpacity(10);
//		// setShowEdges(false);
//		// setHeaderStyle("widget");
//		// setBackgroundColor("white");
//		// setBorder("4px solid #595959");
//		createControls();
//
//	}
//
//	private void createControls() {
//		Label lab1 = new Label(messages.countryListLabel());
//		// lab1.setAutoFit(true);
//		// lab1.setWrap(false);
//
//		Label lab2 = new Label(messages.addCountryLabel());
//		// lab2.setAutoFit(true);lab2.setBackgroundColor("green");
//		lab2.setHeight("1px");
//		// lab2.setOverflow(Overflow.VISIBLE);
//		// lab2.setWrap(false);
//		lab2.setWidth("100%");
//
//		grid = new DialogGrid(false);
//		// typeGrid.setAutoFitData(Autofit.BOTH);
//		// typeGrid.setOverflow(Overflow.SCROLL);
//		grid.setSize("100%", "100%");
//		// grid.setShowAllRecords(true);
//		grid.addColumns(new String[] { messages.country() });
//		// ListGridField nameField = new ListGridField("name",
//		// "<center><b>Country/Region</b></center>");
//		// businessTypeField.setAlign(Alignment.CENTER);
//		// grid.setFields(nameField);
//		// grid.setCanResizeFields(true);
//		createListGridRecords(typeRecords);
//
//		addGroupButt = new Button(messages.add());
//		addGroupButt.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				showAddEditGroupDialog();
//			}
//		});
//		editGroupButt = new Button(messages.edit());
//		remGroupButt = new Button(messages.remove());
//
//		helpButt = new Button(messages.help());
//		closeButt = new Button(messages.close());
//
//		StyledPanel helpHLay = new StyledPanel();
//		// helpHLay.setAlign(Alignment.LEFT);
//		helpHLay.add(helpButt);
//		StyledPanel closeHLay = new StyledPanel();
//		// closeHLay.setAlign(Alignment.RIGHT);
//		closeHLay.add(closeButt);
//		StyledPanel buttHLay = new StyledPanel();
//		buttHLay.setSize("100%", "10%");
//		buttHLay.add(helpHLay);
//		buttHLay.add(closeHLay);
//
//		StyledPanel buttVLay = new StyledPanel();
//		// buttVLay.setMembersMargin(5);
//		buttVLay.add(addGroupButt);
//		buttVLay.add(editGroupButt);
//		buttVLay.add(remGroupButt);
//
//		StyledPanel groupEditHLay = new StyledPanel();
//		// groupEditHLay.setSize("100%", "*");
//		groupEditHLay.add(grid);
//		groupEditHLay.add(buttVLay);
//
//		closeButt.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				removeFromParent();
//			}
//		});
//
//		StyledPanel MVLay = new StyledPanel();
//		MVLay.setSize("100%", "100%");
//		MVLay.add(lab1);
//		MVLay.add(lab2);
//		MVLay.add(groupEditHLay);
//		MVLay.add(buttHLay);
//
//		add(MVLay);
//		setWidth("450px");
//		show();
//	}
//
//	private void createListGridRecords(String[] records) {
//		for (Object obj : records) {
//			grid.addData((IsSerializable) obj);
//		}
//	}
//
//	private void showAddEditGroupDialog() {
//		DialogBox dlg = new DialogBox();
//		dlg.setTitle(messages.country());
//		dlg.setModal(true);
//		DynamicForm form = new DynamicForm();
//		// form.setSize("100%", "*");
//		TextItem nameText = new TextItem(messages.countryName());
//		// nameText.setWrapTitle(false);
//		nameText.setRequired(true);
//		form.setFields(nameText);
//
//		TextItem a3Text = new TextItem(messages.a3Code());
//		a3Text.setColSpan(1);
//		TextItem a2Text = new TextItem(messages.a2Code());
//		a2Text.setColSpan(1);
//		TextItem isoText = new TextItem(messages.isoCode());
//		isoText.setColSpan(3);
//
//		DynamicForm codeForm = new DynamicForm();
//		codeForm.setIsGroup(true);
//		codeForm.setGroupTitle(messages.countryCode());
//		// codeForm.setWrapItemTitles(false);
//		codeForm.setWidth("100%");
//		codeForm.setNumCols(4);
//		codeForm.setFields(a3Text, a2Text, isoText);
//
//		Button helpButt = new Button(messages.help());
//		// helpButt.setAutoFit(true);
//		StyledPanel helpHLay = new StyledPanel();
//		helpHLay.add(helpButt);
//		helpHLay.setWidth("50%");
//		Button okButt = new Button(messages.ok());
//		// okButt.setAutoFit(true);
//		Button canButt = new Button(messages.cancel());
//		// canButt.setAutoFit(true);
//
//		StyledPanel buttHLay = new StyledPanel();
//		// buttHLay.setAlign(Alignment.RIGHT);
//		// buttHLay.setSize("100%", "*");
//		// buttHLay.setMembersMargin(5);
//		// buttHLay.setLeft(20);
//		buttHLay.add(helpHLay);
//		buttHLay.add(okButt);
//		buttHLay.add(canButt);
//		canButt.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				removeFromParent();
//			}
//
//		});
//
//		StyledPanel mainVLay = new StyledPanel();
//		// mainVLay.setTop(50);
//		mainVLay.setSize("100%", "100%");
//		// mainVLay.setMembers(form, codeForm, buttHLay);
//		dlg.add(mainVLay);
//		dlg.setSize("450px", "300px");
//		dlg.show();
//	}
//	// FinanceApplication.constants()
//	// .manageCountryRegionList()
//
// }
