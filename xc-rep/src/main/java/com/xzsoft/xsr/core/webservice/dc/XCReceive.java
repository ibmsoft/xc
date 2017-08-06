package com.xzsoft.xsr.core.webservice.dc;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.cxf.endpoint.Client;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xzsoft.xsr.core.mapper.XCBAReceiveMapper;
import com.xzsoft.xsr.core.mapper.XCDimMapper;
import com.xzsoft.xsr.core.modal.DcDimSet;
import com.xzsoft.xsr.core.service.impl.XCDimServiceImpl;
import com.xzsoft.xsr.core.util.JsonUtil;
import com.xzsoft.xsr.core.webservice.WebServiceUtil;

@Controller
@RequestMapping("/webServiceController.do")
@Repository("xCReceive")
public class XCReceive {
	public Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private XCDimServiceImpl xzDimServiceImpl;
	@Autowired
	private XCDimMapper xcDimMapper;
	@Autowired
	private XCBAReceiveMapper xcBAReceiveMapper;
	@Autowired
	private WebServiceUtil webServiceUtil;

	// 页面请求加载的
	@RequestMapping(params = "method=getDim", method = RequestMethod.POST)
	public void getDim(@RequestParam(required = true) String dcId, HttpServletRequest request,
			HttpServletResponse response) {

		String json = "{flag:0, msg:'false'}";
		try {
			String str1 = XCDIMReceive(dcId, "08de20be-64c5-4793-876a-7f2dd944085f");
			System.out.println(str1);
		} catch (Exception e) {
			log.error("ws错误", e);
		}
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 调用XC WebService方法，接收维度信息
	 * 
	 * @param dcId
	 * @param suitId
	 * @return
	 * @throws Exception
	 */
	public String XCDIMReceive(String dcId, String suitId) throws Exception {
		StringBuilder returnStr = new StringBuilder();
		Map<String, String> dcEtlSrcValueMap = webServiceUtil.getDcEtlSrc(dcId, "WEB", "云ERP");
		if (null != dcEtlSrcValueMap) {
			String userName = dcEtlSrcValueMap.get("ETL_SRC_USER");
			String password = dcEtlSrcValueMap.get("ETL_SRC_PW");
			Client client = webServiceUtil.getWebServiceClient(dcEtlSrcValueMap.get("URL"));

			// 接收科目
			// 查询科目体系
			String kmType = "BA";
			List<String> kmHrcyList = xcDimMapper.getKMHrcy(suitId);
			if (kmHrcyList.size() > 0) {
				for (String hrcy : kmHrcyList) {
					String repKm = receiveKmDim(dcId, suitId, hrcy, kmType, userName, password, client);
					returnStr.append(repKm);
				}
			}

			// 接收其他维度
			// 查询账簿,只有公共级维度的entityId和ledgerCode为-1
			String entityId = "-1";
			String ledgerCode = "-1";
			// 查询自定义客户维度
			List<DcDimSet> dcDimsetList = xcDimMapper.getDimSetByDcid(dcId);
			// 循环具体一个数据中心对应的客户维度，调用XC维度webservice方法
			for (DcDimSet dim : dcDimsetList) {
				String xcTable = dim.getATTR1();
				String keyMod = dim.getDIM_MEANING();
				String dimTable = dim.getDIM_TABLE();

				switch (keyMod) {
				// 只有公共维度
				case "GY":
					String repGY = receiveOneCusDim(dcId, suitId, entityId, ledgerCode, xcTable, keyMod, dimTable,
							userName, password, client);
					returnStr.append(repGY);
					break;
				case "CP":
					String repCP = receiveOneCusDim(dcId, suitId, entityId, ledgerCode, xcTable, keyMod, dimTable,
							userName, password, client);
					returnStr.append(repCP);
					break;
				case "WL_GR":
					String repWL_GR = receiveOneCusDim(dcId, suitId, entityId, ledgerCode, xcTable, keyMod, dimTable,
							userName, password, client);
					returnStr.append(repWL_GR);
					break;
				case "WL_NB":
					String repWL_NB = receiveOneCusDim(dcId, suitId, entityId, ledgerCode, xcTable, keyMod, dimTable,
							userName, password, client);
					returnStr.append(repWL_NB);
					break;
				case "KH":
					String repKH = receiveOneCusDim(dcId, suitId, entityId, ledgerCode, xcTable, keyMod, dimTable,
							userName, password, client);
					returnStr.append(repKH);
					break;
				// 包含私有维度
				case "XM":
					String repXM = receiveAllEntityCusDim(dcId, suitId, xcTable, keyMod, dimTable, userName, password,
							client);
					returnStr.append(repXM);
					break;
				case "CB":
					String repCB = receiveAllEntityCusDim(dcId, suitId, xcTable, keyMod, dimTable, userName, password,
							client);
					returnStr.append(repCB);
					break;
				case "CUS1":
					String repCus1 = receiveAllEntityCusDim(dcId, suitId, xcTable, keyMod, dimTable, userName, password,
							client);
					returnStr.append(repCus1);
					break;
				case "CUS2":
					String repCus2 = receiveAllEntityCusDim(dcId, suitId, xcTable, keyMod, dimTable, userName, password,
							client);
					returnStr.append(repCus2);
					break;
				case "CUS3":
					String repCus3 = receiveAllEntityCusDim(dcId, suitId, xcTable, keyMod, dimTable, userName, password,
							client);
					returnStr.append(repCus3);
					break;
				case "CUS4":
					String repCus4 = receiveAllEntityCusDim(dcId, suitId, xcTable, keyMod, dimTable, userName, password,
							client);
					returnStr.append(repCus4);
					break;
				}
			}
			// 抽取现金流量项目
			String repCash = xccashItemReceive(dcId, suitId, userName, password, client);
			returnStr.append(repCash);
		} else {
			returnStr.append("WebService地址没有配置！请通过：数据中心管理-接口数据源功能进行配置！");
		}

		return returnStr.toString();
	}

	/**
	 * 接收科目-接收一个科目体系的科目
	 * 
	 * @param dcID
	 * @param entityId
	 * @param ledgerCode
	 * @return 接收一个科目体系的科目出现异常，该方法中进行捕获， 不再向上级方法抛异常，这样不影响下一个科目体系的科目接收
	 */
	public String receiveKmDim(String dcId, String suitId, String kmHrcyCode, String kmType, String userName,
			String password, Client client) {
		// 返回值
		StringBuilder returnStr = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] params = new String[4];
		String xsrDateValue = null;
		try {
			// 查询当前最新时间
			Timestamp dateValue = xcDimMapper.getDimDataValue(dcId, kmHrcyCode, "DATE", "KM");
			if (null != dateValue) {
				xsrDateValue = sdf.format(dateValue);
			} else {
				xsrDateValue = "1900-01-01 00:00:00";
			}
			// 调用webservice的参数
			params[0] = xsrDateValue;
			params[1] = kmHrcyCode;
			params[2] = userName;
			params[3] = password;
			// 调用webservice方法
			Object[] res = client.invoke("getAccounts", (Object[]) params);
			// 将webservice方法的返回值转化为标准json格式
			String tempJson = (String) res[0];
			String resJson = tempJson.replace("\"[", "[").replace("]\"", "]");
			// 将json串转换为map
			Map<String, Object> jsonObjMap = JsonUtil.getJsonObj(resJson);
			// flag存储同步是否成功，如果成功则flag为1，失败为0
			String flag = jsonObjMap.get("flag").toString();
			if ("0".equals(flag)) {
				// 同步时间
				String synchronizedDate = jsonObjMap.get("synchronizedDate").toString();
				String KMREStr = xzDimServiceImpl.receiveKMDim(jsonObjMap.get("data"), dcId, synchronizedDate,
						xsrDateValue, kmHrcyCode);
				returnStr.append(KMREStr);
			} else {
				// msg存储同步失败后的返回信息
				returnStr.append("科目体系:").append(kmHrcyCode).append(",调用科目WebService不成功,WebService返回信息:")
						.append(jsonObjMap.get("msg"));
			}
		} catch (Exception e) {
			log.error("科目体系:" + kmHrcyCode + ",接收科目出错:", e);
			returnStr.append("科目体系:").append(kmHrcyCode).append(",接收科目出错,错误信息:").append(e.getMessage());
			/**
			 * 接收一个科目体系的科目出现异常，在此处捕获后， 不再向上级方法抛异常，这样不影响下一个科目体系的科目接收
			 **/
		}
		return returnStr.toString();
	}

	/**
	 * 私有维度,接收一个维度时，循环所有有账簿的公司,接收所有公司的维度
	 * 
	 * @param dcID
	 * @param entityId
	 * @param ledgerCode
	 * @return
	 */
	public String receiveAllEntityCusDim(String dcId, String suitId, String xcTable, String keyMod, String dimTable,
			String userName, String password, Client client) {
		// 返回值
		StringBuilder returnStr = new StringBuilder();
		try {
			List<HashMap> list = xcBAReceiveMapper.getEntityBooks(suitId);
			for (HashMap entity : list) {
				String entityId = entity.get("ORG_ID").toString();
				String ledgerCode = entity.get("HS_BOOKS").toString();
				String repCus = receiveOneCusDim(dcId, suitId, entityId, ledgerCode, xcTable, keyMod, dimTable,
						userName, password, client);
				returnStr.append(repCus);
			}
		} catch (Exception e) {
			log.error("接收其他维度出错：", e);
			returnStr.append("接收其他维度出错：").append(e.getMessage());
		}
		return returnStr.toString();
	}

	/**
	 * 接收其他维度-接收一个账簿(公司)的某一个维度
	 * 
	 * @param dcID
	 * @param entityId
	 * @param ledgerCode
	 * @return 接收一个账簿(公司)的一个维度出现异常，在该方法中进行捕获，不再向上级方法抛异常，这样不影响其他维度的接收
	 */
	public String receiveOneCusDim(String dcId, String suitId, String entityId, String ledgerCode, String xcTable,
			String keyMod, String dimTable, String userName, String password, Client client) {
		// 返回值
		StringBuilder returnStr = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] params = new String[5];
		String xsrDateValue = null;
		try {
			// 获取当前维度的最新更新时间
			Timestamp dateValue = xcDimMapper.getDimDataValue(dcId, entityId, "DATE", keyMod);
			if (null != dateValue) {
				xsrDateValue = sdf.format(dateValue);
			} else {
				xsrDateValue = "1900-01-01 00:00:00";
			}
			params[0] = xcTable;
			params[1] = xsrDateValue;
			params[2] = ledgerCode;
			params[3] = userName;
			params[4] = password;
			// 调用webservice方法
			Object[] res = client.invoke("getDimensions", (Object[]) params);
			if (null != res) {
				// 将webservice方法的返回值转化为标准json格式
				String tempJson = (String) res[0];
				String resJson = tempJson.replace("\"[", "[").replace("]\"", "]");
				// 将json串转换为map
				Map<String, Object> jsonObjMap = JsonUtil.getJsonObj(resJson);
				// flag存储同步是否成功，如果成功则flag为1，失败为0
				String flag = jsonObjMap.get("flag").toString();
				if ("0".equals(flag)) {
					// 同步时间
					String synchronizedDate = (String) jsonObjMap.get("synchronizedDate");
					String cusREStr = xzDimServiceImpl.receiveCusDim(jsonObjMap.get("data"), dcId, entityId,
							synchronizedDate, xsrDateValue, dimTable, keyMod);
					returnStr.append(cusREStr);
				} else {
					// msg存储同步失败后的返回信息
					returnStr.append("公司:").append(entityId).append(",调用维度:").append(keyMod)
							.append("的WebService不成功,WebService返回信息:").append(jsonObjMap.get("msg"));
				}
			}
		} catch (Exception e) {
			log.error(entityId + "公司，接收" + keyMod + "出错：", e);
			returnStr.append(entityId).append("公司，接收").append(keyMod).append("出错：").append(e.getMessage());
			// 接收一个账簿(公司)的一个维度出现异常，在该方法中进行捕获，不再向上级方法抛异常，这样不影响其他维度的接收
		}

		return returnStr.toString();
	}

	/**
	 * 调用XC WebService方法，接收现金流量项目
	 * 
	 * @param dcId
	 * @param suitId
	 * @return
	 * @throws Exception
	 */
	public String xccashItemReceive(String dcId, String suitId, String userName, String password, Client client) {
		StringBuilder returnStr = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] params = new String[3];
		String xsrDateValue = null;
		try {
			// 查询当前最新时间
			Timestamp dateValue = xcDimMapper.getDimDataValue(dcId, "-1", "DATE", "CA_KM");
			if (null != dateValue) {
				xsrDateValue = sdf.format(dateValue);
			} else {
				xsrDateValue = "1900-01-01 00:00:00";
			}
			// 调用webservice的参数
			params[0] = xsrDateValue;
			params[1] = userName;
			params[2] = password;
			// 调用webservice方法
			Object[] res = client.invoke("getCashItem", (Object[]) params);
			// 将webservice方法的返回值转化为标准json格式
			String tempJson = (String) res[0];
			String resJson = tempJson.replace("\"[", "[").replace("]\"", "]");
			// 将json串转换为map
			Map<String, Object> jsonObjMap = JsonUtil.getJsonObj(resJson);
			// flag存储同步是否成功，如果成功则flag为1，失败为0
			String flag = jsonObjMap.get("flag").toString();
			if ("0".equals(flag)) {
				// 同步时间
				String synchronizedDate = jsonObjMap.get("synchronizedDate").toString();
				String caREStr = xzDimServiceImpl.receiveCaItem(jsonObjMap.get("data"), dcId, synchronizedDate,
						xsrDateValue);
				returnStr.append(caREStr);
			} else {
				// msg存储同步失败后的返回信息
				returnStr.append("调用现金流量项目WebService不成功,WebService返回信息:").append(jsonObjMap.get("msg"));
			}
		} catch (Exception e) {
			log.error("接收现金流量项目出错：", e);
			returnStr.append("接收现金流量项目出错：").append(e.getMessage());
		}

		return returnStr.toString();
	}

	/**
	 * 定时请求
	 * 
	 * @author liuwl
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	public String XCBAReceive(String dcId, String suitId, String mod, String lastUpdateDate) throws Exception {
		StringBuilder returnStr = new StringBuilder();
		String orgId = "";
		String ledgerCode = "";
		String[] params = new String[4];
		String etl_key_id="";
		HashMap keyValueMap=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String dbType=PlatformUtil.getDbType();
		Map<String, String> dcEtlSrcValueMap = webServiceUtil.getDcEtlSrc(dcId, "WEB", "云ERP");
		if (null != dcEtlSrcValueMap) {
			String userName = dcEtlSrcValueMap.get("ETL_SRC_USER");
			String password = dcEtlSrcValueMap.get("ETL_SRC_PW");
			params[2] = userName;
			params[3] = password;
			Client client = webServiceUtil.getWebServiceClient(dcEtlSrcValueMap.get("URL"));
			// 查询EBS账簿
			List<HashMap> list = xcBAReceiveMapper.getEntityBooks(suitId);
			for (int i = 0; i < list.size(); i++) {
				orgId = list.get(i).get("ORG_ID").toString();
				ledgerCode = list.get(i).get("HS_BOOKS").toString();
				params[1] = ledgerCode;
				if ("-1".equals(mod)) {
					//ba
					 keyValueMap = xcBAReceiveMapper.getDateValue(dcId, orgId, "GL_BA");
					if (keyValueMap!=null) {
						lastUpdateDate = sdf.format(keyValueMap.get("DATE_VALUE"));
						etl_key_id = keyValueMap.get("ETL_KEY_ID").toString();
					} else {
						lastUpdateDate = "1900-01-01 00:00:00";
						etl_key_id="";
					}
					params[0] = lastUpdateDate;
					Object[] objects = (client.invoke("getBalance", (Object[]) params));
					returnStr = manageData(objects, dcId, orgId,"GL_BA", lastUpdateDate, ledgerCode, suitId,
							etl_key_id);
					//ca
					keyValueMap = xcBAReceiveMapper.getDateValue(dcId, orgId, "GL_CASH");
					if (keyValueMap!=null) {
						lastUpdateDate = sdf.format(keyValueMap.get("DATE_VALUE"));
						etl_key_id = keyValueMap.get("ETL_KEY_ID").toString();
					} else {
						lastUpdateDate = "1900-01-01 00:00:00";
						etl_key_id="";
					}
					params[0] = lastUpdateDate;
					Object[] objects1 = (client.invoke("getCashSum", (Object[]) params));
					returnStr.append(manageData(objects1, dcId, orgId,"GL_CASH", lastUpdateDate, ledgerCode, suitId,
							etl_key_id));
				}else if ("GL_BA".equals(mod)) {
					//ba
					keyValueMap = xcBAReceiveMapper.getDateValue(dcId, orgId, "GL_BA");
					if (keyValueMap!=null) {
						lastUpdateDate = sdf.format(keyValueMap.get("DATE_VALUE"));
						etl_key_id = keyValueMap.get("ETL_KEY_ID").toString();
					} else {
						lastUpdateDate = "1900-01-01 00:00:00";
						etl_key_id="";
					}
					params[0] = lastUpdateDate;
					Object[] objects = (client.invoke("getBalance", (Object[]) params));
					returnStr = manageData(objects, dcId, orgId,"GL_BA", lastUpdateDate, ledgerCode, suitId,
							etl_key_id);
				}else if ("GL_CASH".equals(mod)) {
					keyValueMap = xcBAReceiveMapper.getDateValue(dcId, orgId, "GL_CASH");
					if (keyValueMap!=null) {
						lastUpdateDate = sdf.format(keyValueMap.get("DATE_VALUE"));
						etl_key_id = keyValueMap.get("ETL_KEY_ID").toString();
					} else {
						lastUpdateDate = "1900-01-01 00:00:00";
						etl_key_id="";
					}
					params[0] = lastUpdateDate;
					Object[] objects = (client.invoke("getCashSum", (Object[]) params));
					returnStr = manageData(objects, dcId, orgId,"GL_CASH", lastUpdateDate, ledgerCode, suitId,
							etl_key_id);
				}
			}
		} else {
			returnStr.append("WebService地址没有配置！请通过：数据中心管理-接口数据源功能进行配置！");
		}
		return returnStr.toString();

	}

	/**
	 * 数据抽取入口
	 * 
	 * @author liuwl
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	public String extractData(String dcId, String entityId, String suitId) throws Exception {
		StringBuilder returnStr = new StringBuilder();
		String ledgerCode = "";
		String etl_key_id = "";
		String[] params = new String[4];
		String lastUpdateDate = "";
		HashMap keyValueMap=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String dbType=PlatformUtil.getDbType();
		Map<String, String> dcEtlSrcValueMap = webServiceUtil.getDcEtlSrc(dcId, "WEB", "云ERP");
		if (null != dcEtlSrcValueMap) {
			String userName = dcEtlSrcValueMap.get("ETL_SRC_USER");
			String password = dcEtlSrcValueMap.get("ETL_SRC_PW");
			Client client = webServiceUtil.getWebServiceClient(dcEtlSrcValueMap.get("URL"));
			ledgerCode = xcBAReceiveMapper.getEntityBook(suitId, entityId);
			params[1] = ledgerCode;
			params[2] = userName;
			params[3] = password;
			//ba
		    keyValueMap = xcBAReceiveMapper.getDateValue(dcId, entityId, "GL_BA");
			if (keyValueMap!=null) {
				lastUpdateDate = sdf.format(keyValueMap.get("DATE_VALUE"));
				etl_key_id = keyValueMap.get("ETL_KEY_ID").toString();
			} else {
				lastUpdateDate = "1900-01-01 00:00:00";
				etl_key_id="";
			}
			params[0] = lastUpdateDate;
			Object[] objects = (client.invoke("getBalance", (Object[]) params));
			returnStr = manageData(objects, dcId, entityId,"GL_BA", lastUpdateDate, ledgerCode, suitId,
					etl_key_id);
			//ca
			keyValueMap = xcBAReceiveMapper.getDateValue(dcId, entityId, "GL_CASH");
			if (keyValueMap!=null) {
				lastUpdateDate = sdf.format(keyValueMap.get("DATE_VALUE"));
				etl_key_id = keyValueMap.get("ETL_KEY_ID").toString();
			} else {
				lastUpdateDate = "1900-01-01 00:00:00";
				etl_key_id="";
			}
			params[0] = lastUpdateDate;
			Object[] objects1 = (client.invoke("getCashSum", (Object[]) params));
			returnStr.append(manageData(objects1, dcId, entityId,"GL_CASH", lastUpdateDate, ledgerCode, suitId,
					etl_key_id));
			
		} else {
			returnStr.append("WebService地址没有配置！请通过：数据中心管理-接口数据源功能进行配置！");
		}

		return returnStr.toString();
	}

	/**
	 * 数据处理
	 * 
	 * @author liuwl
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public StringBuilder manageData(Object[] objects, String dcId, String orgId, String mod, String lastUpdateDate,
			String ledgerCode, String suitId, String etl_key_id) throws Exception {
		StringBuilder returnStr = new StringBuilder();
		String resJson = objects[0].toString().replace("\"[", "[").replace("]\"", "]");
		JSONObject jsonObject = JSONObject.fromObject(resJson);
		Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);
		String flag = mapJson.get("flag").toString();
		String msg = mapJson.get("msg").toString();
		String ts = mapJson.get("synchronizedDate").toString();
		Timestamp synchronizedDate = Timestamp.valueOf(ts);
		if (!flag.equals("0")) {
			returnStr.append("公司：" + orgId + "的" + mod + "模块数据抽取失败 ");
			return returnStr;
		} else {
			returnStr.append(msg);
		}
		xcBAReceiveMapper.delete_XCGL(dcId, orgId, mod, lastUpdateDate, ledgerCode);
		String dataJson = mapJson.get("data").toString();
		if (dataJson.equals("[]")) {
			returnStr.append("公司：" + orgId+mod+ "无总账数据 ");
		} else {
			JSONArray jsonArray = JSONArray.fromObject(dataJson);
			List<Map<String, String>> mapListJson = (List<Map<String, String>>) jsonArray;
			xcBAReceiveMapper.insert_XCGL(dcId, orgId, mod, suitId, mapListJson,synchronizedDate);
		}
		if (etl_key_id == "" || etl_key_id == null) {
			xcBAReceiveMapper.insertDateValue(dcId, orgId, mod, synchronizedDate);
		} else {
			xcBAReceiveMapper.updateDateValue(synchronizedDate, etl_key_id);
		}
		return returnStr;
	}
}
