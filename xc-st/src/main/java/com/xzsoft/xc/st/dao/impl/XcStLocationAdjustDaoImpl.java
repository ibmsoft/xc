package com.xzsoft.xc.st.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.st.common.JsonHelper;
import com.xzsoft.xc.st.dao.XcStLocationAdjustDao;
import com.xzsoft.xc.st.mapper.XcStCommonMapper;
import com.xzsoft.xc.st.mapper.XcStLocationAdjustMapper;
import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName XcStLocationAdjustDaoImpl
  * @Description 处理货位调整单的dao接口实现类
  * @author RenJianJian
  * @date 2016年12月7日 下午4:54:24
 */
@Repository("xcStLocationAdjustDao")
public class XcStLocationAdjustDaoImpl implements XcStLocationAdjustDao {
	@Resource
	private XcStLocationAdjustMapper xcStLocationAdjustMapper;
	@Resource
	private XcStCommonMapper xcStCommonMapper;
	@Override
	public Dto selectXcStLocationAdjustH(String LO_ADJUST_H_ID) throws Exception {
		try {
			return xcStLocationAdjustMapper.selectXcStLocationAdjustH(LO_ADJUST_H_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Dto selectXcStLocationAdjustL(String LO_ADJUST_L_ID) throws Exception {
		try {
			return xcStLocationAdjustMapper.selectXcStLocationAdjustL(LO_ADJUST_L_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public List<Dto> selectXcStLocationAdjustLByHId(String LO_ADJUST_H_ID) throws Exception {
		try {
			return xcStLocationAdjustMapper.selectXcStLocationAdjustLByHId(LO_ADJUST_H_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void insertXcStLocationAdjustH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//主信息
			xcStLocationAdjustMapper.insertXcStLocationAdjustH(pDto);

			ArrayList insertDtlList = (ArrayList) pDto.get("insertDtlList");
			Dto insertDtl = new BaseDto();
			for (int i = 0;i < insertDtlList.size();i++) {
				Dto dto = (Dto)insertDtlList.get(i);
				dto.put("CREATION_DATE",createDate);
				dto.put("CREATED_BY",userId);
				dto.put("LAST_UPDATE_DATE",createDate);
				dto.put("LAST_UPDATED_BY",userId);
			}
			insertDtl.put("dbType",PlatformUtil.getDbType());
			insertDtl.put("list",insertDtlList);
			JsonHelper.parseJSONNull2Null(insertDtlList);
			xcStLocationAdjustMapper.insertXcStLocationAdjustL(insertDtl);
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void updateXcStLocationAdjustH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//更新主表信息
			xcStLocationAdjustMapper.updateXcStLocationAdjustH(pDto);

			//删除明细行
			ArrayList deleteDtlList = (ArrayList) pDto.get("deleteDtlList");
			if(deleteDtlList!=null && deleteDtlList.size()>0){
				Dto deleteDtl = new BaseDto();
				deleteDtl.put("dbType",PlatformUtil.getDbType());
				deleteDtl.put("list",deleteDtlList);
				JsonHelper.parseJSONNull2Null(deleteDtlList);
				xcStLocationAdjustMapper.deleteXcStLocationAdjustLByLId(deleteDtl);
			}
			//新增明细行
			ArrayList insertDtlList = (ArrayList) pDto.get("insertDtlList");
			if(insertDtlList != null && insertDtlList.size() > 0){
				Dto insertDtl = new BaseDto();
				for (int i = 0;i < insertDtlList.size();i++) {
					Dto dto = (Dto)insertDtlList.get(i);
					dto.put("CREATION_DATE",createDate);
					dto.put("CREATED_BY",userId);
					dto.put("LAST_UPDATE_DATE",createDate);
					dto.put("LAST_UPDATED_BY",userId);
				}
				insertDtl.put("dbType",PlatformUtil.getDbType());
				insertDtl.put("list",insertDtlList);
				JsonHelper.parseJSONNull2Null(insertDtlList);
				xcStLocationAdjustMapper.insertXcStLocationAdjustL(insertDtl);
			}
			//更新明细行
			ArrayList updateDtlList = (ArrayList) pDto.get("updateDtlList");
			if(updateDtlList!=null && updateDtlList.size()>0){
				Dto updateDtl = new BaseDto();
				for (int i = 0;i < updateDtlList.size();i++) {
					Dto dto = (Dto)updateDtlList.get(i);
					dto.put("LAST_UPDATE_DATE",createDate);
					dto.put("LAST_UPDATED_BY",userId);
				}
				updateDtl.put("dbType",PlatformUtil.getDbType());
				updateDtl.put("list",updateDtlList);
				JsonHelper.parseJSONNull2Null(updateDtlList);
				xcStLocationAdjustMapper.updateXcStLocationAdjustL(updateDtl);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void deleteXcStLocationAdjustH(List<String> stLocationAdjustHIds) throws Exception {
		try {
			//删除物资调拨单行表
			xcStLocationAdjustMapper.deleteXcStLocationAdjustLByHId(stLocationAdjustHIds);
			//删除货位调整单主表
			xcStLocationAdjustMapper.deleteXcStLocationAdjustH(stLocationAdjustHIds);
			//删除货位调整单批次OR序列号表
			xcStCommonMapper.deleteXcStLoAdjustSerByHId(stLocationAdjustHIds);
		} catch (Exception e) {
			throw e;
		}
	}
}