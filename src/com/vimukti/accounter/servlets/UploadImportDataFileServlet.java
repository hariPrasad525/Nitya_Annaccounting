package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import au.com.bytecode.opencsv.CSVReader;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.vimukti.accounter.main.ServerConfiguration;

public class UploadImportDataFileServlet extends BaseServlet {

	/**
	 * used to upload the Statement file and to store the statement records in
	 * that file
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject object = new JSONObject();
		String[] headers = null;
		boolean isHeader = true;
		int noOfLines = 0;
		try {
			HttpSession httpSession = request.getSession();
			Long companyID = (Long) httpSession.getAttribute(COMPANY_ID);
			if (companyID == null)
				return;

			MultipartRequest multi = new MultipartRequest(request,
					ServerConfiguration.getTmpDir(), 50 * 1024 * 1024,
					"ISO-8859-1", new DefaultFileRenamePolicy());

			Enumeration<?> files = multi.getFileNames();
			while (files.hasMoreElements()) {
				String fileID = (String) files.nextElement();
				File file = multi.getFile(fileID);
				if (file != null) {
					CSVReader reader = new CSVReader(new FileReader(file));
					JSONObject allRecords = new JSONObject();
					String nextLine[];
					while ((nextLine = reader.readNext()) != null) {
						if (isHeader) {
							headers = nextLine;
							isHeader = false;
						} else {
							if (nextLine.length == headers.length) {
								noOfLines++;
								for (int i = 0; i < nextLine.length; i++) {
									String value = nextLine[i].trim()
											.replaceAll("\"", "");
									if (!allRecords.has(headers[i])) {
										JSONArray array = new JSONArray();
										array.put(value);
										allRecords.put(headers[i], array);
									} else {
										JSONArray array2 = allRecords
												.getJSONArray(headers[i]);
										array2.put(value);
									}
								}
							}
						}
					}
					object.put("fileID", file.getAbsolutePath());
					object.put("first20Records", allRecords);
					object.put("noOfRows", noOfLines);
				}
			}
			String resp = object.toString();
			resp = resp.replace(">", "&gt;");
			resp = resp.replace("<", "&lt;");
			response.setHeader("Content-Type", "text/html");
			response.getWriter().print(resp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
