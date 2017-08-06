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

import java.nio.charset.Charset;
import java.util.List;

import com.google.common.collect.Lists;

public class BaseAppConstants {

	public final static Charset utf8 = Charset.forName("UTF-8");

	public final static String DEFAULT_USER_PASSWORD = "abcabc";
	public static final String DEFAULT_BACKGROUND_IMG = "blue.jpg";
	public static final String DEFAULT_HOME_URL = "http://www.cubedrive.com";
	
	public static final String DEFAULT_KEY = "heloowhoisthisisoh#4kkj@kljkop";
	public static final String MY_FILE_KEY = "Tokkokokdfdsfekey%ghj";
	public static final String ATTACHMENT_KEY = "attachmentsecret";
	
	
	public static final int USER_ROLE_ADMIN_ID = 1;
	public static final int USER_ROLE_USER_ID = 2;
	public static final String USER_ROLE_ADMIN = "admin";
	public static final String USER_ROLE_USER = "user";
	
	public static final String USER_SETTING_LANG_EN = "en";
	public static final String USER_SETTING_THEME_BLUE = "blue";
	
	public final static String THUMBNAIL_PREFIX = "thumb_";

	public final static String DEFAULT_USER_IMAGE_PATH = "images/people.png";
	
	public static final String SECURITY_NORMAL = "normal";
	public static final String SECURITY_HIGH = "high";
	
	public static String USER_UPLOAD_DIR = "upload";
	
	public final static int FILE_STATUS_ACTIVE = 0;
	public final static int FILE_STATUS_TEMPORARY = -1;
	public final static int FILE_STATUS_ARCHIVE = -2;
	public final static int FILE_STATUS_HISTORY = -9;
	
	public final static int NO_PERMISSION = 0;
	public final static int READ_PERMISSION = 1;
	public final static int WRITE_PERMISSION_NOT_EXPORT = 2;
	public final static int WRITE_PERMISSION = 3;
	public final static int DELETE_PERMISSION = 4;
	public final static int FULL_PERMISSION = 7;
	
	public static final List<String> ACCEPT_IMAGE_TYPE = Lists.newArrayList("png", "jpg", "jpeg", "gif", "bmp");
	
	public final static String SHEET_OLD_TEMPLATE_EXNAME= "myXlt";
	public final static String SHEET_FILE_EXNAME = "xls";
	public final static String SHEET_TEMPLATE_EXNAME = "xlt";
    public final static String SHEET_DB_TABLE_EXNAME = "xld";
	public final static String EDITOR_EXNAME = "myDoc";
	public final static String EDITOR_TEMPLATE_EXNAME = "myDot";
	public final static String PPT_EXNAME = "myPpt";
	public final static String FOLDER_EXNAME = "myFolder";
	public final static String ATTACH_EXNAME = "attach";
	public final static String PHOTO_EXNAME = "photo";

	public final static String PERMISSION = "permission";
	public final static String ALLOW_INVITE = "allowInvite";
	
	// remove this now, for tempalte we define it as exname name ...
	// public final static int FILE_STATUS_TEMPLATE = -10;

	public final static int FILE_PRIVATE = 0;
	public final static int FILE_PUBLIC_VIEW = 1;
	public final static int FILE_PUBLIC_EDIT = 2;
	
}
