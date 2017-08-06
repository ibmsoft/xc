package com.xzsoft.xc.fa.dao;

import java.util.List;

import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaDivisionH;
import com.xzsoft.xc.fa.modal.FaDivisionL;

public interface FaDivisionDao {

	public void insertfaDivision(FaDivisionH faDivisionH);
	
	public void insertFaDivisionL(List<FaDivisionL> faDivisionL);
	
	public void updateFaAddition(FaAddition faAddition);
	
	public void insertFaAddition(List<FaAddition> faAdditions);
	
	public void deleteFaDivistionH(String divisionId);
	
	public void deleteFaDivistionL(String divisionId);
	
	public void deleteXcFaTrans(String divisionId);
	
	public List<FaAddition> findAssetIdByDivisonId(String divisionId);
	
	public void deleteFaAddition(String assetId);

	public int checkSubFaDivision(String divisionId);
	
	public int checkFaInPeriod(String divisionId);
}
