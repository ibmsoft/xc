package com.xzsoft.xc.st.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.st.common.JsonHelper;
import com.xzsoft.xc.st.dao.XcStCostChangeDao;
import com.xzsoft.xc.st.mapper.XcStCostChangeMapper;
import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName XcStCostChangeDaoImpl
  * @Description 处理库存成本调整单的dao接口实现类
  * @author RenJianJian
  * @date 2016年12月7日 下午4:54:24
 */
@Repository("xcStCostChangeDao")
public class XcStCostChangeDaoImpl implements XcStCostChangeDao {
	@Resource
	private XcStCostChangeMapper xcStCostChangeMapper;
	@Override
	public Dto selectXcStCostChangeH(String COST_CHANGE_H_ID) throws Exception {
		try {
			return xcStCostChangeMapper.selectXcStCostChangeH(COST_CHANGE_H_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Dto selectXcStCostChangeL(String COST_CHANGE_L_ID) throws Exception {
		try {
			return xcStCostChangeMapper.selectXcStCostChangeL(COST_CHANGE_L_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public List<Dto> selectXcStCostChangeLByHId(String COST_CHANGE_H_ID) throws Exception {
		try {
			return xcStCostChangeMapper.selectXcStCostChangeLByHId(COST_CHANGE_H_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void insertXcStCostChangeH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//主信息
			xcStCostChangeMapper.insertXcStCostChangeH(pDto);

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
			xcStCostChangeMapper.insertXcStCostChangeL(insertDtl);
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void updateXcStCostChangeH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//更新主表信息
			xcStCostChangeMapper.updateXcStCostChangeH(pDto);

			//删除明细行
			ArrayList deleteDtlList = (ArrayList) pDto.get("deleteDtlList");
			if(deleteDtlList!=null && deleteDtlList.size()>0){
				Dto deleteDtl = new BaseDto();
				deleteDtl.put("dbType",PlatformUtil.getDbType());
				deleteDtl.put("list",deleteDtlList);
				JsonHelper.parseJSONNull2Null(deleteDtlList);
				xcStCostChangeMapper.deleteXcStCostChangeLByLId(deleteDtl);
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
				xcStCostChangeMapper.insertXcStCostChangeL(insertDtl);
			}
			//更新明细行
			ArrayList updateDtlList = (ArrayList) pDto.get("updateDtlList");
			if(updateDtlList!=null && updateDtlList.size()>0){
				Dto updateDtl = new BaseDto();
				for (int i = 0;i < updateDtlList.size();i++) {
					Dto dto = (Dto)updateDtlList.get(i);
					dto.put("CREATION_DATE",createDate);
					dto.put("CREATED_BY",userId);
					dto.put("LAST_UPDATE_DATE",createDate);
					dto.put("LAST_UPDATED_BY",userId);
				}
				updateDtl.put("dbType",PlatformUtil.getDbType());
				updateDtl.put("list",updateDtlList);
				JsonHelper.parseJSONNull2Null(updateDtlList);
				xcStCostChangeMapper.updateXcStCostChangeL(updateDtl);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void deleteXcStCostChangeH(List<String> stCostChangeHIds) throws Exception {
		try {
			xcStCostChangeMapper.deleteXcStCostChangeLByHId(stCostChangeHIds);
			xcStCostChangeMapper.deleteXcStCostChangeH(stCostChangeHIds);
		} catch (Exception e) {
			throw e;
		}
	}
}