package com.xzsoft.xc.fa.mapper;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.XcFaDepr;
import com.xzsoft.xc.fa.modal.XcFaTrans;

public interface FaAdditionMapper {
    
    /**
     * 
     * insertFaAddition:(新增)
     *
     * @param faAddition
     * @author q p
     */
    public void insertFaAddition(FaAddition faAddition);
    
    /**
     * insertFaTrans:(新增资产事务)
     *
     * @param xcFaTransans
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public void insertFaTrans(XcFaTrans xcFaTransans);
    
    /**
	 * 
	 * updateFaTrans:(更新事务)
	 *
	 * @param xcFaTran
	 * @author q p
	 */
	public void updateFaTrans(XcFaTrans xcFaTran);
    
    /**
     * insertFaDepr:(新增资产计提折旧)
     *
     * @param xcFaDepr
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public void insertFaDepr(XcFaDepr xcFaDepr);
    
    /**
     * getDepr:(根据资产ID和期间获取资产的折旧信息)
     *
     * @param assetId 资产ID
     * @param periodCode 期间
     * @return 资产折旧Bean
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public XcFaDepr getDepr(@Param("assetId") String assetId, @Param("periodCode") String periodCode);
    
    /**
     * 
     * updateFaAddition:(修改)
     *
     * @param faAddition
     * @author q p
     */
    public void updateFaAddition(FaAddition faAddition);
    
    /**
     * deleteFaAddition:(删除资产)
     *
     * @param assetId 资产ID
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public void deleteFaAddition(String assetId);
    
    /**
     * delFaDepr:(删除资产指定期间的计提折旧)
     *
     * @param assetId 资产ID
     * @param periodCode 期间编码
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public void delFaDepr(@Param("assetId") String assetId, @Param("periodCode") String periodCode);
    
    /**
     * delFaDepr:(删除资产的所有计提折旧)
     *
     * @param assetId 资产ID
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public void delFaDeprWithId(@Param("assetId") String assetId);
    
    /**
     * getFaAdditionWithId:(根据资产ID获取资产)
     *
     * @param assetId 资产ID
     * @return 返回资产Bean
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public FaAddition getFaAdditionWithId(String assetId);
    
    /**
     * getKmIdByCode:(查询科目id)
     *
     * @param accCode 科目编码
     * @param ledgerId 账簿id
     * @return
     * @author q p
     */
    public String getKmIdByCode(String accCode, String ledgerId);
    
    /**
     * getFaParams:(获取账簿的资产参数设置值)
     *
     * @param ledgerId 账簿ID
     * @return 返回资产参数值Json串
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public String getFaParams(String ledgerId);
    
    /**
     * deleteFaTrans:(删除事务)
     *
     * @param srcId 来源头id
     * @author q p
     */
    public void deleteFaTrans(String srcId);
    
    /**
	 * 
	 * deleteFaTrans:(删除事务)
	 *
	 * @param assetId 事务id
	 * @param transCode 事务类型
	 * @author q p
	 */
	public void deleteFaTransByAssetTranCode(@Param("assetId")String assetId, @Param("transCode")String transCode);
	
	/**
	 * 
	 * getZjfsKmByAsset:(查询录入方式科目)
	 *
	 * @param assetId
	 * @return
	 * @author q p
	 */
	public String getZjfsKmByAsset(String assetId);
	
	/**
	 * 
	 * deleteFaTransByAssetId:(删除事务)
	 *
	 * @param assetId
	 * @author q p
	 */
	public void deleteFaTransByAssetId(String assetId);
	
	/**
	 * 
	 * deleteFaHistoryByAssetId:(删除历史表)
	 *
	 * @param assetId
	 * @author q p
	 */
	public void deleteFaHistoryByAssetId(String assetId);
	
	/**
	 * 
	 * deleteGlInterfaceByAsset:(删除会计平台记录)
	 *
	 * @param assetId
	 * @author q p
	 */
	public void deleteGlInterfaceByAsset(String assetId);
	
	/**
	 * 
	 * checkFaAdj:(校验资产是否做过调整)
	 *
	 * @param assetId
	 * @return
	 * @author q p
	 */
	public int checkFaAdj(String assetId);
}
