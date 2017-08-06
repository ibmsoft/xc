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
package com.cubedrive.base.web.account;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.account.UserSetting;
import com.cubedrive.base.domain.account.UserSetting.Lang;
import com.cubedrive.base.service.account.UserService;
import com.cubedrive.base.web.BaseController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/userSetting")
public class UserSettingController extends BaseController {

	@Autowired
	private UserService userService;
	
	/**
	 * Action to create a new file
	 * 
	 * @param exname
	 * @param name
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "updateLang")
	public void updateLang(@RequestParam(required = true) String lang,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		User loginUser = this.getLoginUser();
		UserSetting setting = userService.load(loginUser);
		
		if (lang.equals(Lang.en.name())) setting.setLang(Lang.en);
		else if (lang.equals(Lang.zh_CN.name())) setting.setLang(Lang.zh_CN);
		
		userService.updateSetting(setting);

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);

		outputJson(results, request, response);
	}

}
