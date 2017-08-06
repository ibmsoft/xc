package com.xzsoft.xc.gl.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xzsoft.xc.gl.modal.GlJzTpl;
import com.xzsoft.xc.gl.modal.GlJzTplDtl;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName:XCGLBalCarryoverUtil
 * @note:总账月末结转工具类
 * @author tangxl
 */
public class XCGLBalCarryoverUtil{
	@SuppressWarnings("serial")
	private final static HashMap<String, Integer> SEG_ORDER_MAP = new HashMap<String, Integer>(){
		{
			put("XC_AP_VENDORS", 1);
			put("XC_AR_CUSTOMERS",2);
			put("XIP_PUB_EMPS", 3);
			put("XIP_PUB_ORGS", 4);
			put("XC_GL_PRODUCTS",5);
			put("XIP_PUB_DEPTS", 6);
			put("XC_PM_PROJECTS", 7);
			put("XC_GL_CUSTOM1", 8);
			put("XC_GL_CUSTOM2", 9);
			put("XC_GL_CUSTOM3", 10);
			put("XC_GL_CUSTOM4", 11);
		}
	};
	private final static BigDecimal fLAG_DECIMAL = new BigDecimal(-1);
	/**
	 * 
	 * @methodName  carrayoverBalIntoVoucher
	 * @author      tangxl
	 * @date        2016年7月19日
	 * @describe    根据结转模板将余额记录转换成凭转存到凭证表中
	 * <document>
	 *  <title>结转数据转存凭证模板说明</title>
	 *  <p>
	 *    1、被结转科目的辅助项必须包含转入科目的辅助项；
	 *    2、对被结转科目按照【其辅助项的值】，在转入科目辅助项的维度下进行分组，依次生成科目为【转入科目】，辅助段为【转出科目】的凭证行信息，金额的方向根据【转入科目】的方向，计算转出科目的值；
	 *    3、如果【转入科目】没有启用【辅助段】信息，那么生成的【转入科目】凭证明细行只有一条， 金额方向跟步骤2保持一致；
	 *    4、简例说明
	 *    转入科目为 A，辅助段启用了 【供应商】和【客户】
	 *    转出科目为B、C、D，B启用了【供应商】和【客户】，c启用了【供应商】、【客户】、【个人往来】，D启用了【供应商】、【客户】、【组织】
	 *    转出科目B的辅助段值为 b1和c1，C的为 b1，c1和p1，D的为b1，c2，o1
	 *    生成A的凭证分录明细行记录如下：
	 *    A b1 c1 金额为 B、C 与 A方向【同向加】【反向减】的值
	 *    A b1 c2 金额为 D 与 A方向【同向加】【反向减】的值
	 *  </p>
	 * </document>
	 * @param       glJzTpl
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static String carrayoverBalIntoVoucher(GlJzTpl glJzTpl,String periodCode,String voucherDate,VoucherHandlerService voucherHandlerService) throws Exception{
		HashMap<String, BigDecimal> groupSegMap = new HashMap<String, BigDecimal>();
		List<GlJzTplDtl> dtlList = glJzTpl.getJzDtlList();
		//借方金额
		BigDecimal drDecimal = new BigDecimal(0);
		//贷方金额
		BigDecimal crDecimal = new BigDecimal(0);
		//凭证分录明细信息
		JSONArray vLineArray = new JSONArray();
		//凭证主表信息
		JSONObject mainObject = new JSONObject();
		mainObject.put("ledgerCode", glJzTpl.getLedgerCode());
		mainObject.put("periodCode", periodCode);
		mainObject.put("categoryCode", glJzTpl.getCategoryCode());
		mainObject.put("srcCode", "");
		mainObject.put("srcId", "");
		mainObject.put("serialNum", "");
		mainObject.put("attchTotal", "0");
		mainObject.put("summary", glJzTpl.getSummary());
		mainObject.put("templateType", "1");
		mainObject.put("vStatus", "1");
		mainObject.put("createdBy", CurrentSessionVar.getUserId());
		mainObject.put("creationDate", voucherDate);
		for(GlJzTplDtl t:dtlList){
			//一条明细包含多个凭证分录行明细
			if(t.getCcidCombineList() !=null && t.getCcidCombineList().size()>0){
				for(HashMap<String, Object> m:t.getCcidCombineList()){
					calculateVline(null,t, vLineArray, m,glJzTpl.getLedgerId());
					if("1".equals(t.getBalDirection())){
						drDecimal = drDecimal.add((BigDecimal)m.get("CONVERT_AMOUNT"));
					}else{
						crDecimal = crDecimal.add((BigDecimal)m.get("CONVERT_AMOUNT"));
					}
				}
			}else{
			//一条明细只有一条凭证分录
				calculateVline(voucherHandlerService,t, vLineArray, null,glJzTpl.getLedgerId());
				if("1".equals(t.getBalDirection())){
					drDecimal = drDecimal.add(t.getConvertAmount());
				}else{
					crDecimal = crDecimal.add(t.getConvertAmount());
				}
			}
			
		}
		List<Integer> segOrderList = new ArrayList<Integer>();
		if (glJzTpl.getAccSegmentCode() == null || glJzTpl.getAccSegmentCode().size() == 0) {
			//1-【转入科目】不存在辅助段信息
			JSONObject additionalObject = createVlineWithoutSetment(glJzTpl,voucherHandlerService,drDecimal,crDecimal);
			if("1".equals(glJzTpl.getZrAccDirection())){
				vLineArray.add(0, additionalObject);
			}else{
				vLineArray.add(additionalObject);
			}
			for(int k=0;k<vLineArray.size();k++){
				JSONObject o = vLineArray.getJSONObject(k);
				o.put("orderBy", k+1);
			}
			mainObject.put("lines", vLineArray.toString());
		}else{
			//2-【转入科目】的辅助段信息存在
			for(String p:glJzTpl.getAccSegmentCode()){
				segOrderList.add(SEG_ORDER_MAP.get(p));
			}
			//3-根据【转入科目】的辅助段排序，取出【转出科目】的辅助段组合信息
			for(int k=0;k<vLineArray.size();k++){
				JSONObject t = vLineArray.getJSONObject(k);
				String[] setCodes = t.getString("ccCode").split("/");
				String[] zrCcIdCode = {glJzTpl.getZrAccCode(),"00","00","00","00","00","00","00","00","00","00","00"};
				//--组装转入科目明细行的CCID_CODE
				for(Integer q:segOrderList){
					zrCcIdCode[q] = setCodes[q];
				}
				StringBuffer keyBuffer = new StringBuffer();
				//--将数组转化成Map的key
				for(int i=0;i<zrCcIdCode.length;i++){
					if(i != zrCcIdCode.length-1){
						keyBuffer.append(zrCcIdCode[i]).append("/");
					}else{
						keyBuffer.append(zrCcIdCode[i]);
					}
				}
				String tmpSegCode = keyBuffer.toString();
				if(!groupSegMap.containsKey(tmpSegCode)){
					if(t.getString("direction").equals(glJzTpl.getZrAccDirection())){
						groupSegMap.put(tmpSegCode, new BigDecimal(t.get("sumAmount").toString()));
					}else{
						BigDecimal subtartctDecimal = new BigDecimal(0).subtract(new BigDecimal(t.get("sumAmount").toString()));
						groupSegMap.put(tmpSegCode, subtartctDecimal);
					}
				}else{
					if(t.getString("direction").equals(glJzTpl.getZrAccDirection())){
						BigDecimal sumDecimal = groupSegMap.get(tmpSegCode).add(new BigDecimal(t.get("sumAmount").toString()));
						groupSegMap.put(tmpSegCode, sumDecimal);
					}else{
						BigDecimal subtartctDecimal = groupSegMap.get(tmpSegCode).subtract(new BigDecimal(t.get("sumAmount").toString()));
						groupSegMap.put(tmpSegCode, subtartctDecimal);
					}
				}
			}
			//4-- 转出科目明细信息汇总完毕，生成转入科目的明细信息
			for(Entry<String, BigDecimal> entry:groupSegMap.entrySet()){
				JSONObject zrObject = new JSONObject();
				zrObject.put("summary",glJzTpl.getSummary());
				String zrSegmentCode = entry.getKey();
				BigDecimal zrDecimal = entry.getValue();
				zrDecimal = zrDecimal.multiply(fLAG_DECIMAL);
				String[] zrSegmentArray = zrSegmentCode.split("/");
				HashMap<String, String> segments = new HashMap<String, String>();
				segments.put(XConstants.XC_GL_ACCOUNTS, glJzTpl.getZrAccCode());
				segments.put(XConstants.XC_AP_VENDORS, zrSegmentArray[1]);
				segments.put(XConstants.XC_AR_CUSTOMERS, zrSegmentArray[2]);
				segments.put(XConstants.XC_GL_PRODUCTS, zrSegmentArray[5]);
				segments.put(XConstants.XIP_PUB_ORGS, zrSegmentArray[4]);
				segments.put(XConstants.XIP_PUB_EMPS, zrSegmentArray[3]);
				segments.put(XConstants.XIP_PUB_DETPS, zrSegmentArray[6]);
				segments.put(XConstants.XC_PM_PROJECTS, zrSegmentArray[7]);
				segments.put(XConstants.XC_GL_CUSTOM1, zrSegmentArray[8]);
				segments.put(XConstants.XC_GL_CUSTOM2, zrSegmentArray[9]);
				segments.put(XConstants.XC_GL_CUSTOM3, zrSegmentArray[10]);
				segments.put(XConstants.XC_GL_CUSTOM4, zrSegmentArray[11]);
				String ccid = voucherHandlerService.handlerCCID(glJzTpl.getLedgerId(), glJzTpl.getZrAccId(), segments).getString("ccid");
				if ("".equals(ccid)) {
					throw new Exception("转入科目生成CCID失败！");
				}
				zrObject.put("ccid", ccid);
				zrObject.put("accCode", glJzTpl.getZrAccCode());
				zrObject.put("vendorCode", "00");
				zrObject.put("customerCode", "00");
				zrObject.put("prodCode", "00");
				zrObject.put("orgCode", "00");
				zrObject.put("empCode", "00");
				zrObject.put("deptCode", "00");
				zrObject.put("prjCode", "00");
				zrObject.put("cust1Code", "00");
				zrObject.put("cust2Code", "00");
				zrObject.put("cust3Code", "00");
				zrObject.put("cust4Code", "00");
				zrObject.put("caCode", "");
				zrObject.put("srcDtlId", "");
				zrObject.put("orderBy", "");
				zrObject.put("amount", "0");
				zrObject.put("exchangeRate", "1");
				zrObject.put("enterDR", "0");
				zrObject.put("enterCR", "0");
				zrObject.put("orderBy", 1);
				if ("1".equals(glJzTpl.getZrAccDirection())) {
					zrObject.put("accountDR", zrDecimal.toPlainString());
					zrObject.put("accountCR", "0");
				} else {
					zrObject.put("accountCR", zrDecimal.toPlainString());
					zrObject.put("accountDR", "0");
				}
				if("1".equals(glJzTpl.getZrAccDirection())){
					vLineArray.add(0, zrObject);
				}else{
					vLineArray.add(zrObject);
				}
			}
			for(int k=0;k<vLineArray.size();k++){
				JSONObject o = vLineArray.getJSONObject(k);
				o.put("orderBy", k+1);
			}
			mainObject.put("lines", vLineArray.toString());
		}
		return mainObject.toString();
	}
	/**
	 * 
	 * @methodName  createVlineWithoutSetment
	 * @author      tangxl
	 * @date        2016年7月20日
	 * @describe    转入科目不含辅助明细时生成额外凭证明细操作
	 * @param       glJzTpl
	 * @param       voucherHandlerService
	 * @param       drDecimal
	 * @param       crDecimal
	 * @return
	 * @变动说明       
	 * @version     1.0
	 * @throws Exception 
	 */
	private static JSONObject createVlineWithoutSetment(GlJzTpl glJzTpl,VoucherHandlerService voucherHandlerService,BigDecimal drDecimal,BigDecimal crDecimal) throws Exception{
		// 1-- 转入科目不存在辅助段
		JSONObject additionalObject = new JSONObject();
		additionalObject.put("summary", glJzTpl.getSummary());
		HashMap<String, String> segments = new HashMap<String, String>();
		segments.put(XConstants.XC_GL_ACCOUNTS, glJzTpl.getZrAccCode());
		String ccid = voucherHandlerService.handlerCCID(glJzTpl.getLedgerId(), glJzTpl.getZrAccId(), segments).getString("ccid");
		if ("".equals(ccid)) {
			throw new Exception("转入科目生成CCID失败！");
		}
		additionalObject.put("ccid", ccid);
		additionalObject.put("accCode", glJzTpl.getZrAccCode());
		additionalObject.put("vendorCode", "00");
		additionalObject.put("customerCode", "00");
		additionalObject.put("prodCode", "00");
		additionalObject.put("orgCode", "00");
		additionalObject.put("empCode", "00");
		additionalObject.put("deptCode", "00");
		additionalObject.put("prjCode", "00");
		additionalObject.put("cust1Code", "00");
		additionalObject.put("cust2Code", "00");
		additionalObject.put("cust3Code", "00");
		additionalObject.put("cust4Code", "00");
		additionalObject.put("caCode", "");
		additionalObject.put("srcDtlId", "");
		additionalObject.put("orderBy", "");
		additionalObject.put("amount", "0");
		additionalObject.put("exchangeRate", "1");
		additionalObject.put("enterDR", "0");
		additionalObject.put("enterCR", "0");
		additionalObject.put("orderBy", 1);
		if ("1".equals(glJzTpl.getZrAccDirection())) {
			BigDecimal accountDR = drDecimal.subtract(crDecimal);
			accountDR = accountDR.multiply(fLAG_DECIMAL);
			additionalObject.put("accountDR", accountDR.toPlainString());
			additionalObject.put("accountCR", "0");
		} else {
			BigDecimal accountCR = crDecimal.subtract(drDecimal);
			accountCR = accountCR.multiply(fLAG_DECIMAL);
			additionalObject.put("accountCR", accountCR.toPlainString());
			additionalObject.put("accountDR", "0");
		}
		return additionalObject;
	}
    /**
     * 	
     * @methodName  方法名
     * @author      tangxl
     * @date        2016年8月10日
     * @describe    
     * <p>
     *    转入科目没有启用辅助段，一条明细行对应一条凭证分录明细行，生成科目组合的11段值均为00
     *    转入科目启用了辅助段，明细行的取数科目只能为自己本身，一条明细行可能对应多个凭证分录明细行，科目id从余额表中取出
     * </p>
     * @param t
     * @param vLineArray
     * @throws Exception
     * @变动说明       
     * @version     1.0
     */
	public static void calculateVline(VoucherHandlerService voucherHandlerService,GlJzTplDtl t,JSONArray vLineArray,HashMap<String, Object> map,String ledgerId) throws Exception{
		BigDecimal convertVal = t.getConvertAmount();
		if(map != null){
			convertVal = (BigDecimal)map.get("CONVERT_AMOUNT");
		}
		JSONObject subObject = new JSONObject();
		if(map != null){
			subObject.put("ccid", map.get("CCID"));
			subObject.put("ccCode", map.get("CCID_CODE"));
		}else{
			//生成不含辅助段的科目组合id---ccid
			HashMap<String, String> segments = new HashMap<String, String>();
			segments.put(XConstants.XC_GL_ACCOUNTS, t.getAccCode());
			String ccid = voucherHandlerService.handlerCCID(ledgerId, t.getZcAccId(), segments).getString("ccid");
			if ("".equals(ccid)) {
				//throw new Exception("转入科目生成CCID失败！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_ZRKMSCCSB", null));
			}
			subObject.put("ccid", ccid);
		}
		subObject.put("summary", t.getSummary());
		subObject.put("direction", t.getBalDirection());
		subObject.put("accCode", t.getAccCode());
		subObject.put("vendorCode", "00");
		subObject.put("customerCode", "00");
		subObject.put("prodCode", "00");
		subObject.put("orgCode", "00");
		subObject.put("empCode", "00");
		subObject.put("deptCode", "00");
		subObject.put("prjCode", "00");
		subObject.put("cust1Code", "00");
		subObject.put("cust2Code", "00");
		subObject.put("cust3Code", "00");
		subObject.put("cust4Code", "00");
		subObject.put("caCode", "");
		subObject.put("srcDtlId", "");
		subObject.put("orderBy", "");
		subObject.put("amount", "0");
		subObject.put("exchangeRate", "1");
		subObject.put("enterDR", "0");
		subObject.put("enterCR", "0");
		subObject.put("orderBy", 1);
		subObject.put("sumAmount", convertVal.toPlainString());
		if("1".equals(t.getBalDirection())){
			subObject.put("accountDR", convertVal.toPlainString());
			subObject.put("accountCR", "0");
		}else{
			subObject.put("accountDR", "0");
			subObject.put("accountCR", convertVal.toPlainString());
		}
		vLineArray.add(subObject);
	
	}
}
