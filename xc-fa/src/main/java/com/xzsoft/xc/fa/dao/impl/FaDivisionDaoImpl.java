package com.xzsoft.xc.fa.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.fa.dao.FaDivisionDao;
import com.xzsoft.xc.fa.mapper.FaAdjMapper;
import com.xzsoft.xc.fa.mapper.FaDivisionMapper;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaDivision;
import com.xzsoft.xc.fa.modal.FaDivisionH;
import com.xzsoft.xc.fa.modal.FaDivisionL;
@Repository("faDivisionDao")
public class FaDivisionDaoImpl  implements FaDivisionDao{
	private static final Logger log = Logger.getLogger(FaDivisionDaoImpl.class);

	@Resource
	private FaDivisionMapper faDivisionMapper;
	@Override
	public void insertfaDivision(FaDivisionH faDivisionH) {
		faDivisionMapper.insertfaDivision(faDivisionH);
	}

	@Override
	public void insertFaDivisionL(List<FaDivisionL> faDivisionL) {
		faDivisionMapper.insertFaDivisionL(faDivisionL);
	}
	@Override
	public void updateFaAddition(FaAddition faAddition) {
		faDivisionMapper.updateFaAddition(faAddition);
		
	}

	@Override
	public void insertFaAddition(List<FaAddition> faAdditions) {
		faDivisionMapper.insertFaAddition(faAdditions);
		
	}

	@Override
	public void deleteFaDivistionH(String divisionId) {
		faDivisionMapper.deleteFaDivistionH(divisionId);
		
	}

	@Override
	public void deleteFaDivistionL(String divisionId) {
		faDivisionMapper.deleteFaDivistionL(divisionId);
		
	}

	@Override
	public void deleteXcFaTrans(String divisionId) {
		faDivisionMapper.deleteXcFaTrans(divisionId);
	}

	@Override
	public List<FaAddition> findAssetIdByDivisonId(String divisionId) {
		return faDivisionMapper.findAssetIdByDivisonId(divisionId);
	}

	@Override
	public void deleteFaAddition(String assetId) {
		faDivisionMapper.deleteFaAddition(assetId);
		
	}

	@Override
	public int checkSubFaDivision(String divisionId) {
		return faDivisionMapper.checkSubFaDivision(divisionId);
	}

	@Override
	public int checkFaInPeriod(String divisionId) {
		return faDivisionMapper.checkFaInPeriod(divisionId);
	}


}
