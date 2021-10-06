package com.vimukti.accounter.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParseFile {
	public String fileName;
	public Map<String, String> messages = new HashMap<String, String>();

	public ParseFile(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * For parsing .xlsx
	 * 
	 * @return
	 */
	public Map<String, String> parse() {

		try {

			FileInputStream fileInputStream = new FileInputStream(fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			XSSFSheet worksheet = workbook.getSheet("Sheet1");
			Iterator<Row> rows = worksheet.iterator();
			String key = null;
			String value = null;
			while (rows.hasNext()) {
				XSSFRow row = ((XSSFRow) rows.next());
				XSSFCell cellA1 = row.getCell((short) 0);
				String line = cellA1.getStringCellValue();
				if (!line.startsWith("#")) {
					StringTokenizer stringTokenizer = new StringTokenizer(line,
							"=");
					while (stringTokenizer.hasMoreElements()) {
						if (stringTokenizer.countTokens() == 2) {
							key = stringTokenizer.nextToken();
							value = stringTokenizer.nextToken();
							messages.put(key, value);
						} else {
							key = stringTokenizer.nextToken();
							messages.put(key, "");
						}

					}

				}
			}
		} catch (Exception e) {
			parseXLSFiles();

		}

		return messages;
	}

	/**
	 * For parsing .xls files
	 * 
	 * @return
	 */
	private Map<String, String> parseXLSFiles() {
		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheet("Sheet1");
			Iterator<Row> rows = worksheet.iterator();
			String key = null;
			String value = null;
			while (rows.hasNext()) {
				HSSFRow row = ((HSSFRow) rows.next());
				HSSFCell cellA1 = row.getCell((short) 0);
				String line = cellA1.getStringCellValue();
				if (!line.startsWith("#")) {
					StringTokenizer stringTokenizer = new StringTokenizer(line,
							"=");
					while (stringTokenizer.hasMoreElements()) {
						if (stringTokenizer.countTokens() == 2) {
							key = stringTokenizer.nextToken();
							value = stringTokenizer.nextToken();
							messages.put(key, value);
						} else {
							key = stringTokenizer.nextToken();
							messages.put(key, "");
						}
						System.out.println("Key:" + key + "__" + "Value:"
								+ value);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Invalid file name");
		} catch (IOException e) {
			System.out.println("Invalid name");

		}

		return messages;
	}

}
