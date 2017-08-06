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
package com.cubedrive.base;

import java.io.File;
import java.util.TimeZone;

public class BaseAppConfiguration {

	private static final BaseAppConfiguration _instance = new BaseAppConfiguration();

	private BaseAppConfiguration() {

	}

	void init() {

	}

	public String getServerAreaZoneGmt() {
		return String.valueOf(TimeZone.getDefault().getRawOffset() / 1000 / 60); // as
																					// minutes
	}

	public int getServerAreaZoneGmtAsInt() {
		return TimeZone.getDefault().getRawOffset() / 1000 / 60;
	}

	public TimeZone getAppTimeZone() {
		return TimeZone.getDefault();
	}

	public File getHomeDir() {
		return AppContext.getHomeDir();
	}

	public String getUserFileServer() {
		return AppContext.getUserFileServer();
	}

	public String getMainUrl() {
		return AppContext.getMainUrl();
	}

	public String getDefaultLang() {
		return AppContext.getDefaultLang();
	}

	public String getVersion() {
		return null;
	}

	public String getIdEncryptLevel() {
		return null;
	}

	public String getServerId() {
		return null;
	}

	public File getTempDir() {
		return AppContext.getTempDir();
	}

	public static BaseAppConfiguration instance() {
		return _instance;
	}

}
