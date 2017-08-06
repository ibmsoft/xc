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
package com.cubedrive.base.persistence.account;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.base.domain.account.UserSetting;

@Repository
public interface UserSettingMapper {

   Integer insertUserSetting(@Param(value = "userId") Integer userId, 
    		@Param(value = "userSetting") UserSetting userSetting);

    void updateUserSetting(@Param(value = "userSetting") UserSetting userSetting);
    
    UserSetting load(@Param(value = "userId") Integer userId);

}
