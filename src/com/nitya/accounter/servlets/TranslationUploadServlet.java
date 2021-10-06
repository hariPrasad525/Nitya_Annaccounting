package com.nitya.accounter.servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.main.MessageLoader;
import com.nitya.accounter.main.ServerConfiguration;

public class TranslationUploadServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String TRANSLATION_UPLOAD = "/WEB-INF/translate/upload.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("isValid", Boolean.FALSE);
		dispatch(req, resp, TRANSLATION_UPLOAD);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String password = req.getParameter("password1");
		String isSubmittingFile = req.getParameter("isSubmittingFile");
		if (isSubmittingFile == null || isSubmittingFile.isEmpty()
				|| !Boolean.valueOf(isSubmittingFile)) {
			String adminPassword = ServerConfiguration.getAdminPassword();
			if (password == null || (password.isEmpty())
					|| !(password.equals(adminPassword))) {
				req.setAttribute("message", "wrong password");
				req.setAttribute("isValid", Boolean.FALSE);
				dispatch(req, resp, TRANSLATION_UPLOAD);
			} else if (password.equals(adminPassword)) {
				req.setAttribute("isValid", Boolean.TRUE);
				dispatch(req, resp, TRANSLATION_UPLOAD);
			}
			return;
		}
		String contentType = req.getContentType();
		byte dataBytes[] = null;
		if ((contentType != null)
				&& (contentType.indexOf("multipart/form-data") >= 0)) {
			DataInputStream in = new DataInputStream(req.getInputStream());
			int formDataLength = req.getContentLength();
			dataBytes = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength) {
				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
		}
		String file = new String(dataBytes);
		String saveFile = file.substring(file.indexOf("filename=\"") + 10);
		saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
		saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,
				saveFile.indexOf("\""));
		int lastIndex = contentType.lastIndexOf("=");
		String boundary = contentType.substring(lastIndex + 1,
				contentType.length());
		int pos;
		pos = file.indexOf("filename=\"");
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		int boundaryLocation = file.indexOf(boundary, pos) - 4;
		int startPos = (file.substring(0, pos)).length();
		int endPos = (file.substring(0, boundaryLocation)).length();
		saveFile = System.getProperty("java.io.tmpdir") + saveFile;
		File ff = new File(saveFile);
		FileOutputStream fileOut = new FileOutputStream(ff);
		fileOut.write(dataBytes, startPos, (endPos - startPos));
		fileOut.flush();
		fileOut.close();
		try {
			new MessageLoader(new FileInputStream(ff)).loadMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Completed the Inseting of messages..");

	}
}
