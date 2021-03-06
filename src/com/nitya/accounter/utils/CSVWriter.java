package com.nitya.accounter.utils;

import java.io.PrintWriter;
import java.io.Writer;

public class CSVWriter {

	/**
	 * Constructor
	 * 
	 * @param pw
	 *            PrintWriter where fields will be written.
	 * @param forceQuotes
	 *            true if you want all fields surrounded in quotes, whether or
	 *            not they contain commas, quotes or spaces.
	 * @param separator
	 *            field separator character, usually ',' in North America, ';'
	 *            in Europe and sometimes '\t' for tab.
	 * @param lineSeparator
	 *            gives the delimiter for the line; is per default set to the
	 *            system property 'line.separator'
	 */
	public CSVWriter(PrintWriter pw, boolean forceQuotes, char separator,
			String lineSeparator) {
		this.pw = pw;
		this.forceQuotes = forceQuotes;
		this.separator = separator;
		this.comment = "# ";
		this.lineSeparator = lineSeparator;
	} // end of CSVWriter

	public CSVWriter(Writer w, boolean forceQuotes, char separator,
			String lineSeparator) {
		this(new PrintWriter(w), forceQuotes, separator, lineSeparator);
	}

	/**
	 * Constructor with default field separator ','.
	 * 
	 * @param pw
	 *            PrintWriter where fields will be written.
	 */
	public CSVWriter(PrintWriter pw) {
		this.pw = pw;
		this.forceQuotes = false;
		this.separator = ',';
		this.comment = "# ";
		this.lineSeparator = System.getProperty("line.separator");
	} // end of CSVWriter

	public CSVWriter(Writer w) {
		this(new PrintWriter(w));
	}

	/**
	 * Constructor with default field separator ','.
	 * 
	 * @param pw
	 *            PrintWriter where fields will be written.
	 * @param comment
	 *            Character used to start a comment line
	 */
	public CSVWriter(PrintWriter pw, char comment) {
		this.pw = pw;
		this.forceQuotes = false;
		this.separator = ',';
		this.comment = String.valueOf(comment) + " ";
		this.lineSeparator = System.getProperty("line.separator");
	} // end of CSVWriter

	public CSVWriter(Writer w, char comment) {
		this(new PrintWriter(w), comment);
	}

	/**
	 * PrintWriter where CSV fields will be written.
	 */
	PrintWriter pw;

	/**
	 * true if you want all fields surrounded in quotes, whether or not they
	 * contain commas, quotes or spaces.
	 */
	boolean forceQuotes;

	/*
	 * field separator character, usually ',' in North America, ';' in Europe
	 * and sometimes '\t' for tab.
	 */
	char separator;

	/**
	 * true if there has was a field previously written to this line, meaning
	 * there is a comma pending to be written.
	 */
	boolean wasPreviousField = false;

	/**
	 * Character to start a comment line with. May be '#' for example.
	 */
	String comment;

	/**
	 * Line separator.
	 */
	String lineSeparator;

	/**
	 * Writes a single coment line to the file given by the <code>text</code>.
	 * This is the text leaded by the <code>comment char + " "</code>, given in
	 * the constructor.
	 * 
	 * @param text
	 *            contains the comment text.
	 */
	public void writeCommentln(String text) {
		if (wasPreviousField)
			writeln(); // close open line since we need to start a new one for
						// comment
		pw.print(comment);
		// wasPreviousField = false; // to prevent a comma after the comment
		// sign
		write(text);
		writeln();
	} // end of writeComentln

	/**
	 * Writes a single value in a line suited by a newline to the file given by
	 * the <code>token</code>.
	 * 
	 * @param token
	 *            contains the value.
	 */
	public void writeln(String token) {
		write(token);
		writeln();
	} // end of writeln

	/**
	 * Writes a new line in the CVS output file to demark the end of record.
	 */
	public void writeln() {
		/* don't bother to write last pending comma on the line */
		wasPreviousField = false;
		pw.print(lineSeparator);
	} // end of writeln

	/**
	 * Writes a single line of comma separated values from the array given by
	 * <code>line</code>.
	 * 
	 * @param line
	 *            containig an array of tokens.
	 */
	public void writeln(String[] line) {
		for (int ii = 0; ii < line.length; ii++) {
			write(line[ii]);
		} // end of for

		writeln(); // write newLine

	} // end of writeln

	/**
	 * Write one csv field to the file, followed by a separator unless it is the
	 * last field on the line. Lead and trailing blanks will be removed.
	 * 
	 * @param s
	 *            The string to write. Any additional quotes or embedded quotes
	 *            will be provided by write.
	 */
	public void write(String s) {
		if (wasPreviousField) {
			pw.print(separator);
		}

		if (s == null) {
			pw.print("");
			return;
		} // end of if s == null

		s = s.trim();
		if (s.indexOf('\"') >= 0) {
			/* worst case, needs surrounding quotes and internal quotes doubled */
			pw.print('\"');
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (c == '\"') {
					pw.print("\"\"");
				} else {
					pw.print(c);
				}
			}
			pw.print('\"');
			// end of if \"
		} else if (s.indexOf('\n') >= 0) {
			// bad case as well: having a new line in the token: \n
			pw.print('\"');
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (c == '\n') {
					pw.print("\\n");
				} else {
					pw.print(c);
				}
			}
			pw.print('\"');
			// end of if \n
		} else if (forceQuotes || s.indexOf(separator) >= 0) {
			/* need surrounding quotes */
			pw.print('\"');
			pw.print(s);
			pw.print('\"');
		} else {
			/* ordinary case, no surrounding quotes needed */
			pw.print(s);
		}
		/* make a note to print trailing comma later */
		wasPreviousField = true;
	} // end of write

	/**
	 * Close the PrintWriter.
	 */
	public void close() {
		if (pw != null) {
			pw.close();
			pw = null;
		} // end of if
	} // end of close

} // end CSVWriter

// end of file
