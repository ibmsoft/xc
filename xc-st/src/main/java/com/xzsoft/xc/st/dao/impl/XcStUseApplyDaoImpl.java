package com.xzsoft.xc.st.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.st.common.JsonHelper;
import com.xzsoft.xc.st.dao.XcStUseApplyDao;
import com.xzsoft.xc.st.mapper.XcStUseApplyMapper;
import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName XcStUseApplyDaoImpl
  * @Description 处理领用申请的dao接口实现类
  * @author RenJianJian
  * @date 2016年12月7日 下午4:05:42
 */
@Repository("xcStUseApplyDao")
public class XcStUseApplyDaoImpl implements XcStUseApplyDao {
	@Resource
	private XcStUseApplyMapper xcStUseApplyMapper;
	@Override
	public Dto selectXcStUseApplyH(String USE_APPLY_H_ID) throws Exception {
		try {
			return xcStUseApplyMapper.selectXcStUseApplyH(USE_APPLY_H_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Dto selectXcStUseApplyL(String USE_APPLY_L_ID) throws Exception {
		try {
			return xcStUseApplyMapper.selectXcStUseApplyL(USE_APPLY_L_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public List<Dto> selectXcStUseApplyLByHId(String USE_APPLY_H_ID) throws Exception {
		try {
			return xcStUseApplyMapper.selectXcStUseApplyLByHId(USE_APPLY_H_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void insertXcStUseApplyH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//主信息
			xcStUseApplyMapper.insertXcStUseApplyH(pDto);

			ArrayList insertDtlList = (ArrayList) pDto.get("insertDtlList");
			Dto insertDtl = new BaseDto();
			for (int i = 0;i < insertDtlList.size();i++) {
				Dto dto = (Dto)insertDtlList.get(i);
				dto.put("USE_APPLY_L_CODE",i + 1);
				dto.put("CREATION_DATE",createDate);
				dto.put("CREATED_BY",userId);
				dto.put("LAST_UPDATE_DATE",createDate);
				dto.put("LAST_UPDATED_BY",userId);
			}
			insertDtl.put("dbType",PlatformUtil.getDbType());
			insertDtl.put("list",insertDtlList);
			JsonHelper.parseJSONNull2Null(insertDtlList);
			xcStUseApplyMapper.insertXcStUseApplyL(insertDtl);
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void updateXcStUseApplyH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//更新主表信息
			xcStUseApplyMapper.updateXcStUseApplyH(pDto);

			//删除明细行
			ArrayList deleteDtlList = (ArrayList) pDto.get("deleteDtlList");
			if(deleteDtlList!=null && deleteDtlList.size()>0){
				Dto deleteDtl = new BaseDto();
				deleteDtl.put("dbType",PlatformUtil.getDbType());
				deleteDtl.put("list",deleteDtlList);
				JsonHelper.parseJSONNull2Null(deleteDtlList);
				xcStUseApplyMapper.deleteXcStUseApplyLByLId(deleteDtl);
			}
			//新增明细行
			ArrayList insertDtlList = (ArrayList) pDto.get("insertDtlList");
			if(insertDtlList != null && insertDtlList.size() > 0){
				//通过主ID得到行信息，从而对行号进行控制
				List<Dto> dtlList = selectXcStUseApplyLByHId(pDto.getAsString("USE_APPLY_H_ID"));
				Dto insertDtl = new BaseDto();
				for (int i = 0;i < insertDtlList.size();i++) {
					Dto dto = (Dto)insertDtlList.get(i);
					dto.put("USE_APPLY_L_CODE",i + 1 + ((dtlList == null || dtlList.size() == 0) ? 0 : dtlList.get(0).getAsInteger("USE_APPLY_L_CODE")));
					dto.put("CREATION_DATE",createDate);
					dto.put("CREATED_BY",userId);
					dto.put("LAST_UPDATE_DATE",createDate);
					dto.put("LAST_UPDATED_BY",userId);
				}
				insertDtl.put("dbType",PlatformUtil.getDbType());
				insertDtl.put("list",insertDtlList);
				JsonHelper.parseJSONNull2Null(insertDtlList);
				xcStUseApplyMapper.insertXcStUseApplyL(insertDtl);
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
				xcStUseApplyMapper.updateXcStUseApplyL(updateDtl);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void deleteXcStUseApplyH(List<String> stUseApplyHIds) throws Exception {
		try {
			xcStUseApplyMapper.deleteXcStUseApplyLByHId(stUseApplyHIds);
			xcStUseApplyMapper.deleteXcStUseApplyH(stUseApplyHIds);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void updateXcStUseApplyHCloseStatus(List<String> stUseApplyHIds) throws Exception {
		try {
			xcStUseApplyMapper.updateXcStUseApplyHCloseStatus(stUseApplyHIds);
		} catch (Exception e) {
			throw e;
		}
	}
}