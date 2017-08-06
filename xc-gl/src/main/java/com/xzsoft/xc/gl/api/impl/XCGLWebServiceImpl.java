package com.xzsoft.xc.gl.api.impl;

import java.util.HashMap;

import javax.jws.WebService;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.xzsoft.xc.gl.api.XCGLWebService;
import com.xzsoft.xc.gl.service.GLVoucherHandlerService;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.gl.service.XCGLFetchDataService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: XCGLWebServiceImpl 
 * @Description: 总账系统对外服务API接口实现类
 * @author linp
 * @date 2016年3月16日 上午10:30:31 
 *
 */
@Component
@WebService(endpointInterface = "com.xzsoft.xc.gl.api.XCGLWebService",targetNamespace = "http://ws.xzsoft.com")
public class XCGLWebServiceImpl implements XCGLWebService {
	// 日志记录器
	private static final Logger log = Logger.getLogger(XCGLWebServiceImpl.class.getName()) ;
	
	private VoucherHandlerService getVoucherHandlerService(){
		return (VoucherHandlerService) AppContext.getApplicationContext().getBean("voucherHandlerService") ;
	}

	private GLVoucherHandlerService getGLVoucherHandlerService(){
		return (GLVoucherHandlerService) AppContext.getApplicationContext().getBean("glVoucherHandlerService") ;
	}
	
	private XCGLFetchDataService getXCGLFetchDataService(){
		return (XCGLFetchDataService) AppContext.getApplicationContext().getBean("xCGLFetchDataService") ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgers</p> 
	 * <p>Description: 同步账簿信息 </p> 
	 * @param lastUpdateDate
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#getLedgers(java.lang.String, java.lang.String)
	 */
	@Override
	public String getLedgers(String lastUpdateDate, String userName, String userPwd) {
		String returnMsg = "";
		String maxDate = "";
		try {
			String lang =  XCUtil.getLang();
			HashMap<String, String> map = this.getXCGLFetchDataService().fecthLedgerData(lastUpdateDate, userName, userPwd,"reportFecth");
		    if(map != null){
		    	maxDate = map.get("synchronizeDate");
		    	switch (lang) {
				case XConstants.ZH:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"取数成功！\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				case XConstants.EN:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"It is success to take the data\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				default:
					break;
				}
		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用XCGLWebService.getLedgers获取账簿信息异常，异常信息："+e.getMessage());
			returnMsg = "{\"flag\":1,\"data\":\"\",\"msg\":\""+e.getMessage()+"\",\"synchronizedDate\":\""+maxDate+"\"}";
		}
		return returnMsg;
	
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getDimensions</p> 
	 * <p>Description: 同步维度信息 </p> 
	 * @param dimType
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#getDimensions(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getDimensions(String dimType, String lastUpdateDate, String ledgerCode, String userName, String userPwd) {
		String returnMsg = "";
		String maxDate = "";
		try {
			String lang = XCUtil.getLang();
			HashMap<String, String> map = this.getXCGLFetchDataService().fecthDimensionsData(dimType, lastUpdateDate, ledgerCode, userName, userPwd, "reportFecth");
		    if(map != null){
		    	maxDate = map.get("synchronizeDate");
		    	switch (lang) {
				case XConstants.ZH:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"取数成功！\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				case XConstants.EN:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"It is success to take the data\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				default:
					break;
				}
		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用XCGLWebService.fecthDimensionsData获取科目段信息异常，异常信息："+e.getMessage());
			returnMsg = "{\"flag\":1,\"data\":\"\",\"msg\":\""+e.getMessage()+"\",\"synchronizedDate\":\""+maxDate+"\"}";
		}
		return returnMsg;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAccounts</p> 
	 * <p>Description:科目信息同步处理  </p> 
	 * @param lastUpdateDate
	 * @param hrcyCode
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#getAccounts(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getAccounts(String lastUpdateDate, String hrcyCode, String userName, String userPwd) {
		String returnMsg = "";
		String maxDate = "";
		try {
			String lang = XCUtil.getLang();//语言值
			HashMap<String, String> map = this.getXCGLFetchDataService().fecthAccountData(lastUpdateDate, hrcyCode, userName, userPwd,"reportFecth") ;
		    if(map != null){
		    	maxDate = map.get("synchronizeDate");
		    	switch (lang) {
				case XConstants.ZH:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"取数成功！\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				case XConstants.EN:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"It is success to take the data\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				default:
					break;
				}
		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用XCGLWebService.fecthDimensionsData获取科目段信息异常，异常信息："+e.getMessage());
			returnMsg = "{\"flag\":1,\"data\":\"\",\"msg\":\""+e.getMessage()+"\",\"synchronizedDate\":\""+maxDate+"\"}";
		}
		return returnMsg;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getCCID</p> 
	 * <p>Description: 同步科目组合信息</p> 
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#getCCID(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getCCID(String lastUpdateDate, String ledgerCode, String userName, String userPwd) {
		String returnMsg = "";
		String maxDate = "";
		try {
			String lang = XCUtil.getLang();
			HashMap<String, String> map = this.getXCGLFetchDataService().fecthCcidData(lastUpdateDate, ledgerCode, userName, userPwd, "reportFecth");
		    if(map != null){
		    	maxDate = map.get("synchronizeDate");
		    	switch (lang) {
				case XConstants.ZH:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"取数成功！\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				case XConstants.EN:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"It is success to take the data\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				default:
					break;
				}
		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用XCGLWebService.getCCID获取科目组合信息异常，异常信息："+e.getMessage());
			returnMsg = "{\"flag\":1,\"data\":\"\",\"msg\":\""+e.getMessage()+"\",\"synchronizedDate\":\""+maxDate+"\"}";
		}
		return returnMsg;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getBalance</p> 
	 * <p>Description: 同步总账余额信息 </p> 
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#getBalance(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getBalance(String lastUpdateDate, String ledgerCode, String userName, String userPwd) {
		String returnMsg = "";
		String maxDate = "";
		try {
			String lang = XCUtil.getLang();//语言值
			HashMap<String, String> map = this.getXCGLFetchDataService().fecthBalancesData(lastUpdateDate, ledgerCode, userName, userPwd, "reportFecth");
		    if(map != null){
		    	maxDate = map.get("synchronizeDate");
		    	switch (lang) {
				case XConstants.ZH:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"取数成功！\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				case XConstants.EN:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"It is success to take the data\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				default:
					break;
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用XCGLWebService.getBalance获取余额信息异常，异常信息："+e.getMessage());
			returnMsg = "{\"flag\":1,\"data\":\"\",\"msg\":\""+e.getMessage()+"\",\"synchronizedDate\":\""+maxDate+"\"}";
		}
		return returnMsg;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getCashSum</p> 
	 * <p>Description: 同步现金流信息  </p> 
	 * @param lastUpdateDate
	 * @param ledgerCode
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#getCashSum(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getCashSum(String lastUpdateDate, String ledgerCode, String userName, String userPwd) {
		String returnMsg = "";
		String maxDate = "";
		try {
			String lang = XCUtil.getLang();//语言值
			HashMap<String, String> map = this.getXCGLFetchDataService().fecthCashSumData(lastUpdateDate,ledgerCode,userName, userPwd,"reportFecth");
		    if(map != null){
		    	maxDate = map.get("synchronizeDate");
		    	switch (lang) {
				case XConstants.ZH:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"取数成功！\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				case XConstants.EN:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"It is success to take the data\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				default:
					break;
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用XCGLWebService.getCashSum获取现金流信息异常，异常信息："+e.getMessage());
			returnMsg = "{\"flag\":1,\"data\":\"\",\"msg\":\""+e.getMessage()+"\",\"synchronizedDate\":\""+maxDate+"\"}";
		}
		return returnMsg;
	}

	@Override
	public String getCashItem(String lastUpdateDate, String userName,
			String userPwd) {
		String returnMsg = "";
		String maxDate = "";
		try {
			HashMap<String, String> map = this.getXCGLFetchDataService().fetchCashItemDate(lastUpdateDate,userName, userPwd,"reportFecth");
		    String lang = XCUtil.getLang();
			if(map != null){
		    	maxDate = map.get("synchronizeDate");
		    	switch (lang) {
				case XConstants.ZH:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"取数成功！\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				case XConstants.EN:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"It is success to take the data\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				default:
					break;
				}
		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用XCGLWebService.getCashItem获取现金流项目异常，异常信息："+e.getMessage());
			returnMsg = "{\"flag\":1,\"data\":\"\",\"msg\":\""+e.getMessage()+"\",\"synchronizedDate\":\""+maxDate+"\"}";
		}
		return returnMsg;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getVouchers</p> 
	 * <p>Description: 同步凭证信息 </p> 
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#getVouchers(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getVouchers(String lastUpdateDate, String ledgerCode, String userName, String userPwd) {
		String returnMsg = "";
		String maxDate = "";
		try {
			String lang = XCUtil.getLang();
			HashMap<String, String> map = this.getXCGLFetchDataService().fecthVoucherData(lastUpdateDate, ledgerCode, userName, userPwd, "reportFecth");
		    if(map != null){
		    	maxDate = map.get("synchronizeDate");
		    	switch (lang) {
				case XConstants.ZH:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"取数成功！\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				case XConstants.EN:
					returnMsg = "{\"flag\":0,\"data\":"+map.get("data")+",\"msg\":\"It is success to take the data\",\"synchronizedDate\":\""+maxDate+"\"}";
					break;
				default:
					break;
				}
		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("调用XCGLWebService.getVouchers获取凭证信息异常，异常信息："+e.getMessage());
			returnMsg = "{\"flag\":1,\"data\":\"\",\"msg\":\""+e.getMessage()+"\",\"synchronizedDate\":\""+maxDate+"\"}";
		}
		return returnMsg;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doNewVoucher</p> 
	 * <p>Description: 保存凭证 </p> 
	 * @param jsonObject
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doNewVoucher(java.lang.String, java.lang.String)
	 */
	@Override
	public String doSaveVoucher(String jsonObject, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();
			jsonStr = this.getVoucherHandlerService().doSaveVoucher(jsonObject) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("headId", "") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_SAVE_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doSaveAndSubmitVoucher</p> 
	 * <p>Description: 保存并提交单张凭证信息 </p> 
	 * @param jsonObject
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doNewAndSubmitVoucher(java.lang.String, java.lang.String)
	 */
	@Override
	public String doSaveAndSubmitVoucher(String jsonObject, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();
			jsonStr = this.getGLVoucherHandlerService().doSaveAndSubmitVoucher(jsonObject) ;
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_SAVE_SUBMIT_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doSubmitVouchers</p> 
	 * <p>Description: 提交凭证 </p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doSubmitVouchers(java.lang.String, java.lang.String)
	 */
	@Override
	public String doSubmitVouchers(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();
			jsonStr = this.getGLVoucherHandlerService().doSubmitGLVouchers(jsonArray) ;
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_SUBMIT_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doCancelSubmitVouchers</p> 
	 * <p>Description: 撤回提交 </p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doCancelSubmitVouchers(java.lang.String, java.lang.String)
	 */
	@Override
	public String doCancelSubmitVouchers(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();
			jsonStr = this.getGLVoucherHandlerService().doCancelSubmitGLVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_REVOKE_SUBMIT_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doDelVouchers</p> 
	 * <p>Description: 删除凭证信息 </p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doDelVouchers(java.lang.String, java.lang.String)
	 */
	@Override
	public String doDelVouchers(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();
			jsonStr = this.getVoucherHandlerService().doDelVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_DELETE_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doCheckVouchers</p> 
	 * <p>Description: 凭证审核 </p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doCheckVouchers(java.lang.String, java.lang.String)
	 */
	@Override
	public String doCheckVouchers(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();
			jsonStr = this.getVoucherHandlerService().doCheckVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_VERIFY_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doUncheckVouchers</p> 
	 * <p>Description: 取消凭证审核</p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doUncheckVouchers(java.lang.String, java.lang.String)
	 */
	@Override
	public String doUncheckVouchers(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();
			jsonStr = this.getVoucherHandlerService().doUncheckVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_CANCEL_VERIFY_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doRejectVouchers</p> 
	 * <p>Description: 驳回凭证 </p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doRejectVouchers(java.lang.String, java.lang.String)
	 */
	@Override
	public String doRejectVouchers(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();
			jsonStr = this.getVoucherHandlerService().doRejectVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_REJECT_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doSignVouchers</p> 
	 * <p>Description: 出纳签字 </p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doSignVouchers(java.lang.String, java.lang.String)
	 */
	@Override
	public String doSignVouchers(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang  = XCUtil.getLang();//语言值
			jsonStr = this.getVoucherHandlerService().doSignVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_SIGN_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doUnsignVouchers</p> 
	 * <p>Description: 取消签字 </p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doUnsignVouchers(java.lang.String, java.lang.String)
	 */
	@Override
	public String doUnsignVouchers(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();//语言值
			jsonStr = this.getVoucherHandlerService().doUnsignVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg",XipUtil.getMessage(lang, "XC_GL_CANCEL_SIGN_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doAccount</p> 
	 * <p>Description: 凭证过账 </p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doAccount(java.lang.String, java.lang.String)
	 */
	@Override
	public String doAccount(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();//语言值
			jsonStr = this.getVoucherHandlerService().doAccount(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_ACCOUNTING_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doViewVoucher</p> 
	 * <p>Description: 凭证查看 </p> 
	 * @param jsonArray
	 * @param userName
	 * @param userPwd
	 * @return 
	 * @see com.xzsoft.xc.gl.api.XCGLWebService#doViewVoucher(java.lang.String, java.lang.String)
	 */
	@Override
	public String doViewVoucher(String jsonArray, String userName, String userPwd) {
		String jsonStr = null ;
		String lang = "";
		try {
			lang = XCUtil.getLang();//语言值
			jsonStr = this.getVoucherHandlerService().doViewVoucher(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_SEEK_FAIL", null)+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return jsonStr ;
	}
}
