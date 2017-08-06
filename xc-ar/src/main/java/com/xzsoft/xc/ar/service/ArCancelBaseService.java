package com.xzsoft.xc.ar.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.modal.ArCancelHBean;
import com.xzsoft.xc.ar.modal.ArCancelLBean;

/** 
 * @className ArCancelBaseService
 * @Description 核销单Service
 * @author 韦才文
 * @version 创建时间：2016年7月13日 上午9:57:58 
 */
public interface ArCancelBaseService {
	/*
	 * 保存核销单
	 */
	public JSONObject saveCancel(String str,String deleteDtl,String gridStr,String lang) throws Exception;
	/*
	 * 凭证审核
	 */
	public JSONObject checkArVoucher(String arInfo,String lang) throws Exception;
	/*
	 * 取消凭证审核
	 */
	public JSONObject cancelCheckArVoucher(String arInfo,String lang) throws Exception;
	/*
	 * 保存明细
	 */
	public void saveDtl(JSONArray jsonGrid,String arCancelType,String deleteDtl,String cancelCode,String glDate,String description) throws Exception;
	/*
	 * 删除核销单
	 */
	public JSONObject deleteCancel(String rows,String arInfo,String lang) throws Exception;
}
