package com.xzsoft.xc.fa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaDeprMethod;
import com.xzsoft.xc.fa.modal.XcFaDepr;

public interface FaDepreciationMapper {
    
    /**
     * findFaForDepr:(获取需要计提折旧的资产列表（需要折旧的，未清理的，在折旧期间范围内的资产）)
     *
     * @param ledgerId 账簿ID
     * @param periodCode 期间编码
     * @return 返回资产列表
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public List<FaAddition> findFaForDepr(@Param("ledgerId") String ledgerId, @Param("periodCode") String periodCode);
    
    /**
     * updateDeprForFa:(更新资产卡片计提折旧相关信息)
     *
     * @param faAdditions 资产卡片Bean
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since   Ver 1.0
    */
    public void updateDeprForFa(FaAddition faAdditions);
    
    public void saveFaDepr(XcFaDepr depr);
    
    /**
     * createFaHistory:(根据期间和资产ID创建资产卡片的备份数据)
     *
     * @param assetId 资产ID
     * @param periodCode 期间
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public void createFaHistory(@Param("assetId") String assetId, @Param("periodCode") String periodCode);
    
    /**
     * recoverFaFromHistory:(根据资产卡片的备份恢复资产卡片，只针对资产折旧所修改的几个字段)
     *
     * @param assetId 资产ID
     * @param periodCode 期间
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since   Ver 1.0
    */
    public void recoverFaFromHistory(@Param("assetId") String assetId,@Param("periodCode")String periodCode);
    /**
     * delFaHistory:(删除资产卡片备份)
     *
     * @param assetId 资产ID
     * @param periodCode 期间
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public void delFaHistory(@Param("assetId") String assetId, @Param("periodCode") String periodCode);
    
    /**
     * getDeprMethods:(获取折旧方法)
     *
     * @return
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public List<FaDeprMethod> getDeprMethods();
    
}
