package com.xzsoft.xsr.core.webservice;

import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xsr.core.mapper.XCDimMapper;

@Service
public class WebServiceUtil {
	
	@Autowired
	private XCDimMapper xcDimMapper; 
	/**
	 * 通过数据中心ID，数据源类型， 数据源名称返回数据源信息
	 * 返回值Map中的键: ETL_SRC_NAME, ETL_SRC_TYPE, DB_TYPE, URL, ETL_SRC_USER, ETL_SRC_PW
	 * @param dcId
	 * @param srcType
	 * @param srcName
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getDcEtlSrc(String dcId, String srcType, String srcName) throws Exception {
		Map<String,String> dcEtlSrcValueMap = xcDimMapper.getDcEtlSrc(dcId, srcType, srcName);
		return dcEtlSrcValueMap;
	}
	/**
	 * 传入参数webservice地址，返回client
	 * @param webServiceAddress
	 * @return
	 */
	public static Client getWebServiceClient(String webServiceAddress) {
		// 基于CXF调用WS测试代码
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		// WS发布地址
		Client client = (Client) dcf.createClient(webServiceAddress);
		return client;
	}

}
