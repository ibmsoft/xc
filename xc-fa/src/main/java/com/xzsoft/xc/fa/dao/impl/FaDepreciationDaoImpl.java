package com.xzsoft.xc.fa.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.fa.api.DeprMethodService;
import com.xzsoft.xc.fa.dao.FaDepreciationDao;
import com.xzsoft.xc.fa.mapper.FaDepreciationMapper;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaDeprMethod;
import com.xzsoft.xc.fa.modal.XcFaDepr;

@Repository("faDepreciationDao")
public class FaDepreciationDaoImpl implements FaDepreciationDao {
    @Resource
    private FaDepreciationMapper faDepreciationMapper;
    
    @Override
    public void saveFaDepr(XcFaDepr depr) {
        faDepreciationMapper.saveFaDepr(depr);
        
    }
    
    @Override
    public List<FaAddition> findFaForDepr(String ledgerId, String periodCode) {
        return faDepreciationMapper.findFaForDepr(ledgerId, periodCode);
    }
    
    @Override
    public void updateDeprForFa(FaAddition faAdditions) {
        faDepreciationMapper.updateDeprForFa(faAdditions);
    }
    
    @Override
    public HashMap<String, DeprMethodService> getDeprMethods() throws Exception {
        List<FaDeprMethod> deprMethodList = faDepreciationMapper.getDeprMethods();
        HashMap<String, DeprMethodService> deprMethodMap = new HashMap<String, DeprMethodService>();
        for (FaDeprMethod deprMethod : deprMethodList) {
            Class deprClass = Class.forName(deprMethod.getDeprClass());
            DeprMethodService deprInst = (DeprMethodService) deprClass.newInstance();
            deprMethodMap.put(deprMethod.getMethodCode(), deprInst);
        }
        return deprMethodMap;
    }
    
    @Override
    public void createFaHistory(String assetId, String periodCode) {
        faDepreciationMapper.createFaHistory(assetId, periodCode);
    }
    
    @Override
    public void delFaHistory(String assetId, String periodCode) {
        faDepreciationMapper.delFaHistory(assetId, periodCode);
    }

    @Override
    public void recoverFaFromHistory(String assetId, String periodCode) {
        faDepreciationMapper.recoverFaFromHistory(assetId, periodCode);
    }
    
}
