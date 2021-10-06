package com.nitya.accounter.servlets;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.utils.SecureUtils;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class UploadAttachmentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3451003399754758827L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			MultipartRequest multi = new MultipartRequest(request,
					ServerConfiguration.getTmpDir(), 50 * 1024 * 1024,
					"ISO-8859-1", new DefaultFileRenamePolicy());

			response.setContentType("text/html");
			String attachmentId = SecureUtils.createID();
			Enumeration<?> files = multi.getFileNames();
			String fileID = (String) files.nextElement();
			File file = multi.getFile(fileID);
			if (file != null) {
				long length = file.length();
				response.getWriter().print(attachmentId + "#" + length);
			} else {
				response.getWriter().print("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
