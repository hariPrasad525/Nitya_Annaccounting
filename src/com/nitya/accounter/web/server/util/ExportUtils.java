package com.nitya.accounter.web.server.util;

import java.io.IOException;
import java.io.OutputStream;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.IContext;

public class ExportUtils {

	public static void exportToPDF(IXDocReport report, IContext context,
			OutputStream out, String srcFile) throws XDocConverterException,
			XDocReportException, IOException {

		Options options = Options.getTo(ConverterTypeTo.PDF);
		if (srcFile != null && srcFile.endsWith(".odt")) {
			options = options.via(ConverterTypeVia.ODFDOM);
		} else {
			options = options.via(ConverterTypeVia.XWPF);
		}

		report.convert(context, options, out);

	}

}
