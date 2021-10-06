package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.main.ServerConfiguration;

public class DownloadFileServlet extends BaseServlet {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	class FileInfo {
		boolean inline;

		public FileInfo(String filename2, int size, InputStream in2,
				boolean inline) {
			this.fileName = filename2;
			this.fileSize = size;
			this.in = in2;
			this.inline = inline;
		}

		String fileName;
		long fileSize;
		InputStream in;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException {
		try {
			Long companyId = (Long) req.getSession().getAttribute(COMPANY_ID);
			if (companyId == null) {
				return;
			}
			String fileName = req.getParameter("fileName");
			if (fileName == null
					|| req.getSession().getAttribute("identityID") == null) {
			}
			File file = new File(ServerConfiguration.getAttachmentsDir()
					+ File.separator + companyId + File.separator + fileName);
			if (!file.exists()) {
				return;
			}
			String type = "thumbnail";
			String destImagePath = "";
			File thumbnailFolder = new File(
					ServerConfiguration.getAttachmentsDir() + File.separator
							+ companyId, type);
			if (!thumbnailFolder.exists())
				thumbnailFolder.mkdir();
			destImagePath = thumbnailFolder.getAbsolutePath() + File.separator
					+ file.getName();
			File file2 = new File(destImagePath);
			File resize = file2;
			if (!file2.exists()) {
				try {
					// resize = resizeFile(companyId, fileName, file, response);
					resize = file;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/*
			 * if the file is not present before, the processed file will be
			 * sent to the client
			 */
			response.setContentLength((int) resize.length());
			// Open the file and output streams
			FileInputStream in = new FileInputStream(resize);
			OutputStream out = response.getOutputStream();
			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} finally {
		}
	}

	private File resizeFile(long companyID, String fileName, File file,
			HttpServletResponse response) throws Exception {
		Process proces;
		String type = "thumbnail";
		String destImagePath = "";
		File thumbnailFolder = new File(ServerConfiguration.getAttachmentsDir()
				+ File.separator + companyID, type);
		if (!thumbnailFolder.exists())
			thumbnailFolder.mkdir();
		destImagePath = thumbnailFolder.getAbsolutePath() + File.separator
				+ fileName;
		String command;
		String cmd;
		String os = System.getProperty("os.name");
		if (os.startsWith("Mac")) {
			String originalHeight = Utility.getImageProperty("height",
					file.getAbsolutePath());
			String originalWidth = Utility.getImageProperty("width",
					file.getAbsolutePath());
			String resizedImageSize = originalWidth + "X" + originalHeight;
			command = Utility.getCommandForMAC(type,
					resizedImageSize.split("X")[0],
					resizedImageSize.split("X")[1]);
			cmd = "sips " + command + file.getAbsolutePath() + " --out "
					+ destImagePath;
		} else {
			command = Utility.getCommandForType(type);
			cmd = "convert " + file.getAbsolutePath() + " " + command
					+ destImagePath;
		}
		proces = Runtime.getRuntime().exec(cmd);
		proces.waitFor();
		File f = new File(destImagePath);
		return f;
	}
}
