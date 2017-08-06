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
package com.cubedrive.base.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubedrive.base.BaseAppConfiguration;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.account.UserSetting;
import com.cubedrive.base.persistence.account.UserMapper;
import com.cubedrive.base.persistence.account.UserSettingMapper;

@Service
public class UserService  {
    
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserSettingMapper userSettingMapper;
	
	public User getCurrentUser() {
		return userMapper.getById(1);
	}

	public User getUserById(Integer userId) {
		return userMapper.getById(userId);
	}

	public User getUserByName(String username) {
		return userMapper.getByUsername(username);
	}
	
	public String getLang(User loginUser) {
		String lang = BaseAppConfiguration.instance().getDefaultLang();
        UserSetting userSetting = userSettingMapper.load(loginUser.getId());
        if (userSetting != null) {
            lang = userSetting.getLang().name();
        } 
        
        return lang;
	}
	
	// this is the method to create setting ...
	public void createSetting(Integer userId, UserSetting userSetting) {
		userSettingMapper.insertUserSetting(userId, userSetting);
	}

	// update setting now ...
	public void updateSetting(UserSetting userSetting) {
		userSettingMapper.updateUserSetting(userSetting);
	}
	
	public UserSetting load(User user) {
		UserSetting setting =  user.getSetting();
		if (setting == null) {
			setting = new UserSetting();
			setting.setUserId(user.getId());
			userSettingMapper.insertUserSetting(user.getId(), setting);
		}
		
		return setting;
	}
	
	public String currentUserLang() {
		String lang = null;
		User currentUser = getCurrentUser();
		if(currentUser != null){
        UserSetting userSetting = load(currentUser);
	        if (userSetting != null && userSetting.getLang()!= null) {
	            lang = userSetting.getLang().name();
	        }
        }
		if(lang == null)
        	lang = BaseAppConfiguration.instance().getDefaultLang();
        return lang;
	}

}
