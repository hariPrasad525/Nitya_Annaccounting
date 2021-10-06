package com.nitya.accounter.servlets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nitya.accounter.main.ServerConfiguration;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class UploadStatementFileServlet extends BaseServlet {

	/**
	 * used to upload the Statement file and to store the statement records in
	 * that file
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONArray array = new JSONArray();
		String[] headers = null;
		boolean isHeader = true;
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
					DataInputStream in = new DataInputStream(
							new FileInputStream(file.getAbsolutePath()));
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));
					String strLine;
					while ((strLine = br.readLine()) != null) {
						JSONObject jsonObject = new JSONObject();
						String[] values = strLine.split(",");

						if (isHeader) {
							headers = values;
							isHeader = false;
						} else {
							if (values.length == headers.length) {
								for (int i = 0; i < values.length; i++) {
									String value = values[i].trim().replaceAll(
											"\"", "");

									jsonObject.put(headers[i], value);

								}
								array.put(jsonObject);
							}
						}
					}
				}
			}
			StringBuilder builder = new StringBuilder(array.toString());
			response.getWriter().print(builder);

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
