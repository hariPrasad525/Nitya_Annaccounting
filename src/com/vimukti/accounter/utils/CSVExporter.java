package com.vimukti.accounter.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.web.client.Global;

public class CSVExporter<T> {

	private ICSVExportRunner<T> runner;

	public CSVExporter(ICSVExportRunner<T> runner) {
		this.runner = runner;
	}

	public String export(List<T> list) throws IOException {

		// take runner, create file in temp folder with random string,
		// fill data and return file name.
		String createRandomFileName = SecureUtils.createRandomFileName();
		File csvfile = new File(ServerConfiguration.getTmpDir(),
				createRandomFileName);
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				csvfile), "UTF-8");

		String[] columns = runner.getColumns();
		if (list.isEmpty()) {
			out.write(Global.get().messages().noRecordsToShow());

		} else {
			// For headers
			for (String columnValue : columns) {
				out.write(columnValue);
				out.write(",");
			}
			out.write("\n");
			// For body

			for (T t : list) {
				for (int i = 0; i < columns.length; i++) {
					String columnValue = runner.getColumnValue(t, i);
					out.write(columnValue);
					out.write(",");
				}
				out.write("\n");
			}
		}
		out.flush();
		out.close();
		return createRandomFileName;
	}
}
