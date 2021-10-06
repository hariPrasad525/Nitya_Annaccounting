package com.nitya.accounter.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * the main purpose of this servlet is to create the cache file reading the
 * CacheFileNames.txt
 * 
 * @author vimukti8
 * 
 */
public class CacheFilenameCreatorServlet extends BaseServlet {

	/**
	 * change the content of the file of "config/CacheFileNames.txt" with the
	 * contents of accounter.client before submitting to server and change the
	 * version number i.e. first line of the file
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MAC_CACHE_FILE = "config/mac_cache.txt";
	private static final String IPAD_CACHE_FILE = "config/ipad_cache.txt";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		StringBuffer requestURL = req.getRequestURL();
		String[] split = requestURL.toString().split("/");
		String fileName = split[split.length - 1];
		File fileToRead = null;

		/**
		 * read the request url and decide which file we need to create
		 */
		if (fileName.equalsIgnoreCase("mac")) {
			fileToRead = new File(MAC_CACHE_FILE);
		} else if (fileName.equalsIgnoreCase("ipad")) {
			fileToRead = new File(IPAD_CACHE_FILE);
		} else {
			fileToRead = new File(MAC_CACHE_FILE);
		}

		FileReader fr = null;
		try {
			fr = new FileReader(fileToRead);
			BufferedReader br = new BufferedReader(fr);
			PrintWriter out = resp.getWriter();
			String line;
			do {
				line = br.readLine();
				if (line == null) {
					break;
				}
				out.println(line);
			} while (line != null);
			out.close();

		} catch (Exception e) {
			log(e.getLocalizedMessage());
		}

	}

}
