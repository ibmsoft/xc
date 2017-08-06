/**
 * Enterprise Spreadsheet Solutions
 * Copyright(c) FeyaSoft Inc. All right reserved.
 * info@enterpriseSheet.com
 * http://www.enterpriseSheet.com
 * 
 * Licensed under the EnterpriseSheet Commercial License.
 * http://enterprisesheet.com/license.jsp
 * 
 * You need to have a valid license key to access this file.
 */
package com.cubedrive.sheet.service.sheet._import.csv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Fenqiang Zhuang. User: Fenqiang Zhuang Date: Jan 16, 2004
 * Time:5:25:25 PM CSV format is used Microsoft Word and Excel. Fields are
 * separated by commas, and enclosed in quotes if they contain commas. And field
 * can not include any double quote. Embedded spaces do not normally require
 * surrounding quotes. The last field on the line is not followed by a comma.
 * Null fields are represented by two commas in a row. This program will remove
 * quote from the string, and the string will be separate by the separator. The
 * default separator is double Quote. Exception, if meet single one double
 * quote, ignore the whole left line. In special case, the space also need to be
 * separated.
 */
public class CSVParser {
	/**
	 * field separator character, usually ',' in North America, ';' in Europe
	 * and sometimes '\t' for tab.
	 */
	private final char separator;

	// quote character, usually '\"'
	private final char quote;

	// private final char singleQuote = '\'';

	/**
	 * true if line should trim lead/trail whitespace from fields returned.
	 */
	private final boolean trim;

	/**
	 * category of end of line char.
	 */
	private static final int EOL = 0;

	/**
	 * category of ordinary character
	 */
	private static final int ORDINARY = 1;

	/**
	 * categotory of the quote mark "
	 */
	private static final int QUOTE = 2;

	/**
	 * category of the separator, e.g. comma, semicolon or tab.
	 */
	private static final int SEPARATOR = 3;

	/**
	 * category of characters treated as white space.
	 */
	private static final int WHITESPACE = 4;

	/**
	 * input line for separate
	 */
	private String csvLine;

	private String csvLineOrg;

	/**
	 * parser: We are in blanks before the field.
	 */
	private static final int SEEKING_START = 0;

	/**
	 * parser: We are in the middle of an ordinary field.
	 */
	private static final int IN_PLAIN = 1;

	/**
	 * parser: e are in middle of field surrounded in quotes.
	 */
	private static final int IN_QUOTED = 2;

	/**
	 * parser: We have just hit a quote, might be doubled or might be last one.
	 */
	private static final int AFTER_END_QUOTE = 3;

	/**
	 * parser: We are in blanks after the field looking for the separator
	 */
	private static final int SKIPPING_TAIL = 4;

	/**
	 * Send a flag which express must have more field append. This is used to
	 * handle specail case when the last field is empty.
	 */
	private boolean mustHaveMoreFieldFlag;

	/**
	 * This is a special case which white space is also a default separator.
	 */
	private boolean whitespaceIsDefaultSeparator;

	/**
	 * constructor
	 * 
	 * @param csvLineIn
	 * @param separator
	 * @param quote
	 * @param trim
	 */
	public CSVParser(String csvLineIn, char separator, char quote, boolean trim) {
		csvLine = csvLineIn + "\n";
		csvLineOrg = csvLineIn;
		this.separator = separator;
		this.quote = quote;
		this.trim = trim;
		this.mustHaveMoreFieldFlag = true;
		this.whitespaceIsDefaultSeparator = false;
	}

	/**
	 * Default value for comma amd quote
	 * 
	 * @param csvLine
	 */
	public CSVParser(String csvLine) {
		// pass line to parse, field separator, quotation symbol to general
		// constructor
		this(csvLine, ',', '\"', true);
	}

	/**
	 * Default value for comma amd quote
	 * 
	 * @param csvLine
	 */
	public CSVParser(String csvLine, char separator) {
		// pass line to parse, field separator, quotation symbol to general
		// constructor
		this(csvLine, separator, '\"', true);
	}

	/**
	 * Default value for comma amd quote
	 * 
	 * @param csvLine
	 */
	public CSVParser(String csvLine, boolean whitespaceIsDefaultSeparator) {
		// pass line to parse, field separator, quotation symbol to general
		// constructor
		this(csvLine, ' ', '\"', true);
		this.whitespaceIsDefaultSeparator = whitespaceIsDefaultSeparator;
	}

	/**
	 * categorise a character for the finite state machine.
	 * 
	 * @param c
	 *            the character to categorise
	 * @return integer representing the character's category.
	 */
	private int categorise(char c) {
		switch (c) {
		case ' ':
		case '\r':
		case 0xff:
			return WHITESPACE;

		case '\n':
			return EOL; /* artificially applied to end of line */

		default:
			if (c == quote) {
				return QUOTE;
			} else if (c == separator /*
									 * dynamically determined so can't use as
									 * case label
									 */) {
				return SEPARATOR;
			}
			/* do our tests in crafted order, hoping for an early return */
			else if ('!' <= c && c <= '~') {
				return ORDINARY;
			} else if (0x00 <= c && c <= 0x20) {
				return WHITESPACE;
			} else if (Character.isWhitespace(c)) {
				return WHITESPACE;
			} else {
				return ORDINARY;
			}
		}
	}

	/**
	 * Get all fields in the line, return in arrayList
	 * 
	 * @return Array of strings, one for each field. Possibly empty, but never
	 *         null.
	 */
	public ArrayList<String> getAllFieldsInArrayList() throws Exception {
		ArrayList<String> arrayForLine = new ArrayList<String>();

		// continue until eol
		do {
			String field = getFieldBySeparator();
			if (field == null) {
				if (mustHaveMoreFieldFlag) {
					arrayForLine.add("");
				}
				break;
			}
			// store token
			arrayForLine.add(field);
		} while (true);

		return arrayForLine;
	}

	/**
	 * Get all fields in the line and return in set format.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Set<String> getAllFieldsInSet() throws Exception {
		Set<String> setOfString = new HashSet<String>();

		// continue until eol
		do {
			String field = getFieldBySeparator();
			if (field == null) {
				if (mustHaveMoreFieldFlag) {
					setOfString.add("");
				}
				break;
			}
			// store token
			setOfString.add(field);
		} while (true);

		return setOfString;
	}

	/**
	 * loop for each char in the line to find a field guaranteed to leave early
	 * by hitting EOL
	 * 
	 * @return
	 */
	private String getFieldBySeparator() throws Exception {
		StringBuffer field = new StringBuffer(64);
		int state = SEEKING_START;

		for (int i = 0; i < csvLine.length(); i++) {
			char c = csvLine.charAt(i);

			int category = categorise(c);

			// for debug
			if (false) {
				System.out.println("char:" + c + " state:" + state + " field:"
						+ field.length());
			}

			switch (state) {
			case SEEKING_START: { /* in blanks before field */
				switch (category) {
				case WHITESPACE:
					/* ignore */
					break;

				case QUOTE:
					// field.append(c);
					state = IN_QUOTED;
					break;

				case SEPARATOR:
					/* end of empty field */
					csvLine = csvLine.substring(i + 1);
					mustHaveMoreFieldFlag = true;
					return "";

				case EOL:
					/* null to mark end of line */
					csvLine = null;
					return null;

				case ORDINARY:
					field.append(c);
					state = IN_PLAIN;
					break;
				}
				break;
			}
			case IN_PLAIN: { /* in middle of ordinary field */
				switch (category) {
				/*
				 * case QUOTE: throw new Exception("Meet double quote on line: "
				 * + csvLineOrg);
				 * System.out.println("Meet double quote on line: " +
				 * csvLineOrg); return field.toString();
				 */
				case QUOTE:
					field.append(c);
					break;

				case SEPARATOR:
					/* done */
					csvLine = csvLine.substring(i + 1);
					mustHaveMoreFieldFlag = true;
					return maybeTrim(field.toString());

				case EOL:
					csvLine = csvLine.substring(i); /* push EOL back */
					mustHaveMoreFieldFlag = false;
					return maybeTrim(field.toString());

				case WHITESPACE:
					if (whitespaceIsDefaultSeparator) {
						csvLine = csvLine.substring(i + 1);
						mustHaveMoreFieldFlag = true;
						return maybeTrim(field.toString());
					} else {
						field.append(' ');
						break;
					}

				case ORDINARY:
					field.append(c);
					break;
				}
				break;
			}

			case IN_QUOTED: { /* in middle of field surrounded in quotes */
				switch (category) {

				case QUOTE:
					// field.append(c);
					state = AFTER_END_QUOTE;
					break;

				case EOL:
					/*
					 * throw new Exception("Meet double quote on line: " +
					 * csvLineOrg);
					 */
					csvLine = csvLine.substring(i); /* push back eol */
					mustHaveMoreFieldFlag = false;
					return maybeTrim(field.toString());

				case WHITESPACE:
					field.append(' ');
					break;

				case SEPARATOR:
				case ORDINARY:
					field.append(c);
					break;
				}
				break;
			}

			case AFTER_END_QUOTE: {

				/*
				 * In situation like this "xxx" which may turn out to be
				 * xxx""xxx" or "xxx", We find out here.
				 */
				switch (category) {

				case QUOTE:
					/* was a double quote, e.g. a literal " */
					field.append(c);
					state = IN_QUOTED;
					break;

				case SEPARATOR:
					/* we are done with field. */
					csvLine = csvLine.substring(i + 1);
					mustHaveMoreFieldFlag = true;
					return maybeTrim(field.toString());

				case EOL:
					csvLine = csvLine.substring(i); /* push back eol */
					mustHaveMoreFieldFlag = false;
					return maybeTrim(field.toString());

				case WHITESPACE:
					/* ignore trailing spaces up to separator */
					if (whitespaceIsDefaultSeparator) {
						csvLine = csvLine.substring(i + 1);
						mustHaveMoreFieldFlag = true;
						return maybeTrim(field.toString());
					} else {
						state = SKIPPING_TAIL;
						break;
					}

				case ORDINARY:

					throw new Exception("Unexpected character found in line: "
							+ csvLineOrg);

					// System.out.println("Unexpected character found in line: "
					// + csvLineOrg);
					// mustHaveMoreFieldFlag = false;
					// break;
					// return field.toString();
				}
				break;
			}

			case SKIPPING_TAIL: {
				/* in spaces after field seeking separator */

				switch (category) {

				case SEPARATOR:
					/* we are done. */
					csvLine = csvLine.substring(i + 1);
					mustHaveMoreFieldFlag = true;
					return maybeTrim(field.toString());

				case EOL:
					csvLine = csvLine.substring(i); /* push back eol */
					mustHaveMoreFieldFlag = false;
					return maybeTrim(field.toString());

				case WHITESPACE:
					/* ignore trailing spaces up to separator */
					break;

				case QUOTE:
				case ORDINARY:
					/**
					 * throw new Exception("Unexpected character on line: " +
					 * csvLineOrg);
					 */
					System.out.println("Unexpected character found in line: "
							+ csvLineOrg);
					break;
				// return maybeTrim(field.toString());
				}
				break;
			}
			}
		}
		return field.toString();
	}

	/**
	 * Trim the string, but only if we are in trimming mode.
	 * 
	 * @param s
	 *            String to be trimmed.
	 * @return String or trimmed string.
	 */
	private String maybeTrim(String s) {
		if (trim) {
			return s.trim();
		} else {
			return s;
		}
	}
}