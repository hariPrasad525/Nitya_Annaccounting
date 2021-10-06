/*
 * package com.vimukti.accounter.web.client.ui.payroll;
 * 
 * import java.util.List;
 * 
 * import com.google.gwt.user.client.rpc.AsyncCallback; import
 * com.vimukti.accounter.web.client.Global; import
 * com.vimukti.accounter.web.client.core.AccounterCoreType; import
 * com.vimukti.accounter.web.client.core.ClientEmployeeCategory; import
 * com.vimukti.accounter.web.client.core.IAccounterCore; import
 * com.vimukti.accounter.web.client.core.Utility; import
 * com.vimukti.accounter.web.client.core.ValidationResult; import
 * com.vimukti.accounter.web.client.ui.Accounter; import
 * com.vimukti.accounter.web.client.ui.UIUtils; import
 * com.vimukti.accounter.web.client.ui.core.GroupDialog; import
 * com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler; import
 * com.vimukti.accounter.web.client.ui.core.InputDialog; import
 * com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;
 * 
 * public class EmployeeCategoryListDialog extends
 * GroupDialog<ClientEmployeeCategory> {
 * 
 * private InputDialog inputDlg; private ClientEmployeeCategory
 * employeeCategory; List<ClientEmployeeCategory> employeeCategories; private
 * GroupDialogButtonsHandler dialogButtonsHandler; private
 * List<ClientEmployeeCategory> list;
 * 
 * public EmployeeCategoryListDialog(String title, String descript) {
 * super(title, descript);
 * this.getElement().setId("EmployeeCategoryListDialog"); setWidth("380px");
 * initialise(); center(); initList(); }
 * 
 * private void initList() {
 * Accounter.createPayrollService().getEmployeeCategories( new
 * AsyncCallback<List<ClientEmployeeCategory>>() {
 * 
 * @Override public void onFailure(Throwable caught) { // TODO Auto-generated
 * method stub
 * 
 * }
 * 
 * @Override public void onSuccess(List<ClientEmployeeCategory> result) { list =
 * result; initGrid(list); } }); }
 * 
 * public void initialise() { this.button1.setVisible(true);
 * getGrid().setType(AccounterCoreType.EMPLOYEE_CATEGORY);
 * getGrid().addRecordClickHandler( new
 * GridRecordClickHandler<ClientEmployeeCategory>() {
 * 
 * @Override public boolean onRecordClick(ClientEmployeeCategory core, int
 * column) { enableEditRemoveButtons(true); return true; }
 * 
 * });
 * 
 * dialogButtonsHandler = new GroupDialogButtonsHandler() {
 * 
 * public void onCloseButtonClick() {
 * 
 * }
 * 
 * public void onFirstButtonClick() { showAddEditGroupDialog(null); }
 * 
 * public void onSecondButtonClick() {
 * showAddEditGroupDialog((ClientEmployeeCategory) getSelectedRecord()); }
 * 
 * public void onThirdButtonClick() { if (listGridView != null) {
 * deleteObject((IAccounterCore) listGridView.getSelection()); if
 * (employeeCategories == null) { enableEditRemoveButtons(false); } } }
 * 
 * };
 * 
 * addGroupButtonsHandler(dialogButtonsHandler); this.okbtn.setVisible(false); }
 * 
 * protected void showAddEditGroupDialog(ClientEmployeeCategory rec) {
 * employeeCategory = rec; inputDlg = new InputDialog(this,
 * messages.employeeCategory(), "", messages.employeeCategory()) { };
 * 
 * if (employeeCategory != null) { inputDlg.setTextItemValue(0,
 * employeeCategory.getName()); } inputDlg.show(); }
 * 
 * @Override public Object getGridColumnValue(ClientEmployeeCategory group, int
 * index) { switch (index) { case 0: if (group != null) return group.getName();
 * } return null; }
 * 
 * @Override public String[] setColumns() { return new String[] {
 * messages.name() }; }
 * 
 * @Override protected List<ClientEmployeeCategory> getRecords() { return list;
 * }
 * 
 * @Override protected boolean onOK() { if (inputDlg != null) { if
 * (employeeCategory != null) { editCustomerGroups(); inputDlg = null; } else {
 * createCustomerGroups(); inputDlg = null; } } return true; }
 * 
 * private void createCustomerGroups() { ClientEmployeeCategory employeeCategory
 * = new ClientEmployeeCategory();
 * employeeCategory.setName(inputDlg.getTextItems().get(0).getValue()
 * .toString());
 * 
 * saveOrUpdate(employeeCategory); list.add(employeeCategory); }
 * 
 * private void editCustomerGroups() {
 * employeeCategory.setName(inputDlg.getTextValueByIndex(0));
 * saveOrUpdate(employeeCategory); for (ClientEmployeeCategory category : list)
 * { if (category.getID() == employeeCategory.getID()) { int index =
 * list.indexOf(category); list.remove(category); list.add(index,
 * employeeCategory); break; } } }
 * 
 * @Override protected ValidationResult validate() { ValidationResult result =
 * new ValidationResult(); if (inputDlg != null) { String value =
 * inputDlg.getTextItems().get(0).getValue().toString(); ClientEmployeeCategory
 * employeeCategoryByName = Utility .getObjectByName(this.list,
 * UIUtils.toStr(value)); if (employeeCategoryByName != null) { if
 * ((employeeCategoryByName.getName().equalsIgnoreCase(value) ? true :
 * employeeCategoryByName == null)) { result.addError(this,
 * messages.alreadyExist()); } } } else { ClientEmployeeCategory
 * employeeCategoryByName2 = Utility .getObjectByName(this.list,
 * inputDlg.getTextItems().get(0) .getValue().toString()); if
 * (employeeCategoryByName2 != null) { result.addError(this,
 * messages.payeeGroupAlreadyExists(Global .get().Customer())); } } return
 * result; }
 * 
 * @Override public void setFocus() { // TODO Auto-generated method stub
 * 
 * }
 * 
 * @Override public String getHeaderStyle(int index) { switch (index) { case 0:
 * return "name";
 * 
 * default: break; } return null; }
 * 
 * @Override public String getRowElementsStyle(int index) { switch (index) {
 * case 0: return "name";
 * 
 * default: break; } return null; }
 * 
 * }
 */