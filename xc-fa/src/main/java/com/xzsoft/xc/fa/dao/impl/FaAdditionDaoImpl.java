package com.xzsoft.xc.fa.dao.impl;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.fa.dao.FaAdditionDao;
import com.xzsoft.xc.fa.mapper.FaAdditionMapper;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.XcFaDepr;
import com.xzsoft.xc.fa.modal.XcFaTrans;

@Repository("faAdditionDao")
public class FaAdditionDaoImpl implements FaAdditionDao {

	private static final Logger log = Logger.getLogger(FaAdditionDaoImpl.class);

	@Resource
	private FaAdditionMapper faAdditionMapper;

	@Override
	public void insertFaAddition(FaAddition faAddition) {
		faAdditionMapper.insertFaAddition(faAddition);
	}

	@Override
	public void updateFaAddition(FaAddition faAddition) {
		faAdditionMapper.updateFaAddition(faAddition);
	}

	@Override
	public void insertFaTrans(XcFaTrans xcFaTransans) {
		faAdditionMapper.insertFaTrans(xcFaTransans);
	}

	@Override
	public void insertFaDepr(XcFaDepr xcFaDepr) {
		faAdditionMapper.insertFaDepr(xcFaDepr);
	}

	@Override
	public void deleteFaAddition(String assetId) {
		faAdditionMapper.deleteFaAddition(assetId);
	}

	@Override
	public void delFaDepr(String assetId, String periodCode) {
		faAdditionMapper.delFaDepr(assetId, periodCode);
	}

	@Override
	public void delFaDeprWithId(String assetId) {
		faAdditionMapper.delFaDeprWithId(assetId);
	}

	@Override
	public FaAddition getFaAdditionWithId(String assetId) {
		return faAdditionMapper.getFaAdditionWithId(assetId);
	}

	@Override
	public String getKmIdByCode(String accCode, String ledgerId) {
		return faAdditionMapper.getKmIdByCode(accCode, ledgerId);
	}

	@Override
	public JSONObject getFaParams(String ledgerId) {
		JSONObject json = new JSONObject();
		String result = faAdditionMapper.getFaParams(ledgerId);
		if(result!=null){
			json = JSONObject.fromObject(result);
		}
		return json;
	}

	@Override
	public void deleteFaTrans(String srcId) {
		faAdditionMapper.deleteFaTrans(srcId);
	}

    @Override
    public XcFaDepr getDepr(String assetId, String periodCode) {
        return faAdditionMapper.getDepr(assetId, periodCode);
    }

	@Override
	public void deleteFaTrans(String assetId, String transCode) {
		faAdditionMapper.deleteFaTransByAssetTranCode(assetId, transCode);
	}

	@Override
	public String getZjfsKmByAsset(String assetId) {
		return faAdditionMapper.getZjfsKmByAsset(assetId);
	}

	@Override
	public void deleteFaTransByAssetId(String assetId) {
		faAdditionMapper.deleteFaTransByAssetId(assetId);
	}

	@Override
	public void deleteFaHistoryByAssetId(String assetId) {
		faAdditionMapper.deleteFaHistoryByAssetId(assetId);
	}

	@Override
	public void deleteGlInterfaceByAsset(String assetId) {
		faAdditionMapper.deleteGlInterfaceByAsset(assetId);
	}

	@Override
	public int checkFaAdj(String assetId) {
		return faAdditionMapper.checkFaAdj(assetId);
	}

	@Override
	public void updateFaTrans(XcFaTrans xcFaTran) {
		faAdditionMapper.updateFaTrans(xcFaTran);
		
	}
}