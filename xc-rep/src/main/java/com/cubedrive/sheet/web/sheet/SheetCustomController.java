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
package com.cubedrive.sheet.web.sheet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.sheet.domain.sheet.SheetCustom;
import com.cubedrive.sheet.service.sheet.SheetCustomService;
import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/sheetCustom")
public class SheetCustomController extends BaseSheetController {

	@Autowired
	private SheetCustomService sheetCustomService;

	@Autowired
	private DocumentFileService documentFileService;

	/**
	 * This will get a list of custom for the sheet
	 * 
	 * http://localhost:8080/sheet/sheetCustom/list
	 * 
	 * @param documentId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		User loginUser = this.getLoginUser();

		List<SheetCustomObj> objs = sheetCustomService.findByUserId(loginUser);

		Map<String, Object> results = Maps.newHashMap();
		results.put("totalCount", objs.size());
		results.put("results", objs);
		results.put("metaData", SheetCustomObj.createMetaData());
		outputJson(results, request, response);
	}

	/**
	 * http://localhost:8080/sheet/sheetCustom/create?category=number&content=*
	 * 
	 * @param data
	 * @param name
	 * @param documentId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "create")
	public void create(@RequestParam(required = true) String content,
			@RequestParam(required = true) String category,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil
				.getDesEncrypterForFile(loginUser.getUsername());

		SheetCustom custom = new SheetCustom();
		custom.setUser(loginUser);
		custom.setCategory(category);
		custom.setContent(content);

		sheetCustomService.insert(custom);

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);

		outputJson(results, request, response);
	}

	@RequestMapping(value = "load")
	public void load(@RequestParam(required = true) String id,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil
				.getDesEncrypterForFile(loginUser.getUsername());

		SheetCustom custom = sheetCustomService.getById(new Integer(id));

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);
		results.put("object", custom);

		outputJson(results, request, response);
	}

	@RequestMapping(value = "delete")
	public void delete(@RequestParam(required = true) String id,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		User loginUser = this.getLoginUser();

		SheetCustom custom = sheetCustomService.getById(new Integer(id));
		sheetCustomService.delete(custom);

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);

		outputJson(results, request, response);
	}

}