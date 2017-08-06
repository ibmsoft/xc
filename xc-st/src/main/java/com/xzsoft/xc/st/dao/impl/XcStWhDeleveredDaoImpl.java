package com.xzsoft.xc.st.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.st.common.JsonHelper;
import com.xzsoft.xc.st.dao.XcStCommonDao;
import com.xzsoft.xc.st.dao.XcStWhDeleveredDao;
import com.xzsoft.xc.st.mapper.XcStCommonMapper;
import com.xzsoft.xc.st.mapper.XcStWhDeleveredMapper;
import com.xzsoft.xc.st.modal.BaseDto;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName XcStWhDeleveredDaoImpl
  * @Description 处理出库及退库主表的dao接口实现类
  * @author RenJianJian
  * @date 2016年12月2日 下午1:03:50
 */
@Repository("xcStWhDeleveredDao")
public class XcStWhDeleveredDaoImpl implements XcStWhDeleveredDao {
	@Resource
	private XcStWhDeleveredMapper xcStWhDeleveredMapper;
	@Resource
	private XcStCommonMapper xcStCommonMapper;
	@Resource
	private XcStCommonDao xcStCommonDao;
	@Override
	public Dto selectXcStWhDeleveredH(String DELEVERED_H_ID) throws Exception {
		try {
			//主信息
			return xcStWhDeleveredMapper.selectXcStWhDeleveredH(DELEVERED_H_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Dto selectXcStWhDeleveredL(String DELEVERED_L_ID) throws Exception {
		try {
			//主信息
			return xcStWhDeleveredMapper.selectXcStWhDeleveredL(DELEVERED_L_ID);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Dto> selectXcStWhDeleveredLByHId(String DELEVERED_H_ID) throws Exception {
		try {
			//主信息
			return xcStWhDeleveredMapper.selectXcStWhDeleveredLByHId(DELEVERED_H_ID);
		} catch (Exception e) {
			throw e;
		}}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void insertXcStWhDeleveredH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//往入库及退货货位表插入数据
			ArrayList bLocationInsertList = new ArrayList();
			//主信息
			xcStWhDeleveredMapper.insertXcStWhDeleveredH(pDto);
			ArrayList insertDtlList = (ArrayList) pDto.get("insertDtlList");
			Dto insertDtl = new BaseDto();
			for (int i = 0;i < insertDtlList.size();i++) {
				Dto dto = (Dto)insertDtlList.get(i);
				dto.put("DELEVERED_L_CODE",i + 1);
				dto.put("CREATION_DATE",createDate);
				dto.put("CREATED_BY",userId);
				dto.put("LAST_UPDATE_DATE",createDate);
				dto.put("LAST_UPDATED_BY",userId);
				//往货位表插入数据
				if(!dto.getAsString("LOCATION_ID").isEmpty()){
					dto.put("B_LOCATION_ID",java.util.UUID.randomUUID().toString());
					dto.put("BUSINESS_H_ID",dto.getAsString("DELEVERED_H_ID"));
					dto.put("BUSINESS_L_ID",dto.getAsString("DELEVERED_L_ID"));
					bLocationInsertList.add(dto);
				}
			}
			insertDtl.put("dbType",PlatformUtil.getDbType());
			insertDtl.put("list",insertDtlList);
			JsonHelper.parseJSONNull2Null(insertDtlList);
			xcStWhDeleveredMapper.insertXcStWhDeleveredL(insertDtl);
			if(bLocationInsertList != null && bLocationInsertList.size() > 0){
				//记录入库入库及退货货位表
				insertDtl.put("dbType",PlatformUtil.getDbType());
				insertDtl.put("list",bLocationInsertList);
				xcStCommonDao.insertXcStWhBLocation(insertDtl);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateXcStWhDeleveredH(Dto pDto) throws Exception {
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			//更新主表信息
			xcStWhDeleveredMapper.updateXcStWhDeleveredH(pDto);
			//删除明细行
			ArrayList deleteDtlList = (ArrayList) pDto.get("deleteDtlList");
			if(deleteDtlList!=null && deleteDtlList.size()>0){
				Dto deleteDtl = new BaseDto();
				deleteDtl.put("dbType",PlatformUtil.getDbType());
				deleteDtl.put("list",deleteDtlList);
				JsonHelper.parseJSONNull2Null(deleteDtlList);
				xcStWhDeleveredMapper.deleteXcStWhDeleveredLByLId(deleteDtl);
				for (int i = 0;i < deleteDtlList.size();i++) {
					Dto dto = (Dto)deleteDtlList.get(i);
					dto.put("BUSINESS_L_ID",dto.getAsString("DELEVERED_L_ID"));
				}
				//删除出库及退库货位表
				xcStCommonMapper.deleteXcStWhBLocationByLId(deleteDtl);
				//删除出库及退库批次OR序列号表
				xcStCommonMapper.deleteXcStWhBSerByLId(deleteDtl);
			}
			//新增明细行
			ArrayList insertDtlList = (ArrayList) pDto.get("insertDtlList");
			if(insertDtlList != null && insertDtlList.size() > 0){
				//通过主ID得到行信息，从而对行号进行控制
				List<Dto> dtlList = selectXcStWhDeleveredLByHId(pDto.getAsString("DELEVERED_H_ID"));
				//往入库及退货货位表插入数据
				ArrayList bLocationInsertList = new ArrayList();
				Dto insertDtl = new BaseDto();
				for (int i = 0;i < insertDtlList.size();i++) {
					Dto dto = (Dto)insertDtlList.get(i);
					dto.put("DELEVERED_L_CODE",i + 1 + ((dtlList == null || dtlList.size() == 0) ? 0 : dtlList.get(0).getAsInteger("DELEVERED_L_CODE")));
					dto.put("CREATION_DATE",createDate);
					dto.put("CREATED_BY",userId);
					dto.put("LAST_UPDATE_DATE",createDate);
					dto.put("LAST_UPDATED_BY",userId);
					//往货位表插入数据
					if(!dto.getAsString("LOCATION_ID").isEmpty()){
						dto.put("B_LOCATION_ID",java.util.UUID.randomUUID().toString());
						dto.put("BUSINESS_H_ID",dto.getAsString("DELEVERED_H_ID"));
						dto.put("BUSINESS_L_ID",dto.getAsString("DELEVERED_L_ID"));
						bLocationInsertList.add(dto);
					}
				}
				insertDtl.put("dbType",PlatformUtil.getDbType());
				insertDtl.put("list",insertDtlList);
				JsonHelper.parseJSONNull2Null(insertDtlList);
				xcStWhDeleveredMapper.insertXcStWhDeleveredL(insertDtl);
				if(bLocationInsertList != null && bLocationInsertList.size() > 0){
					//记录入库入库及退货货位表
					insertDtl.put("dbType",PlatformUtil.getDbType());
					insertDtl.put("list",bLocationInsertList);
					xcStCommonDao.insertXcStWhBLocation(insertDtl);
				}
			}
			//更新明细行
			ArrayList updateDtlList = (ArrayList) pDto.get("updateDtlList");
			if(updateDtlList!=null && updateDtlList.size()>0){
				Dto updateDtl = new BaseDto();
				//往入库及退货货位表插入数据
				ArrayList bLocationUpdateList = new ArrayList();
				for (int i = 0;i < updateDtlList.size();i++) {
					Dto dto = (Dto)updateDtlList.get(i);
					dto.put("LAST_UPDATE_DATE",createDate);
					dto.put("LAST_UPDATED_BY",userId);
					//往货位表更新数据
					if(!dto.getAsString("LOCATION_ID").isEmpty()){
						dto.put("BUSINESS_H_ID",dto.getAsString("ENTRY_H_ID"));
						dto.put("BUSINESS_L_ID",dto.getAsString("ENTRY_L_ID"));
						bLocationUpdateList.add(dto);
					}
				}
				updateDtl.put("dbType",PlatformUtil.getDbType());
				updateDtl.put("list",updateDtlList);
				JsonHelper.parseJSONNull2Null(updateDtlList);
				xcStWhDeleveredMapper.updateXcStWhDeleveredL(updateDtl);
				if(bLocationUpdateList != null && bLocationUpdateList.size() > 0){
					//记录入库入库及退货货位表
					updateDtl.put("dbType",PlatformUtil.getDbType());
					updateDtl.put("list",bLocationUpdateList);
					xcStCommonDao.updateXcStWhBLocation(updateDtl);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void deleteXcStWhDeleveredH(List<String> stWhDeleveredHIds) throws Exception {
		try {
			//删除出库及退库行表
			xcStWhDeleveredMapper.deleteXcStWhDeleveredLByHId(stWhDeleveredHIds);
			//删除出库及退库主表
			xcStWhDeleveredMapper.deleteXcStWhDeleveredH(stWhDeleveredHIds);
			//删除出库及退库货位表
			xcStCommonMapper.deleteXcStWhBLocationByHId(stWhDeleveredHIds);
			//删除出库及退库批次OR序列号表
			xcStCommonMapper.deleteXcStWhBSerByHId(stWhDeleveredHIds);
		} catch (Exception e) {
			throw e;
		}
	}
}