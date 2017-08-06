package com.xzsoft.xc.apar.dao;

import java.util.List;

import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentLBean;

/** 
 * @ClassName: ApDao 
 * @Description: TODO
 * @author weicw 
 * @date 2016年9月2日 下午4:25:17 
 * 
 * 
 */
public interface ApArDao {
	/*
	 *  查询应付单主表信息 
	 */
	public ApDocumentHBean getApInvGlH(String apInvGlHId);
	/*
	 * 查询应付单行表信息
	 */
	public List<ApDocumentLBean> getApInvGlL(String apInvGlHId);
	/*
	 *  查询应收单主表信息 
	 */
	public ArDocumentHBean getArInvGlH(String arInvGlHId);
	/*
	 * 查询应收单行表信息
	 */
	public List<ArDocumentLBean> getArInvGlL(String arInvGlHId);
}

