package com.xzsoft.xc.fa.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.fa.dao.FaAdjDao;
import com.xzsoft.xc.fa.mapper.FaAdjMapper;
import com.xzsoft.xc.fa.modal.FaAdjH;
import com.xzsoft.xc.fa.modal.FaAdjL;

@Repository("faAdjDao")
public class FaAdjDaoImpl implements FaAdjDao {

	private static final Logger log = Logger.getLogger(FaAdjDaoImpl.class);

	@Resource
	private FaAdjMapper faAdjMapper;

	@Override
	public void insertFaAdjH(FaAdjH faAdjH) {
		faAdjMapper.insertFaAdjH(faAdjH);
	}

	@Override
	public void insertFaAdjL(List<FaAdjL> faAdjLs) {
		faAdjMapper.insertFaAdjL(faAdjLs);
	}

	@Override
	public void updateFaAddition(List<FaAdjL> faAdjLs) {
		if(faAdjLs!=null && faAdjLs.size()>0){
			for(FaAdjL faAdjL : faAdjLs){
				faAdjMapper.updateFaAddition(faAdjL);
			}
		}
		
	}
	
	@Override
	public String getCurrentOpenedPeriod(String ledgerId) {
		return faAdjMapper.getCurrentOpenedPeriod(ledgerId);
	}

	@Override
	public List<String> getAllPeriodsByLedger(String ledgerId) {
		return faAdjMapper.getAllPeriodsByLedger(ledgerId);
	}

	@Override
	public FaAdjH getAdjHById(String adjHid) {
		return faAdjMapper.getAdjHById(adjHid);
	}

	@Override
	public List<FaAdjL> getAdjLByHid(String adjHid) {
		return faAdjMapper.getAdjLByHid(adjHid);
	}

	@Override
	public void updateAdjHStatus(String hId, String status) {
		faAdjMapper.updateAdjHStatus(hId, status);
	}

	@Override
	public void revokeFaAddition(List<FaAdjL> faAdjLs) {
		if(faAdjLs!=null && faAdjLs.size()>0){
			for(FaAdjL faAdjL : faAdjLs){
				faAdjMapper.revokeFaAddition(faAdjL);
			}
		}
	}

	@Override
	public String checkFaHasBeenAdj(String adjDate, String adjType,
			String assetId, String status) {
		return faAdjMapper.checkFaHasBeenAdj(adjDate, adjType, assetId, status);
	}

	@Override
	public void delAutoCreateAdjLs(String adjHid) {
		faAdjMapper.delAutoCreateAdjLs(adjHid);
	}

	@Override
	public FaAdjL getAdjLByLid(String adjLid) {
		return faAdjMapper.getAdjLByLid(adjLid);
	}

	@Override
	public int checkInPeriod(String adjHid) {
		return faAdjMapper.checkInPeriod(adjHid);
	}

	@Override
	public void updateFaAdjH(FaAdjH faAdjH) {
		faAdjMapper.updateFaAdjH(faAdjH);
	}

	@Override
	public void updateFaAdjL(List<FaAdjL> faAdjLs) {
		for(FaAdjL faAdjL : faAdjLs){
			faAdjMapper.updateFaAdjL(faAdjL);
		}
	}

	@Override
	public int checkAdj4After(String adjDate, String assetId) {
		return faAdjMapper.checkAdj4After(adjDate, assetId);
	}

	@Override
	public String checkFaHasBeenAdjUpdate(String adjDate, String adjType,
			String assetId, String adjDid) {
		return faAdjMapper.checkFaHasBeenAdjUpdate(adjDate, adjType, assetId, adjDid);
	}
}