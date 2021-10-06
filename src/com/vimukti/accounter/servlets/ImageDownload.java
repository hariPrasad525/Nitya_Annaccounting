package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;

public class ImageDownload extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException {

		try {

			String fileName = req.getParameter("fid");
			String pid = req.getParameter("pid");
			String aid = req.getParameter("aid");
			String companyName = req.getParameter("cname");

			if (pid == null || aid == null || companyName == null) {
				return;
			}

			String companyFolder = ServerConfiguration
					.getAttachmentsDir(companyName);
			File file = new File(companyFolder + File.separator
					+ "Inline Images" + File.separator + pid + aid);
			if (!file.exists()) {
				return;
			}

			InputStream in = new FileInputStream(file);

			response.setHeader("Content-Disposition", "inline; filename=\""
					+ "test.jpg" + "\"");

			byte[] buff = new byte[1054];
			int read = 0;
			do {
				read = in.read(buff);
				if (read != -1) {
					response.getOutputStream().write(buff, 0, read);
				}
			} while (read != -1);

			response.getOutputStream().flush();
			response.getOutputStream().close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
