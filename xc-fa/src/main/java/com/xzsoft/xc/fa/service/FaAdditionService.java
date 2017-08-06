package com.xzsoft.xc.fa.service;

import java.util.HashMap;

import com.xzsoft.xc.fa.modal.FaAddition;


public interface FaAdditionService {
	
	public FaAddition saveFaAddition(FaAddition faAddition, HashMap<String, Object> extraParams) throws Exception;	
	
}
