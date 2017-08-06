package com.xzsoft.xc.st.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.st.common.JsonHelper;
import com.xzsoft.xc.st.dao.XcStTransshipmentDao;
import com.xzsoft.xc.st.mapper.XcStCommonMapper;
import com.xzsoft.xc.st.mapper.XcStTransshipmentMapper;
import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName XcStTransshipmentDaoImpl
  * @Description 处理物资调拨单的dao接口实现类
  * @author RenJianJian
  * @date 2016年12月7日 下午4:54:24
 */
@Repository("xcStTransshipmentDao")
public class XcStTransshipmentDaoImpl implements XcStTransshipmentDao {
	@Resource
	private XcStTransshipmentMapper xcStTransshipmentMapper;
	@Resource
	private XcStCommonMapper xcStCommonMapper;
	@Override
	public Dto selectXcStTransshipmentH(String TR_H_ID) throws Exception {
		try {
			return xcStTransshipmentMapper.selectXcStTransshipmentH(TR_H_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Dto selectXcStTransshipmentL(String TR_L_ID) throws Exception {
		try {
			return xcStTransshipmentMapper.selectXcStTransshipmentL(TR_L_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public List<Dto> selectXcStTransshipmentLByHId(String TR_H_ID) throws Exception {
		try {
			return xcStTransshipmentMapper.selectXcStTransshipmentLByHId(TR_H_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void insertXcStTransshipmentH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//主信息
			xcStTransshipmentMapper.insertXcStTransshipmentH(pDto);

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
			xcStTransshipmentMapper.insertXcStTransshipmentL(insertDtl);
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void updateXcStTransshipmentH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//更新主表信息
			xcStTransshipmentMapper.updateXcStTransshipmentH(pDto);

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
				xcStTransshipmentMapper.updateXcStTransshipmentL(updateDtl);
			}
			//删除明细行
			ArrayList deleteDtlList = (ArrayList) pDto.get("deleteDtlList");
			if(deleteDtlList!=null && deleteDtlList.size()>0){
				Dto deleteDtl = new BaseDto();
				deleteDtl.put("dbType",PlatformUtil.getDbType());
				deleteDtl.put("list",deleteDtlList);
				JsonHelper.parseJSONNull2Null(deleteDtlList);
				xcStTransshipmentMapper.deleteXcStTransshipmentLByLId(deleteDtl);
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
				xcStTransshipmentMapper.insertXcStTransshipmentL(insertDtl);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void deleteXcStTransshipmentH(List<String> stTransshipmentHIds) throws Exception {
		try {
			//删除物资调拨单行表
			xcStTransshipmentMapper.deleteXcStTransshipmentLByHId(stTransshipmentHIds);
			//删除物资调拨单主表
			xcStTransshipmentMapper.deleteXcStTransshipmentH(stTransshipmentHIds);
			//删除物资调拨单货位表
			xcStCommonMapper.deleteXcStTransshipmentLoByHId(stTransshipmentHIds);
			//删除物资调拨单批次OR序列号表
			xcStCommonMapper.deleteXcStTransshipmentSerByHId(stTransshipmentHIds);
		} catch (Exception e) {
			throw e;
		}
	}
}