package com.nitya.accounter.server.imports;

import java.util.List;
import java.util.Map;

import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ImportField;
import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public interface Importer<T extends IAccounterCore> {

	/**
	 * Returns Output Object that is Generated after Matching
	 * 
	 * Warning: This Method should me called after loading the Data
	 * 
	 * @return
	 */
	public T getData();

	/**
	 * 
	 * Loads the Data(ColumnName,ColumnValue) from File
	 * 
	 * @param data
	 */
	public List<AccounterException> loadData(Map<String, String> data);

	/**
	 * Updates the All Fields in the Importer with CSVColumnName
	 * 
	 * @param importMap
	 *            <FieldName,CSVColumnName>
	 */
	public void updateFields(Map<String, String> importMap);

	/**
	 * Returns the List of Fields that importer have
	 * 
	 * @return
	 */
	public List<ImportField> getFields();

	/**
	 * Date Format of the Imported CSV File
	 * 
	 * @param dateFormate
	 */
	public void setDateFormat(String dateFormat);

	/**
	 * Setting the companyId
	 * 
	 * @param CompanyId
	 */
	public void setCompanyId(long CompanyId);

}
