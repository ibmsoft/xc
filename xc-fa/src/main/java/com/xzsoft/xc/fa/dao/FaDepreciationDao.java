package com.xzsoft.xc.fa.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.fa.api.DeprMethodService;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.XcFaDepr;

public interface FaDepreciationDao {
	
	
	/**
	 * findFaForDepr:(获取需要计提折旧的资产列表（需要折旧的，未清理的，在折旧期间范围内的资产）)
	 *
	 * @param ledgerId 账簿ID
	 * @param periodCode 期间编码
	 * @return 返回资产列表
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public List<FaAddition> findFaForDepr(String ledgerId,String periodCode); 
	public void updateDeprForFa(FaAddition faAdditions);
	/**
	 * createFaHistory:(根据期间和资产ID创建资产卡片的备份数据)
	 *
	 * @param assetId 资产ID
	 * @param periodCode 期间
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public void createFaHistory(String assetId,String periodCode);
    /**
     * recoverFaFromHistory:(根据资产卡片的备份恢复资产卡片，只针对资产折旧所修改的几个字段)
     *
     * @param assetId 资产ID
     * @param periodCode 期间
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since   Ver 1.0
    */
    public void recoverFaFromHistory(String assetId,String periodCode);
    /**
     * delFaHistory:(删除资产卡片备份)
     *
     * @param assetId 资产ID
     * @param periodCode 期间
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public void delFaHistory(String assetId,String periodCode);
	/**
	 * saveFaDepr:(保存折旧信息)
	 *
	 * @param depr 折旧信息
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public void saveFaDepr(XcFaDepr depr);
	/**
     * getDeprMethods:(获取折旧方法)
     *
     * @return
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since   Ver 1.0
    */
    public HashMap<String,DeprMethodService> getDeprMethods() throws Exception;

}
