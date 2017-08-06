package com.xzsoft.xc.ap.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApCheckUtilDao;
import com.xzsoft.xc.ap.mapper.ApCheckUtilMapper;
import com.xzsoft.xc.ap.modal.ApCheckUtilBean;
/**
 * 
  * @ClassName ApCheckUtilDaoImpl
  * @Description 应付模块检查验证Dao层接口实现类
  * @author 任建建
  * @date：2016年6月28日 上午9:04:28
 */
@Repository("ApCheckUtilDao")
public class ApCheckUtilDaoImpl implements ApCheckUtilDao{
	@Resource
	ApCheckUtilMapper apCheckUtilMapper;
	/*
	 * 
	  * <p>Title selectGlNoPayAmtByInvId</p>
	  * <p>Description 根据发票ID，获取应付单的金额信息</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#selectGlNoPayAmtByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean selectGlNoPayAmtByInvId(String apInvHId) throws Exception {
		return apCheckUtilMapper.selectGlNoPayAmtByInvId(apInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateInvHcByInvId</p>
	  * <p>Description 根据发票ID，查询该发票是否做过红冲</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkInvGlCreateInvHcByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkInvGlCreateInvHcByInvId(String apInvHId) throws Exception {
		return apCheckUtilMapper.checkInvGlCreateInvHcByInvId(apInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateGlHcByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应付单是否做过红冲</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkInvGlCreateGlHcByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkInvGlCreateGlHcByInvId(String apInvHId) throws Exception {
		return apCheckUtilMapper.checkInvGlCreateGlHcByInvId(apInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateReqPayByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应付单是否做过付款申请</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkInvGlCreateReqPayByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkInvGlCreateReqPayByInvId(String apInvHId) throws Exception {
		return apCheckUtilMapper.checkInvGlCreateReqPayByInvId(apInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreatePayByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应付单是否做过付款</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkInvGlCreatePayByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkInvGlCreatePayByInvId(String apInvHId) throws Exception {
		return apCheckUtilMapper.checkInvGlCreatePayByInvId(apInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateCancelHByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应付单是否做过核销单主信息</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkInvGlCreateCancelHByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkInvGlCreateCancelHByInvId(String apInvHId) throws Exception {
		return apCheckUtilMapper.checkInvGlCreateCancelHByInvId(apInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateCancelLByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应付单是否做过核销单明细</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkInvGlCreateCancelLByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkInvGlCreateCancelLByInvId(String apInvHId) throws Exception {
		return apCheckUtilMapper.checkInvGlCreateCancelLByInvId(apInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateAdjByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应付单是否做过调整单</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkInvGlCreateAdjByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkInvGlCreateAdjByInvId(String apInvHId) throws Exception {
		return apCheckUtilMapper.checkInvGlCreateAdjByInvId(apInvHId);
	}
	/*
	 * 
	  * <p>Title selectInvStatus</p>
	  * <p>Description 根据发票ID，查询发票的流程状态和业务状态</p>
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#selectInvStatus(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean selectInvStatus(String apInvHId) throws Exception {
		return apCheckUtilMapper.selectInvStatus(apInvHId);
	}
	/*
	 * 
	  * <p>Title selectGlNoPayAmtByGlId</p>
	  * <p>Description 根据应付单ID，查询应付单的金额信息</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#selectGlNoPayAmtByGlId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean selectGlNoPayAmtByGlId(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.selectGlNoPayAmtByGlId(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlVStatus</p>
	  * <p>Description 根据应付单ID，应付单的凭证状态是否是审核通过状态</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkGlVStatus(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkGlVStatus(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.checkGlVStatus(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateByInvId</p>
	  * <p>Description 根据应付单ID，判断当前应付单是否由发票复核生成</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkGlCreateByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkGlCreateByInvId(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.checkGlCreateByInvId(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateInvHcByInvId</p>
	  * <p>Description 根据应付单ID，判断当前应付单对应的发票是否做过红冲</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkGlCreateInvHcByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkGlCreateInvHcByInvId(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.checkGlCreateInvHcByInvId(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateGlHcByInvId</p>
	  * <p>Description 根据应付单ID，判断当前应付单是否做过红冲</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkGlCreateGlHcByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkGlCreateGlHcByInvId(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.checkGlCreateGlHcByInvId(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreatePayReqByInvId</p>
	  * <p>Description 根据应付单ID，判断当前应付单是否做过付款申请</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkGlCreatePayReqByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkGlCreatePayReqByInvId(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.checkGlCreatePayReqByInvId(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreatePayByInvId</p>
	  * <p>Description 根据应付单ID，判断当前应付单是否做过付款</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkGlCreatePayByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkGlCreatePayByInvId(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.checkGlCreatePayByInvId(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateCancelHByInvId</p>
	  * <p>Description 根据应付单ID，判断当前应付单是否被别的应付单核销过</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkGlCreateCancelHByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkGlCreateCancelHByInvId(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.checkGlCreateCancelHByInvId(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateCancelLByInvId</p>
	  * <p>Description 根据应付单ID，判断当前应付单是核销过别的应付单</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkGlCreateCancelLByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkGlCreateCancelLByInvId(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.checkGlCreateCancelLByInvId(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateAdjByInvId</p>
	  * <p>Description 根据应付单ID，判断当前应付单是否做过调整单</p>
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkGlCreateAdjByInvId(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkGlCreateAdjByInvId(String apInvGlHId) throws Exception {
		return apCheckUtilMapper.checkGlCreateAdjByInvId(apInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkPayVStatus</p>
	  * <p>Description 根据付款单ID ，查询付款单的凭证状态是否是审核通过状态</p>
	  * @param apPayHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#checkPayVStatus(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean checkPayVStatus(String apPayHId) throws Exception {
		return apCheckUtilMapper.checkPayVStatus(apPayHId);
	}
	/*
	 * 
	  * <p>Title selectReqPayStatus</p>
	  * <p>Description 根据付款申请ID，查询付款申请的流程状态和业务状态</p>
	  * @param apReqPayHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApCheckUtilDao#selectReqPayStatus(java.lang.String)
	 */
	@Override
	public ApCheckUtilBean selectReqPayStatus(String apReqPayHId) throws Exception {
		return apCheckUtilMapper.selectReqPayStatus(apReqPayHId);
	}
	@Override
	public int checkPayCreateHcByPayId(String apPayHId) throws Exception {
		return apCheckUtilMapper.checkPayCreateHcByPayId(apPayHId);
	}
	@Override
	public int checkPayCreateCancelByPayId(String apPayHId) throws Exception {
		return apCheckUtilMapper.checkPayCreateCancelByPayId(apPayHId);
	}
	@Override
	public ApCheckUtilBean checkArApCancelByInvId(String AP_INV_GL_H_ID) throws Exception {
		return apCheckUtilMapper.checkArApCancelByInvId(AP_INV_GL_H_ID);
	}
}