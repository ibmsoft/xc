package com.xzsoft.xc.fa.service;

import java.util.HashMap;

import com.xzsoft.xc.fa.modal.FaAdj;


public interface FaAdjService {
	
	/**
	 * saveFaAdj:(保存资产调整)
	 *
	 * @param faAdj
	 * @param extraParams
	 * @throws Exception
	 * @author q p
	 */
	public void saveFaAdj(FaAdj faAdj, HashMap<String, Object> extraParams) throws Exception;	
	
	/**
	 * submitFaAdj:(提交资产调整)
	 *
	 * @param adjHid 调整头
	 * @param extraParams
	 * @throws Exception
	 * @author q p
	 */
	public void submitFaAdj(String adjHid, HashMap<String, Object> extraParams) throws Exception;
	
	/**
	 * revokeFaAdj:(撤销资产调整)
	 *
	 * @param adjHid 调整头
	 * @param extraParams
	 * @throws Exception
	 * @author q p
	 */
	public void revokeFaAdj(String adjHid, HashMap<String, Object> extraParams) throws Exception;
}
