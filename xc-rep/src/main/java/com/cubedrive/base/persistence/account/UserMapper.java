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

import com.cubedrive.base.domain.account.User;

@Repository
public interface UserMapper {

	User getById(@Param(value = "userId") Integer userId);

	User getByUsername(@Param(value = "username") String username);

	Integer insertUser(@Param(value = "user") User user);
}
