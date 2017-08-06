package com.xzsoft.xc.gl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.gl.modal.VInterface;

public interface VInterfaceMapper {
	/**
	 * insertVInterface:(新增会计平台记录)
	 *
	 * @param vInterface
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public void insertVInterface(VInterface vInterface);
	
	/**
	 * getVInterfaceBySrcHid:(根据来源ID查询会计平台记录)
	 *
	 * @param srcHid
	 * @return
	 * @author q p
	 */
	public List<VInterface> getVInterfaceBySrcHid(String srcHid); 
	
	/**
	 * deleteVInterfaceBySrcHid:(根据来源ID删除会计平台记录)
	 *
	 * @param srcHid
	 * @author q p
	 */
	public void deleteVInterfaceBySrcHid(String srcHid); 
	
	/**
	 * deleteVInterfaceBySrc:(根据来源和事务类型删除会计平台记录)
	 *
	 * @param srcHId 来源头ID
	 * @param srcLId 来源行ID
	 * @param transCode 事务编码
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public void deleteVInterfaceBySrc(@Param("srcHId")String srcHId,@Param("srcLId")String srcLId,@Param("transCode")String transCode);
	
	/**
	 * getVHeaderId4Interface:(根据来源和事务类型获取凭证头ID)
	 *
     * @param srcHId 来源头ID
     * @param srcLId 来源行ID
     * @param transCode 事务编码
	 * @return 返回凭证头ID
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public String getVHeaderId4Interface(@Param("srcHId")String srcHId,@Param("srcLId")String srcLId,@Param("transCode")String transCode);
}
