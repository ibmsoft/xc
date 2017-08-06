package com.xzsoft.xc.ar.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ar.dao.ArCheckUtilDao;
import com.xzsoft.xc.ar.mapper.ArCheckUtilMapper;
import com.xzsoft.xc.ar.modal.ArInvGlHBean;
import com.xzsoft.xc.ar.modal.ArInvoiceHBean;
/**
 * 
  * @ClassName ArCheckUtilDaoImpl
  * @Description 应收模块检查验证Dao层接口实现类
  * @author RenJianJian
  * @date 2016年7月11日 下午4:13:19
 */
@Repository("arCheckUtilDao")
public class ArCheckUtilDaoImpl implements ArCheckUtilDao{
	@Resource
	ArCheckUtilMapper arCheckUtilMapper;
	/*
	 * 
	  * <p>Title selectGlNoPayAmtByInvId</p>
	  * <p>Description 根据发票ID，获取应收单的金额信息</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#selectGlNoPayAmtByInvId(java.lang.String)
	 */
	@Override
	public ArInvGlHBean selectGlNoPayAmtByInvId(String arInvHId) throws Exception {
		return arCheckUtilMapper.selectGlNoPayAmtByInvId(arInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateInvHcByInvId</p>
	  * <p>Description 根据发票ID，查询该发票是否做过红冲</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkInvGlCreateInvHcByInvId(java.lang.String)
	 */
	@Override
	public int checkInvGlCreateInvHcByInvId(String arInvHId) throws Exception {
		return arCheckUtilMapper.checkInvGlCreateInvHcByInvId(arInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateGlHcByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应收单是否做过红冲</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkInvGlCreateGlHcByInvId(java.lang.String)
	 */
	@Override
	public int checkInvGlCreateGlHcByInvId(String arInvHId) throws Exception {
		return arCheckUtilMapper.checkInvGlCreateGlHcByInvId(arInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreatePayByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应收单是否做过付款</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkInvGlCreatePayByInvId(java.lang.String)
	 */
	@Override
	public int checkInvGlCreatePayByInvId(String arInvHId) throws Exception {
		return arCheckUtilMapper.checkInvGlCreatePayByInvId(arInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateCancelHByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应收单是否做过核销单主信息</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkInvGlCreateCancelHByInvId(java.lang.String)
	 */
	@Override
	public int checkInvGlCreateCancelHByInvId(String arInvHId) throws Exception {
		return arCheckUtilMapper.checkInvGlCreateCancelHByInvId(arInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateCancelLByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应收单是否做过核销单明细</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkInvGlCreateCancelLByInvId(java.lang.String)
	 */
	@Override
	public int checkInvGlCreateCancelLByInvId(String arInvHId) throws Exception {
		return arCheckUtilMapper.checkInvGlCreateCancelLByInvId(arInvHId);
	}
	/*
	 * 
	  * <p>Title checkInvGlCreateAdjByInvId</p>
	  * <p>Description 根据发票ID，查询该发票生成的应收单是否做过调整单</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkInvGlCreateAdjByInvId(java.lang.String)
	 */
	@Override
	public int checkInvGlCreateAdjByInvId(String arInvHId) throws Exception {
		return arCheckUtilMapper.checkInvGlCreateAdjByInvId(arInvHId);
	}
	/*
	 * 
	  * <p>Title selectInvStatus</p>
	  * <p>Description 根据发票ID，查询发票的流程状态和业务状态</p>
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#selectInvStatus(java.lang.String)
	 */
	@Override
	public ArInvoiceHBean selectInvStatus(String arInvHId) throws Exception {
		return arCheckUtilMapper.selectInvStatus(arInvHId);
	}
	/*
	 * 
	  * <p>Title selectGlNoPayAmtByGlId</p>
	  * <p>Description 根据应收单ID，查询应收单的金额信息</p>
	  * @param arInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#selectGlNoPayAmtByGlId(java.lang.String)
	 */
	@Override
	public ArInvGlHBean selectGlNoPayAmtByGlId(String arInvGlHId) throws Exception {
		return arCheckUtilMapper.selectGlNoPayAmtByGlId(arInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlVStatus</p>
	  * <p>Description 根据应收单ID，应收单的凭证状态是否是审核通过状态</p>
	  * @param arInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkGlVStatus(java.lang.String)
	 */
	@Override
	public int checkGlVStatus(String arInvGlHId) throws Exception {
		return arCheckUtilMapper.checkGlVStatus(arInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateByInvId</p>
	  * <p>Description 根据应收单ID，判断当前应收单是否由发票复核生成</p>
	  * @param arInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkGlCreateByInvId(java.lang.String)
	 */
	@Override
	public int checkGlCreateByInvId(String arInvGlHId) throws Exception {
		return arCheckUtilMapper.checkGlCreateByInvId(arInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateInvHcByInvId</p>
	  * <p>Description 根据应收单ID，判断当前应收单对应的发票是否做过红冲</p>
	  * @param arInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkGlCreateInvHcByInvId(java.lang.String)
	 */
	@Override
	public int checkGlCreateInvHcByInvId(String arInvGlHId) throws Exception {
		return arCheckUtilMapper.checkGlCreateInvHcByInvId(arInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateGlHcByInvId</p>
	  * <p>Description 根据应收单ID，判断当前应收单是否做过红冲</p>
	  * @param arInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkGlCreateGlHcByInvId(java.lang.String)
	 */
	@Override
	public int checkGlCreateGlHcByInvId(String arInvGlHId) throws Exception {
		return arCheckUtilMapper.checkGlCreateGlHcByInvId(arInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreatePayByInvId</p>
	  * <p>Description 根据应收单ID，判断当前应收单是否做过付款</p>
	  * @param arInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkGlCreatePayByInvId(java.lang.String)
	 */
	@Override
	public int checkGlCreatePayByInvId(String arInvGlHId) throws Exception {
		return arCheckUtilMapper.checkGlCreatePayByInvId(arInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateCancelHByInvId</p>
	  * <p>Description 根据应收单ID，判断当前应收单是否被别的应收单核销过</p>
	  * @param arInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkGlCreateCancelHByInvId(java.lang.String)
	 */
	@Override
	public int checkGlCreateCancelHByInvId(String arInvGlHId) throws Exception {
		return arCheckUtilMapper.checkGlCreateCancelHByInvId(arInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateCancelLByInvId</p>
	  * <p>Description 根据应收单ID，判断当前应收单是核销过别的应收单</p>
	  * @param arInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkGlCreateCancelLByInvId(java.lang.String)
	 */
	@Override
	public int checkGlCreateCancelLByInvId(String arInvGlHId) throws Exception {
		return arCheckUtilMapper.checkGlCreateCancelLByInvId(arInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkGlCreateAdjByInvId</p>
	  * <p>Description 根据应收单ID，判断当前应收单是否做过调整单</p>
	  * @param arInvGlHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkGlCreateAdjByInvId(java.lang.String)
	 */
	@Override
	public int checkGlCreateAdjByInvId(String arInvGlHId) throws Exception {
		return arCheckUtilMapper.checkGlCreateAdjByInvId(arInvGlHId);
	}
	/*
	 * 
	  * <p>Title checkPayVStatus</p>
	  * <p>Description 根据收款单ID ，查询收款单的凭证状态是否是审核通过状态</p>
	  * @param arPayHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkPayVStatus(java.lang.String)
	 */
	@Override
	public int checkPayVStatus(String arPayHId) throws Exception {
		return arCheckUtilMapper.checkPayVStatus(arPayHId);
	}
	/*
	 * 
	  * <p>Title checkPayCreateHcByPayId</p>
	  * <p>Description 通过收款单ID查询收款单是否做过红冲</p>
	  * @param arPayHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkPayCreateHcByPayId(java.lang.String)
	 */
	@Override
	public int checkPayCreateHcByPayId(String arPayHId) throws Exception {
		return arCheckUtilMapper.checkPayCreateHcByPayId(arPayHId);
	}
	/*
	 * 
	  * <p>Title checkPayCreateCancelByPayId</p>
	  * <p>Description 通过收款单ID查询收款单是否做过核销</p>
	  * @param arPayHId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArCheckUtilDao#checkPayCreateCancelByPayId(java.lang.String)
	 */
	@Override
	public int checkPayCreateCancelByPayId(String arPayHId) throws Exception {
		return arCheckUtilMapper.checkPayCreateCancelByPayId(arPayHId);
	}
	@Override
	public int checkApArCancelByInvId(String AR_INV_GL_H_ID) throws Exception {
		return arCheckUtilMapper.checkApArCancelByInvId(AR_INV_GL_H_ID);
	}
}