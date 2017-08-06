package com.xzsoft.xc.fa.service;

import java.util.HashMap;

import com.xzsoft.xc.fa.modal.FaDivision;

public interface FaDivisionService {

public	void saveFaDivision(FaDivision faDivision,HashMap<String, Object> extraParams)  throws Exception ;

public void deleteFaDivision(FaDivision faDivision,
		HashMap<String, Object> extraParams) throws Exception;

}
