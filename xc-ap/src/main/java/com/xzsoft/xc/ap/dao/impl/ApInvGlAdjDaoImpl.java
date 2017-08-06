package com.xzsoft.xc.ap.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApInvGlAdjDao;
import com.xzsoft.xc.ap.mapper.ApInvGlAdjMapper;
import com.xzsoft.xc.ap.modal.ApInvGlAdj;
import com.xzsoft.xc.ap.modal.ApInvGlHBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
 


/**
 * 
  * @ClassName：ApInvGlAdjDaoImpl
  * @Description：调整单Dao层接口实现类
  * @author：韦才文
  * @date：2016年7月22日 上午10:05:00
 */
@Repository("apInvGlAdjDao")
public class ApInvGlAdjDaoImpl implements ApInvGlAdjDao {
	@Resource
	ApInvGlAdjMapper mapper;
	/**
	 * @Title:addApAdjInfo
	 * @Description:添加调整单信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApInvGlAdj
	 * @return 
	 * @throws 
	 */
	@Override
	public void addApAdjInfo(ApInvGlAdj apInvGlAdj) {
		mapper.addApAdjInfo(apInvGlAdj);
		
	}
	/**
	 * @Title:addInvTrans
	 * @Description:添加交易明细表信息
	 * 参数格式:
	 * @param  com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void addInvTrans(ApInvTransBean apInvTransBean) {
		mapper.addInvTrans(apInvTransBean);
		
	}
	/**
	 * @Title:updateInvGlH
	 * @Description:更新应付单
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateInvGlH(HashMap<String, Object> map) {
		mapper.updateInvGlH(map);
		
	}


	/**
	 * @Title:isAddOrEdit
	 * @Description:判断新增或修改调整单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String isAddOrEdit(String glAdjId) {
		return mapper.isAddOrEdit(glAdjId);
	}
	/**
	 * @Title:updateInvGlhadj
	 * @Description:修改调整单
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApInvGlAdj
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateInvGlhadj(ApInvGlAdj apInvGLAdj) {
		mapper.updateInvGlhadj(apInvGLAdj);
		
	}
	/**
	 * @TitleupdateInvtrans
	 * @Description:修改往来明细
	 * 参数格式:
	 * @param  com.xzsoft.xc.apar.modal.ApInvTransBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateInvtrans(ApInvTransBean apInvTransBean) {
		mapper.updateInvtrans(apInvTransBean);
		
	}
	/**
	 * @Title updateInvglh
	 * @Description: 更新应付单
	 * 参数格式:
	 * @param  com.xzsoft.xc.ap.modal.ApInvGlHBean
	 * @return 
	 * @throws 
	 */
	@Override
	public void updateInvglh(ApInvGlHBean apInvGlHBean) {
		mapper.updateInvglh(apInvGlHBean);
		
	}
	/**
	 * @Title:delApInvAdj
	 * @Description: 删除调整单
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	@Override
	public void delApInvAdj(List<String> list) {
		mapper.delApInvAdj(list);
	}
	/**
	 * @Title:delApInvTrans
	 * @Description: 删除交易明细
	 * 参数格式:
	 * @param java.util.List
	 * @return
	 * @throws 
	 */
	@Override
	public void delApInvTrans(List<String> list) {
		mapper.delApInvTrans(list);
	}
	/**
	 * @Title: updateInvByDel
	 * @Description: 更新应付单（删除时）
	 * 参数格式:
	 * @param java.util.HashMap
	 * @return
	 * @throws 
	 */
	@Override
	public void updateInvByDel(HashMap<String, Object> map) {
		mapper.updateInvByDel(map);
	}
	/**
	 * @Title getInvAdjInfo
	 * @Description:查询调整单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApInvGlAdj
	 * @throws 
	 */
	@Override
	public ApInvGlAdj getInvAdjInfo(String invAdjId){
		return mapper.getInvAdjInfo(invAdjId);
	}
}

